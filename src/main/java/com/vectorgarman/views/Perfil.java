package com.vectorgarman.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.vectorgarman.api.ClienteAPI;
import com.vectorgarman.dto.ApiResponse;
import com.vectorgarman.dto.Colonia;
import com.vectorgarman.dto.Estado;
import com.vectorgarman.dto.Municipio;
import com.vectorgarman.dto.Ubicacion;
import com.vectorgarman.dto.Usuario;
import com.vectorgarman.utils.SessionManager;

public class Perfil extends JFrame {
    private Usuario usuarioLogueado;
    
    private JTextField txtNombreUsuario;
    private JButton btnEditarNombre;
    private JButton btnCancelarNombre;
    private JButton btnGuardarNombre;
    
    private JComboBox<Estado> comboEstado;
    private JComboBox<Municipio> comboMunicipio;
    private JComboBox<Colonia> comboColonia;
    private JButton btnGuardarUbicacion;
    
    private String nombreUsuarioOriginal;
    private Long idColoniaOriginal;

    public Perfil(Usuario usuario) {
        this.usuarioLogueado = usuario;
        this.nombreUsuarioOriginal = usuario.getNombreusuario();
        this.idColoniaOriginal = usuario.getIdcolonia();
        
        setTitle("CiviConnect - Perfil");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}

        inicializarComponentes();

