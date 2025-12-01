package com.vectorgarman.views;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.vectorgarman.api.ClienteAPI;
import com.vectorgarman.dto.ApiResponse;

public class CambiarContrasena extends JDialog {

    private JTextField txtEmail;
    private JTextField txtToken;
    private JPasswordField txtNuevaPassword;

    private JPanel emailPanel;
    private JPanel tokenPanel;

    private JButton btnOkEmail;
    private JButton btnOkToken;

    private String emailGuardado;

    public CambiarContrasena() {
        setTitle("CiviConnect - Cambiar Contraseña");
        setLayout(new BorderLayout());
        setModal(true);
        setResizable(false);

        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        panelSuperior.setBackground(Color.WHITE);
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        try {
            BufferedImage logoImg = ImageIO.read(getClass().getResourceAsStream("/assets/CiviConnectCut.png"));
            Image scaledLogo = logoImg.getScaledInstance(300, 64, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(scaledLogo));
            lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
            lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelSuperior.add(lblLogo);
            panelSuperior.add(Box.createRigidArea(new Dimension(0, 10)));
        } catch (IOException e) {
            System.err.println("Error al cargar el logo: " + e.getMessage());
        }

        JLabel titulo = new JLabel("Cambiar Contraseña", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelSuperior.add(titulo);

        add(panelSuperior, BorderLayout.NORTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}

        inicializarPanelEmail();
        inicializarPanelToken();

        JPanel container = new JPanel(new CardLayout());
        container.add(emailPanel, "email");
        container.add(tokenPanel, "token");

        add(container, BorderLayout.CENTER);

        tokenPanel.setVisible(false);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Inicializa el panel para solicitar el email del usuario.
     */
    private void inicializarPanelEmail() {
        emailPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        emailPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        JLabel lblEmail = new JLabel("Ingresa tu email para cambiar la contraseña:");
        txtEmail = new JTextField();

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnOkEmail = new JButton("OK");
        JButton btnCancelar = new JButton("Cancelar");

        btnPanel.add(btnOkEmail);
        btnPanel.add(btnCancelar);
        btnOkEmail.setEnabled(true);

        btnOkEmail.addActionListener(e -> {
            btnOkEmail.setEnabled(false);
            onEmailOk();
        });

        btnCancelar.addActionListener(e -> volverALogin());

        emailPanel.add(lblEmail);
        emailPanel.add(txtEmail);
        emailPanel.add(btnPanel);
    }

    /**
     * Inicializa el panel para ingresar el token de verificación y la nueva contraseña.
     */
    private void inicializarPanelToken() {
        tokenPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        tokenPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblEmail = new JLabel("Pega el código de verificación (revisa tu correo):");
        txtToken = new JTextField();

        JLabel lblNuevaPassword = new JLabel("Nueva contraseña:");
        txtNuevaPassword = new JPasswordField();

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnOkToken = new JButton("OK");
        JButton btnCancelar = new JButton("Cancelar");

        btnPanel.add(btnOkToken);
        btnPanel.add(btnCancelar);
        btnOkToken.setEnabled(true);

        btnOkToken.addActionListener(e -> {
            btnOkToken.setEnabled(false);
            onTokenOk();
        });

        btnCancelar.addActionListener(e -> volverALogin());

        tokenPanel.add(lblEmail);
        tokenPanel.add(txtToken);
        tokenPanel.add(lblNuevaPassword);
        tokenPanel.add(txtNuevaPassword);
        tokenPanel.add(btnPanel);
    }

    /**
     * Procesa la solicitud de recuperación de contraseña enviando un token al email.
     */
    private void onEmailOk() {
        String email = txtEmail.getText().trim();

        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, ingresa tu email",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);
            btnOkEmail.setEnabled(true);
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
                        emailGuardado = email;

                        emailPanel.setVisible(false);
                        tokenPanel.setVisible(true);

                        JOptionPane.showMessageDialog(this, message,
                                "Código enviado", JOptionPane.INFORMATION_MESSAGE);

                    } else {
                        JOptionPane.showMessageDialog(this, message,
                                "Error", JOptionPane.ERROR_MESSAGE);
                        btnOkEmail.setEnabled(true);
                    }
                });

            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this,
                            "Error al conectar con el servidor:\n" + ex.getMessage(),
                            "Error de Conexión", JOptionPane.ERROR_MESSAGE);
                    btnOkEmail.setEnabled(true);
                });
            }
        }).start();
    }

    /**
     * Procesa el cambio de contraseña utilizando el token de verificación.
     */
    private void onTokenOk() {
        String token = txtToken.getText().trim();
        String nuevaPass = new String(txtNuevaPassword.getPassword()).trim();

        if (token.isEmpty() || nuevaPass.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debe ingresar el token y la nueva contraseña",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            btnOkToken.setEnabled(true);
            return;
        }

        new Thread(() -> {
            try {
                ClienteAPI api = new ClienteAPI();
                ApiResponse<?> response = api.cambiarContrasena(token, nuevaPass, emailGuardado);

                SwingUtilities.invokeLater(() -> {
                    String status = response.getStatus();
                    String mensaje = response.getMensaje();
                    String detalles = response.getError() != null ? response.getError() : "";
                    String message = mensaje + (detalles == null ? "" : "\n" + detalles);

                    if ("OK".equals(status)) {
                        JOptionPane.showMessageDialog(this, message,
                                "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        volverALogin();
                    } else {
                        JOptionPane.showMessageDialog(this, message,
                                "Error", JOptionPane.ERROR_MESSAGE);
                        btnOkToken.setEnabled(true);
                    }
                });

            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this,
                            "Error al conectar con el servidor:\n" + ex.getMessage(),
                            "Error de Conexión", JOptionPane.ERROR_MESSAGE);
                    btnOkToken.setEnabled(true);
                });
            }
        }).start();
    }

    /**
     * Cierra la ventana actual y abre la ventana de login.
     */
    private void volverALogin() {
        dispose();
        new Login().setVisible(true);
    }

    /**
     * Método principal para ejecutar la ventana de cambio de contraseña de forma independiente.
     */
    public static void main(String[] args) {
        new CambiarContrasena();
    }
}
