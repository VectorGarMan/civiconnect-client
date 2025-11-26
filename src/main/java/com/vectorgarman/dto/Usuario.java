package com.vectorgarman.dto;

import java.time.LocalDate;

public class Usuario {

    private Long idusuario;

    private Long idtipousuario;

    private Long idcolonia;

    private String email;

    private String contrasena;

    private String nombreusuario;

    private LocalDate fecharegistro = LocalDate.now();

    private Boolean empleadogubverificado;

    public Usuario() {
    }

    public Usuario(Long idusuario, Long idtipousuario, Long idcolonia, String email, String contrasena, String nombreusuario, LocalDate fecharegistro, Boolean empleadogubverificado) {
        this.idusuario = idusuario;
        this.idtipousuario = idtipousuario;
        this.idcolonia = idcolonia;
        this.email = email;
        this.contrasena = contrasena;
        this.nombreusuario = nombreusuario;
        this.fecharegistro = fecharegistro;
        this.empleadogubverificado = empleadogubverificado;
    }

    public Long getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(Long idusuario) {
        this.idusuario = idusuario;
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

    public LocalDate getFecharegistro() {
        return fecharegistro;
    }

    public void setFecharegistro(LocalDate fecharegistro) {
        this.fecharegistro = fecharegistro;
    }

    public Boolean getEmpleadogubverificado() {
        return empleadogubverificado;
    }

    public void setEmpleadogubverificado(Boolean empleadogubverificado) {
        this.empleadogubverificado = empleadogubverificado;
    }
}
