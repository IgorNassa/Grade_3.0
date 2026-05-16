package br.sistema.view.panel.crud;

import br.sistema.controller.dtos.DisciplinaDTO;
import br.sistema.controller.interfaces.DisciplinaController;
import br.sistema.util.ServiceRegistry;
import br.sistema.view.util.AppTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Painel CRUD de Disciplinas — tema dark, sem dependência de banco.
 * Substitui o DisciplinaView original com visual profissional.
 */
public class DisciplinaView extends JPanel {

    // Modelo da tabela
    private final DefaultTableModel tableModel;
    private final JTable tabela;

    // Campos do formulário
    private JPanel painelFormulario;
    private JLabel lblFormTitulo;
    private JTextField txtNome;

    // Estado de edição
    private int linhaSelecionada = -1;

    private final DisciplinaController disciplinaController;

    public DisciplinaView() {
        this.disciplinaController = ServiceRegistry.getInstance().get(DisciplinaController.class);
        setLayout(new BorderLayout(0, 0));
        setBackground(AppTheme.BG_CONTENT);
        setBorder(new EmptyBorder(32, 32, 32, 32));

        tableModel = new DefaultTableModel(new String[]{"#", "Nome da Disciplina"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tabela = criarTabela();

        add(montarTopo(), BorderLayout.NORTH);
        add(montarCorpo(), BorderLayout.CENTER);

        refreshTable();
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

        JButton btnNovo = AppTheme.primaryButton("+ Cadastrar Disciplina");
        btnNovo.setPreferredSize(new Dimension(180, 40));
        btnNovo.addActionListener(e -> abrirFormularioNovo());

        JPanel painelAcao = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 8));
        painelAcao.setOpaque(false);
        painelAcao.add(btnNovo);
        painel.add(painelAcao, BorderLayout.EAST);

        return painel;
    }

    // ── Corpo ─────────────────────────────────────────────────────────────────
    private JPanel montarCorpo() {
        JPanel corpo = new JPanel(new BorderLayout(0, 20));
        corpo.setOpaque(false);

        painelFormulario = montarFormulario();
        painelFormulario.setVisible(false);

        corpo.add(painelFormulario, BorderLayout.NORTH);
        corpo.add(montarTabela(), BorderLayout.CENTER);

        return corpo;
    }

    // ── Formulário ────────────────────────────────────────────────────────────
    private JPanel montarFormulario() {
        JPanel card = AppTheme.cardPanel(new GridBagLayout());
        card.setBorder(new EmptyBorder(24, 28, 24, 28));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(0, 0, 10, 14);
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.HORIZONTAL;

        // Título do Formulário
        gc.gridx = 0; gc.gridy = 0; gc.gridwidth = 4;
        lblFormTitulo = new JLabel("Cadastrar Nova Disciplina");
        lblFormTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblFormTitulo.setForeground(AppTheme.TEXT_PRIMARY);
        lblFormTitulo.setBorder(new EmptyBorder(0, 0, 16, 0));
        card.add(lblFormTitulo, gc);

        // Row 1 — Labels
        gc.gridy = 1; gc.gridwidth = 1; gc.weightx = 0;
        card.add(AppTheme.label("Nome da Disciplina"), gc);

        // Field Nome
        gc.gridy = 2; gc.gridx = 0; gc.weightx = 1.0;
        txtNome = AppTheme.styledField("Ex: Matemática", 300);
        card.add(txtNome, gc);

        // Botões
        gc.gridx = 1; gc.weightx = 0;
        JButton btnSalvar = AppTheme.primaryButton("Salvar");
        card.add(btnSalvar, gc);

        gc.gridx = 2;
        JButton btnCancelar = AppTheme.secondaryButton("Cancelar");
        card.add(btnCancelar, gc);

        gc.gridx = 3;
        JButton btnExcluir = AppTheme.dangerButton("Excluir");
        card.add(btnExcluir, gc);

        btnSalvar.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> fecharFormulario());
        btnExcluir.addActionListener(e -> excluir());
        txtNome.addActionListener(e -> salvar());

        return card;
    }

    private void abrirFormularioNovo() {
        linhaSelecionada = -1;
        lblFormTitulo.setText("Cadastrar Nova Disciplina");
        txtNome.setText("");
        painelFormulario.setVisible(true);
        revalidate(); repaint();
        txtNome.requestFocus();
    }

    private void abrirFormularioEdicao(int row) {
        linhaSelecionada = row;
        lblFormTitulo.setText("Editar Disciplina: " + tableModel.getValueAt(row, 1));
        txtNome.setText((String) tableModel.getValueAt(row, 1));
        painelFormulario.setVisible(true);
        revalidate(); repaint();
    }

    private void fecharFormulario() {
        painelFormulario.setVisible(false);
        txtNome.setText("");
        linhaSelecionada = -1;
        tabela.clearSelection();
        revalidate(); repaint();
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

        // Scroll
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

        // Container para organizar topo (header + separator)
        JPanel topo = new JPanel();
        topo.setOpaque(false);
        topo.setLayout(new BoxLayout(topo, BoxLayout.Y_AXIS));
        topo.add(header);
        topo.add(new JSeparator() {{
            setForeground(AppTheme.BORDER_COLOR);
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        }});

        card.add(topo, BorderLayout.NORTH);
        card.add(scroll, BorderLayout.CENTER);

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
                    abrirFormularioEdicao(row);
                }
            }
        });

        return t;
    }

    // ── Ações CRUD ────────────────────────────────────────────────────────────
    private void refreshTable() {
        tableModel.setRowCount(0);
        try {
            List<DisciplinaDTO> lista = disciplinaController.findAll();
            for (DisciplinaDTO d : lista) {
                tableModel.addRow(new Object[]{d.id(), d.nome()});
            }
        } catch (Exception e) {
            mostrarErro("Erro ao carregar disciplinas: " + e.getMessage());
        }
    }

    private void salvar() {
        String nome = txtNome.getText().trim();
        if (nome.isEmpty()) {
            mostrarErro("O nome da disciplina não pode ser vazio.");
            return;
        }

        try {
            if (linhaSelecionada >= 0) {
                Long id = (Long) tableModel.getValueAt(linhaSelecionada, 0);
                DisciplinaDTO dto = new DisciplinaDTO(id, nome);
                disciplinaController.update(dto);
                mostrarSucesso("Disciplina atualizada com sucesso!");
            } else {
                DisciplinaDTO dto = new DisciplinaDTO(null, nome);
                disciplinaController.save(dto);
                mostrarSucesso("Disciplina cadastrada com sucesso!");
            }
            fecharFormulario();
            refreshTable();
        } catch (Exception e) {
            mostrarErro("Erro ao salvar disciplina: " + e.getMessage());
        }
    }

    private void excluir() {
        int row = tabela.getSelectedRow();
        if (row < 0) {
            mostrarErro("Selecione uma disciplina na tabela para excluir.");
            return;
        }
        
        Long id = (Long) tableModel.getValueAt(row, 0);
        String nome = (String) tableModel.getValueAt(row, 1);
        
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Deseja realmente excluir a disciplina \"" + nome + "\"?",
            "Confirmar Exclusão",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                disciplinaController.delete(new DisciplinaDTO(id, nome));
                mostrarSucesso("Disciplina excluída com sucesso!");
                fecharFormulario();
                refreshTable();
            } catch (Exception e) {
                mostrarErro("Erro ao excluir disciplina: " + e.getMessage());
            }
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
        // Removido - usando banco de dados
    }

    private void mostrarErro(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarSucesso(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
}