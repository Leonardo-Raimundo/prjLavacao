package br.edu.ifsc.fln.model.domain;

import java.io.Serializable;

public class Veiculo implements Serializable {

    private int id;
    private String placa;
    private Modelo modelo;
    private Cor cor;
    private String observacoes;
    private Cliente cliente;

    public Veiculo(int id, String placa, Modelo modelo, Cor cor, String observacoes) {
        this();
        this.id = id;
        this.placa = placa;
        this.modelo = modelo;
        this.cor = cor;
        this.observacoes = observacoes;

    }

    public Veiculo(int id, String placa, Modelo modelo, Cor cor, String observacoes, Cliente cliente) {
        this.id = id;
        this.placa = placa;
        this.modelo = modelo;
        this.cor = cor;
        this.observacoes = observacoes;
        this.modelo = modelo;
        this.cliente = cliente;
    }

    public Veiculo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    public Cor getCor() {
        return cor;
    }

    public void setCor(Cor cor) {
        this.cor = cor;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public String toString() {
        return "Veiculo{" + "placa=" + placa + ", modelo=" + modelo + '}';
    }

}
