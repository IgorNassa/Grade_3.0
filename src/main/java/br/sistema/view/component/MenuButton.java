package br.sistema.view.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MenuButton extends JButton {
    private static final Color COR_BG = new Color(28, 17, 41);
    private static final Color COR_A = new Color(123, 63, 125);
    private static final Color COR_H = new Color(92, 51, 122);
    private static final Color COR_TXT= Color.WHITE;

    private  boolean ativo;

    public MenuButton(String txt,boolean ativo){
        super(txt);
        this.ativo = ativo;

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
        setBorder(BorderFactory.createEmptyBorder(0, 22, 0, 0));

        setFocusPainted(false);
        setContentAreaFilled(true);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

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
}
