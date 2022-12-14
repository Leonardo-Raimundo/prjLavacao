package br.edu.ifsc.fln.model.domain;

public enum ETipoCombustivel {
    GASOLINA(1, "gasolina"), ETANOL(2, "etanol"), FLEX(3, "flex"), DIESEL(4, "diesel"), GNV(5, "gnv"), OUTRO(6, "outro");
    private int id;
    private String tipoCombustivel;

    private ETipoCombustivel(int id, String tipoCombustivel) {
        this.id = id;
        this.tipoCombustivel = tipoCombustivel;
    }

    private ETipoCombustivel(String tipoCombustivel) {
        this.tipoCombustivel = tipoCombustivel;
    }

    public int getId() {
        return id;
    }

    public String getTipoCombustivel() {
        return tipoCombustivel;
    }
}
