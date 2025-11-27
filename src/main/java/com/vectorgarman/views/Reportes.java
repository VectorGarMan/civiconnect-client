package com.vectorgarman.views;

import com.vectorgarman.api.ClienteAPI;
import com.vectorgarman.dto.*;
import com.vectorgarman.utils.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

public class Reportes extends JFrame {
    private Usuario usuarioLogueado;

    private JComboBox<Estado> comboEstado;
    private JComboBox<Municipio> comboMunicipio;
    private JComboBox<Colonia> comboColonia;

    private JButton btnCrearReporte;
    private JButton btnPerfil;

    private JPanel panelListaReportes;

    public Reportes(Usuario usuario) {
        this.usuarioLogueado = usuario;
        setTitle("CiviConnect - Reportes");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}

        inicializarHeader();
        inicializarListaReportes();
        cargarEstadosYUbicacion();
        cargarReportes();

        setSize(1000, 600);

        setLocationRelativeTo(null);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    /** HEADER **/
    private void inicializarHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel filtrosPanel = new JPanel(new GridLayout(2, 3, 10, 5));

        JLabel lblEstado = new JLabel("Estado:");
        JLabel lblMunicipio = new JLabel("Municipio:");
        JLabel lblColonia = new JLabel("Colonia:");

        comboEstado = new JComboBox<>();
        comboMunicipio = new JComboBox<>();
        comboColonia = new JComboBox<>();

        comboMunicipio.setEnabled(false);
        comboColonia.setEnabled(false);

        filtrosPanel.add(lblEstado);
        filtrosPanel.add(lblMunicipio);
        filtrosPanel.add(lblColonia);
        filtrosPanel.add(comboEstado);
        filtrosPanel.add(comboMunicipio);
        filtrosPanel.add(comboColonia);

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

        header.add(filtrosPanel, BorderLayout.WEST);

        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnCrearReporte = new JButton("+");
        btnCrearReporte.setPreferredSize(new Dimension(45, 35));

        btnPerfil = new JButton("Mi perfil");
        btnPerfil.setPreferredSize(new Dimension(70, 35));

        botonesPanel.add(btnCrearReporte);
        botonesPanel.add(btnPerfil);

        header.add(botonesPanel, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);
    }

    /** LISTA DE REPORTES **/
    private void inicializarListaReportes() {
        panelListaReportes = new JPanel();
        panelListaReportes.setLayout(new BoxLayout(panelListaReportes, BoxLayout.Y_AXIS));
        panelListaReportes.setBackground(new Color(245, 245, 245));
        panelListaReportes.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scroll = new JScrollPane(panelListaReportes);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        add(scroll, BorderLayout.CENTER);
    }

//     TODO: DESCUBRIR POR QUÉ NO SE ESTÁN SELECCIONANDO LOS COMBOBOX CON LOS DATOS DEL USUARIO DE LA SESION ACTIVA.
    private void cargarEstadosYUbicacion() {
        new Thread(() -> {
            try {
                ClienteAPI api = new ClienteAPI();

                // Cargar estados primero
                ApiResponse<?> responseEstados = api.obtenerEstados();

                // Cargar ubicación del usuario
                Ubicacion ubicacion = null;
                if (usuarioLogueado != null && usuarioLogueado.getIdusuario() != null) {
                    ApiResponse<?> responseUbicacion = api.obtenerUbicacionPorIdUsuario(usuarioLogueado.getIdusuario());
                    System.out.println("Respuesta ubicación: " + responseUbicacion.toString());
                    if ("OK".equals(responseUbicacion.getStatus()) && responseUbicacion.getData() instanceof Map<?, ?> ubicacionMap) {
                        ubicacion = mapearUbicacion(ubicacionMap);
                        System.out.println(ubicacion);

                        // Guardar en SessionManager
                        SessionManager.getInstance().setUbicacionUsuario(ubicacion);
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

                            // Seleccionar el estado del usuario si está disponible
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
                    // Si hay error, cargar estados normalmente
                    cargarEstados();
                });
            }
        }).start();
    }

