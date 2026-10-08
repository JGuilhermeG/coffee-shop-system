package com.mycompany.testebd;

import javax.swing.JOptionPane;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;
/**
 * Classe utilitária que fornece métodos para obter listas de produtos, funcionários,
 * clientes, pedidos e itens de pedido diretamente do banco de dados.
 * Esses dados são usados pelas telas de listagem (JTable).
 */
public class CafeteriaUtilidades{

    /**
     * Retorna uma lista de produtos (cada produto é um Object[] com {id, nome, preco, quantidade}).
     * @param apenasComEstoque se true, filtra apenas produtos com quantidade > 0
     */
    public static ArrayList<Object[]> getProdutosArray(boolean apenasComEstoque){
        ArrayList<Object[]> lista = new ArrayList<>();
        String sql = "SELECT id, nome, preco, quantidade FROM produtos";
        if(apenasComEstoque){sql = "SELECT id, nome, preco, quantidade FROM produtos WHERE quantidade > 0";}
        sql += " ORDER BY id";
        ConnectionFactory factory = new ConnectionFactory();
        try{
            Connection c = factory.obtemConexao();
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()){
                Object[] linha = {rs.getInt("id"), rs.getString("nome"), rs.getDouble("preco"), rs.getInt("quantidade")};
                lista.add(linha);
            }
            rs.close(); st.close(); c.close();
        }
        catch(Exception e){e.printStackTrace(); JOptionPane.showMessageDialog(null, "Erro ao listar produtos: " + e.getMessage());}
        return lista;
    }

    /**
     * Retorna lista de funcionários (id, nome, cargo).
     */
    public static ArrayList<Object[]> getFuncionariosArray(){
        ArrayList<Object[]> lista = new ArrayList<>();
        String sql = "SELECT id, nome, cargo FROM usuarios WHERE tipo = 'FUNCIONARIO' ORDER BY id";
        ConnectionFactory factory = new ConnectionFactory();
        try{
            Connection c = factory.obtemConexao();
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                Object[] linha ={rs.getInt("id"), rs.getString("nome"), rs.getString("cargo")};
                lista.add(linha);
            }
            rs.close(); st.close(); c.close();
        }
        catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao listar funcionários: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Retorna lista de clientes (id, nome, saldo).
     */
    public static ArrayList<Object[]> getClientesArray() {
        ArrayList<Object[]> lista = new ArrayList<>();
        String sql = "SELECT id, nome, saldo FROM usuarios WHERE tipo = 'CLIENTE' ORDER BY id";
        ConnectionFactory factory = new ConnectionFactory();
        try{
            Connection c = factory.obtemConexao();
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                Object[] linha = {
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getDouble("saldo")
                };
                lista.add(linha);
            }
            rs.close(); st.close(); c.close();
        }
        catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao listar clientes: " + e.getMessage());
        }
        return lista;
    }
    /**
     * Retorna lista de pedidos (id, cliente, status, valor_total, data_formatada).
     */
    public static ArrayList<Object[]> getPedidosArray(){
        ArrayList<Object[]> lista = new ArrayList<>();
        String sql = "SELECT p.id, u.nome AS cliente, p.status, p.valor_total, p.data_pedido FROM pedidos p JOIN usuarios u ON p.cliente_id = u.id ORDER BY p.id DESC";
        ConnectionFactory factory = new ConnectionFactory();
        try{
            Connection c = factory.obtemConexao();
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(sql);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
            while(rs.next()){
                Timestamp ts = rs.getTimestamp("data_pedido");
                String dataFormatada = (ts != null) ? sdf.format(ts) : "";
                Object[] linha = {rs.getInt("id"), rs.getString("cliente"), rs.getString("status"), rs.getDouble("valor_total"), dataFormatada};
                lista.add(linha);
            }
            rs.close(); st.close(); c.close();
        }
        catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao listar pedidos: " + e.getMessage());
        }
        return lista;
    }
    /**
     * Retorna os itens de um pedido específico.
     * Cada item: {nome_produto, quantidade, preco_unitario, subtotal}
     */
    public static ArrayList<Object[]> getItensPedidoArray(int pedidoId){
        ArrayList<Object[]> lista = new ArrayList<>();
        String sql = "SELECT pr.nome, ip.quantidade, ip.preco_unitario FROM itens_pedido ip JOIN produtos pr ON ip.produto_id = pr.id WHERE ip.pedido_id = ? ORDER BY pr.nome";
        ConnectionFactory factory = new ConnectionFactory();
        try{
            Connection c = factory.obtemConexao();
            PreparedStatement stm = c.prepareStatement(sql);
            stm.setInt(1, pedidoId);
            ResultSet rs = stm.executeQuery();
            while(rs.next()){
                int qtd = rs.getInt("quantidade");
                double precoUnit = rs.getDouble("preco_unitario");
                double subtotal = qtd * precoUnit;
                Object[] linha = {rs.getString("nome"), qtd, precoUnit, subtotal};
                lista.add(linha);
            }
            rs.close(); stm.close(); c.close();
        }
        catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao listar itens do pedido: " + e.getMessage());
        }
        return lista;
    }
}