package br.sistema.view.panel.crud;

import br.sistema.controller.dtos.AulaDTO;
import br.sistema.controller.dtos.DisciplinaDTO;
import br.sistema.controller.dtos.TurmaDTO;
import br.sistema.controller.interfaces.AulaController;
import br.sistema.controller.interfaces.DisciplinaController;
import br.sistema.controller.interfaces.TurmaController;
import br.sistema.util.ServiceRegistry;
import br.sistema.view.util.AppTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GradeView extends JPanel {

    private static final String[] DIAS = {"Horário", "Segunda", "Terça", "Quarta", "Quinta", "Sexta"};
    private static final String[] SLOTS_MATUTINO = {
            "07:00–07:50", "07:50–08:40", "08:40–09:30", "— Intervalo —", "09:50–10:40", "10:40–11:30"
    };

    private final TurmaController turmaController;
    private final DisciplinaController disciplinaController;
    private final AulaController aulaController;
    private final JComboBox<TurmaDTO> cmbTurma;
    private final JComboBox<String> cmbTurno;
    private JScrollPane scrollGrade;

    public GradeView() {
        this.turmaController = ServiceRegistry.getInstance().get(TurmaController.class);
        this.disciplinaController = ServiceRegistry.getInstance().get(DisciplinaController.class);
        this.aulaController = ServiceRegistry.getInstance().get(AulaController.class);

        setLayout(new BorderLayout(0, 0));
        setBackground(AppTheme.BG_CONTENT);
        setBorder(new EmptyBorder(32, 32, 32, 32));

        cmbTurma = new JComboBox<>();
        cmbTurma.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof TurmaDTO turma) setText(turma.nome());
                return this;
            }
        });
        cmbTurma.setPreferredSize(new Dimension(120, 38));

        cmbTurno = AppTheme.styledCombo(new String[]{"MATUTINO", "VESPERTINO", "NOTURNO"});
        cmbTurno.setPreferredSize(new Dimension(140, 38));

        add(montarTopo(), BorderLayout.NORTH);

        scrollGrade = montarScrollGrade();
        add(scrollGrade, BorderLayout.CENTER);

        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                recarregarListaTurmas();
                recarregarGrade();
            }
        });
    }

    private JPanel montarTopo() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setOpaque(false);
        painel.setBorder(new EmptyBorder(0, 0, 24, 0));

        JLabel titulo = AppTheme.sectionTitle("Visualizar Grade");
        JLabel sub = AppTheme.label("Consulte a grade horária gerada por turma e turno.");
        sub.setBorder(new EmptyBorder(4, 0, 0, 0));

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.add(titulo);
        textos.add(sub);

        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        filtros.setOpaque(false);
        filtros.add(AppTheme.label("Turma:"));
        filtros.add(cmbTurma);
        filtros.add(AppTheme.label("Turno:"));
        filtros.add(cmbTurno);
        JButton btnVisualizar = AppTheme.primaryButton("Visualizar");
        btnVisualizar.addActionListener(e -> recarregarGrade());
        filtros.add(btnVisualizar);

        painel.add(textos, BorderLayout.WEST);
        painel.add(filtros, BorderLayout.EAST);
        return painel;
    }

    private void recarregarListaTurmas() {
        TurmaDTO selecionada = (TurmaDTO) cmbTurma.getSelectedItem();
        cmbTurma.removeAllItems();
        for (TurmaDTO t : turmaController.findAll()) {
            cmbTurma.addItem(t);
        }
        if (selecionada != null) cmbTurma.setSelectedItem(selecionada);
    }

    private JScrollPane montarScrollGrade() {
        JPanel card = montarCard();
        JScrollPane scroll = new JScrollPane(card);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setBackground(AppTheme.BG_CONTENT);
        scroll.getViewport().setBackground(AppTheme.BG_CONTENT);
        scroll.getViewport().setOpaque(false);
        estilizarScrollBar(scroll);
        return scroll;
    }

    private JPanel montarCard() {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AppTheme.BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(AppTheme.BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                g2.dispose();
            }
        };
        card.setOpaque(false);

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 14));
        header.setOpaque(false);

        TurmaDTO turma = (TurmaDTO) cmbTurma.getSelectedItem();
        String nomeTurma = (turma != null) ? turma.nome() : "";
        String turno = (String) cmbTurno.getSelectedItem();

        JLabel lbl = new JLabel("Grade — Turma " + nomeTurma + " | " + turno);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(AppTheme.TEXT_PRIMARY);
        header.add(lbl);

        JSeparator sep = new JSeparator();
        sep.setForeground(AppTheme.BORDER_COLOR);
        sep.setBackground(AppTheme.BORDER_COLOR);

        JPanel topCard = new JPanel(new BorderLayout());
        topCard.setOpaque(false);
        topCard.add(header, BorderLayout.CENTER);
        topCard.add(sep, BorderLayout.SOUTH);

        card.add(topCard, BorderLayout.NORTH);
        card.add(montarTabela(turma), BorderLayout.CENTER);

        return card;
    }

    private JScrollPane montarTabela(TurmaDTO turmaSelecionada) {
        DefaultTableModel model = new DefaultTableModel(DIAS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        // --- LÓGICA DE BUSCA REAL NO BANCO ---
        String[][] gradeMatrix = new String[5][6]; // [slot 0-4][dia 1-5]

        if (turmaSelecionada != null) {
            // Cria um dicionário Rápido de ID da Disciplina -> Nome da Disciplina
            Map<Long, String> discMap = disciplinaController.findAll().stream()
                    .collect(Collectors.toMap(DisciplinaDTO::id, DisciplinaDTO::nome));

            // Filtra as aulas reais da turma no banco
            List<AulaDTO> aulas = aulaController.findAll().stream()
                    .filter(a -> a.turmaId().equals(turmaSelecionada.id()))
                    .toList();

            // Preenche a matriz nas posições exatas
            for (AulaDTO aula : aulas) {
                int dia = aula.diaDaSemana().getValue(); // 1 = Seg, 5 = Sex
                int slot = aula.slotHorario(); // 0 a 4
                if (dia >= 1 && dia <= 5 && slot >= 0 && slot <= 4) {
                    gradeMatrix[slot][dia] = discMap.getOrDefault(aula.disciplinaId(), "Desconhecida");
                }
            }
        }

        // --- CONSTRUÇÃO VISUAL DA TABELA ---
        int slotDados = 0; // Ponteiro para os slots (0 a 4) ignorando a linha do Intervalo

        for (String slotName : SLOTS_MATUTINO) {
            Object[] row = new Object[6];
            row[0] = slotName;

            if (slotName.contains("Intervalo")) {
                for (int d = 1; d <= 5; d++) row[d] = "── Intervalo ──";
            } else {
                for (int d = 1; d <= 5; d++) {
                    String disciplinaDoHorario = gradeMatrix[slotDados][d];
                    row[d] = (disciplinaDoHorario != null) ? disciplinaDoHorario : "Vago";
                }
                slotDados++;
            }
            model.addRow(row);
        }

        JTable table = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                String val = String.valueOf(getValueAt(row, col));

                if (col == 0) {
                    c.setBackground(AppTheme.BG_SIDEBAR);
                    c.setForeground(AppTheme.TEXT_SECONDARY);
                    if (c instanceof JComponent jc) jc.setFont(new Font("Segoe UI", Font.BOLD, 12));
                } else if (val.contains("Intervalo")) {
                    c.setBackground(new Color(40, 35, 65));
                    c.setForeground(AppTheme.TEXT_MUTED);
                } else if (val.equals("Vago")) {
                    c.setBackground(AppTheme.BG_TABLE_ROW); // Fundo normal, mas texto mais apagado
                    c.setForeground(new Color(120, 120, 140));
                } else if (isRowSelected(row)) {
                    c.setBackground(AppTheme.BG_TABLE_SEL);
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(row % 2 == 0 ? AppTheme.BG_TABLE_ROW : AppTheme.BG_TABLE_ALT);
                    c.setForeground(AppTheme.TEXT_PRIMARY);
                }
                if (c instanceof JLabel jl) jl.setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        };

        table.setRowHeight(52);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(1, 1));
        table.setBackground(AppTheme.BG_TABLE_ROW);
        table.setForeground(AppTheme.TEXT_PRIMARY);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setFillsViewportHeight(true);
        table.setSelectionBackground(AppTheme.BG_TABLE_SEL);

        JTableHeader th = table.getTableHeader();
        th.setBackground(AppTheme.PURPLE_DARK);
        th.setForeground(Color.WHITE);
        th.setFont(new Font("Segoe UI", Font.BOLD, 13));
        th.setPreferredSize(new Dimension(0, 44));
        th.setReorderingAllowed(false);
        th.setBorder(BorderFactory.createEmptyBorder());

        table.getColumnModel().getColumn(0).setPreferredWidth(120);
        table.getColumnModel().getColumn(0).setMaxWidth(140);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(AppTheme.BG_CARD);
        scroll.setBackground(AppTheme.BG_CARD);
        estilizarScrollBar(scroll);

        return scroll;
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

    private void recarregarGrade() {
        Container parent = scrollGrade.getParent();
        parent.remove(scrollGrade);
        scrollGrade = montarScrollGrade();
        parent.add(scrollGrade, BorderLayout.CENTER);
        parent.revalidate();
        parent.repaint();
    }

    public void focarTurmaERecarregar(TurmaDTO turma) {
        recarregarListaTurmas();
        cmbTurma.setSelectedItem(turma);
        recarregarGrade();
    }
}