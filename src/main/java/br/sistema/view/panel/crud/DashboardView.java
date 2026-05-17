package br.sistema.view.panel.crud;

import br.sistema.view.util.AppTheme;

import javax.swing.*;
import java.awt.*;

public class DashboardView extends JPanel {

    public DashboardView() {
        setLayout(new BorderLayout());
        setBackground(AppTheme.BG_CONTENT);
        setBorder(BorderFactory.createEmptyBorder(32, 32, 32, 32));

        add(montarTopo(), BorderLayout.NORTH);
        add(montarCards(), BorderLayout.CENTER);
    }

    private JPanel montarTopo() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setOpaque(false);
        painel.setBorder(BorderFactory.createEmptyBorder(0, 0, 28, 0));

        JLabel titulo = new JLabel("Dashboard");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(AppTheme.TEXT_PRIMARY);

        JLabel subtitulo = new JLabel("Bem-vindo ao SGDG — Sistema Gerador de Grade Escolar");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitulo.setForeground(AppTheme.TEXT_SECONDARY);

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.add(titulo);
        textos.add(Box.createVerticalStrut(6));
        textos.add(subtitulo);

        painel.add(textos, BorderLayout.WEST);
        return painel;
    }

    private JPanel montarCards() {
        JPanel grid = new JPanel(new GridLayout(2, 3, 20, 20));
        grid.setOpaque(false);

        grid.add(criarCard("Professores", "Gerencie o cadastro\nde professores e suas\ndisciplinas.", new Color(114, 95, 231), "👨‍🏫"));
        grid.add(criarCard("Disciplinas", "Cadastre e edite as\ndisciplinas da grade\ncurricular.", new Color(72, 199, 142), "📚"));
        grid.add(criarCard("Turmas", "Organize as turmas\ndo Ensino Médio e\nFundamental.", new Color(255, 180, 50), "🏫"));
        grid.add(criarCard("Turnos", "Configure os turnos:\nMatutino, Vespertino\ne Noturno.", new Color(100, 180, 255), "🕐"));
        grid.add(criarCard("Gerar Grade", "Execute o algoritmo\nde geração automática\nda grade escolar.", new Color(200, 100, 255), "⚡"));
        grid.add(criarCard("Visualizar Grade", "Consulte a grade\ngerada em formato\nde tabela semanal.", new Color(255, 130, 100), "📋"));

        return grid;
    }

    private JPanel criarCard(String titulo, String descricao, Color acento, String emoji) {
        JPanel card = AppTheme.cardPanel(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        topo.setOpaque(false);

        JLabel icone = new JLabel(emoji);
        icone.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        topo.add(icone);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(AppTheme.TEXT_PRIMARY);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        topo.add(lblTitulo);

        // Barra de acento
        JPanel acente = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(acento);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                g2.dispose();
            }
        };
        acente.setOpaque(false);
        acente.setPreferredSize(new Dimension(40, 3));

        String[] linhas = descricao.split("\n");
        JPanel textoPanel = new JPanel();
        textoPanel.setOpaque(false);
        textoPanel.setLayout(new BoxLayout(textoPanel, BoxLayout.Y_AXIS));
        for (String linha : linhas) {
            JLabel l = new JLabel(linha);
            l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            l.setForeground(AppTheme.TEXT_SECONDARY);
            textoPanel.add(l);
        }

        JPanel centro = new JPanel();
        centro.setOpaque(false);
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));
        centro.add(acente);
        centro.add(Box.createVerticalStrut(12));
        centro.add(textoPanel);

        card.add(topo, BorderLayout.NORTH);
        card.add(centro, BorderLayout.CENTER);

        // Hover glow
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(acento.darker(), 1),
                    BorderFactory.createEmptyBorder(21, 21, 21, 21)
                ));
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));
            }
        });

        return card;
    }
}
