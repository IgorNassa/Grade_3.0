package br.sistema.view.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MenuButton extends JButton {
    private static final Color COR_BG = new Color(29, 19, 60);
    private static final Color COR_A = new Color(91, 28, 152);
    private static final Color COR_H = new Color(92, 51, 122);
    private static final Color COR_TXT= Color.WHITE;
    private static final int RAIO_BORDA = 14;

    private  boolean ativo;

    public MenuButton(String txt,Icon icone, boolean ativo){
        super(txt);
        this.ativo = ativo;

        setIcon(icone);
        setIconTextGap(10);
        setHorizontalAlignment(SwingConstants.LEFT);
        setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 0));

        configurarVisual();
        configurarHover();
    }

    private void configurarVisual(){
        setMaximumSize(new Dimension(215, 42));
        setPreferredSize(new Dimension(215, 42));
        setAlignmentX(Component.CENTER_ALIGNMENT);

        setFont(new Font("Segoe UI", Font.BOLD, 13));
        setForeground(COR_TXT);
        atualizarVisual();

        setHorizontalAlignment(SwingConstants.LEFT);
        setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 0));

        setFocusPainted(false);
        setContentAreaFilled(true);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);

    }

    private void configurarHover(){

       addMouseListener(new MouseAdapter() {
           @Override
           public void mouseEntered(MouseEvent e) {
               if (!ativo){
                   setBackground(COR_H);
               }
           }

           @Override
           public void mouseExited(MouseEvent e) {
               atualizarVisual();
           }
       });

    }

    private void atualizarVisual(){
        setBackground(ativo ? COR_A : COR_BG);
    }

    public void setAtivo(boolean ativo){
        this.ativo = ativo;
        atualizarVisual();
    }

    @Override
        protected  void paintComponent(Graphics g){
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
                RAIO_BORDA,
                RAIO_BORDA
        );
        g2.dispose();
        super.paintComponent(g);
    }
}
