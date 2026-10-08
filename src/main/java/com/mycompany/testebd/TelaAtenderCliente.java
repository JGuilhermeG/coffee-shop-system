package com.mycompany.testebd;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
/**
 * Tela de atendimento ao cliente (usada pelo funcionário).
 * Permite atualizar o status de um pedido e visualizar seus itens.
 */
public class TelaAtenderCliente extends JDialog{
    public TelaAtenderCliente(JFrame parent){
        super(parent, "Pedidos - Atendimento", true);
        executarFluxo();
    }

    private void executarFluxo(){
        try{
            // Exibe a lista de pedidos para referência
            new TelaListagemPedidos();
            
            // ETAPA 1: atualizar status
            String idStr = JOptionPane.showInputDialog("Digite o ID do pedido para atualizar (0 para cancelar):");
            if(idStr == null || idStr.trim().isEmpty() || idStr.equals("0")){dispose(); return;}
            int id = Integer.parseInt(idStr);
            if(id <= 0){JOptionPane.showMessageDialog(null, "ID inválido!"); dispose(); return;}
            String[] opcoes = {"Preparando", "Finalizado", "Cancelado"};
            String status = (String) JOptionPane.showInputDialog(null, "Escolha o novo status:", "Atualizar Pedido", JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
            if(status == null || status.trim().isEmpty()){dispose(); return;}
            Connection c = new ConnectionFactory().obtemConexao();
            PreparedStatement stm = c.prepareStatement("UPDATE pedidos SET status = ? WHERE id = ?");
            stm.setString(1, status);
            stm.setInt(2, id);
            int linhas = stm.executeUpdate();
            JOptionPane.showMessageDialog(null, linhas > 0 ? "Pedido atualizado!" : "Pedido não encontrado!");
            stm.close(); c.close();

            // ETAPA 2: ver itens (opcional, após atualizar)
            String itensStr = JOptionPane.showInputDialog("Digite o ID do pedido para ver os itens (0 para cancelar):");
            if(itensStr == null || itensStr.trim().isEmpty() || itensStr.equals("0")){dispose(); return;}
            int idPedido = Integer.parseInt(itensStr);
            if(idPedido <= 0){
                JOptionPane.showMessageDialog(null, "ID inválido!");
                dispose(); return;
            }
            new TelaItensPedido(null, idPedido);
            dispose();
        }
        catch(NumberFormatException e){JOptionPane.showMessageDialog(null, "Digite apenas números válidos!");
        }
        catch (Exception e){JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());}
    }
}