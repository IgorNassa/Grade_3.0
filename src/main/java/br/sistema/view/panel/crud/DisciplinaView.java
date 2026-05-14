package br.sistema.view.panel.crud;

import br.sistema.view.util.AppTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Painel CRUD de Disciplinas — tema dark, sem dependência de banco.
 * Substitui o DisciplinaView original com visual profissional.
 */
public class DisciplinaView extends JPanel {

    // Modelo da tabela
    private final DefaultTableModel tableModel;
    private final JTable tabela;

    // Campos do formulário
    private JTextField txtNome;

    // Estado de edição
    private int linhaSelecionada = -1;

    public DisciplinaView() {
        setLayout(new BorderLayout(0, 0));
        setBackground(AppTheme.BG_CONTENT);
        setBorder(new EmptyBorder(32, 32, 32, 32));

        tableModel = new DefaultTableModel(new String[]{"#", "Nome da Disciplina"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tabela = criarTabela();

        add(montarTopo(), BorderLayout.NORTH);
        add(montarCorpo(), BorderLayout.CENTER);

        popularDadosFicticios();
    }

    // ── Topo ──────────────────────────────────────────────────────────────────
    private JPanel montarTopo() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setOpaque(false);
        painel.setBorder(new EmptyBorder(0, 0, 24, 0));

        JLabel titulo = AppTheme.sectionTitle("Disciplinas");
        JLabel sub = AppTheme.label("Gerencie o cadastro de disciplinas da grade curricular.");
        sub.setBorder(new EmptyBorder(4, 0, 0, 0));

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.add(titulo);
        textos.add(sub);

        painel.add(textos, BorderLayout.WEST);
        return painel;
    }

    // ── Corpo ─────────────────────────────────────────────────────────────────
    private JPanel montarCorpo() {
        JPanel corpo = new JPanel(new BorderLayout(0, 20));
        corpo.setOpaque(false);

        corpo.add(montarFormulario(), BorderLayout.NORTH);
        corpo.add(montarTabela(), BorderLayout.CENTER);

        return corpo;
    }

    // ── Formulário ────────────────────────────────────────────────────────────
    private JPanel montarFormulario() {
        JPanel card = AppTheme.cardPanel(new GridBagLayout());
        card.setBorder(new EmptyBorder(20, 24, 20, 24));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(0, 0, 0, 14);
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.HORIZONTAL;

        // Label Nome
        gc.gridx = 0; gc.gridy = 0; gc.weightx = 0;
        card.add(AppTheme.label("Nome da Disciplina"), gc);

        // Field Nome
        gc.gridx = 1; gc.weightx = 1.0;
        txtNome = AppTheme.styledField("Ex: Matemática", 300);
        card.add(txtNome, gc);

        // Botões
        gc.gridx = 2; gc.weightx = 0;
        JButton btnSalvar = AppTheme.primaryButton("Salvar");
        btnSalvar.setPreferredSize(new Dimension(100, 38));
        card.add(btnSalvar, gc);

        gc.gridx = 3;
        JButton btnLimpar = AppTheme.secondaryButton("Limpar");
        card.add(btnLimpar, gc);

        gc.gridx = 4;
        JButton btnExcluir = AppTheme.dangerButton("Excluir");
        card.add(btnExcluir, gc);

        // Ações
        btnSalvar.addActionListener(e -> salvar());
        btnLimpar.addActionListener(e -> limpar());
        btnExcluir.addActionListener(e -> excluir());
        txtNome.addActionListener(e -> salvar());

        return card;
    }

    // ── Tabela ────────────────────────────────────────────────────────────────
    private JPanel montarTabela() {
        JPanel card = AppTheme.cardPanel(new BorderLayout());
        card.setBorder(new EmptyBorder(0, 0, 0, 0));

        // Cabeçalho
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 14));
        header.setOpaque(false);
        JLabel titulo = new JLabel("Lista de Disciplinas");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titulo.setForeground(AppTheme.TEXT_PRIMARY);
        header.add(titulo);
        card.add(header, BorderLayout.NORTH);

