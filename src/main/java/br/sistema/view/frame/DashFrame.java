package br.sistema.view.frame;

import br.sistema.view.component.CustomTitleBar;
import br.sistema.view.panel.ContentPanel;
import br.sistema.view.panel.HeaderPanel;
import br.sistema.view.panel.SidebarPanel;
import br.sistema.view.util.AppTheme;
import br.sistema.view.util.NavigationController;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Frame principal do SGDG.
 *
 * Implementa barra de título customizada (CustomTitleBar) integrada
 * ao tema dark/purple, com botões minimizar / maximizar / fechar
 * sempre visíveis e sem flickering.
 *
 * Cuidados especiais:
 *  - setUndecorated(true) ANTES de qualquer pack()/setVisible().
 *  - Root panel com fundo sólido (sem transparência) para evitar
 *    artefatos visuais em maximize/fullscreen.
 *  - CustomTitleBar trata windowClosing via dispatchEvent, respeitando
 *    a lógica do NavigationController.
 */
public class DashFrame { // Cache refresh

    private static final int LARGURA = 1280;
    private static final int ALTURA  = 760;

    private final JFrame       frame        = new JFrame("SGDG — Sistema de Grade Escolar");
    private final ContentPanel contentPanel = new ContentPanel();
    private final SidebarPanel sidebarPanel = new SidebarPanel(contentPanel);
    private final HeaderPanel  headerPanel  = new HeaderPanel();

    public DashFrame() {
        aplicarTemaFlatLaf();
        NavigationController.getInstance().registrar(contentPanel, frame);
        configurarFrame();
        montarLayout();
        configurarEventos();
        frame.setVisible(true);
    }

    // ── Tema FlatLaf + UIManager ──────────────────────────────────────────
    public static void aplicarTemaFlatLaf() {
        try {
            FlatDarkLaf.setup();
        } catch (Exception e) {
            System.err.println("[DashFrame] FlatLaf não disponível: " + e.getMessage());
        }

        UIManager.put("Panel.background",               AppTheme.BG_CONTENT);
        UIManager.put("Frame.background",               AppTheme.BG_MAIN);

        UIManager.put("OptionPane.background",          AppTheme.BG_CARD);
        UIManager.put("OptionPane.messageForeground",   AppTheme.TEXT_PRIMARY);

        UIManager.put("Button.background",              AppTheme.PURPLE_DARK);
        UIManager.put("Button.foreground",              Color.WHITE);
        UIManager.put("Button.focusedBackground",       AppTheme.PURPLE);
        UIManager.put("Button.hoverBackground",         AppTheme.PURPLE_HOVER);
        UIManager.put("Button.arc",                     10);
        UIManager.put("Button.borderWidth",             0);

        UIManager.put("TextField.background",           AppTheme.BG_FIELD);
        UIManager.put("TextField.foreground",           AppTheme.TEXT_PRIMARY);
        UIManager.put("TextField.caretForeground",      AppTheme.TEXT_PRIMARY);
        UIManager.put("TextField.selectionBackground",  AppTheme.PURPLE_DARK);
        UIManager.put("TextField.border",               BorderFactory.createLineBorder(AppTheme.BORDER_COLOR));
        UIManager.put("ComboBox.background",            AppTheme.BG_FIELD);
        UIManager.put("ComboBox.foreground",            AppTheme.TEXT_PRIMARY);
        UIManager.put("ComboBox.selectionBackground",   AppTheme.PURPLE_DARK);
        UIManager.put("ComboBox.selectionForeground",   Color.WHITE);
        UIManager.put("ComboBox.buttonBackground",      AppTheme.BG_FIELD);
        UIManager.put("ComboBox.arc",                   8);

        UIManager.put("Table.background",               AppTheme.BG_TABLE_ROW);
        UIManager.put("Table.foreground",               AppTheme.TEXT_PRIMARY);
        UIManager.put("Table.selectionBackground",      AppTheme.BG_TABLE_SEL);
        UIManager.put("Table.selectionForeground",      Color.WHITE);
        UIManager.put("Table.gridColor",                AppTheme.BORDER_COLOR);
        UIManager.put("TableHeader.background",         AppTheme.PURPLE_DARK);
        UIManager.put("TableHeader.foreground",         Color.WHITE);
        UIManager.put("TableHeader.separatorColor",     AppTheme.BORDER_COLOR);

        UIManager.put("ScrollBar.background",           AppTheme.BG_CARD);
        UIManager.put("ScrollBar.thumb",                AppTheme.PURPLE_SUBTLE);
        UIManager.put("ScrollBar.track",                AppTheme.BG_CARD);
        UIManager.put("ScrollBar.width",                8);

        UIManager.put("ScrollPane.background",          AppTheme.BG_CONTENT);
        UIManager.put("Viewport.background",            AppTheme.BG_CONTENT);

        UIManager.put("Separator.foreground",           AppTheme.BORDER_COLOR);
        UIManager.put("Label.foreground",               AppTheme.TEXT_PRIMARY);

        UIManager.put("ProgressBar.background",         AppTheme.BG_CARD);
        UIManager.put("ProgressBar.foreground",         AppTheme.PURPLE);
        UIManager.put("ProgressBar.arc",                6);

        UIManager.put("ToolTip.background",             AppTheme.BG_CARD);
        UIManager.put("ToolTip.foreground",             AppTheme.TEXT_PRIMARY);
        UIManager.put("ToolTip.border",                 BorderFactory.createLineBorder(AppTheme.BORDER_COLOR));

        UIManager.put("Component.arc",                  8);
        UIManager.put("TextComponent.arc",              8);
        UIManager.put("defaultFont",                    new Font("Segoe UI", Font.PLAIN, 13));
    }

    // ── Configuração do frame ──────────────────────────────────────────────
    private void configurarFrame() {
        // undecorated ANTES de qualquer operação de display
        frame.setUndecorated(true);

        frame.setSize(LARGURA, ALTURA);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.setBackground(AppTheme.BG_MAIN);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (NavigationController.getInstance().confirmarFechamento()) {
                    frame.dispose();
                    System.exit(0);
                }
            }
        });
    }

    // ── Montagem do layout ─────────────────────────────────────────────────
    private void montarLayout() {
        // Root sem transparência → evita artefatos em maximize/fullscreen
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AppTheme.BG_MAIN);
        root.setOpaque(true);

        // Barra de título customizada no topo absoluto
        CustomTitleBar titleBar = new CustomTitleBar(frame, "SGDG — Grade Escolar");
        root.add(titleBar, BorderLayout.NORTH);

        // Corpo: sidebar à esquerda, conteúdo (header interno + content) à direita
        JPanel corpo = new JPanel(new BorderLayout());
        corpo.setOpaque(true);
        corpo.setBackground(AppTheme.BG_MAIN);

        JPanel centro = new JPanel(new BorderLayout());
        centro.setBackground(AppTheme.BG_CONTENT);
        centro.setOpaque(true);
        centro.add(headerPanel,  BorderLayout.NORTH);
        centro.add(contentPanel, BorderLayout.CENTER);

        corpo.add(sidebarPanel, BorderLayout.WEST);
        corpo.add(centro,       BorderLayout.CENTER);

        root.add(corpo, BorderLayout.CENTER);

        frame.setContentPane(root);
    }

    // ── Eventos ────────────────────────────────────────────────────────────
    private void configurarEventos() {
        headerPanel.getBtnMenu().addActionListener(e -> {
            sidebarPanel.toggleMinimizado();
            frame.revalidate();
            frame.repaint();
        });
    }
}
