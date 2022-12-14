package br.edu.ifsc.fln.model.domain;

import java.io.Serializable;

public class Modelo implements Serializable {

    private int id;

    private String descricao;

    private Marca marca;

    private Motor motor;

    public Modelo() {
        this.createMotor();
    }

    public Modelo(int id, String descricao, Marca marca) {
        this.id = id;
        this.descricao = descricao;
        this.marca = marca;
    }

    private void createMotor() {
        this.motor = new Motor();
        this.motor.setModelo(this);
    }

    public Modelo(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public Motor getMotor() {
        return motor;
    }

    @Override
    public String toString() {
        return descricao;
    }

}
