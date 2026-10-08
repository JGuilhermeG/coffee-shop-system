# ☕ Sistema de Gestão de Cafeteria (Java & MySQL)

> Aplicação desktop desenvolvida em Java com interface gráfica nativa e persistência relacional em MySQL via JDBC.

---

## 📌 Sobre o Projeto

Projeto prático desenvolvido para a disciplina de **Linguagem de Programação I**[cite: 13]. O sistema simula as operações quotidianas de uma cafeteria, estruturado com controlo de acesso baseado em perfis (Dono, Funcionário e Cliente)[cite: 15, 17, 20].

### Perfis de Acesso:
- **Dono (`Dono.java`, `TelaMenuDono.java`):** Gestão administrativa completa, incluindo cadastro, alteração e listagem de produtos e funcionários[cite: 15, 18, 20].
- **Funcionário (`Funcionario.java`, `TelaMenuFuncionario.java`):** Atendimento e acompanhamento de pedidos em tempo real[cite: 15, 18, 20].
- **Cliente (`Cliente.java`, `TelaMenuClientes.java`):** Consulta do cardápio e realização de compras com débito direto no saldo[cite: 15, 17, 18, 20].

---

## 🛠️ Tecnologias Utilizadas

- **Linguagem:** Java 21
- **Interface Gráfica:** Java AWT / Swing (janelas, tabelas e caixas de diálogo)[cite: 15]
- **Base de Dados:** MySQL[cite: 15, 17, 20]
- **Conexão:** JDBC (`ConnectionFactory.java`)[cite: 15, 17, 20]
- **Gestão de Dependências:** Maven[cite: 20]

---

## 📂 Estrutura das Classes Principais

```text
src/main/java/com/mycompany/testebd/
├── Main.java                        # Ponto de entrada (inicializa a tela de login)
├── ConnectionFactory.java           # Gestão de conexão JDBC com o MySQL
├── CafeteriaUtilidades.java         # Métodos utilitários e regras de apoio
├── Usuario.java                     # Classe base de utilizadores
├── Cliente.java / Dono.java / Funcionario.java # Especializações de perfil
└── Tela*.java                       # Interfaces gráficas de menus, cadastros e listagens
```

---

## ⚙️ Como Executar

### Pré-requisitos
- **Java JDK 17+** (ou JDK 21) instalado[cite: 20]
- Servidor **MySQL** em execução local na porta `3306`[cite: 20]

### Configuração da Base de Dados
Certifique-se de que a base de dados `cafeteria_db` está criada no seu servidor MySQL local[cite: 20]:
```sql
CREATE DATABASE cafeteria_db;
```
Valide o utilizador e a palavra-passe de acesso no ficheiro `ConnectionFactory.java`[cite: 20].

### Execução da Aplicação
1. Abra a pasta do projeto no VS Code[cite: 20].
2. Abra a classe `src/main/java/com/mycompany/testebd/Main.java`[cite: 20].
3. Clique em **Run** sobre o método `public static void main` para iniciar a interface a partir da `TelaLogin`[cite: 20].

---

## 👥 Autores

- Bruno Fabrini Mesquita[cite: 13]
- Gustavo Fernandes De Almeida[cite: 13]
- José Guilherme Duarte[cite: 13]
- Syden Rafael Escobar[cite: 13]
- Thomas Boehm Machado[cite: 13]