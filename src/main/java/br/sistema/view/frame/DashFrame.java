package br.sistema.view.frame;

import br.sistema.view.panel.SidebarPanel;

import javax.swing.*;
import java.awt.*;

public class DashFrame {

    private final JFrame dashFrame = new JFrame("Dashboard");
    private final SidebarPanel sidebarPanel = new SidebarPanel();

    public DashFrame(){
        dashFrame.setSize(1250,720);
        dashFrame.setLocationRelativeTo(null);
        dashFrame.setLayout(new BorderLayout());

        dashFrame.add(sidebarPanel, BorderLayout.WEST);

        dashFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        dashFrame.setVisible(true);
    }
}
