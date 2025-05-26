package com.encontreaqui.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "avaliacoes")
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "A nota da avaliação não pode ser nula.")
    @Min(value = 1, message = "A nota mínima é 1.")
    @Max(value = 5, message = "A nota máxima é 5.")
    private Integer nota;

    @Column(columnDefinition = "TEXT")
    private String comentario;

    @Column(columnDefinition = "TEXT")
    private String resposta;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCadastro;

    @NotNull(message = "O tipo do item avaliado não pode ser nulo.")
    @Size(min = 1, max = 50, message = "O tipo do item deve ter entre 1 e 50 caracteres.")
    @Column(name = "tipo_item")
    private String tipoItem;

    @NotNull(message = "O id do item avaliado não pode ser nulo.")
    @Column(name = "item_id")
    private Long itemId;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // NOVOS CAMPOS PARA MODERAÇÃO
    @Column(nullable = false)
    private boolean flagged = false;

    @Column(columnDefinition = "TEXT")
    private String flagReason;

    public Avaliacao() {
        this.dataCadastro = new Date();
    }

    @PrePersist
    protected void onCreate() {
        if (dataCadastro == null) {
            dataCadastro = new Date();
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getNota() { return nota; }
    public void setNota(Integer nota) { this.nota = nota; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public String getResposta() { return resposta; }
    public void setResposta(String resposta) { this.resposta = resposta; }

    public Date getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(Date dataCadastro) { this.dataCadastro = dataCadastro; }

    public String getTipoItem() { return tipoItem; }
    public void setTipoItem(String tipoItem) { this.tipoItem = tipoItem; }

    public Long getItemId() { return itemId; }
    public void setItemId(Long itemId) { this.itemId = itemId; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public boolean isFlagged() { return flagged; }
    public void setFlagged(boolean flagged) { this.flagged = flagged; }

    public String getFlagReason() { return flagReason; }
    public void setFlagReason(String flagReason) { this.flagReason = flagReason; }
}
