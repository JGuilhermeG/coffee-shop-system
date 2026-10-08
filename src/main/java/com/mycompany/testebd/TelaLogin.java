package com.mycompany.testebd;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
/**
 * Tela de login do sistema.
 * Coleta nome e senha, consulta o banco e instancia o tipo correto de usuário.
 */
public class TelaLogin extends JFrame{
    private JTextField txtNome;
    private JPasswordField txtSenha;
    private JButton btnEntrar, btnCadastrar, btnSair;

    public TelaLogin(){
        setTitle("Sistema de Gestão - Cafeteria Mauá");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 1, 5, 5));

        // Linha 1: campo nome
        JPanel linha1 = new JPanel();
        linha1.add(new JLabel("Nome:"));
        txtNome = new JTextField(15);
        linha1.add(txtNome);
        add(linha1);

        // Linha 2: campo senha
        JPanel linha2 = new JPanel();
        linha2.add(new JLabel("Senha:"));
        txtSenha = new JPasswordField(15);
        linha2.add(txtSenha);
        add(linha2);

        // Linha 3: botões
        JPanel linha3 = new JPanel();
        btnEntrar = new JButton("Entrar");
        btnCadastrar = new JButton("Cadastrar Cliente");
        btnSair = new JButton("Sair");
        linha3.add(btnEntrar);
        linha3.add(btnCadastrar);
        linha3.add(btnSair);
        add(linha3);

        // Ações
        btnEntrar.addActionListener(e -> realizarLogin());
        btnCadastrar.addActionListener(e -> cadastrarCliente());
        btnSair.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    /**
     * Tenta autenticar o usuário e, se bem-sucedido, fecha a tela de login e exibe o menu correspondente.
     */
    private void realizarLogin(){
        String nome = txtNome.getText().trim();
        if(nome.isEmpty()){
            JOptionPane.showMessageDialog(this, "Digite o nome de usuário.");
            return;
        }
        String senha = new String(txtSenha.getPassword());
        Usuario user = autenticar(nome, senha);
        if(user != null){dispose(); user.exibirMenu();}// fecha a tela de login e abre o menu específico do usuário
    }

    /**
     * Consulta o banco de dados e, se credenciais válidas, retorna o objeto Usuario (Dono, Funcionario ou Cliente).
     * Clientes não precisam de senha (campo senha vazio ou NULL no banco).
     */
    private Usuario autenticar(String nome, String senha) {
        String sql = "SELECT * FROM usuarios WHERE nome = ?";
        ConnectionFactory factory = new ConnectionFactory();
        try{
            Connection c = factory.obtemConexao();
            PreparedStatement stm = c.prepareStatement(sql);
            stm.setString(1, nome);
            ResultSet rs = stm.executeQuery();
            if(rs.next()){
                int id = rs.getInt("id");
                String tipo = rs.getString("tipo");
                String senhaBD = rs.getString("senha");

                // Se não for cliente, verifica a senha
                if(!tipo.equals("CLIENTE")){
                    if(senhaBD == null || !senha.equals(senhaBD)){
                        JOptionPane.showMessageDialog(this, "Senha incorreta!");
                        rs.close(); stm.close(); c.close();
                        return null;
                    }
                }
                Usuario user;
                if(tipo.equals("DONO")){
                    user = new Dono(id, nome, senha);
                }
                else if(tipo.equals("FUNCIONARIO")){
                    String cargo = rs.getString("cargo");
                    user = new Funcionario(id, nome, senha, cargo);
                }
                // CLIENTE
                else{
                    double saldo = rs.getDouble("saldo");
                    user = new Cliente(id, nome, saldo);
                }
                rs.close(); stm.close(); c.close();
                return user;
            }
            else{
                JOptionPane.showMessageDialog(this, "Usuário não encontrado!");
                rs.close(); stm.close(); c.close();
                return null;
            }
        }
        catch(Exception e){JOptionPane.showMessageDialog(this, "Erro na conexão: " + e.getMessage()); return null;}
    }
    
    // Abre a tela de cadastro de novo cliente
    private void cadastrarCliente(){new TelaCadastroCliente(this);}
}