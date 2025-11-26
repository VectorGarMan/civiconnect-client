package com.vectorgarman.views;

import com.vectorgarman.api.ClienteAPI;
import com.vectorgarman.dto.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Map;

public class Registro extends JDialog {
    private JPanel contentPane;
    private JTextField emailField;
    private JTextField nombreUsuarioField;
    private JPasswordField passwordField;
    private JComboBox<TipoUsuario> comboTipoUsuario;
    private JComboBox<Estado> comboEstado;
    private JComboBox<Municipio> comboMunicipio;
    private JComboBox<Colonia> comboColonia;
    private JButton btnRegistrar;
    private JButton btnIniciarSesion;

    public Registro() {
        setTitle("Registro de Usuario");
        setSize(500, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        cargarTiposDeUsuario();
        cargarEstados();

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

        int row = 0;

        // Título "REGISTRO"
        JLabel lblTitulo = new JLabel("REGISTRO");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 4;
        gbc.insets = new Insets(20, 10, 20, 10);
        contentPane.add(lblTitulo, gbc);

        // COLUMNA IZQUIERDA

        // Tipo de Usuario
        JLabel lblTipoUsuario = new JLabel("Tipo de Usuario:");
        lblTipoUsuario.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 10, 5, 5);
        contentPane.add(lblTipoUsuario, gbc);

        comboTipoUsuario = new JComboBox<>();
        comboTipoUsuario.setFont(new Font("Arial", Font.PLAIN, 14));
        comboTipoUsuario.setPreferredSize(new Dimension(200, 35));
        gbc.gridx = 0;
        gbc.gridy = row + 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 10, 10, 5);
        contentPane.add(comboTipoUsuario, gbc);

