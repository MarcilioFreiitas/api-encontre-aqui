package com.encontreaqui.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ServicoDTO {
    // Campos comuns
    private Long id;
    private String titulo;
    private String descricao;
    private BigDecimal preco;
    private String categoria;
    private String localizacao;
    private Date dataCriacao;
    private Date dataAtualizacao;
    
    // Campos específicos do serviço
    private String areaAtuacao;
    private Integer tempoMedioAtendimento;
    private boolean necessitaAgendamento;
    private String profissionalResponsavel;
    
    // Campos para relacionamento
    private Long usuarioId;
    private List<String> fotos;
    
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
    
    public Date getDataCriacao() {
        return dataCriacao;
    }
    
    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
    
    public Date getDataAtualizacao() {
        return dataAtualizacao;
    }
    
    public void setDataAtualizacao(Date dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
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
}
