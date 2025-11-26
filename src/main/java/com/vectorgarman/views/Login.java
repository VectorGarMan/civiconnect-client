package com.vectorgarman.views;

import com.vectorgarman.api.ClienteAPI;
import com.vectorgarman.dto.ApiResponse;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Login extends JDialog {
    private JPanel contentPane;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton btnLogin;
    private JButton btnRegistrar;
    private JButton btnRecuperar;

    public Login() {
        setTitle("CiviConnect");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Agregar listener para cerrar el programa al cerrar la ventana
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        inicializarComponentes();

        setModal(true);
    }

    private void inicializarComponentes() {
        contentPane = new JPanel();
        contentPane.setBackground(new Color(245, 245, 245));
        contentPane.setLayout(new GridBagLayout());
        setContentPane(contentPane);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Título "LOGIN"
        JLabel lblTitulo = new JLabel("LOGIN");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 30, 10);
        contentPane.add(lblTitulo, gbc);

        // Etiqueta Email
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 10, 5, 10);
        contentPane.add(lblEmail, gbc);

        // Campo de texto Email
        emailField = new JTextField(25);
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        emailField.setPreferredSize(new Dimension(300, 35));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 10, 15, 10);
        contentPane.add(emailField, gbc);

        // Etiqueta Contraseña
        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 10, 5, 10);
        contentPane.add(lblPassword, gbc);

        // Campo de contraseña
        passwordField = new JPasswordField(25);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(300, 35));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 10, 20, 10);
        contentPane.add(passwordField, gbc);

        // Botón Login
        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setBackground(new Color(70, 130, 180));
        btnLogin.setForeground(Color.BLACK);
        btnLogin.setPreferredSize(new Dimension(300, 40));
        btnLogin.setFocusPainted(false);
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onLogin();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 20, 10);
        contentPane.add(btnLogin, gbc);

        // Panel para los botones inferiores
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(1, 2, 10, 0));
        panelBotones.setOpaque(false);

        // Botón Registrar Usuario
        btnRegistrar = new JButton("Registrar Usuario");
        btnRegistrar.setFont(new Font("Arial", Font.PLAIN, 12));
        btnRegistrar.setBackground(new Color(60, 179, 113));
        btnRegistrar.setForeground(Color.BLACK);
        btnRegistrar.setPreferredSize(new Dimension(145, 35));
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onRegistrar();
            }
        });
        panelBotones.add(btnRegistrar);

        // Botón Recuperar Contraseña
        btnRecuperar = new JButton("<html><center>Recuperar<br>Contraseña</center></html>");
        btnRecuperar.setFont(new Font("Arial", Font.PLAIN, 11));
        btnRecuperar.setBackground(new Color(220, 20, 60));
        btnRecuperar.setForeground(Color.BLACK);
        btnRecuperar.setPreferredSize(new Dimension(145, 35));
        btnRecuperar.setFocusPainted(false);
        btnRecuperar.setMargin(new Insets(2, 2, 2, 2));
        btnRecuperar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onRecuperar();
            }
        });
        panelBotones.add(btnRecuperar);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 10, 10, 10);
        contentPane.add(panelBotones, gbc);

        // Cerrar con ESC
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, completa todos los campos",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Deshabilitar botón mientras se procesa
        btnLogin.setEnabled(false);
        btnLogin.setText("Iniciando sesión...");

        // Ejecutar en un hilo separado para no bloquear la UI
        new Thread(() -> {
            try {
                ClienteAPI api = new ClienteAPI();
                ApiResponse<?> response = api.login(email, password);

                // Volver al hilo de UI para mostrar resultados
                SwingUtilities.invokeLater(() -> {
                    btnLogin.setEnabled(true);
                    btnLogin.setText("Iniciar Sesión");

                    Object statusObj = getFieldValue(response, "status");
                    Object mensajeObj = getFieldValue(response, "mensaje");
                    Object detallesObj = getFieldValue(response, "detalles");

                    String status = statusObj != null ? statusObj.toString() : "";
                    String mensaje = mensajeObj != null ? mensajeObj.toString() : "";
                    String detalles = detallesObj != null ? detallesObj.toString() : "";

                    if ("OK".equals(status)) {
                        JOptionPane.showMessageDialog(this,
                                mensaje,
                                "Éxito",
                                JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                        // Aquí puedes abrir la ventana principal de tu aplicación
                    } else if ("WARNING".equals(status)) {
                        JOptionPane.showMessageDialog(this,
                                mensaje + (detalles.isEmpty() ? "" : "\n" + detalles),
                                "Advertencia",
                                JOptionPane.WARNING_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this,
                                mensaje + (detalles.isEmpty() ? "" : "\n" + detalles),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    btnLogin.setEnabled(true);
                    btnLogin.setText("Iniciar Sesión");
                    JOptionPane.showMessageDialog(this,
                            "Error al conectar con el servidor:\n" + ex.getMessage(),
                            "Error de Conexión",
                            JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }

    private Object getFieldValue(Object obj, String fieldName) {
        try {
            java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            return null;
        }
    }

    private void onRegistrar() {
        // Cerrar ventana de login y abrir registro
        dispose();
        Registro registroDialog = new Registro();
        registroDialog.setVisible(true);
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
            System.out.println(e.getMessage());
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Login dialog = new Login();
                dialog.setVisible(true);
            }
        });
    }
}