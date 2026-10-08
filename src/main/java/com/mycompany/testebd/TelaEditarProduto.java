package com.mycompany.testebd;

import javax.swing.*;
import java.awt.*;
/**
 * Diálogo para editar os dados de um produto existente.
 * Recebe os valores atuais e permite alterá-los.
 */
public class TelaEditarProduto extends JDialog{
    private JTextField txtNome, txtPreco, txtQuantidade;
    private boolean confirmado = false;

    public TelaEditarProduto(JFrame parent, int id, String nome, double preco, int quantidade) {
        super(parent, "Editar Produto #" + id, true);

        setSize(420, 260);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // Painel com campos preenchidos
        JPanel painelCampos = new JPanel(new GridLayout(3, 2, 5, 5));
        painelCampos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        painelCampos.add(new JLabel("Nome:"));
        txtNome = new JTextField(nome);
        painelCampos.add(txtNome);

        painelCampos.add(new JLabel("Preço (R$):"));
        txtPreco = new JTextField(String.format("%.2f", preco));
        painelCampos.add(txtPreco);

        painelCampos.add(new JLabel("Quantidade:"));
        txtQuantidade = new JTextField(String.valueOf(quantidade));
        painelCampos.add(txtQuantidade);

        add(painelCampos, BorderLayout.CENTER);

        // Botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");

        btnSalvar.addActionListener(e ->{if(validarCampos()){confirmado = true; dispose();}});

        btnCancelar.addActionListener(e -> dispose());

        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnCancelar);

        add(painelBotoes, BorderLayout.SOUTH);

        setVisible(true);
    }

    private boolean validarCampos() {
        if(txtNome.getText().trim().isEmpty()){JOptionPane.showMessageDialog(this, "Nome do produto é obrigatório."); return false;}
        try{
            double preco = Double.parseDouble(txtPreco.getText().trim());
            if(preco <= 0){JOptionPane.showMessageDialog(this, "Preço deve ser maior que zero."); return false;}
        }
        catch(NumberFormatException e){JOptionPane.showMessageDialog(this, "Preço inválido."); return false;}
        try{
            int qtd = Integer.parseInt(txtQuantidade.getText().trim());
            if(qtd < 0){JOptionPane.showMessageDialog(this, "Quantidade não pode ser negativa.");return false;}
        }
        catch(NumberFormatException e){JOptionPane.showMessageDialog(this, "Quantidade inválida."); return false;
        }
        return true;
    }
    
    public boolean isConfirmado(){return confirmado;}
    public String getNome(){return txtNome.getText().trim();}
    public double getPreco(){return Double.parseDouble(txtPreco.getText().trim());}
    public int getQuantidade(){return Integer.parseInt(txtQuantidade.getText().trim());}
}