package com.mycompany.testebd;

import javax.swing.JOptionPane;
import java.sql.*;
/**
 * Representa o usuário DONO (administrador).
 * Possui acesso total: cadastrar/alterar produtos, gerenciar funcionários.
 */
public class Dono extends Usuario{
    public Dono(int id, String nome, String senha){
        super(id, nome, senha,true);
    }

    @Override
    public void exibirMenu(){new TelaMenuDono(this);}// abre a janela principal do Dono

    @Override
    public String getDados(){return String.format("ADMINISTRADOR: %s (ID: %d)", getNome(), getId());}

    public void verCardapio(){new TelaListagemProdutos(false);}// Exibe cardápio completo (inclusive produtos sem estoque)
    /**
     * Cadastra um novo produto no banco de dados.
     * Utiliza a TelaCadastroProduto para coletar os dados.
     */
    public void cadastrarProduto(){
        TelaCadastroProduto dialog = new TelaCadastroProduto(null);
        if(dialog.isConfirmado()){
            String nomeProd = dialog.getNome();
            double preco = dialog.getPreco();
            int qtd = dialog.getQuantidade();
            ConnectionFactory factory = new ConnectionFactory();
            String sql = "INSERT INTO produtos (nome, preco, quantidade) VALUES (?, ?, ?)";
            try{
                Connection c = factory.obtemConexao();
                PreparedStatement stm = c.prepareStatement(sql);
                stm.setString(1, nomeProd);
                stm.setDouble(2, preco);
                stm.setInt(3, qtd);
                stm.executeUpdate();
                JOptionPane.showMessageDialog(null, "Produto " + nomeProd + " cadastrado!");
                new TelaListagemProdutos(false);// atualiza a listagem
                stm.close(); c.close();
            }
            catch(Exception e){JOptionPane.showMessageDialog(null, "Erro ao cadastrar: " + e.getMessage());}
        }
    }
    /**
     * Altera um produto existente: primeiro busca pelo ID, depois exibe diálogo de edição.
     */
    public void alterarProduto(){
        TelaListagemProdutos tela = new TelaListagemProdutos(false);
        try{
            String idStr = JOptionPane.showInputDialog("Digite o ID do produto (0 para cancelar):");
            if(idStr == null || idStr.equals("0")){tela.dispose(); return;}
            int id = Integer.parseInt(idStr);
            Connection c = new ConnectionFactory().obtemConexao();
            String sql = "SELECT nome, preco, quantidade FROM produtos WHERE id = ?";
            PreparedStatement stm = c.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if(!rs.next()){
                JOptionPane.showMessageDialog(null, "Produto não encontrado!");
                tela.dispose();
                return;
            }
            String nome = rs.getString("nome");
            double preco = rs.getDouble("preco");
            int qtd = rs.getInt("quantidade");
            String novoNome = JOptionPane.showInputDialog("Nome:", nome);
            if(novoNome == null){tela.dispose(); return;}
            String novoPrecoStr = JOptionPane.showInputDialog("Preço:", preco);
            if(novoPrecoStr == null){tela.dispose(); return;}
            String novaQtdStr = JOptionPane.showInputDialog("Quantidade:", qtd);
            if(novaQtdStr == null){tela.dispose(); return;}
            double novoPreco = Double.parseDouble(novoPrecoStr);
            int novaQtd = Integer.parseInt(novaQtdStr);
            String update = "UPDATE produtos SET nome = ?, preco = ?, quantidade = ? WHERE id = ?";
            PreparedStatement upd = c.prepareStatement(update);
            upd.setString(1, novoNome);
            upd.setDouble(2, novoPreco);
            upd.setInt(3, novaQtd);
            upd.setInt(4, id);
            upd.executeUpdate();
            JOptionPane.showMessageDialog(null, "Produto atualizado!");
            rs.close(); stm.close(); upd.close(); c.close(); tela.dispose();
        }
        catch(Exception e){JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());}
    }

    // Gerencia funcionários: abre submenu
    public void gerenciarFuncionarios(){
        new TelaGerenciarFuncionarios(null, this);
    }

    // Cadastra um novo funcionário
    public void cadastrarFuncionario(){
        TelaCadastroFuncionario dialog = new TelaCadastroFuncionario(null);
        if(dialog.isConfirmado()){
            String nome = dialog.getNome();
            String senha = dialog.getSenha();
            String cargo = dialog.getCargo();
            String sql = "INSERT INTO usuarios (nome, senha, tipo, cargo) VALUES (?, ?, 'FUNCIONARIO', ?)";
            ConnectionFactory factory = new ConnectionFactory();
            try{
                Connection c = factory.obtemConexao();
                PreparedStatement stm = c.prepareStatement(sql);
                stm.setString(1, nome);
                stm.setString(2, senha);
                stm.setString(3, cargo);
                stm.executeUpdate();
                JOptionPane.showMessageDialog(null, "Funcionário contratado!");
                stm.close(); c.close();
            }
            catch(Exception e){JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());}
        }
    }

    // Lista funcionários e permite demitir (excluir)
    public void listarEDemitir(){
        TelaListagemFuncionarios tela = new TelaListagemFuncionarios(null);
        try{
            String idStr = JOptionPane.showInputDialog("Digite o ID do funcionário para demitir (0 para sair)");
            if(idStr == null || idStr.equals("0")){tela.dispose(); return;}
            int id = Integer.parseInt(idStr);
            int confirm = JOptionPane.showConfirmDialog(null, "Deseja demitir funcionário ID " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if(confirm != JOptionPane.YES_OPTION){tela.dispose();return;}
            String sql = "DELETE FROM usuarios WHERE id = ? AND tipo = 'FUNCIONARIO'";
            Connection c = new ConnectionFactory().obtemConexao();
            PreparedStatement stm = c.prepareStatement(sql);
            stm.setInt(1, id);
            int linhas = stm.executeUpdate();
            JOptionPane.showMessageDialog(null, linhas > 0 ? "Funcionário demitido!" : "Funcionário não encontrado!");
            stm.close(); c.close(); tela.dispose();
        }
        catch(Exception e){JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());}
    }
}