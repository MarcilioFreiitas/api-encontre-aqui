package com.encontreaqui.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "comercios")
public class Comercio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Dados específicos do comércio
    @NotNull(message = "O título não pode ser nulo.")
    @Size(min = 3, max = 255, message = "O título deve ter entre 3 e 255 caracteres.")
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @NotNull(message = "O endereço completo não pode ser nulo.")
    @Size(min = 10, max = 255, message = "O endereço completo deve ter entre 10 e 255 caracteres.")
    private String enderecoCompleto;

    @NotNull(message = "O horário de funcionamento não pode ser nulo.")
    @Pattern(regexp = "^\\d{2}:\\d{2}-\\d{2}:\\d{2}$", message = "O horário de funcionamento deve estar no formato HH:MM-HH:MM.")
    private String horarioFuncionamento;

    @NotNull(message = "O telefone não pode ser nulo.")
    @Pattern(regexp = "\\+\\d{2}\\s\\d{9}", message = "O telefone deve estar no formato +DD DDDDDDDDD.")
    private String telefone;

    @Size(max = 255, message = "O website deve ter no máximo 255 caracteres.")
    private String website;

    @NotNull(message = "O tipo de estabelecimento não pode ser nulo.")
    @Size(min = 3, max = 100, message = "O tipo de estabelecimento deve ter entre 3 e 100 caracteres.")
    private String tipoEstabelecimento;
    
    // Novo campo: categoria – pode ser obrigatório
    @NotNull(message = "A categoria não pode ser nula.")
    @Size(min = 1, max = 100, message = "A categoria deve ter entre 1 e 100 caracteres.")
    private String categoria;
    
    // Dados comuns de auditoria
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAtualizacao;
    
    // Associação com fotos – relacionamento OneToMany
    @OneToMany(mappedBy = "comercio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Foto> fotos;
    
    // Associação com o usuário que criou o comércio
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    
    public Comercio() {}

    // Construtor completo, getters e setters
    // Exemplo de construtor com campo categoria (semântica semelhante aos demais)
    public Comercio(Long id, String titulo, String descricao, String enderecoCompleto,
                    String horarioFuncionamento, String telefone, String website,
                    String tipoEstabelecimento, String categoria, Date dataCriacao, 
                    Date dataAtualizacao, List<Foto> fotos, Usuario usuario) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.enderecoCompleto = enderecoCompleto;
        this.horarioFuncionamento = horarioFuncionamento;
        this.telefone = telefone;
        this.website = website;
        this.tipoEstabelecimento = tipoEstabelecimento;
        this.categoria = categoria;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
        this.fotos = fotos;
        this.usuario = usuario;
    }

    // Getters and Setters

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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
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

    public List<Foto> getFotos() {
        return fotos;
    }

    public void setFotos(List<Foto> fotos) {
        this.fotos = fotos;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
