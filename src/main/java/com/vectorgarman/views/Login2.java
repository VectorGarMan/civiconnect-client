package com.vectorgarman.views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Login2 extends JDialog {
    private JPanel contentPane;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton btnLogin;
    private JButton btnRegistrar;
    private JButton btnRecuperar;

    public Login2() {
        setTitle("Login");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        inicializarComponentes();

        setModal(true);
    }

    private void inicializarComponentes() {
        contentPane = new JPanel();
        contentPane.setLayout(null);
        contentPane.setBackground(new Color(245, 245, 245));
        setContentPane(contentPane);

        // Título "LOGIN"
        JLabel lblTitulo = new JLabel("LOGIN");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBounds(0, 30, 400, 40);
        contentPane.add(lblTitulo);

        // Etiqueta Email
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Arial", Font.PLAIN, 14));
        lblEmail.setBounds(50, 100, 300, 20);
        contentPane.add(lblEmail);

        // Campo de texto Email
        emailField = new JTextField();
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        emailField.setBounds(50, 125, 300, 35);
        contentPane.add(emailField);

        // Etiqueta Contraseña
        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        lblPassword.setBounds(50, 175, 300, 20);
        contentPane.add(lblPassword);

        // Campo de contraseña
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBounds(50, 200, 300, 35);
        contentPane.add(passwordField);

        // Botón Login
        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setBackground(new Color(70, 130, 180));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBounds(50, 250, 300, 40);
        btnLogin.setFocusPainted(false);
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onLogin();
            }
        });
        contentPane.add(btnLogin);

        // Botón Registrar Usuario
        btnRegistrar = new JButton("Registrar Usuario");
        btnRegistrar.setFont(new Font("Arial", Font.PLAIN, 12));
        btnRegistrar.setBackground(new Color(60, 179, 113));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setBounds(50, 300, 145, 30);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onRegistrar();
            }
        });
        contentPane.add(btnRegistrar);

        // Botón Recuperar Contraseña
        btnRecuperar = new JButton("Recuperar Contraseña");
        btnRecuperar.setFont(new Font("Arial", Font.PLAIN, 12));
        btnRecuperar.setBackground(new Color(220, 20, 60));
        btnRecuperar.setForeground(Color.WHITE);
        btnRecuperar.setBounds(205, 300, 145, 30);
        btnRecuperar.setFocusPainted(false);
        btnRecuperar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onRecuperar();
            }
        });
        contentPane.add(btnRecuperar);

        // Cerrar con ESC
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onLogin() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, completa todos los campos",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Aquí va tu lógica de autenticación
        JOptionPane.showMessageDialog(this,
                "Login exitoso!\nEmail: " + email,
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);

        dispose();
    }

    private void onRegistrar() {
        // Aquí va tu lógica de registro
        JOptionPane.showMessageDialog(this,
                "Redirigiendo a registro de usuario...",
                "Registro",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void onRecuperar() {
        // Aquí va tu lógica de recuperación de contraseña
        String email = JOptionPane.showInputDialog(this,
                "Ingresa tu email para recuperar la contraseña:",
                "Recuperar Contraseña",
                JOptionPane.QUESTION_MESSAGE);

        if (email != null && !email.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Se ha enviado un correo a: " + email,
                    "Recuperación",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Login2 dialog = new Login2();
                dialog.setVisible(true);
            }
        });
    }
}