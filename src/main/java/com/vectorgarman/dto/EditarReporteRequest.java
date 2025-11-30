package com.vectorgarman.dto;

public class EditarReporteRequest {
    private Long idreporte;
    private Long idusuario;
    private Long idcolonia;
    private Long idnivelprioridad;
    private Long idestadoreporte;
    private Long idcategoria;
    private String titulo;
    private String descripcion;
    private String solucionpropuesta;
    private String calle;
    private String referencia;

    public EditarReporteRequest() {
    }

    public EditarReporteRequest(Long idreporte, Long idusuario, Long idcolonia, Long idnivelprioridad, Long idestadoreporte, Long idcategoria, String titulo, String descripcion, String solucionpropuesta, String calle, String referencia) {
        this.idreporte = idreporte;
        this.idusuario = idusuario;
        this.idcolonia = idcolonia;
        this.idnivelprioridad = idnivelprioridad;
        this.idestadoreporte = idestadoreporte;
        this.idcategoria = idcategoria;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.solucionpropuesta = solucionpropuesta;
        this.calle = calle;
        this.referencia = referencia;
    }

    public Long getIdreporte() {
        return idreporte;
    }

    public void setIdreporte(Long idreporte) {
        this.idreporte = idreporte;
    }

    public Long getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(Long idusuario) {
        this.idusuario = idusuario;
    }

    public Long getIdcolonia() {
        return idcolonia;
    }

    public void setIdcolonia(Long idcolonia) {
        this.idcolonia = idcolonia;
    }

    public Long getIdnivelprioridad() {
        return idnivelprioridad;
    }

    public void setIdnivelprioridad(Long idnivelprioridad) {
        this.idnivelprioridad = idnivelprioridad;
    }

    public Long getIdestadoreporte() {
        return idestadoreporte;
    }

    public void setIdestadoreporte(Long idestadoreporte) {
        this.idestadoreporte = idestadoreporte;
    }

    public Long getIdcategoria() {
        return idcategoria;
    }

    public void setIdcategoria(Long idcategoria) {
        this.idcategoria = idcategoria;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getSolucionpropuesta() {
        return solucionpropuesta;
    }

    public void setSolucionpropuesta(String solucionpropuesta) {
        this.solucionpropuesta = solucionpropuesta;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }
}
