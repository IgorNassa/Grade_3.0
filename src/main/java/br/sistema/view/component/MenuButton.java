package br.sistema.view.component;

import br.sistema.view.util.AppTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Botão do menu lateral — usa AppTheme para consistência visual.
 */
public class MenuButton extends JButton {

    private static final int RAIO_BORDA = 10;

    private boolean ativo;
    private boolean hover;

    public MenuButton(String txt, Icon icone, boolean ativo) {
        super(txt);
        this.ativo = ativo;

        if (icone != null) {
            setIcon(icone);
            setIconTextGap(10);
        }
        setHorizontalAlignment(SwingConstants.LEFT);
        setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 0));

        configurarVisual();
        configurarHover();
    }

    private void configurarVisual() {
        setMaximumSize(new Dimension(220, 42));
        setPreferredSize(new Dimension(220, 42));
        setAlignmentX(Component.CENTER_ALIGNMENT);

        setFont(new Font("Segoe UI", Font.BOLD, 13));
        setForeground(AppTheme.TEXT_PRIMARY);

        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void configurarHover() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hover = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
                repaint();
            }
        });
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (ativo) {
            // Fundo ativo com roxo sólido
            g2.setColor(AppTheme.PURPLE_DARK);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), RAIO_BORDA, RAIO_BORDA);
            // Barra lateral indicadora
            g2.setColor(AppTheme.PURPLE);
            g2.fillRoundRect(0, 6, 4, getHeight() - 12, 4, 4);
        } else if (hover) {
            // Fundo hover sutil
            g2.setColor(AppTheme.PURPLE_SUBTLE);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), RAIO_BORDA, RAIO_BORDA);
        } else {
            // Transparente quando inativo
            g2.setColor(new Color(0, 0, 0, 0));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), RAIO_BORDA, RAIO_BORDA);
        }

        g2.dispose();

        // Ajusta cor do texto conforme estado
        setForeground(ativo ? Color.WHITE : hover ? AppTheme.TEXT_PRIMARY : AppTheme.TEXT_SECONDARY);

        super.paintComponent(g);
    }
}
