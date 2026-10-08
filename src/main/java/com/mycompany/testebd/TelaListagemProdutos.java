package com.mycompany.testebd;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
/**
 * Tela que lista os produtos, podendo mostrar apenas os com estoque (true)
 * ou todos (false). Utilizada por Cliente, Funcionário e Dono.
 */
public class TelaListagemProdutos extends JFrame {
    public TelaListagemProdutos(boolean apenasComEstoque){
        super(apenasComEstoque ? "Cardápio - Produtos disponíveis" : "Todos os produtos");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] colunas = {"ID", "Nome", "Preço (R$)", "Estoque"};
        DefaultTableModel modelo = new DefaultTableModel(colunas, 0);
        JTable tabela = new JTable(modelo);

        ArrayList<Object[]> produtos = CafeteriaUtilidades.getProdutosArray(apenasComEstoque);
        for(Object[] linha : produtos){
            modelo.addRow(linha);
        }

        JScrollPane scroll = new JScrollPane(tabela);
        add(scroll, BorderLayout.CENTER);

        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> dispose());
        add(btnFechar, BorderLayout.SOUTH);

        setVisible(true);
    }
}