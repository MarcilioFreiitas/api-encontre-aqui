package com.encontreaqui.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "servicos")
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O título não pode ser nulo.")
    @Size(min = 3, max = 255, message = "O título deve ter entre 3 e 255 caracteres.")
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @NotNull(message = "O preço não pode ser nulo.")
    @Positive(message = "O preço deve ser um valor positivo.")
    private BigDecimal preco;

    @NotNull(message = "A categoria não pode ser nula.")
    @Size(min = 3, max = 100, message = "A categoria deve ter entre 3 e 100 caracteres.")
    private String categoria;

    @NotNull(message = "A localização não pode ser nula.")
    @Size(min = 3, max = 255, message = "A localização deve ter entre 3 e 255 caracteres.")
    private String localizacao;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAtualizacao;

    @NotNull(message = "A área de atuação não pode ser nula.")
    @Size(min = 3, max = 100, message = "A área de atuação deve ter entre 3 e 100 caracteres.")
    private String areaAtuacao;

    @NotNull(message = "O tempo médio de atendimento não pode ser nulo.")
    @Positive(message = "O tempo médio de atendimento deve ser um número positivo.")
    private Integer tempoMedioAtendimento;

    @NotNull(message = "A indicação de agendamento prévio não pode ser nula.")
    private Boolean necessitaAgendamento;

    @NotNull(message = "O nome do profissional responsável não pode ser nulo.")
    @Size(min = 3, max = 100, message = "O nome do profissional responsável deve ter entre 3 e 100 caracteres.")
    private String profissionalResponsavel;

    // Associação com o usuário
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Associação com fotos
    @OneToMany(mappedBy = "servico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Foto> fotos;

    // Campos de moderação
    private boolean flagged = false;
    private String flagReason;

    public Servico() { }

    public Servico(Long id, String titulo, String descricao, BigDecimal preco, String categoria, String localizacao,
                   Date dataCriacao, Date dataAtualizacao, String areaAtuacao, Integer tempoMedioAtendimento,
                   Boolean necessitaAgendamento, String profissionalResponsavel, Usuario usuario, List<Foto> fotos) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.preco = preco;
        this.categoria = categoria;
        this.localizacao = localizacao;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
        this.areaAtuacao = areaAtuacao;
        this.tempoMedioAtendimento = tempoMedioAtendimento;
        this.necessitaAgendamento = necessitaAgendamento;
        this.profissionalResponsavel = profissionalResponsavel;
        this.usuario = usuario;
        this.fotos = fotos;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }

    public Date getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(Date dataCriacao) { this.dataCriacao = dataCriacao; }

    public Date getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(Date dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }

    public String getAreaAtuacao() { return areaAtuacao; }
    public void setAreaAtuacao(String areaAtuacao) { this.areaAtuacao = areaAtuacao; }

    public Integer getTempoMedioAtendimento() { return tempoMedioAtendimento; }
    public void setTempoMedioAtendimento(Integer tempoMedioAtendimento) { this.tempoMedioAtendimento = tempoMedioAtendimento; }

    public Boolean getNecessitaAgendamento() { return necessitaAgendamento; }
    public void setNecessitaAgendamento(Boolean necessitaAgendamento) { this.necessitaAgendamento = necessitaAgendamento; }

    public String getProfissionalResponsavel() { return profissionalResponsavel; }
    public void setProfissionalResponsavel(String profissionalResponsavel) { this.profissionalResponsavel = profissionalResponsavel; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public List<Foto> getFotos() { return fotos; }
    public void setFotos(List<Foto> fotos) { this.fotos = fotos; }

    public boolean isFlagged() { return flagged; }
    public void setFlagged(boolean flagged) { this.flagged = flagged; }

    public String getFlagReason() { return flagReason; }
    public void setFlagReason(String flagReason) { this.flagReason = flagReason; }
}