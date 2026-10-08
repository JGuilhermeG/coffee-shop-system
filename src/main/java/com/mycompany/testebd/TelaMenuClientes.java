package com.mycompany.testebd;

import javax.swing.*;
import java.awt.*;
/**
 * Interface gráfica principal do cliente.
 * Oferece opções: ver cardápio, fazer pedido, ver saldo e sair.
 */
public class TelaMenuClientes extends JFrame{
    public TelaMenuClientes(Cliente cliente){
        setTitle("Autoatendimento - Cliente");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 350);
        setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel(new GridLayout(4, 1, 10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JButton btnCardapio = new JButton("Ver Cardápio");
        JButton btnFazerPedido = new JButton("Fazer Pedido");
        JButton btnVerSaldo = new JButton("Ver Saldo");
        JButton btnSair = new JButton("Sair");

        btnCardapio.addActionListener(e -> cliente.visualizarCardapio());
        btnFazerPedido.addActionListener(e -> cliente.fazerPedido());
        btnVerSaldo.addActionListener(e -> cliente.verSaldo());
        btnSair.addActionListener(e -> voltarAoLogin());

        painelPrincipal.add(btnCardapio);
        painelPrincipal.add(btnFazerPedido);
        painelPrincipal.add(btnVerSaldo);
        painelPrincipal.add(btnSair);

        add(painelPrincipal);
        setVisible(true);
    }
    private void voltarAoLogin(){
        int confirmacao = JOptionPane.showConfirmDialog(this, "Deseja fazer logout e voltar à tela de login?", "Confirmar Saída", JOptionPane.YES_NO_OPTION);
        if(confirmacao == JOptionPane.YES_OPTION){this.dispose(); new TelaLogin();}
    }
}