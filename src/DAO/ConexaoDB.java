package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexaoDB {
    //url do banco de dados, utilizando postgres
    private static final String url = "jdbc:postgresql://localhost:5432/Stockeasy";
    //variaveis para acesso ao banco, com credenciais de login explicitas
    private static final String username = "postgres";
    private static final String senha = "2580";
    //criação da tabela das tabelas para a aplicacao (Categoria, Material, Usuario e Movimentacao)
    static {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()){
            // Tabela de categorias
            statement.execute("CREATE TABLE IF NOT EXISTS categoria (" +
                    "id SERIAL PRIMARY KEY," +
                    "nome VARCHAR(100) NOT NULL)");

            // Tabela de materiais
            statement.execute("CREATE TABLE IF NOT EXISTS material (" +
                    "id SERIAL PRIMARY KEY," +
                    "nome VARCHAR(100) NOT NULL," +
                    "descricao TEXT," +
                    "quantidade_estoque INT NOT NULL," +
                    "unidade_medida VARCHAR(20)," +
                    "id_categoria INT REFERENCES categoria(id))");

            // Tabela de usuários
            statement.execute("CREATE TABLE IF NOT EXISTS usuario (" +
                    "id SERIAL PRIMARY KEY," +
                    "nome VARCHAR(100) NOT NULL," +
                    "email VARCHAR(100) UNIQUE NOT NULL," +
                    "senha VARCHAR(100) NOT NULL)");

            // Tabela de movimentações
            statement.execute("CREATE TABLE IF NOT EXISTS movimentacao (" +
                    "id SERIAL PRIMARY KEY," +
                    "id_material INT NOT NULL REFERENCES material(id)," +
                    "tipo VARCHAR(20) NOT NULL," + // ENTRADA ou SAIDA
                    "quantidade INT NOT NULL," +
                    "data_movimentacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "id_usuario INT REFERENCES usuario(id))");

            System.out.println("Tabelas criadas com sucesso!");
        } catch (SQLException e){
            System.out.println("Erro na conexão ao banco de dados" + e);
        }
    }
    //conexão com o banco
    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(url, username, senha);
    }
}
