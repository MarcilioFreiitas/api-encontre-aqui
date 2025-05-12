package com.encontreaqui.dto;

import java.util.Date;

public class AvaliacaoDTO {
    
    private Long id;
    private Integer nota;
    private String comentario;
    private String resposta; // Novo campo para armazenar a resposta do dono do anúncio
    private Date dataCadastro;
    private String tipoItem;
    private Long itemId;
    private Long usuarioId;
    
    public AvaliacaoDTO() { }
    
    // Getters e Setters
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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
    
    public Long getUsuarioId() {
        return usuarioId;
    }
    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
}
