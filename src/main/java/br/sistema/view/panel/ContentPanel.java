package br.sistema.view.panel;


import javax.swing.*;
import java.awt.*;
import br.sistema.view.panel.crud.DisciplinaView;

public class ContentPanel extends JPanel{

    private static final Color COR_BG = new Color(245, 247, 250);

    public ContentPanel(){
        configurarPanel();
        montarConteudo();
        add(new DisciplinaView(), BorderLayout.CENTER);
    }

    private void configurarPanel(){

        setBackground(COR_BG);
        setLayout(new BorderLayout());

    }

    private void montarConteudo(){
        JLabel lblConteudo = new JLabel("Area de conteudo");

        lblConteudo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblConteudo.setForeground(new Color(120, 120, 140));

        lblConteudo.setHorizontalAlignment(SwingConstants.CENTER);

        add(lblConteudo, BorderLayout.CENTER);
    }




}
