package com.vectorgarman.dto;

public class EliminarComentarioRequest {
    private Long idcomentario;
    private Long idusuario;

    public EliminarComentarioRequest() {
    }

    public EliminarComentarioRequest(Long idcomentario, Long idusuario) {
        this.idcomentario = idcomentario;
        this.idusuario = idusuario;
    }

    public Long getIdcomentario() {
        return idcomentario;
    }

    public void setIdcomentario(Long idcomentario) {
        this.idcomentario = idcomentario;
    }

    public Long getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(Long idusuario) {
        this.idusuario = idusuario;
    }
}
