package com.vectorgarman.views;

import com.vectorgarman.dto.Usuario;
import com.vectorgarman.utils.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Perfil extends JFrame {
    private Usuario usuarioLogueado;

    public Perfil(Usuario usuario) {
        this.usuarioLogueado = usuario;
        setTitle("CiviConnect - Perfil");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}

        inicializarComponentes();

        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Al cerrar la ventana de perfil, volver a la pantalla de reportes
                dispose();
            }
        });
    }

    private void inicializarComponentes() {
        // Panel principal
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBackground(new Color(245, 245, 245));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Botón de regresar en la parte superior
        JButton btnRegresar = new JButton("← Regresar a Reportes");
        btnRegresar.setFont(new Font("Arial", Font.BOLD, 14));
        btnRegresar.setForeground(new Color(13, 110, 253));
        btnRegresar.setBackground(new Color(240, 248, 255));
        btnRegresar.setFocusPainted(false);
        btnRegresar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(13, 110, 253), 1),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        btnRegresar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRegresar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnRegresar.setMaximumSize(new Dimension(200, 40));

        // Efecto hover para botón regresar
        btnRegresar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnRegresar.setBackground(new Color(13, 110, 253));
                btnRegresar.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnRegresar.setBackground(new Color(240, 248, 255));
                btnRegresar.setForeground(new Color(13, 110, 253));
            }
        });

        btnRegresar.addActionListener(e -> {
            dispose(); // Cierra la ventana de perfil y regresa a reportes
        });

        // Título
        JLabel lblTitulo = new JLabel("Mi Perfil");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitulo.setForeground(new Color(50, 50, 50));

        // Separador
        JSeparator separador1 = new JSeparator();
        separador1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        separador1.setForeground(new Color(200, 200, 200));

        // Panel de información del usuario
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setBackground(Color.WHITE);
        panelInfo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        panelInfo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        // Avatar circular
        JPanel panelAvatar = crearAvatar(usuarioLogueado.getNombreusuario(), 80);
        panelAvatar.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Nombre de usuario
        JLabel lblNombre = new JLabel(usuarioLogueado.getNombreusuario() != null 
                ? usuarioLogueado.getNombreusuario() : "Usuario");
        lblNombre.setFont(new Font("Arial", Font.BOLD, 20));
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblNombre.setForeground(new Color(50, 50, 50));

        // Email
        JLabel lblEmail = new JLabel(usuarioLogueado.getEmail() != null 
                ? usuarioLogueado.getEmail() : "");
        lblEmail.setFont(new Font("Arial", Font.PLAIN, 14));
        lblEmail.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblEmail.setForeground(new Color(100, 100, 100));

        // Badge de verificación (si aplica)
        if (usuarioLogueado.getEmpleadogubverificado() != null && usuarioLogueado.getEmpleadogubverificado()) {
            JLabel lblVerificado = new JLabel("<html><font face='Segoe UI Emoji'>✓ </font><font face='Arial'>Empleado Gubernamental Verificado</font></html>");
            lblVerificado.setFont(new Font("Arial", Font.BOLD, 12));
            lblVerificado.setForeground(new Color(25, 135, 84));
            lblVerificado.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelInfo.add(Box.createRigidArea(new Dimension(0, 10)));
            panelInfo.add(lblVerificado);
        }

        // Agregar componentes al panel de información
        panelInfo.add(panelAvatar);
        panelInfo.add(Box.createRigidArea(new Dimension(0, 15)));
        panelInfo.add(lblNombre);
        panelInfo.add(Box.createRigidArea(new Dimension(0, 5)));
        panelInfo.add(lblEmail);

        // Botón de cerrar sesión
        JButton btnLogout = new JButton("🚪 Cerrar Sesión");
        btnLogout.setFont(new Font("Arial", Font.BOLD, 16));
        btnLogout.setBackground(new Color(220, 53, 69));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogout.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogout.setMaximumSize(new Dimension(300, 50));

        // Efecto hover
        btnLogout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnLogout.setBackground(new Color(180, 30, 45));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnLogout.setBackground(new Color(220, 53, 69));
            }
        });

        btnLogout.addActionListener(e -> cerrarSesion());

        // Agregar componentes al panel principal
        panelPrincipal.add(btnRegresar);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));
        panelPrincipal.add(lblTitulo);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 10)));
        panelPrincipal.add(separador1);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 30)));
        panelPrincipal.add(panelInfo);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 30)));
        panelPrincipal.add(btnLogout);

        add(panelPrincipal, BorderLayout.CENTER);
    }

    private JPanel crearAvatar(String nombreUsuario, int tamano) {
        JPanel panelAvatar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Color del círculo
                g2.setColor(new Color(13, 110, 253));
                g2.fillOval(0, 0, tamano, tamano);

                // Inicial del usuario
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, tamano / 2));
                String inicial = nombreUsuario != null && !nombreUsuario.isEmpty()
                        ? String.valueOf(nombreUsuario.charAt(0)).toUpperCase() : "?";
                FontMetrics fm = g2.getFontMetrics();
                int x = (tamano - fm.stringWidth(inicial)) / 2;
                int y = ((tamano - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(inicial, x, y);
            }
        };
        panelAvatar.setPreferredSize(new Dimension(tamano, tamano));
        panelAvatar.setOpaque(false);
        return panelAvatar;
    }

    private void cerrarSesion() {
        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Estás seguro de que deseas cerrar sesión?",
                "Confirmar Cierre de Sesión",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            // Cerrar sesión usando SessionManager
            SessionManager.getInstance().cerrarSesion();

            // Cerrar todas las ventanas abiertas
            Window[] windows = Window.getWindows();
            for (Window window : windows) {
                window.dispose();
            }

            // Abrir ventana de login
            SwingUtilities.invokeLater(() -> {
                Login loginDialog = new Login();
                loginDialog.setVisible(true);
            });
        }
    }
}

// Made with Bob
