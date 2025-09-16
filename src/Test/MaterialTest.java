package Test;

import DAO.CategoriaDAO;
import DAO.MaterialDAO;
import Model.Categoria;
import Model.Material;

public class MaterialTest {
    public static void main(String[] args) {
        CategoriaDAO categoriaDAO = new CategoriaDAO();
        MaterialDAO materialDAO = new MaterialDAO();

        // Criar categoria
        Categoria c = new Categoria(0, "Reagentes");
        categoriaDAO.salvar(c);

        // 1) Criar material via construtor
        Material m = new Material(0, "Ácido Sulfúrico", "Ácido para análises", 50, "ml", c);
        materialDAO.salvar(m);
        assert m.getId() > 0;

        // 2) Buscar por ID
        Material buscado = materialDAO.buscarPorId(m.getId());
        assert buscado != null;
        assert buscado.getNome().equals("Ácido Sulfúrico");

        // 3) Atualizar
        m = new Material(m.getId(), "Ácido Sulfúrico", "Ácido para análises", 100, "ml", c);
        materialDAO.atualizar(m);
        Material atualizado = materialDAO.buscarPorId(m.getId());
        assert atualizado.getQuantidadeEstoque() == 100;

        // 4) Listar todos
        assert materialDAO.listarTodos().size() > 0;

        // 5) Deletar
        materialDAO.deletar(m.getId());
        assert materialDAO.buscarPorId(m.getId()) == null;

        // Limpar categoria
        categoriaDAO.deletar(c.getId());

        System.out.println("Todos os testes de Material passaram!");
    }
}
