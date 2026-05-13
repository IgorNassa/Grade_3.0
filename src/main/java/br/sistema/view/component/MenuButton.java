package br.sistema.view.component;

import br.sistema.view.theme.ThemeColors;
import br.sistema.view.theme.ThemeDimensions;
import br.sistema.view.theme.ThemeFonts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MenuButton extends JButton {

    private boolean ativo;

    public MenuButton(String texto, Icon icone, boolean ativo) {
        super(texto);

        this.ativo = ativo;

        setIcon(icone);

        configurarVisual();
        configurarHover();
    }

    private void configurarVisual() {
        setMaximumSize(new Dimension(
                ThemeDimensions.MENU_WIDTH,
                ThemeDimensions.MENU_HEIGHT
        ));

        setPreferredSize(new Dimension(
                ThemeDimensions.MENU_WIDTH,
                ThemeDimensions.MENU_HEIGHT
        ));

        setAlignmentX(Component.CENTER_ALIGNMENT);

        setFont(ThemeFonts.MENU);
        setForeground(ThemeColors.TEXT);

        setHorizontalAlignment(SwingConstants.LEFT);
        setIconTextGap(10);

        setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 0));

        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);

        setCursor(new Cursor(Cursor.HAND_CURSOR));

        atualizarVisual();
    }

    private void configurarHover() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!ativo) {
                    setBackground(ThemeColors.PURPLE_HOVER);
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                atualizarVisual();
            }
        });
    }

    private void atualizarVisual() {
        setBackground(
                ativo
                        ? ThemeColors.PURPLE
                        : new Color(0, 0, 0, 0)
        );

        repaint();
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
        atualizarVisual();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.setColor(getBackground());

        g2.fillRoundRect(
                0,
                0,
                getWidth(),
                getHeight(),
                ThemeDimensions.MENU_RADIUS,
                ThemeDimensions.MENU_RADIUS
        );

        g2.dispose();

        super.paintComponent(g);
    }
}