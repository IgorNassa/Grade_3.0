package br.sistema.view.panel.crud;

import br.sistema.view.util.AppTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Painel CRUD de Turnos — tema dark.
 * Turno: tipo (MATUTINO/VESPERTINO/NOTURNO), início, fim, tempo de aula, qtd aulas, aulas antes do intervalo.
 */
public class TurnoView extends JPanel {

    private final DefaultTableModel tableModel;
    private final JTable tabela;

    private JComboBox<String> cmbTipo;
    private JTextField txtInicio;
    private JTextField txtFim;
    private JTextField txtTempoAula;
    private JTextField txtQtdAulas;
    private JTextField txtAulasIntervalo;

    private int linhaSelecionada = -1;

    public TurnoView() {
        setLayout(new BorderLayout());
        setBackground(AppTheme.BG_CONTENT);
        setBorder(new EmptyBorder(32, 32, 32, 32));

        tableModel = new DefaultTableModel(
            new String[]{"#", "Tipo", "Início", "Fim", "T. Aula (min)", "Qtd. Aulas", "Antes Intervalo"}, 0
        ) { @Override public boolean isCellEditable(int r, int c) { return false; } };

        tabela = criarTabela();

        add(montarTopo(), BorderLayout.NORTH);
        add(montarCorpo(), BorderLayout.CENTER);
        popularDadosFicticios();
    }

