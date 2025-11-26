package com.vectorgarman.views;

import com.vectorgarman.api.ClienteAPI;
import com.vectorgarman.dto.ApiResponse;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CambiarContrasena extends JDialog {

    private JTextField txtEmail;
    private JTextField txtToken;
    private JPasswordField txtNuevaPassword;

    private JPanel emailPanel;  // Panel rojo
    private JPanel tokenPanel;  // Panel azul

    private String emailGuardado; // Para mantener el email

    public CambiarContrasena() {
        setTitle("CiviConnect");
        setSize(400, 230);
        setLocationRelativeTo(null);
        setModal(true);
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Cambiar Contraseña", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(titulo, BorderLayout.NORTH);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        // Agregar listener para cerrar el programa al cerrar la ventana
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        inicializarPanelEmail();
        inicializarPanelToken();

        JPanel container = new JPanel(null);
        container.setLayout(new CardLayout());

        container.add(emailPanel, "email");
        container.add(tokenPanel, "token");

        add(container, BorderLayout.CENTER);

        tokenPanel.setVisible(false);

        setVisible(true);
    }

    private void inicializarPanelEmail() {
        emailPanel = new JPanel();
        emailPanel.setLayout(new GridLayout(3, 1, 5, 5));
        emailPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblEmail = new JLabel("Ingresa tu email para cambiar la contraseña:");
        txtEmail = new JTextField();

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnOk = new JButton("OK");
        JButton btnCancelar = new JButton("Cancelar");

        btnPanel.add(btnOk);
        btnPanel.add(btnCancelar);

        btnOk.addActionListener(e -> onEmailOk());
        btnCancelar.addActionListener(e -> {
            dispose();
            Login dialog = new Login();
            dialog.setVisible(true);
        });

        emailPanel.add(lblEmail);
        emailPanel.add(txtEmail);
        emailPanel.add(btnPanel);
    }

    private void inicializarPanelToken() {
        tokenPanel = new JPanel();
        tokenPanel.setLayout(new GridLayout(4, 1, 5, 5));
        tokenPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblToken = new JLabel("Token:");
        txtToken = new JTextField();

        JLabel lblNuevaPassword = new JLabel("Nueva contraseña:");
        txtNuevaPassword = new JPasswordField();

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnOk = new JButton("OK");
        JButton btnCancelar = new JButton("Cancelar");

        btnPanel.add(btnOk);
        btnPanel.add(btnCancelar);

        btnOk.addActionListener(e -> onTokenOk());
        btnCancelar.addActionListener(e -> {
            dispose();
            Login dialog = new Login();
            dialog.setVisible(true);
        });

        tokenPanel.add(lblToken);
        tokenPanel.add(txtToken);
        tokenPanel.add(lblNuevaPassword);
        tokenPanel.add(txtNuevaPassword);
        tokenPanel.add(btnPanel);
    }

    private void onEmailOk() {
        String email = txtEmail.getText().trim();

        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, ingresa tu email",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        new Thread(() -> {
            try {
                ClienteAPI api = new ClienteAPI();
                ApiResponse<?> response = api.olvideContrasena(email);

                SwingUtilities.invokeLater(() -> {
                    String status = response.getStatus();
                    String mensaje = response.getMensaje();
                    String detalles = response.getError() != null ? response.getError() : "";
                    String message = mensaje + (detalles.isEmpty() ? "" : "\n" + detalles);

                    if ("OK".equals(status)) {
                        this.emailGuardado = email;

                        emailPanel.setVisible(false);
                        tokenPanel.setVisible(true);

                        JOptionPane.showMessageDialog(this,
                                message,
                                "Código enviado",
                                JOptionPane.INFORMATION_MESSAGE);

                    } else {
                        JOptionPane.showMessageDialog(this,
                                message,
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                });

            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                        "Error al conectar con el servidor:\n" + ex.getMessage(),
                        "Error de Conexión",
                        JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }


    private void onTokenOk() {
        String token = txtToken.getText().trim();
        String nuevaPass = new String(txtNuevaPassword.getPassword()).trim();

        if (token.isEmpty() || nuevaPass.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debe ingresar el token y la nueva contraseña",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Ejecutar en un hilo separado para no bloquear la UI
        new Thread(() -> {
            try {
                ClienteAPI api = new ClienteAPI();
                ApiResponse<?> response = api.cambiarContrasena(token, nuevaPass, emailGuardado);

                // Volver al hilo de UI para mostrar resultados
                SwingUtilities.invokeLater(() -> {
                    String status = response.getStatus();
                    String mensaje = response.getMensaje();
                    String detalles = response.getError() != null ? response.getError() : "";

                    String message = mensaje + (detalles == null ? "" : "\n" + detalles);
                    if ("OK".equals(status)) {
                        JOptionPane.showMessageDialog(this,
                                message,
                                "Éxito",
                                JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                        Login dialog = new Login();
                        dialog.setVisible(true);
                    } else if ("ERROR".equals(status)) {
                        JOptionPane.showMessageDialog(this,
                                message,
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        dispose();
                        Login dialog = new Login();
                        dialog.setVisible(true);
                    }

                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    dispose();
                    CambiarContrasena dialog = new CambiarContrasena();
                    dialog.setVisible(true);
                    JOptionPane.showMessageDialog(this,
                            "Error al conectar con el servidor:\n" + ex.getMessage(),
                            "Error de Conexión",
                            JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }

    public static void main(String[] args) {
        new CambiarContrasena();
    }
}
