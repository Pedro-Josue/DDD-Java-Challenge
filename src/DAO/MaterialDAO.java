package DAO;

import Model.Categoria;
import Model.Material;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterialDAO {
    public void salvar(Material m) {
        String sql = "INSERT INTO material (nome, descricao, quantidade_estoque, unidade_medida, id_categoria) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, m.getNome());
            ps.setString(2, m.getDescricao());
            ps.setInt(3, m.getQuantidadeEstoque());
            ps.setString(4, m.getUnidadeMedida());
            ps.setInt(5, m.getCategoria().getId());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) m.setId(rs.getInt(1));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao salvar material: " + e.getMessage());
        }
    }

    public Material buscarPorId(int id) {
        String sql = "SELECT * FROM material WHERE id=?";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Material m = new Material(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("descricao"),
                            rs.getInt("quantidade_estoque"),
                            rs.getString("unidade_medida"),
                            new Categoria(rs.getInt("id_categoria"), null) // só o id da categoria por enquanto
                    );
                    return m;
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar material: " + e.getMessage());
        }
        return null;
    }

    public List<Material> listarTodos() {
        List<Material> lista = new ArrayList<>();
        String sql = "SELECT * FROM material ORDER BY id";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Material m = new Material(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("descricao"),
                        rs.getInt("quantidade_estoque"),
                        rs.getString("unidade_medida"),
                        new Categoria(rs.getInt("id_categoria"), null) // só o id da categoria por enquanto
                );
                lista.add(m);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar materiais: " + e.getMessage());
        }
        return lista;
    }

    public void atualizar(Material m) {
        String sql = "UPDATE material SET nome=?, descricao=?, quantidade_estoque=?, unidade_medida=?, id_categoria=? WHERE id=?";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, m.getNome());
            ps.setString(2, m.getDescricao());
            ps.setInt(3, m.getQuantidadeEstoque());
            ps.setString(4, m.getUnidadeMedida());
            ps.setInt(5, m.getCategoria().getId());
            ps.setInt(6, m.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar material: " + e.getMessage());
        }
    }

    public void deletar(int id) {
        String sql = "DELETE FROM material WHERE id=?";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao deletar material: " + e.getMessage());
        }
    }
}