        // Email
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = row + 2;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 10, 5, 5);
        contentPane.add(lblEmail, gbc);

        emailField = new JTextField(20);
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        emailField.setPreferredSize(new Dimension(200, 35));
        gbc.gridx = 0;
        gbc.gridy = row + 3;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 10, 10, 5);
        contentPane.add(emailField, gbc);

        // Nombre de Usuario
        JLabel lblNombreUsuario = new JLabel("Nombre de Usuario:");
        lblNombreUsuario.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = row + 4;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 10, 5, 5);
        contentPane.add(lblNombreUsuario, gbc);

        nombreUsuarioField = new JTextField(20);
        nombreUsuarioField.setFont(new Font("Arial", Font.PLAIN, 14));
        nombreUsuarioField.setPreferredSize(new Dimension(200, 35));
        gbc.gridx = 0;
        gbc.gridy = row + 5;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 10, 10, 5);
        contentPane.add(nombreUsuarioField, gbc);

        // Contraseña
        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = row + 6;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 10, 5, 5);
        contentPane.add(lblPassword, gbc);

        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(200, 35));
        gbc.gridx = 0;
        gbc.gridy = row + 7;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 10, 10, 5);
        contentPane.add(passwordField, gbc);

        // COLUMNA DERECHA

        // Estado
        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 10);
        contentPane.add(lblEstado, gbc);

        comboEstado = new JComboBox<>();
        comboEstado.setFont(new Font("Arial", Font.PLAIN, 14));
        comboEstado.setPreferredSize(new Dimension(200, 35));
        gbc.gridx = 1;
        gbc.gridy = row + 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 5, 10, 10);
        contentPane.add(comboEstado, gbc);

        // Municipio
        JLabel lblMunicipio = new JLabel("Municipio:");
        lblMunicipio.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = row + 2;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 10);
        contentPane.add(lblMunicipio, gbc);

        comboMunicipio = new JComboBox<>();
        comboMunicipio.setFont(new Font("Arial", Font.PLAIN, 14));
        comboMunicipio.setPreferredSize(new Dimension(200, 35));
        gbc.gridx = 1;
        gbc.gridy = row + 3;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 5, 10, 10);
        contentPane.add(comboMunicipio, gbc);

        comboEstado.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    Estado estadoSeleccionado = (Estado) comboEstado.getSelectedItem();
                    if (estadoSeleccionado != null) {
                        cargarMunicipios((long) estadoSeleccionado.getIdestado());
                    }
                }
            }
        });

        // Colonia
        JLabel lblColonia = new JLabel("Colonia:");
        lblColonia.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = row + 4;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 10);
        contentPane.add(lblColonia, gbc);

        comboColonia = new JComboBox<>();
        comboColonia.setFont(new Font("Arial", Font.PLAIN, 14));
        comboColonia.setPreferredSize(new Dimension(200, 35));
        gbc.gridx = 1;
        gbc.gridy = row + 5;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 5, 10, 10);
        contentPane.add(comboColonia, gbc);

        row += 8;

        comboMunicipio.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    Municipio municipioSeleccionado = (Municipio) comboMunicipio.getSelectedItem();
                    if (municipioSeleccionado != null) {
                        cargarColonias((long) municipioSeleccionado.getIdmunicipio());
                    }
                }
            }
        });

        // Botón Registrar
        btnRegistrar = new JButton("Registrar Usuario");
        btnRegistrar.setFont(new Font("Arial", Font.BOLD, 14));
        btnRegistrar.setBackground(new Color(60, 179, 113));
        btnRegistrar.setForeground(Color.BLACK);
        btnRegistrar.setPreferredSize(new Dimension(420, 40));
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onRegistrar();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 10, 10, 10);
        contentPane.add(btnRegistrar, gbc);

        // Botón Iniciar Sesión
        btnIniciarSesion = new JButton("Iniciar Sesión");
        btnIniciarSesion.setFont(new Font("Arial", Font.PLAIN, 12));
        btnIniciarSesion.setBackground(new Color(70, 130, 180));
        btnIniciarSesion.setForeground(Color.BLACK);
        btnIniciarSesion.setPreferredSize(new Dimension(420, 35));
        btnIniciarSesion.setFocusPainted(false);
        btnIniciarSesion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onIniciarSesion();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 10, 10, 10);
        contentPane.add(btnIniciarSesion, gbc);

        // Cerrar con ESC
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onRegistrar() {
        // Obtener valores
        String email = emailField.getText().trim();
        String nombreUsuario = nombreUsuarioField.getText().trim();
        String password = new String(passwordField.getPassword());

        // Validación de campos vacíos
        if (email.isEmpty() ||
                nombreUsuario.isEmpty() ||
                password.isEmpty() ||
                comboTipoUsuario.getSelectedIndex() == -1 ||
                comboEstado.getSelectedIndex() == -1 ||
                comboMunicipio.getSelectedIndex() == -1 ||
                comboColonia.getSelectedIndex() == -1) {

            JOptionPane.showMessageDialog(this,
                    "Por favor, completa todos los campos antes de continuar.",
                    "Campos Incompletos",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Deshabilitar botón mientras se procesa
        btnRegistrar.setEnabled(false);
        btnRegistrar.setText("Registrando...");

        // Hilo separado para no bloquear la UI
        new Thread(() -> {
            try {
                ClienteAPI api = new ClienteAPI();

                // Obtener las entidades seleccionadas desde los ComboBox
                TipoUsuario tipoSeleccionado = (TipoUsuario) comboTipoUsuario.getSelectedItem();
                Colonia coloniaSeleccionada = (Colonia) comboColonia.getSelectedItem();

                // Extraer los IDs reales directamente del objeto seleccionado
                Long idTipoUsuario = tipoSeleccionado != null ? tipoSeleccionado.getIdtipousuario().longValue() : null;
                Long idColonia = coloniaSeleccionada != null ? coloniaSeleccionada.getIdcolonia().longValue() : null;


                ApiResponse<?> response = api.crearUsuario(idTipoUsuario, idColonia, email, password, nombreUsuario);

                SwingUtilities.invokeLater(() -> {
                    btnRegistrar.setEnabled(true);
                    btnRegistrar.setText("Registrar");

                    Object statusObj = getFieldValue(response, "status");
                    Object mensajeObj = getFieldValue(response, "mensaje");
                    Object detallesObj = getFieldValue(response, "detalles");

                    String status = statusObj != null ? statusObj.toString() : "";
                    String mensaje = mensajeObj != null ? mensajeObj.toString() : "";
                    String detalles = detallesObj != null ? detallesObj.toString() : "";

                    // TODO: validar los STATUS de acuerdo a los RESPONSE de la API
                    switch (status) {
                        case "OK":
                            JOptionPane.showMessageDialog(this,
                                    mensaje,
                                    "Registro Exitoso",
                                    JOptionPane.INFORMATION_MESSAGE);
                            dispose(); // Cerrar ventana si ya no se usa
                            break;

                        case "WARNING":
                            JOptionPane.showMessageDialog(this,
                                    mensaje + (!detalles.isEmpty() ? "\n" + detalles : ""),
                                    "Advertencia",
                                    JOptionPane.WARNING_MESSAGE);
                            break;

                        default:
                            JOptionPane.showMessageDialog(this,
                                    mensaje + (!detalles.isEmpty() ? "\n" + detalles : ""),
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                    }
                });

            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    btnRegistrar.setEnabled(true);
                    btnRegistrar.setText("Registrar");
                    JOptionPane.showMessageDialog(this,
                            "Error al conectar con el servidor:\n" + ex.getMessage(),
                            "Error de Conexión",
                            JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }


    private void onIniciarSesion() {
        // Cerrar ventana de registro y abrir login
        dispose();
        Login loginDialog = new Login();
        loginDialog.setVisible(true);
    }

    private void cargarTiposDeUsuario() {
        // Ejecutar en un hilo separado para no bloquear la UI
        new Thread(() -> {
            try {
                ClienteAPI api = new ClienteAPI();
                ApiResponse<?> response = api.obtenertiposDeUsuario();

                // Volver al hilo de UI para actualizar el ComboBox
                SwingUtilities.invokeLater(() -> {
                    Object statusObj = getFieldValue(response, "status");
                    String status = statusObj != null ? statusObj.toString() : "";

                    if ("OK".equals(status)) {
                        Object dataObj = getFieldValue(response, "data");

                        if (dataObj instanceof List<?> tiposdeusuario) {
                            comboTipoUsuario.removeAllItems();

                            for (Object item : tiposdeusuario) {
                                if (item instanceof Map<?, ?> tiposdeusuarioMap) {

                                    // Extraer los valores
                                    Object idObj = tiposdeusuarioMap.get("idtipousuario");
                                    Object descripcionObj = tiposdeusuarioMap.get("descripcion");
                                    Object nombreObj = tiposdeusuarioMap.get("nombre");

                                    if (idObj != null && nombreObj != null) {
                                        Integer id = ((Number) idObj).intValue();
                                        String descripcion = descripcionObj.toString();
                                        String nombre = nombreObj.toString();

                                        TipoUsuario tipoUsuario = new TipoUsuario(id, nombre, descripcion);
                                        comboTipoUsuario.addItem(tipoUsuario);
                                    }
                                }
                            }

                            // Seleccionar el primer elemento si hay estados
                            if (comboTipoUsuario.getItemCount() > 0) {
                                comboTipoUsuario.setSelectedIndex(0);
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Error al cargar los tipos de usuarios",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this,
                            "Error al conectar con el servidor:\n" + ex.getMessage(),
                            "Error de Conexión",
                            JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }

    private void cargarEstados() {
        // Ejecutar en un hilo separado para no bloquear la UI
        new Thread(() -> {
            try {
                ClienteAPI api = new ClienteAPI();
                ApiResponse<?> response = api.obtenerEstados();

                // Volver al hilo de UI para actualizar el ComboBox
                SwingUtilities.invokeLater(() -> {
                    Object statusObj = getFieldValue(response, "status");
                    String status = statusObj != null ? statusObj.toString() : "";

                    if ("OK".equals(status)) {
                        Object dataObj = getFieldValue(response, "data");

                        if (dataObj instanceof List<?> estados) {
                            comboEstado.removeAllItems();

                            for (Object item : estados) {
                                if (item instanceof Map<?, ?> estadoMap) {

                                    // Extraer los valores
                                    Object idObj = estadoMap.get("idestado");
                                    Object codigoObj = estadoMap.get("codigo");
                                    Object nombreObj = estadoMap.get("nombre");

                                    if (idObj != null && nombreObj != null) {
                                        Integer id = ((Number) idObj).intValue();
                                        String codigo = codigoObj != null ? codigoObj.toString() : "";
                                        String nombre = nombreObj.toString();

                                        Estado estado = new Estado(id, codigo, nombre);
                                        comboEstado.addItem(estado);
                                    }
                                }
                            }

                            // Seleccionar el primer elemento si hay estados
                            if (comboEstado.getItemCount() > 0) {
                                comboEstado.setSelectedIndex(0);
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Error al cargar los estados",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this,
                            "Error al conectar con el servidor:\n" + ex.getMessage(),
                            "Error de Conexión",
                            JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }

    private void cargarMunicipios(Long idestado) {
        // Ejecutar en un hilo separado para no bloquear la UI
        new Thread(() -> {
            try {
                ClienteAPI api = new ClienteAPI();
                ApiResponse<?> response = api.obtenerMunicipios(idestado);

                SwingUtilities.invokeLater(() -> {
                    Object statusObj = getFieldValue(response, "status");
                    String status = statusObj != null ? statusObj.toString() : "";

                    if ("OK".equals(status)) {
                        Object dataObj = getFieldValue(response, "data");


                        if (dataObj instanceof List<?> municipios) {

                            comboMunicipio.removeAllItems(); // ← CORRECTO: limpiar municipios

                            for (Object item : municipios) {
                                if (item instanceof Map<?, ?> municipioMap) {

                                    Object idObj = municipioMap.get("idmunicipio");
                                    Object nombreObj = municipioMap.get("nombre");

                                    if (idObj != null && nombreObj != null) {
                                        Integer id = ((Number) idObj).intValue();
                                        String nombre = nombreObj.toString();

                                        Municipio municipio = new Municipio(id, nombre);
                                        comboMunicipio.addItem(municipio);
                                    }
                                }
                            }

                            if (comboMunicipio.getItemCount() > 0) {
                                comboMunicipio.setSelectedIndex(0);
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Error al cargar los municipios",
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

    private void cargarColonias(Long idmunicipio) {
        new Thread(() -> {
            try {
                ClienteAPI api = new ClienteAPI();
                ApiResponse<?> response = api.obtenerColonia(idmunicipio);

                SwingUtilities.invokeLater(() -> {
                    Object statusObj = getFieldValue(response, "status");
                    String status = statusObj != null ? statusObj.toString() : "";

                    comboColonia.removeAllItems(); // Limpia colonias

                    if ("OK".equals(status)) {
                        Object dataObj = getFieldValue(response, "data");

                        if (dataObj instanceof List<?> colonias) {

                            for (Object item : colonias) {
                                if (item instanceof Map<?, ?> coloniaMap) {

                                    Object idObj = coloniaMap.get("idcolonia");
                                    Object nombreObj = coloniaMap.get("nombre");

                                    if (idObj != null && nombreObj != null) {
                                        Integer id = ((Number) idObj).intValue();
                                        String nombre = nombreObj.toString();

                                        Colonia colonia = new Colonia(id, nombre);
                                        comboColonia.addItem(colonia);
                                    }
                                }
                            }

                            if (comboColonia.getItemCount() > 0) {
                                comboColonia.setSelectedIndex(0);
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Error al cargar las colonias.",
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

    private Object getFieldValue(Object obj, String fieldName) {
        try {
            java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            return null;
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
                Registro dialog = new Registro();
                dialog.setVisible(true);
            }
        });
    }
}