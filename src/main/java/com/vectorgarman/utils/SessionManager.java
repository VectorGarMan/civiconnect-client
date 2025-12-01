package com.vectorgarman.utils;

import com.vectorgarman.dto.Usuario;
import com.vectorgarman.dto.Ubicacion;

public class SessionManager {
    private static SessionManager instance;
    private Usuario usuarioLogueado;
    private Ubicacion ubicacionUsuario;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setUsuarioLogueado(Usuario usuario) {
        this.usuarioLogueado = usuario;
    }

    public Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public void setUbicacionUsuario(Ubicacion ubicacion) {
        this.ubicacionUsuario = ubicacion;
    }

    public Ubicacion getUbicacionUsuario() {
        return ubicacionUsuario;
    }

    public void cerrarSesion() {
        this.usuarioLogueado = null;
        this.ubicacionUsuario = null;
    }

    public boolean isLoggedIn() {
        return usuarioLogueado != null;
    }

    /**
     * Verifica si el usuario logueado es un empleado gubernamental verificado
     * @return true si es empleado gubernamental verificado, false en caso contrario
     */
    public boolean isUsuarioGubernamental() {
        if (usuarioLogueado == null) {
            return false;
        }
        Boolean esGubernamental = usuarioLogueado.getEmpleadogubverificado();
        return esGubernamental != null && esGubernamental;
    }
}