package br.sistema.view.util;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Tema visual centralizado do sistema SGDG.
 * Paleta dark com destaque roxo — consistente com o LoginScreen.
 */
public class AppTheme {

    // ─── Paleta de Cores ────────────────────────────────────────────────────────
    public static final Color BG_MAIN        = new Color(15, 14, 20);   // fundo principal
    public static final Color BG_SIDEBAR     = new Color(22, 18, 40);   // sidebar
    public static final Color BG_CARD        = new Color(28, 24, 48);   // cards / paineis
    public static final Color BG_CONTENT     = new Color(18, 16, 30);   // área de conteúdo
    public static final Color BG_FIELD       = new Color(30, 26, 50);   // campos de input
    public static final Color BG_TABLE_ROW   = new Color(24, 20, 42);   // linhas da tabela
    public static final Color BG_TABLE_ALT   = new Color(28, 24, 48);   // linhas alternadas
    public static final Color BG_TABLE_SEL   = new Color(80, 60, 160);  // linha selecionada

    public static final Color PURPLE         = new Color(114, 95, 231); // destaque principal
    public static final Color PURPLE_HOVER   = new Color(130, 112, 245);
    public static final Color PURPLE_DARK    = new Color(80, 65, 170);
    public static final Color PURPLE_SUBTLE  = new Color(60, 50, 110);  // seções da sidebar

    public static final Color SUCCESS        = new Color(72, 199, 142); // verde sucesso
    public static final Color DANGER         = new Color(255, 99, 99);  // vermelho perigo
    public static final Color WARNING        = new Color(255, 180, 50); // amarelo aviso

    public static final Color TEXT_PRIMARY   = new Color(230, 228, 242);
    public static final Color TEXT_SECONDARY = new Color(150, 145, 170);
    public static final Color TEXT_MUTED     = new Color(90, 85, 110);
    public static final Color BORDER_COLOR   = new Color(50, 44, 80);

    // ─── Helpers ────────────────────────────────────────────────────────────────

    /** Aplica estilo de botão primário (roxo sólido) */
    public static JButton primaryButton(String label) {
        JButton btn = new JButton(label) {
            boolean hover, pressed;
            { addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hover = true;  repaint(); }
                public void mouseExited (MouseEvent e) { hover = false; pressed = false; repaint(); }
                public void mousePressed(MouseEvent e) { pressed = true;  repaint(); }
                public void mouseReleased(MouseEvent e){ pressed = false; repaint(); }
            }); }

            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(pressed ? PURPLE_DARK : hover ? PURPLE_HOVER : PURPLE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(110, 38));
        return btn;
    }

    /** Botão secundário (outline) */
    public static JButton secondaryButton(String label) {
        JButton btn = new JButton(label) {
            boolean hover;
            { addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hover = true;  repaint(); }
                public void mouseExited (MouseEvent e) { hover = false; repaint(); }
            }); }

            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hover ? PURPLE_SUBTLE : BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(hover ? PURPLE : BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(TEXT_PRIMARY);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(110, 38));
        return btn;
    }

    /** Botão de perigo (vermelho) */
    public static JButton dangerButton(String label) {
        JButton btn = new JButton(label) {
            boolean hover;
            { addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hover = true;  repaint(); }
                public void mouseExited (MouseEvent e) { hover = false; repaint(); }
            }); }

            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color base = new Color(180, 50, 50);
                g2.setColor(hover ? new Color(220, 60, 60) : base);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(110, 38));
        return btn;
    }

    /** Cria um JTextField estilizado com placeholder */
    public static JTextField styledField(String placeholder, int width) {
        JTextField field = new JTextField() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_FIELD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(isFocusOwner() ? PURPLE : BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D gp = (Graphics2D) g.create();
                    gp.setColor(TEXT_MUTED);
                    gp.setFont(getFont());
                    FontMetrics fm = gp.getFontMetrics();
                    gp.drawString(placeholder, getInsets().left, (getHeight() + fm.getAscent()) / 2 - 3);
                    gp.dispose();
                }
            }
        };
        field.setOpaque(false);
        field.setBorder(new EmptyBorder(0, 12, 0, 12));
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(TEXT_PRIMARY);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setPreferredSize(new Dimension(width, 38));
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) { field.repaint(); }
            public void focusLost(java.awt.event.FocusEvent e)   { field.repaint(); }
        });
        return field;
    }

    /** Cria um JComboBox estilizado */
    public static <T> JComboBox<T> styledCombo(T[] items) {
        JComboBox<T> combo = new JComboBox<>(items);
        combo.setBackground(BG_FIELD);
        combo.setForeground(TEXT_PRIMARY);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setPreferredSize(new Dimension(160, 38));
        combo.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        return combo;
    }

    /** Label padrão */
    public static JLabel label(String txt) {
        JLabel l = new JLabel(txt);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        l.setForeground(TEXT_SECONDARY);
        return l;
    }

    /** Label de título de seção */
    public static JLabel sectionTitle(String txt) {
        JLabel l = new JLabel(txt);
        l.setFont(new Font("Segoe UI", Font.BOLD, 22));
        l.setForeground(TEXT_PRIMARY);
        return l;
    }

    /** Cria painel com borda arredondada e fundo card */
    public static JPanel cardPanel(LayoutManager layout) {
        JPanel panel = new JPanel(layout) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 16, 16);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        return panel;
    }
}
