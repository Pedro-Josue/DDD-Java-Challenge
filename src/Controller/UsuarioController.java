package Controller;

import DAO.UsuarioDAO;
import Model.Usuario;

import java.sql.SQLException;
import java.util.List;

public class UsuarioController {
    public UsuarioDAO usuarioDAO = new UsuarioDAO();

    // --- Métodos de relevância ---

    public boolean validarLogin(String email, String senha) {
        List<Usuario> usuarios = usuarioDAO.listarTodos();
        for (Usuario u : usuarios) {
            if (u.getEmail().equals(email) && u.getSenha().equals(senha)) {
                return true;
            }
        }
        return false;
    }

    // 2) Criar novo usuário com validação
    public String criarUsuario(Usuario u) {
        if (u.getNome().trim().length() < 3) return "Nome muito curto!";
        if (!u.getEmail().contains("@")) return "Email inválido!";
        if (u.getSenha().length() < 4) return "Senha deve ter ao menos 4 caracteres!";
        usuarioDAO.salvar(u);
        return "Usuário criado com sucesso!";
    }

    // 3) Listar todos os usuários
    public List<Usuario> listarUsuarios() {
        return usuarioDAO.listarTodos();
    }

    // 4) Buscar usuário por email usando filtro da lista
    public Usuario buscarUsuarioPorEmail(String email) {
        List<Usuario> usuarios = usuarioDAO.listarTodos();
        for (Usuario u : usuarios) {
            if (u.getEmail().equals(email)) return u;
        }
        return null;
    }
}
