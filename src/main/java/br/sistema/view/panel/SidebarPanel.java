package br.sistema.view.panel;
import br.sistema.view.component.MenuButton;
import javax.swing.*;
import java.awt.*;



public class SidebarPanel extends  JPanel {

    /* config base*/
    private static final int LARGURA_SIDEBAR = 250;
    private static final int ALTURA_SIDEBAR = 720;
    private static final Color COR_BG = new Color(28, 17, 41);
    private static final Color COR_TXT = Color.WHITE;
    private static final Color COR_TXT_SEC = new Color(170, 160, 185);


    public SidebarPanel(){
        configurarSidebar();
        montarTopo();
        montarMenu();
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

        JLabel lblLogo = new JLabel("LOGO");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblLogo.setForeground(COR_TXT);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(lblLogo);
        add(Box.createVerticalStrut(12));

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
        btnDashboard = new MenuButton("Dashboard", true);
        add(btnDashboard);

        add(Box.createVerticalStrut(24));
        add(criarTituloSecao("CADASTROS"));

        btnProfessores = new MenuButton("Professores",false);
        add(btnProfessores);
        btnDisciplinas = new MenuButton("Disciplinas",false);
        add(btnDisciplinas);
        btnTurmas = new MenuButton("Turmas",false);
        add(btnTurmas);
        btnTurnos = new MenuButton("Turnos",false);
        add(btnTurnos);

        add(Box.createVerticalStrut(16));
        add(criarTituloSecao("GRADE"));


        btnGerarGrade = new MenuButton("Gerar Grade", false);
        add(btnGerarGrade);
        btnVisualizar = new MenuButton("Visualizar Grade", false);
        add(btnVisualizar);

        add(Box.createVerticalStrut(16));
        add(criarTituloSecao("CONFIGURAÇÕES"));

       btnConfig = new MenuButton("Configurações", false);
       add(btnConfig);

    }

    private JLabel criarTituloSecao(String txt){
        JLabel label = new JLabel(txt);

        label.setFont(new Font("Segoe UI", Font.BOLD,11));
        label.setForeground(COR_TXT_SEC);

        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        label.setMaximumSize(new Dimension(215, 28));
        label.setPreferredSize(new Dimension(215, 28));

        label.setBorder(BorderFactory.createEmptyBorder(0, 22, 0, 0));

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

}