        // Separador
        JSeparator sep = new JSeparator();
        sep.setForeground(AppTheme.BORDER_COLOR);
        sep.setBackground(AppTheme.BORDER_COLOR);
        card.add(sep, BorderLayout.CENTER);

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setBackground(AppTheme.BG_CARD);
        scroll.getViewport().setBackground(AppTheme.BG_CARD);

        // Barra de rolagem estilizada
        scroll.getVerticalScrollBar().setBackground(AppTheme.BG_CARD);
        scroll.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            protected void configureScrollBarColors() {
                thumbColor = AppTheme.PURPLE_SUBTLE;
                trackColor = AppTheme.BG_CARD;
            }
        });

        JPanel scrollContainer = new JPanel(new BorderLayout());
        scrollContainer.setOpaque(false);
        scrollContainer.setBorder(new EmptyBorder(4, 0, 0, 0));
        scrollContainer.add(scroll);

        card.add(scrollContainer, BorderLayout.SOUTH);

        // Ajuste layout do card
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.removeAll();
        card.add(header);
        card.add(new JSeparator() {{ setForeground(AppTheme.BORDER_COLOR); setMaximumSize(new Dimension(Integer.MAX_VALUE, 1)); }});
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
        t.setForeground(AppTheme.TEXT_PRIMARY);
        t.setSelectionBackground(AppTheme.BG_TABLE_SEL);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // Cabeçalho da tabela
        JTableHeader header = t.getTableHeader();
        header.setBackground(AppTheme.BG_CARD);
        header.setForeground(AppTheme.TEXT_SECONDARY);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.BORDER_COLOR));
        header.setPreferredSize(new Dimension(0, 40));

        // Coluna ID pequena
        t.getColumnModel().getColumn(0).setPreferredWidth(50);
        t.getColumnModel().getColumn(0).setMaxWidth(80);

        // Selecionar linha ao clicar
        t.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int row = t.getSelectedRow();
                if (row >= 0) {
                    linhaSelecionada = row;
                    txtNome.setText((String) tableModel.getValueAt(row, 1));
                }
            }
        });

        return t;
    }

    // ── Ações CRUD ────────────────────────────────────────────────────────────
    private void salvar() {
        String nome = txtNome.getText().trim();
        if (nome.isEmpty()) {
            mostrarErro("O nome da disciplina não pode ser vazio.");
            return;
        }

        if (linhaSelecionada >= 0) {
            // Editar linha existente
            tableModel.setValueAt(nome, linhaSelecionada, 1);
            mostrarSucesso("Disciplina atualizada com sucesso!");
        } else {
            // Verificar duplicata
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (nome.equalsIgnoreCase((String) tableModel.getValueAt(i, 1))) {
                    mostrarErro("Já existe uma disciplina com esse nome.");
                    return;
                }
            }
            int novoId = tableModel.getRowCount() + 1;
            tableModel.addRow(new Object[]{novoId, nome});
            mostrarSucesso("Disciplina cadastrada com sucesso!");
        }
        limpar();
    }

    private void excluir() {
        int row = tabela.getSelectedRow();
        if (row < 0) {
            mostrarErro("Selecione uma disciplina na tabela para excluir.");
            return;
        }
        String nome = (String) tableModel.getValueAt(row, 1);
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Deseja realmente excluir a disciplina \"" + nome + "\"?",
            "Confirmar Exclusão",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeRow(row);
            renumerarTabela();
            mostrarSucesso("Disciplina excluída com sucesso!");
            limpar();
        }
    }

    private void limpar() {
        txtNome.setText("");
        linhaSelecionada = -1;
        tabela.clearSelection();
        txtNome.requestFocus();
    }

    private void renumerarTabela() {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            tableModel.setValueAt(i + 1, i, 0);
        }
    }

    private void popularDadosFicticios() {
        String[] disciplinas = {"Matemática", "Português", "História", "Geografia", "Física",
                "Química", "Biologia", "Inglês", "Educação Física", "Arte"};
        for (int i = 0; i < disciplinas.length; i++) {
            tableModel.addRow(new Object[]{i + 1, disciplinas[i]});
        }
    }

    private void mostrarErro(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarSucesso(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
}