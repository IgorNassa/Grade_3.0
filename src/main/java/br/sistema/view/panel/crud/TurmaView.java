package br.sistema.view.panel.crud;

import br.sistema.view.util.AppTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Painel CRUD de Turmas — tema dark.
 * Turma: nome + tipo (Ensino Médio / Fundamental).
 */
public class TurmaView extends JPanel {

    private final DefaultTableModel tableModel;
    private final JTable tabela;

    private JTextField txtNome;
    private JComboBox<String> cmbTipo;

    private int linhaSelecionada = -1;

    public TurmaView() {
        setLayout(new BorderLayout());
        setBackground(AppTheme.BG_CONTENT);
        setBorder(new EmptyBorder(32, 32, 32, 32));

        tableModel = new DefaultTableModel(new String[]{"#", "Nome da Turma", "Tipo"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = criarTabela();

        add(montarTopo(), BorderLayout.NORTH);
        add(montarCorpo(), BorderLayout.CENTER);
        popularDadosFicticios();
    }

    private JPanel montarTopo() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(0, 0, 24, 0));

        JLabel titulo = AppTheme.sectionTitle("Turmas");
        JLabel sub = AppTheme.label("Cadastre e gerencie as turmas do Ensino Médio e Fundamental.");
        sub.setBorder(new EmptyBorder(4, 0, 0, 0));

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.add(titulo);
        textos.add(sub);

        p.add(textos, BorderLayout.WEST);
        return p;
    }

    private JPanel montarCorpo() {
        JPanel corpo = new JPanel(new BorderLayout(0, 20));
        corpo.setOpaque(false);
        corpo.add(montarFormulario(), BorderLayout.NORTH);
        corpo.add(montarTabela(), BorderLayout.CENTER);
        return corpo;
    }

    private JPanel montarFormulario() {
        JPanel card = AppTheme.cardPanel(new GridBagLayout());
        card.setBorder(new EmptyBorder(20, 24, 20, 24));

        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(0, 0, 8, 14);

        // Row 0 — Labels
        gc.gridy = 0; gc.gridx = 0; gc.weightx = 0;
        card.add(AppTheme.label("Nome da Turma"), gc);
        gc.gridx = 1;
        card.add(AppTheme.label("Tipo de Ensino"), gc);

        // Row 1 — Fields + Buttons
        gc.gridy = 1; gc.gridx = 0; gc.weightx = 0.5; gc.insets = new Insets(0, 0, 0, 14);
        txtNome = AppTheme.styledField("Ex: 1A, 2B, 3C", 200);
        card.add(txtNome, gc);

        gc.gridx = 1; gc.weightx = 0;
        cmbTipo = AppTheme.styledCombo(new String[]{"Ensino Médio", "Ensino Fundamental"});
        card.add(cmbTipo, gc);

        gc.gridx = 2;
        JButton btnSalvar = AppTheme.primaryButton("Salvar");
        card.add(btnSalvar, gc);

        gc.gridx = 3;
        JButton btnLimpar = AppTheme.secondaryButton("Limpar");
        card.add(btnLimpar, gc);

        gc.gridx = 4;
        JButton btnExcluir = AppTheme.dangerButton("Excluir");
        card.add(btnExcluir, gc);

        btnSalvar.addActionListener(e -> salvar());
        btnLimpar.addActionListener(e -> limpar());
        btnExcluir.addActionListener(e -> excluir());
        txtNome.addActionListener(e -> salvar());

        return card;
    }

    private JPanel montarTabela() {
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

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 14));
        header.setOpaque(false);
        JLabel titulo = new JLabel("Lista de Turmas");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titulo.setForeground(AppTheme.TEXT_PRIMARY);
        header.add(titulo);
        card.add(header);

        card.add(new JSeparator() {{
            setForeground(AppTheme.BORDER_COLOR);
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        }});

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(AppTheme.BG_CARD);
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(scroll);
        return card;
    }

    private JTable criarTabela() {
        JTable t = new JTable(tableModel) {
            @Override public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (isRowSelected(row)) {
                    c.setBackground(AppTheme.BG_TABLE_SEL);
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(row % 2 == 0 ? AppTheme.BG_TABLE_ROW : AppTheme.BG_TABLE_ALT);
                    c.setForeground(AppTheme.TEXT_PRIMARY);
                }
                return c;
            }
        };
        t.setRowHeight(42);
        t.setShowGrid(false);
        t.setIntercellSpacing(new Dimension(0, 1));
        t.setBackground(AppTheme.BG_TABLE_ROW);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JTableHeader h = t.getTableHeader();
        h.setBackground(AppTheme.BG_CARD);
        h.setForeground(AppTheme.TEXT_SECONDARY);
        h.setFont(new Font("Segoe UI", Font.BOLD, 12));
        h.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.BORDER_COLOR));
        h.setPreferredSize(new Dimension(0, 40));

        t.getColumnModel().getColumn(0).setPreferredWidth(50);
        t.getColumnModel().getColumn(0).setMaxWidth(80);

        t.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int row = t.getSelectedRow();
                if (row >= 0) {
                    linhaSelecionada = row;
                    txtNome.setText((String) tableModel.getValueAt(row, 1));
                    cmbTipo.setSelectedItem(tableModel.getValueAt(row, 2));
                }
            }
        });
        return t;
    }

    private void salvar() {
        String nome = txtNome.getText().trim();
        String tipo = (String) cmbTipo.getSelectedItem();

        if (nome.isEmpty()) { mostrarErro("O nome da turma é obrigatório."); return; }

        if (linhaSelecionada >= 0) {
            tableModel.setValueAt(nome, linhaSelecionada, 1);
            tableModel.setValueAt(tipo, linhaSelecionada, 2);
            mostrarSucesso("Turma atualizada!");
        } else {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (nome.equalsIgnoreCase((String) tableModel.getValueAt(i, 1))) {
                    mostrarErro("Já existe uma turma com esse nome.");
                    return;
                }
            }
            tableModel.addRow(new Object[]{tableModel.getRowCount() + 1, nome, tipo});
            mostrarSucesso("Turma cadastrada com sucesso!");
        }
        limpar();
    }

    private void excluir() {
        int row = tabela.getSelectedRow();
        if (row < 0) { mostrarErro("Selecione uma turma para excluir."); return; }
        String nome = (String) tableModel.getValueAt(row, 1);
        int c = JOptionPane.showConfirmDialog(this,
            "Excluir a turma \"" + nome + "\"?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (c == JOptionPane.YES_OPTION) {
            tableModel.removeRow(row);
            renumerar();
            mostrarSucesso("Turma excluída!");
            limpar();
        }
    }

    private void limpar() {
        txtNome.setText(""); linhaSelecionada = -1;
        tabela.clearSelection(); txtNome.requestFocus();
    }

    private void renumerar() {
        for (int i = 0; i < tableModel.getRowCount(); i++) tableModel.setValueAt(i + 1, i, 0);
    }

    private void popularDadosFicticios() {
        Object[][] dados = {
            {1, "1A", "Ensino Médio"}, {2, "1B", "Ensino Médio"},
            {3, "2A", "Ensino Médio"}, {4, "2B", "Ensino Médio"},
            {5, "3A", "Ensino Médio"}, {6, "6A", "Ensino Fundamental"},
        };
        for (Object[] row : dados) tableModel.addRow(row);
    }

    private void mostrarErro(String msg)   { JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE); }
    private void mostrarSucesso(String msg){ JOptionPane.showMessageDialog(this, msg, "Sucesso", JOptionPane.INFORMATION_MESSAGE); }
}
