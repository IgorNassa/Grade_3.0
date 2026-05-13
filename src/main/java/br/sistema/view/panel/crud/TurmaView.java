package br.sistema.view.panel.crud;

import javax.swing.*;
import java.awt.*;

public class TurmaView extends BaseCrudPanel {

    public TurmaView() {
        super();

        montarHeader();
        montarFormulario();
        montarBotoes();
        montarTabela();
    }

    private void montarHeader() {
        painelHeader.setLayout(new BorderLayout());
        painelHeader.setBackground(new Color(245, 247, 250));

        JLabel titulo = new JLabel("Cadastro de Turmas");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(new Color(29, 19, 60));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 24, 0, 0));

        painelHeader.add(titulo, BorderLayout.WEST);
    }

    private void montarFormulario() {
        painelFormulario.setLayout(new FlowLayout(FlowLayout.LEFT, 16, 24));
        painelFormulario.setBackground(new Color(245, 247, 250));

        painelFormulario.add(new JLabel("Nome:"));
        painelFormulario.add(new JTextField(15));

        painelFormulario.add(new JLabel("Ensino Médio:"));
        painelFormulario.add(new JCheckBox());

        painelFormulario.add(new JLabel("Turno:"));
        painelFormulario.add(new JComboBox<>(new String[]{
                "Manhã",
                "Tarde",
                "Noite"
        }));
    }

    private void montarBotoes() {
        painelBotoes.setLayout(new FlowLayout(FlowLayout.LEFT, 16, 8));
        painelBotoes.setBackground(new Color(245, 247, 250));

        painelBotoes.add(new JButton("Salvar"));
        painelBotoes.add(new JButton("Editar"));
        painelBotoes.add(new JButton("Excluir"));
        painelBotoes.add(new JButton("Limpar"));
    }

    private void montarTabela() {
        painelTabela.setLayout(new BorderLayout());

        String[] colunas = {
                "ID",
                "Nome",
                "Ensino Médio",
                "Turno"
        };

        Object[][] dados = {
                {1, "1A", "Sim", "Manhã"},
                {2, "2B", "Sim", "Tarde"}
        };

        JTable tabela = new JTable(dados, colunas);

        painelTabela.add(new JScrollPane(tabela), BorderLayout.CENTER);
    }
}