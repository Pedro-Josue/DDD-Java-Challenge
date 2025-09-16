package DAO;

import Model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    public void salvar(Usuario u) {
        String sql = "INSERT INTO usuario (nome, email, senha) VALUES (?, ?, ?)";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, u.getNome());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getSenha());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) u.setId(rs.getInt(1));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao salvar usuário: " + e.getMessage());
        }
    }

    public Usuario buscarPorId(int id) {
        String sql = "SELECT * FROM usuario WHERE id=?";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(rs.getInt("id"), rs.getString("nome"), rs.getString("email"), rs.getString("senha"));
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar usuário: " + e.getMessage());
        }
        return null;
    }

    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuario ORDER BY nome";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Usuario(rs.getInt("id"), rs.getString("nome"), rs.getString("email"), rs.getString("senha")));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar usuários: " + e.getMessage());
        }
        return lista;
    }

    public void atualizar(Usuario u) {
        String sql = "UPDATE usuario SET nome=?, email=?, senha=? WHERE id=?";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, u.getNome());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getSenha());
            ps.setInt(4, u.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar usuário: " + e.getMessage());
        }
    }

    public void deletar(int id) {
        String sql = "DELETE FROM usuario WHERE id=?";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao deletar usuário: " + e.getMessage());
        }
    }
}
