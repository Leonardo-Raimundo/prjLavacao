package br.edu.ifsc.fln.model.domain;

public class PessoaJuridica extends Cliente {

    private String cnpj;
    private String inscricaoEstadual;

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    @Override
    public String getDados() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.getDados()).append("\n");
        sb.append("CNPJ.......: ").append(cnpj).append("\n");
        sb.append("Inscrição Estadual......: ").append(inscricaoEstadual).append("\n");
        return sb.toString();
    }
}
