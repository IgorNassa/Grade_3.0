package br.sistema.view.panel;
import br.sistema.view.component.MenuButton;
import br.sistema.view.util.IconUtil;

import javax.swing.*;
import java.awt.*;



public class SidebarPanel extends  JPanel {

    /* config base*/
    private static final int LARGURA_SIDEBAR = 250;
    private static final int ALTURA_SIDEBAR = 720;
    private static final Color COR_BG = new Color(29, 19, 60);
    private static final Color COR_TXT = Color.WHITE;
    private static final Color COR_TXT_SEC = new Color(71, 57, 105);


    public SidebarPanel(){
        configurarSidebar();
        montarTopo();
        montarMenu();
        montarRodape();
        configurarEventos();
    }

    //=======BUTTONS=======//
    private MenuButton btnDashboard;
    private MenuButton btnProfessores;
    private MenuButton btnDisciplinas;
    private MenuButton btnTurmas;
    private MenuButton btnTurnos;
    private MenuButton btnGerarGrade;
    private MenuButton btnVisualizar;
    private MenuButton btnConfig;





    private void configurarSidebar(){

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(LARGURA_SIDEBAR, ALTURA_SIDEBAR));
        setBackground(COR_BG);

    }

    private void montarTopo(){
        add(Box.createVerticalStrut(20));

        JLabel lblLogo = new JLabel(
                IconUtil.carregarIcone(
                        "/icons/logo.png",
                        120,
                        120
                )
        );
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblLogo.setForeground(COR_TXT);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(lblLogo);
        add(Box.createVerticalStrut(10));

        JLabel lblEscola = new JLabel("COLÉGIO ESTADUAL");
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

    private void montarMenu (){
        btnDashboard = new MenuButton(
                "Dashboard",
                IconUtil.carregarIcone("/icons/book.png", 24, 24),
                true
        );

        add(btnDashboard);

        add(Box.createVerticalStrut(24));
        add(criarTituloSecao("CADASTROS"));

        btnProfessores = new MenuButton("Professores", IconUtil.carregarIcone("/icons/professor.png", 24, 24), false);
        add(btnProfessores);

        add(Box.createVerticalStrut(6));

        btnDisciplinas = new MenuButton("Disciplinas", IconUtil.carregarIcone("/icons/disciplinas.png",24, 24),false);
        add(btnDisciplinas);

        add(Box.createVerticalStrut(6));

        btnTurmas = new MenuButton("Turmas",IconUtil.carregarIcone("/icons/professor.png", 24, 24),false);
        add(btnTurmas);

        add(Box.createVerticalStrut(6));

        btnTurnos = new MenuButton("Turnos",IconUtil.carregarIcone("/icons/turnos.png", 24, 24), false);
        add(btnTurnos);


        add(Box.createVerticalStrut(16));
        add(criarTituloSecao("GRADE"));


        btnGerarGrade = new MenuButton("Gerar Grade",IconUtil.carregarIcone("/icons/grade.png", 24, 24),  false);
        add(btnGerarGrade);

        add(Box.createVerticalStrut(6));

        btnVisualizar = new MenuButton("Visualizar Grade",IconUtil.carregarIcone("/icons/visualizar.png", 24, 24),  false);
        add(btnVisualizar);

        add(Box.createVerticalStrut(20));
        add(criarTituloSecao("CONFIGURAÇÕES"));

       btnConfig = new MenuButton("Configurações", IconUtil.carregarIcone("/icons/config.png", 24, 24), false);
       add(btnConfig);

    }

    private JLabel criarTituloSecao(String txt){
        JLabel label = new JLabel(txt);

        label.setFont(new Font("Segoe UI", Font.PLAIN,11));
        label.setForeground(COR_TXT_SEC);

        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        label.setMaximumSize(new Dimension(215, 28));
        label.setPreferredSize(new Dimension(215, 28));

        label.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 0));

        return label;
    }

    private void ativarBotao(MenuButton botaoSelecionado){

        btnDashboard.setAtivo(false);
        btnProfessores.setAtivo(false);
        btnDisciplinas.setAtivo(false);
        btnTurmas.setAtivo(false);
        btnTurnos.setAtivo(false);
        btnGerarGrade.setAtivo(false);
        btnVisualizar.setAtivo(false);
        btnConfig.setAtivo(false);

        botaoSelecionado.setAtivo(true);
    }

    private void configurarEventos(){
        btnDashboard.addActionListener(e -> ativarBotao(btnDashboard));
        btnProfessores.addActionListener(e -> ativarBotao(btnProfessores));
        btnDisciplinas.addActionListener(e -> ativarBotao(btnDisciplinas));
        btnTurmas.addActionListener(e -> ativarBotao(btnTurmas));
        btnTurnos.addActionListener(e -> ativarBotao(btnTurnos));
        btnGerarGrade.addActionListener(e -> ativarBotao(btnGerarGrade));
        btnVisualizar.addActionListener(e -> ativarBotao(btnVisualizar));
        btnConfig.addActionListener(e -> ativarBotao(btnConfig));
    }

    private void montarRodape(){
        add(Box.createVerticalGlue());

        JLabel lblVersao = new JLabel("SGDG v3.0");
        lblVersao.setFont(new  Font("Segoe UI", Font.PLAIN, 11));
        lblVersao.setForeground(COR_TXT_SEC);
        lblVersao.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(lblVersao);
        add(Box.createVerticalStrut(18));
    }

}
