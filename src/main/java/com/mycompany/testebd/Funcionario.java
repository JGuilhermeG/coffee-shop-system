package com.mycompany.testebd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
/**
 * Representa um funcionário da cafeteria.
 * Pode visualizar cardápio (apenas itens com estoque), atualizar status de pedidos e ver itens de um pedido.
 */
public class Funcionario extends Usuario{
    public Funcionario(int id, String nome, String senha, String cargo) {
        super(id, nome, senha, cargo);
    }

    @Override
    public void exibirMenu(){new TelaMenuFuncionario(this);}

    @Override
    public String getDados(){return String.format("Funcionário: %s | Cargo: %s", getNome(), getCargo());}

    // Exibe apenas produtos com estoque > 0 (cardápio disponível)
    public void verCardapio(){new TelaListagemProdutos(true);}

    // Abre a tela de atendimento (atualizar status e ver itens)
    public void atenderCliente(){new TelaAtenderCliente(null);}

    /**
     * Permite visualizar os itens de um pedido específico, digitando seu ID.
     * Valida se o pedido existe antes de abrir a janela.
     */
    public void verItensDePedido(){
        try{
            String idStr = JOptionPane.showInputDialog("Digite o ID do pedido para visualizar itens (0 para cancelar):");
            if(idStr == null || idStr.equals("0")){return;}
            int id = Integer.parseInt(idStr);
            if(id <= 0){JOptionPane.showMessageDialog(null, "ID inválido! Não pode ser negativo.");return;}
            Connection c = new ConnectionFactory().obtemConexao();
            String sql = "SELECT id FROM pedidos WHERE id = ?";
            PreparedStatement stm = c.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if(!rs.next()){
                JOptionPane.showMessageDialog(null, "Pedido não encontrado!");
                rs.close(); stm.close(); c.close(); return;
            }
            rs.close(); stm.close(); c.close();
            new TelaItensPedido(null, id);
        }
        catch(NumberFormatException e){JOptionPane.showMessageDialog(null, "Digite apenas números válidos!");}
        catch (Exception e){JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());}
    }
}