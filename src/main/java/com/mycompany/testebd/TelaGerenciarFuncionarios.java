package com.mycompany.testebd;

import javax.swing.*;
import java.awt.*;
/**
 * Tela que apresenta opções para cadastrar novo funcionário ou listar/demitir.
 * É chamada pelo Dono.
 */
public class TelaGerenciarFuncionarios extends JDialog{

    public TelaGerenciarFuncionarios(JFrame parent, Dono dono){
        super(parent, "Gerenciar Funcionários", false);
        setSize(300, 200);
        setLocationRelativeTo(parent);

        JPanel painelBotoes = new JPanel(new GridLayout(3, 1, 10, 10));
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JButton btnCadastrar = new JButton("Cadastrar Novo Funcionário");
        JButton btnListarDemitir = new JButton("Listar / Demitir");
        JButton btnVoltar = new JButton("Voltar");

        btnCadastrar.addActionListener(e -> dono.cadastrarFuncionario());
        btnListarDemitir.addActionListener(e -> dono.listarEDemitir());
        btnVoltar.addActionListener(e -> dispose());

        painelBotoes.add(btnCadastrar);
        painelBotoes.add(btnListarDemitir);
        painelBotoes.add(btnVoltar);

        add(painelBotoes, BorderLayout.CENTER);
        setVisible(true);
    }
}