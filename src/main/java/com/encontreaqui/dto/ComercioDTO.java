package com.encontreaqui.dto;

public class ComercioDTO {
    private Long id;
    private String titulo;
    private String descricao;
    private String enderecoCompleto;
    private String horarioFuncionamento;
    private String telefone;
    private String website;
    private String tipoEstabelecimento;
    
    // Construtores, getters e setters
    public ComercioDTO() {}

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
    public String getEnderecoCompleto() {
        return enderecoCompleto;
    }
    public void setEnderecoCompleto(String enderecoCompleto) {
        this.enderecoCompleto = enderecoCompleto;
    }
    public String getHorarioFuncionamento() {
        return horarioFuncionamento;
    }
    public void setHorarioFuncionamento(String horarioFuncionamento) {
        this.horarioFuncionamento = horarioFuncionamento;
    }
    public String getTelefone() {
        return telefone;
    }
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    public String getWebsite() {
        return website;
    }
    public void setWebsite(String website) {
        this.website = website;
    }
    public String getTipoEstabelecimento() {
        return tipoEstabelecimento;
    }
    public void setTipoEstabelecimento(String tipoEstabelecimento) {
        this.tipoEstabelecimento = tipoEstabelecimento;
    }
}
