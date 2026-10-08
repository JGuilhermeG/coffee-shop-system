package com.mycompany.testebd;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
/**
 * Janela que mostra todos os produtos de um determinado pedido,
 * com quantidade, preço unitário e subtotal.
 */
public class TelaItensPedido extends JDialog{
    public TelaItensPedido(JFrame parent, int pedidoId){
        super(parent, "Itens do Pedido #" + pedidoId, true);

        setSize(500, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        String[] colunas = {"Produto", "Quantidade", "Preço Unitário", "Subtotal"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);

        JTable tabelaItens = new JTable(model);

        ArrayList<Object[]> itens = CafeteriaUtilidades.getItensPedidoArray(pedidoId);

        if(itens.isEmpty()){JOptionPane.showMessageDialog(this, "Nenhum item encontrado para este pedido.");}

        for(Object[] linha : itens){
            model.addRow(linha);
        }

        add(new JScrollPane(tabelaItens), BorderLayout.CENTER);

        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> dispose());
        add(btnFechar, BorderLayout.SOUTH);

        setVisible(true);
    }
}