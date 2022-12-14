package br.edu.ifsc.fln.model.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrdemServico {

    private long numero;
    private BigDecimal total;
    private LocalDate agenda;
    private double taxaDesconto;

    private EstatusServico eStatusServico;

    private List<ItemOS> itensOS;
    private Veiculo veiculo;

    public long getNumero() {
        return numero;
    }

    public void setNumero(long numero) {
        this.numero = numero;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public LocalDate getAgenda() {
        return agenda;
    }

    public void setAgenda(LocalDate agenda) {
        this.agenda = agenda;
    }

    public double getTaxaDesconto() {
        return taxaDesconto;
    }

    public void setTaxaDesconto(double taxaDesconto) {
        this.taxaDesconto = taxaDesconto;
    }

    public EstatusServico geteStatusServico() {
        return eStatusServico;
    }

    public void seteStatusServico(EstatusServico eStatusServico) {
        this.eStatusServico = eStatusServico;
    }

    public List<ItemOS> getItensOS() {
        return itensOS;
    }

    public void setItensOS(List<ItemOS> itensOS) {
        this.itensOS = itensOS;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public void add(ItemOS itemOS) {
        if (itensOS == null) {
            itensOS = new ArrayList<>();
        }
        itensOS.add(itemOS);
        itemOS.setOrdemServico(this);
    }

    public void remove(ItemOS itemOS) {
        itensOS.remove(itemOS);
    }

    public void calcularTotalServico() {
        total = new BigDecimal(0.0);
        for (ItemOS item : this.getItensOS()) {
            total = total.add(item.getValorServico());
        }
        if (taxaDesconto >= 0) {
            BigDecimal desconto = new BigDecimal(total.doubleValue() * taxaDesconto / 100.0);
            total = total.subtract(desconto);
        }
    }

}
