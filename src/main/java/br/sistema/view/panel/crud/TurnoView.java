package br.sistema.view.panel.crud;

import br.sistema.controller.dtos.TurnoDTO;
import br.sistema.controller.interfaces.TurnoController;
import br.sistema.model.entity.TipoTurno;
import br.sistema.util.ServiceRegistry;
import br.sistema.view.util.AppTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Painel CRUD de Turnos — tema dark.
 * Turno: tipo (MATUTINO/VESPERTINO/NOTURNO), início, fim, tempo de aula, qtd aulas, aulas antes do intervalo.
 */
public class TurnoView extends JPanel {

    private final DefaultTableModel tableModel;
    private final JTable tabela;

    private JPanel painelFormulario;
    private JLabel lblFormTitulo;
    private JComboBox<String> cmbTipo;
    private JTextField txtInicio;
    private JTextField txtFim;
    private JTextField txtTempoAula;
    private JTextField txtQtdAulas;
    private JTextField txtAulasIntervalo;

    private int linhaSelecionada = -1;
    private final TurnoController turnoController;

    public TurnoView() {
        this.turnoController = ServiceRegistry.getInstance().get(TurnoController.class);
        setLayout(new BorderLayout());
        setBackground(AppTheme.BG_CONTENT);
        setBorder(new EmptyBorder(32, 32, 32, 32));

        tableModel = new DefaultTableModel(
            new String[]{"ID", "Tipo", "Início", "Fim", "T. Aula (min)", "Qtd. Aulas", "Antes Intervalo"}, 0
        ) { @Override public boolean isCellEditable(int r, int c) { return false; } };

        tabela = criarTabela();

        add(montarTopo(), BorderLayout.NORTH);
        add(montarCorpo(), BorderLayout.CENTER);
        refreshTable();
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

        JButton btnNovo = AppTheme.primaryButton("+ Cadastrar Turno");
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
        gc.insets = new Insets(0, 0, 10, 12);

        // Título do Formulário
        gc.gridx = 0; gc.gridy = 0; gc.gridwidth = 9;
        lblFormTitulo = new JLabel("Cadastrar Novo Turno");
        lblFormTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblFormTitulo.setForeground(AppTheme.TEXT_PRIMARY);
        lblFormTitulo.setBorder(new EmptyBorder(0, 0, 16, 0));
        card.add(lblFormTitulo, gc);

        // Row 1 — Labels
        gc.gridy = 1; gc.gridwidth = 1; gc.insets = new Insets(0, 0, 6, 12);
        addLabel(card, gc, 0, "Tipo");
        addLabel(card, gc, 1, "Início (HH:mm)");
        addLabel(card, gc, 2, "Fim (HH:mm)");
        addLabel(card, gc, 3, "T. Aula (min)");
        addLabel(card, gc, 4, "Qtd. Aulas");
        addLabel(card, gc, 5, "Antes Intervalo");

        // Row 2 — Fields
        gc.gridy = 2; gc.insets = new Insets(0, 0, 0, 12);
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
        JButton btnCancelar = AppTheme.secondaryButton("Limpar/Novo");
        card.add(btnCancelar, gc);

        gc.gridx = 8;
        JButton btnExcluir = AppTheme.dangerButton("Excluir");
        card.add(btnExcluir, gc);

        btnSalvar.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> fecharFormulario());
        btnExcluir.addActionListener(e -> excluir());

