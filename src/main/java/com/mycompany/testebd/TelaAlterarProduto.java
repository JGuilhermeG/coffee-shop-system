package com.mycompany.testebd;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
/**
 * Tela que exibe todos os produtos em uma tabela e permite selecionar um para editar.
 * Utilizada pelo Dono no menu "Alterar Produto".
 * O usuário seleciona uma linha, clica em "Editar Produto Selecionado" e um diálogo
 * (TelaEditarProduto) é aberto para modificar nome, preço ou quantidade.
 */
public class TelaAlterarProduto extends JDialog{
    private JTable tabela;
    private DefaultTableModel modelo;

    public TelaAlterarProduto(JFrame parent){
        super(parent, "Alterar Produto", true);// true = janela modal (bloqueia a tela pai)
        setSize(750, 500);
        setLocationRelativeTo(parent);
        JPanel painelCampos = new JPanel(new GridLayout(4, 2, 5, 5));
        painelCampos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] colunas = {"ID", "Nome", "Preço (R$)", "Estoque"};
        modelo = new DefaultTableModel(colunas, 0);
        tabela = new JTable(modelo);
        carregarDados();// Preenche a tabela com os produtos do BD

        JScrollPane scroll = new JScrollPane(tabela);
        add(scroll, BorderLayout.CENTER);

        // Botões ficam na parte inferior
        JPanel painelBotoes = new JPanel(new FlowLayout());
        JButton btnEditar = new JButton("Editar Produto Selecionado");
        JButton btnFechar = new JButton("Fechar");

        btnEditar.addActionListener(e -> editarProduto());
        btnFechar.addActionListener(e -> dispose());

        painelBotoes.add(btnEditar);
        painelBotoes.add(btnFechar);
        add(painelBotoes, BorderLayout.SOUTH);

        setVisible(true);
    }

    /**
     * Carrega os dados do banco de dados e os insere no modelo da tabela.
     * Utiliza o método utilitário CafeteriaUtilidades.getProdutosArray(false)
     * que retorna todos os produtos (inclusive com estoque zero).
     */
    private void carregarDados(){
        modelo.setRowCount(0);// Limpa todas as linhas existentes
        ArrayList<Object[]> produtos = CafeteriaUtilidades.getProdutosArray(false);
        for (Object[] linha : produtos){
            modelo.addRow(linha);
        }
    }

    /**
     * Método chamado ao clicar em "Editar Produto Selecionado".
     * Obtém a linha selecionada na tabela, extrai os dados do produto,
     * abre a TelaEditarProduto e, se o usuário confirmar a edição,
     * atualiza o banco de dados e recarrega a tabela.
     */
    private void editarProduto(){
        int linhaSelecionada = tabela.getSelectedRow();
        if(linhaSelecionada == -1){
            JOptionPane.showMessageDialog(this, "Selecione um produto na tabela.");
            return;
        }
        // Extrai os valores da linha selecionada
        int id = (int)tabela.getValueAt(linhaSelecionada, 0);
        String nome = (String)tabela.getValueAt(linhaSelecionada, 1);
        double preco = (double)tabela.getValueAt(linhaSelecionada, 2);
        int quantidade = (int)tabela.getValueAt(linhaSelecionada, 3);

        // Abre a janela de edição (modal) com os dados atuais
        TelaEditarProduto dialog = new TelaEditarProduto(null, id, nome, preco, quantidade);
        if(dialog.isConfirmado()){
            // Se o usuário clicou em "Salvar" na tela de edição, aplica as alterações
            String sql = "UPDATE produtos SET nome = ?, preco = ?, quantidade = ? WHERE id = ?";
            ConnectionFactory factory = new ConnectionFactory();
            try{
                Connection c = factory.obtemConexao();
                PreparedStatement stm = c.prepareStatement(sql);
                stm.setString(1, dialog.getNome());
                stm.setDouble(2, dialog.getPreco());
                stm.setInt(3, dialog.getQuantidade());
                stm.setInt(4, id);
                stm.executeUpdate();
                JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!");
                stm.close();c.close();
                carregarDados(); // recarrega a tabela (mas ela não está visível)
            }
            catch (Exception e){JOptionPane.showMessageDialog(this, "Erro ao atualizar: " + e.getMessage());}
        }
    }
}