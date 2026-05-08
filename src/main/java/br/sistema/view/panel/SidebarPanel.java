package br.sistema.view.panel;

import javax.swing.*;
import java.awt.*;



public class SidebarPanel extends  JPanel {

    public SidebarPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(250, 720));
        setBackground(new Color(18, 18, 24));

        JLabel lblLogoTxt = new JLabel("SGDG");
        lblLogoTxt.setFont(new Font("Arial", Font.BOLD, 24));
        lblLogoTxt.setHorizontalAlignment(JLabel.CENTER);

        add(lblLogoTxt);

        JLabel lblLogoSubTxt = new JLabel("Sistema de Gerenciamento de Grade");
        lblLogoSubTxt.setFont(new Font("Arial", Font.ITALIC, 12));

        add(lblLogoSubTxt);


    }
}
