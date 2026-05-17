package br.sistema.view.panel.crud;

import br.sistema.controller.dtos.TurmaDTO;
import br.sistema.controller.interfaces.TurmaController;
import br.sistema.util.ServiceRegistry;
import br.sistema.view.util.AppTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Painel CRUD de Turmas — tema dark.
 * Turma: nome + tipo (Ensino Médio / Fundamental).
 */
public class TurmaView extends JPanel {

    private final DefaultTableModel tableModel;
    private final JTable tabela;

    private JPanel painelFormulario;
    private JLabel lblFormTitulo;
    private JTextField txtNome;
    private JComboBox<String> cmbTipo;

    private int linhaSelecionada = -1;

    private final TurmaController turmaController;

    public TurmaView() {
        this.turmaController = ServiceRegistry.getInstance().get(TurmaController.class);
        setLayout(new BorderLayout());
        setBackground(AppTheme.BG_CONTENT);
        setBorder(new EmptyBorder(32, 32, 32, 32));

        tableModel = new DefaultTableModel(new String[]{"#", "Nome da Turma", "Tipo"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = criarTabela();

        add(montarTopo(), BorderLayout.NORTH);
        add(montarCorpo(), BorderLayout.CENTER);
        refreshTable();
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

        JButton btnNovo = AppTheme.primaryButton("+ Cadastrar Turma");
        btnNovo.setPreferredSize(new Dimension(180, 40));
        btnNovo.addActionListener(e -> abrirFormularioNovo());

        JPanel painelAcao = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 8));
        painelAcao.setOpaque(false);
        painelAcao.add(btnNovo);
        p.add(painelAcao, BorderLayout.EAST);

        return p;
    }

    private JPanel montarCorpo() {
        JPanel corpo = new JPanel(new BorderLayout(0, 20));
        corpo.setOpaque(false);

        painelFormulario = montarFormulario();
        painelFormulario.setVisible(false);

        corpo.add(painelFormulario, BorderLayout.NORTH);
        corpo.add(montarTabela(), BorderLayout.CENTER);
        return corpo;
    }

    private JPanel montarFormulario() {
        JPanel card = AppTheme.cardPanel(new GridBagLayout());
        card.setBorder(new EmptyBorder(24, 28, 24, 28));

        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(0, 0, 10, 14);

        // Título do Formulário
        gc.gridx = 0; gc.gridy = 0; gc.gridwidth = 5;
        lblFormTitulo = new JLabel("Cadastrar Nova Turma");
        lblFormTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblFormTitulo.setForeground(AppTheme.TEXT_PRIMARY);
        lblFormTitulo.setBorder(new EmptyBorder(0, 0, 16, 0));
        card.add(lblFormTitulo, gc);

        // Row 1 — Labels
        gc.gridy = 1; gc.gridwidth = 1; gc.insets = new Insets(0, 0, 6, 14);
        card.add(AppTheme.label("Nome da Turma"), gc);
        gc.gridx = 1;
        card.add(AppTheme.label("Tipo de Ensino"), gc);

        // Row 2 — Fields + Buttons
        gc.gridy = 2; gc.gridx = 0; gc.weightx = 0.5; gc.insets = new Insets(0, 0, 0, 14);
        txtNome = AppTheme.styledField("Ex: 1A, 2B, 3C", 200);
        card.add(txtNome, gc);

        gc.gridx = 1; gc.weightx = 0;
        cmbTipo = AppTheme.styledCombo(new String[]{"Ensino Médio", "Ensino Fundamental"});
        card.add(cmbTipo, gc);

        gc.gridx = 2;
        JButton btnSalvar = AppTheme.primaryButton("Salvar");
        card.add(btnSalvar, gc);

        gc.gridx = 3;
        JButton btnCancelar = AppTheme.secondaryButton("Limpar/Novo");
        card.add(btnCancelar, gc);

        gc.gridx = 4;
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
        lblFormTitulo.setText("Cadastrar Nova Turma");
        limpar();
        painelFormulario.setVisible(true);
        revalidate(); repaint();
        txtNome.requestFocus();
    }

    private void abrirFormularioEdicao(int row) {
        linhaSelecionada = row;
        lblFormTitulo.setText("Editar Turma: " + tableModel.getValueAt(row, 1));
        txtNome.setText((String) tableModel.getValueAt(row, 1));
        cmbTipo.setSelectedItem(tableModel.getValueAt(row, 2));
        painelFormulario.setVisible(true);
        revalidate(); repaint();
    }

    private void fecharFormulario() {
        // Agora o formulário não fecha mais automaticamente (fica fixo)
        // apenas limpamos os campos e voltamos para o estado de "Novo"
        linhaSelecionada = -1;
        lblFormTitulo.setText("Cadastrar Nova Turma");
        limpar();
        revalidate(); repaint();
    }

    private JPanel montarTabela() {
        JPanel card = AppTheme.cardPanel(new BorderLayout());
        card.setBorder(new EmptyBorder(0, 0, 0, 0));

        // Cabeçalho
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 14));
        header.setOpaque(false);
        JLabel titulo = new JLabel("Lista de Turmas");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titulo.setForeground(AppTheme.TEXT_PRIMARY);
        header.add(titulo);

        // Scroll
        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(AppTheme.BG_CARD);

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
                    abrirFormularioEdicao(row);
                }
            }
        });
        return t;
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        try {
            List<TurmaDTO> lista = turmaController.findAll();
            for (TurmaDTO t : lista) {
                tableModel.addRow(new Object[]{t.id(), t.nome(), t.eMedio() ? "Ensino Médio" : "Ensino Fundamental"});
            }
        } catch (Exception e) {
            mostrarErro("Erro ao carregar turmas: " + e.getMessage());
        }
    }

    private void salvar() {
        String nome = txtNome.getText().trim();
        String tipo = (String) cmbTipo.getSelectedItem();
        boolean eMedio = "Ensino Médio".equals(tipo);

        if (nome.isEmpty()) { 
            mostrarErro("O nome da turma é obrigatório."); 
            return; 
        }

        try {
            if (linhaSelecionada >= 0) {
                Long id = (Long) tableModel.getValueAt(linhaSelecionada, 0);
                TurmaDTO dto = new TurmaDTO(id, nome, eMedio);
                turmaController.update(dto);
                mostrarSucesso("Turma atualizada com sucesso!");
            } else {
                TurmaDTO dto = new TurmaDTO(null, nome, eMedio);
                turmaController.save(dto);
                mostrarSucesso("Turma cadastrada com sucesso!");
            }
            fecharFormulario();
            refreshTable();
        } catch (Exception e) {
            mostrarErro("Erro ao salvar turma: " + e.getMessage());
        }
    }

    private void excluir() {
        int row = tabela.getSelectedRow();
        if (row < 0) { 
            mostrarErro("Selecione uma turma para excluir."); 
            return; 
        }
        
        Long id = (Long) tableModel.getValueAt(row, 0);
        String nome = (String) tableModel.getValueAt(row, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Deseja realmente excluir a turma \"" + nome + "\"?", 
            "Confirmar Exclusão", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                turmaController.delete(new TurmaDTO(id, nome, false));
                mostrarSucesso("Turma excluída com sucesso!");
                fecharFormulario();
                refreshTable();
            } catch (Exception e) {
                mostrarErro("Erro ao excluir turma: " + e.getMessage());
            }
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
        // Removido - usando banco de dados
    }

    private void mostrarErro(String msg)   { JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE); }
    private void mostrarSucesso(String msg){ JOptionPane.showMessageDialog(this, msg, "Sucesso", JOptionPane.INFORMATION_MESSAGE); }
}
