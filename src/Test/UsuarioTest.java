package Test;

import DAO.UsuarioDAO;
import Model.Usuario;

public class UsuarioTest {
    public static void main(String[] args) {
        UsuarioDAO dao = new UsuarioDAO();

        // 1) Criar usuário via construtor
        Usuario u = new Usuario(0, "João da Silva", "joao@gmail.com", "123456");
        dao.salvar(u);
        assert u.getId() > 0;

        // 2) Buscar por ID
        Usuario buscado = dao.buscarPorId(u.getId());
        assert buscado != null;
        assert buscado.getEmail().equals("joao@gmail.com");

        // 3) Atualizar
        u = new Usuario(u.getId(), "João Souza", "joao@gmail.com", "123456");
        dao.atualizar(u);
        Usuario atualizado = dao.buscarPorId(u.getId());
        assert "João Souza".equals(atualizado.getNome());

        // 4) Listar todos
        assert dao.listarTodos().size() > 0;

        // 5) Deletar
        dao.deletar(u.getId());
        assert dao.buscarPorId(u.getId()) == null;

        System.out.println("Todos os testes de Usuario passaram!");
    }
}
