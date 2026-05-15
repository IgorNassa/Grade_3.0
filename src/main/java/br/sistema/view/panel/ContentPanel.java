package br.sistema.view.panel;

import br.sistema.controller.dtos.TurmaDTO;
import br.sistema.view.panel.crud.*;
import br.sistema.view.util.AppTheme;

import javax.swing.*;
import java.awt.*;

/**
 * Painel de conteúdo principal — usa CardLayout para alternar entre as telas.
 */
public class ContentPanel extends JPanel {

    private CardLayout cardLayout;
    private GradeView gradeView; // Referência armazenada para comunicação direta

    // Chaves de cada card
    public static final String DASHBOARD    = "DASHBOARD";
    public static final String PROFESSORES  = "PROFESSORES";
    public static final String DISCIPLINAS  = "DISCIPLINAS";
    public static final String TURMAS       = "TURMAS";
    public static final String TURNOS       = "TURNOS";
    public static final String GERAR_GRADE  = "GERAR_GRADE";
    public static final String VER_GRADE    = "VER_GRADE";

    public ContentPanel() {
        configurarPanel();
        montarConteudo();
    }

    private void configurarPanel() {
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        setBackground(AppTheme.BG_CONTENT);
    }

    private void montarConteudo() {
        this.gradeView = new GradeView(); // Instância inicializada sem parâmetros

        add(new DashboardView(),   DASHBOARD);
        add(new ProfessorView(),   PROFESSORES);
        add(new DisciplinaView(),  DISCIPLINAS);
        add(new TurmaView(),       TURMAS);
        add(new TurnoView(),       TURNOS);
        add(new GerarGradeView(),  GERAR_GRADE);
        add(this.gradeView,        VER_GRADE);

        mostrar(DASHBOARD);
    }

    public void mostrar(String chave) {
        cardLayout.show(this, chave);
    }

    /**
     * Sincroniza o componente de destino com a turma gerada e redireciona a interface.
     */
    public void exibirGradeDaTurma(TurmaDTO turma) {
        gradeView.focarTurmaERecarregar(turma);
        mostrar(VER_GRADE);
    }

    // Atalhos de compatibilidade
    public void mostrarDisciplina()  { mostrar(DISCIPLINAS); }
    public void mostrarTurno()       { mostrar(TURNOS); }
}