package Model;

public class Material {
    private int id;
    private String nome;
    private String descricao;
    private int quantidadeEstoque;
    private String unidadeMedida;
    private Categoria categoria;
    //construtor

    public Material(int id, String nome, String descricao, int quantidadeEstoque, String unidadeMedida, Categoria categoria) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.quantidadeEstoque = quantidadeEstoque;
        this.unidadeMedida = unidadeMedida;
        this.categoria = categoria;
    }
    //getters
    public int getId() {
        return id;
    }
    public String getNome() {
        return nome;
    }
    public String getDescricao() {
        return descricao;
    }
    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }
    public String getUnidadeMedida() {
        return unidadeMedida;
    }
    public Categoria getCategoria() {
        return categoria;
    }
    //setters
    public void setId(int id) {
        this.id = id;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public void setQuantidadeEstoque(int quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }
    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