//    private void cargarEstadosYUbicacion() {
//        new Thread(() -> {
//            try {
//                ClienteAPI api = new ClienteAPI();
//
//                // Cargar estados primero
//                System.out.println("Cargando estados...");
//                ApiResponse<?> responseEstados = api.obtenerEstados();
//                System.out.println("Estados cargados: " + responseEstados.getStatus());
//
//                // Cargar ubicación del usuario
//                Ubicacion ubicacion = null;
//                if (usuarioLogueado != null && usuarioLogueado.getIdusuario() != null) {
//                    System.out.println("Cargando ubicación para usuario: " + usuarioLogueado.getIdusuario());
//                    ApiResponse<?> responseUbicacion = api.obtenerUbicacionPorIdUsuario(usuarioLogueado.getIdusuario());
//                    System.out.println("Respuesta ubicación: " + responseUbicacion.toString());
//
//                    if ("OK".equals(responseUbicacion.getStatus()) && responseUbicacion.getData() instanceof Map<?, ?> ubicacionMap) {
//                        ubicacion = mapearUbicacion(ubicacionMap);
//                        System.out.println("Ubicación mapeada: " + ubicacion);
//                        System.out.println("ID Estado: " + ubicacion.getIdEstado());
//                        System.out.println("ID Municipio: " + ubicacion.getIdMunicipio());
//                        System.out.println("ID Colonia: " + ubicacion.getIdColonia());
//
//                        // Guardar en SessionManager
//                        SessionManager.getInstance().setUbicacionUsuario(ubicacion);
//                        System.out.println("Ubicación guardada en SessionManager");
//                    } else {
//                        System.out.println("Error en respuesta de ubicación o datos vacíos");
//                    }
//                } else {
//                    System.out.println("Usuario logueado es null o no tiene ID");
//                }
//
//                final Ubicacion ubicacionFinal = ubicacion;
//                System.out.println("Ubicación final a usar: " + ubicacionFinal);
//
//                SwingUtilities.invokeLater(() -> {
//                    String status = responseEstados.getStatus() != null ? responseEstados.getStatus() : "";
//                    Object data = responseEstados.getData();
//
//                    if ("OK".equals(status)) {
//                        if (data instanceof List<?> listaEstados) {
//                            comboEstado.removeAllItems();
//                            System.out.println("Cargando " + listaEstados.size() + " estados en combo");
//
//                            for (Object item : listaEstados) {
//                                if (item instanceof Map<?, ?> estadoMap) {
//                                    Integer id = estadoMap.get("idestado") != null
//                                            ? ((Number) estadoMap.get("idestado")).intValue()
//                                            : null;
//                                    String codigo = estadoMap.get("codigo") != null
//                                            ? estadoMap.get("codigo").toString()
//                                            : "";
//                                    String nombre = estadoMap.get("nombre") != null
//                                            ? estadoMap.get("nombre").toString()
//                                            : "";
//
//                                    if (id != null && !nombre.isEmpty()) {
//                                        Estado estado = new Estado(id, codigo, nombre);
//                                        comboEstado.addItem(estado);
//                                    }
//                                }
//                            }
//
//                            System.out.println("Estados cargados en combo: " + comboEstado.getItemCount());
//
//                            // Seleccionar el estado del usuario si está disponible
//                            if (ubicacionFinal != null) {
//                                System.out.println("Intentando seleccionar estado ID: " + ubicacionFinal.getIdEstado());
//                                seleccionarEstadoPorId(ubicacionFinal.getIdEstado());
//                            } else {
//                                System.out.println("Ubicación final es null, seleccionando primer estado");
//                                if (comboEstado.getItemCount() > 0) {
//                                    comboEstado.setSelectedIndex(0);
//                                }
//                            }
//                        }
//                    } else {
//                        System.out.println("Error cargando estados: " + status);
//                    }
//                });
//
//            } catch (Exception ex) {
//                System.out.println("Excepción en cargarEstadosYUbicacion: " + ex.getMessage());
//                ex.printStackTrace();
//                SwingUtilities.invokeLater(() -> {
//                    JOptionPane.showMessageDialog(this,
//                            "Error al conectar con el servidor:\n" + ex.getMessage(),
//                            "Error de Conexión",
//                            JOptionPane.ERROR_MESSAGE);
//                    // Si hay error, cargar estados normalmente
//                    cargarEstados();
//                });
//            }
//        }).start();
//    }

    private void seleccionarEstadoPorId(Long idEstado) {
        for (int i = 0; i < comboEstado.getItemCount(); i++) {
            Estado estado = comboEstado.getItemAt(i);
            if (estado.getIdestado() == idEstado.intValue()) {
                comboEstado.setSelectedIndex(i);
                break;
            }
        }
    }

