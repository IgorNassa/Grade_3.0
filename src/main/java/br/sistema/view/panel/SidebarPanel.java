package br.sistema.view.panel;

import br.sistema.view.component.MenuButton;
import br.sistema.view.util.AppTheme;
import br.sistema.view.util.IconUtil;

import javax.swing.*;
import java.awt.*;

/**
 * Sidebar com navegação completa — tema dark consistente com o LoginScreen.
 */
public class SidebarPanel extends JPanel {

    private static final int LARGURA = 248;
    private final ContentPanel contentPanel;

    // Botões de menu
    private MenuButton btnDashboard;
    private MenuButton btnProfessores;
    private MenuButton btnDisciplinas;
    private MenuButton btnTurmas;
    private MenuButton btnTurnos;
    private MenuButton btnGerarGrade;
    private MenuButton btnVisualizar;

    public SidebarPanel(ContentPanel contentPanel) {
        this.contentPanel = contentPanel;
        configurar();
        montarTopo();
        montarMenu();
        montarRodape();
        configurarEventos();
    }

    private void configurar() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(LARGURA, 0));
        setBackground(AppTheme.BG_SIDEBAR);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Linha separadora direita
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(AppTheme.BORDER_COLOR);
        g2.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight());
        g2.dispose();
    }

    // ── Topo: Logo + Nome ──────────────────────────────────────────────────────
    private void montarTopo() {
        add(Box.createVerticalStrut(28));

        // Ícone logo (tenta carregar, usa emoji fallback)
        JLabel lblLogo = new JLabel();
        try {
            lblLogo = new JLabel(IconUtil.carregarIcone("/icons/logo.png", 64, 64));
        } catch (Exception ignored) {
            lblLogo = new JLabel("🎓");
            lblLogo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        }
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(lblLogo);

        add(Box.createVerticalStrut(14));

        JLabel lblNome = new JLabel("PROF. FLÁVIO WARKEN");
        lblNome.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblNome.setForeground(AppTheme.TEXT_PRIMARY);
        lblNome.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(lblNome);

        JLabel lblSistema = new JLabel("Sistema de Grade Escolar");
        lblSistema.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblSistema.setForeground(AppTheme.TEXT_MUTED);
        lblSistema.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(lblSistema);

        add(Box.createVerticalStrut(28));

        // Separador
        JSeparator sep = new JSeparator() {
            @Override public Dimension getMaximumSize() { return new Dimension(Integer.MAX_VALUE, 1); }
        };
        sep.setForeground(AppTheme.BORDER_COLOR);
        sep.setBackground(AppTheme.BORDER_COLOR);
        add(sep);

        add(Box.createVerticalStrut(16));
    }

    // ── Menu ──────────────────────────────────────────────────────────────────
    private void montarMenu() {
        // Dashboard
        btnDashboard = new MenuButton("Dashboard", iconSeguro("/icons/book.png"), true);
        add(btnDashboard);

        add(Box.createVerticalStrut(20));
        add(secaoLabel("CADASTROS"));
        add(Box.createVerticalStrut(4));

        btnProfessores = new MenuButton("Professores", iconSeguro("/icons/professor.png"), false);
        add(btnProfessores);
        add(Box.createVerticalStrut(4));

        btnDisciplinas = new MenuButton("Disciplinas", iconSeguro("/icons/disciplinas.png"), false);
        add(btnDisciplinas);
        add(Box.createVerticalStrut(4));

        btnTurmas = new MenuButton("Turmas", iconSeguro("/icons/professor.png"), false);
        add(btnTurmas);
        add(Box.createVerticalStrut(4));

        btnTurnos = new MenuButton("Turnos", iconSeguro("/icons/turnos.png"), false);
        add(btnTurnos);

        add(Box.createVerticalStrut(20));
        add(secaoLabel("GRADE"));
        add(Box.createVerticalStrut(4));

        btnGerarGrade = new MenuButton("Gerar Grade", iconSeguro("/icons/grade.png"), false);
        add(btnGerarGrade);
        add(Box.createVerticalStrut(4));

        btnVisualizar = new MenuButton("Visualizar Grade", iconSeguro("/icons/visualizar.png"), false);
        add(btnVisualizar);
    }

    private Icon iconSeguro(String path) {
        try { return IconUtil.carregarIcone(path, 20, 20); }
        catch (Exception e) { return null; }
    }

    private JLabel secaoLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lbl.setForeground(AppTheme.TEXT_MUTED);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl.setMaximumSize(new Dimension(210, 22));
        lbl.setPreferredSize(new Dimension(210, 22));
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 0));
        lbl.setHorizontalAlignment(SwingConstants.LEFT);
        return lbl;
    }

    // ── Rodapé ────────────────────────────────────────────────────────────────
    private void montarRodape() {
        add(Box.createVerticalGlue());

        JSeparator sep = new JSeparator() {
            @Override public Dimension getMaximumSize() { return new Dimension(Integer.MAX_VALUE, 1); }
        };
        sep.setForeground(AppTheme.BORDER_COLOR);
        add(sep);

        add(Box.createVerticalStrut(12));

        JLabel lblVersao = new JLabel("SGDG v3.0  •  Flávio Warken");
        lblVersao.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblVersao.setForeground(AppTheme.TEXT_MUTED);
        lblVersao.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(lblVersao);

        add(Box.createVerticalStrut(16));
    }

    // ── Eventos ───────────────────────────────────────────────────────────────
    private void configurarEventos() {
        btnDashboard.addActionListener(e -> {
            ativar(btnDashboard);
            contentPanel.mostrar(ContentPanel.DASHBOARD);
        });
        btnProfessores.addActionListener(e -> {
            ativar(btnProfessores);
            contentPanel.mostrar(ContentPanel.PROFESSORES);
        });
        btnDisciplinas.addActionListener(e -> {
            ativar(btnDisciplinas);
            contentPanel.mostrar(ContentPanel.DISCIPLINAS);
        });
        btnTurmas.addActionListener(e -> {
            ativar(btnTurmas);
            contentPanel.mostrar(ContentPanel.TURMAS);
        });
        btnTurnos.addActionListener(e -> {
            ativar(btnTurnos);
            contentPanel.mostrar(ContentPanel.TURNOS);
        });
        btnGerarGrade.addActionListener(e -> {
            ativar(btnGerarGrade);
            contentPanel.mostrar(ContentPanel.GERAR_GRADE);
        });
        btnVisualizar.addActionListener(e -> {
            ativar(btnVisualizar);
            contentPanel.mostrar(ContentPanel.VER_GRADE);
        });
    }

    private void ativar(MenuButton botao) {
        MenuButton[] todos = {btnDashboard, btnProfessores, btnDisciplinas, btnTurmas,
                              btnTurnos, btnGerarGrade, btnVisualizar};
        for (MenuButton b : todos) b.setAtivo(false);
        botao.setAtivo(true);
    }
}
