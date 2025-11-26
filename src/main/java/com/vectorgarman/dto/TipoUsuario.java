package com.vectorgarman.dto;

public class TipoUsuario {
    private Integer idtipousuario;
    private String nombre;
    private String descripcion;

    public TipoUsuario() {
    }

    public TipoUsuario(Integer idtipousuario, String nombre, String descripcion) {
        this.idtipousuario = idtipousuario;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Integer getIdtipousuario() {
        return idtipousuario;
    }

    public void setIdtipousuario(Integer idtipousuario) {
        this.idtipousuario = idtipousuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return this.nombre;
    }
}
