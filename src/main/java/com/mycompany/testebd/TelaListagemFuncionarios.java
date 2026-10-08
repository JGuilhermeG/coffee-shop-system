package com.mycompany.testebd;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
/**
 * Tela que lista os funcionários cadastrados.
 * A tabela é desabilitada para seleção (apenas visualização).
 * Usada pelo Dono antes de demitir.
 */
public class TelaListagemFuncionarios extends JDialog{
    private DefaultTableModel modelo;
    private JTable tabela;

    public TelaListagemFuncionarios(JFrame parent){
        super(parent, "Funcionários Cadastrados", true);
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);

        String[] colunas = {"ID", "Nome", "Cargo"};
        modelo = new DefaultTableModel(colunas, 0);
        tabela = new JTable(modelo);

        tabela.setEnabled(false);// impede seleção (apenas visualização)

        carregarDados();

        JScrollPane scroll = new JScrollPane(tabela);
        add(scroll, BorderLayout.CENTER);

        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> dispose());
        add(btnFechar, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void carregarDados(){
        modelo.setRowCount(0);
        ArrayList<Object[]> funcionarios = CafeteriaUtilidades.getFuncionariosArray();
        for(Object[] linha : funcionarios){
            modelo.addRow(linha);
        }
    }
}