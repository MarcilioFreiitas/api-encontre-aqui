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

    // A nota da avaliação (de 1 a 5)
    @NotNull(message = "A nota da avaliação não pode ser nula.")
    @Min(value = 1, message = "A nota mínima é 1.")
    @Max(value = 5, message = "A nota máxima é 5.")
    private Integer nota;

    // Comentário opcional da avaliação
    @Column(columnDefinition = "TEXT")
    private String comentario;

    // NEW: Field to store the response (reply) to a comment
    @Column(columnDefinition = "TEXT")
    private String resposta;

    // Data de cadastro da avaliação (definida automaticamente)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCadastro;

    /**
     * Campo que indica o tipo do item avaliado.
     * Pode ser, por exemplo, "comercio", "servico" ou "aluguel".
     */
    @NotNull(message = "O tipo do item avaliado não pode ser nulo.")
    @Size(min = 1, max = 50, message = "O tipo do item deve ter entre 1 e 50 caracteres.")
    @Column(name = "tipo_item")
    private String tipoItem;

    /**
     * Armazena o ID do item avaliado. Esse valor corresponde ao ID da entidade
     * Comércio, Serviço ou Aluguel, conforme informado em 'tipoItem'.
     */
    @NotNull(message = "O id do item avaliado não pode ser nulo.")
    @Column(name = "item_id")
    private Long itemId;

    // Relacionamento com o usuário que fez a avaliação
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Construtor padrão
    public Avaliacao() {
        this.dataCadastro = new Date();
    }

    // Construtor completo para facilitar a criação
    public Avaliacao(@NotNull Integer nota, String comentario, @NotNull String tipoItem,
                     @NotNull Long itemId, @NotNull Usuario usuario) {
        this.nota = nota;
        this.comentario = comentario;
        this.tipoItem = tipoItem;
        this.itemId = itemId;
        this.usuario = usuario;
        this.dataCadastro = new Date();
    }

    @PrePersist
    protected void onCreate() {
        if (dataCadastro == null) {
            dataCadastro = new Date();
        }
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public Integer getNota() {
        return nota;
    }

    public void setNota(Integer nota) {
        this.nota = nota;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getTipoItem() {
        return tipoItem;
    }

    public void setTipoItem(String tipoItem) {
        this.tipoItem = tipoItem;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
