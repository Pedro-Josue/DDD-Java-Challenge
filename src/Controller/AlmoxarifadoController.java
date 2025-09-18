package Controller;

import DAO.MaterialDAO;
import DAO.MovimentacaoDAO;
import Model.Material;
import Model.Movimentacao;

import java.sql.SQLException;
import java.util.List;

public class AlmoxarifadoController {
    public MaterialDAO materialDAO = new MaterialDAO();
    public MovimentacaoDAO movimentacaoDAO = new MovimentacaoDAO();

    // --- Métodos de relevância ---

    // 1) Listar materiais com estoque baixo
    public List<Material> listarEstoqueBaixo(int limite) {
        return materialDAO.listarTodos().stream()
                .filter(m -> m.getQuantidadeEstoque() <= limite)
                .toList();
    }

    // 2) Total de materiais em estoque
    public int totalEstoque() {
        return materialDAO.listarTodos().stream()
                .mapToInt(Material::getQuantidadeEstoque)
                .sum();
    }

    // 3) Últimas movimentações registradas
    public List<Movimentacao> ultimasMovimentacoes(int qtd) {
        List<Movimentacao> todas = movimentacaoDAO.listarTodos();
        return todas.stream().sorted((a,b) -> b.getId() - a.getId()) // mais recentes primeiro
                .limit(qtd)
                .toList();
    }

    // 4) Verificar se material existe antes de registrar movimentação
    public boolean materialExiste(int idMaterial) {
        return materialDAO.buscarPorId(idMaterial) != null;
    }
}
