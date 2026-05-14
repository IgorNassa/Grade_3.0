package br.sistema.view.frame;

import br.sistema.view.panel.ContentPanel;
import br.sistema.view.panel.HeaderPanel;
import br.sistema.view.panel.SidebarPanel;
import br.sistema.view.util.AppTheme;

import javax.swing.*;
import java.awt.*;

/**
 * Frame principal do sistema — integra Sidebar + Header + ContentPanel.
 * Tema dark consistente com o LoginScreen.
 */
public class DashFrame {

    private static final int LARGURA = 1280;
    private static final int ALTURA  = 760;

    private final JFrame frame = new JFrame("SGDG — Sistema de Grade Escolar");
    private final ContentPanel contentPanel = new ContentPanel();
    private final SidebarPanel sidebarPanel = new SidebarPanel(contentPanel);
    private final HeaderPanel headerPanel   = new HeaderPanel();

    public DashFrame() {
        configurarFrame();
        montarLayout();
        configurarEventos();
        frame.setVisible(true);
    }

    private void configurarFrame() {
        frame.setSize(LARGURA, ALTURA);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setBackground(AppTheme.BG_MAIN);

        // Aplica look and feel escuro ao JOptionPane
        UIManager.put("OptionPane.background",         AppTheme.BG_CARD);
        UIManager.put("Panel.background",              AppTheme.BG_CARD);
        UIManager.put("OptionPane.messageForeground",  AppTheme.TEXT_PRIMARY);
        UIManager.put("Button.background",             AppTheme.PURPLE);
        UIManager.put("Button.foreground",             Color.WHITE);
    }

    private void montarLayout() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AppTheme.BG_MAIN);

        // Painel central (header + conteúdo)
        JPanel centro = new JPanel(new BorderLayout());
        centro.setBackground(AppTheme.BG_CONTENT);
        centro.add(headerPanel, BorderLayout.NORTH);
        centro.add(contentPanel, BorderLayout.CENTER);

        root.add(sidebarPanel, BorderLayout.WEST);
        root.add(centro, BorderLayout.CENTER);

        frame.setContentPane(root);
    }

    private void configurarEventos() {
        headerPanel.getBtnMenu().addActionListener(e -> {
            sidebarPanel.setVisible(!sidebarPanel.isVisible());
            frame.revalidate();
            frame.repaint();
        });
    }
}