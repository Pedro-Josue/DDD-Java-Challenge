package Test;

import DAO.CategoriaDAO;
import DAO.MaterialDAO;
import DAO.MovimentacaoDAO;
import DAO.UsuarioDAO;
import Model.Categoria;
import Model.Material;
import Model.Movimentacao;
import Model.Usuario;

import java.time.LocalDateTime;

public class MovimentacaoTest {
    public static void main(String[] args) {
        CategoriaDAO categoriaDAO = new CategoriaDAO();
        MaterialDAO materialDAO = new MaterialDAO();
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        MovimentacaoDAO movimentacaoDAO = new MovimentacaoDAO();

        // Criar categoria
        Categoria c = new Categoria(0, "Reagentes");
        categoriaDAO.salvar(c);

        // Criar material
        Material m = new Material(0, "Ácido Clorídrico", "Ácido forte", 200, "ml", c);
        materialDAO.salvar(m);

        // Criar usuário
        Usuario u = new Usuario(0, "Ana", "ana@gmail.com", "123");
        usuarioDAO.salvar(u);

        // 1) Criar movimentação via construtor
        Movimentacao mv = new Movimentacao(0, m, "SAIDA", 20, LocalDateTime.now(), u);
        movimentacaoDAO.salvar(mv);
        assert mv.getId() > 0;

        // 2) Buscar por ID
        Movimentacao buscada = movimentacaoDAO.buscarPorId(mv.getId());
        assert buscada != null;
        assert buscada.getTipo().equals("SAIDA");

        // 3) Atualizar
        mv = new Movimentacao(mv.getId(), m, "SAIDA", 30, mv.getData(), u);
        movimentacaoDAO.atualizar(mv);
        Movimentacao atualizada = movimentacaoDAO.buscarPorId(mv.getId());
        assert atualizada.getQuantidade() == 30;

        // 4) Listar todos
        assert movimentacaoDAO.listarTodos().size() > 0;

        // 5) Deletar
        movimentacaoDAO.deletar(mv.getId());
        assert movimentacaoDAO.buscarPorId(mv.getId()) == null;

        // Limpar material, usuário e categoria
        materialDAO.deletar(m.getId());
        usuarioDAO.deletar(u.getId());
        categoriaDAO.deletar(c.getId());

        System.out.println("Todos os testes de Movimentacao passaram!");
    }
}
