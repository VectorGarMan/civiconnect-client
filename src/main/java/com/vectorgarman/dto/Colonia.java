package com.vectorgarman.dto;

public class Colonia {
    private Integer idcolonia;
    private Integer idmunicipio;
    private Integer idestado;
    private String codigo;
    private String nombre;

    public Colonia() {
    }

    public Colonia(Integer idcolonia, Integer idmunicipio, Integer idestado, String codigo, String nombre) {
        this.idcolonia = idcolonia;
        this.idmunicipio = idmunicipio;
        this.idestado = idestado;
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public Colonia(Integer idcolonia, String nombre) {
        this.idcolonia = idcolonia;
        this.nombre = nombre;
    }


    public Integer getIdcolonia() {
        return idcolonia;
    }

    public void setIdcolonia(Integer idcolonia) {
        this.idcolonia = idcolonia;
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