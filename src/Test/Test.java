package Test;

import Controller.AlmoxarifadoController;
import Controller.UsuarioController;
import DAO.CategoriaDAO;
import DAO.MaterialDAO;
import DAO.MovimentacaoDAO;
import DAO.UsuarioDAO;
import Model.Categoria;
import Model.Material;
import Model.Movimentacao;
import Model.Usuario;

public class Test {
    public static void main(String[] args) {
        //Instanciando os controllers (metodos de relevancia)
        UsuarioController usuarioController = new UsuarioController();
        AlmoxarifadoController almoxarifadoController = new AlmoxarifadoController();

        //Instanciando DAOs
        CategoriaDAO categoriaDAO = new CategoriaDAO();
        MaterialDAO materialDAO = new MaterialDAO();
        MovimentacaoDAO movimentacaoDAO = new MovimentacaoDAO();
        UsuarioDAO usuarioDAO = new UsuarioDAO();

        System.out.println("=== INICIANDO TESTES DO SISTEMA DE ALMOXARIFADO ===\n");

        //Teste de validação de usuario
        Usuario usuarioValido = new Usuario(0, "João Silva", "joao@email.com", "senha123");
        String resultado = usuarioController.criarUsuario(usuarioValido);
        assert resultado.equals("Usuário criado com sucesso!");
        System.out.println("Validação de usuário OK");

        //Teste de login
        assert usuarioController.validarLogin("joao@email.com", "senha123");
        assert !usuarioController.validarLogin("joao@email.com", "senhaerrada");
        System.out.println("Login OK");

        //Teste de CRUD de Categoria
        Categoria categoria = new Categoria(0, "Material de Escritório");
        categoriaDAO.salvar(categoria);
        assert categoriaDAO.buscarPorId(categoria.getId()) != null;
        categoria.setNome("Material de Escritório - Atualizado");
        categoriaDAO.atualizar(categoria);
        assert !categoriaDAO.listarTodos().isEmpty();
        System.out.println("CRUD de Categoria OK");

        //Teste de CRUD de Material
        Material material = new Material(0, "Papel A4", "Papel sulfite A4", 100, "pacote", categoria);
        materialDAO.salvar(material);
        assert materialDAO.buscarPorId(material.getId()) != null;
        material.setQuantidadeEstoque(150);
        materialDAO.atualizar(material);
        assert !materialDAO.listarTodos().isEmpty();
        System.out.println("CRUD de Material OK");

        //Teste de estoque baixo
        Material materialEstoqueBaixo = new Material(0, "Caneta Azul", "Caneta esferográfica azul", 5, "unidade", categoria);
        materialDAO.salvar(materialEstoqueBaixo);
        assert !almoxarifadoController.listarEstoqueBaixo(10).isEmpty();
        System.out.println("Estoque baixo OK");

        //Teste de total em estoque
        assert almoxarifadoController.totalEstoque() > 0;
        System.out.println("Total em estoque OK");

        //Teste de CRUD de Movimentação
        Usuario usuarioMovimentacao = usuarioController.buscarUsuarioPorEmail("joao@email.com");
        Movimentacao movimentacao = new Movimentacao(0, material, "entrada", 50, java.time.LocalDateTime.now(), usuarioMovimentacao);
        movimentacaoDAO.salvar(movimentacao);
        assert movimentacaoDAO.buscarPorId(movimentacao.getId()) != null;
        assert !movimentacaoDAO.listarTodos().isEmpty();
        System.out.println("CRUD de Movimentação OK");

        //Teste de últimas movimentações
        assert almoxarifadoController.ultimasMovimentacoes(3).size() <= 3;
        System.out.println("Últimas movimentações OK");

        //Teste de verificação de material existente
        assert almoxarifadoController.materialExiste(material.getId());
        assert !almoxarifadoController.materialExiste(9999);
        System.out.println("Verificação de material existente OK");

        //Teste de CRUD de Usuario
        Usuario novoUsuario = new Usuario(0, "Maria Santos", "maria@email.com", "senha456");
        usuarioDAO.salvar(novoUsuario);
        assert usuarioDAO.buscarPorId(novoUsuario.getId()) != null;
        novoUsuario.setNome("Maria Santos - Atualizado");
        usuarioDAO.atualizar(novoUsuario);
        assert usuarioDAO.listarTodos().size() >= 2;
        System.out.println("CRUD de Usuário OK");

        System.out.println("\n=== TODOS OS TESTES FORAM CONCLUÍDOS COM SUCESSO! ===");

        // Limpeza dos dados de teste para nao afetar o banco de dados
        movimentacaoDAO.deletar(movimentacao.getId());
        materialDAO.deletar(materialEstoqueBaixo.getId());
        materialDAO.deletar(material.getId());
        categoriaDAO.deletar(categoria.getId());
        usuarioDAO.deletar(novoUsuario.getId());
        usuarioDAO.deletar(usuarioValido.getId());
    }

}
