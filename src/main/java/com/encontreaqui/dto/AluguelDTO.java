package com.encontreaqui.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class AluguelDTO {
    // Identificador
    private Long id;

    // Dados do anúncio
    private String titulo;
    private String descricao;
    private String categoria;
    private String enderecoCompleto;
    private BigDecimal valorAluguel;
    private Integer numeroDeQuartos;
    private Double areaEmM2;
    private Boolean mobiliado;
    private BigDecimal valorCaucao;
    private String periodoMinimoContrato;
    private Integer numeroDeBanheiros;
    private Integer numeroDeVagas;
    private Date dataDisponibilidade;
    private String status;

    // Relacionamentos
    private Long usuarioId;
    private List<String> fotos;

    // Cálculos auxiliares
    private Double mediaAvaliacoes;

    // Campos de moderação
    private boolean flagged;
    private String flagReason;

    // Construtor padrão
    public AluguelDTO() {}

    // Getters e Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getEnderecoCompleto() {
        return enderecoCompleto;
    }
    public void setEnderecoCompleto(String enderecoCompleto) {
        this.enderecoCompleto = enderecoCompleto;
    }

    public BigDecimal getValorAluguel() {
        return valorAluguel;
    }
    public void setValorAluguel(BigDecimal valorAluguel) {
        this.valorAluguel = valorAluguel;
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

    public Integer getNumeroDeBanheiros() {
        return numeroDeBanheiros;
    }
    public void setNumeroDeBanheiros(Integer numeroDeBanheiros) {
        this.numeroDeBanheiros = numeroDeBanheiros;
    }

    public Integer getNumeroDeVagas() {
        return numeroDeVagas;
    }
    public void setNumeroDeVagas(Integer numeroDeVagas) {
        this.numeroDeVagas = numeroDeVagas;
    }

    public Date getDataDisponibilidade() {
        return dataDisponibilidade;
    }
    public void setDataDisponibilidade(Date dataDisponibilidade) {
        this.dataDisponibilidade = dataDisponibilidade;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }
    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public List<String> getFotos() {
        return fotos;
    }
    public void setFotos(List<String> fotos) {
        this.fotos = fotos;
    }

    public Double getMediaAvaliacoes() {
        return mediaAvaliacoes;
    }
    public void setMediaAvaliacoes(Double mediaAvaliacoes) {
        this.mediaAvaliacoes = mediaAvaliacoes;
    }

    // Getters e Setters dos campos de moderação
    public boolean isFlagged() {
        return flagged;
    }
    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    public String getFlagReason() {
        return flagReason;
    }
    public void setFlagReason(String flagReason) {
        this.flagReason = flagReason;
    }
}