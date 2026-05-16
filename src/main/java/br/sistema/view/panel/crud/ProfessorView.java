package br.sistema.view.panel.crud;

import br.sistema.controller.dtos.ProfessorDTO;
import br.sistema.controller.interfaces.ProfessorController;
import br.sistema.util.ServiceRegistry;
import br.sistema.view.util.AppTheme;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProfessorView extends JPanel {

    private final DefaultTableModel model;
    private final JTable tabela;
    private int linhaSelecionada = -1;

    private JPanel painelFormulario;
    private JTextField txtNome;
    private JTextField txtDisciplinas;
    private JLabel lblFormTitulo;

    private final ProfessorController professorController;

    public ProfessorView() {
        this.professorController = ServiceRegistry.getInstance().get(ProfessorController.class);
        setLayout(new BorderLayout());
        setBackground(AppTheme.BG_CONTENT);
        setBorder(new EmptyBorder(32, 32, 32, 32));

        model = new DefaultTableModel(new String[]{"ID", "Professor", "Disciplinas", "Status"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
            @Override public Class<?> getColumnClass(int c) { return c == 0 ? Integer.class : String.class; }
        };

        tabela = buildTable();

        add(buildHeader(), BorderLayout.NORTH);
        add(buildBody(),   BorderLayout.CENTER);
        refreshTable();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // HEADER
    // ─────────────────────────────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(0, 0, 24, 0));

        JLabel titulo = AppTheme.sectionTitle("Professores");
        JLabel sub = AppTheme.label("Gestão de professores e suas respectivas disciplinas lecionadas.");
        sub.setBorder(new EmptyBorder(4, 0, 0, 0));

        JPanel txt = new JPanel();
        txt.setOpaque(false);
        txt.setLayout(new BoxLayout(txt, BoxLayout.Y_AXIS));
        txt.add(titulo);
        txt.add(sub);
        p.add(txt, BorderLayout.WEST);

        JButton btnNovo = AppTheme.primaryButton("+ Cadastrar Professor");
        btnNovo.setPreferredSize(new Dimension(180, 40));
        btnNovo.addActionListener(e -> abrirFormularioNovo());
        
        JPanel painelAcao = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 8));
        painelAcao.setOpaque(false);
        painelAcao.add(btnNovo);
        p.add(painelAcao, BorderLayout.EAST);

        return p;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // BODY
    // ─────────────────────────────────────────────────────────────────────────
    private JPanel buildBody() {
        JPanel body = new JPanel(new BorderLayout(0, 20));
        body.setOpaque(false);

        painelFormulario = buildForm();
        painelFormulario.setVisible(false); // Inicialmente oculto

        body.add(painelFormulario,  BorderLayout.NORTH);
        body.add(buildTablePanel(), BorderLayout.CENTER);
        return body;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // FORMULÁRIO (Exibido sob demanda)
    // ─────────────────────────────────────────────────────────────────────────
    private JPanel buildForm() {
        JPanel card = AppTheme.cardPanel(new GridBagLayout());
        card.setBorder(new EmptyBorder(20, 24, 20, 24));

        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.WEST;
        gc.fill   = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(0, 0, 12, 14);

        // Título do formulário
        lblFormTitulo = new JLabel("Cadastrar Novo Professor");
        lblFormTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblFormTitulo.setForeground(AppTheme.TEXT_PRIMARY);
        gc.gridy = 0; gc.gridx = 0; gc.gridwidth = 5;
        card.add(lblFormTitulo, gc);

        // Labels row
        gc.gridy = 1; gc.gridwidth = 1; gc.insets = new Insets(0, 0, 4, 14);
        gc.gridx = 0; gc.weightx = 0;   card.add(AppTheme.label("Nome do Professor"), gc);
        gc.gridx = 1; gc.weightx = 0;   card.add(AppTheme.label("Disciplinas (separadas por vírgula)"), gc);

        // Fields row
        gc.gridy = 2; gc.insets = new Insets(0, 0, 0, 14);
        gc.gridx = 0; gc.weightx = 0.40;
        txtNome = AppTheme.styledField("Ex: João Silva", 240);
        card.add(txtNome, gc);

        gc.gridx = 1; gc.weightx = 0.60;
        txtDisciplinas = AppTheme.styledField("Ex: Matemática, Física", 340);
        card.add(txtDisciplinas, gc);

        JButton btnSalvar   = AppTheme.primaryButton("Salvar");
        JButton btnCancelar = AppTheme.secondaryButton("Cancelar");
        JButton btnExcluir  = AppTheme.dangerButton("Excluir");

        btnSalvar.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> fecharFormulario());
        btnExcluir.addActionListener(e -> excluir());

        gc.gridx = 2; gc.weightx = 0; card.add(btnSalvar,   gc);
        gc.gridx = 3;                  card.add(btnCancelar, gc);
        gc.gridx = 4;                  card.add(btnExcluir,  gc);

        return card;
    }

    private void abrirFormularioNovo() {
        linhaSelecionada = -1;
        lblFormTitulo.setText("Cadastrar Novo Professor");
        txtNome.setText("");
        txtDisciplinas.setText("");
        tabela.clearSelection();
        painelFormulario.setVisible(true);
        revalidate();
        repaint();
        txtNome.requestFocus();
    }

    private void abrirFormularioEdicao(int row) {
        linhaSelecionada = row;
        lblFormTitulo.setText("Editar Professor: ID " + model.getValueAt(row, 0));
        txtNome.setText((String) model.getValueAt(row, 1));
        txtDisciplinas.setText((String) model.getValueAt(row, 2));
        painelFormulario.setVisible(true);
        revalidate();
        repaint();
    }

    private void fecharFormulario() {
        painelFormulario.setVisible(false);
        linhaSelecionada = -1;
        txtNome.setText("");
        txtDisciplinas.setText("");
        tabela.clearSelection();
        revalidate();
        repaint();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TABELA CONTAINER
    // ─────────────────────────────────────────────────────────────────────────
    private JPanel buildTablePanel() {
        JPanel card = AppTheme.cardPanel(new BorderLayout());

        // ── Toolbar ──────────────────────────────────────────────────────────
        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setOpaque(false);
        toolbar.setBorder(new EmptyBorder(16, 20, 14, 20));

        JLabel tit = new JLabel("Lista de Professores");
        tit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tit.setForeground(AppTheme.TEXT_PRIMARY);

        toolbar.add(tit, BorderLayout.WEST);
        card.add(toolbar, BorderLayout.NORTH);

        // Separador
        JSeparator sep = new JSeparator();
        sep.setForeground(AppTheme.BORDER_COLOR);
        sep.setBackground(AppTheme.BORDER_COLOR);

        // Scroll
        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(AppTheme.BG_CARD);
        scroll.setBackground(AppTheme.BG_CARD);
        scroll.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override protected void configureScrollBarColors() {
                thumbColor = AppTheme.PURPLE_SUBTLE;
                trackColor = AppTheme.BG_CARD;
            }
            @Override protected JButton createDecreaseButton(int o) { return invisibleBtn(); }
            @Override protected JButton createIncreaseButton(int o) { return invisibleBtn(); }
            JButton invisibleBtn() {
                JButton b = new JButton(); b.setPreferredSize(new Dimension(0, 0)); return b;
            }
        });

        JPanel containerCentral = new JPanel(new BorderLayout());
        containerCentral.setOpaque(false);
        containerCentral.add(sep, BorderLayout.NORTH);
        containerCentral.add(scroll, BorderLayout.CENTER);

        card.add(containerCentral, BorderLayout.CENTER);
        return card;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TABELA (Layout Clean e Elegante)
    // ─────────────────────────────────────────────────────────────────────────
    private JTable buildTable() {
        JTable t = new JTable(model) {
            @Override public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                boolean sel = isRowSelected(row);
                Color bg = sel ? AppTheme.BG_TABLE_SEL
                         : row % 2 == 0 ? AppTheme.BG_TABLE_ROW : AppTheme.BG_TABLE_ALT;
                c.setBackground(bg);
                c.setForeground(sel ? Color.WHITE : AppTheme.TEXT_PRIMARY);
                return c;
            }
        };

        t.setRowHeight(46);
        t.setShowGrid(false);
        t.setIntercellSpacing(new Dimension(0, 0));
        t.setBackground(AppTheme.BG_TABLE_ROW);
        t.setForeground(AppTheme.TEXT_PRIMARY);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        t.setFillsViewportHeight(true);
        t.setSelectionBackground(AppTheme.BG_TABLE_SEL);

        // Header
        JTableHeader h = t.getTableHeader();
        h.setReorderingAllowed(false);
        h.setBackground(AppTheme.BG_CARD);
        h.setForeground(AppTheme.TEXT_SECONDARY);
        h.setFont(new Font("Segoe UI", Font.BOLD, 12));
        h.setPreferredSize(new Dimension(0, 40));
        h.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.BORDER_COLOR));

        // Coluna ID
        t.getColumnModel().getColumn(0).setPreferredWidth(60);
        t.getColumnModel().getColumn(0).setMaxWidth(80);

        // Coluna Status
        t.getColumnModel().getColumn(3).setPreferredWidth(100);
        t.getColumnModel().getColumn(3).setMaxWidth(120);

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

    // ─────────────────────────────────────────────────────────────────────────
    // AÇÕES
    // ─────────────────────────────────────────────────────────────────────────
    private void refreshTable() {
        model.setRowCount(0);
        try {
            List<ProfessorDTO> lista = professorController.findAll();
            for (ProfessorDTO p : lista) {
                String dStr = p.disciplinas() != null ? String.join(", ", p.disciplinas()) : "";
                model.addRow(new Object[]{p.id(), p.nome(), dStr, "Ativo"});
            }
        } catch (Exception e) {
            erro("Erro ao carregar professores: " + e.getMessage());
        }
    }

    private void salvar() {
        String nome = txtNome.getText().trim();
        String discStr = txtDisciplinas.getText().trim();
        
        if (nome.isEmpty()) { 
            erro("O nome do professor é obrigatório."); 
            return; 
        }

        List<String> disciplinas = Arrays.stream(discStr.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        try {
            if (linhaSelecionada >= 0) {
                Long id = (Long) model.getValueAt(linhaSelecionada, 0);
                ProfessorDTO dto = new ProfessorDTO(id, nome, disciplinas);
                professorController.update(dto);
                ok("Professor atualizado com sucesso!");
            } else {
                ProfessorDTO dto = new ProfessorDTO(null, nome, disciplinas);
                professorController.save(dto);
                ok("Professor cadastrado com sucesso!");
            }
            fecharFormulario();
            refreshTable();
        } catch (Exception e) {
            erro("Erro ao salvar professor: " + e.getMessage());
        }
    }

    private void excluir() {
        int row = tabela.getSelectedRow();
        if (row < 0) { 
            erro("Selecione um professor na tabela."); 
            return; 
        }

        Long id = (Long) model.getValueAt(row, 0);
        String nome = (String) model.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(this, 
                "Deseja realmente excluir o professor \"" + nome + "\"?",
                "Confirmar Exclusão", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                professorController.delete(new ProfessorDTO(id, nome, null));
                ok("Professor excluído com sucesso!");
                fecharFormulario();
                refreshTable();
            } catch (Exception e) {
                erro("Erro ao excluir professor: " + e.getMessage());
            }
        }
    }

    private void erro(String msg) { JOptionPane.showMessageDialog(this, msg, "Erro",    JOptionPane.ERROR_MESSAGE); }
    private void ok(String msg)   { JOptionPane.showMessageDialog(this, msg, "Sucesso", JOptionPane.INFORMATION_MESSAGE); }
}
