package com.mycompany.testebd;

import javax.swing.*;
import java.awt.*;
/**
 * Interface gráfica principal do administrador (Dono).
 * Opções: ver cardápio, cadastrar produto, alterar produto,
 * gerenciar funcionários e sair.
 */
public class TelaMenuDono extends JFrame{
    public TelaMenuDono(Dono dono){
        setTitle("Painel Administrativo - Cafeteria");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 350);
        setLocationRelativeTo(null);
        JPanel painelPrincipal = new JPanel(new GridLayout(5, 1, 10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JButton btnCardapio = new JButton("Ver Cardápio");
        JButton btnCadastrar = new JButton("Cadastrar Produto");
        JButton btnAlterar = new JButton("Alterar Produto");
        JButton btnGerenciarFunc = new JButton("Gerenciar Funcionários");
        JButton btnSair = new JButton("Sair");

        btnCardapio.addActionListener(e -> dono.verCardapio());
        btnCadastrar.addActionListener(e -> dono.cadastrarProduto());
        btnAlterar.addActionListener(e -> dono.alterarProduto());
        btnGerenciarFunc.addActionListener(e -> dono.gerenciarFuncionarios());
        btnSair.addActionListener(e -> voltarAoLogin());

        painelPrincipal.add(btnCardapio);
        painelPrincipal.add(btnCadastrar);
        painelPrincipal.add(btnAlterar);
        painelPrincipal.add(btnGerenciarFunc);
        painelPrincipal.add(btnSair);

        add(painelPrincipal);
        setVisible(true);
    }
    private void voltarAoLogin(){
        int confirmacao = JOptionPane.showConfirmDialog(this, "Deseja fazer logout e voltar à tela de login?", "Confirmar Saída", JOptionPane.YES_NO_OPTION);
        if(confirmacao == JOptionPane.YES_OPTION){this.dispose(); new TelaLogin();}
    }
}