package com.vectorgarman;

import javax.swing.SwingUtilities;

import com.vectorgarman.utils.SessionManager;
import com.vectorgarman.views.Login;
import com.vectorgarman.views.Reportes;

public class Main {
    /**
     * Punto de entrada principal de la aplicación.
     * Verifica si existe una sesión activa y muestra la ventana correspondiente.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            if (SessionManager.getInstance().isLoggedIn()) {
                new Reportes(SessionManager.getInstance().getUsuarioLogueado());
            } else {
                Login login = new Login();
                login.setVisible(true);
            }
        });
    }
}