package br.sistema.view.frame;

import br.sistema.view.panel.ContentPanel;
import br.sistema.view.panel.HeaderPanel;
import br.sistema.view.panel.SidebarPanel;

import javax.swing.*;
import java.awt.*;

public class DashFrame {

    private static final int LARGURA_FRAME = 1250;
    private static final int ALTURA_FRAME = 720;

    private final JFrame dashFrame = new JFrame("Dashboard");
    private final ContentPanel contentPanel = new ContentPanel();
    private final SidebarPanel sidebarPanel = new SidebarPanel(contentPanel);
    private final HeaderPanel headerPanel = new HeaderPanel();
    private final JPanel mainPanel = new JPanel();

    public DashFrame() {
        configurarFrame();
        montarLayout();
        configurarEventos();

        dashFrame.setVisible(true);
    }

    private void configurarFrame() {
        dashFrame.setSize(LARGURA_FRAME, ALTURA_FRAME);
        dashFrame.setLocationRelativeTo(null);
        dashFrame.setLayout(new BorderLayout());
        dashFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void montarLayout() {
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        dashFrame.add(sidebarPanel, BorderLayout.WEST);
        dashFrame.add(mainPanel, BorderLayout.CENTER);
    }

    private void configurarEventos() {
        headerPanel.getBtnMenu().addActionListener(e -> alternarSidebar());
    }

    private void alternarSidebar() {
        boolean visivel = sidebarPanel.isVisible();

        sidebarPanel.setVisible(!visivel);

        dashFrame.revalidate();
        dashFrame.repaint();
    }
}