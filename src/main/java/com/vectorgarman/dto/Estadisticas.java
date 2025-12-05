package com.vectorgarman.dto;

public class Estadisticas {
    private String categoria;
    private Long totalreportes;
    private Long pendientes;
    private Long enproceso;
    private Long solucionados;

    public Estadisticas() {
    }

    public Estadisticas(String categoria, Long totalreportes, Long pendientes, Long enproceso, Long solucionados) {
        this.categoria = categoria;
        this.totalreportes = totalreportes;
        this.pendientes = pendientes;
        this.enproceso = enproceso;
        this.solucionados = solucionados;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Long getTotalreportes() {
        return totalreportes;
    }

    public void setTotalreportes(Long totalreportes) {
        this.totalreportes = totalreportes;
    }

    public Long getPendientes() {
        return pendientes;
    }

    public void setPendientes(Long pendientes) {
        this.pendientes = pendientes;
    }

    public Long getEnproceso() {
        return enproceso;
    }

    public void setEnproceso(Long enproceso) {
        this.enproceso = enproceso;
    }

    public Long getSolucionados() {
        return solucionados;
    }

    public void setSolucionados(Long solucionados) {
        this.solucionados = solucionados;
    }
}
