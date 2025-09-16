package DAO;

import Model.Categoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {
    public void salvar(Categoria c) {
        String sql = "INSERT INTO categoria (nome) VALUES (?)";
        try (Connection connection = ConexaoDB.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, c.getNome());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) c.setId(rs.getInt(1));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao salvar categoria: " + e.getMessage());
        }
    }

    public Categoria buscarPorId(int id) {
        String sql = "SELECT * FROM categoria WHERE id=?";
        try (Connection connection = ConexaoDB.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Categoria(rs.getInt("id"), rs.getString("nome"));
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar categoria: " + e.getMessage());
        }
        return null;
    }

    public List<Categoria> listarTodos() {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT * FROM categoria ORDER BY nome";
        try (Connection connection = ConexaoDB.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Categoria(rs.getInt("id"), rs.getString("nome")));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar categorias: " + e.getMessage());
        }
        return lista;
    }

    public void atualizar(Categoria c) {
        String sql = "UPDATE categoria SET nome=? WHERE id=?";
        try (Connection connection = ConexaoDB.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, c.getNome());
            ps.setInt(2, c.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar categoria: " + e.getMessage());
        }
    }

    public void deletar(int id) {
        String sql = "DELETE FROM categoria WHERE id=?";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao deletar categoria: " + e.getMessage());
        }
    }
}
