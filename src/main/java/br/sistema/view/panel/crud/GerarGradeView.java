package br.sistema.view.panel.crud;

import br.sistema.view.util.AppTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Tela de Geração de Grade — permite configurar carga horária e disparar o gerador.
 */
public class GerarGradeView extends JPanel {

    private JComboBox<String> cmbTurma;
    private JComboBox<String> cmbTurno;

    // Campos de carga horária por disciplina
    private final String[] DISCIPLINAS = {
        "Matemática", "Português", "História", "Geografia",
        "Física", "Química", "Biologia", "Inglês", "Arte", "Ed. Física"
    };
    private final JTextField[] txtCarga = new JTextField[DISCIPLINAS.length];

    public GerarGradeView() {
        setLayout(new BorderLayout());
        setBackground(AppTheme.BG_CONTENT);
        setBorder(new EmptyBorder(32, 32, 32, 32));

        add(montarTopo(), BorderLayout.NORTH);
        add(montarCorpo(), BorderLayout.CENTER);
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
        cmbTurma = AppTheme.styledCombo(new String[]{"1A", "1B", "2A", "2B", "3A"});
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

        // Título
        JLabel tituloCard = new JLabel("Carga Horária Semanal (aulas por semana)");
        tituloCard.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tituloCard.setForeground(AppTheme.TEXT_PRIMARY);
        tituloCard.setBorder(new EmptyBorder(0, 0, 16, 0));
        card.add(tituloCard, BorderLayout.NORTH);

        // Grid de disciplinas
        JPanel grid = new JPanel(new GridLayout(0, 2, 20, 12));
        grid.setOpaque(false);

        for (int i = 0; i < DISCIPLINAS.length; i++) {
            JPanel linha = new JPanel(new BorderLayout(12, 0));
            linha.setOpaque(false);

            JLabel lblDisc = new JLabel(DISCIPLINAS[i]);
            lblDisc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            lblDisc.setForeground(AppTheme.TEXT_PRIMARY);
            lblDisc.setPreferredSize(new Dimension(180, 38));

            txtCarga[i] = AppTheme.styledField("0", 80);
            txtCarga[i].setText("2");

            linha.add(lblDisc, BorderLayout.WEST);
            linha.add(txtCarga[i], BorderLayout.CENTER);
            grid.add(linha);
        }

        card.add(grid, BorderLayout.CENTER);

        // Botão gerar
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

    private void gerarGrade() {
        String turma = (String) cmbTurma.getSelectedItem();
        String turno = (String) cmbTurno.getSelectedItem();

        // Valida carga
        int totalAulas = 0;
        for (int i = 0; i < DISCIPLINAS.length; i++) {
            try {
                int qtd = Integer.parseInt(txtCarga[i].getText().trim());
                if (qtd < 0) {
                    JOptionPane.showMessageDialog(this,
                        "A carga de \"" + DISCIPLINAS[i] + "\" não pode ser negativa.",
                        "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                totalAulas += qtd;
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "Valor inválido em \"" + DISCIPLINAS[i] + "\". Use apenas números inteiros.",
                    "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        if (totalAulas == 0) {
            JOptionPane.showMessageDialog(this,
                "Defina ao menos uma aula para alguma disciplina.",
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Simula processamento
        JDialog loading = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Processando...", true);
        loading.setUndecorated(true);
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(AppTheme.BG_CARD);
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));
        JLabel msg = new JLabel("⚡ Gerando grade para " + turma + " – " + turno + "...");
        msg.setForeground(AppTheme.TEXT_PRIMARY);
        msg.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(msg, BorderLayout.CENTER);
        loading.setContentPane(panel);
        loading.pack();
        loading.setLocationRelativeTo(this);

        final int totalAulasFinal = totalAulas;
        Timer timer = new Timer(1800, e -> {
            loading.dispose();
            JOptionPane.showMessageDialog(this,
                "✅ Grade gerada com sucesso!\nTurma: " + turma + "  |  Turno: " + turno +
                "\nTotal de aulas semanais: " + totalAulasFinal,
                "Grade Gerada", JOptionPane.INFORMATION_MESSAGE);
        });
        timer.setRepeats(false);
        timer.start();
        loading.setVisible(true);
    }
}
