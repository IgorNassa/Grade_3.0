package br.sistema.view.panel.crud;

import br.sistema.view.util.AppTheme;
import br.sistema.util.ServiceRegistry;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DashboardView extends JPanel {

    private JLabel lblTotalProfessores;
    private JLabel lblTotalDisciplinas;
    private JLabel lblTotalTurmas;
    private JLabel lblTotalTurnos;

    public DashboardView() {
        setLayout(new BorderLayout());
        setBackground(AppTheme.BG_CONTENT);
        setBorder(BorderFactory.createEmptyBorder(32, 32, 32, 32));

        JPanel topoPanel = new JPanel();
        topoPanel.setOpaque(false);
        topoPanel.setLayout(new BoxLayout(topoPanel, BoxLayout.Y_AXIS));
        topoPanel.add(montarTopo());
        topoPanel.add(montarResumos());
        topoPanel.add(Box.createVerticalStrut(28));

        add(topoPanel, BorderLayout.NORTH);
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

    private JPanel montarResumos() {
        JPanel painel = new JPanel(new GridLayout(1, 4, 18, 0));
        painel.setOpaque(false);

        // Buscar totais reais
        int totalProfs = getProfessorCount();
        int totalDiscs = getDisciplinaCount();
        int totalTurmas = getTurmaCount();
        int totalTurnos = getTurnoCount();

        // 1. Professores
        painel.add(new ResumoCard(
                "Professores",
                totalProfs,
                "Total cadastrados",
                null,
                AppTheme.PURPLE,
                "professor",
                new Color(53, 44, 84),
                false));

        // 2. Disciplinas
        painel.add(new ResumoCard(
                "Disciplinas",
                totalDiscs,
                "Total cadastradas",
                null,
                AppTheme.SUCCESS,
                "disciplina",
                new Color(25, 54, 45),
                false));

        // 3. Turmas
        painel.add(new ResumoCard(
                "Turmas",
                totalTurmas,
                "Total cadastradas",
                null,
                AppTheme.WARNING,
                "turma",
                new Color(55, 45, 30),
                false));

        // 4. Turnos
        painel.add(new ResumoCard(
                "Turnos",
                totalTurnos,
                "Total cadastrados",
                null,
                new Color(100, 180, 255),
                "turno",
                new Color(30, 42, 68),
                false));

        return painel;
    }

    private int getProfessorCount() {
        try {
            return ServiceRegistry.getInstance().get(br.sistema.controller.interfaces.ProfessorController.class)
                    .findAll().size();
        } catch (Exception e) {
            return 0;
        }
    }

    private int getDisciplinaCount() {
        try {
            return ServiceRegistry.getInstance().get(br.sistema.controller.interfaces.DisciplinaController.class)
                    .findAll().size();
        } catch (Exception e) {
            return 0;
        }
    }

    private int getTurmaCount() {
        try {
            return ServiceRegistry.getInstance().get(br.sistema.controller.interfaces.TurmaController.class).findAll()
                    .size();
        } catch (Exception e) {
            return 0;
        }
    }

    private int getTurnoCount() {
        try {
            return ServiceRegistry.getInstance().get(br.sistema.controller.interfaces.TurnoController.class).findAll()
                    .size();
        } catch (Exception e) {
            return 0;
        }
    }

    public void atualizarResumo() {
        if (lblTotalProfessores != null) {
            lblTotalProfessores.setText(String.valueOf(getProfessorCount()));
        }

        if (lblTotalDisciplinas != null) {
            lblTotalDisciplinas.setText(String.valueOf(getDisciplinaCount()));
        }

        if (lblTotalTurmas != null) {
            lblTotalTurmas.setText(String.valueOf(getTurmaCount()));
        }

        if (lblTotalTurnos != null) {
            lblTotalTurnos.setText(String.valueOf(getTurnoCount()));
        }

        revalidate();
        repaint();
    }

    private JPanel montarCards() {
        JPanel grid = new JPanel(new GridLayout(2, 3, 20, 20));
        grid.setOpaque(false);

        grid.add(criarCard("Professores", "Gerencie o cadastro\nde professores e suas\ndisciplinas.",
                new Color(114, 95, 231), "professor"));
        grid.add(criarCard("Disciplinas", "Cadastre e edite as\ndisciplinas da grade\ncurricular.",
                new Color(72, 199, 142), "disciplina"));
        grid.add(criarCard("Turmas", "Organize as turmas\ndo Ensino Médio e\nFundamental.", new Color(255, 180, 50),
                "turma"));
        grid.add(criarCard("Turnos", "Configure os turnos:\nMatutino, Vespertino\ne Noturno.", new Color(100, 180, 255),
                "turno"));
        grid.add(criarCard("Gerar Grade", "Execute o algoritmo\nde geração automática\nda grade escolar.",
                new Color(200, 100, 255), "gerar"));
        grid.add(criarCard("Visualizar Grade", "Consulte a grade\ngerada em formato\nde tabela semanal.",
                new Color(255, 130, 100), "visualizar"));

        return grid;
    }

    private JPanel criarCard(String titulo, String descricao, Color acento, String tipoIcone) {
        JPanel card = AppTheme.cardPanel(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        topo.setOpaque(false);

        JLabel icone = new JLabel(new VectorIcon(tipoIcone, acento, 28));
        topo.add(icone);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(AppTheme.TEXT_PRIMARY);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        topo.add(lblTitulo);

        // Barra de acento
        JPanel acente = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
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
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(acento.darker(), 1),
                        BorderFactory.createEmptyBorder(21, 21, 21, 21)));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));
            }
        });

        return card;
    }

    // Componente customizado de card de resumo
    private class ResumoCard extends JPanel {
        private final Color acento;
        private boolean hover = false;

        public ResumoCard(String titulo, int total, String textoTotal, String badgeTexto, Color acento,
                String tipoIcone, Color iconBg, boolean isDateCard) {
            this.acento = acento;
            setLayout(new GridBagLayout());
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));

            GridBagConstraints gc = new GridBagConstraints();
            gc.fill = GridBagConstraints.HORIZONTAL;

            // 1. Container do ícone (56x56)
            JPanel iconPanel = new JPanel(new BorderLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(iconBg);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                    g2.dispose();
                }
            };
            iconPanel.setOpaque(false);
            iconPanel.setPreferredSize(new Dimension(56, 56));

            JLabel lblIcon = new JLabel(new VectorIcon(tipoIcone, acento, 56));
            iconPanel.add(lblIcon, BorderLayout.CENTER);

            gc.gridx = 0;
            gc.gridy = 0;
            gc.gridheight = 3;
            gc.weightx = 0.0;
            gc.insets = new Insets(0, 0, 0, 18);
            gc.anchor = GridBagConstraints.WEST;
            add(iconPanel, gc);

            // 2. Título do card (Professores, etc)
            JLabel lblTitulo = new JLabel(titulo);
            lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lblTitulo.setForeground(AppTheme.TEXT_SECONDARY);

            gc.gridx = 1;
            gc.gridy = 0;
            gc.gridheight = 1;
            gc.weightx = 1.0;
            gc.insets = new Insets(0, 0, 2, 0);
            add(lblTitulo, gc);

            // 3. Valor principal (ex: 48)
            JLabel lblVal = new JLabel(String.valueOf(total));
            lblVal.setFont(new Font("Segoe UI", Font.BOLD, 32));
            lblVal.setForeground(Color.WHITE);
            if ("Professores".equals(titulo)) {
                DashboardView.this.lblTotalProfessores = lblVal;
            } else if ("Disciplinas".equals(titulo)) {
                DashboardView.this.lblTotalDisciplinas = lblVal;
            } else if ("Turmas".equals(titulo)) {
                DashboardView.this.lblTotalTurmas = lblVal;
            } else if ("Turnos".equals(titulo)) {
                DashboardView.this.lblTotalTurnos = lblVal;
            }

            gc.gridx = 1;
            gc.gridy = 1;
            gc.insets = new Insets(0, 0, 4, 0);
            add(lblVal, gc);

            // 4. Descrição secundária (ex: Total cadastrados)
            JLabel lblSub = new JLabel(textoTotal);
            lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblSub.setForeground(AppTheme.TEXT_MUTED);

            gc.gridx = 1;
            gc.gridy = 2;
            gc.insets = new Insets(0, 0, 0, 0);
            add(lblSub, gc);

            // 5. Badge no canto inferior direito
            if (badgeTexto != null && !badgeTexto.isEmpty()) {
                JPanel badge = new JPanel(new BorderLayout()) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(new Color(72, 199, 142, 25));
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                        g2.setColor(new Color(72, 199, 142, 80));
                        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                        g2.dispose();
                    }
                };
                badge.setOpaque(false);
                badge.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));

                JLabel lblBadge = new JLabel("↑ " + badgeTexto);
                lblBadge.setFont(new Font("Segoe UI", Font.BOLD, 10));
                lblBadge.setForeground(new Color(100, 220, 140));
                badge.add(lblBadge, BorderLayout.CENTER);

                GridBagConstraints gcBadge = new GridBagConstraints();
                gcBadge.gridx = 2;
                gcBadge.gridy = 2;
                gcBadge.weightx = 0.0;
                gcBadge.anchor = GridBagConstraints.SOUTHEAST;
                gcBadge.insets = new Insets(0, 8, 2, 0);
                add(badge, gcBadge);
            }

            // 6. Badge de data superior direito (apenas para Turnos)
            if (isDateCard) {
                JPanel dateBadge = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 3)) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(new Color(255, 255, 255, 10));
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                        g2.setColor(new Color(255, 255, 255, 18));
                        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);
                        g2.dispose();
                    }
                };
                dateBadge.setOpaque(false);
                dateBadge.setBorder(BorderFactory.createEmptyBorder(1, 6, 1, 8));

                JLabel lblCal = new JLabel("📅");
                lblCal.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 11));

                String dateStr = java.time.LocalDate.now().format(
                        java.time.format.DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy",
                                new java.util.Locale("pt", "BR")));
                JLabel lblDate = new JLabel(dateStr);
                lblDate.setFont(new Font("Segoe UI", Font.BOLD, 9));
                lblDate.setForeground(AppTheme.TEXT_SECONDARY);

                dateBadge.add(lblCal);
                dateBadge.add(lblDate);

                GridBagConstraints gcDate = new GridBagConstraints();
                gcDate.gridx = 2;
                gcDate.gridy = 0;
                gcDate.gridheight = 3;
                gcDate.weightx = 1.0;
                gcDate.weighty = 1.0;
                gcDate.anchor = GridBagConstraints.NORTHEAST;
                gcDate.insets = new Insets(-8, 0, 0, -8);
                add(dateBadge, gcDate);
            }

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
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(AppTheme.BG_CARD);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);

            if (hover) {
                g2.setColor(acento);
                g2.setStroke(new BasicStroke(1.2f));
            } else {
                g2.setColor(AppTheme.BORDER_COLOR);
                g2.setStroke(new BasicStroke(1f));
            }
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
            g2.dispose();
        }
    }

    // Ícones vetoriais modernos desenhados via código com suporte a escala dinâmica
    private static class VectorIcon implements Icon {
        private final String tipo;
        private final Color cor;
        private final int tamanho;

        public VectorIcon(String tipo, Color cor, int tamanho) {
            this.tipo = tipo;
            this.cor = cor;
            this.tamanho = tamanho;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(cor);

            double scale = (double) tamanho / 56.0;
            g2.translate(x, y);
            g2.scale(scale, scale);

            if ("professor".equals(tipo)) {
                // Diamond Top (Chapéu)
                int[] xs = { 12, 28, 44, 28 };
                int[] ys = { 22, 14, 22, 30 };
                g2.fillPolygon(xs, ys, 4);

                g2.setColor(cor.brighter());
                g2.setStroke(new BasicStroke(1f));
                g2.drawPolygon(xs, ys, 4);

                // Cap Base (arc below)
                g2.setColor(cor);
                g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawArc(19, 27, 18, 9, 0, -180);

                // Tassel
                g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(28, 22, 41, 25);
                g2.drawLine(41, 25, 41, 35);
                g2.fillOval(39, 34, 4, 4);

            } else if ("disciplina".equals(tipo)) {
                // Open Book
                g2.setStroke(new BasicStroke(2.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                // Pages
                g2.drawRoundRect(13, 16, 14, 21, 4, 4);
                g2.drawRoundRect(27, 16, 14, 21, 4, 4);

                // Lines
                g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(17, 22, 23, 22);
                g2.drawLine(17, 27, 23, 27);
                g2.drawLine(17, 32, 23, 32);

                g2.drawLine(31, 22, 37, 22);
                g2.drawLine(31, 27, 37, 27);
                g2.drawLine(31, 32, 37, 32);

            } else if ("turma".equals(tipo)) {
                // Group Silhouette
                // Left
                g2.setColor(new Color(cor.getRed(), cor.getGreen(), cor.getBlue(), 180));
                g2.fillOval(15, 22, 7, 7);
                g2.fillArc(11, 30, 15, 10, 0, 180);

                // Right
                g2.fillOval(34, 22, 7, 7);
                g2.fillArc(30, 30, 15, 10, 0, 180);

                // Center
                g2.setColor(cor.brighter());
                g2.fillOval(23, 16, 10, 10);
                g2.fillArc(17, 27, 22, 13, 0, 180);

                g2.setColor(cor);
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawOval(23, 16, 10, 10);
                g2.drawArc(17, 27, 22, 13, 0, 180);

            } else if ("turno".equals(tipo)) {
                // Clock
                g2.setStroke(new BasicStroke(2.8f));
                g2.drawOval(14, 14, 28, 28);

                g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(28, 28, 28, 20);
                g2.drawLine(28, 28, 34, 28);

            } else if ("gerar".equals(tipo)) {
                // Lightning Bolt
                g2.setStroke(new BasicStroke(2.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int[] xs = { 30, 39, 27, 29, 17, 29 };
                int[] ys = { 10, 25, 25, 46, 31, 31 };
                g2.drawPolygon(xs, ys, 6);
                g2.fillPolygon(xs, ys, 6);

            } else if ("visualizar".equals(tipo)) {
                // Checklist/Table
                g2.setStroke(new BasicStroke(2.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawRoundRect(15, 13, 26, 30, 4, 4);

                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                // Header line
                g2.drawLine(20, 20, 36, 20);
                // Lines with small check marks
                g2.drawLine(25, 27, 36, 27);
                g2.drawLine(25, 34, 36, 34);

                // Small checks or dots
                g2.fillOval(20, 26, 3, 3);
                g2.fillOval(20, 33, 3, 3);
            }

            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return tamanho;
        }

        @Override
        public int getIconHeight() {
            return tamanho;
        }
    }
}
