package br.edu.ifsc.fln.model.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Cliente implements ICliente {

    private int id;
    private String nome;
    private String celular;
    private String email;
    private LocalDate dataCadastro;

    protected List<Veiculo> veiculos;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public List<Veiculo> getVeiculos() {
        return veiculos;
    }

    public void setVeiculos(List<Veiculo> veiculos) {
        this.veiculos = veiculos;
    }

    public void add(Veiculo veiculo) {
        if (veiculos == null) {
            veiculos = new ArrayList<>();
        }
        veiculos.add(veiculo);
        veiculo.setCliente(this);
    }

    public void remove(Veiculo veiculo) {
        if (veiculos != null) {
            veiculos.remove(veiculo);
            veiculo.setCliente(null);
        }
    }

    @Override
    public String toString() {
        return nome;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Cliente other = (Cliente) obj;
        return this.id == other.id;
    }

    public String getDados() {
        StringBuilder sb = new StringBuilder();
        sb.append("Dados do cliente ").append(this.getClass().getSimpleName()).append("\n");
        sb.append("Id........: ").append(id).append("\n");
        sb.append("Nome......: ").append(nome).append("\n");
        sb.append("Celular......: ").append(celular).append("\n");
        sb.append("Email.....: ").append(email).append("\n");
        sb.append("Data Cadastro.....: ").append(dataCadastro).append("\n");
        return sb.toString();
    }
    
     public String getDados(String observacao) {
        StringBuilder sb = new StringBuilder();
        sb.append("Dados do cliente ").append(this.getClass().getSimpleName()).append("\n");
        sb.append("Id........: ").append(id).append("\n");
        sb.append("Nome......: ").append(nome).append("\n");
        sb.append("Celular......: ").append(celular).append("\n");
        sb.append("Email.....: ").append(email).append("\n");
        sb.append("Data Cadastro.....: ").append(dataCadastro).append("\n");
        return sb.toString();
    }

}
