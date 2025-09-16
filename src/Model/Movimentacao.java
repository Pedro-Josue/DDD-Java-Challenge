package Model;

import java.time.LocalDateTime;

public class Movimentacao {
    private int id;
    private Material material;
    private String tipo;
    private int quantidade;
    private LocalDateTime data;
    private Usuario usuario;
    //construtor
    public Movimentacao(int id, Material material, String tipo, int quantidade, LocalDateTime data, Usuario usuario) {
        this.id = id;
        this.material = material;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.data = data;
        this.usuario = usuario;
    }
    //getters
    public int getId() {
        return id;
    }
    public Material getMaterial() {
        return material;
    }
    public String getTipo() {
        return tipo;
    }
    public int getQuantidade() {
        return quantidade;
    }
    public LocalDateTime getData() {
        return data;
    }
    public Usuario getUsuario() {
        return usuario;
    }
    //setters
    public void setId(int id) {
        this.id = id;
    }
    public void setMaterial(Material material) {
        this.material = material;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
    public void setData(LocalDateTime data) {
        this.data = data;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
