package com.vectorgarman.dto;

public class CambioContrasenaRequest {
    String token;
    String nuevaContrasena;
    String email;

    public CambioContrasenaRequest() {
    }

    public CambioContrasenaRequest(String token, String nuevaContrasena, String email) {
        this.token = token;
        this.nuevaContrasena = nuevaContrasena;
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNuevaContrasena() {
        return nuevaContrasena;
    }

    public void setNuevaContrasena(String nuevaContrasena) {
        this.nuevaContrasena = nuevaContrasena;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