        return card;
    }

    private void abrirFormularioNovo() {
        linhaSelecionada = -1;
        lblFormTitulo.setText("Cadastrar Novo Turno");
        limpar();
        painelFormulario.setVisible(true);
        revalidate(); repaint();
        txtInicio.requestFocus();
    }

    private void abrirFormularioEdicao(int row) {
        linhaSelecionada = row;
        lblFormTitulo.setText("Editar Turno: " + tableModel.getValueAt(row, 1));
        cmbTipo.setSelectedItem(tableModel.getValueAt(row, 1));
        txtInicio.setText((String) tableModel.getValueAt(row, 2));
        txtFim.setText((String) tableModel.getValueAt(row, 3));
        txtTempoAula.setText(String.valueOf(tableModel.getValueAt(row, 4)));
        txtQtdAulas.setText(String.valueOf(tableModel.getValueAt(row, 5)));
        txtAulasIntervalo.setText(String.valueOf(tableModel.getValueAt(row, 6)));
        painelFormulario.setVisible(true);
        revalidate(); repaint();
    }

    private void fecharFormulario() {
        // Agora o formulário não fecha mais automaticamente (fica fixo)
        // apenas limpamos os campos e voltamos para o estado de "Novo"
        linhaSelecionada = -1;
        lblFormTitulo.setText("Cadastrar Novo Turno");
        limpar();
        revalidate(); repaint();
    }

    private void addLabel(JPanel p, GridBagConstraints gc, int gridx, String txt) {
        GridBagConstraints g = (GridBagConstraints) gc.clone();
        g.gridx = gridx;
        g.weightx = 0;
        p.add(AppTheme.label(txt), g);
    }

    private JPanel montarTabela() {
        JPanel card = AppTheme.cardPanel(new BorderLayout());
        card.setBorder(new EmptyBorder(0, 0, 0, 0));

        // Cabeçalho
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 14));
        header.setOpaque(false);
        JLabel titulo = new JLabel("Lista de Turnos Cadastrados");
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

        t.getColumnModel().getColumn(0).setMaxWidth(50);

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
            List<TurnoDTO> lista = turnoController.findAll();
            DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm");
            for (TurnoDTO t : lista) {
                tableModel.addRow(new Object[]{
                    t.id(),
                    t.nomeTurno().name(),
                    t.inicioTurno().format(df),
                    t.fimTurno().format(df),
                    t.tempoAula(),
                    t.aulasTurno(),
                    t.aulasAntesIntervalo()
                });
            }
        } catch (Exception e) {
            mostrarErro("Erro ao carregar turnos: " + e.getMessage());
        }
    }

    private void salvar() {
        String tipoStr = (String) cmbTipo.getSelectedItem();
        String inicio = txtInicio.getText().trim();
        String fim = txtFim.getText().trim();
        String tempoStr = txtTempoAula.getText().trim();
        String qtdStr = txtQtdAulas.getText().trim();
        String intervStr = txtAulasIntervalo.getText().trim();

        if (inicio.isEmpty() || fim.isEmpty() || tempoStr.isEmpty() || qtdStr.isEmpty() || intervStr.isEmpty()) {
            mostrarErro("Todos os campos são obrigatórios.");
            return;
        }

        try {
            LocalTime tInicio = LocalTime.parse(inicio);
            LocalTime tFim = LocalTime.parse(fim);
            int tempo = Integer.parseInt(tempoStr);
            int qtd = Integer.parseInt(qtdStr);
            int interv = Integer.parseInt(intervStr);
            TipoTurno tipo = TipoTurno.valueOf(tipoStr);

            if (linhaSelecionada >= 0) {
                Long id = (Long) tableModel.getValueAt(linhaSelecionada, 0);
                TurnoDTO dto = new TurnoDTO(id, tipo, tInicio, tFim, tempo, qtd, interv);
                turnoController.update(dto);
                mostrarSucesso("Turno atualizado com sucesso!");
            } else {
                TurnoDTO dto = new TurnoDTO(null, tipo, tInicio, tFim, tempo, qtd, interv);
                turnoController.save(dto);
                mostrarSucesso("Turno cadastrado com sucesso!");
            }
            fecharFormulario();
            refreshTable();
        } catch (Exception e) {
            mostrarErro("Erro ao salvar turno: " + e.getMessage());
        }
    }

    private void excluir() {
        int row = tabela.getSelectedRow();
        if (row < 0) { 
            mostrarErro("Selecione um turno para excluir."); 
            return; 
        }

        Long id = (Long) tableModel.getValueAt(row, 0);
        String nome = (String) tableModel.getValueAt(row, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Excluir o turno \"" + nome + "\"?",
            "Confirmar", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                turnoController.delete(new TurnoDTO(id, null, null, null, null, null, null));
                mostrarSucesso("Turno excluído!");
                fecharFormulario();
                refreshTable();
            } catch (Exception e) {
                mostrarErro("Erro ao excluir turno: " + e.getMessage());
            }
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