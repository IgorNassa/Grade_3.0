package br.sistema.view.component;

import br.sistema.view.util.AppTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Barra de título customizada — integrada ao tema dark/purple do SGDG.
 *
 * Cuidados especiais para evitar problemas comuns em undecorated frames:
 *  - Ícones pintados via Graphics2D (não dependem de recursos externos).
 *  - Cores explícitas em todo repaint — sem depender de L&F herdado.
 *  - Hover detectado por MouseAdapter dedicado por botão.
 *  - Suporte a maximize/restore com detecção de estado do frame.
 *  - Arrasto funciona em qualquer resolução; ignora cliques nos botões.
 *  - setOpaque(true) em todos os painéis internos para evitar flickering.
 */
public class CustomTitleBar extends JPanel {

    // ── Dimensões ──────────────────────────────────────────────────────────
    private static final int HEIGHT      = 40;
    private static final int BTN_W       = 46;
    private static final int BTN_H       = 40;
    private static final int ICON_RADIUS = 6; // raio do X / _ / □

    // ── Estado ─────────────────────────────────────────────────────────────
    private final JFrame frame;
    private int dragOffsetX, dragOffsetY;
    private boolean dragging = false;

    // Referência ao botão de max/restore para atualizar o ícone
    private final TitleButton btnMaximize;

    public CustomTitleBar(JFrame frame, String appTitle) {
        this.frame = frame;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0, HEIGHT));
        setBackground(AppTheme.BG_SIDEBAR);
        setOpaque(true);

        add(buildLeft(appTitle), BorderLayout.WEST);

        // Cria btnMaximize aqui para poder passar referência
        btnMaximize = new TitleButton(TitleButton.Kind.MAXIMIZE);
        btnMaximize.addActionListener(e -> toggleMaximize());

        add(buildRight(), BorderLayout.EAST);

        // Borda inferior separando da área de conteúdo
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.BORDER_COLOR));

        // ── Arrasto ──────────────────────────────────────────────────────
        MouseAdapter drag = new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    dragging = true;
                    dragOffsetX = e.getX();
                    dragOffsetY = e.getY();
                }
            }
            @Override public void mouseDragged(MouseEvent e) {
                if (dragging && (frame.getExtendedState() & JFrame.MAXIMIZED_BOTH) == 0) {
                    frame.setLocation(
                        e.getXOnScreen() - dragOffsetX,
                        e.getYOnScreen() - dragOffsetY
                    );
                }
            }
            @Override public void mouseReleased(MouseEvent e) { dragging = false; }
        };
        addMouseListener(drag);
        addMouseMotionListener(drag);

        // Duplo-clique na barra = maximizar/restaurar
        addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e))
                    toggleMaximize();
            }
        });

        // Escuta mudanças de estado para manter ícone de max/restore correto
        frame.addWindowStateListener(e -> SwingUtilities.invokeLater(btnMaximize::repaint));
    }

    // ── Lado esquerdo: logo + título ───────────────────────────────────────
    private JPanel buildLeft(String title) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        p.setOpaque(false);


        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitle.setForeground(AppTheme.TEXT_PRIMARY);
        p.add(lblTitle);

        return p;
    }

    // ── Lado direito: minimizar / maximizar / fechar ───────────────────────
    private JPanel buildRight() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        p.setOpaque(false);

        TitleButton btnMin   = new TitleButton(TitleButton.Kind.MINIMIZE);
        TitleButton btnClose = new TitleButton(TitleButton.Kind.CLOSE);

        btnMin.addActionListener(e -> frame.setState(JFrame.ICONIFIED));
        btnClose.addActionListener(e -> {
            // Dispara o windowClosing para respeitar a lógica do NavigationController
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        });

        p.add(btnMin);
        p.add(btnMaximize);
        p.add(btnClose);
        return p;
    }

    private void toggleMaximize() {
        if ((frame.getExtendedState() & JFrame.MAXIMIZED_BOTH) != 0) {
            frame.setExtendedState(JFrame.NORMAL);
        } else {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
        btnMaximize.repaint();
    }

    // ══════════════════════════════════════════════════════════════════════
    // Botão individual da title bar
    // ══════════════════════════════════════════════════════════════════════
    private class TitleButton extends JButton {

        enum Kind { MINIMIZE, MAXIMIZE, CLOSE }

        private final Kind kind;
        private boolean hover = false;

        TitleButton(Kind kind) {
            this.kind = kind;
            setPreferredSize(new Dimension(BTN_W, BTN_H));
            setMinimumSize(new Dimension(BTN_W, BTN_H));
            setMaximumSize(new Dimension(BTN_W, BTN_H));

            // Transparente por padrão — pintamos tudo manualmente
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(Cursor.getDefaultCursor());

            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { hover = true;  repaint(); }
                @Override public void mouseExited (MouseEvent e) { hover = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,  RenderingHints.VALUE_STROKE_PURE);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING,       RenderingHints.VALUE_RENDER_QUALITY);

            int w = getWidth(), h = getHeight();

            // ── Fundo hover ──────────────────────────────────────────────
            if (hover) {
                Color bg = (kind == Kind.CLOSE)
                    ? new Color(200, 50, 50, 200)       // vermelho para fechar
                    : new Color(AppTheme.PURPLE_SUBTLE.getRed(),
                                AppTheme.PURPLE_SUBTLE.getGreen(),
                                AppTheme.PURPLE_SUBTLE.getBlue(), 180);
                g2.setColor(bg);
                g2.fillRect(0, 0, w, h);
            }

            // ── Ícone: SEMPRE branco, sempre visível ─────────────────────
            // Usamos cor com alpha 255 e stroke explícito para não desaparecer.
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            int cx = w / 2, cy = h / 2, r = ICON_RADIUS;

            switch (kind) {
                case MINIMIZE -> {
                    // Linha horizontal simples (underline)
                    g2.drawLine(cx - r, cy + 3, cx + r, cy + 3);
                }
                case MAXIMIZE -> {
                    boolean maximized = (frame.getExtendedState() & JFrame.MAXIMIZED_BOTH) != 0;
                    if (maximized) {
                        // Ícone "restaurar": dois retângulos sobrepostos
                        g2.drawRect(cx - r + 2, cy - r, r * 2 - 2, r * 2 - 2);
                        g2.drawRect(cx - r,     cy - r + 2, r * 2 - 2, r * 2 - 2);
                    } else {
                        // Ícone "maximizar": retângulo simples
                        g2.drawRect(cx - r, cy - r, r * 2, r * 2);
                    }
                }
                case CLOSE -> {
                    // X
                    g2.drawLine(cx - r, cy - r, cx + r, cy + r);
                    g2.drawLine(cx + r, cy - r, cx - r, cy + r);
                }
            }

            g2.dispose();
            // NÃO chama super.paintComponent() — pintamos tudo manualmente
            // para evitar que o L&F sobrescreva nossas cores
        }
    }
}
