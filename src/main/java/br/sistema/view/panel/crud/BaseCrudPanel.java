package br.sistema.view.panel.crud;

import javax.swing.*;
import java.awt.*;

public class BaseCrudPanel extends JPanel {

    protected JPanel painelHeader;
    protected JPanel painelFormulario;
    protected JPanel painelBotoes;
    protected JPanel painelTabela;

    public BaseCrudPanel(){
        configurarPainel();
        mostrarEstrutura();
    }

    private void configurarPainel(){
            setLayout(new BorderLayout());
            setBackground(new Color(245, 247, 250));
    }

    private void mostrarEstrutura(){

        painelHeader = new JPanel();
        painelFormulario = new JPanel();
        painelBotoes = new JPanel();
        painelTabela = new JPanel();

        painelHeader.setPreferredSize(new Dimension(0, 80));

        painelFormulario.setPreferredSize(new Dimension(0, 140));

        painelBotoes.setPreferredSize(new Dimension(0, 60));

        painelTabela.setBackground(Color.WHITE);

        JPanel topo = new JPanel();
        topo.setLayout(new BoxLayout(topo, BoxLayout.Y_AXIS));

        topo.add(painelHeader);
        topo.add(painelFormulario);
        topo.add(painelBotoes);

        add(topo, BorderLayout.NORTH);
        add(painelTabela, BorderLayout.CENTER);


    }
}