//    private void seleccionarEstadoPorId(Long idEstado) {
//        System.out.println("Buscando estado con ID: " + idEstado);
//        for (int i = 0; i < comboEstado.getItemCount(); i++) {
//            Estado estado = comboEstado.getItemAt(i);
//            System.out.println("Estado en combo [" + i + "]: ID=" + estado.getIdestado() + ", Nombre=" + estado.getNombre());
//            if (estado.getIdestado() == idEstado.intValue()) {
//                System.out.println("¡Estado encontrado! Seleccionando índice: " + i);
//                comboEstado.setSelectedIndex(i);
//                return;
//            }
//        }
//        System.out.println("Estado con ID " + idEstado + " no encontrado en el combo");
//    }

    private void seleccionarMunicipioPorId(Long idMunicipio) {
        for (int i = 0; i < comboMunicipio.getItemCount(); i++) {
            Municipio municipio = comboMunicipio.getItemAt(i);
            if (municipio.getIdmunicipio() == idMunicipio.intValue()) {
                comboMunicipio.setSelectedIndex(i);
                break;
            }
        }
    }

    private void seleccionarColoniaPorId(Long idColonia) {
        for (int i = 0; i < comboColonia.getItemCount(); i++) {
            Colonia colonia = comboColonia.getItemAt(i);
            if (colonia.getIdcolonia() == idColonia.intValue()) {
                comboColonia.setSelectedIndex(i);
                break;
            }
        }
    }

    private void cargarEstados() {
        new Thread(() -> {
            try {
                ClienteAPI api = new ClienteAPI();
                ApiResponse<?> response = api.obtenerEstados();

                SwingUtilities.invokeLater(() -> {
                    String status = response.getStatus() != null ? response.getStatus() : "";
                    Object data = response.getData();

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

                            if (comboEstado.getItemCount() > 0) {
                                comboEstado.setSelectedIndex(0);
                            }

                        } else {
                            JOptionPane.showMessageDialog(this,
                                    "El servidor devolvió un formato inesperado",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }

                    } else {
                        JOptionPane.showMessageDialog(this,
                                response.getMensaje(),
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

                        // Seleccionar municipio del usuario si está disponible
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

                        // Seleccionar colonia del usuario si está disponible
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

    private void cargarReportes() {
        new Thread(() -> {
            try {
                ClienteAPI api = new ClienteAPI();
                ApiResponse<?> response = api.obtenerTodosLosReportes();

                SwingUtilities.invokeLater(() -> {
                    String status = response.getStatus() != null ? response.getStatus() : "";

                    if ("OK".equals(status)) {
                        Object dataObj = response.getData();

                        if (dataObj instanceof List<?> reportes) {
                            panelListaReportes.removeAll();

                            for (Object item : reportes) {
                                if (item instanceof Map<?, ?> reporteMap) {
                                    JPanel tarjeta = crearTarjetaReporte(reporteMap);
                                    panelListaReportes.add(tarjeta);
                                    panelListaReportes.add(Box.createRigidArea(new Dimension(0, 15)));
                                }
                            }

                            panelListaReportes.revalidate();
                            panelListaReportes.repaint();
                        }
                    } else {
                        JOptionPane.showMessageDialog(this,
                                response.getMensaje(),
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

    private JPanel crearTarjetaReporte(Map<?, ?> reporteMap) {
        // Extraer datos del mapa del reporteView
        Map<?, ?> reporteView = reporteMap.get("reporteView") instanceof Map ? (Map<?, ?>) reporteMap.get("reporteView") : null;

        if (reporteView == null) return new JPanel();

        String creador = reporteView.get("creador") != null ? reporteView.get("creador").toString() : "Anónimo";
        String fechaCreacion = reporteView.get("fechacreacion") != null ? reporteView.get("fechacreacion").toString() : "";
        String estadoReporte = reporteView.get("estadoreporte") != null ? reporteView.get("estadoreporte").toString() : "Pendiente";
        String colorEstado = reporteView.get("colorestado") != null ? reporteView.get("colorestado").toString() : "#FFA500";
        String prioridad = reporteView.get("prioridad") != null ? reporteView.get("prioridad").toString() : "Media";
        String titulo = reporteView.get("titulo") != null ? reporteView.get("titulo").toString() : "Sin título";
        String descripcion = reporteView.get("descripcion") != null ? reporteView.get("descripcion").toString() : "";
        Long totalVotos = reporteView.get("totalvotos") != null ? ((Number) reporteView.get("totalvotos")).longValue() : 0;
        Long totalComentarios = reporteView.get("totalcomentarios") != null ? ((Number) reporteView.get("totalcomentarios")).longValue() : 0;

        // Panel principal de la tarjeta
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BorderLayout());
        tarjeta.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        tarjeta.setMaximumSize(new Dimension(900, 350));
        tarjeta.setBackground(Color.WHITE);

        // Barra de color según estado
        JPanel barraColor = new JPanel();
        barraColor.setPreferredSize(new Dimension(900, 30));
        try {
            barraColor.setBackground(Color.decode(colorEstado));
        } catch (Exception e) {
            barraColor.setBackground(new Color(255, 165, 0)); // Naranja por defecto
        }
        tarjeta.add(barraColor, BorderLayout.NORTH);

        // Contenido del reporte
        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        contenido.setBackground(Color.WHITE);

        // Línea 1: Creador y fecha
        JLabel lblCreadorFecha = new JLabel("@ " + creador + " • " + fechaCreacion);
        lblCreadorFecha.setFont(new Font("Arial", Font.BOLD, 13));
        lblCreadorFecha.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Línea 2: Estado y prioridad
        JLabel lblEstadoPrioridad = new JLabel(
                "<html>Estado del reporte: <span style='color:" + colorEstado + ";'><b>" + estadoReporte + "</b></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Prioridad: " + prioridad + "</html>"
        );
        lblEstadoPrioridad.setFont(new Font("Arial", Font.PLAIN, 12));
        lblEstadoPrioridad.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Título del reporte
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Descripción
        JTextArea txtDescripcion = new JTextArea(descripcion);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setEditable(false);
        txtDescripcion.setFont(new Font("Arial", Font.PLAIN, 12));
        txtDescripcion.setBackground(Color.WHITE);
        txtDescripcion.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)  // top, left, bottom, right
        ));
        txtDescripcion.setMaximumSize(new Dimension(850, 80));
        txtDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Evidencia
        JLabel lblEvidencia = new JLabel("Evidencia:");
        lblEvidencia.setFont(new Font("Arial", Font.BOLD, 13));
        lblEvidencia.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel panelEvidencias = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelEvidencias.setBackground(Color.WHITE);
        panelEvidencias.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Cargar evidencias del array
        Object evidenciasObj = reporteMap.get("evidencias");
        if (evidenciasObj instanceof List<?> evidencias && !evidencias.isEmpty()) {
            int count = Math.min(evidencias.size(), 3);
            for (int i = 0; i < count; i++) {
                if (evidencias.get(i) instanceof Map<?, ?> evidenciaMap) {
                    try {
                        // Obtener el string base64 de la evidencia
                        Object archivoObj = evidenciaMap.get("archivo");

                        if (archivoObj != null) {
                            String base64String = archivoObj.toString();

                            // Decodificar base64 a bytes
                            byte[] bytesImagen = java.util.Base64.getDecoder().decode(base64String);

                            // Convertir bytes a imagen
                            java.io.InputStream in = new java.io.ByteArrayInputStream(bytesImagen);
                            java.awt.image.BufferedImage image = javax.imageio.ImageIO.read(in);

                            if (image != null) {
                                // Escalar la imagen para que quepa en el espacio de 150x100
                                java.awt.Image scaledImage = image.getScaledInstance(150, 100, java.awt.Image.SCALE_SMOOTH);
                                ImageIcon icon = new ImageIcon(scaledImage);

                                JLabel lblImagen = new JLabel(icon);
                                lblImagen.setPreferredSize(new Dimension(150, 100));
                                lblImagen.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                                panelEvidencias.add(lblImagen);

                                // En la parte donde creas las etiquetas de imagen, después de crear lblImagen:
                                lblImagen.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                                lblImagen.addMouseListener(new MouseAdapter() {
                                    @Override
                                    public void mouseClicked(MouseEvent e) {
                                        mostrarImagenGrande(image, lblImagen);
                                    }
                                });
                            } else {
                                // Si no se pudo leer la imagen, mostrar placeholder
                                JLabel imgPlaceholder = new JLabel("Error img");
                                imgPlaceholder.setPreferredSize(new Dimension(150, 100));
                                imgPlaceholder.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                                imgPlaceholder.setHorizontalAlignment(SwingConstants.CENTER);
                                panelEvidencias.add(imgPlaceholder);
                            }
                        } else {
                            // Si no hay string base64, mostrar placeholder
                            JLabel imgPlaceholder = new JLabel("Sin imagen");
                            imgPlaceholder.setPreferredSize(new Dimension(150, 100));
                            imgPlaceholder.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                            imgPlaceholder.setHorizontalAlignment(SwingConstants.CENTER);
                            panelEvidencias.add(imgPlaceholder);
                        }
                    } catch (Exception ex) {
                        // Si hay error al procesar la imagen, mostrar placeholder con error
                        JLabel imgPlaceholder = new JLabel("Error");
                        imgPlaceholder.setPreferredSize(new Dimension(150, 100));
                        imgPlaceholder.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
                        imgPlaceholder.setHorizontalAlignment(SwingConstants.CENTER);
                        panelEvidencias.add(imgPlaceholder);
                        System.out.println(ex.getMessage()); // Para debug
                    }
                }
            }
        } else {
            // Si no hay evidencias, mostrar mensaje
            JLabel sinEvidencias = new JLabel("Sin evidencias");
            sinEvidencias.setPreferredSize(new Dimension(150, 100));
            sinEvidencias.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            sinEvidencias.setHorizontalAlignment(SwingConstants.CENTER);
            panelEvidencias.add(sinEvidencias);
        }

        // Votos y comentarios
        JLabel lblVotosComentarios = new JLabel("Total votos: " + totalVotos + "     Total comentarios: " + totalComentarios);
        lblVotosComentarios.setFont(new Font("Arial", Font.PLAIN, 12));
        lblVotosComentarios.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Agregar componentes al contenido
        contenido.add(lblCreadorFecha);
        contenido.add(Box.createRigidArea(new Dimension(0, 5)));
        contenido.add(lblEstadoPrioridad);
        contenido.add(Box.createRigidArea(new Dimension(0, 10)));
        contenido.add(lblTitulo);
        contenido.add(Box.createRigidArea(new Dimension(0, 5)));
        contenido.add(txtDescripcion);
        contenido.add(Box.createRigidArea(new Dimension(0, 10)));
        contenido.add(lblEvidencia);
        contenido.add(panelEvidencias);
        contenido.add(Box.createRigidArea(new Dimension(0, 10)));
        contenido.add(lblVotosComentarios);

        tarjeta.add(contenido, BorderLayout.CENTER);

        return tarjeta;
    }

    private void mostrarImagenGrande(BufferedImage imagenOriginal, JLabel labelOrigen) {
        // Crear un JDialog modal
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(labelOrigen),
                "Vista Previa", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Crear el panel principal
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.BLACK);

        // Crear el label con la imagen original (sin escalar)
        JLabel lblImagenGrande = new JLabel(new ImageIcon(imagenOriginal));
        lblImagenGrande.setHorizontalAlignment(SwingConstants.CENTER);

        // Agregar scroll por si la imagen es muy grande
        JScrollPane scrollPane = new JScrollPane(lblImagenGrande);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);

        // Panel de controles
        JPanel panelControles = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelControles.setBackground(Color.DARK_GRAY);
        panelControles.setPreferredSize(new Dimension(0, 50)); // Altura fija para controles

        JButton btnCerrar = new JButton("Cerrar (ESC)");
        btnCerrar.addActionListener(e -> dialog.dispose());

        panelControles.add(btnCerrar);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(panelControles, BorderLayout.SOUTH);

        // Configurar el diálogo
        dialog.setContentPane(panel);

        // Usar pack() para auto-ajustar al tamaño preferido de los componentes
        dialog.pack();

        // Obtener dimensiones de la pantalla
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int maxAnchoPantalla = (int) (screenSize.width * 0.9);
        int maxAltoPantalla = (int) (screenSize.height * 0.8);

        // Establecer tamaño mínimo y máximo
        Dimension tamanioActual = dialog.getSize();

        // Tamaño mínimo: 400x350 (300 para imagen + 50 para controles)
        Dimension tamanioMinimo = new Dimension(400, 350);

        // Tamaño máximo: el menor entre el tamaño actual y los límites de pantalla
        Dimension tamanioMaximo = new Dimension(
                Math.min(Math.max(tamanioActual.width, 400), maxAnchoPantalla),
                Math.min(Math.max(tamanioActual.height, 350), maxAltoPantalla)
        );

        // Si el tamaño después de pack() es muy pequeño, usar el mínimo
        if (tamanioActual.width < tamanioMinimo.width || tamanioActual.height < tamanioMinimo.height) {
            dialog.setSize(tamanioMinimo);
        }

        // Si el tamaño después de pack() es muy grande, usar el máximo
        if (tamanioActual.width > tamanioMaximo.width || tamanioActual.height > tamanioMaximo.height) {
            dialog.setSize(tamanioMaximo);
        }

        // Configurar límites de redimensionamiento
        dialog.setMinimumSize(tamanioMinimo);
        dialog.setMaximumSize(tamanioMaximo);

        dialog.setLocationRelativeTo(labelOrigen);

        // Agregar atajo de teclado (ESC para cerrar)
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cerrar");
        panel.getActionMap().put("cerrar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        // Hacer el diálogo redimensionable
        dialog.setResizable(true);
        dialog.setVisible(true);
    }
}