package br.sistema.view.frame;

import br.sistema.view.util.AppTheme;
import br.sistema.view.util.NavigationController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedHashMap;
import java.util.Map;

public class GeradorView extends JFrame {

    private static final int    LARGURA      = 820;
    private static final int    ALTURA       = 680;
    private static final int    RAIO         = 16;
    private static final Font   FONT_TITLE   = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font   FONT_SUB     = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font   FONT_LABEL   = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font   FONT_BOLD14  = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font   FONT_BOLD13  = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font   FONT_PLAIN13 = new Font("Segoe UI", Font.PLAIN, 13);

    private JComboBox<String>  cmbTurma;
    private JComboBox<String>  cmbTurno;
    private DefaultTableModel  tabelaModel;
    private JTable             tabela;
    private JProgressBar       progressBar;
    private JLabel             lblStatus;
    private JButton            btnGerar;
    private JButton            btnLimpar;

    private static final String[] DISCIPLINAS = {
            "Matemática", "Português",  "História",   "Geografia",
            "Física",     "Química",    "Biologia",   "Inglês",
            "Arte",       "Ed. Física", "Filosofia",  "Sociologia"
    };

    private int dragX, dragY;

    // ─────────────────────────────────────────────────────────────────────────
    // Construtor — setUndecorated OBRIGATORIAMENTE antes do invokeLater
    // ─────────────────────────────────────────────────────────────────────────
    public GeradorView() {
        setUndecorated(true);                      // deve vir ANTES de ser displayable
        setBackground(new Color(0, 0, 0, 0));      // suporte a cantos arredondados
        setResizable(false);

        SwingUtilities.invokeLater(this::inicializar);
    }

    public static void exibir() {
        SwingUtilities.invokeLater(() -> new GeradorView());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GeradorView::exibir);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Inicialização
    // ─────────────────────────────────────────────────────────────────────────
    private void inicializar() {
        configurarFrame();
        JPanel root = construirRaiz();
        setContentPane(root);
        habilitarArrasto(root);
        configurarListeners();
        setVisible(true);
    }

