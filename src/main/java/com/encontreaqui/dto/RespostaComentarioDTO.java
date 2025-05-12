package com.encontreaqui.dto;

public class RespostaComentarioDTO {
    private Long comentarioId;
    private String resposta;
    
    // Getters e Setters
    public Long getComentarioId() {
        return comentarioId;
    }
    public void setComentarioId(Long comentarioId) {
        this.comentarioId = comentarioId;
    }
    public String getResposta() {
        return resposta;
    }
    public void setResposta(String resposta) {
        this.resposta = resposta;
    }
}
