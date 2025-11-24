package com.vectorgarman.views;

import com.vectorgarman.api.ClienteAPI;
import com.vectorgarman.dto.ApiResponse;
import com.vectorgarman.dto.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class testForm extends JFrame {
    private JPanel panel1;
    private JButton textField1;
    private JTextField textField2;
    private JTextField textField9;
    private JTextField textField7;
    private JLabel lblTitulo;
    private JLabel lblNombre;
    private JLabel lblEmail;
    private JPasswordField txtPassword;
    private JButton btnEnviar;
    private JButton btnCancelar;

    private ClienteAPI clienteAPI;

    public testForm() {
        // Inicializar ClienteAPI
        clienteAPI = new ClienteAPI();

        // Configuración de la ventana
        setTitle("Formulario de Prueba");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Crear la interfaz
        createUIComponents();

        // Establecer el panel principal
        setContentPane(panel1);

        // Ajustar tamaño y centrar
        pack();
        setSize(400, 500);
        setLocationRelativeTo(null);

        // Event listeners
        btnEnviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onEnviar();
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancelar();
            }
        });

        setVisible(true);
    }

    private void createUIComponents() {
        // Panel principal
        panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(10, 10));
        panel1.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel superior - Título
        JPanel panelTitulo = new JPanel();
        lblTitulo = new JLabel("Formulario de Registro");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        panelTitulo.add(lblTitulo);
        panel1.add(panelTitulo, BorderLayout.NORTH);

        // Panel central - Campos del formulario (4 filas x 1 columna)
        JPanel panelCentral = new JPanel(new GridLayout(8, 1, 5, 5));

        // Campo 1 - Nombre
        lblNombre = new JLabel("Nombre:");
        textField2 = new JTextField(20);
        panelCentral.add(lblNombre);
        panelCentral.add(textField2);

        // Campo 2 - Email
        lblEmail = new JLabel("Email:");
        textField7 = new JTextField(20);
        panelCentral.add(lblEmail);
        panelCentral.add(textField7);

        // Campo 3 - Teléfono
        JLabel lblTelefono = new JLabel("Teléfono:");
        textField9 = new JTextField(20);
        panelCentral.add(lblTelefono);
        panelCentral.add(textField9);

        // Campo 4 - Password
        JLabel lblPassword = new JLabel("Contraseña:");
        txtPassword = new JPasswordField(20);
        panelCentral.add(lblPassword);
        panelCentral.add(txtPassword);

        panel1.add(panelCentral, BorderLayout.CENTER);

        // Panel inferior - Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        btnEnviar = new JButton("Enviar");
        btnCancelar = new JButton("Cancelar");
        textField1 = new JButton("Extra");

        panelBotones.add(textField1);
        panelBotones.add(btnEnviar);
        panelBotones.add(btnCancelar);

        panel1.add(panelBotones, BorderLayout.SOUTH);
    }

    private void onEnviar() {
        // Deshabilitar botón mientras se procesa
        btnEnviar.setEnabled(false);
        btnEnviar.setText("Cargando...");

        // Limpiar campos
        textField2.setText("");
        textField7.setText("");
        textField9.setText("");

        // Ejecutar la llamada a la API en un hilo separado
        SwingWorker<ApiResponse<?>, Void> worker = new SwingWorker<ApiResponse<?>, Void>() {
            @Override
            protected ApiResponse<?> doInBackground() throws Exception {
                // Llamada a la API
                return clienteAPI.getUsuarioPorId(26L);
            }

            @Override
            protected void done() {
                try {
                    // Obtener el resultado
//                    ApiResponse<?> apiResponse =
                    Usuario usuario = (Usuario) get().getData();

                    // Llenar los campos con los datos del usuario
                    textField2.setText(usuario.getNombreusuario());


                } catch (Exception ex) {
                    // Mostrar error en el campo de nombre
                    textField2.setText("Error: " + ex.getMessage());
                    ex.printStackTrace();
                } finally {
                    // Rehabilitar botón
                    btnEnviar.setEnabled(true);
                    btnEnviar.setText("Enviar");
                }
            }
        };

        // Iniciar el worker
        worker.execute();
    }

    private void onCancelar() {
        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de cancelar?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);

        if (respuesta == JOptionPane.YES_OPTION) {
            dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new testForm();
            }
        });
    }
}