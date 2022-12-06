package br.edu.ifsc.fln.model.domain;

import java.time.LocalDate;

public class PessoaFisica extends Cliente {

    private String cpf;
    private LocalDate dataNascimento;

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    @Override
    public String getDados() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.getDados()).append("\n");
        sb.append("CPF......: ").append(cpf).append("\n");
        sb.append("Data de nascimento.....: ").append(dataNascimento).append("\n");
        return sb.toString();
    }
}
