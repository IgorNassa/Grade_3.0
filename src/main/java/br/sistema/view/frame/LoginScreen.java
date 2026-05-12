package br.sistema.view.frame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginScreen extends JFrame {

    private final Color BACKGROUND = new Color(12, 12, 18);
    private final Color CARD = new Color(24, 24, 34, 240);
    private final Color FIELD = new Color(18, 18, 28);
    private final Color BORDER = new Color(55, 55, 75);
    private final Color PURPLE = new Color(108, 92, 231);
    private final Color TEXT = new Color(210, 210, 220);

    // Variaveis para configurar para arrastar uma janela
    private int mouseX;
    private int mouseY;

    // Fontes

    private Font loadFont(float size, int style) {

        try {

            Font font = Font.createFont(
                    Font.TRUETYPE_FONT,
                    getClass().getResourceAsStream("/Fonts/JetBrainsMono-Regular.ttf")
            );

            return font.deriveFont(style, size);

        } catch (Exception e) {

            return new Font("SansSerif", style, (int) size);
        }
    }

    public LoginScreen() {

        Font font12 = loadFont(12f, Font.PLAIN);
        Font font14 = loadFont(14f, Font.PLAIN);
        Font fontTitle = loadFont(30f, Font.BOLD);


        // tira a barra padrão

        setUndecorated(true);

        setTitle("Flavio Warken");
        setSize(700, 760);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);


        // painel principal


        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND);

        // topbar


        JPanel topBar = new JPanel(new BorderLayout());

        topBar.setPreferredSize(new Dimension(0, 42));
        topBar.setBackground(BACKGROUND);

        // logo e nome

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 8));
        leftPanel.setOpaque(false);

        ImageIcon icon = new ImageIcon(
                getClass().getResource("/icons/fundoFlavioWarken.png")
        );

        Image img = icon.getImage().getScaledInstance(
                22,
                22,
                Image.SCALE_SMOOTH
        );

        JLabel logo = new JLabel(new ImageIcon(img));

        JLabel appName = new JLabel("Flavio Warken");
        appName.setForeground(Color.WHITE);
        appName.setFont(font14);

        leftPanel.add(logo);
        leftPanel.add(appName);

        // botões


        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 6));
        buttonsPanel.setOpaque(false);

        JButton minimizeBtn = new JButton("_");
        JButton closeBtn = new JButton("x");

        styleTopButton(minimizeBtn);
        styleTopButton(closeBtn);

        minimizeBtn.addActionListener(e ->
                setState(JFrame.ICONIFIED)
        );

        closeBtn.addActionListener(e ->
                System.exit(0)
        );

        buttonsPanel.add(minimizeBtn);
        buttonsPanel.add(closeBtn);

        topBar.add(leftPanel, BorderLayout.WEST);
        topBar.add(buttonsPanel, BorderLayout.EAST);


        // arrastar janelas

        MouseAdapter drag = new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {

                mouseX = e.getX();
                mouseY = e.getY();
            }

            @Override
            public void mouseDragged(MouseEvent e) {

                setLocation(
                        e.getXOnScreen() - mouseX,
                        e.getYOnScreen() - mouseY
                );
            }
        };

        topBar.addMouseListener(drag);
        topBar.addMouseMotionListener(drag);

        // fundo

        JPanel background = new JPanel(new GridBagLayout());
        background.setBackground(BACKGROUND);

        // card

        RoundedPanel card = new RoundedPanel(40, CARD);

        card.setPreferredSize(new Dimension(470, 620));

        card.setLayout(new GridBagLayout());

        card.setBorder(new EmptyBorder(40, 35, 40, 35));

        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.weightx = 1;

        // logo central

        ImageIcon logoIcon = new ImageIcon(
                getClass().getResource("/icons/fundoFlavioWarken.png")
        );

        Image image = logoIcon.getImage().getScaledInstance(
                230,
                230,
                Image.SCALE_SMOOTH
        );

        JLabel centerLogo = new JLabel(new ImageIcon(image)) {

            @Override
            protected void paintComponent(Graphics g) {

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setComposite(
                        AlphaComposite.getInstance(
                                AlphaComposite.SRC_OVER,
                                0.18f
                        )
                );

                super.paintComponent(g2);

                g2.dispose();
            }
        };

        centerLogo.setHorizontalAlignment(SwingConstants.CENTER);

        c.gridy = 0;
        c.insets = new Insets(-20, 0, -170, 0);

        card.add(centerLogo, c);

        // Titulo

        JLabel title = new JLabel(
                "Flavio Warken",
                SwingConstants.CENTER
        );

        title.setFont(fontTitle);

        title.setForeground(new Color(245, 240, 255));

        c.gridy = 1;

        c.insets = new Insets(40, 0, 0, 0);

        card.add(title, c);

        // subtitulo

        JLabel subtitle = new JLabel(
                "Gerador de Grade Escolar",
                SwingConstants.CENTER
        );

        subtitle.setFont(font14);

        subtitle.setForeground(new Color(150, 150, 170));

        c.gridy = 2;

        c.insets = new Insets(10, 0, 35, 0);

        card.add(subtitle, c);

        // login label

        JLabel loginLabel = new JLabel("Login");

        loginLabel.setFont(font14);

        loginLabel.setForeground(TEXT);

        c.gridy = 3;

        c.insets = new Insets(0, 0, 8, 0);

        card.add(loginLabel, c);

        // login field

        RoundedTextField loginField = new RoundedTextField(" Digite seu login");

        loginField.setFont(font14);

        c.gridy = 4;

        c.insets = new Insets(0, 0, 22, 0);

        card.add(loginField, c);


        // senha label

        JLabel passLabel = new JLabel("Senha");

        passLabel.setFont(font14);

        passLabel.setForeground(TEXT);

        c.gridy = 5;

        c.insets = new Insets(0, 0, 8, 0);

        card.add(passLabel, c);

        // senha field

        RoundedPasswordField passField = new RoundedPasswordField(" Digite sua Senha");

        passField.setFont(font14);

        c.gridy = 6;

        c.insets = new Insets(0, 0, 35, 0);

        card.add(passField, c);

        // botao

        RoundedButton btn = new RoundedButton("Entrar");

        btn.setFont(font14);

        c.gridy = 7;

        c.insets = new Insets(0, 0, 0, 0);

        card.add(btn, c);

        background.add(card);

        // Adiciona tudo

        mainPanel.add(topBar, BorderLayout.NORTH);
        mainPanel.add(background, BorderLayout.CENTER);

        setContentPane(mainPanel);

        setVisible(true);
    }


    // estilo botões topbar

    private void styleTopButton(JButton button) {

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setForeground(Color.WHITE);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(42, 28));
    }

    // Text field

    class RoundedTextField extends JTextField {

        private final String placeholder;

        public RoundedTextField(String placeholder) {

            this.placeholder = placeholder;

            setPreferredSize(new Dimension(380, 52));

            setOpaque(false);

            setBorder(new EmptyBorder(0, 18, 0, 18));

            setForeground(Color.WHITE);

            setCaretColor(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(FIELD);

            g2.fillRoundRect(
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    32,
                    32
            );

            g2.setColor(BORDER);

            g2.drawRoundRect(
                    0,
                    0,
                    getWidth() - 1,
                    getHeight() - 1,
                    32,
                    32
            );

            g2.dispose();

            super.paintComponent(g);

            // PLACEHOLDER
            if (getText().isEmpty() && !isFocusOwner()) {

                Graphics2D gPlaceholder = (Graphics2D) g.create();

                gPlaceholder.setRenderingHint(
                        RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON
                );

                gPlaceholder.setColor(new Color(140, 140, 160));

                gPlaceholder.setFont(getFont());

                Insets in = getInsets();

                FontMetrics fm = gPlaceholder.getFontMetrics();

                gPlaceholder.drawString(
                        placeholder,
                        in.left,
                        (getHeight() + fm.getAscent()) / 2 - 4
                );

                gPlaceholder.dispose();
            }
        }
    }

    // senha field

    class RoundedPasswordField extends JPasswordField {

        private final String placeholder;

        public RoundedPasswordField(String placeholder) {

            this.placeholder = placeholder;

            setPreferredSize(new Dimension(380, 52));

            setOpaque(false);

            setBorder(new EmptyBorder(0, 18, 0, 18));

            setForeground(Color.WHITE);

            setCaretColor(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(FIELD);

            g2.fillRoundRect(
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    32,
                    32
            );

            g2.setColor(BORDER);

            g2.drawRoundRect(
                    0,
                    0,
                    getWidth() - 1,
                    getHeight() - 1,
                    32,
                    32
            );

            g2.dispose();

            super.paintComponent(g);

            // PLACEHOLDER
            if (getPassword().length == 0 && !isFocusOwner()) {

                Graphics2D gPlaceholder = (Graphics2D) g.create();

                gPlaceholder.setRenderingHint(
                        RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON
                );

                gPlaceholder.setColor(new Color(140, 140, 160));

                gPlaceholder.setFont(getFont());

                Insets in = getInsets();

                FontMetrics fm = gPlaceholder.getFontMetrics();

                gPlaceholder.drawString(
                        placeholder,
                        in.left,
                        (getHeight() + fm.getAscent()) / 2 - 4
                );

                gPlaceholder.dispose();
            }
        }
    }


    // Card

    class RoundedPanel extends JPanel {

        private final int radius;
        private final Color color;

        public RoundedPanel(int radius, Color color) {

            this.radius = radius;
            this.color = color;

            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(color);

            g2.fillRoundRect(
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    radius,
                    radius
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }

    // Botao

    class RoundedButton extends JButton {

        private boolean hover = false;

        public RoundedButton(String text) {

            super(text);

            setPreferredSize(new Dimension(380, 52));

            setFocusPainted(false);

            setContentAreaFilled(false);

            setBorderPainted(false);

            setForeground(Color.WHITE);

            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addMouseListener(new java.awt.event.MouseAdapter() {

                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {

                    hover = true;
                    repaint();
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {

                    hover = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            Color start = hover
                    ? new Color(98, 72, 200)
                    : new Color(138, 92, 246);

            Color end = hover
                    ? new Color(78, 62, 180)
                    : new Color(108, 92, 231);

            GradientPaint gradient = new GradientPaint(
                    0,
                    0,
                    start,
                    getWidth(),
                    getHeight(),
                    end
            );

            g2.setPaint(gradient);

            g2.fillRoundRect(
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    32,
                    32
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }


   /* // Chamando a main

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            try {

                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName()
                );

            } catch (Exception ignored) {
            }

            new LoginScreen();
        });
    }*/
}