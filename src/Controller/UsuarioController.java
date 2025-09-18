package Controller;

import DAO.UsuarioDAO;
import Model.Usuario;

import java.sql.SQLException;
import java.util.List;

public class UsuarioController {
    //Instanciando a classe UsuarioDAO para realizar as operações necessarias
    public UsuarioDAO usuarioDAO = new UsuarioDAO();

    //Metodos de relevancia
    //Metodo para validacao de login do usuario
    public boolean validarLogin(String email, String senha) {
        List<Usuario> usuarios = usuarioDAO.listarTodos();
        for (Usuario u : usuarios) {
            if (u.getEmail().equals(email) && u.getSenha().equals(senha)) {
                return true;
            }
        }
        return false;
    }
    //Criar novo usuario com validacao
    public String criarUsuario(Usuario u) {
        if (u.getNome().trim().length() < 3) return "Nome muito curto!";
        if (!u.getEmail().contains("@")) return "Email inválido!";
        if (u.getSenha().length() < 4) return "Senha deve ter ao menos 4 caracteres!";
        usuarioDAO.salvar(u);
        return "Usuário criado com sucesso!";
    }

    //Listar todos os usuarios chamando o metodo da classe DAO
    public List<Usuario> listarUsuarios() {
        return usuarioDAO.listarTodos();
    }

    //Buscar usuario por email usando filtro da classe DAO
    public Usuario buscarUsuarioPorEmail(String email) {
        List<Usuario> usuarios = usuarioDAO.listarTodos();
        for (Usuario u : usuarios) {
            if (u.getEmail().equals(email)) return u;
        }
        return null;
    }
}
