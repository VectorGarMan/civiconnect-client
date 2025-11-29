package com.vectorgarman.dto;

public class VotarReporteRequest {

    private Long idusuario;
    private Long idreporte;

    public VotarReporteRequest() {
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
}
