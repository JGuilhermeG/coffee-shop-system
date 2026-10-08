package com.mycompany.testebd;

import javax.swing.*;
import java.awt.*;
/**
 * Diálogo para cadastrar um novo produto no cardápio.
 * Coleta nome, preço e quantidade inicial.
 */
public class TelaCadastroProduto extends JDialog{
    private JTextField txtNome, txtPreco, txtQuantidade;
    private boolean confirmado = false;

    public TelaCadastroProduto(JFrame parent){
        super(parent, "Cadastrar Produto", true);
        setSize(400, 200);
        setLocationRelativeTo(parent);

        // Painel de campos
        JPanel painelCampos = new JPanel(new GridLayout(3, 2, 5, 5));
        painelCampos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        painelCampos.add(new JLabel("Nome:"));
        txtNome = new JTextField();
        painelCampos.add(txtNome);

        painelCampos.add(new JLabel("Preço (R$):"));
        txtPreco = new JTextField();
        painelCampos.add(txtPreco);

        painelCampos.add(new JLabel("Quantidade em estoque:"));
        txtQuantidade = new JTextField();
        painelCampos.add(txtQuantidade);

        add(painelCampos, BorderLayout.CENTER);

        // Botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");

        btnSalvar.addActionListener(e ->{if(validarCampos()){confirmado = true;dispose();}});
        
        btnCancelar.addActionListener(e -> dispose());

        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnCancelar);
        
        add(painelBotoes, BorderLayout.SOUTH);

        setVisible(true);
    }

    private boolean validarCampos(){
        if(txtNome.getText().trim().isEmpty()){JOptionPane.showMessageDialog(this, "Nome do produto é obrigatório."); return false;}
        try{
            double preco = Double.parseDouble(txtPreco.getText().trim());
            if(preco <= 0){JOptionPane.showMessageDialog(this, "Preço deve ser maior que zero."); return false;}
        }
        catch(NumberFormatException e){JOptionPane.showMessageDialog(this, "Preço inválido."); return false;}
        try{
            int qtd = Integer.parseInt(txtQuantidade.getText().trim());
            if(qtd < 0){JOptionPane.showMessageDialog(this, "Quantidade não pode ser negativa."); return false;}
        }
        catch(NumberFormatException e){JOptionPane.showMessageDialog(this, "Quantidade inválida."); return false;} return true;
    }
    public boolean isConfirmado(){return confirmado;}
    public String getNome(){return txtNome.getText().trim();}
    public double getPreco(){return Double.parseDouble(txtPreco.getText().trim());}
    public int getQuantidade(){return Integer.parseInt(txtQuantidade.getText().trim());}
}