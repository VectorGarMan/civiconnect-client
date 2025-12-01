package com.vectorgarman.dto;

public class ActualizarNombreUsuarioRequest {
    private Long idusuario;
    private String nuevoNombreUsuario;
    private Long idcolonia;

    public ActualizarNombreUsuarioRequest() {
    }

    public ActualizarNombreUsuarioRequest(Long idusuario, String nuevoNombreUsuario, Long idcolonia) {
        this.idusuario = idusuario;
        this.nuevoNombreUsuario = nuevoNombreUsuario;
        this.idcolonia = idcolonia;
    }

    public Long getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(Long idusuario) {
        this.idusuario = idusuario;
    }

    public String getNuevoNombreUsuario() {
        return nuevoNombreUsuario;
    }

    public void setNuevoNombreUsuario(String nuevoNombreUsuario) {
        this.nuevoNombreUsuario = nuevoNombreUsuario;
    }

    public Long getIdcolonia() {
        return idcolonia;
    }

    public void setIdcolonia(Long idcolonia) {
        this.idcolonia = idcolonia;
    }
}
