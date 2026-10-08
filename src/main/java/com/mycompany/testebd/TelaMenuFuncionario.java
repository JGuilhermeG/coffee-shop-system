package com.mycompany.testebd;

import javax.swing.*;
import java.awt.*;
/**
 * Interface gráfica principal do funcionário.
 * Opções: ver cardápio, atender cliente (atualizar status) e sair.
 */
public class TelaMenuFuncionario extends JFrame {

    public TelaMenuFuncionario(Funcionario funcionario){
        setTitle("Painel Operacional - Funcionário");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 350);
        setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel(new GridLayout(3, 1, 10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JButton btnCardapio = new JButton("Ver Cardápio");
        JButton btnAtenderCliente = new JButton("Atender Cliente (Status)");
        JButton btnSair = new JButton("Sair");

        btnCardapio.addActionListener(e -> funcionario.verCardapio());
        btnAtenderCliente.addActionListener(e -> funcionario.atenderCliente());
        btnSair.addActionListener(e -> voltarAoLogin());

        painelPrincipal.add(btnCardapio);
        painelPrincipal.add(btnAtenderCliente);
        painelPrincipal.add(btnSair);

        add(painelPrincipal);
        setVisible(true);
    }
    private void voltarAoLogin(){
        int confirmacao = JOptionPane.showConfirmDialog(this, "Deseja fazer logout e voltar à tela de login?", "Confirmar Saída", JOptionPane.YES_NO_OPTION);
        if(confirmacao == JOptionPane.YES_OPTION){this.dispose(); new TelaLogin();}
    }
}