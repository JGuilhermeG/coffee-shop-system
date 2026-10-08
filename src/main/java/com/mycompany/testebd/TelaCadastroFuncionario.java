package com.mycompany.testebd;

import javax.swing.*;
import java.awt.*;
/**
 * Diálogo para cadastrar um novo funcionário (usado pelo Dono).
 * Coleta nome, senha e cargo.
 */
public class TelaCadastroFuncionario extends JDialog{
    private JTextField txtNome, txtSenha, txtCargo;
    private boolean confirmado = false;

    public TelaCadastroFuncionario(JFrame parent){
        super(parent, "Cadastrar Funcionário", true);// modal
        setSize(400, 200);
        setLocationRelativeTo(parent);

        // Painel de campos
        setLayout(new BorderLayout(10, 10));
        JPanel painelCampos = new JPanel(new GridLayout(3, 2, 5, 5));
        painelCampos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        painelCampos.add(new JLabel("Nome:"));
        txtNome = new JTextField();
        painelCampos.add(txtNome);

        painelCampos.add(new JLabel("Senha:"));
        txtSenha = new JTextField();
        painelCampos.add(txtSenha);

        painelCampos.add(new JLabel("Cargo:"));
        txtCargo = new JTextField();
        painelCampos.add(txtCargo);
        
        add(painelCampos, BorderLayout.CENTER);

        // Botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");

        btnSalvar.addActionListener(e ->{if(validarCampos()){confirmado = true; dispose();}});
        btnCancelar.addActionListener(e -> dispose());

        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnCancelar);
        
        add(painelBotoes, BorderLayout.SOUTH);

        setVisible(true);
    }

    private boolean validarCampos(){
        if(txtNome.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(this, "Nome é obrigatório.");
            return false;
        }
        if(txtSenha.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(this, "Senha é obrigatória.");
            return false;
        }
        if(txtCargo.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(this, "Cargo é obrigatório.");
            return false;
        }
        return true;
    }

    public boolean isConfirmado(){return confirmado;}
    public String getNome(){return txtNome.getText().trim();}
    public String getSenha(){return txtSenha.getText().trim();}
    public String getCargo(){return txtCargo.getText().trim();}
}