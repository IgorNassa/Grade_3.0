package br.sistema.view.panel;

import br.sistema.view.util.IconUtil;

import javax.swing.*;
import java.awt.*;


public class HeaderPanel extends  JPanel{
    private JButton btnMenu;
    private static final int ALTURA_HEADER = 70;

    private static final Color COR_BG = Color.WHITE;
    private static final Color COR_TXT = new Color(91, 28, 152);
    private static final Color COR_BORDA = new Color(230, 230, 235);

    public HeaderPanel(){
        configurarHeader();
        montarConteudo();
    }

    private void configurarHeader(){
        setPreferredSize(new Dimension(0, ALTURA_HEADER));
        setBackground(COR_BG);
        setLayout(new BorderLayout());
    }

    public JButton getBtnMenu() {
        return btnMenu;
    }

    private void montarConteudo(){

        JPanel painelEsquerdo = new JPanel();
        painelEsquerdo.setOpaque(false);
        painelEsquerdo.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 0));

        btnMenu = new JButton(
                IconUtil.carregarIcone(
                        "/icons/menu.png",
                        24,
                        24
                )
        );
        btnMenu.setForeground(COR_TXT);
        btnMenu.setBorderPainted(false);
        btnMenu.setContentAreaFilled(false);
        btnMenu.setFocusPainted(false);
        btnMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));



        JLabel lblTitulo = new JLabel("Dashboard");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(COR_TXT);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 24, 0, 0));

        painelEsquerdo.add(btnMenu);
        painelEsquerdo.add(lblTitulo);

        add(painelEsquerdo, BorderLayout.WEST);

    }
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        g.setColor(COR_BORDA);
        g.drawLine(0,getHeight() - 1, getWidth(), getHeight() - 1 );
    }
}