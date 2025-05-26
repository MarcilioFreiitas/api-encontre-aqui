package com.encontreaqui.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "alugueis")
public class Aluguel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Campos do anúncio de aluguel
    @NotNull(message = "O título não pode ser nulo.")
    @Size(min = 3, max = 255, message = "O título deve ter entre 3 e 255 caracteres.")
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @NotNull(message = "A categoria não pode ser nula.")
    @Size(min = 1, max = 100, message = "A categoria deve ter entre 1 e 100 caracteres.")
    private String categoria;

    @NotNull(message = "O endereço completo não pode ser nulo.")
    @Size(min = 10, max = 255, message = "O endereço completo deve ter entre 10 e 255 caracteres.")
    private String enderecoCompleto;

    @NotNull(message = "O valor do aluguel não pode ser nulo.")
    @Positive(message = "O valor do aluguel deve ser um valor positivo.")
    private BigDecimal valorAluguel;

    @NotNull(message = "O número de quartos não pode ser nulo.")
    @Positive(message = "O número de quartos deve ser um valor positivo.")
    private Integer numeroDeQuartos;

    @NotNull(message = "A área total do imóvel não pode ser nula.")
    @Positive(message = "A área total deve ser um valor positivo.")
    private Double areaEmM2;

    @NotNull(message = "A indicação de mobiliado não pode ser nula.")
    private Boolean mobiliado;

    @NotNull(message = "O valor do caução não pode ser nulo.")
    @Positive(message = "O valor do caução deve ser um valor positivo.")
    private BigDecimal valorCaucao;

    @NotNull(message = "O período mínimo de contrato não pode ser nulo.")
    @Size(min = 3, max = 100, message = "O período mínimo de contrato deve ter entre 3 e 100 caracteres.")
    private String periodoMinimoContrato;

    @Positive(message = "O número de banheiros deve ser um valor positivo.")
    private Integer numeroDeBanheiros;

    @Positive(message = "O número de vagas deve ser um valor positivo.")
    private Integer numeroDeVagas;

    @Temporal(TemporalType.DATE)
    private Date dataDisponibilidade;

    private String status;

    // Relacionamento com as fotos do aluguel
    @OneToMany(mappedBy = "aluguel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Foto> fotos;

    // Relacionamento com o usuário que cadastrou o aluguel
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // === Campos de moderação ===
    private boolean flagged = false;
    private String flagReason;

    public Aluguel() {
    }

    // getters e setters existentes
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
    // getters e setters dos campos de moderação
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
