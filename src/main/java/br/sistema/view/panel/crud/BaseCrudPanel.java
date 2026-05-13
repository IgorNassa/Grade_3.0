package br.sistema.view.panel.crud;

import javax.swing.*;
import java.awt.*;

import br.sistema.view.theme.ThemeColors;



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
             setBackground(ThemeColors.BACKGROUND);
    }

    private void mostrarEstrutura(){

        painelHeader = new JPanel();
        painelFormulario = new JPanel();
        painelBotoes = new JPanel();
        painelTabela = new JPanel();

        painelHeader.setPreferredSize(new Dimension(0, 80));
        painelHeader.setBackground(ThemeColors.BACKGROUND);

        painelFormulario.setPreferredSize(new Dimension(0, 140));
        painelFormulario.setBackground(ThemeColors.CARD);

        painelBotoes.setPreferredSize(new Dimension(0, 60));
        painelBotoes.setBackground(ThemeColors.BACKGROUND);

        painelTabela.setBackground(ThemeColors.CARD);

        JPanel topo = new JPanel();
        topo.setLayout(new BoxLayout(topo, BoxLayout.Y_AXIS));

        topo.setBorder(
                BorderFactory.createEmptyBorder(
                        18,
                        18,
                        18,
                        18
                )
        );

        topo.add(painelHeader);
        topo.add(painelFormulario);
        topo.add(painelBotoes);

        add(topo, BorderLayout.NORTH);
        add(painelTabela, BorderLayout.CENTER);


    }
}
