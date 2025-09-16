package Test;

import DAO.CategoriaDAO;
import Model.Categoria;

public class CategoriaTest {
    public static void main(String[] args) {
        CategoriaDAO dao = new CategoriaDAO();

        // 1) Criar categoria via construtor
        Categoria c = new Categoria(0, "Reagentes");
        dao.salvar(c);
        assert c.getId() > 0;
        System.out.println("Categoria salva com ID: " + c.getId());

        // 2) Buscar por ID
        Categoria buscada = dao.buscarPorId(c.getId());
        assert buscada != null;
        assert buscada.getNome().equals("Reagentes");

        // 3) Atualizar
        c = new Categoria(c.getId(), "Equipamentos Descartáveis");
        dao.atualizar(c);
        Categoria atualizada = dao.buscarPorId(c.getId());
        assert "Equipamentos Descartáveis".equals(atualizada.getNome());

        // 4) Listar todos
        assert dao.listarTodos().size() > 0;

        // 5) Deletar
        dao.deletar(c.getId());
        assert dao.buscarPorId(c.getId()) == null;

        System.out.println("Todos os testes de Categoria passaram!");
    }
}
