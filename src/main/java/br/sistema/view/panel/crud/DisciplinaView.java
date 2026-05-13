package br.sistema.view.panel.crud;

import br.sistema.view.theme.ThemeColors;
import br.sistema.view.theme.ThemeFonts;

import javax.swing.*;
import java.awt.*;

public class DisciplinaView extends BaseCrudPanel {

    public DisciplinaView() {
        super();

        montarHeader();
        montarFormulario();
        montarBotoes();
        montarTabela();
    }

    private void montarHeader() {

        painelHeader.setLayout(new BorderLayout());

        painelHeader.setBackground(
                ThemeColors.BACKGROUND
        );

        JLabel titulo =
                new JLabel("Cadastro de Disciplinas");

        titulo.setFont(ThemeFonts.TITLE);

        titulo.setForeground(
                ThemeColors.TEXT_TITLE
        );

        titulo.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        24,
                        0,
                        0
                )
        );

        painelHeader.add(
                titulo,
                BorderLayout.WEST
        );
    }

    private void montarFormulario() {

        painelFormulario.setLayout(
                new FlowLayout(
                        FlowLayout.LEFT,
                        16,
                        24
                )
        );

        painelFormulario.setBackground(
                ThemeColors.CARD
        );

        JLabel lblNome = new JLabel("Nome:");

        lblNome.setFont(ThemeFonts.BODY);

        lblNome.setForeground(
                ThemeColors.TEXT
        );

        JTextField txtNome =
                new JTextField(25);

        JLabel lblCarga =
                new JLabel("Carga Horária:");

        lblCarga.setFont(ThemeFonts.BODY);

        lblCarga.setForeground(
                ThemeColors.TEXT
        );

        JTextField txtCarga =
                new JTextField(10);

        painelFormulario.add(lblNome);
        painelFormulario.add(txtNome);

        painelFormulario.add(lblCarga);
        painelFormulario.add(txtCarga);
    }

    private void montarBotoes() {

        painelBotoes.setLayout(
                new FlowLayout(
                        FlowLayout.LEFT,
                        16,
                        8
                )
        );

        painelBotoes.setBackground(
                ThemeColors.BACKGROUND
        );

        painelBotoes.add(
                new JButton("Salvar")

        );

        painelBotoes.add(
                new JButton("Editar")
        );

        painelBotoes.add(
                new JButton("Excluir")
        );

        painelBotoes.add(
                new JButton("Limpar")
        );
    }

    private void montarTabela() {

        painelTabela.setLayout(
                new BorderLayout()
        );

        String[] colunas = {
                "ID",
                "Nome",
                "Carga Horária"
        };

        Object[][] dados = {
                {1, "Matemática", 80},
                {2, "Português", 80}
        };

        JTable tabela =
                new JTable(dados, colunas);

        painelTabela.add(
                new JScrollPane(tabela),
                BorderLayout.CENTER
        );
    }
}