        setSize(800, 810);
        setLocationRelativeTo(null);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }

    /**
     * Inicializa y configura todos los componentes visuales de la ventana de perfil.
     */
    private void inicializarComponentes() {
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBackground(new Color(245, 245, 245));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        try {
            BufferedImage logoImg = ImageIO.read(getClass().getResourceAsStream("/assets/CiviConnectCut.png"));
            Image scaledLogo = logoImg.getScaledInstance(350, 75, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(scaledLogo));
            lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
            lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelPrincipal.add(lblLogo);
            panelPrincipal.add(Box.createRigidArea(new Dimension(0, 15)));
        } catch (IOException e) {
            System.err.println("Error al cargar el logo: " + e.getMessage());
        }

        JPanel panelBotonSalir = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelBotonSalir.setBackground(new Color(245, 245, 245));
        panelBotonSalir.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelBotonSalir.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        JButton btnSalir = new JButton("Regresar");
        btnSalir.setFont(new Font("Arial", Font.BOLD, 14));
        btnSalir.setForeground(new Color(13, 110, 253));
        btnSalir.setBackground(new Color(240, 248, 255));
        btnSalir.setFocusPainted(false);
        btnSalir.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnSalir.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnSalir.setBackground(new Color(13, 110, 253));
                btnSalir.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnSalir.setBackground(new Color(240, 248, 255));
                btnSalir.setForeground(new Color(13, 110, 253));
            }
        });

        btnSalir.addActionListener(e -> dispose());
        panelBotonSalir.add(btnSalir);

        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelTitulo.setBackground(new Color(245, 245, 245));
        panelTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelTitulo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        JLabel lblTitulo = new JLabel("Mi Perfil");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(50, 50, 50));
        panelTitulo.add(lblTitulo);

        JSeparator separador1 = new JSeparator();
        separador1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        separador1.setForeground(new Color(200, 200, 200));

        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setBackground(Color.WHITE);
        panelInfo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        panelInfo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));

        JLabel lblEmail = new JLabel(
                (usuarioLogueado.getEmpleadogubverificado() != null && usuarioLogueado.getEmpleadogubverificado()
                        ? "Empleado Gubernamental Verificado - "
                        : "")
                        + (usuarioLogueado.getEmail() != null
                        ? usuarioLogueado.getEmail()
                        : "")
        );
        lblEmail.setFont(new Font("Arial", Font.PLAIN, 14));
        lblEmail.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblEmail.setForeground(new Color(100, 100, 100));

        JLabel lblNombreLabel = new JLabel("Nombre de Usuario");
        lblNombreLabel.setFont(new Font("Arial", Font.BOLD, 13));
        lblNombreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtNombreUsuario = new JTextField(usuarioLogueado.getNombreusuario());
        txtNombreUsuario.setFont(new Font("Arial", Font.PLAIN, 13));
        txtNombreUsuario.setMaximumSize(new Dimension(300, 35));
        txtNombreUsuario.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtNombreUsuario.setHorizontalAlignment(JTextField.CENTER);
        txtNombreUsuario.setEditable(false);
        txtNombreUsuario.setBackground(new Color(245, 245, 245));
        
        JPanel panelBotonesNombre = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelBotonesNombre.setBackground(Color.WHITE);
        panelBotonesNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        btnEditarNombre = new JButton("Editar");
        btnEditarNombre.setFont(new Font("Arial", Font.PLAIN, 12));
        btnEditarNombre.setFocusPainted(false);
        btnEditarNombre.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        btnCancelarNombre = new JButton("Cancelar");
        btnCancelarNombre.setFont(new Font("Arial", Font.PLAIN, 12));
        btnCancelarNombre.setFocusPainted(false);
        btnCancelarNombre.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCancelarNombre.setVisible(false);
        
        btnGuardarNombre = new JButton("Guardar");
        btnGuardarNombre.setFont(new Font("Arial", Font.BOLD, 12));
        btnGuardarNombre.setBackground(new Color(25, 135, 84));
        btnGuardarNombre.setForeground(Color.WHITE);
        btnGuardarNombre.setFocusPainted(false);
        btnGuardarNombre.setBorderPainted(false);
        btnGuardarNombre.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnGuardarNombre.setVisible(false);
        
        btnEditarNombre.addActionListener(e -> habilitarEdicionNombre());
        btnCancelarNombre.addActionListener(e -> cancelarEdicionNombre());
        btnGuardarNombre.addActionListener(e -> guardarNombreUsuario());
        
        panelBotonesNombre.add(btnEditarNombre);
        panelBotonesNombre.add(btnCancelarNombre);
        panelBotonesNombre.add(btnGuardarNombre);

        panelInfo.add(lblEmail);
        panelInfo.add(Box.createRigidArea(new Dimension(0, 20)));
        panelInfo.add(lblNombreLabel);
        panelInfo.add(Box.createRigidArea(new Dimension(0, 5)));
        panelInfo.add(txtNombreUsuario);
        panelInfo.add(Box.createRigidArea(new Dimension(0, 10)));
        panelInfo.add(panelBotonesNombre);

        JPanel panelUbicacion = new JPanel();
        panelUbicacion.setLayout(new BoxLayout(panelUbicacion, BoxLayout.Y_AXIS));
        panelUbicacion.setBackground(Color.WHITE);
        panelUbicacion.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        panelUbicacion.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        JPanel panelTituloUbicacion = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelTituloUbicacion.setBackground(Color.WHITE);
        panelTituloUbicacion.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelTituloUbicacion.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        
        JLabel lblUbicacionTitulo = new JLabel("Ubicación");
        lblUbicacionTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblUbicacionTitulo.setForeground(new Color(50, 50, 50));
        panelTituloUbicacion.add(lblUbicacionTitulo);

        JPanel panelCombos = new JPanel();
        panelCombos.setLayout(new GridLayout(2, 3, 10, 5));
        panelCombos.setBackground(Color.WHITE);
        panelCombos.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCombos.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        
        JLabel lblEstado = new JLabel("Estado:");
        JLabel lblMunicipio = new JLabel("Municipio:");
        JLabel lblColonia = new JLabel("Colonia:");
        
        comboEstado = new JComboBox<>();
        comboMunicipio = new JComboBox<>();
        comboMunicipio.setEnabled(false);
        comboColonia = new JComboBox<>();
        comboColonia.setEnabled(false);
        
        panelCombos.add(lblEstado);
        panelCombos.add(lblMunicipio);
        panelCombos.add(lblColonia);
        panelCombos.add(comboEstado);
        panelCombos.add(comboMunicipio);
        panelCombos.add(comboColonia);

        JPanel panelBotonUbicacion = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelBotonUbicacion.setBackground(Color.WHITE);
        panelBotonUbicacion.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        btnGuardarUbicacion = new JButton("Guardar");
        btnGuardarUbicacion.setFont(new Font("Arial", Font.BOLD, 12));
        btnGuardarUbicacion.setBackground(new Color(25, 135, 84));
        btnGuardarUbicacion.setForeground(Color.WHITE);
        btnGuardarUbicacion.setFocusPainted(false);
        btnGuardarUbicacion.setBorderPainted(false);
        btnGuardarUbicacion.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        btnGuardarUbicacion.addActionListener(e -> guardarUbicacion());
        panelBotonUbicacion.add(btnGuardarUbicacion);

        panelUbicacion.add(panelTituloUbicacion);
        panelUbicacion.add(Box.createRigidArea(new Dimension(0, 15)));
        panelUbicacion.add(panelCombos);
        panelUbicacion.add(Box.createRigidArea(new Dimension(0, 15)));
        panelUbicacion.add(panelBotonUbicacion);

        JPanel panelBotonLogout = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelBotonLogout.setBackground(new Color(245, 245, 245));
        panelBotonLogout.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelBotonLogout.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        
        JButton btnLogout = new JButton("Cerrar Sesión");
        btnLogout.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogout.setBackground(new Color(240, 248, 255));
        btnLogout.setForeground(new Color(13, 110, 253));
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogout.setPreferredSize(new Dimension(175, 40));

        btnLogout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnLogout.setBackground(new Color(13, 110, 253));
                btnLogout.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnLogout.setBackground(new Color(240, 248, 255));
                btnLogout.setForeground(new Color(13, 110, 253));
            }
        });

        btnLogout.addActionListener(e -> cerrarSesion());
        panelBotonLogout.add(btnLogout);

        panelPrincipal.add(panelBotonSalir);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 10)));
        panelPrincipal.add(panelTitulo);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 10)));
        panelPrincipal.add(separador1);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));
        panelPrincipal.add(panelInfo);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));
        panelPrincipal.add(panelUbicacion);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));
        panelPrincipal.add(panelBotonLogout);

        JScrollPane scrollPane = new JScrollPane(panelPrincipal);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);

        add(scrollPane, BorderLayout.CENTER);

        cargarEstadosYUbicacion();
        
        comboEstado.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED && comboEstado.getSelectedItem() != null) {
                Estado estadoSeleccionado = (Estado) comboEstado.getSelectedItem();
                cargarMunicipios((long) estadoSeleccionado.getIdestado());
            }
        });

        comboMunicipio.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED && comboMunicipio.getSelectedItem() != null) {
                Municipio municipioSeleccionado = (Municipio) comboMunicipio.getSelectedItem();
                cargarColonias((long) municipioSeleccionado.getIdmunicipio());
            }
        });
    }

    /**
     * Habilita la edición del nombre de usuario mostrando los botones de guardar y cancelar.
     */
    private void habilitarEdicionNombre() {
        txtNombreUsuario.setEditable(true);
        txtNombreUsuario.setBackground(Color.WHITE);
        txtNombreUsuario.requestFocus();
        txtNombreUsuario.selectAll();
        
        btnEditarNombre.setVisible(false);
        btnCancelarNombre.setVisible(true);
        btnGuardarNombre.setVisible(true);
    }

    /**
     * Cancela la edición del nombre de usuario restaurando el valor original.
     */
    private void cancelarEdicionNombre() {
        txtNombreUsuario.setText(nombreUsuarioOriginal);
        txtNombreUsuario.setEditable(false);
        txtNombreUsuario.setBackground(new Color(245, 245, 245));
        
        btnEditarNombre.setVisible(true);
        btnCancelarNombre.setVisible(false);
        btnGuardarNombre.setVisible(false);
    }

    /**
     * Guarda el nuevo nombre de usuario enviando la actualización al servidor.
     */
    private void guardarNombreUsuario() {
        String nuevoNombre = txtNombreUsuario.getText().trim();
        
        if (nuevoNombre.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El nombre de usuario no puede estar vacío",
                    "Campo requerido",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (nuevoNombre.equals(nombreUsuarioOriginal)) {
            cancelarEdicionNombre();
            return;
        }

        btnGuardarNombre.setEnabled(false);
        btnGuardarNombre.setText("Guardando...");

        new Thread(() -> {
            try {
                ClienteAPI api = new ClienteAPI();
                
                Long idColoniaActual = idColoniaOriginal;
                if (comboColonia.getSelectedItem() != null) {
                    Colonia coloniaSeleccionada = (Colonia) comboColonia.getSelectedItem();
                    idColoniaActual = (long) coloniaSeleccionada.getIdcolonia();
                }
                
                ApiResponse<?> response = api.actualizarNombreUsuario(
                        usuarioLogueado.getIdusuario(),
                        nuevoNombre,
                        idColoniaActual
                );

                SwingUtilities.invokeLater(() -> {
                    if (response != null && response.isSuccess()) {
                        nombreUsuarioOriginal = nuevoNombre;
                        usuarioLogueado.setNombreusuario(nuevoNombre);
                        
                        JOptionPane.showMessageDialog(this,
                                "Nombre de usuario actualizado exitosamente",
                                "Éxito",
                                JOptionPane.INFORMATION_MESSAGE);
                        
                        cancelarEdicionNombre();
                    } else {
                        btnGuardarNombre.setEnabled(true);
                        btnGuardarNombre.setText("Guardar");
                        JOptionPane.showMessageDialog(this,
                                "Error al actualizar el nombre: " + 
                                (response != null ? response.getMensaje() : "Error desconocido"),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    btnGuardarNombre.setEnabled(true);
                    btnGuardarNombre.setText("Guardar");
                    JOptionPane.showMessageDialog(this,
                            "Error al conectar con el servidor: " + ex.getMessage(),
                            "Error de Conexión",
                            JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }

    /**
     * Guarda la nueva ubicación del usuario actualizando la colonia seleccionada.
     */
    private void guardarUbicacion() {
        if (comboColonia.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar una colonia",
                    "Campo requerido",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Colonia coloniaSeleccionada = (Colonia) comboColonia.getSelectedItem();
        Long nuevaIdColonia = (long) coloniaSeleccionada.getIdcolonia();
        
        if (nuevaIdColonia.equals(idColoniaOriginal)) {
            JOptionPane.showMessageDialog(this,
                    "No hay cambios en la ubicación",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        btnGuardarUbicacion.setEnabled(false);
        btnGuardarUbicacion.setText("Guardando...");

        new Thread(() -> {
            try {
                ClienteAPI api = new ClienteAPI();
                ApiResponse<?> response = api.actualizarNombreUsuario(
                        usuarioLogueado.getIdusuario(),
                        usuarioLogueado.getNombreusuario(),
                        nuevaIdColonia
                );

                SwingUtilities.invokeLater(() -> {
                    if (response != null && response.isSuccess()) {
                        idColoniaOriginal = nuevaIdColonia;
                        usuarioLogueado.setIdcolonia(nuevaIdColonia);
                        
                        actualizarUbicacionEnSession(nuevaIdColonia);
                        
                        JOptionPane.showMessageDialog(this,
                                "Ubicación actualizada exitosamente",
                                "Éxito",
                                JOptionPane.INFORMATION_MESSAGE);
                        
                        btnGuardarUbicacion.setEnabled(true);
                        btnGuardarUbicacion.setText("Guardar");
                    } else {
                        btnGuardarUbicacion.setEnabled(true);
                        btnGuardarUbicacion.setText("Guardar");
                        JOptionPane.showMessageDialog(this,
                                "Error al actualizar la ubicación: " +
                                (response != null ? response.getMensaje() : "Error desconocido"),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    btnGuardarUbicacion.setEnabled(true);
                    btnGuardarUbicacion.setText("Guardar");
                    JOptionPane.showMessageDialog(this,
                            "Error al conectar con el servidor: " + ex.getMessage(),
                            "Error de Conexión",
                            JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }

    /**
     * Actualiza la información de ubicación en la sesión del usuario.
     */
    private void actualizarUbicacionEnSession(Long nuevaIdColonia) {
        new Thread(() -> {
            try {
                ClienteAPI api = new ClienteAPI();
                ApiResponse<?> responseUbicacion = api.obtenerUbicacionPorIdUsuario(usuarioLogueado.getIdusuario());
                
                if ("OK".equals(responseUbicacion.getStatus()) && responseUbicacion.getData() instanceof Map<?, ?> ubicacionMap) {
                    Ubicacion ubicacion = mapearUbicacion(ubicacionMap);
                    SessionManager.getInstance().setUbicacionUsuario(ubicacion);
                }
            } catch (Exception ex) {
                System.err.println("Error al actualizar ubicación en sesión: " + ex.getMessage());
            }
        }).start();
    }

    /**
     * Carga el catálogo de estados y la ubicación actual del usuario.
     */
    private void cargarEstadosYUbicacion() {
        new Thread(() -> {
            try {
                ClienteAPI api = new ClienteAPI();

                ApiResponse<?> responseEstados = api.obtenerEstados();

                Ubicacion ubicacion = null;
                if (usuarioLogueado != null && usuarioLogueado.getIdusuario() != null) {
                    ApiResponse<?> responseUbicacion = api.obtenerUbicacionPorIdUsuario(usuarioLogueado.getIdusuario());
                    if ("OK".equals(responseUbicacion.getStatus()) && responseUbicacion.getData() instanceof Map<?, ?> ubicacionMap) {
                        ubicacion = mapearUbicacion(ubicacionMap);
                    }
                }

                final Ubicacion ubicacionFinal = ubicacion;

                SwingUtilities.invokeLater(() -> {
                    String status = responseEstados.getStatus() != null ? responseEstados.getStatus() : "";
                    Object data = responseEstados.getData();

                    if ("OK".equals(status)) {
                        if (data instanceof List<?> listaEstados) {
                            comboEstado.removeAllItems();

                            for (Object item : listaEstados) {
                                if (item instanceof Map<?, ?> estadoMap) {
                                    Integer id = estadoMap.get("idestado") != null
                                            ? ((Number) estadoMap.get("idestado")).intValue()
                                            : null;
                                    String codigo = estadoMap.get("codigo") != null
                                            ? estadoMap.get("codigo").toString()
                                            : "";
                                    String nombre = estadoMap.get("nombre") != null
                                            ? estadoMap.get("nombre").toString()
                                            : "";

                                    if (id != null && !nombre.isEmpty()) {
                                        Estado estado = new Estado(id, codigo, nombre);
                                        comboEstado.addItem(estado);
                                    }
                                }
                            }

                            if (ubicacionFinal != null) {
                                seleccionarEstadoPorId(ubicacionFinal.getIdEstado());
                            } else if (comboEstado.getItemCount() > 0) {
                                comboEstado.setSelectedIndex(0);
                            }
                        }
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

    /**
     * Selecciona un estado en el combo box por su identificador.
     */
    private void seleccionarEstadoPorId(Long idEstado) {
        for (int i = 0; i < comboEstado.getItemCount(); i++) {
            Estado estado = comboEstado.getItemAt(i);
            if (estado.getIdestado() == idEstado.intValue()) {
                comboEstado.setSelectedIndex(i);
                break;
            }
        }
    }

    /**
     * Selecciona un municipio en el combo box por su identificador.
     */
    private void seleccionarMunicipioPorId(Long idMunicipio) {
        for (int i = 0; i < comboMunicipio.getItemCount(); i++) {
            Municipio municipio = comboMunicipio.getItemAt(i);
            if (municipio.getIdmunicipio() == idMunicipio.intValue()) {
                comboMunicipio.setSelectedIndex(i);
                break;
            }
        }
    }

    /**
     * Selecciona una colonia en el combo box por su identificador.
     */
    private void seleccionarColoniaPorId(Long idColonia) {
        for (int i = 0; i < comboColonia.getItemCount(); i++) {
            Colonia colonia = comboColonia.getItemAt(i);
            if (colonia.getIdcolonia() == idColonia.intValue()) {
                comboColonia.setSelectedIndex(i);
                break;
            }
        }
    }

    /**
     * Carga los municipios correspondientes al estado seleccionado.
     */
    private void cargarMunicipios(Long idestado) {
        new Thread(() -> {
            try {
                ClienteAPI api = new ClienteAPI();
                ApiResponse<?> response = api.obtenerMunicipios(idestado);

                SwingUtilities.invokeLater(() -> {
                    String status = response.getStatus() != null ? response.getStatus() : "";
                    Object data = response.getData();

                    if ("OK".equals(status) && data instanceof List<?> municipios) {
                        comboMunicipio.removeAllItems();
                        comboMunicipio.setEnabled(true);

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

                        Ubicacion ubicacion = SessionManager.getInstance().getUbicacionUsuario();
                        if (ubicacion != null && ubicacion.getIdEstado().equals(idestado)) {
                            seleccionarMunicipioPorId(ubicacion.getIdMunicipio());
                        } else if (comboMunicipio.getItemCount() > 0) {
                            comboMunicipio.setSelectedIndex(0);
                        }
                    }
                });

            } catch (Exception ex) {
                SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(this,
                                "Error al conectar con el servidor:\n" + ex.getMessage(),
                                "Error de Conexión",
                                JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }

    /**
     * Carga las colonias correspondientes al municipio seleccionado.
     */
    private void cargarColonias(Long idmunicipio) {
        new Thread(() -> {
            try {
                ClienteAPI api = new ClienteAPI();
                ApiResponse<?> response = api.obtenerColonia(idmunicipio);

                SwingUtilities.invokeLater(() -> {
                    String status = response.getStatus() != null ? response.getStatus() : "";
                    Object data = response.getData();

                    comboColonia.removeAllItems();
                    comboColonia.setEnabled(false);

                    if ("OK".equals(status) && data instanceof List<?> colonias) {
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

                        Ubicacion ubicacion = SessionManager.getInstance().getUbicacionUsuario();
                        if (ubicacion != null && ubicacion.getIdMunicipio().equals(idmunicipio)) {
                            seleccionarColoniaPorId(ubicacion.getIdColonia());
                            comboColonia.setEnabled(true);
                        } else if (comboColonia.getItemCount() > 0) {
                            comboColonia.setSelectedIndex(0);
                            comboColonia.setEnabled(true);
                        }
                    }
                });

            } catch (Exception ex) {
                SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(this,
                                "Error al conectar con el servidor:\n" + ex.getMessage(),
                                "Error de Conexión",
                                JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }

    /**
     * Convierte los datos de ubicación desde un mapa a un objeto Ubicacion.
     */
    private Ubicacion mapearUbicacion(Map<?, ?> ubicacionMap) {
        try {
            Long idEstado = ubicacionMap.get("idEstado") != null ?
                    ((Number) ubicacionMap.get("idEstado")).longValue() : null;
            Long idMunicipio = ubicacionMap.get("idMunicipio") != null ?
                    ((Number) ubicacionMap.get("idMunicipio")).longValue() : null;
            Long idColonia = ubicacionMap.get("idColonia") != null ?
                    ((Number) ubicacionMap.get("idColonia")).longValue() : null;
            String nombreEstado = ubicacionMap.get("nombreEstado") != null ?
                    ubicacionMap.get("nombreEstado").toString() : "";
            String nombreMunicipio = ubicacionMap.get("nombreMunicipio") != null ?
                    ubicacionMap.get("nombreMunicipio").toString() : "";
            String nombreColonia = ubicacionMap.get("nombreColonia") != null ?
                    ubicacionMap.get("nombreColonia").toString() : "";

            return new Ubicacion(idColonia, nombreColonia, idMunicipio, nombreMunicipio, idEstado, nombreEstado);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Cierra la sesión del usuario actual y regresa a la pantalla de login.
     */
    private void cerrarSesion() {
        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Estás seguro de que deseas cerrar sesión?",
                "Confirmar Cierre de Sesión",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            SessionManager.getInstance().cerrarSesion();

            Window[] windows = Window.getWindows();
            for (Window window : windows) {
                window.dispose();
            }

            SwingUtilities.invokeLater(() -> {
                Login loginDialog = new Login();
                loginDialog.setVisible(true);
            });
        }
    }
}
