package com.encontreaqui.model;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
public class Aluguel extends Anuncio {

    @NotNull(message = "O número de quartos não pode ser nulo.")
    @Positive(message = "O número de quartos deve ser um valor positivo.")
    private Integer numeroDeQuartos;

    @NotNull(message = "A área total do imóvel não pode ser nula.")
    @Positive(message = "A área total deve ser um valor positivo.")
    private Double areaEmM2;

    @NotNull(message = "A indicação de mobiliado ou não não pode ser nula.")
    private Boolean mobiliado;

    @NotNull(message = "O valor do caução não pode ser nulo.")
    @Positive(message = "O valor do caução deve ser um valor positivo.")
    private BigDecimal valorCaucao;

    @NotNull(message = "O período mínimo de contrato não pode ser nulo.")
    @Size(min = 3, max = 100, message = "O período mínimo de contrato deve ter entre 3 e 100 caracteres.")
    private String periodoMinimoContrato;

    public Aluguel(Integer numeroDeQuartos, Double areaEmM2, Boolean mobiliado, BigDecimal valorCaucao,
                   String periodoMinimoContrato) {
        super();
        this.numeroDeQuartos = numeroDeQuartos;
        this.areaEmM2 = areaEmM2;
        this.mobiliado = mobiliado;
        this.valorCaucao = valorCaucao;
        this.periodoMinimoContrato = periodoMinimoContrato;
    }

    public Integer getNumeroDeQuartos() {
        return numeroDeQuartos;
    }

    public void setNumeroDeQuartos(Integer numeroDeQuartos) {
        this.numeroDeQuartos = numeroDeQuartos;
    }

    public Double getAreaEmM2() {
        return areaEmM2;
    }

    public void setAreaEmM2(Double areaEmM2) {
        this.areaEmM2 = areaEmM2;
    }

    public Boolean getMobiliado() {
        return mobiliado;
    }

    public void setMobiliado(Boolean mobiliado) {
        this.mobiliado = mobiliado;
    }

    public BigDecimal getValorCaucao() {
        return valorCaucao;
    }

    public void setValorCaucao(BigDecimal valorCaucao) {
        this.valorCaucao = valorCaucao;
    }

    public String getPeriodoMinimoContrato() {
        return periodoMinimoContrato;
    }

    public void setPeriodoMinimoContrato(String periodoMinimoContrato) {
        this.periodoMinimoContrato = periodoMinimoContrato;
    }
}
