package com.encontreaqui.dto;

import java.math.BigDecimal;

public class AluguelDTO {
    // Campos comuns (herdados de Anuncio, se desejado)
    private Long id;
    private String titulo;
    private String descricao;
    private BigDecimal preco;
    private String categoria;
    private String localizacao;
    
    // Campos específicos da entidade Aluguel
    private Integer numeroDeQuartos;
    private Double areaEmM2;
    private Boolean mobiliado;
    private BigDecimal valorCaucao;
    private String periodoMinimoContrato;

    // Construtor padrão
    public AluguelDTO() { }

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
    public BigDecimal getPreco() {
        return preco;
    }
    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }
    public String getCategoria() {
        return categoria;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    public String getLocalizacao() {
        return localizacao;
    }
    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
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
