package com.mycompany.testebd;

import javax.swing.JOptionPane;
import java.sql.*;
/**
 * Cliente da cafeteria: pode visualizar cardápio, fazer pedidos e ver saldo.
 * O saldo é debitado automaticamente ao finalizar o pedido.
 * Se o saldo for insuficiente, o pedido é cancelado e o estoque é restaurado.
 */
public class Cliente extends Usuario{
    public Cliente(int id, String nome, Double saldo){
        super(id, nome, saldo);
    }

    @Override
    public void exibirMenu(){
        new TelaMenuClientes(this);
    }
    
    @Override
    public String getDados(){return String.format("Cliente: %s | ID: %d | Saldo: R$ %.2f", getNome(), getId(), getSaldo());}

    // Mostra cardápio (apenas produtos com estoque)
    public TelaListagemProdutos visualizarCardapio(){return new TelaListagemProdutos(true);}

    /**
     * Fluxo completo de realização de um pedido:
     * 1. Cria um pedido com status 'Aberto'
     * 2. Adiciona itens (chama adicionarItensPedido)
     * 3. Verifica saldo: se suficiente, debita e finaliza; senão, cancela pedido e devolve estoque.
     */
    public void fazerPedido(){
        ConnectionFactory factory = new ConnectionFactory();
        int pedidoId = -1;
        try{
            TelaListagemProdutos telaCardapio = visualizarCardapio();
            //1. Criar novo pedido
            String sqlPedido = "INSERT INTO pedidos (cliente_id, status, valor_total) VALUES (?, 'Aberto', 0)";
            Connection c1 = factory.obtemConexao();
            PreparedStatement stm1 = c1.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS);
            stm1.setInt(1, getId());
            stm1.executeUpdate();
            ResultSet rs1 = stm1.getGeneratedKeys();
            if(rs1.next()){pedidoId = rs1.getInt(1);}
            else{
                JOptionPane.showMessageDialog(null, "Erro ao criar pedido!");
                rs1.close(); stm1.close(); c1.close(); return;
            }
            rs1.close(); stm1.close(); c1.close();
            //2. Adicionar itens ao pedido (loop)
            double total = adicionarItensPedido(pedidoId, telaCardapio);
            //Verifica se não colocou nada no pedido
            if(total == 0){
                String sqlDelete = "DELETE FROM pedidos WHERE id = ?";
                Connection cNada = factory.obtemConexao();
                PreparedStatement stmNada = cNada.prepareStatement(sqlDelete);
                stmNada.setInt(1, pedidoId);
                stmNada.executeUpdate();
                stmNada.close();
                cNada.close();
                JOptionPane.showMessageDialog(null, "Pedido cancelado (nenhum item adicionado).");
                return;
            }
            //3. Verificar saldo
            if(getSaldo() >= total){
                // Atualiza valor total do pedido
                String sqlUpdate = "UPDATE pedidos SET valor_total = ? WHERE id = ?";
                Connection c2 = factory.obtemConexao();
                PreparedStatement stm2 = c2.prepareStatement(sqlUpdate);
                stm2.setDouble(1, total);
                stm2.setInt(2, pedidoId);
                stm2.executeUpdate(); stm2.close(); c2.close();
                // Debita o saldo do cliente
                double novoSaldo = getSaldo() - total;
                setSaldo(novoSaldo);
                String sqlSaldo = "UPDATE usuarios SET saldo = ? WHERE id = ?";
                Connection c3 = factory.obtemConexao();
                PreparedStatement stm3 = c3.prepareStatement(sqlSaldo);
                stm3.setDouble(1, novoSaldo);
                stm3.setInt(2, getId());
                stm3.executeUpdate(); stm3.close(); c3.close();
                JOptionPane.showMessageDialog(null, String.format("Pedido #%d realizado com sucesso!\nTotal: R$ %.2f\nSaldo restante: R$ %.2f", pedidoId, total, novoSaldo));
                new TelaListagemPedidos();// exibe listagem de todos os pedidos
            }
            else{
                // Saldo insuficiente: devolve estoque e remove o pedido
                Connection c4 = factory.obtemConexao();
                String sqlDevolver = "UPDATE produtos p JOIN itens_pedido ip ON p.id = ip.produto_id SET p.quantidade = p.quantidade + ip.quantidade WHERE ip.pedido_id = ?";
                PreparedStatement stmDevolver = c4.prepareStatement(sqlDevolver);
                stmDevolver.setInt(1, pedidoId);
                stmDevolver.executeUpdate();
                stmDevolver.close();
                // Remove itens do pedido
                String sqlDeleteItens = "DELETE FROM itens_pedido WHERE pedido_id = ?";
                PreparedStatement stmItens = c4.prepareStatement(sqlDeleteItens);
                stmItens.setInt(1, pedidoId);
                stmItens.executeUpdate();
                stmItens.close();
                // Remove o pedido
                String sqlDeletePedido = "DELETE FROM pedidos WHERE id = ?";
                PreparedStatement stmPedido = c4.prepareStatement(sqlDeletePedido);
                stmPedido.setInt(1, pedidoId);
                stmPedido.executeUpdate();stmPedido.close(); c4.close();
                JOptionPane.showMessageDialog(null, String.format("Saldo insuficiente!\nNecessário: R$ %.2f\nDisponível: R$ %.2f", total, getSaldo()));
            }
        }
        catch(Exception e){JOptionPane.showMessageDialog(null, "Erro ao fazer pedido: " + e.getMessage());}
    }
    /**
     * Loop para adicionar itens ao pedido.
     * Permite ao cliente digitar ID do produto e quantidade.
     * Atualiza estoque e tabela itens_pedido.
     * Retorna o valor total do pedido.
     */
    private double adicionarItensPedido(int pedidoId, TelaListagemProdutos telaCardapio){
        double totalGeral = 0;
        StringBuilder resumo = new StringBuilder("\nSEU PEDIDO\n");
        ConnectionFactory factory = new ConnectionFactory();
        while(true){
            try{
                String produtoIdStr = JOptionPane.showInputDialog("ID do produto (0 para finalizar):");
                if(produtoIdStr == null || produtoIdStr.equals("0")){telaCardapio.dispose();break;}
                int produtoId = Integer.parseInt(produtoIdStr);
                // Busca dados do produto
                String sqlBusca = "SELECT nome, preco, quantidade FROM produtos WHERE id = ?";
                Connection c = factory.obtemConexao();
                PreparedStatement stmBusca = c.prepareStatement(sqlBusca);
                stmBusca.setInt(1, produtoId);
                ResultSet rs = stmBusca.executeQuery();
                if(rs.next()){
                    String nomeProduto = rs.getString("nome");
                    double precoUnitario = rs.getDouble("preco");
                    int estoqueDisponivel = rs.getInt("quantidade");
                    String qtdStr = JOptionPane.showInputDialog(String.format("Produto: %s\nPreço: R$ %.2f\nEstoque: %d\n\nQuantidade desejada:", nomeProduto, precoUnitario, estoqueDisponivel));
                    if(qtdStr == null){telaCardapio.dispose(); rs.close(); stmBusca.close(); c.close(); return totalGeral;}//break antes
                    int quantidade = Integer.parseInt(qtdStr);
                    if(quantidade <= 0){
                        JOptionPane.showMessageDialog(null, "Quantidade deve ser maior que zero!");
                        rs.close(); stmBusca.close(); c.close();continue;
                    }
                    if(quantidade > estoqueDisponivel){
                        JOptionPane.showMessageDialog(null, "Estoque insuficiente! Disponível: " + estoqueDisponivel);
                        rs.close(); stmBusca.close(); c.close(); continue;
                    }
                    double subtotal = precoUnitario*quantidade;
                    // Verifica se o produto já foi adicionado ao pedido (para somar quantidade)
                    String sqlVerifica = "SELECT quantidade FROM itens_pedido WHERE pedido_id = ? AND produto_id = ?";
                    PreparedStatement stmVerifica = c.prepareStatement(sqlVerifica);

                    stmVerifica.setInt(1, pedidoId);
                    stmVerifica.setInt(2, produtoId);

                    ResultSet rsVerifica = stmVerifica.executeQuery();

                    if(rsVerifica.next()){
                        // Já existe: atualiza quantidade
                        String sqlUpdateItem = "UPDATE itens_pedido SET quantidade = quantidade + ? WHERE pedido_id = ? AND produto_id = ?";
                        PreparedStatement stmUpdate = c.prepareStatement(sqlUpdateItem);
                        stmUpdate.setInt(1, quantidade);
                        stmUpdate.setInt(2, pedidoId);
                        stmUpdate.setInt(3, produtoId);
                        stmUpdate.executeUpdate();
                        stmUpdate.close();
                    }
                    else{
                        // Novo item: insere
                        String sqlItem = "INSERT INTO itens_pedido (pedido_id, produto_id, quantidade, preco_unitario) VALUES (?, ?, ?, ?)";
                        PreparedStatement stmItem = c.prepareStatement(sqlItem);
                        stmItem.setInt(1, pedidoId);
                        stmItem.setInt(2, produtoId);
                        stmItem.setInt(3, quantidade);
                        stmItem.setDouble(4, precoUnitario);
                        stmItem.executeUpdate();
                        stmItem.close();
                    }
                    rsVerifica.close(); stmVerifica.close();
                    // Abate do estoque
                    String sqlEstoque = "UPDATE produtos SET quantidade = quantidade - ? WHERE id = ?";
                    PreparedStatement stmEstoque = c.prepareStatement(sqlEstoque);
                    stmEstoque.setInt(1, quantidade);
                    stmEstoque.setInt(2, produtoId);
                    stmEstoque.executeUpdate();
                    totalGeral += subtotal;
                    resumo.append(String.format("- %s x%d: R$ %.2f\n", nomeProduto, quantidade, subtotal));
                    stmEstoque.close();
                }
                else{JOptionPane.showMessageDialog(null, "Produto não encontrado para o ID: " + produtoId);}
                rs.close(); stmBusca.close(); c.close();
            }
            catch(NumberFormatException e){JOptionPane.showMessageDialog(null, "Por favor, digite apenas números válidos.");}
            catch(SQLException e){JOptionPane.showMessageDialog(null, "Erro de banco de dados: " + e.getMessage());}
            catch(Exception e){JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());}
        }
        if(totalGeral > 0){JOptionPane.showMessageDialog(null, resumo.toString() + String.format("\nTOTAL DO PEDIDO: R$ %.2f", totalGeral));}
        telaCardapio.dispose();
        return totalGeral;
    }
    // Exibe o saldo atual do cliente
    public void verSaldo(){
        JOptionPane.showMessageDialog(null, String.format("Seu saldo atual: R$ %.2f", getSaldo()));
    }
}