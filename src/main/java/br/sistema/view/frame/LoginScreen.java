package br.sistema.view.frame;

import br.sistema.view.util.AppTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginScreen extends JFrame {

    // Cores alinhadas ao AppTheme para consistência visual
    private final Color BACKGROUND = AppTheme.BG_MAIN;
    private final Color CARD       = AppTheme.BG_CARD;
    private final Color FIELD      = AppTheme.BG_FIELD;
    private final Color BORDER     = AppTheme.BORDER_COLOR;
    private final Color PURPLE     = AppTheme.PURPLE;
    private final Color TEXT       = AppTheme.TEXT_PRIMARY;
    private final Color TEXT_MUTED = AppTheme.TEXT_SECONDARY;

    // Variáveis para arrastar a janela
    private int mouseX;
    private int mouseY;

    // Fontes
    private Font loadFont(float size, int style) {
        try {
            Font font = Font.createFont(
                    Font.TRUETYPE_FONT,
                    getClass().getResourceAsStream("/Fonts/JetBrainsMono-Regular.ttf"));
            return font.deriveFont(style, size);
        } catch (Exception e) {
            return new Font("SansSerif", style, (int) size);
        }
    }

    public LoginScreen() {
        Font font12 = loadFont(12f, Font.PLAIN);
        Font font14 = loadFont(14f, Font.PLAIN);
        Font fontTitle = loadFont(32f, Font.BOLD);
        Font fontSubtitle = loadFont(26f, Font.BOLD);

        // Remove a barra padrão nativa
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0)); // Transparente para cantos arredondados

        setTitle("Flavio Warken");
        setSize(1000, 650); // Ajustado para o layout dividido mais largo
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // Painel raiz com bordas arredondadas e sombra/borda sutis
        JPanel rootPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Fundo escuro do app
                g2.setColor(BACKGROUND);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

                // Borda sutil em volta de todo o frame
                g2.setColor(new Color(45, 45, 55));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);

                g2.dispose();
            }
        };
        rootPanel.setOpaque(false);

        // Container principal dividindo esquerdo (visual) e direito (form)
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.BOTH;
        gc.weighty = 1.0;

        // --------------------------------------------------------------------------------
        // //
        // 1. LADO ESQUERDO (Painel Visual)
        // --------------------------------------------------------------------------------
        // //
        gc.gridx = 0;
        gc.weightx = 0.45; // Ocupa 45% da largura
        gc.insets = new Insets(15, 15, 15, 15); // Margens em relação à janela

        RoundedPanel leftPanel = new RoundedPanel(24, CARD) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Gradiente suave sobrepondo o fundo roxo escuro
                GradientPaint gp = new GradientPaint(0, 0, new Color(114, 95, 231, 60), 0, getHeight(),
                        new Color(20, 20, 30, 80));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);

                // Formas abstratas desenhadas dinamicamente simulando as "dunas/montanhas"
                g2.setColor(new Color(255, 255, 255, 5));
                g2.fillOval(-150, getHeight() - 250, 500, 500);
                g2.fillOval(getWidth() - 200, -100, 400, 400);

                g2.dispose();
            }
        };
        leftPanel.setLayout(new GridBagLayout());

        GridBagConstraints leftGc = new GridBagConstraints();
        leftGc.gridx = 0;
        leftGc.weightx = 1.0;
        leftGc.fill = GridBagConstraints.NONE;
        leftGc.anchor = GridBagConstraints.CENTER;

        // Logo central SVG levemente opaca (com renderização vetorial FlatLaf)
        try {
            com.formdev.flatlaf.extras.FlatSVGIcon svgIcon = new com.formdev.flatlaf.extras.FlatSVGIcon(
                    "icons/fundoFlavioWarken.svg", 400, 400);
            JLabel logo = new JLabel(svgIcon) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    // Aplica leve opacidade na logo (45%)
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.45f));
                    super.paintComponent(g2);
                    g2.dispose();
                }
            };
            leftGc.gridy = 0;
            leftGc.insets = new Insets(0, 0, 20, 0);
            leftPanel.add(logo, leftGc);
        } catch (Exception ignored) {
        }

        // Nome Flavio Warken centralizado horizontalmente
        JLabel logoText = new JLabel("Flavio Warken");
        logoText.setFont(fontTitle); // Usando a fonte grande
        logoText.setForeground(Color.WHITE);
        leftGc.gridy = 1;
        leftGc.insets = new Insets(0, 0, 10, 0);
        leftPanel.add(logoText, leftGc);

        // Subtítulo centralizado
        JLabel subtitleLeft = new JLabel("Gerador de Grade Escolar");
        subtitleLeft.setFont(font14);
        subtitleLeft.setForeground(new Color(200, 200, 220));
        leftGc.gridy = 2;
        leftGc.insets = new Insets(0, 0, 0, 0);
        leftPanel.add(subtitleLeft, leftGc);

        contentPanel.add(leftPanel, gc);

        // --------------------------------------------------------------------------------
        // //
        // 2. LADO DIREITO (Formulário e Controles de Janela)
        // --------------------------------------------------------------------------------
        // //
        gc.gridx = 1;
        gc.weightx = 0.55;
        gc.insets = new Insets(0, 0, 0, 0); // Ocupa todo o resto do espaço

        JPanel rightContainer = new JPanel(new BorderLayout());
        rightContainer.setOpaque(false);

        // Topbar (Apenas minimizar e fechar do lado direito)
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setPreferredSize(new Dimension(0, 50));

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        buttonsPanel.setOpaque(false);

        JButton minimizeBtn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? Color.WHITE : TEXT_MUTED);
                g2.setStroke(new BasicStroke(2));
                int cx = getWidth() / 2;
                int cy = getHeight() / 2;
                int r = 6;
                g2.drawLine(cx - r, cy, cx + r, cy);
                g2.dispose();
            }
        };
        JButton closeBtn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(255, 80, 80) : TEXT_MUTED);
                g2.setStroke(new BasicStroke(2));
                int cx = getWidth() / 2;
                int cy = getHeight() / 2;
                int r = 6;
                g2.drawLine(cx - r, cy - r, cx + r, cy + r);
                g2.drawLine(cx + r, cy - r, cx - r, cy + r);
                g2.dispose();
            }
        };

        styleTopButton(minimizeBtn);
        styleTopButton(closeBtn);

        minimizeBtn.addActionListener(e -> setState(JFrame.ICONIFIED));
        closeBtn.addActionListener(e -> System.exit(0));

        buttonsPanel.add(minimizeBtn);
        buttonsPanel.add(closeBtn);
        topBar.add(buttonsPanel, BorderLayout.EAST);

        rightContainer.add(topBar, BorderLayout.NORTH);

        // Envoltório para centralizar o formulário no meio da tela direita
        JPanel formWrapper = new JPanel(new GridBagLayout());
        formWrapper.setOpaque(false);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);

        GridBagConstraints rc = new GridBagConstraints();
        rc.fill = GridBagConstraints.HORIZONTAL;
        rc.weightx = 1.0;
        rc.gridx = 0;

        // Título Formulário
        JLabel formTitle = new JLabel("Acesse sua conta");
        formTitle.setFont(fontTitle);
        formTitle.setForeground(Color.WHITE);
        rc.gridy = 0;
        rc.insets = new Insets(0, 0, 10, 0);
        formPanel.add(formTitle, rc);

        // Subtítulo Formulário
        JLabel formSubtitle = new JLabel("Bem-vindo de volta! Insira seus dados.");
        formSubtitle.setFont(font14);
        formSubtitle.setForeground(TEXT_MUTED);
        rc.gridy = 1;
        rc.insets = new Insets(0, 0, 45, 0);
        formPanel.add(formSubtitle, rc);

        // Label Login
        JLabel loginLabel = new JLabel("Usuário");
        loginLabel.setFont(font12);
        loginLabel.setForeground(TEXT);
        rc.gridy = 2;
        rc.insets = new Insets(0, 5, 8, 0);
        formPanel.add(loginLabel, rc);

        // Field Login
        RoundedTextField loginField = new RoundedTextField("Digite seu usuário");
        loginField.setFont(font14);
        rc.gridy = 3;
        rc.insets = new Insets(0, 0, 20, 0);
        formPanel.add(loginField, rc);

        // Label Senha
        JLabel passLabel = new JLabel("Senha");
        passLabel.setFont(font12);
        passLabel.setForeground(TEXT);
        rc.gridy = 4;
        rc.insets = new Insets(0, 5, 8, 0);
        formPanel.add(passLabel, rc);

        // Field Senha
        RoundedPasswordField passField = new RoundedPasswordField("Digite sua senha");
        passField.setFont(font14);
        rc.gridy = 5;
        rc.insets = new Insets(0, 0, 45, 0);
        formPanel.add(passField, rc);

        // Botão Entrar
        RoundedButton btn = new RoundedButton("Entrar");
        btn.setFont(font14.deriveFont(Font.BOLD));
        rc.gridy = 6;
        rc.insets = new Insets(0, 0, 0, 0);
        formPanel.add(btn, rc);

        // Ação de Login (Enter nos campos ou clique no botão)
        java.awt.event.ActionListener loginAction = e -> {
            String user = loginField.getText().trim();
            String pass = new String(passField.getPassword()).trim();

            if ((user.equals("admin") && pass.equals("1234")) ||
                (user.equals("flavio") && pass.equals("1234"))) {
                dispose();
                new DashFrame();
            } else {
                loginField.setErrorState(true);
                passField.setErrorState(true);
                shakeWindow();
            }
        };
        btn.addActionListener(loginAction);
        loginField.addActionListener(loginAction);
        passField.addActionListener(loginAction);

        // Centraliza e adiciona o formPanel no wrapper
        formWrapper.add(formPanel);
        rightContainer.add(formWrapper, BorderLayout.CENTER);

        contentPanel.add(rightContainer, gc);

        rootPanel.add(contentPanel, BorderLayout.CENTER);
        setContentPane(rootPanel);

        // Arrastar a janela usando qualquer área vazia do painel
        MouseAdapter drag = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                setLocation(e.getXOnScreen() - mouseX, e.getYOnScreen() - mouseY);
            }
        };
        rootPanel.addMouseListener(drag);
        rootPanel.addMouseMotionListener(drag);
        topBar.addMouseListener(drag);
        topBar.addMouseMotionListener(drag);

        setVisible(true);
    }

    // Estilo dos botões da topbar
    private void styleTopButton(JButton button) {
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setForeground(TEXT_MUTED);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFont(new Font("SansSerif", Font.BOLD, 18));
        button.setPreferredSize(new Dimension(45, 30));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(TEXT_MUTED);
            }
        });
    }

    // Animação de tremor na janela quando o login falha
    private void shakeWindow() {
        Point o = getLocation();
        Timer t = new Timer(30, null);
        t.addActionListener(new java.awt.event.ActionListener() {
            int c = 0;
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (c % 2 == 0) setLocation(o.x + 10, o.y);
                else setLocation(o.x - 10, o.y);
                c++;
                if (c > 8) {
                    t.stop();
                    setLocation(o);
                }
            }
        });
        t.start();
    }

    // Text field com cantos arredondados, focus state e placeholder puro
    class RoundedTextField extends JTextField {
        private final String placeholder;
        private boolean errorState = false;

        public void setErrorState(boolean errorState) {
            this.errorState = errorState;
            repaint();
        }

        public RoundedTextField(String placeholder) {
            this.placeholder = placeholder;
            setPreferredSize(new Dimension(340, 50));
            setOpaque(false);
            setBorder(new EmptyBorder(0, 18, 0, 18));
            setForeground(Color.WHITE);
            setCaretColor(Color.WHITE);

            addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent evt) {
                    repaint();
                }

                public void focusLost(java.awt.event.FocusEvent evt) {
                    repaint();
                }
            });

            addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    setErrorState(false);
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(FIELD);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);

            if (errorState) {
                g2.setColor(new Color(255, 80, 80));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
            } else if (isFocusOwner()) {
                g2.setColor(PURPLE);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
            } else {
                g2.setColor(BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
            }
            g2.dispose();

            super.paintComponent(g);

            if (getText().isEmpty() && !isFocusOwner()) {
                Graphics2D gPlaceholder = (Graphics2D) g.create();
                gPlaceholder.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                gPlaceholder.setColor(TEXT_MUTED);
                gPlaceholder.setFont(getFont());
                FontMetrics fm = gPlaceholder.getFontMetrics();
                gPlaceholder.drawString(placeholder, getInsets().left, (getHeight() + fm.getAscent()) / 2 - 4);
                gPlaceholder.dispose();
            }
        }
    }

    // Password field com as mesmas propriedades estéticas
    class RoundedPasswordField extends JPasswordField {
        private final String placeholder;
        private boolean errorState = false;

        public void setErrorState(boolean errorState) {
            this.errorState = errorState;
            repaint();
        }

        public RoundedPasswordField(String placeholder) {
            this.placeholder = placeholder;
            setPreferredSize(new Dimension(340, 50));
            setOpaque(false);
            setBorder(new EmptyBorder(0, 18, 0, 18));
            setForeground(Color.WHITE);
            setCaretColor(Color.WHITE);

            addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent evt) {
                    repaint();
                }

                public void focusLost(java.awt.event.FocusEvent evt) {
                    repaint();
                }
            });

            addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    setErrorState(false);
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(FIELD);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);

            if (errorState) {
                g2.setColor(new Color(255, 80, 80));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
            } else if (isFocusOwner()) {
                g2.setColor(PURPLE);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
            } else {
                g2.setColor(BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
            }
            g2.dispose();

            super.paintComponent(g);

            if (getPassword().length == 0 && !isFocusOwner()) {
                Graphics2D gPlaceholder = (Graphics2D) g.create();
                gPlaceholder.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                gPlaceholder.setColor(TEXT_MUTED);
                gPlaceholder.setFont(getFont());
                FontMetrics fm = gPlaceholder.getFontMetrics();
                gPlaceholder.drawString(placeholder, getInsets().left, (getHeight() + fm.getAscent()) / 2 - 4);
                gPlaceholder.dispose();
            }
        }
    }

    // Painel com cantos arredondados base
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
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // Botão com efeitos Hover e Pressed
    class RoundedButton extends JButton {
        private boolean hover = false;
        private boolean pressed = false;

        public RoundedButton(String text) {
            super(text);
            setPreferredSize(new Dimension(340, 50));
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setForeground(Color.WHITE);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hover = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hover = false;
                    pressed = false;
                    repaint();
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    pressed = true;
                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    pressed = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Transição de cores nos estados
            if (pressed) {
                g2.setColor(PURPLE.darker());
            } else if (hover) {
                g2.setColor(PURPLE.brighter());
            } else {
                g2.setColor(PURPLE);
            }

            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
            g2.dispose();

            super.paintComponent(g);
        }
    }
}