    private void configurarFrame() {
        setTitle("SGDG — Gerador de Grade");
        setSize(LARGURA, ALTURA);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        // setUndecorated, setBackground e setResizable já foram chamados no construtor

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                fecharComConfirmacao();
            }
        });
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Raiz com cantos arredondados
    // ─────────────────────────────────────────────────────────────────────────
    private JPanel construirRaiz() {
        JPanel raiz = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AppTheme.BG_MAIN);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), RAIO * 2, RAIO * 2);
                g2.setColor(AppTheme.BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1,
                        RAIO * 2, RAIO * 2);
                g2.dispose();
            }
        };
        raiz.setOpaque(false);

        raiz.add(construirTopBar(), BorderLayout.NORTH);
        raiz.add(construirCorpo(),  BorderLayout.CENTER);
        raiz.add(construirRodape(), BorderLayout.SOUTH);

        return raiz;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TOP BAR — botão home + título + controles de janela
    // ─────────────────────────────────────────────────────────────────────────
    private JPanel construirTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);
        bar.setBorder(new EmptyBorder(18, 24, 0, 18));
        bar.setPreferredSize(new Dimension(0, 64));

        // ── Lado esquerdo ─────────────────────────────────────────────────────
        JPanel esq = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        esq.setOpaque(false);

        // Botão ← Dashboard
        JButton btnHome = new JButton("← Dashboard") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover()
                        ? AppTheme.PURPLE_SUBTLE : AppTheme.BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(getModel().isRollover()
                        ? AppTheme.PURPLE : AppTheme.BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnHome.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnHome.setForeground(AppTheme.TEXT_SECONDARY);
        btnHome.setFocusPainted(false);
        btnHome.setBorderPainted(false);
        btnHome.setContentAreaFilled(false);
        btnHome.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHome.setPreferredSize(new Dimension(120, 30));
        btnHome.addActionListener(e -> {
            NavigationController.getInstance().setGeradorAtivo(false);
            dispose();
            NavigationController.getInstance().irParaDashboard();
        });

        // Separador visual
        JLabel sep = new JLabel("|");
        sep.setForeground(AppTheme.BORDER_COLOR);
        sep.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        JLabel icone = new JLabel("⚡");
        icone.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));

        JLabel titulo = new JLabel("Gerador de Grade Escolar");
        titulo.setFont(FONT_TITLE);
        titulo.setForeground(AppTheme.TEXT_PRIMARY);

        esq.add(btnHome);
        esq.add(Box.createHorizontalStrut(4));
        esq.add(sep);
        esq.add(Box.createHorizontalStrut(4));
        esq.add(icone);
        esq.add(titulo);

        // ── Lado direito — minimizar + fechar ─────────────────────────────────
        JPanel dir = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        dir.setOpaque(false);

        JButton btnMin    = criarBotaoJanela(false);
        JButton btnFechar = criarBotaoJanela(true);

        btnMin.addActionListener(e -> setState(JFrame.ICONIFIED));
        btnFechar.addActionListener(e -> fecharComConfirmacao());

        dir.add(btnMin);
        dir.add(btnFechar);

        bar.add(esq, BorderLayout.WEST);
        bar.add(dir, BorderLayout.EAST);
        return bar;
    }

    private JButton criarBotaoJanela(boolean fechar) {
        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                Color cor = getModel().isRollover()
                        ? (fechar ? new Color(255, 80, 80) : AppTheme.TEXT_PRIMARY)
                        : AppTheme.TEXT_MUTED;
                g2.setColor(cor);
                g2.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND,
                        BasicStroke.JOIN_ROUND));
                int cx = getWidth() / 2, cy = getHeight() / 2, r = 6;
                if (fechar) {
                    g2.drawLine(cx - r, cy - r, cx + r, cy + r);
                    g2.drawLine(cx + r, cy - r, cx - r, cy + r);
                } else {
                    g2.drawLine(cx - r, cy, cx + r, cy);
                }
                g2.dispose();
            }
        };
        btn.setPreferredSize(new Dimension(32, 28));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // CORPO
    // ─────────────────────────────────────────────────────────────────────────
    private JPanel construirCorpo() {
        JPanel corpo = new JPanel(new BorderLayout(0, 16));
        corpo.setOpaque(false);
        corpo.setBorder(new EmptyBorder(12, 24, 0, 24));

        corpo.add(construirCardSelecao(),       BorderLayout.NORTH);
        corpo.add(construirCardCargaHoraria(),  BorderLayout.CENTER);

        return corpo;
    }

    private JPanel construirCardSelecao() {
        JPanel card = AppTheme.cardPanel(new GridBagLayout());
        card.setBorder(new EmptyBorder(16, 20, 16, 20));
        card.setPreferredSize(new Dimension(0, 90));

        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.WEST;
        gc.insets  = new Insets(0, 0, 0, 20);
        gc.gridy   = 0;

        JLabel sub = new JLabel("Configure os parâmetros antes de gerar a grade.");
        sub.setFont(FONT_SUB);
        sub.setForeground(AppTheme.TEXT_SECONDARY);
        gc.gridx = 0; gc.gridwidth = 6;
        card.add(sub, gc);

        gc.gridy = 1; gc.gridwidth = 1;
        gc.insets = new Insets(12, 0, 0, 8);

        gc.gridx = 0;
        card.add(rotulo("Turma:"), gc);

        gc.gridx = 1;
        cmbTurma = AppTheme.styledCombo(new String[]{"1A", "1B", "2A", "2B", "3A", "3B"});
        cmbTurma.setPreferredSize(new Dimension(110, 36));
        card.add(cmbTurma, gc);

        gc.gridx = 2; gc.insets = new Insets(12, 16, 0, 8);
        card.add(rotulo("Turno:"), gc);

        gc.gridx = 3; gc.insets = new Insets(12, 0, 0, 8);
        cmbTurno = AppTheme.styledCombo(new String[]{"MATUTINO", "VESPERTINO", "NOTURNO"});
        cmbTurno.setPreferredSize(new Dimension(140, 36));
        card.add(cmbTurno, gc);

        // Spacer expansível
        gc.gridx = 4; gc.weightx = 1.0; gc.fill = GridBagConstraints.HORIZONTAL;
        card.add(Box.createHorizontalGlue(), gc);

        return card;
    }

    private JPanel construirCardCargaHoraria() {
        JPanel card = AppTheme.cardPanel(new BorderLayout());

        // Cabeçalho do card
        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setOpaque(false);
        cabecalho.setBorder(new EmptyBorder(16, 20, 14, 20));

        JLabel tit = new JLabel("Carga Horária Semanal por Disciplina");
        tit.setFont(FONT_BOLD14);
        tit.setForeground(AppTheme.TEXT_PRIMARY);

        JLabel hint = new JLabel("Clique em uma célula da coluna \"Aulas\" para editar.");
        hint.setFont(FONT_LABEL);
        hint.setForeground(AppTheme.TEXT_MUTED);

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.add(tit);
        textos.add(Box.createVerticalStrut(3));
        textos.add(hint);
        cabecalho.add(textos, BorderLayout.WEST);

        btnLimpar = AppTheme.secondaryButton("Limpar");
        btnLimpar.setPreferredSize(new Dimension(90, 34));
        JPanel acaoCab = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        acaoCab.setOpaque(false);
        acaoCab.add(btnLimpar);
        cabecalho.add(acaoCab, BorderLayout.EAST);

        // Separador
        JSeparator separador = new JSeparator();
        separador.setForeground(AppTheme.BORDER_COLOR);
        separador.setBackground(AppTheme.BORDER_COLOR);

        JPanel topo = new JPanel(new BorderLayout());
        topo.setOpaque(false);
        topo.add(cabecalho,  BorderLayout.CENTER);
        topo.add(separador,  BorderLayout.SOUTH);

        card.add(topo,             BorderLayout.NORTH);
        card.add(construirTabela(), BorderLayout.CENTER);

        return card;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TABELA
    // ─────────────────────────────────────────────────────────────────────────
    private JScrollPane construirTabela() {
        tabelaModel = new DefaultTableModel(
                new String[]{"#", "Disciplina", "Aulas/Semana", "Distribuição"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return col == 2; }
            @Override public Class<?> getColumnClass(int col) {
                return col == 0 ? Integer.class : String.class;
            }
        };

        for (int i = 0; i < DISCIPLINAS.length; i++) {
            tabelaModel.addRow(new Object[]{
                    i + 1, DISCIPLINAS[i], "2", gerarBarraDistribuicao(2)
            });
        }

        tabela = new JTable(tabelaModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                boolean sel = isRowSelected(row);
                c.setBackground(sel ? AppTheme.BG_TABLE_SEL
                        : row % 2 == 0 ? AppTheme.BG_TABLE_ROW : AppTheme.BG_TABLE_ALT);
                c.setForeground(sel ? Color.WHITE : AppTheme.TEXT_PRIMARY);
                if (c instanceof JLabel jl) {
                    jl.setBorder(new EmptyBorder(0, 12, 0, 12));
                    if (col == 0) jl.setHorizontalAlignment(JLabel.CENTER);
                    if (col == 2 && !sel) {
                        c.setBackground(AppTheme.BG_FIELD);
                        c.setForeground(AppTheme.PURPLE_HOVER);
                        jl.setHorizontalAlignment(JLabel.CENTER);
                    }
                }
                return c;
            }
        };

        estilizarTabela();

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(AppTheme.BG_CARD);
        scroll.setBackground(AppTheme.BG_CARD);
        estilizarScrollBar(scroll);

        return scroll;
    }

    private void estilizarTabela() {
        tabela.setRowHeight(44);
        tabela.setShowGrid(false);
        tabela.setIntercellSpacing(new Dimension(0, 1));
        tabela.setBackground(AppTheme.BG_TABLE_ROW);
        tabela.setForeground(AppTheme.TEXT_PRIMARY);
        tabela.setFont(FONT_PLAIN13);
        tabela.setFillsViewportHeight(true);
        tabela.setSelectionBackground(AppTheme.BG_TABLE_SEL);
        tabela.putClientProperty("terminateEditOnFocusLost", true);

        JTableHeader header = tabela.getTableHeader();
        header.setReorderingAllowed(false);
        header.setBackground(AppTheme.PURPLE_DARK);
        header.setForeground(Color.WHITE);
        header.setFont(FONT_BOLD13);
        header.setPreferredSize(new Dimension(0, 40));
        header.setBorder(BorderFactory.createEmptyBorder());

        tabela.getColumnModel().getColumn(0).setPreferredWidth(40);
        tabela.getColumnModel().getColumn(0).setMaxWidth(50);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(220);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(100);
        tabela.getColumnModel().getColumn(2).setMaxWidth(120);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(300);
    }

    private void estilizarScrollBar(JScrollPane scroll) {
        scroll.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override protected void configureScrollBarColors() {
                thumbColor = AppTheme.PURPLE_SUBTLE;
                trackColor = AppTheme.BG_CARD;
            }
            @Override protected JButton createDecreaseButton(int o) { return btnInvisivel(); }
            @Override protected JButton createIncreaseButton(int o) { return btnInvisivel(); }
            private JButton btnInvisivel() {
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(0, 0));
                return b;
            }
        });
    }

    // ─────────────────────────────────────────────────────────────────────────
    // RODAPÉ
    // ─────────────────────────────────────────────────────────────────────────
    private JPanel construirRodape() {
        JPanel rodape = new JPanel(new BorderLayout(0, 8));
        rodape.setOpaque(false);
        rodape.setBorder(new EmptyBorder(12, 24, 20, 24));

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(false);
        progressBar.setBackground(AppTheme.BG_CARD);
        progressBar.setForeground(AppTheme.PURPLE);
        progressBar.setBorderPainted(false);
        progressBar.setPreferredSize(new Dimension(0, 6));
        progressBar.setVisible(false);

        lblStatus = new JLabel(" ");
        lblStatus.setFont(FONT_LABEL);
        lblStatus.setForeground(AppTheme.TEXT_MUTED);

        JPanel progressoPanel = new JPanel(new BorderLayout(0, 4));
        progressoPanel.setOpaque(false);
        progressoPanel.add(progressBar, BorderLayout.NORTH);
        progressoPanel.add(lblStatus,   BorderLayout.CENTER);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        botoes.setOpaque(false);

        JButton btnCancelar = AppTheme.secondaryButton("Fechar");
        btnCancelar.setPreferredSize(new Dimension(100, 40));
        btnCancelar.addActionListener(e -> fecharComConfirmacao());

        btnGerar = AppTheme.primaryButton("⚡ Gerar Grade");
        btnGerar.setPreferredSize(new Dimension(150, 40));
        btnGerar.setFont(FONT_BOLD14);

        botoes.add(btnCancelar);
        botoes.add(btnGerar);

        rodape.add(progressoPanel, BorderLayout.CENTER);
        rodape.add(botoes,         BorderLayout.SOUTH);

        return rodape;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ARRASTAR JANELA
    // ─────────────────────────────────────────────────────────────────────────
    private void habilitarArrasto(JPanel raiz) {
        MouseAdapter drag = new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                dragX = e.getX();
                dragY = e.getY();
            }
            @Override public void mouseDragged(MouseEvent e) {
                setLocation(e.getXOnScreen() - dragX, e.getYOnScreen() - dragY);
            }
        };
        raiz.addMouseListener(drag);
        raiz.addMouseMotionListener(drag);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // LISTENERS
    // ─────────────────────────────────────────────────────────────────────────
    private void configurarListeners() {
        // Atualiza barra de distribuição ao editar aulas
        tabelaModel.addTableModelListener(e -> {
            if (e.getColumn() == 2) {
                int row = e.getFirstRow();
                String val = String.valueOf(tabelaModel.getValueAt(row, 2));
                try {
                    int qtd = Integer.parseInt(val.trim());
                    tabelaModel.setValueAt(gerarBarraDistribuicao(qtd), row, 3);
                } catch (NumberFormatException ex) {
                    tabelaModel.setValueAt("—", row, 3);
                }
            }
        });

        btnLimpar.addActionListener(e -> limparCarga());
        btnGerar.addActionListener(e -> executarGeracao());

        cmbTurno.addActionListener(e -> {
            lblStatus.setText("Turno: " + cmbTurno.getSelectedItem() + " — pronto para gerar.");
            lblStatus.setForeground(AppTheme.TEXT_SECONDARY);
        });
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GERAÇÃO COM SwingWorker
    // ─────────────────────────────────────────────────────────────────────────
    private void executarGeracao() {
        Map<String, Integer> carga = new LinkedHashMap<>();
        int totalAulas = 0;

        for (int i = 0; i < tabelaModel.getRowCount(); i++) {
            String disciplina = (String) tabelaModel.getValueAt(i, 1);
            String rawVal     = String.valueOf(tabelaModel.getValueAt(i, 2)).trim();
            int qtd;
            try {
                qtd = Integer.parseInt(rawVal);
                if (qtd < 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                mostrarErro("Valor inválido em \"" + disciplina
                        + "\".\nUse apenas números inteiros ≥ 0.");
                return;
            }
            if (qtd > 0) carga.put(disciplina, qtd);
            totalAulas += qtd;
        }

        if (totalAulas == 0) {
            mostrarAviso("Defina ao menos uma aula para alguma disciplina.");
            return;
        }

        String turma = (String) cmbTurma.getSelectedItem();
        String turno = (String) cmbTurno.getSelectedItem();
        final int total = totalAulas;

        NavigationController.getInstance().setGeradorAtivo(true);

        btnGerar.setEnabled(false);
        btnLimpar.setEnabled(false);
        progressBar.setVisible(true);
        progressBar.setValue(0);
        lblStatus.setText("Iniciando geração para " + turma + " — " + turno + "…");
        lblStatus.setForeground(AppTheme.TEXT_SECONDARY);

        SwingWorker<Void, Integer> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                for (int i = 1; i <= 100; i++) {
                    Thread.sleep(18);
                    publish(i);
                }
                return null;
            }

            @Override
            protected void process(java.util.List<Integer> chunks) {
                int v = chunks.get(chunks.size() - 1);
                progressBar.setValue(v);
                if      (v < 40) lblStatus.setText("Analisando restrições de professores…");
                else if (v < 70) lblStatus.setText("Distribuindo aulas na grade semanal…");
                else if (v < 90) lblStatus.setText("Verificando conflitos de horário…");
                else             lblStatus.setText("Finalizando e persistindo grade…");
            }

            @Override
            protected void done() {
                SwingUtilities.invokeLater(() -> {
                    NavigationController.getInstance().setGeradorAtivo(false);
                    progressBar.setValue(100);
                    lblStatus.setText("✅ Grade gerada com sucesso!");
                    lblStatus.setForeground(AppTheme.SUCCESS);
                    btnGerar.setEnabled(true);
                    btnLimpar.setEnabled(true);
                    exibirResultado(turma, turno, total, carga);
                });
            }
        };
        worker.execute();
    }

    private void exibirResultado(String turma, String turno,
                                 int total, Map<String, Integer> carga) {
        JPanel painel = new JPanel(new BorderLayout(0, 14));
        painel.setBackground(AppTheme.BG_CARD);
        painel.setBorder(new EmptyBorder(20, 24, 20, 24));

        JLabel tit = new JLabel("✅ Grade Gerada com Sucesso!");
        tit.setFont(FONT_BOLD14);
        tit.setForeground(AppTheme.SUCCESS);
        painel.add(tit, BorderLayout.NORTH);

        StringBuilder sb = new StringBuilder();
        sb.append("<html><body style='font-family:Segoe UI;font-size:12px;color:#E6E4F2;'>");
        sb.append("<b>Turma:</b> ").append(turma)
                .append(" &nbsp;|&nbsp; <b>Turno:</b> ").append(turno)
                .append("<br><br><b>Total de aulas semanais:</b> ").append(total)
                .append("<br><br><b>Distribuição por disciplina:</b><br>");
        carga.forEach((d, q) ->
                sb.append("&nbsp;&nbsp;• ").append(d)
                        .append(": <b>").append(q).append("</b> aula(s)<br>")
        );
        sb.append("</body></html>");

        JLabel resumo = new JLabel(sb.toString());
        painel.add(resumo, BorderLayout.CENTER);

        UIManager.put("OptionPane.background",        AppTheme.BG_CARD);
        UIManager.put("Panel.background",             AppTheme.BG_CARD);
        UIManager.put("OptionPane.messageForeground", AppTheme.TEXT_PRIMARY);

        JOptionPane.showMessageDialog(this, painel,
                "Grade Gerada — " + turma, JOptionPane.PLAIN_MESSAGE);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // FECHAMENTO COM CONFIRMAÇÃO
    // ─────────────────────────────────────────────────────────────────────────
    private void fecharComConfirmacao() {
        if (NavigationController.getInstance().confirmarFechamento()) {
            NavigationController.getInstance().setGeradorAtivo(false);
            dispose();
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // UTILITÁRIOS
    // ─────────────────────────────────────────────────────────────────────────
    private void limparCarga() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Limpar todos os valores de carga horária?",
                "Confirmar", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            for (int i = 0; i < tabelaModel.getRowCount(); i++) {
                tabelaModel.setValueAt("0", i, 2);
                tabelaModel.setValueAt(gerarBarraDistribuicao(0), i, 3);
            }
            lblStatus.setText("Carga limpa. Defina os valores e gere a grade.");
            lblStatus.setForeground(AppTheme.TEXT_MUTED);
            progressBar.setValue(0);
            progressBar.setVisible(false);
        }
    }

    private String gerarBarraDistribuicao(int aulas) {
        int max = 10, barras = Math.min(aulas, max);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < barras; i++)   sb.append("■");
        for (int i = barras; i < max; i++) sb.append("░");
        return sb.append("  (").append(aulas).append("/sem)").toString();
    }

    private JLabel rotulo(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(FONT_LABEL);
        l.setForeground(AppTheme.TEXT_SECONDARY);
        return l;
    }

    private void mostrarErro(String msg) {
        JOptionPane.showMessageDialog(this, msg,
                "Erro de Validação", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarAviso(String msg) {
        JOptionPane.showMessageDialog(this, msg,
                "Atenção", JOptionPane.WARNING_MESSAGE);
    }
}