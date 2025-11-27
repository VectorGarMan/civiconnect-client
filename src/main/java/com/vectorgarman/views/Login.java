package com.vectorgarman.views;

import com.vectorgarman.api.ClienteAPI;
import com.vectorgarman.dto.ApiResponse;
import com.vectorgarman.dto.Usuario;
import com.vectorgarman.utils.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.Map;

public class Login extends JDialog {
    private JPanel contentPane;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton btnLogin;
    private JButton btnRegistrar;
    private JButton btnRecuperar;

    public Login() {
        setTitle("CiviConnect - Login");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setResizable(false);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        // Agregar listener para cerrar el programa al cerrar la ventana
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}

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
        btnRegistrar.setFont(new Font("Arial", Font.PLAIN, 14));
        btnRegistrar.setBackground(new Color(209, 209, 209));
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
        btnRecuperar = new JButton("<html><br><center>Olvidé mi<br>contraseña</center><br></html>");
        btnRecuperar.setFont(new Font("Arial", Font.PLAIN, 14));
        btnRecuperar.setBackground(new Color(209, 209, 209));
        btnRecuperar.setForeground(Color.BLACK);
        btnRecuperar.setPreferredSize(new Dimension(145, 40));
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

        btnLogin.setEnabled(false);
        btnLogin.setText("Iniciando sesión...");

        new Thread(() -> {
            try {
                ClienteAPI api = new ClienteAPI();
                ApiResponse<?> response = api.login(email, password);

                SwingUtilities.invokeLater(() -> {
                    btnLogin.setEnabled(true);
                    btnLogin.setText("Iniciar Sesión");

                    String status = response.getStatus();
                    String mensaje = response.getMensaje();

                    if ("OK".equals(status)) {
                        JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);

                        // Mapear usuario usando el mismo patrón que cargarTiposDeUsuario
                        Object dataObj = response.getData();
                        Usuario usuarioLogueado = mapearUsuarioDesdeMapa(dataObj);

                        if (usuarioLogueado != null) {
                            SessionManager.getInstance().setUsuarioLogueado(usuarioLogueado);
                            this.dispose();
                            new Reportes(usuarioLogueado);
                        } else {
                            JOptionPane.showMessageDialog(this,
                                    "Error al procesar los datos del usuario",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        String detalles = response.getError() != null ? response.getError() : "";
                        String message = mensaje + (detalles.isEmpty() ? "" : "\n" + detalles);

                        int messageType = "WARNING".equals(status) ? JOptionPane.WARNING_MESSAGE : JOptionPane.ERROR_MESSAGE;
                        JOptionPane.showMessageDialog(this, message, "Error", messageType);
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

    private Usuario mapearUsuarioDesdeMapa(Object dataObj) {
        if (dataObj instanceof Map<?, ?> usuarioMap) {
            Object idusuarioObj = usuarioMap.get("idusuario");
            Object idtipousuarioObj = usuarioMap.get("idtipousuario");
            Object idcoloniaObj = usuarioMap.get("idcolonia");
            Object emailObj = usuarioMap.get("email");
            Object nombreusuarioObj = usuarioMap.get("nombreusuario");
            Object fecharegistroObj = usuarioMap.get("fecharegistro");
            Object empleadogubverificadoObj = usuarioMap.get("empleadogubverificado");

            if (idusuarioObj != null && idtipousuarioObj != null) {
                Long idusuario = ((Number) idusuarioObj).longValue();
                Long idtipousuario = ((Number) idtipousuarioObj).longValue();
                Long idcolonia = idcoloniaObj != null ? ((Number) idcoloniaObj).longValue() : null;

                String emailUsuario = emailObj != null ? emailObj.toString() : "";
                String nombreusuario = nombreusuarioObj != null ? nombreusuarioObj.toString() : "";

                // Usar la fecha que viene del servidor directamente
                LocalDate fecharegistro = null;
                if (fecharegistroObj != null) {
                    try {
                        fecharegistro = LocalDate.parse(fecharegistroObj.toString());
                    } catch (Exception e) {
                        System.err.println("Error al parsear fecha: " + e.getMessage());
                        // Si hay error, usar null y que el constructor de Usuario use su valor por defecto
                    }
                }

                Boolean empleadogubverificado = false;
                if (empleadogubverificadoObj != null) {
                    if (empleadogubverificadoObj instanceof Boolean) {
                        empleadogubverificado = (Boolean) empleadogubverificadoObj;
                    } else if (empleadogubverificadoObj instanceof String) {
                        empleadogubverificado = Boolean.parseBoolean(empleadogubverificadoObj.toString());
                    } else if (empleadogubverificadoObj instanceof Number) {
                        empleadogubverificado = ((Number) empleadogubverificadoObj).intValue() == 1;
                    }
                }

                return new Usuario(
                        idusuario,
                        idtipousuario,
                        idcolonia,
                        emailUsuario,
                        "", // No guardar contraseña
                        nombreusuario,
                        fecharegistro, // Usar la fecha del servidor (puede ser null)
                        empleadogubverificado
                );
            }
        }
        return null;
    }

    private void onRegistrar() {
        // Cerrar ventana de login y abrir registro
        dispose();
        Registro registroDialog = new Registro();
        registroDialog.setVisible(true);
    }

    private void onRecuperar() {
        dispose();
        CambiarContrasena dialog = new CambiarContrasena();
        dialog.setVisible(true);
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