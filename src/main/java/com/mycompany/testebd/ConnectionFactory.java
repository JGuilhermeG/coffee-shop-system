package com.mycompany.testebd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*Fábrica de conexões com o banco de dados MySQL.
Centraliza as credenciais e a URL de conexão.
*/
public class ConnectionFactory {
    // Credenciais do banco de dados (alterar conforme necessidade)
    private String usuario = "root";
    private String senha = "tinCTrom";
    private String host = "localhost";
    private String porta = "3306";
    private String bd = "cafeteria_db";
    /**
     * Estabelece e retorna uma conexão com o banco cafeteria_db.
     * Utiliza timezone de São Paulo e permite recuperação de chave pública (para MySQL 8+).
     * @return Connection ativa
     * @throws SQLException em caso de erro de conexão
     */
    public Connection obtemConexao() throws SQLException {
        String url = "jdbc:mysql://" + host + ":" + porta + "/" + bd + "?useSSL=false" + "&allowPublicKeyRetrieval=true" + "&serverTimezone=America/Sao_Paulo";
        // Linha abaixo pode ser comentada em produção, mas útil para debug
        System.out.println("Tentando conectar em: " + url);
        return DriverManager.getConnection(url, usuario, senha);
    }
}
