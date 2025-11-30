package com.vectorgarman.dto;

public class ComentarioRequest {
    private Long idusuario;
    private Long idreporte;
    private Long idcomentariopadre;
    private String contenido;

    public ComentarioRequest() {
    }

    public ComentarioRequest(Long idusuario, Long idreporte, Long idcomentariopadre, String contenido) {
        this.idusuario = idusuario;
        this.idreporte = idreporte;
        this.idcomentariopadre = idcomentariopadre;
        this.contenido = contenido;
    }

    public Long getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(Long idusuario) {
        this.idusuario = idusuario;
    }

    public Long getIdreporte() {
        return idreporte;
    }

    public void setIdreporte(Long idreporte) {
        this.idreporte = idreporte;
    }

    public Long getIdcomentariopadre() {
        return idcomentariopadre;
    }

    public void setIdcomentariopadre(Long idcomentariopadre) {
        this.idcomentariopadre = idcomentariopadre;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
}
