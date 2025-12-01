package com.vectorgarman.utils;

import com.vectorgarman.dto.Ubicacion;
import com.vectorgarman.dto.Usuario;

public class SessionManager {
    private static SessionManager instance;
    private Usuario usuarioLogueado;
    private Ubicacion ubicacionUsuario;

    private SessionManager() {}

    /**
     * Obtiene la instancia única del gestor de sesión (patrón Singleton).
     */
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    /**
     * Establece el usuario actualmente autenticado en la sesión.
     */
    public void setUsuarioLogueado(Usuario usuario) {
        this.usuarioLogueado = usuario;
    }

    /**
     * Obtiene el usuario actualmente autenticado.
     */
    public Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }

    /**
     * Establece la ubicación del usuario en la sesión.
     */
    public void setUbicacionUsuario(Ubicacion ubicacion) {
        this.ubicacionUsuario = ubicacion;
    }

    /**
     * Obtiene la ubicación del usuario almacenada en la sesión.
     */
    public Ubicacion getUbicacionUsuario() {
        return ubicacionUsuario;
    }

    /**
     * Cierra la sesión actual eliminando los datos del usuario y su ubicación.
     */
    public void cerrarSesion() {
        this.usuarioLogueado = null;
        this.ubicacionUsuario = null;
    }

    /**
     * Verifica si existe un usuario autenticado en la sesión.
     */
    public boolean isLoggedIn() {
        return usuarioLogueado != null;
    }

    /**
     * Verifica si el usuario autenticado es un empleado gubernamental verificado.
     */
    public boolean isUsuarioGubernamental() {
        if (usuarioLogueado == null) {
            return false;
        }
        Boolean esGubernamental = usuarioLogueado.getEmpleadogubverificado();
        return esGubernamental != null && esGubernamental;
    }
}