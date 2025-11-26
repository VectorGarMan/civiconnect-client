package com.vectorgarman.dto;

public class Municipio {
    private Integer idmunicipio;
    private Integer idestado;
    private String codigo;
    private String nombre;

    public Municipio() {
    }

    public Municipio(Integer idmunicipio, Integer idestado, String codigo, String nombre) {
        this.idmunicipio = idmunicipio;
        this.idestado = idestado;
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public Municipio(Integer idmunicipio, String nombre) {
        this.idmunicipio = idmunicipio;
        this.nombre = nombre;
    }

    public Integer getIdmunicipio() {
        return idmunicipio;
    }

    public void setIdmunicipio(Integer idmunicipio) {
        this.idmunicipio = idmunicipio;
    }

    public Integer getIdestado() {
        return idestado;
    }

    public void setIdestado(Integer idestado) {
        this.idestado = idestado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        // Esto es lo que se mostrará en el ComboBox
        return nombre;
    }
}