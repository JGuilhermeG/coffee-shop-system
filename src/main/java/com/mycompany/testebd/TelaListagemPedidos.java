package com.mycompany.testebd;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
/**
 * Tela que exibe todos os pedidos em uma tabela (ID, cliente, status, total, data).
 * Permite visualizar os itens de um pedido selecionado.
 */
public class TelaListagemPedidos extends JFrame{
    private JTable tabela;
    private DefaultTableModel modelo;

    public TelaListagemPedidos(){
        super("Pedidos Cadastrados");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] colunas = {"ID", "Cliente", "Status", "Total (R$)", "Data"};
        modelo = new DefaultTableModel(colunas, 0){
            @Override
            public boolean isCellEditable(int row, int column){return false;}// impede edição direta na tabela
        };
        tabela = new JTable(modelo);
        carregarDados();

        JScrollPane scroll = new JScrollPane(tabela);
        add(scroll, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout());
        JButton btnVerItens = new JButton("Ver Itens do Pedido");
        JButton btnFechar = new JButton("Fechar");

        btnVerItens.addActionListener(e -> verItensPedido());
        btnFechar.addActionListener(e -> dispose());

        painelBotoes.add(btnVerItens);
        painelBotoes.add(btnFechar);
        add(painelBotoes, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void carregarDados(){
        modelo.setRowCount(0);
        ArrayList<Object[]> pedidos = CafeteriaUtilidades.getPedidosArray();

        for(Object[] linha : pedidos){
            modelo.addRow(linha);
        }
    }

    public void verItensPedido() {
        try{
            String idStr = JOptionPane.showInputDialog("Digite o ID do pedido (0 para cancelar):");
            if(idStr == null || idStr.equals("0")){return;}
            int id = Integer.parseInt(idStr);
            if(id <= 0){JOptionPane.showMessageDialog(null, "ID inválido!"); return;}

            new TelaItensPedido(null, id);
        }
        catch(NumberFormatException e){JOptionPane.showMessageDialog(null, "Digite apenas números válidos!");}
    }
}