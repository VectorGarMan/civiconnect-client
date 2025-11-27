package com.vectorgarman.dto;

public class Ubicacion {
    private Long IdColonia;
    private String NombreColonia;

    private Long IdMunicipio;
    private String NombreMunicipio;

    private Long IdEstado;
    private String NombreEstado;

    public Ubicacion() {
    }

    public Ubicacion(Long idColonia, String nombreColonia, Long idMunicipio, String nombreMunicipio, Long idEstado, String nombreEstado) {
        IdColonia = idColonia;
        NombreColonia = nombreColonia;
        IdMunicipio = idMunicipio;
        NombreMunicipio = nombreMunicipio;
        IdEstado = idEstado;
        NombreEstado = nombreEstado;
    }

    public Long getIdColonia() {
        return IdColonia;
    }

    public void setIdColonia(Long idColonia) {
        IdColonia = idColonia;
    }

    public String getNombreColonia() {
        return NombreColonia;
    }

    public void setNombreColonia(String nombreColonia) {
        NombreColonia = nombreColonia;
    }

    public Long getIdMunicipio() {
        return IdMunicipio;
    }

    public void setIdMunicipio(Long idMunicipio) {
        IdMunicipio = idMunicipio;
    }

    public String getNombreMunicipio() {
        return NombreMunicipio;
    }

    public void setNombreMunicipio(String nombreMunicipio) {
        NombreMunicipio = nombreMunicipio;
    }

    public Long getIdEstado() {
        return IdEstado;
    }

    public void setIdEstado(Long idEstado) {
        IdEstado = idEstado;
    }

    public String getNombreEstado() {
        return NombreEstado;
    }

    public void setNombreEstado(String nombreEstado) {
        NombreEstado = nombreEstado;
    }

    @Override
    public String toString() {
        return "Ubicacion{" +
                "IdColonia=" + IdColonia +
                ", NombreColonia='" + NombreColonia + '\'' +
                ", IdMunicipio=" + IdMunicipio +
                ", NombreMunicipio='" + NombreMunicipio + '\'' +
                ", IdEstado=" + IdEstado +
                ", NombreEstado='" + NombreEstado + '\'' +
                '}';
    }
}
