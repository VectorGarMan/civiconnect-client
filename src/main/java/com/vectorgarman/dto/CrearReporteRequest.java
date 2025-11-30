package com.vectorgarman.dto;

public class CrearReporteRequest {
    private Long idusuario;
    private Long idcolonia;
    private Long idnivelprioridad;
    private Long idestadoreporte;
    private Long idcategoria;
    private Long titulo;
    private Long descripcion;
    private Long solucionpropuesta;
    private Long calle;
    private Long referencia;

    public CrearReporteRequest() {
    }

    public CrearReporteRequest(Long idusuario, Long idcolonia, Long idnivelprioridad, Long idestadoreporte, Long idcategoria, Long titulo, Long descripcion, Long solucionpropuesta, Long calle, Long referencia) {
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

    public Long getTitulo() {
        return titulo;
    }

    public void setTitulo(Long titulo) {
        this.titulo = titulo;
    }

    public Long getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(Long descripcion) {
        this.descripcion = descripcion;
    }

    public Long getSolucionpropuesta() {
        return solucionpropuesta;
    }

    public void setSolucionpropuesta(Long solucionpropuesta) {
        this.solucionpropuesta = solucionpropuesta;
    }

    public Long getCalle() {
        return calle;
    }

    public void setCalle(Long calle) {
        this.calle = calle;
    }

    public Long getReferencia() {
        return referencia;
    }

    public void setReferencia(Long referencia) {
        this.referencia = referencia;
    }
}
