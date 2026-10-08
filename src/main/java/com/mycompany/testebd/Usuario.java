package com.mycompany.testebd;
/**
 * Classe abstrata que representa um usuário do sistema.
 * Define atributos comuns e força a implementação de exibirMenu() e getDados().
 * Usa polimorfismo para Dono, Funcionario e Cliente.
 */
public abstract class Usuario{
    private int id;
    private String nome;
    private String senha;
    private String tipo;
    private String cargo;
    private Double saldo;
    private Boolean acessoTotal;

    // Construtor completo (usado internamente pelos outros construtores)
    public Usuario(int id, String nome, String senha, String tipo, String cargo, Double saldo, Boolean acessoTotal) {
        this.id = id;
        this.nome = nome;
        this.senha = senha;
        this.tipo = tipo;
        this.cargo = cargo;
        this.saldo = saldo;
        this.acessoTotal = acessoTotal;
    }
    
    // Construtor para Cliente (sem senha, saldo é definido)
    public Usuario(int id, String nome, Double saldo) {
        this(id, nome, "", "CLIENTE", null, saldo, false);
    }
    
    // Construtor para Funcionario (possui senha e cargo)
    public Usuario(int id, String nome, String senha, String cargo){
        this(id, nome, senha, "FUNCIONARIO", cargo, null, false);
    }
    
    // Construtor para Dono (acesso total)
    public Usuario(int id, String nome, String senha, Boolean acessoTotal){
        this(id, nome, senha, "DONO", null, null, acessoTotal);
    }
    
    // Métodos abstratos que cada tipo deve implementar
    public abstract void exibirMenu();   // abre a interface gráfica específica
    public abstract String getDados();   // retorna informações resumidas do usuário
    
    // Valida senha (clientes sempre passam, pois senha é vazia)
    public boolean autenticar(String senhaInformada){return this.senha.equals(senhaInformada);}
    
    // Getters e Setters
    public int getId(){return id;}
    public String getNome(){return nome;}
    public String getSenha(){return senha;}
    public String getTipo(){return tipo;}
    public String getCargo(){return cargo;}
    public Double getSaldo(){return saldo;}
    public Boolean getAcessoTotal(){return acessoTotal;}
    
    public void setCargo(String cargo){this.cargo = cargo;}
    public void setSaldo(Double saldo){this.saldo = saldo;}
    
    @Override
    public String toString(){return nome + " (" + tipo + ")";}
}