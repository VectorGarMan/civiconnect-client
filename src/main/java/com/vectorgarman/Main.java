package com.vectorgarman;

import com.vectorgarman.utils.SessionManager;
import com.vectorgarman.views.Login;
import com.vectorgarman.views.Reportes;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Verificar si ya hay un usuario logueado
            if (SessionManager.getInstance().isLoggedIn()) {
                new Reportes(SessionManager.getInstance().getUsuarioLogueado());
            } else {
                Login login = new Login();
                login.setVisible(true);
            }
        });
    }
}