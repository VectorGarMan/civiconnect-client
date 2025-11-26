package com.vectorgarman.dto;

public class UsuarioRequest {
    private Long idtipousuario;

    private Long idcolonia;

    private String email;

    private String contrasena;

    private String nombreusuario;

    public UsuarioRequest() {
    }

    public UsuarioRequest(Long idtipousuario, Long idcolonia, String email, String contrasena, String nombreusuario) {
        this.idtipousuario = idtipousuario;
        this.idcolonia = idcolonia;
        this.email = email;
        this.contrasena = contrasena;
        this.nombreusuario = nombreusuario;
    }

    public Long getIdtipousuario() {
        return idtipousuario;
    }

    public void setIdtipousuario(Long idtipousuario) {
        this.idtipousuario = idtipousuario;
    }

    public Long getIdcolonia() {
        return idcolonia;
    }

    public void setIdcolonia(Long idcolonia) {
        this.idcolonia = idcolonia;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getNombreusuario() {
        return nombreusuario;
    }

    public void setNombreusuario(String nombreusuario) {
        this.nombreusuario = nombreusuario;
    }
}
