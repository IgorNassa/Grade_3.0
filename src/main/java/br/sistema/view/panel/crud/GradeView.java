package br.sistema.view.panel.crud;

import br.sistema.view.util.AppTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.DayOfWeek;
import java.util.*;

/**
 * Painel de visualização da Grade Gerada.
 * Exibe uma tabela semanal (Segunda–Sexta) por horário.
 */
public class GradeView extends JPanel {

    // Dias da semana
    private static final String[] DIAS = {"Horário", "Segunda", "Terça", "Quarta", "Quinta", "Sexta"};

    // Turnos e slots fictícios
    private static final String[] SLOTS_MATUTINO = {
        "07:00–07:50", "07:50–08:40", "08:40–09:30", "— Intervalo —", "09:50–10:40", "10:40–11:30"
    };

    private final JComboBox<String> cmbTurma;
    private final JComboBox<String> cmbTurno;
    private JPanel painelTabela;

    public GradeView() {
        setLayout(new BorderLayout());
        setBackground(AppTheme.BG_CONTENT);
        setBorder(new EmptyBorder(32, 32, 32, 32));

        cmbTurma = AppTheme.styledCombo(new String[]{"1A", "1B", "2A", "2B", "3A"});
        cmbTurma.setPreferredSize(new Dimension(120, 38));

        cmbTurno = AppTheme.styledCombo(new String[]{"MATUTINO", "VESPERTINO", "NOTURNO"});
        cmbTurno.setPreferredSize(new Dimension(140, 38));

        add(montarTopo(), BorderLayout.NORTH);

        painelTabela = montarGrade();
        JScrollPane scroll = new JScrollPane(painelTabela);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setBackground(AppTheme.BG_CONTENT);
        scroll.getViewport().setBackground(AppTheme.BG_CONTENT);
        add(scroll, BorderLayout.CENTER);
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

        // Filtros
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

    private JPanel montarGrade() {
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AppTheme.BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(AppTheme.BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 16, 16);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(0, 0, 0, 0));

        // Título do card
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 14));
        header.setOpaque(false);
        String turma = (String) cmbTurma.getSelectedItem();
        String turno = (String) cmbTurno.getSelectedItem();
        JLabel lbl = new JLabel("Grade — Turma " + turma + " | " + turno);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(AppTheme.TEXT_PRIMARY);
        header.add(lbl);
        card.add(header);

        card.add(new JSeparator() {{
            setForeground(AppTheme.BORDER_COLOR);
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        }});

        // Tabela grade
        String[] disciplinas = {"Matemática", "Português", "História", "Física", "Química",
                                "Biologia", "Inglês", "Geografia", "Arte", "Ed. Física"};
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
            @Override public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                String val = (String) getValueAt(row, col);
                if (col == 0) {
                    c.setBackground(AppTheme.BG_SIDEBAR);
                    c.setForeground(AppTheme.TEXT_SECONDARY);
                    ((JComponent) c).setFont(new Font("Segoe UI", Font.BOLD, 12));
                } else if (val != null && val.contains("Intervalo")) {
                    c.setBackground(new Color(40, 35, 65));
                    c.setForeground(AppTheme.TEXT_MUTED);
                } else if (isRowSelected(row)) {
                    c.setBackground(AppTheme.BG_TABLE_SEL);
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(row % 2 == 0 ? AppTheme.BG_TABLE_ROW : AppTheme.BG_TABLE_ALT);
                    c.setForeground(AppTheme.TEXT_PRIMARY);
                }
                ((JLabel) c).setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        };

        table.setRowHeight(52);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(1, 1));
        table.setBackground(AppTheme.BG_TABLE_ROW);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JTableHeader th = table.getTableHeader();
        th.setBackground(AppTheme.PURPLE_DARK);
        th.setForeground(Color.WHITE);
        th.setFont(new Font("Segoe UI", Font.BOLD, 13));
        th.setPreferredSize(new Dimension(0, 44));

        table.getColumnModel().getColumn(0).setPreferredWidth(120);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(AppTheme.BG_CARD);
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(scroll);
        return card;
    }

    private void recarregarGrade() {
        Container parent = painelTabela.getParent().getParent(); // JViewport > JScrollPane
        JScrollPane scroll = (JScrollPane) painelTabela.getParent().getParent();
        painelTabela = montarGrade();
        scroll.setViewportView(painelTabela);
        scroll.revalidate();
        scroll.repaint();
    }
}
