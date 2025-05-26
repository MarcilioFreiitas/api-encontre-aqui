package com.encontreaqui.dto;

import java.util.Date;

public class AvaliacaoDTO {
    private Long id;
    private Integer nota;
    private String comentario;
    private String resposta;
    private Date dataCadastro;
    private String tipoItem;
    private Long itemId;
    private Long usuarioId;

    // NOVOS CAMPOS PARA MODERAÇÃO
    private boolean flagged;
    private String flagReason;

    public AvaliacaoDTO() {}

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

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public boolean isFlagged() { return flagged; }
    public void setFlagged(boolean flagged) { this.flagged = flagged; }

    public String getFlagReason() { return flagReason; }
    public void setFlagReason(String flagReason) { this.flagReason = flagReason; }
}