    private JPanel montarTopo() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(0, 0, 24, 0));

        JLabel titulo = AppTheme.sectionTitle("Turnos");
        JLabel sub = AppTheme.label("Configure os turnos escolares: horários, duração e distribuição de aulas.");
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
        gc.insets = new Insets(0, 0, 8, 12);

        // Row 0 — Labels
        gc.gridy = 0;
        addLabel(card, gc, 0, "Tipo");
        addLabel(card, gc, 1, "Início (HH:mm)");
        addLabel(card, gc, 2, "Fim (HH:mm)");
        addLabel(card, gc, 3, "T. Aula (min)");
        addLabel(card, gc, 4, "Qtd. Aulas");
        addLabel(card, gc, 5, "Antes Intervalo");

        // Row 1 — Fields
        gc.gridy = 1; gc.insets = new Insets(0, 0, 0, 12);
        gc.gridx = 0; gc.weightx = 0;
        cmbTipo = AppTheme.styledCombo(new String[]{"MATUTINO", "VESPERTINO", "NOTURNO"});
        cmbTipo.setPreferredSize(new Dimension(130, 38));
        card.add(cmbTipo, gc);

        gc.gridx = 1; gc.weightx = 0.2;
        txtInicio = AppTheme.styledField("07:00", 90);
        card.add(txtInicio, gc);

        gc.gridx = 2;
        txtFim = AppTheme.styledField("12:00", 90);
        card.add(txtFim, gc);

        gc.gridx = 3; gc.weightx = 0.1;
        txtTempoAula = AppTheme.styledField("50", 70);
        card.add(txtTempoAula, gc);

        gc.gridx = 4;
        txtQtdAulas = AppTheme.styledField("5", 70);
        card.add(txtQtdAulas, gc);

        gc.gridx = 5;
        txtAulasIntervalo = AppTheme.styledField("2", 70);
        card.add(txtAulasIntervalo, gc);

        // Botões
        gc.gridx = 6; gc.weightx = 0;
        JButton btnSalvar = AppTheme.primaryButton("Salvar");
        card.add(btnSalvar, gc);

        gc.gridx = 7;
        JButton btnLimpar = AppTheme.secondaryButton("Limpar");
        card.add(btnLimpar, gc);

        gc.gridx = 8;
        JButton btnExcluir = AppTheme.dangerButton("Excluir");
        card.add(btnExcluir, gc);

        btnSalvar.addActionListener(e -> salvar());
        btnLimpar.addActionListener(e -> limpar());
        btnExcluir.addActionListener(e -> excluir());

        return card;
    }

    private void addLabel(JPanel p, GridBagConstraints gc, int gridx, String txt) {
        GridBagConstraints g = (GridBagConstraints) gc.clone();
        g.gridx = gridx;
        g.weightx = 0;
        p.add(AppTheme.label(txt), g);
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
        JLabel titulo = new JLabel("Lista de Turnos Cadastrados");
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

        t.getColumnModel().getColumn(0).setMaxWidth(50);

        t.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int row = t.getSelectedRow();
                if (row >= 0) {
                    linhaSelecionada = row;
                    cmbTipo.setSelectedItem(tableModel.getValueAt(row, 1));
                    txtInicio.setText((String) tableModel.getValueAt(row, 2));
                    txtFim.setText((String) tableModel.getValueAt(row, 3));
                    txtTempoAula.setText(String.valueOf(tableModel.getValueAt(row, 4)));
                    txtQtdAulas.setText(String.valueOf(tableModel.getValueAt(row, 5)));
                    txtAulasIntervalo.setText(String.valueOf(tableModel.getValueAt(row, 6)));
                }
            }
        });
        return t;
    }

    private void salvar() {
        String tipo = (String) cmbTipo.getSelectedItem();
        String inicio = txtInicio.getText().trim();
        String fim = txtFim.getText().trim();
        String tempoStr = txtTempoAula.getText().trim();
        String qtdStr = txtQtdAulas.getText().trim();
        String intervStr = txtAulasIntervalo.getText().trim();

        if (inicio.isEmpty() || fim.isEmpty() || tempoStr.isEmpty() || qtdStr.isEmpty() || intervStr.isEmpty()) {
            mostrarErro("Todos os campos são obrigatórios.");
            return;
        }

        int tempo, qtd, interv;
        try {
            tempo = Integer.parseInt(tempoStr);
            qtd = Integer.parseInt(qtdStr);
            interv = Integer.parseInt(intervStr);
        } catch (NumberFormatException ex) {
            mostrarErro("Tempo de aula, quantidade e aulas antes do intervalo devem ser números inteiros.");
            return;
        }

        if (!inicio.matches("\\d{2}:\\d{2}") || !fim.matches("\\d{2}:\\d{2}")) {
            mostrarErro("Horários devem estar no formato HH:mm (Ex: 07:30).");
            return;
        }

        if (linhaSelecionada >= 0) {
            tableModel.setValueAt(tipo, linhaSelecionada, 1);
            tableModel.setValueAt(inicio, linhaSelecionada, 2);
            tableModel.setValueAt(fim, linhaSelecionada, 3);
            tableModel.setValueAt(tempo, linhaSelecionada, 4);
            tableModel.setValueAt(qtd, linhaSelecionada, 5);
            tableModel.setValueAt(interv, linhaSelecionada, 6);
            mostrarSucesso("Turno atualizado com sucesso!");
        } else {
            // Verificar se já existe esse tipo
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tipo.equals(tableModel.getValueAt(i, 1))) {
                    mostrarErro("Já existe um turno cadastrado como " + tipo + ".");
                    return;
                }
            }
            tableModel.addRow(new Object[]{tableModel.getRowCount() + 1, tipo, inicio, fim, tempo, qtd, interv});
            mostrarSucesso("Turno cadastrado com sucesso!");
        }
        limpar();
    }

    private void excluir() {
        int row = tabela.getSelectedRow();
        if (row < 0) { mostrarErro("Selecione um turno para excluir."); return; }
        String nome = (String) tableModel.getValueAt(row, 1);
        int c = JOptionPane.showConfirmDialog(this, "Excluir o turno \"" + nome + "\"?",
            "Confirmar", JOptionPane.YES_NO_OPTION);
        if (c == JOptionPane.YES_OPTION) {
            tableModel.removeRow(row);
            renumerar();
            mostrarSucesso("Turno excluído!");
            limpar();
        }
    }

    private void limpar() {
        txtInicio.setText(""); txtFim.setText(""); txtTempoAula.setText("");
        txtQtdAulas.setText(""); txtAulasIntervalo.setText("");
        cmbTipo.setSelectedIndex(0);
        linhaSelecionada = -1;
        tabela.clearSelection();
    }

    private void renumerar() {
        for (int i = 0; i < tableModel.getRowCount(); i++) tableModel.setValueAt(i + 1, i, 0);
    }

    private void popularDadosFicticios() {
        tableModel.addRow(new Object[]{1, "MATUTINO",   "07:00", "12:00", 50, 5, 2});
        tableModel.addRow(new Object[]{2, "VESPERTINO", "13:00", "18:00", 50, 5, 2});
        tableModel.addRow(new Object[]{3, "NOTURNO",    "19:00", "22:40", 50, 4, 2});
    }

    private void mostrarErro(String msg)   { JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE); }
    private void mostrarSucesso(String msg){ JOptionPane.showMessageDialog(this, msg, "Sucesso", JOptionPane.INFORMATION_MESSAGE); }
}