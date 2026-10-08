package com.mycompany.testebd;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
/**
 * Janela de diálogo para cadastrar um novo cliente.
 * Coleta nome e saldo inicial. Senha fica vazia.
 */
public class TelaCadastroCliente extends JDialog{
    private JTextField txtNome, txtSaldo;
    private boolean confirmado = false;// false = não modal (pode interagir com a tela pai)

    public TelaCadastroCliente(JFrame parent){
        super(parent, "Cadastrar Cliente", false);
        setSize(350, 200);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // Painel de campos
        JPanel pCampos = new JPanel(new GridLayout(2, 2, 5, 5));
        pCampos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        txtNome = new JTextField();
        txtSaldo = new JTextField("");
        pCampos.add(new JLabel("Nome:"));
        pCampos.add(txtNome);
        pCampos.add(new JLabel("Saldo Inicial (R$):"));
        pCampos.add(txtSaldo);
        add(pCampos, BorderLayout.CENTER);
        
        // Botões
        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e ->{
        if(validarCampos()){
            ConnectionFactory factory = new ConnectionFactory();
            String sql = "INSERT INTO usuarios (nome, senha, tipo, saldo) VALUES (?, '', 'CLIENTE', ?)";
            try{
                Connection c = factory.obtemConexao();
                PreparedStatement stm = c.prepareStatement(sql);
                stm.setString(1, txtNome.getText());
                stm.setDouble(2, Double.parseDouble(txtSaldo.getText()));
                stm.executeUpdate();
                JOptionPane.showMessageDialog(this, "Cadastro realizado!");
                confirmado = true;
                dispose(); stm.close(); c.close();
            }
            catch(Exception ex){JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());}
        }
    });
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        JPanel pBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pBotoes.add(btnSalvar);
        pBotoes.add(btnCancelar);
        add(pBotoes, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Valida nome não vazio e saldo positivo
    private boolean validarCampos(){
        if(txtNome.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(this, "Nome é obrigatório.");
            return false;
        }
        try{
            double saldo = Double.parseDouble(txtSaldo.getText().trim());
            if(saldo <= 0){
                JOptionPane.showMessageDialog(this, "Saldo deve ser maior que zero.");
                return false;
            }
            return true;
        }
        catch(NumberFormatException e){JOptionPane.showMessageDialog(this, "Saldo inválido."); return false;}
    }
    public boolean isConfirmado(){return confirmado;}
    public String getNome(){return txtNome.getText().trim();}
    public double getSaldo(){return Double.parseDouble(txtSaldo.getText().trim());}
}