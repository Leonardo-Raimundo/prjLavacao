package br.edu.ifsc.fln.model.domain;

public class Cor {
    private int id;
    private String nome;

    public Cor(String nome) {
        this.nome = nome;
    }
    
    public Cor() {
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return nome;
    }
    
    
    
}
