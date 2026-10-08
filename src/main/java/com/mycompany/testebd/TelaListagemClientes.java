package com.mycompany.testebd;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
/**
 * Tela que exibe todos os clientes cadastrados (ID, nome e saldo).
 */
public class TelaListagemClientes extends JFrame{
    public TelaListagemClientes(){
        super("Clientes Cadastrados");
        setSize(550, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] colunas = {"ID", "Nome", "Saldo (R$)"};
        DefaultTableModel modelo = new DefaultTableModel(colunas, 0);
        JTable tabela = new JTable(modelo);

        ArrayList<Object[]> clientes = CafeteriaUtilidades.getClientesArray();
        
        for(Object[] linha : clientes){
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