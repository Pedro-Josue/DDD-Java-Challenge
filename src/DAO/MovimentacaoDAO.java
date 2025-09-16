package DAO;

import Model.Material;
import Model.Movimentacao;
import Model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovimentacaoDAO {
    public void salvar(Movimentacao m) {
        String sql = "INSERT INTO movimentacao (id_material, tipo, quantidade, data_movimentacao, id_usuario) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, m.getMaterial().getId());
            ps.setString(2, m.getTipo());
            ps.setInt(3, m.getQuantidade());
            ps.setTimestamp(4, Timestamp.valueOf(m.getData()));
            ps.setInt(5, m.getUsuario().getId());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) m.setId(rs.getInt(1));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao salvar movimentação: " + e.getMessage());
        }
    }

    public Movimentacao buscarPorId(int id) {
        String sql = "SELECT * FROM movimentacao WHERE id=?";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Movimentacao m = new Movimentacao(
                            rs.getInt("id"),
                            new Material(rs.getInt("id_material"), null, null, 0, null, null),
                            rs.getString("tipo"),
                            rs.getInt("quantidade"),
                            rs.getTimestamp("data_movimentacao").toLocalDateTime(),
                            new Usuario(rs.getInt("id_usuario"), null, null, null)
                    );
                    return m;
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar movimentação: " + e.getMessage());
        }
        return null;
    }

    public List<Movimentacao> listarTodos() {
        List<Movimentacao> lista = new ArrayList<>();
        String sql = "SELECT * FROM movimentacao ORDER BY data_movimentacao DESC";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Movimentacao m = new Movimentacao(
                        rs.getInt("id"),
                        new Material(rs.getInt("id_material"), null, null, 0, null, null),
                        rs.getString("tipo"),
                        rs.getInt("quantidade"),
                        rs.getTimestamp("data_movimentacao").toLocalDateTime(),
                        new Usuario(rs.getInt("id_usuario"), null, null, null)
                );
                lista.add(m);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar movimentações: " + e.getMessage());
        }
        return lista;
    }

    public void atualizar(Movimentacao m) {
        String sql = "UPDATE movimentacao SET id_material=?, tipo=?, quantidade=?, data_movimentacao=?, id_usuario=? WHERE id=?";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, m.getMaterial().getId());
            ps.setString(2, m.getTipo());
            ps.setInt(3, m.getQuantidade());
            ps.setTimestamp(4, Timestamp.valueOf(m.getData()));
            ps.setInt(5, m.getUsuario().getId());
            ps.setInt(6, m.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar movimentação: " + e.getMessage());
        }
    }

    public void deletar(int id) {
        String sql = "DELETE FROM movimentacao WHERE id=?";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao deletar movimentação: " + e.getMessage());
        }
    }
}
