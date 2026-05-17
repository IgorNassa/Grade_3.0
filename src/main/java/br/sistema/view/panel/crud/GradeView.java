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

    private final TurmaController turmaController;
    private final DisciplinaController disciplinaController;
    private final AulaController aulaController;
    private final br.sistema.controller.interfaces.TurnoController turnoController;
    private final JComboBox<TurmaDTO> cmbTurma;
    private final JComboBox<br.sistema.controller.dtos.TurnoDTO> cmbTurno;
    private JScrollPane scrollGrade;

    public GradeView() {
        this.turmaController = ServiceRegistry.getInstance().get(TurmaController.class);
        this.disciplinaController = ServiceRegistry.getInstance().get(DisciplinaController.class);
        this.aulaController = ServiceRegistry.getInstance().get(AulaController.class);
        this.turnoController = ServiceRegistry.getInstance().get(br.sistema.controller.interfaces.TurnoController.class);

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

        cmbTurno = new JComboBox<>();
        cmbTurno.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof br.sistema.controller.dtos.TurnoDTO t) setText(t.nomeTurno().name());
                return this;
            }
        });
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

        JButton btnLimpar = AppTheme.dangerButton("Limpar Grade");
        btnLimpar.addActionListener(e -> limparGrade());
        filtros.add(btnLimpar);

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

        br.sistema.controller.dtos.TurnoDTO turnoSel = (br.sistema.controller.dtos.TurnoDTO) cmbTurno.getSelectedItem();
        cmbTurno.removeAllItems();
        for (br.sistema.controller.dtos.TurnoDTO t : turnoController.findAll()) {
            cmbTurno.addItem(t);
        }
        if (turnoSel != null) cmbTurno.setSelectedItem(turnoSel);
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
        br.sistema.controller.dtos.TurnoDTO turnoDTO = (br.sistema.controller.dtos.TurnoDTO) cmbTurno.getSelectedItem();
        String nomeTurno = (turnoDTO != null) ? turnoDTO.nomeTurno().name() : "";

        JLabel lbl = new JLabel("Grade — Turma " + nomeTurma + " | " + nomeTurno);
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
        card.add(montarTabela(turma, turnoDTO), BorderLayout.CENTER);

        return card;
    }

    private JScrollPane montarTabela(TurmaDTO turmaSelecionada, br.sistema.controller.dtos.TurnoDTO turnoDTO) {
        DefaultTableModel model = new DefaultTableModel(DIAS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        if (turnoDTO == null) return new JScrollPane(new JTable(model));

        // --- LÓGICA DE BUSCA REAL NO BANCO ---
        int totalSlots = turnoDTO.aulasTurno();
        String[][] gradeMatrix = new String[totalSlots][6]; // [slot 0-N][dia 1-5]

        if (turmaSelecionada != null) {
            Map<Long, String> discMap = disciplinaController.findAll().stream()
                    .collect(Collectors.toMap(DisciplinaDTO::id, DisciplinaDTO::nome));

            List<AulaDTO> aulas = aulaController.findAll().stream()
                    .filter(a -> a.turmaId().equals(turmaSelecionada.id()))
                    .toList();

            for (AulaDTO aula : aulas) {
                int dia = aula.diaDaSemana().getValue(); 
                int slot = aula.slotHorario(); 
                if (dia >= 1 && dia <= 5 && slot >= 0 && slot < totalSlots) {
                    gradeMatrix[slot][dia] = discMap.getOrDefault(aula.disciplinaId(), "Desconhecida");
                }
            }
        }

        // --- CONSTRUÇÃO VISUAL DA TABELA ---
        java.time.LocalTime horaAtual = turnoDTO.inicioTurno();
        int duracao = turnoDTO.tempoAula();
        int antesIntervalo = turnoDTO.aulasAntesIntervalo();
        java.time.format.DateTimeFormatter timeFmt = java.time.format.DateTimeFormatter.ofPattern("HH:mm");

        for (int s = 0; s < totalSlots; s++) {
            // Adiciona linha de Intervalo se necessário
            if (s == antesIntervalo) {
                Object[] rowInterv = new Object[6];
                rowInterv[0] = "— Intervalo —";
                for (int d = 1; d <= 5; d++) rowInterv[d] = "── Intervalo ──";
                model.addRow(rowInterv);
                // Opcional: pular tempo do intervalo? (Assumindo 20 min)
                horaAtual = horaAtual.plusMinutes(20);
            }

            java.time.LocalTime horaFim = horaAtual.plusMinutes(duracao);
            Object[] row = new Object[6];
            row[0] = horaAtual.format(timeFmt) + "–" + horaFim.format(timeFmt);

            for (int d = 1; d <= 5; d++) {
                String disciplinaDoHorario = gradeMatrix[s][d];
                row[d] = (disciplinaDoHorario != null) ? disciplinaDoHorario : "Vago";
            }
            model.addRow(row);
            horaAtual = horaFim;
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

    private void limparGrade() {
        TurmaDTO turma = (TurmaDTO) cmbTurma.getSelectedItem();
        if (turma == null) return;

        int confirm = JOptionPane.showConfirmDialog(this,
                "Deseja realmente apagar TODA a grade horária da turma \"" + turma.nome() + "\"?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                aulaController.deleteByTurma(turma);
                JOptionPane.showMessageDialog(this, "Grade removida com sucesso!");
                recarregarGrade();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao limpar grade: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void focarTurmaERecarregar(TurmaDTO turma) {
        recarregarListaTurmas();
        cmbTurma.setSelectedItem(turma);
        recarregarGrade();
    }
}