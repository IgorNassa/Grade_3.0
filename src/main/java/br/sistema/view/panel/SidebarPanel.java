package br.sistema.view.panel;

import br.sistema.view.component.MenuButton;
import br.sistema.view.util.AppTheme;
import br.sistema.view.util.IconUtil;
import br.sistema.view.frame.LoginScreen;

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
    private MenuButton btnLogout;

    // Elementos adicionados para minimização
    private JLabel lblLogo;
    private JLabel lblNome;
    private JLabel lblSistema;
    private JLabel lblVersao;
    private Icon logoIconOriginal;
    private final java.util.List<JLabel> secoesLabels = new java.util.ArrayList<>();
    private Component strutLogoNome;
    private Component strutNomeSeparador;
    private JSeparator footerSep;
    private Component footerStrut;
    private boolean minimizado = false;

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
        setMinimumSize(new Dimension(LARGURA, 0));
        setMaximumSize(new Dimension(LARGURA, Integer.MAX_VALUE));
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

        // Ícone logo (tenta carregar, usa SVG do login, usa emoji fallback se falhar)
        lblLogo = new JLabel();
        try {
            logoIconOriginal = new com.formdev.flatlaf.extras.FlatSVGIcon("icons/fundoFlavioWarken.svg", 64, 64);
            lblLogo.setIcon(logoIconOriginal);
        } catch (Exception ignored) {
            try {
                logoIconOriginal = IconUtil.carregarIcone("/icons/logo.png", 64, 64);
                lblLogo.setIcon(logoIconOriginal);
            } catch (Exception ignored2) {
                lblLogo = new JLabel("🎓");
                lblLogo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
            }
        }
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(lblLogo);

        strutLogoNome = Box.createVerticalStrut(14);
        add(strutLogoNome);

        lblNome = new JLabel("PROF. FLÁVIO WARKEN");
        lblNome.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblNome.setForeground(AppTheme.TEXT_PRIMARY);
        lblNome.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(lblNome);

        lblSistema = new JLabel("Sistema de Grade Escolar");
        lblSistema.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblSistema.setForeground(AppTheme.TEXT_MUTED);
        lblSistema.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(lblSistema);

        strutNomeSeparador = Box.createVerticalStrut(28);
        add(strutNomeSeparador);

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

        add(Box.createVerticalStrut(20));
        add(secaoLabel("SAIR"));
        add(Box.createVerticalStrut(4));

        btnLogout = new MenuButton("Logout", new LogoutIcon(), false);
        add(btnLogout);
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
        secoesLabels.add(lbl);
        return lbl;
    }

    // ── Rodapé ────────────────────────────────────────────────────────────────
    private void montarRodape() {
        add(Box.createVerticalGlue());

        footerSep = new JSeparator() {
            @Override public Dimension getMaximumSize() { return new Dimension(Integer.MAX_VALUE, 1); }
        };
        footerSep.setForeground(AppTheme.BORDER_COLOR);
        add(footerSep);

        footerStrut = Box.createVerticalStrut(12);
        add(footerStrut);

        lblVersao = new JLabel("SGDG v3.0  •  Flávio Warken");
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
        btnLogout.addActionListener(e -> {
            Window parentWindow = SwingUtilities.getWindowAncestor(this);
            if (parentWindow != null) {
                parentWindow.dispose();
            }
            new LoginScreen();
        });
    }

    private void ativar(MenuButton botao) {
        MenuButton[] todos = {btnDashboard, btnProfessores, btnDisciplinas, btnTurmas,
                              btnTurnos, btnGerarGrade, btnVisualizar};
        for (MenuButton b : todos) b.setAtivo(false);
        botao.setAtivo(true);
    }

    public boolean isMinimizado() {
        return minimizado;
    }

    public void toggleMinimizado() {
        setMinimizado(!this.minimizado);
    }

    public void setMinimizado(boolean minimizado) {
        this.minimizado = minimizado;
        
        int larguraAtual = minimizado ? 64 : LARGURA;
        setPreferredSize(new Dimension(larguraAtual, 0));
        setMinimumSize(new Dimension(larguraAtual, 0));
        setMaximumSize(new Dimension(larguraAtual, Integer.MAX_VALUE));

        // Toggle visibility/size of header components
        if (lblNome != null) lblNome.setVisible(!minimizado);
        if (lblSistema != null) lblSistema.setVisible(!minimizado);
        if (strutLogoNome != null) strutLogoNome.setVisible(!minimizado);
        if (strutNomeSeparador != null) strutNomeSeparador.setVisible(!minimizado);
        
        // Hide section titles
        for (JLabel lbl : secoesLabels) {
            lbl.setVisible(!minimizado);
        }
        
        // Hide version footer
        if (footerSep != null) footerSep.setVisible(!minimizado);
        if (footerStrut != null) footerStrut.setVisible(!minimizado);
        if (lblVersao != null) lblVersao.setVisible(!minimizado);

        // Adjust logo icon size/emoji font size
        if (lblLogo != null) {
            if (logoIconOriginal != null) {
                try {
                    if (logoIconOriginal instanceof com.formdev.flatlaf.extras.FlatSVGIcon) {
                        lblLogo.setIcon(((com.formdev.flatlaf.extras.FlatSVGIcon) logoIconOriginal).derive(minimizado ? 32 : 64, minimizado ? 32 : 64));
                    } else {
                        lblLogo.setIcon(IconUtil.carregarIcone("/icons/logo.png", minimizado ? 32 : 64, minimizado ? 32 : 64));
                    }
                } catch (Exception ignored) {}
            } else {
                lblLogo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, minimizado ? 24 : 40));
            }
        }

        // Toggle buttons minimized state
        if (btnDashboard != null) btnDashboard.setMinimizado(minimizado);
        if (btnProfessores != null) btnProfessores.setMinimizado(minimizado);
        if (btnDisciplinas != null) btnDisciplinas.setMinimizado(minimizado);
        if (btnTurmas != null) btnTurmas.setMinimizado(minimizado);
        if (btnTurnos != null) btnTurnos.setMinimizado(minimizado);
        if (btnGerarGrade != null) btnGerarGrade.setMinimizado(minimizado);
        if (btnVisualizar != null) btnVisualizar.setMinimizado(minimizado);
        if (btnLogout != null) btnLogout.setMinimizado(minimizado);

        revalidate();
        repaint();
    }

    // Ícone de Logout desenhado dinamicamente com traçado vetorial de alta definição
    private static class LogoutIcon implements Icon {
        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c.getForeground());
            g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            // Desenhar o colchete da porta (lado esquerdo)
            g2.drawLine(x + 11, y + 3, x + 4, y + 3);
            g2.drawLine(x + 4, y + 3, x + 4, y + 17);
            g2.drawLine(x + 4, y + 17, x + 11, y + 17);

            // Desenhar a seta saindo da porta para a direita
            g2.drawLine(x + 7, y + 10, x + 16, y + 10);
            g2.drawLine(x + 13, y + 7, x + 16, y + 10);
            g2.drawLine(x + 13, y + 13, x + 16, y + 10);

            g2.dispose();
        }

        @Override
        public int getIconWidth() { return 20; }
        @Override
        public int getIconHeight() { return 20; }
    }
}
