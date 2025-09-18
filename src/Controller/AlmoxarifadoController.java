package Controller;

import DAO.MaterialDAO;
import DAO.MovimentacaoDAO;
import Model.Material;
import Model.Movimentacao;
import java.util.List;

public class AlmoxarifadoController {
    //instanciando as classes DAO utilizadas para os metodos de relevancia
    public MaterialDAO materialDAO = new MaterialDAO();
    public MovimentacaoDAO movimentacaoDAO = new MovimentacaoDAO();

    //Metodos de relevancia
    //Listar materiais com estoque baixo
    public List<Material> listarEstoqueBaixo(int limite) {
        return materialDAO.listarTodos().stream()
                .filter(m -> m.getQuantidadeEstoque() <= limite)
                .toList();
    }

    //Total de materiais em estoque
    public int totalEstoque() {
        return materialDAO.listarTodos().stream()
                .mapToInt(Material::getQuantidadeEstoque)
                .sum();
    }

    //Ultimas movimentações registradas
    public List<Movimentacao> ultimasMovimentacoes(int qtd) {
        List<Movimentacao> todas = movimentacaoDAO.listarTodos();
        return todas.stream().sorted((a,b) -> b.getId() - a.getId()) // mais recentes primeiro
                .limit(qtd)
                .toList();
    }

    //Verificar se material existe antes de registrar movimentação
    public boolean materialExiste(int idMaterial) {
        return materialDAO.buscarPorId(idMaterial) != null;
    }
}
