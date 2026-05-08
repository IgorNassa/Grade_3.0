package br.sistema.view.panel;

import javax.swing.*;
import java.awt.*;



public class SidebarPanel extends  JPanel {

    /* config base*/
    private static final int LARGURA_SIDEBAR = 250;
    private static final int ALTURA_SIDEBAR = 720;

    private static final Color COR_BG = new Color(28, 17, 41);
    private static final Color COR_MENU_A = new Color(123, 63, 125);
    private static final Color COR_TXT = Color.WHITE;
    private static final Color COR_TXT_SEC = new Color(170, 160, 185);

    public SidebarPanel(){
        configurarSidebar();
        montarTopo();
        MontarMenu();
    }

    private void configurarSidebar(){

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(LARGURA_SIDEBAR, ALTURA_SIDEBAR));
        setBackground(COR_BG);

    }

    private void montarTopo(){
        add(Box.createVerticalStrut(20));

        JLabel lblLogo = new JLabel("LOGO");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblLogo.setForeground(COR_TXT);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(lblLogo);
        add(Box.createVerticalStrut(12));

        JLabel lblEscola = new JLabel("COLEGIO ESTADUAL");
        lblEscola.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblEscola.setForeground(COR_TXT);
        lblEscola.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblNome = new JLabel("PROF. FLÁVIO WARKEN");
        lblNome.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblNome.setForeground(COR_TXT);
        lblNome.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(lblEscola);
        add(Box.createVerticalStrut(4));
        add(lblNome);

        add(Box.createVerticalStrut(28));

    }

    private void MontarMenu (){
        add(criarBotaoMenu("Dashboard",true ));

        add(Box.createVerticalStrut(24));
        add(criarTituloSecao("CADASTROS"));

        add(criarBotaoMenu("Professores",false));
        add(criarBotaoMenu("Turmas",false));
        add(criarBotaoMenu("Turnos",false));

        add(Box.createVerticalStrut(16));
        add(criarTituloSecao("GRADE"));


        add(criarBotaoMenu("Gerar Grade", false));
        add(criarBotaoMenu("Visualizar Grade", false));

        add(Box.createVerticalStrut(16));
        add(criarTituloSecao("CONFIGURACOES"));
        add(criarBotaoMenu("Config", false));


    }

    private JLabel criarTituloSecao(String txt){
        JLabel label = new JLabel(txt);
        label.setFont(new Font("Segoe UI", Font.BOLD,11));
        label.setForeground(COR_TXT_SEC);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setMaximumSize(new Dimension(210, 28));
        return label;
    }


    private JButton criarBotaoMenu(String txt , boolean ativo){
        JButton botao = new JButton(txt);

        botao.setMaximumSize(new Dimension(215, 42));
        botao.setPreferredSize(new Dimension(215, 42));
        botao.setAlignmentX(Component.CENTER_ALIGNMENT);

        botao.setFont(new Font("Segoe UI", Font.BOLD, 13));
        botao.setForeground(COR_TXT);
        botao.setBackground(ativo ? COR_MENU_A : COR_BG);

        botao.setFocusPainted(false);
        botao.setBorderPainted(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return botao;
    }
}
