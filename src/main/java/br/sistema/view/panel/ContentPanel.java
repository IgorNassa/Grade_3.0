package br.sistema.view.panel;

import br.sistema.view.panel.crud.DisciplinaView;
import br.sistema.view.panel.crud.TurnoView;

import javax.swing.*;
import java.awt.*;

public class ContentPanel extends JPanel {

    private static final Color COR_BG = new Color(245, 247, 250);

    private CardLayout cardLayout;

    public ContentPanel() {
        configurarPanel();
        montarConteudo();
    }

    private void configurarPanel() {
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        setBackground(COR_BG);
    }

    private void montarConteudo() {
        add(new DisciplinaView(), "DISCIPLINA");
        add(new TurnoView(), "TURNO");

        mostrarDisciplina();
    }

    public void mostrarDisciplina() {
        cardLayout.show(this, "DISCIPLINA");
    }

    public void mostrarTurno() {
        cardLayout.show(this, "TURNO");
    }
}