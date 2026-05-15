package br.sistema.view.panel.crud;

import br.sistema.controller.dtos.AulaDTO;
import br.sistema.controller.dtos.DisciplinaDTO;
import br.sistema.controller.dtos.TurmaDTO;
import br.sistema.controller.interfaces.AulaController;
import br.sistema.controller.interfaces.DisciplinaController;
import br.sistema.controller.interfaces.TurmaController;
import br.sistema.util.ServiceRegistry;
import br.sistema.view.panel.ContentPanel;
import br.sistema.view.util.AppTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GerarGradeView extends JPanel {

    private final TurmaController turmaController;
    private final DisciplinaController disciplinaController;
    private final AulaController aulaController;

    private JComboBox<TurmaDTO> cmbTurma;
    private JComboBox<String> cmbTurno;
    private JPanel gridDisciplinas;

    private final Map<DisciplinaDTO, JTextField> campoCargaMap = new LinkedHashMap<>();

    public GerarGradeView() {
        this.turmaController = ServiceRegistry.getInstance().get(TurmaController.class);
        this.disciplinaController = ServiceRegistry.getInstance().get(DisciplinaController.class);
        this.aulaController = ServiceRegistry.getInstance().get(AulaController.class);

        setLayout(new BorderLayout());
        setBackground(AppTheme.BG_CONTENT);
        setBorder(new EmptyBorder(32, 32, 32, 32));

        add(montarTopo(), BorderLayout.NORTH);
        add(montarCorpo(), BorderLayout.CENTER);

        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                recarregarDados();
            }
        });
    }

    private JPanel montarTopo() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(0, 0, 24, 0));

        JLabel titulo = AppTheme.sectionTitle("Gerar Grade");
        JLabel sub = AppTheme.label("Configure a carga horária semanal por disciplina e execute o gerador automático.");
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
        corpo.add(montarSelecao(), BorderLayout.NORTH);
        corpo.add(montarCargaHoraria(), BorderLayout.CENTER);
        return corpo;
    }

    private JPanel montarSelecao() {
        JPanel card = AppTheme.cardPanel(new FlowLayout(FlowLayout.LEFT, 20, 18));
        card.setPreferredSize(new Dimension(0, 80));

        card.add(AppTheme.label("Turma:"));

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
        card.add(cmbTurma);

        card.add(AppTheme.label("Turno:"));
        cmbTurno = AppTheme.styledCombo(new String[]{"MATUTINO", "VESPERTINO", "NOTURNO"});
        cmbTurno.setPreferredSize(new Dimension(150, 38));
        card.add(cmbTurno);

        return card;
    }

    private JPanel montarCargaHoraria() {
        JPanel card = AppTheme.cardPanel(new BorderLayout());
        card.setBorder(new EmptyBorder(20, 24, 20, 24));

        JLabel tituloCard = new JLabel("Carga Horária Semanal (aulas por semana)");
        tituloCard.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tituloCard.setForeground(AppTheme.TEXT_PRIMARY);
        tituloCard.setBorder(new EmptyBorder(0, 0, 16, 0));
        card.add(tituloCard, BorderLayout.NORTH);

        gridDisciplinas = new JPanel(new GridLayout(0, 2, 20, 12));
        gridDisciplinas.setOpaque(false);
        card.add(gridDisciplinas, BorderLayout.CENTER);

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 16));
        rodape.setOpaque(false);

        JButton btnGerar = AppTheme.primaryButton("⚡ Gerar Grade");
        btnGerar.setPreferredSize(new Dimension(160, 44));
        btnGerar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGerar.addActionListener(e -> gerarGrade());
        rodape.add(btnGerar);

        card.add(rodape, BorderLayout.SOUTH);
        return card;
    }

    private void recarregarDados() {
        TurmaDTO selecionada = (TurmaDTO) cmbTurma.getSelectedItem();
        cmbTurma.removeAllItems();
        for (TurmaDTO t : turmaController.findAll()) {
            cmbTurma.addItem(t);
        }
        if (selecionada != null) cmbTurma.setSelectedItem(selecionada);

        gridDisciplinas.removeAll();
        campoCargaMap.clear();

        List<DisciplinaDTO> disciplinasCriadas = disciplinaController.findAll();
        for (DisciplinaDTO disc : disciplinasCriadas) {
            JPanel linha = new JPanel(new BorderLayout(12, 0));
            linha.setOpaque(false);

            JLabel lblDisc = new JLabel(disc.nome());
            lblDisc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            lblDisc.setForeground(AppTheme.TEXT_PRIMARY);
            lblDisc.setPreferredSize(new Dimension(180, 38));

            // Zera o campo por padrão para não gerar lixo aleatório
            JTextField txtCargaField = AppTheme.styledField("0", 80);
            campoCargaMap.put(disc, txtCargaField);

            linha.add(lblDisc, BorderLayout.WEST);
            linha.add(txtCargaField, BorderLayout.CENTER);
            gridDisciplinas.add(linha);
        }

        gridDisciplinas.revalidate();
        gridDisciplinas.repaint();
    }

    private void gerarGrade() {
        TurmaDTO turmaSelecionada = (TurmaDTO) cmbTurma.getSelectedItem();
        String turno = (String) cmbTurno.getSelectedItem();

        if (turmaSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Nenhuma turma selecionada para a operação.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int totalAulas = 0;
        Map<DisciplinaDTO, Integer> cargaParaGerar = new HashMap<>();

        for (Map.Entry<DisciplinaDTO, JTextField> entry : campoCargaMap.entrySet()) {
            DisciplinaDTO disc = entry.getKey();
            try {
                int qtd = Integer.parseInt(entry.getValue().getText().trim());
                if (qtd < 0) {
                    JOptionPane.showMessageDialog(this, "A carga de " + disc.nome() + " não pode ser negativa.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (qtd > 0) {
                    cargaParaGerar.put(disc, qtd);
                    totalAulas += qtd;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Use apenas números inteiros em " + disc.nome(), "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        if (totalAulas == 0) {
            JOptionPane.showMessageDialog(this, "Defina ao menos uma aula.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (totalAulas > 25) {
            JOptionPane.showMessageDialog(this, "A grade comporta no máximo 25 aulas semanais. Reduza a carga.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // --- DELEGA PARA O SEU BACKEND FAZER O TRABALHO PESADO ---
        try {
            aulaController.gerarGrade(turmaSelecionada, cargaParaGerar);
        } catch (Exception ex) {
            // Vai estourar aqui se não tiver professor, ou se der conflito!
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Falha ao Gerar Grade", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this,
                "✅ Grade gerada e salva no banco de dados com sucesso!\nTurma: " + turmaSelecionada.nome() + "  |  Total de aulas: " + totalAulas,
                "Grade Gerada", JOptionPane.INFORMATION_MESSAGE);

        ContentPanel contentPanel = (ContentPanel) SwingUtilities.getAncestorOfClass(ContentPanel.class, this);
        if (contentPanel != null) {
            contentPanel.exibirGradeDaTurma(turmaSelecionada);
        }
    }
}