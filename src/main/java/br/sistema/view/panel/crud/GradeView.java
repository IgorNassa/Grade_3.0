package br.sistema.view.panel.crud;

import br.sistema.view.util.AppTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.util.Random;

/**
 * Painel de visualização da Grade Gerada.
 * Exibe uma tabela semanal (Segunda–Sexta) por horário.
 * CORRIGIDO: tabela alinhada à esquerda, sem deslocamento.
 */
public class GradeView extends JPanel {

    private static final String[] DIAS = {"Horário", "Segunda", "Terça", "Quarta", "Quinta", "Sexta"};

    private static final String[] SLOTS_MATUTINO = {
            "07:00–07:50", "07:50–08:40", "08:40–09:30", "— Intervalo —", "09:50–10:40", "10:40–11:30"
    };

    private final JComboBox<String> cmbTurma;
    private final JComboBox<String> cmbTurno;
    private JScrollPane scrollGrade;

    public GradeView() {
        setLayout(new BorderLayout(0, 0));
        setBackground(AppTheme.BG_CONTENT);
        setBorder(new EmptyBorder(32, 32, 32, 32));

        cmbTurma = AppTheme.styledCombo(new String[]{"1A", "1B", "2A", "2B", "3A"});
        cmbTurma.setPreferredSize(new Dimension(120, 38));

        cmbTurno = AppTheme.styledCombo(new String[]{"MATUTINO", "VESPERTINO", "NOTURNO"});
        cmbTurno.setPreferredSize(new Dimension(140, 38));

        add(montarTopo(), BorderLayout.NORTH);

        scrollGrade = montarScrollGrade();
        add(scrollGrade, BorderLayout.CENTER);
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
        JButton btnFiltrar = AppTheme.primaryButton("Visualizar");
        btnFiltrar.addActionListener(e -> recarregarGrade());
        filtros.add(btnFiltrar);

        painel.add(textos, BorderLayout.WEST);
        painel.add(filtros, BorderLayout.EAST);
        return painel;
    }

    /**
     * Cria um JScrollPane contendo o card da grade.
     * O card usa BorderLayout para que a tabela ocupe toda a área disponível.
     */
    private JScrollPane montarScrollGrade() {
        JPanel card = montarCard();
        JScrollPane scroll = new JScrollPane(card);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setBackground(AppTheme.BG_CONTENT);
        scroll.getViewport().setBackground(AppTheme.BG_CONTENT);
        // Garante que o card se expanda até preencher o viewport
        scroll.getViewport().setOpaque(false);
        estilizarScrollBar(scroll);
        return scroll;
    }

    /**
     * Card com borda arredondada + tabela.
     * Layout corrigido: BorderLayout garante que o JScrollPane
     * da tabela preencha todo o espaço sem deslocamento lateral.
     */
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

        // Cabeçalho do card
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 14));
        header.setOpaque(false);
        String turma = (String) cmbTurma.getSelectedItem();
        String turno = (String) cmbTurno.getSelectedItem();
        JLabel lbl = new JLabel("Grade — Turma " + turma + " | " + turno);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(AppTheme.TEXT_PRIMARY);
        header.add(lbl);

        JSeparator sep = new JSeparator();
        sep.setForeground(AppTheme.BORDER_COLOR);
        sep.setBackground(AppTheme.BORDER_COLOR);

        JPanel topCard = new JPanel(new BorderLayout());
        topCard.setOpaque(false);
        topCard.add(header, BorderLayout.CENTER);
        topCard.add(sep,    BorderLayout.SOUTH);

        card.add(topCard, BorderLayout.NORTH);
        card.add(montarTabela(), BorderLayout.CENTER);

        return card;
    }

    private JScrollPane montarTabela() {
        String[] disciplinas = {
                "Matemática", "Português", "História", "Física", "Química",
                "Biologia", "Inglês", "Geografia", "Arte", "Ed. Física"
        };
        Random rnd = new Random(42);

        DefaultTableModel model = new DefaultTableModel(DIAS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        for (String slot : SLOTS_MATUTINO) {
            Object[] row = new Object[6];
            row[0] = slot;
            if (slot.contains("Intervalo")) {
                for (int d = 1; d <= 5; d++) row[d] = "── Intervalo ──";
            } else {
                for (int d = 1; d <= 5; d++) row[d] = disciplinas[rnd.nextInt(disciplinas.length)];
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
                    if (c instanceof JComponent jc)
                        jc.setFont(new Font("Segoe UI", Font.BOLD, 12));
                } else if (val.contains("Intervalo")) {
                    c.setBackground(new Color(40, 35, 65));
                    c.setForeground(AppTheme.TEXT_MUTED);
                } else if (isRowSelected(row)) {
                    c.setBackground(AppTheme.BG_TABLE_SEL);
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(row % 2 == 0 ? AppTheme.BG_TABLE_ROW : AppTheme.BG_TABLE_ALT);
                    c.setForeground(AppTheme.TEXT_PRIMARY);
                }
                if (c instanceof JLabel jl)
                    jl.setHorizontalAlignment(JLabel.CENTER);
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
}