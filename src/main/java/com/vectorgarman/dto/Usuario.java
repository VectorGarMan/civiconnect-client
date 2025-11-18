package dto;

import java.time.LocalDate;
import java.util.Objects;

public class Usuario {
    private Long idusuario;
    private int idtipousuario;
    private int idcolonia;
    private String email;
    private String contrasena;
    private String nombreusuario;
    private LocalDate fecharegistro;
    private Boolean empleadogubverificado;

    public Usuario(Long idusuario, int idtipousuario, int idcolonia, String email, String contrasena, String nombreusuario, LocalDate fecharegistro, Boolean empleadogubverificado) {
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

    public int getIdtipousuario() {
        return idtipousuario;
    }

    public void setIdtipousuario(int idtipousuario) {
        this.idtipousuario = idtipousuario;
    }

    public int getIdcolonia() {
        return idcolonia;
    }

    public void setIdcolonia(int idcolonia) {
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

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Usuario usuario = (Usuario) object;
        return idtipousuario == usuario.idtipousuario && idcolonia == usuario.idcolonia && Objects.equals(idusuario, usuario.idusuario) && Objects.equals(email, usuario.email) && Objects.equals(contrasena, usuario.contrasena) && Objects.equals(nombreusuario, usuario.nombreusuario) && Objects.equals(fecharegistro, usuario.fecharegistro) && Objects.equals(empleadogubverificado, usuario.empleadogubverificado);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idusuario, idtipousuario, idcolonia, email, contrasena, nombreusuario, fecharegistro, empleadogubverificado);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "idusuario=" + idusuario +
                ", idtipousuario=" + idtipousuario +
                ", idcolonia=" + idcolonia +
                ", email='" + email + '\'' +
                ", contrasena='" + contrasena + '\'' +
                ", nombreusuario='" + nombreusuario + '\'' +
                ", fecharegistro=" + fecharegistro +
                ", empleadogubverificado=" + empleadogubverificado +
                '}';
    }
}