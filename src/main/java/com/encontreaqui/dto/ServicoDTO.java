package com.encontreaqui.dto;

import java.math.BigDecimal;

public class ServicoDTO {
    // Campos comuns (herdados de Anuncio, se desejado)
    private Long id;
    private String titulo;
    private String descricao;
    private BigDecimal preco;
    private String categoria;
    private String localizacao;
    
    // Campos específicos da entidade Serviço
    private String areaAtuacao;
    private Integer tempoMedioAtendimento;
    private boolean necessitaAgendamento;
    private String profissionalResponsavel;

    // Construtor padrão
    public ServicoDTO() { }

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
    public String getAreaAtuacao() {
        return areaAtuacao;
    }
    public void setAreaAtuacao(String areaAtuacao) {
        this.areaAtuacao = areaAtuacao;
    }
    public Integer getTempoMedioAtendimento() {
        return tempoMedioAtendimento;
    }
    public void setTempoMedioAtendimento(Integer tempoMedioAtendimento) {
        this.tempoMedioAtendimento = tempoMedioAtendimento;
    }
    public boolean isNecessitaAgendamento() {
        return necessitaAgendamento;
    }
    public void setNecessitaAgendamento(boolean necessitaAgendamento) {
        this.necessitaAgendamento = necessitaAgendamento;
    }
    public String getProfissionalResponsavel() {
        return profissionalResponsavel;
    }
    public void setProfissionalResponsavel(String profissionalResponsavel) {
        this.profissionalResponsavel = profissionalResponsavel;
    }
}
