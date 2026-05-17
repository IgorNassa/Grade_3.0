package br.sistema.view.panel;

import br.sistema.view.util.AppTheme;
import br.sistema.view.util.IconUtil;

import javax.swing.*;
import java.awt.*;

/**
 * Header do dashboard — tema dark.
 * Mostra o botão de hamburger, título da tela atual e usuário logado.
 */
public class HeaderPanel extends JPanel {

    private JButton btnMenu;
    private JLabel lblTitulo;

    private static final int ALTURA = 64;

    public HeaderPanel() {
        configurar();
        montarConteudo();
    }

    private void configurar() {
        setPreferredSize(new Dimension(0, ALTURA));
        setBackground(AppTheme.BG_SIDEBAR);
        setLayout(new BorderLayout());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(AppTheme.BORDER_COLOR);
        g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
        g2.dispose();
    }

    private void montarConteudo() {
        // ── Esquerda: hamburger + título ────────────────────────────────────
        JPanel esquerda = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        esquerda.setOpaque(false);
        esquerda.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 0));

        btnMenu = criarBtnMenu();
        esquerda.add(btnMenu);

        lblTitulo = new JLabel("Dashboard");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(AppTheme.TEXT_PRIMARY);
        esquerda.add(lblTitulo);

        add(esquerda, BorderLayout.WEST);

        // ── Direita: usuário ────────────────────────────────────────────────
        JPanel direita = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        direita.setOpaque(false);

        // Badge do usuário
        JPanel badge = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        badge.setOpaque(false);

        JLabel avatar = new JLabel("👤");
        avatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        JLabel lblNome = new JLabel("Administrador");
        lblNome.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblNome.setForeground(AppTheme.TEXT_PRIMARY);

        JLabel lblRole = new JLabel("admin@sgdg");
        lblRole.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblRole.setForeground(AppTheme.TEXT_MUTED);

        info.add(lblNome);
        info.add(lblRole);

        badge.add(avatar);
        badge.add(info);
        direita.add(badge);

        add(direita, BorderLayout.EAST);
    }

    private JButton criarBtnMenu() {
        JButton btn = new JButton() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? AppTheme.TEXT_PRIMARY : AppTheme.TEXT_SECONDARY);
                g2.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int cx = getWidth() / 2;
                int cy = getHeight() / 2;
                g2.drawLine(cx - 9, cy - 6, cx + 9, cy - 6);
                g2.drawLine(cx - 9, cy,     cx + 9, cy);
                g2.drawLine(cx - 9, cy + 6, cx + 9, cy + 6);
                g2.dispose();
            }
        };
        btn.setPreferredSize(new Dimension(40, 40));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public JButton getBtnMenu() { return btnMenu; }

    public void setTitulo(String titulo) {
        lblTitulo.setText(titulo);
    }
}