package com.vectorgarman.views;

import com.vectorgarman.api.ClienteAPI;
import com.vectorgarman.dto.*;
import com.vectorgarman.utils.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class Reportes extends JFrame {
    private Usuario usuarioLogueado;

    private JComboBox<Estado> comboEstado;
    private JComboBox<Municipio> comboMunicipio;
    private JComboBox<Colonia> comboColonia;

    private JButton btnCrearReporte;
    private JButton btnPerfil;

    private JPanel panelListaReportes;

    private List<Map<?, ?>> todosLosReportes = new ArrayList<>();
    private List<Map<?, ?>> reportesFiltrados = new ArrayList<>();
    private List<ItemReporte> itemsVisibles = new ArrayList<>();

    private Set<Long> reportesVotadosPorUsuario = new HashSet<>();

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

        setSize(1000, 700);

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
                    // Aplicar filtros cuando cambia el estado
                    aplicarFiltrosYMostrar();
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
                    // Aplicar filtros cuando cambia el municipio
                    aplicarFiltrosYMostrar();
                }
            }
        });

        comboColonia.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    // Aplicar filtros cuando cambia la colonia
                    aplicarFiltrosYMostrar();
                }
            }
        });

        header.add(filtrosPanel, BorderLayout.WEST);

        // Botón para limpiar filtros
        JButton btnLimpiarFiltros = new JButton("<html><br><center>Restablecer<br>filtros</center><br></html>");
        btnLimpiarFiltros.setFont(new Font("Arial", Font.BOLD, 11));
        btnLimpiarFiltros.setPreferredSize(new Dimension(100, 50));
        btnLimpiarFiltros.addActionListener(e -> limpiarFiltros());

        // Espaciador
        JLabel lblEspaciador = new JLabel("                                         ");

        // Botón para crear reporte
        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnCrearReporte = new JButton("➕");
        btnCrearReporte.setPreferredSize(new Dimension(45, 35));

        // Botón de perfil
        btnPerfil = new JButton("👤");
        btnPerfil.setPreferredSize(new Dimension(45, 35));

        botonesPanel.add(btnLimpiarFiltros);
        botonesPanel.add(lblEspaciador);
        botonesPanel.add(btnCrearReporte);
        botonesPanel.add(btnPerfil);


        header.add(botonesPanel, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);
    }

    private void limpiarFiltros() {
        // Restablecer a la ubicación del usuario
        Ubicacion ubicacion = SessionManager.getInstance().getUbicacionUsuario();
        if (ubicacion != null) {
            seleccionarEstadoPorId(ubicacion.getIdEstado());
            seleccionarMunicipioPorId(ubicacion.getIdMunicipio());
            seleccionarColoniaPorId(ubicacion.getIdColonia());
        } else {
            comboEstado.setSelectedIndex(0);
        }
        cargarReportes();
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
                    if ("OK".equals(responseUbicacion.getStatus()) && responseUbicacion.getData() instanceof Map<?, ?> ubicacionMap) {
                        ubicacion = mapearUbicacion(ubicacionMap);

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

    private void seleccionarEstadoPorId(Long idEstado) {
        for (int i = 0; i < comboEstado.getItemCount(); i++) {
            Estado estado = comboEstado.getItemAt(i);
            if (estado.getIdestado() == idEstado.intValue()) {
                comboEstado.setSelectedIndex(i);
                break;
            }
        }
    }

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
                ApiResponse<?> responseVotados = api.obtenerReportesVotadosPorUsuario(usuarioLogueado.getIdusuario());

                if ("OK".equals(responseVotados.getStatus())) {
                    Object dataVotados = responseVotados.getData();

                    if (dataVotados instanceof List<?> reportesVotados) {
                        reportesVotadosPorUsuario.clear();

                        for (Object item : reportesVotados) {
                            if (item instanceof Map<?, ?> reporteMap) {
                                Map<?, ?> reporteView = reporteMap.get("reporteView") instanceof Map
                                        ? (Map<?, ?>) reporteMap.get("reporteView")
                                        : null;

                                if (reporteView != null) {
                                    Object idObj = reporteView.get("idreporte");
                                    if (idObj != null) {
                                        Long idReporte = ((Number) idObj).longValue();
                                        reportesVotadosPorUsuario.add(idReporte);
                                    }
                                }
                            }
                        }
                    }
                }

                ApiResponse<?> response = api.obtenerTodosLosReportes();

                SwingUtilities.invokeLater(() -> {
                    String status = response.getStatus() != null ? response.getStatus() : "";

                    if ("OK".equals(status)) {
                        Object dataObj = response.getData();

                        if (dataObj instanceof List<?> reportes) {
                            // ⭐ CAMBIO IMPORTANTE: Convertir a mapas mutables
                            todosLosReportes.clear();
                            for (Object item : reportes) {
                                if (item instanceof Map<?, ?> reporteMap) {
                                    // Convertir a HashMap mutable
                                    Map<String, Object> reporteMutable = convertirAMapaMutable(reporteMap);
                                    todosLosReportes.add(reporteMutable);
                                }
                            }

                            // Aplicar filtro inicial con la ubicación del usuario
                            aplicarFiltrosYMostrar();
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

    // ============================================
// MÉTOD0 AUXILIAR: Convertir mapa a mutable
// ============================================
    private Map<String, Object> convertirAMapaMutable(Map<?, ?> mapaOriginal) {
        Map<String, Object> mapaMutable = new HashMap<>();

        for (Map.Entry<?, ?> entry : mapaOriginal.entrySet()) {
            String key = entry.getKey().toString();
            Object value = entry.getValue();

            // Si el valor es un mapa, también lo convertimos a mutable
            if (value instanceof Map<?, ?>) {
                value = convertirAMapaMutable((Map<?, ?>) value);
            }
            // Si el valor es una lista, podríamos convertirla también
            else if (value instanceof List<?>) {
                value = new ArrayList<>((List<?>) value);
            }

            mapaMutable.put(key, value);
        }

        return mapaMutable;
    }


    private void aplicarFiltrosYMostrar() {
        // Obtener los valores seleccionados en los combobox
        Estado estadoSeleccionado = (Estado) comboEstado.getSelectedItem();
        Municipio municipioSeleccionado = (Municipio) comboMunicipio.getSelectedItem();
        Colonia coloniaSeleccionado = (Colonia) comboColonia.getSelectedItem();

        // Filtrar los reportes
        reportesFiltrados = filtrarReportes(estadoSeleccionado, municipioSeleccionado, coloniaSeleccionado);

        // Mostrar los reportes filtrados
        mostrarReportesFiltrados();
    }

    private List<Map<?, ?>> filtrarReportes(Estado estado, Municipio municipio, Colonia colonia) {
        List<Map<?, ?>> resultado = new ArrayList<>();

        for (Map<?, ?> reporteMap : todosLosReportes) {
            if (cumpleFiltros(reporteMap, estado, municipio, colonia)) {
                resultado.add(reporteMap);
            }
        }
        return resultado;
    }

    private boolean cumpleFiltros(Map<?, ?> reporteMap, Estado estado, Municipio municipio, Colonia colonia) {
        Map<?, ?> reporteView = reporteMap.get("reporteView") instanceof Map ? (Map<?, ?>) reporteMap.get("reporteView") : null;
        if (reporteView == null) return false;

        // Si no hay filtros seleccionados, mostrar todos los reportes
        if (estado == null && municipio == null && colonia == null) {
            return true;
        }

        // Obtener nombres de ubicación del reporte
        String nombreEstadoReporte = obtenerStringDesdeMap(reporteView, "estado");
        String nombreMunicipioReporte = obtenerStringDesdeMap(reporteView, "municipio");
        String nombreColoniaReporte = obtenerStringDesdeMap(reporteView, "colonia");

        // Si el reporte no tiene datos de ubicación y hay filtros activos, no mostrarlo
        if ((estado != null && nombreEstadoReporte == null) ||
                (municipio != null && nombreMunicipioReporte == null) ||
                (colonia != null && nombreColoniaReporte == null)) {
            return false;
        }

        // Aplicar filtros jerárquicamente por NOMBRE
        boolean cumpleEstado = (estado == null) ||
                (nombreEstadoReporte != null && nombreEstadoReporte.equalsIgnoreCase(estado.getNombre()));

        boolean cumpleMunicipio = (municipio == null) ||
                (nombreMunicipioReporte != null && nombreMunicipioReporte.equalsIgnoreCase(municipio.getNombre()));

        boolean cumpleColonia = (colonia == null) ||
                (nombreColoniaReporte != null && nombreColoniaReporte.equalsIgnoreCase(colonia.getNombre()));

        return cumpleEstado && cumpleMunicipio && cumpleColonia;
    }

    private String obtenerStringDesdeMap(Map<?, ?> map, String key) {
        Object value = map.get(key);
        return value != null ? value.toString().trim() : null;
    }

    private void mostrarReportesFiltrados() {
        panelListaReportes.removeAll();
        itemsVisibles.clear();

        if (reportesFiltrados.isEmpty()) {
            // Mostrar mensaje cuando no hay reportes
            JLabel lblSinReportes = new JLabel("No hay reportes que coincidan con los filtros seleccionados");
            lblSinReportes.setFont(new Font("Arial", Font.ITALIC, 14));
            lblSinReportes.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelListaReportes.add(lblSinReportes);
        } else {
            // Mostrar reportes filtrados
            for (Map<?, ?> reporteMap : reportesFiltrados) {
                JPanel tarjeta = crearTarjetaReporte(reporteMap);
                panelListaReportes.add(tarjeta);
                panelListaReportes.add(Box.createRigidArea(new Dimension(0, 15)));
            }
        }

        panelListaReportes.revalidate();
        panelListaReportes.repaint();
    }

    private JPanel crearTarjetaReporte(Map<?, ?> reporteMap) {
        // Extraer datos del mapa del reporteView
        Map<?, ?> reporteView = reporteMap.get("reporteView") instanceof Map ? (Map<?, ?>) reporteMap.get("reporteView") : null;

        if (reporteView == null) return new JPanel();

        // Obtener todos los datos del reporte
        Long idReporte = reporteView.get("idreporte") != null ? ((Number) reporteView.get("idreporte")).longValue() : null;
        String titulo = reporteView.get("titulo") != null ? reporteView.get("titulo").toString() : "Sin título";
        String descripcion = reporteView.get("descripcion") != null ? reporteView.get("descripcion").toString() : "";
        String solucionpropuesta = reporteView.get("solucionpropuesta") != null ? reporteView.get("solucionpropuesta").toString() : "";
        String fechaCreacion = reporteView.get("fechacreacion") != null ? reporteView.get("fechacreacion").toString() : "";
        String fechaactualizacion = reporteView.get("fechaactualizacion") != null ? reporteView.get("fechaactualizacion").toString() : "";
        Long idusuariocreador = reporteView.get("idusuariocreador") != null ? ((Number) reporteView.get("idusuariocreador")).longValue() : null;
        String creador = reporteView.get("creador") != null ? reporteView.get("creador").toString() : "Anónimo";
        String emailcreador = reporteView.get("emailcreador") != null ? reporteView.get("emailcreador").toString() : "";
        String categoria = reporteView.get("categoria") != null ? reporteView.get("categoria").toString() : "";
        String estadoReporte = reporteView.get("estadoreporte") != null ? reporteView.get("estadoreporte").toString() : "Pendiente";
        String colorEstado = reporteView.get("colorestado") != null ? reporteView.get("colorestado").toString() : "#FFA500";
        String prioridad = reporteView.get("prioridad") != null ? reporteView.get("prioridad").toString() : "Media";
        String colonia = reporteView.get("colonia") != null ? reporteView.get("colonia").toString() : "";
        String municipio = reporteView.get("municipio") != null ? reporteView.get("municipio").toString() : "";
        String estado = reporteView.get("estado") != null ? reporteView.get("estado").toString() : "";
        String calle = reporteView.get("calle") != null ? reporteView.get("calle").toString() : "";
        String referencia = reporteView.get("referencia") != null ? reporteView.get("referencia").toString() : "";
        // TODO ver si puedo organizar la lista de todos los reportes a mostrar mediante el total de votos, de mayor a menor
        Long totalVotos = reporteView.get("totalvotos") != null ? ((Number) reporteView.get("totalvotos")).longValue() : 0;
        Long totalComentarios = reporteView.get("totalcomentarios") != null ? ((Number) reporteView.get("totalcomentarios")).longValue() : 0;

        // Panel principal de la tarjeta
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BorderLayout());
        tarjeta.putClientProperty("idReporte", idReporte);
        tarjeta.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        tarjeta.setMaximumSize(new Dimension(900, 440));
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

        // ⭐ PANEL PRINCIPAL CON DOS COLUMNAS
        JPanel contenidoPrincipal = new JPanel(new BorderLayout(10, 0));
        contenidoPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        contenidoPrincipal.setBackground(Color.WHITE);

        // ========== COLUMNA IZQUIERDA (CONTENIDO PRINCIPAL) ==========
        JPanel columnaIzquierda = new JPanel();
        columnaIzquierda.setLayout(new BoxLayout(columnaIzquierda, BoxLayout.Y_AXIS));
        columnaIzquierda.setBackground(Color.WHITE);

        // Línea 1: Creador y fecha
        JLabel lblCreadorFecha = new JLabel("@ " + creador + " • " + fechaCreacion);
        lblCreadorFecha.setFont(new Font("Arial", Font.BOLD, 13));
        lblCreadorFecha.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Línea 2: Estado y prioridad
        JLabel lblEstadoPrioridad = new JLabel(
                "<html>Estado: <span style='color:" + colorEstado + ";'><b>" + estadoReporte + "</b></span>&nbsp;&nbsp;•&nbsp;&nbsp;Prioridad: <b>" + prioridad + "</b></html>"
        );
        lblEstadoPrioridad.setFont(new Font("Arial", Font.PLAIN, 13));
        lblEstadoPrioridad.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Título del reporte
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 13));
        lblTitulo.setForeground(new Color(20, 20, 20));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Descripción (MÁS COMPACTA)
        JTextArea txtDescripcion = new JTextArea(descripcion);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setEditable(false);
        txtDescripcion.setFont(new Font("Arial", Font.PLAIN, 11));
        txtDescripcion.setBackground(Color.WHITE);
        txtDescripcion.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JScrollPane scrollDescripcion = new JScrollPane(txtDescripcion);
        scrollDescripcion.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
        scrollDescripcion.setPreferredSize(new Dimension(520, 45));
        scrollDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollDescripcion.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollDescripcion.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Solución propuesta (MÁS COMPACTA)
        JLabel lblSolucionpropuesta = new JLabel("Solución propuesta:");
        lblSolucionpropuesta.setFont(new Font("Arial", Font.BOLD, 13));
        lblSolucionpropuesta.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea txtSolucionpropuesta = new JTextArea(solucionpropuesta);
        txtSolucionpropuesta.setLineWrap(true);
        txtSolucionpropuesta.setWrapStyleWord(true);
        txtSolucionpropuesta.setEditable(false);
        txtSolucionpropuesta.setFont(new Font("Arial", Font.PLAIN, 11));
        txtSolucionpropuesta.setBackground(new Color(245, 245, 245));
        txtSolucionpropuesta.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JScrollPane scrollSolucion = new JScrollPane(txtSolucionpropuesta);
        scrollSolucion.setBorder(BorderFactory.createDashedBorder(Color.GRAY));
        scrollSolucion.setPreferredSize(new Dimension(520, 45)); // ⭐ Más compacto y menos ancho
        scrollSolucion.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollSolucion.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollSolucion.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Evidencia (MÁS COMPACTA)
        JLabel lblEvidencia = new JLabel("Evidencia:");
        lblEvidencia.setFont(new Font("Arial", Font.BOLD, 13));
        lblEvidencia.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel panelEvidencias = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        panelEvidencias.setBackground(Color.WHITE);
        panelEvidencias.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Cargar evidencias del array (IMÁGENES MÁS PEQUEÑAS)
        Object evidenciasObj = reporteMap.get("evidencias");
        if (evidenciasObj instanceof List<?> evidencias && !evidencias.isEmpty()) {
            int count = Math.min(evidencias.size(), 3);
            for (int i = 0; i < count; i++) {
                if (evidencias.get(i) instanceof Map<?, ?> evidenciaMap) {
                    try {
                        Object archivoObj = evidenciaMap.get("archivo");
                        if (archivoObj != null) {
                            String base64String = archivoObj.toString();
                            byte[] bytesImagen = java.util.Base64.getDecoder().decode(base64String);
                            java.io.InputStream in = new java.io.ByteArrayInputStream(bytesImagen);
                            java.awt.image.BufferedImage image = javax.imageio.ImageIO.read(in);

                            if (image != null) {
                                // ⭐ Imágenes más pequeñas (120x80 en lugar de 150x100)
                                java.awt.Image scaledImage = image.getScaledInstance(120, 80, java.awt.Image.SCALE_SMOOTH);
                                ImageIcon icon = new ImageIcon(scaledImage);

                                JLabel lblImagen = new JLabel(icon);
                                lblImagen.setPreferredSize(new Dimension(120, 80));
                                lblImagen.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                                panelEvidencias.add(lblImagen);

                                lblImagen.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                                lblImagen.addMouseListener(new MouseAdapter() {
                                    @Override
                                    public void mouseClicked(MouseEvent e) {
                                        mostrarImagenGrande(image, lblImagen);
                                    }
                                });
                            } else {
                                JLabel imgPlaceholder = new JLabel("Error img");
                                imgPlaceholder.setPreferredSize(new Dimension(120, 80));
                                imgPlaceholder.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                                imgPlaceholder.setHorizontalAlignment(SwingConstants.CENTER);
                                panelEvidencias.add(imgPlaceholder);
                            }
                        } else {
                            JLabel imgPlaceholder = new JLabel("Sin imagen");
                            imgPlaceholder.setPreferredSize(new Dimension(120, 80));
                            imgPlaceholder.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                            imgPlaceholder.setHorizontalAlignment(SwingConstants.CENTER);
                            panelEvidencias.add(imgPlaceholder);
                        }
                    } catch (Exception ex) {
                        JLabel imgPlaceholder = new JLabel("Error");
                        imgPlaceholder.setPreferredSize(new Dimension(120, 80));
                        imgPlaceholder.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
                        imgPlaceholder.setHorizontalAlignment(SwingConstants.CENTER);
                        panelEvidencias.add(imgPlaceholder);
                        System.out.println(ex.getMessage());
                    }
                }
            }
        } else {
            JLabel sinEvidencias = new JLabel("Sin evidencias");
            sinEvidencias.setPreferredSize(new Dimension(120, 80));
            sinEvidencias.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            sinEvidencias.setHorizontalAlignment(SwingConstants.CENTER);
            panelEvidencias.add(sinEvidencias);
        }

        // Panel para comentarios y votos
        JPanel panelSocial = new JPanel();
        panelSocial.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelSocial.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelSocial.setBackground(Color.WHITE);

        JButton btnVotar = new JButton("❤");
        btnVotar.setFocusable(false);
        btnVotar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        btnVotar.setBorderPainted(false);
        btnVotar.setContentAreaFilled(false);
        btnVotar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel lblVotos = new JLabel(String.valueOf(totalVotos));
        lblVotos.setFont(new Font("Arial", Font.PLAIN, 12));

        boolean yaVotoEsteReporte = reportesVotadosPorUsuario.contains(idReporte);

        Map<String, Object> reporteConvertido = new java.util.HashMap<>();
        reporteConvertido.put("idreporte", idReporte);
        reporteConvertido.put("totalvotos", totalVotos);
        reporteConvertido.put("yaVoto", yaVotoEsteReporte);
        reporteConvertido.put("reporteView", reporteView);

        itemsVisibles.add(new ItemReporte(reporteConvertido, lblVotos));
        final int index = itemsVisibles.size() - 1;

        if (yaVotoEsteReporte) {
            btnVotar.setForeground(Color.RED);
        } else {
            btnVotar.setForeground(Color.BLACK);
        }

        btnVotar.addActionListener(e -> {
            ItemReporte item = itemsVisibles.get(index);
            boolean yaVoto = (boolean) item.getData().getOrDefault("yaVoto", false);

            if (yaVoto) {
                quitarVoto(index);
                btnVotar.setForeground(Color.BLACK);
            } else {
                votar(index);
                btnVotar.setForeground(Color.RED);
            }
        });

        JButton btnComentarios = new JButton("Ver comentarios (" + totalComentarios + ")");
        btnComentarios.setFocusable(false);
        btnComentarios.setFont(new Font("Arial", Font.PLAIN, 13));
        btnComentarios.setForeground(new Color(13, 110, 253));
        btnComentarios.setBorderPainted(false);
        btnComentarios.setContentAreaFilled(false);
        btnComentarios.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnComentarios.addActionListener(e -> {
            abrirVentanaComentarios(idReporte);
        });

        JLabel lblCrearComentario = new JLabel("Escribir comentario");
        lblCrearComentario.setFont(new Font("Arial", Font.PLAIN, 13));

        // Botón para escribir comentario
        JButton btnCrearComentario = new JButton("💬+");
        btnCrearComentario.setFocusable(false);
        btnCrearComentario.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        btnCrearComentario.setBorderPainted(false);
        btnCrearComentario.setContentAreaFilled(false);
        btnCrearComentario.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnCrearComentario.addActionListener(e -> {
            abrirVentanaCrearComentarioDesdeReporte(idReporte);
        });

        panelSocial.add(btnVotar);
        panelSocial.add(lblVotos);
        panelSocial.add(Box.createHorizontalStrut(15));
        panelSocial.add(btnComentarios);
        panelSocial.add(btnCrearComentario);

        // Agregar componentes a columna izquierda
        columnaIzquierda.add(lblCreadorFecha);
        columnaIzquierda.add(Box.createRigidArea(new Dimension(0, 5)));
        columnaIzquierda.add(lblEstadoPrioridad);
        columnaIzquierda.add(Box.createRigidArea(new Dimension(0, 8)));
        columnaIzquierda.add(lblTitulo);
        columnaIzquierda.add(Box.createRigidArea(new Dimension(0, 5)));
        columnaIzquierda.add(scrollDescripcion);
        columnaIzquierda.add(Box.createRigidArea(new Dimension(0, 8)));
        columnaIzquierda.add(lblSolucionpropuesta);
        columnaIzquierda.add(Box.createRigidArea(new Dimension(0, 5)));
        columnaIzquierda.add(scrollSolucion);
        columnaIzquierda.add(Box.createRigidArea(new Dimension(0, 8)));
        columnaIzquierda.add(lblEvidencia);
        columnaIzquierda.add(panelEvidencias);
        columnaIzquierda.add(Box.createRigidArea(new Dimension(0, 8)));
        columnaIzquierda.add(panelSocial);

        // ========== COLUMNA DERECHA (INFORMACIÓN EXTRA) ==========
        JPanel columnaDerecha = new JPanel();
        columnaDerecha.setLayout(new BoxLayout(columnaDerecha, BoxLayout.Y_AXIS));
        columnaDerecha.setBackground(new Color(248, 249, 250));
        columnaDerecha.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        columnaDerecha.setPreferredSize(new Dimension(280, 0));

        // Título de la sección
        JLabel lblInfoAdicional = new JLabel("Información extra");
        lblInfoAdicional.setFont(new Font("Arial", Font.BOLD, 13));
        lblInfoAdicional.setForeground(new Color(50, 50, 50));
        lblInfoAdicional.setAlignmentX(Component.LEFT_ALIGNMENT);

        JSeparator separador = new JSeparator();
        separador.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        columnaDerecha.add(lblInfoAdicional);
        columnaDerecha.add(Box.createRigidArea(new Dimension(0, 5)));
        columnaDerecha.add(separador);

        // Fecha de actualización
        if (!fechaactualizacion.isEmpty() && !fechaactualizacion.equals(fechaCreacion)) {
            JPanel panelActualizacion = crearCampoInfo("Actualizado", fechaactualizacion);
            columnaDerecha.add(Box.createRigidArea(new Dimension(0, 5)));
            columnaDerecha.add(panelActualizacion);
        }

        // Categoría
        JPanel panelCategoria = crearCampoInfo("Categoría", categoria);

        // Ubicación
        String ubicacionCompleta = String.join(", ",
                !calle.isEmpty() ? calle : "Sin calle",
                !colonia.isEmpty() ? colonia : "",
                !municipio.isEmpty() ? municipio : "",
                !estado.isEmpty() ? estado : ""
        );
        JPanel panelUbicacion = crearCampoInfo("Ubicación", ubicacionCompleta);

        // Referencia
        if (!referencia.isEmpty()) {
            JPanel panelReferencia = crearCampoInfo("Referencia", referencia);
            columnaDerecha.add(Box.createRigidArea(new Dimension(0, 5)));
            columnaDerecha.add(panelReferencia);
        }

        // Email del creador
        if (!emailcreador.isEmpty()) {
            JPanel panelEmail = crearCampoInfo("Email", emailcreador);
            columnaDerecha.add(Box.createRigidArea(new Dimension(0, 5)));
            columnaDerecha.add(panelEmail);
        }

        // Agregar componentes a columna derecha
        columnaDerecha.add(Box.createRigidArea(new Dimension(0, 10)));
        columnaDerecha.add(panelCategoria);
        columnaDerecha.add(Box.createRigidArea(new Dimension(0, 5)));
        columnaDerecha.add(panelUbicacion);

        // Espacio flexible al final
        columnaDerecha.add(Box.createVerticalGlue());

        contenidoPrincipal.add(columnaIzquierda, BorderLayout.CENTER);
        contenidoPrincipal.add(columnaDerecha, BorderLayout.EAST);

        tarjeta.add(contenidoPrincipal, BorderLayout.CENTER);

        return tarjeta;
    }

    // --------------------------------

    // ============================================
// ABRIR VENTANA CREAR COMENTARIO DESDE REPORTE
// ============================================
    private void abrirVentanaCrearComentarioDesdeReporte(Long idReporte) {
        String nombreUsuario = usuarioLogueado.getNombreusuario() != null
                ? usuarioLogueado.getNombreusuario() : "Usuario";
        Boolean esVerificado = usuarioLogueado.getEmpleadogubverificado() != null
                ? usuarioLogueado.getEmpleadogubverificado() : false;

        JDialog ventanaCrear = new JDialog(
                this,
                "Nuevo Comentario",
                true
        );
        ventanaCrear.setSize(700, 400);
        ventanaCrear.setLocationRelativeTo(this);
        ventanaCrear.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel nombre
        JPanel panelNombre = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelNombre.setBackground(Color.WHITE);

        JLabel lblNombreUsuario = new JLabel(nombreUsuario + " ");
        lblNombreUsuario.setFont(new Font("Arial", Font.BOLD, 13));
        panelNombre.add(lblNombreUsuario);

        if (esVerificado) {
            JLabel lblVerificado = new JLabel("<html><font face='Segoe UI Emoji'> ✓ </font><font face='Arial'>Gubernamental</font></html>");
            lblVerificado.setFont(new Font("Arial", Font.BOLD, 13));
            lblVerificado.setForeground(new Color(25, 135, 84));
            panelNombre.add(lblVerificado);
        }

        // Área de texto
        JTextArea txtContenido = new JTextArea();
        txtContenido.setLineWrap(true);
        txtContenido.setWrapStyleWord(true);
        txtContenido.setFont(new Font("Arial", Font.PLAIN, 14));
        txtContenido.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        txtContenido.setBackground(new Color(248, 249, 250));

        JScrollPane scrollContenido = new JScrollPane(txtContenido);
        scrollContenido.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // Panel inferior con botones
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelInferior.setBackground(Color.WHITE);

        JLabel lblContador = new JLabel("0 caracteres");
        lblContador.setFont(new Font("Arial", Font.PLAIN, 12));
        lblContador.setForeground(Color.GRAY);

        txtContenido.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { actualizar(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { actualizar(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { actualizar(); }

            private void actualizar() {
                int longitud = txtContenido.getText().length();
                lblContador.setText(longitud + " caracteres");
                lblContador.setForeground(longitud > 1000 ? Color.RED : Color.GRAY);
            }
        });

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Arial", Font.PLAIN, 13));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(e -> ventanaCrear.dispose());

        JButton btnEnviar = new JButton("<html><font face='Arial'>Enviar</font><font face='Segoe UI Emoji'> ➤ </font></html>");
        btnEnviar.setFont(new Font("Arial", Font.BOLD, 13));
        btnEnviar.setBackground(new Color(13, 110, 253));
        btnEnviar.setForeground(Color.WHITE);
        btnEnviar.setFocusPainted(false);
        btnEnviar.setBorderPainted(false);
        btnEnviar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnEnviar.setPreferredSize(new Dimension(120, 35));

        btnEnviar.addActionListener(e -> {
            String contenido = txtContenido.getText().trim();

            if (contenido.isEmpty()) {
                JOptionPane.showMessageDialog(ventanaCrear,
                        "El contenido del comentario no puede estar vacío",
                        "Campo requerido",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (contenido.length() > 1000) {
                JOptionPane.showMessageDialog(ventanaCrear,
                        "El comentario no puede exceder los 1000 caracteres",
                        "Contenido muy largo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            btnEnviar.setEnabled(false);
            btnEnviar.setText("Enviando...");

            enviarComentarioDesdeReporte(idReporte, contenido, ventanaCrear, btnEnviar);
        });

        panelInferior.add(lblContador);
        panelInferior.add(Box.createHorizontalStrut(10));
        panelInferior.add(btnCancelar);
        panelInferior.add(btnEnviar);

        panelPrincipal.add(panelNombre, BorderLayout.NORTH);
        panelPrincipal.add(scrollContenido, BorderLayout.CENTER);
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        ventanaCrear.add(panelPrincipal);
        ventanaCrear.setVisible(true);
    }

    // ============================================
// ENVIAR COMENTARIO DESDE REPORTE
// ============================================
    private void enviarComentarioDesdeReporte(Long idReporte, String contenido,
                                              JDialog ventanaActual, JButton btnEnviar) {
        new Thread(() -> {
            try {
                ClienteAPI api = new ClienteAPI();
                ApiResponse<?> response = api.crearComentario(
                        usuarioLogueado.getIdusuario(),
                        idReporte,
                        null, // ⭐ null = comentario principal, no respuesta
                        contenido
                );

                SwingUtilities.invokeLater(() -> {
                    if (response != null && response.isSuccess()) {
                        JOptionPane.showMessageDialog(ventanaActual,
                                "Comentario enviado exitosamente",
                                "Éxito",
                                JOptionPane.INFORMATION_MESSAGE);

                        ventanaActual.dispose();

                        // ⭐ Actualizar el contador SOLO UNA VEZ
                        actualizarContadorComentarios(idReporte);
                    } else {
                        btnEnviar.setEnabled(true);
                        btnEnviar.setText("<html><font face='Arial'>Enviar</font><font face='Segoe UI Emoji'> ➤ </font></html>");
                        JOptionPane.showMessageDialog(ventanaActual,
                                "Error al enviar el comentario: " +
                                        (response != null ? response.getMensaje() : "Error desconocido"),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    btnEnviar.setEnabled(true);
                    btnEnviar.setText("<html><font face='Arial'>Enviar</font><font face='Segoe UI Emoji'> ➤ </font></html>");
                    JOptionPane.showMessageDialog(ventanaActual,
                            "Error al conectar con el servidor: " + ex.getMessage(),
                            "Error de Conexión",
                            JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }

// ============================================
    // MÉTOD0 ACTUALIZADO: actualizarContadorComentarios
// ============================================
    private void actualizarContadorComentarios(Long idReporte) {
        boolean actualizado = false;

        // ⭐ SOLO actualizar en todosLosReportes (la fuente de verdad)
        for (Map<?, ?> reporteMap : todosLosReportes) {
            if (reporteMap instanceof Map) {
                Map<String, Object> reporteMutable = (Map<String, Object>) reporteMap;
                Map<?, ?> reporteView = reporteMutable.get("reporteView") instanceof Map
                        ? (Map<?, ?>) reporteMutable.get("reporteView")
                        : null;

                if (reporteView instanceof Map) {
                    Map<String, Object> reporteViewMutable = (Map<String, Object>) reporteView;
                    Long idReporteActual = reporteViewMutable.get("idreporte") != null
                            ? ((Number) reporteViewMutable.get("idreporte")).longValue()
                            : null;

                    if (idReporteActual != null && idReporteActual.equals(idReporte)) {
                        Long totalComentarios = reporteViewMutable.get("totalcomentarios") != null
                                ? ((Number) reporteViewMutable.get("totalcomentarios")).longValue()
                                : 0L;

                        Long nuevoTotal = totalComentarios + 1;

                        // Actualizar el mapa
                        reporteViewMutable.put("totalcomentarios", nuevoTotal);

                        // ⭐ Actualizar la UI solo UNA vez
                        if (!actualizado) {
                            actualizarBotónComentariosEnUI(idReporte, nuevoTotal);
                            actualizado = true;
                        }

                        break; // ⭐ Salir del bucle
                    }
                }
            }
        }

        // ⭐ Actualizar reportesFiltrados solo si es necesario mostrarlos
        // (esto se hará automáticamente cuando se vuelva a aplicar filtros)
        if (actualizado) {
            // Forzar re-aplicación de filtros para sincronizar reportesFiltrados
            SwingUtilities.invokeLater(() -> {
                aplicarFiltrosYMostrar();
            });
        }
    }

// ============================================
// MÉTOD0 MEJORADO: actualizarBotónComentariosEnUI
// ============================================
private void actualizarBotónComentariosEnUI(Long idReporte, Long nuevoTotal) {
    for (Component comp : panelListaReportes.getComponents()) {
        if (comp instanceof JPanel) {
            JPanel tarjeta = (JPanel) comp;

            // Verificar si esta tarjeta corresponde al reporte
            Object idReporteProperty = tarjeta.getClientProperty("idReporte");

            if (idReporteProperty instanceof Long && ((Long) idReporteProperty).equals(idReporte)) {
                // Buscar el botón dentro de la tarjeta
                JButton btnComentarios = encontrarBotonComentarios(tarjeta, idReporte);

                if (btnComentarios != null) {
                    btnComentarios.setText("Ver comentarios (" + nuevoTotal + ")");

                    // Forzar actualización visual completa
                    btnComentarios.invalidate();
                    btnComentarios.revalidate();
                    btnComentarios.repaint();

                    tarjeta.invalidate();
                    tarjeta.revalidate();
                    tarjeta.repaint();

                    panelListaReportes.invalidate();
                    panelListaReportes.revalidate();
                    panelListaReportes.repaint();

                    return; // ⭐ Salir después de encontrar y actualizar
                }
            }
        }
    }
}

    // ============================================
// ENCONTRAR BOTÓN COMENTARIOS (RECURSIVO)
// ============================================
    private JButton encontrarBotonComentarios(Container container, Long idReporteBuscado) {
        // Buscar recursivamente en todos los componentes
        for (Component comp : container.getComponents()) {
            if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                String texto = btn.getText();
                if (texto != null && texto.startsWith("Ver comentarios")) {
                    return btn;
                }
            } else if (comp instanceof Container) {
                // Buscar recursivamente en contenedores hijos
                JButton result = encontrarBotonComentarios((Container) comp, idReporteBuscado);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    // ============================================
// ELIMINAR COMENTARIO
// ============================================
    private void eliminarComentario(Long idComentario, ComentarioContext context, JDialog ventanaActual) {
        new Thread(() -> {
            try {
                ClienteAPI api = new ClienteAPI();
                ApiResponse<?> response = api.eliminarComentario(idComentario, usuarioLogueado.getIdusuario());

                SwingUtilities.invokeLater(() -> {
                    if (response != null && response.isSuccess()) {
                        JOptionPane.showMessageDialog(
                                ventanaActual,
                                "Comentario eliminado exitosamente",
                                "Éxito",
                                JOptionPane.INFORMATION_MESSAGE
                        );

                        // ⭐ NUEVO: Decrementar el contador de comentarios
                        decrementarContadorComentarios(context.idReporte);

                        // Cerrar ventana actual y recargar el nivel actual
                        ventanaActual.dispose();
                        cargarYMostrarComentarios(context.idReporte, context.idComentarioPadre);
                    } else {
                        JOptionPane.showMessageDialog(
                                ventanaActual,
                                "Error al eliminar el comentario: " +
                                        (response != null ? response.getMensaje() : "Error desconocido"),
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(
                                ventanaActual,
                                "Error al conectar con el servidor: " + ex.getMessage(),
                                "Error de Conexión",
                                JOptionPane.ERROR_MESSAGE
                        )
                );
            }
        }).start();
    }

    private void decrementarContadorComentarios(Long idReporte) {
        boolean actualizado = false;

        // Actualizar en todosLosReportes (la fuente de verdad)
        for (Map<?, ?> reporteMap : todosLosReportes) {
            if (reporteMap instanceof Map) {
                Map<String, Object> reporteMutable = (Map<String, Object>) reporteMap;
                Map<?, ?> reporteView = reporteMutable.get("reporteView") instanceof Map
                        ? (Map<?, ?>) reporteMutable.get("reporteView")
                        : null;

                if (reporteView instanceof Map) {
                    Map<String, Object> reporteViewMutable = (Map<String, Object>) reporteView;
                    Long idReporteActual = reporteViewMutable.get("idreporte") != null
                            ? ((Number) reporteViewMutable.get("idreporte")).longValue()
                            : null;

                    if (idReporteActual != null && idReporteActual.equals(idReporte)) {
                        Long totalComentarios = reporteViewMutable.get("totalcomentarios") != null
                                ? ((Number) reporteViewMutable.get("totalcomentarios")).longValue()
                                : 0L;

                        // No permitir valores negativos
                        Long nuevoTotal = Math.max(0L, totalComentarios - 1);

                        // Actualizar el mapa
                        reporteViewMutable.put("totalcomentarios", nuevoTotal);

                        // Actualizar la UI solo UNA vez
                        if (!actualizado) {
                            actualizarBotónComentariosEnUI(idReporte, nuevoTotal);
                            actualizado = true;
                        }

                        break;
                    }
                }
            }
        }

        // Forzar re-aplicación de filtros para sincronizar reportesFiltrados
        if (actualizado) {
            SwingUtilities.invokeLater(() -> {
                aplicarFiltrosYMostrar();
            });
        }
    }

    // ============================================
// REGRESAR AL NIVEL ANTERIOR
// ============================================
    private void regresarAlNivelAnterior(ComentarioContext context) {
        // Si estamos en el nivel raíz (comentarios principales del reporte)
        if (context.idComentarioPadre == null) {
            // Cerrar ventana y volver a la pantalla de reportes
            // No hacer nada más, la ventana ya se cerró
            return;
        }

        // Si no estamos en el nivel raíz, necesitamos encontrar el padre del padre
        Long idPadreDelPadre = encontrarPadreDelComentario(context.todosLosComentarios, context.idComentarioPadre);

        // Cargar el nivel anterior
        cargarYMostrarComentarios(context.idReporte, idPadreDelPadre);
    }

    // ============================================
// ENCONTRAR PADRE DE UN COMENTARIO
// ============================================
    private Long encontrarPadreDelComentario(List<?> todosLosComentarios, Long idComentario) {
        for (Object obj : todosLosComentarios) {
            if (obj instanceof Map<?, ?> comentarioMap) {
                Object idComentarioObj = comentarioMap.get("idcomentario");
                if (idComentarioObj != null && ((Number) idComentarioObj).longValue() == idComentario) {
                    Object idPadreObj = comentarioMap.get("idcomentariopadre");
                    return idPadreObj != null ? ((Number) idPadreObj).longValue() : null;
                }
            }
        }
        return null;
    }

    // ============================================
// CLASE AUXILIAR PARA CONTEXTO DE COMENTARIOS
// ============================================
    private static class ComentarioContext {
        Long idReporte;
        Long idComentarioPadre; // null si es raíz
        List<?> todosLosComentarios;

        ComentarioContext(Long idReporte, Long idComentarioPadre, List<?> comentarios) {
            this.idReporte = idReporte;
            this.idComentarioPadre = idComentarioPadre;
            this.todosLosComentarios = comentarios;
        }
    }

    // ============================================
// MÉTODO PRINCIPAL: ABRIR VENTANA DE COMENTARIOS
// ============================================
    private void abrirVentanaComentarios(Long idReporte) {
        cargarYMostrarComentarios(idReporte, null);
    }

    // ============================================
// MÉTODO UNIFICADO: CARGAR Y MOSTRAR COMENTARIOS
// ============================================
    private void cargarYMostrarComentarios(Long idReporte, Long idComentarioPadre) {
        new Thread(() -> {
            try {
                ClienteAPI api = new ClienteAPI();
                ApiResponse<?> response = api.obtenerComentariosPorReporte(idReporte);

                if (response != null && response.isSuccess()) {
                    List<?> comentarios = response.getData() instanceof List<?>
                            ? (List<?>) response.getData()
                            : new ArrayList<>();

                    SwingUtilities.invokeLater(() ->
                            mostrarVentanaComentarios(new ComentarioContext(idReporte, idComentarioPadre, comentarios))
                    );
                } else {
                    SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(this,
                                    "No se pudieron cargar los comentarios: " +
                                            (response != null ? response.getMensaje() : "Error desconocido"),
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE)
                    );
                }
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(this,
                                "Error al obtener los comentarios: " + ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE)
                );
            }
        }).start();
    }

    // ============================================
// MOSTRAR VENTANA DE COMENTARIOS (UNIFICADO)
// ============================================
    private void mostrarVentanaComentarios(ComentarioContext context) {
        // Determinar título de la ventana
        String titulo = context.idComentarioPadre == null
                ? "Comentarios del Reporte #" + context.idReporte
                : "Respuestas al Comentario #" + context.idComentarioPadre;

        // Crear ventana modal
        JDialog ventana = new JDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                titulo,
                true
        );
        ventana.setSize(900, 700);
        ventana.setLocationRelativeTo(this);
        ventana.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Panel principal con BorderLayout para incluir header
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(new Color(240, 242, 245));

        // ========== HEADER CON BOTÓN REGRESAR ==========
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(new Color(255, 255, 255));
        panelHeader.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        // Botón regresar
        JButton btnRegresar = new JButton("← Regresar");
        btnRegresar.setFont(new Font("Arial", Font.BOLD, 14));
        btnRegresar.setForeground(new Color(13, 110, 253));
        btnRegresar.setBackground(new Color(240, 248, 255));
        btnRegresar.setFocusPainted(false);
        btnRegresar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(13, 110, 253), 1),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        btnRegresar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Efecto hover
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
            ventana.dispose();
            regresarAlNivelAnterior(context);
        });

        // Título centrado
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(30, 30, 30));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        panelHeader.add(btnRegresar, BorderLayout.WEST);
        panelHeader.add(lblTitulo, BorderLayout.CENTER);

        // Filtrar comentarios según nivel
        List<Map<?, ?>> comentariosFiltrados = filtrarComentariosPorPadre(
                context.todosLosComentarios,
                context.idComentarioPadre
        );

        // Panel de comentarios con scroll
        JPanel panelComentarios = new JPanel();
        panelComentarios.setLayout(new BoxLayout(panelComentarios, BoxLayout.Y_AXIS));
        panelComentarios.setBackground(new Color(240, 242, 245));
        panelComentarios.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Mostrar comentarios o mensaje vacío
        if (comentariosFiltrados.isEmpty()) {
            JLabel lblVacio = new JLabel(
                    context.idComentarioPadre == null
                            ? "No hay comentarios aún"
                            : "No hay respuestas aún"
            );
            lblVacio.setFont(new Font("Arial", Font.ITALIC, 14));
            lblVacio.setForeground(Color.GRAY);
            lblVacio.setAlignmentX(Component.LEFT_ALIGNMENT);
            panelComentarios.add(lblVacio);
        } else {
            for (Map<?, ?> comentario : comentariosFiltrados) {
                JPanel tarjeta = crearTarjetaComentario(comentario, context, ventana);
                tarjeta.setAlignmentX(Component.LEFT_ALIGNMENT);
                panelComentarios.add(tarjeta);
                panelComentarios.add(Box.createRigidArea(new Dimension(0, 15)));
            }
        }

        // Scroll
        JScrollPane scrollPane = new JScrollPane(panelComentarios);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);

        // Ensamblar panel principal
        panelPrincipal.add(panelHeader, BorderLayout.NORTH);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);

        ventana.add(panelPrincipal);
        ventana.setVisible(true);
    }

    // ============================================
// FILTRAR COMENTARIOS POR PADRE
// ============================================
    private List<Map<?, ?>> filtrarComentariosPorPadre(List<?> todosComentarios, Long idPadre) {
        List<Map<?, ?>> resultado = new ArrayList<>();

        for (Object obj : todosComentarios) {
            if (obj instanceof Map<?, ?> comentarioMap) {
                Object idPadreComentario = comentarioMap.get("idcomentariopadre");

                boolean cumpleFiltro = (idPadre == null && idPadreComentario == null) ||
                        (idPadre != null && idPadreComentario != null &&
                                ((Number) idPadreComentario).longValue() == idPadre);

                if (cumpleFiltro) {
                    resultado.add(comentarioMap);
                }
            }
        }

        return resultado;
    }

    // ============================================
// CREAR TARJETA DE COMENTARIO (UNIFICADO)
// ============================================
    private JPanel crearTarjetaComentario(Map<?, ?> comentarioMap, ComentarioContext context, JDialog ventanaActual) {
        // Extraer datos del comentario
        Long idComentario = comentarioMap.get("idcomentario") != null
                ? ((Number) comentarioMap.get("idcomentario")).longValue() : null;
        Long idUsuario = comentarioMap.get("idusuario") != null
                ? ((Number) comentarioMap.get("idusuario")).longValue() : null;
        String contenido = comentarioMap.get("contenido") != null
                ? comentarioMap.get("contenido").toString() : "";
        String fechaCreacion = comentarioMap.get("fechacreacion") != null
                ? comentarioMap.get("fechacreacion").toString() : "";
        Boolean editado = comentarioMap.get("editado") != null
                ? (Boolean) comentarioMap.get("editado") : false;
        Boolean esOficial = comentarioMap.get("esoficial") != null
                ? (Boolean) comentarioMap.get("esoficial") : false;

        String nombreUsuario = obtenerNombreUsuario(idUsuario);

        // Contar respuestas hijas
        int numRespuestas = contarRespuestas(context.todosLosComentarios, idComentario);

        // Determinar si es comentario raíz o respuesta (para tamaño de avatar)
        boolean esRaiz = (context.idComentarioPadre == null);
        int tamanoAvatar = esRaiz ? 50 : 40;

        // Panel principal de la tarjeta
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BorderLayout(esRaiz ? 15 : 12, 0));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(esRaiz ? 15 : 12, esRaiz ? 15 : 12, esRaiz ? 15 : 12, esRaiz ? 15 : 12)
        ));

        // Avatar
        JPanel panelAvatar = crearAvatar(nombreUsuario, esOficial, tamanoAvatar);

        // Panel de contenido
        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setBackground(Color.WHITE);

        // Encabezado (usuario y fecha)
        JPanel panelEncabezado = crearEncabezadoComentario(nombreUsuario, fechaCreacion, editado, esOficial);

        // Contenido con scroll
        JScrollPane scrollContenido = crearScrollContenido(contenido, esRaiz ? 120 : 100);

        // Panel de acciones (pasamos idUsuario para verificar si es del usuario actual)
        JPanel panelAcciones = crearPanelAcciones(
                idComentario,
                idUsuario, // ⭐ Nuevo parámetro
                numRespuestas,
                context,
                ventanaActual
        );

        // Agregar componentes al panel de contenido
        panelContenido.add(panelEncabezado);
        panelContenido.add(scrollContenido);
        panelContenido.add(panelAcciones);

        // Agregar todo a la tarjeta
        tarjeta.add(panelAvatar, BorderLayout.WEST);
        tarjeta.add(panelContenido, BorderLayout.CENTER);
        tarjeta.setMaximumSize(new Dimension(850, tarjeta.getPreferredSize().height));

        return tarjeta;
    }

    // ============================================
// CREAR AVATAR
// ============================================
    private JPanel crearAvatar(String nombreUsuario, boolean esOficial, int tamano) {
        JPanel panelAvatar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Color del círculo
                if (esOficial) {
                    g2.setColor(new Color(25, 135, 84));
                } else {
                    g2.setColor(tamano > 40 ? new Color(13, 110, 253) : new Color(108, 117, 125));
                }
                g2.fillOval(0, 0, tamano, tamano);

                // Inicial del usuario
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, tamano > 40 ? 20 : 16));
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

    // ============================================
// CREAR ENCABEZADO DE COMENTARIO
// ============================================
    private JPanel crearEncabezadoComentario(String nombreUsuario, String fechaCreacion, Boolean editado, Boolean esOficial) {
        JPanel panelEncabezado = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelEncabezado.setBackground(Color.WHITE);
        panelEncabezado.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblUsuario = new JLabel(nombreUsuario);
        lblUsuario.setFont(new Font("Arial", Font.BOLD, 13));
        lblUsuario.setForeground(new Color(30, 30, 30));

        JLabel lblFecha = new JLabel(" • " + fechaCreacion + (editado ? " (editado) • " : " • "));
        lblFecha.setFont(new Font("Arial", Font.PLAIN, 13));
        lblFecha.setForeground(Color.GRAY);

        panelEncabezado.add(lblUsuario);
        panelEncabezado.add(lblFecha);

        if (esOficial) {
            JLabel lblOficial = new JLabel("<html><font face='Segoe UI Emoji'>✓ </font><font face='Arial'>Gubernamental</font></html>");
            lblOficial.setFont(new Font("Arial", Font.BOLD, 13));
            lblOficial.setForeground(new Color(25, 135, 84));
            panelEncabezado.add(lblOficial);
        }

        return panelEncabezado;
    }

    // ============================================
// CREAR SCROLL DE CONTENIDO
// ============================================
    private JScrollPane crearScrollContenido(String contenido, int alturaMaxima) {
        JTextArea txtContenido = new JTextArea(contenido);
        txtContenido.setLineWrap(true);
        txtContenido.setWrapStyleWord(true);
        txtContenido.setEditable(false);
        txtContenido.setFont(new Font("Arial", Font.PLAIN, 13));
        txtContenido.setBackground(Color.WHITE);
        txtContenido.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

        JScrollPane scrollContenido = new JScrollPane(txtContenido);
        scrollContenido.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollContenido.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollContenido.setBorder(null);
        scrollContenido.setBackground(Color.WHITE);
        scrollContenido.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollContenido.setMaximumSize(new Dimension(Integer.MAX_VALUE, alturaMaxima));
        scrollContenido.setPreferredSize(new Dimension(600, Math.min(txtContenido.getPreferredSize().height + 10, alturaMaxima)));

        return scrollContenido;
    }

    // ============================================
// CREAR PANEL DE ACCIONES
// ============================================
    private JPanel crearPanelAcciones(Long idComentario, Long idUsuarioComentario, int numRespuestas,
                                      ComentarioContext context, JDialog ventanaActual) {
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelAcciones.setBackground(Color.WHITE);
        panelAcciones.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Botón "Ver respuestas"
        if (numRespuestas > 0) {
            JButton btnVerRespuestas = new JButton("Ver " + numRespuestas + " respuesta" + (numRespuestas > 1 ? "s" : ""));
            btnVerRespuestas.setFont(new Font("Arial", Font.BOLD, 12));
            btnVerRespuestas.setForeground(new Color(13, 110, 253));
            btnVerRespuestas.setBorderPainted(false);
            btnVerRespuestas.setContentAreaFilled(false);
            btnVerRespuestas.setFocusPainted(false);
            btnVerRespuestas.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            btnVerRespuestas.addActionListener(e -> {
                ventanaActual.dispose();
                // Navegar al siguiente nivel
                cargarYMostrarComentarios(context.idReporte, idComentario);
            });

            panelAcciones.add(btnVerRespuestas);
        } else {
            JLabel lblSinRespuestas = new JLabel("Sin respuestas");
            lblSinRespuestas.setFont(new Font("Arial", Font.ITALIC, 12));
            lblSinRespuestas.setForeground(Color.LIGHT_GRAY);
            panelAcciones.add(lblSinRespuestas);
        }

        // Botón "Responder"
        JButton btnResponder = new JButton("Responder");
        btnResponder.setFont(new Font("Arial", Font.PLAIN, 12));
        btnResponder.setForeground(new Color(108, 117, 125));
        btnResponder.setBorderPainted(false);
        btnResponder.setContentAreaFilled(false);
        btnResponder.setFocusPainted(false);
        btnResponder.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnResponder.addActionListener(e -> {
            ventanaActual.dispose();
            abrirVentanaCrearComentario(context.idReporte, idComentario, context);
        });

        panelAcciones.add(btnResponder);

        // Botones "Editar" y "Eliminar" (solo si el comentario es del usuario actual)
        if (idUsuarioComentario != null &&
                idUsuarioComentario.equals(usuarioLogueado.getIdusuario())) {

            // Separador visual
            JLabel lblSeparador = new JLabel(" • ");
            lblSeparador.setFont(new Font("Arial", Font.PLAIN, 12));
            lblSeparador.setForeground(Color.LIGHT_GRAY);
            panelAcciones.add(lblSeparador);

            // Botón "Editar"
            JButton btnEditar = new JButton("<html><font face='Segoe UI Emoji'> ✏ </font><font face='Arial'>Editar</font></html>");
            btnEditar.setFont(new Font("Arial", Font.PLAIN, 12));
            btnEditar.setForeground(new Color(13, 110, 253)); // Azul
            btnEditar.setBorderPainted(false);
            btnEditar.setContentAreaFilled(false);
            btnEditar.setFocusPainted(false);
            btnEditar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            // Efecto hover
            btnEditar.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    btnEditar.setForeground(new Color(10, 88, 202)); // Azul más oscuro
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    btnEditar.setForeground(new Color(13, 110, 253));
                }
            });

            btnEditar.addActionListener(e -> {
                ventanaActual.dispose();
                abrirVentanaEditarComentario(idComentario, context);
            });

            panelAcciones.add(btnEditar);

            // Separador visual
            JLabel lblSeparador2 = new JLabel(" • ");
            lblSeparador2.setFont(new Font("Arial", Font.PLAIN, 12));
            lblSeparador2.setForeground(Color.LIGHT_GRAY);
            panelAcciones.add(lblSeparador2);

            // Botón "Eliminar"
            JButton btnEliminar = new JButton("<html><font face='Segoe UI Emoji'> 🗑️ </font><font face='Arial'>Eliminar</font></html>");
            btnEliminar.setFont(new Font("Arial", Font.PLAIN, 12));
            btnEliminar.setForeground(new Color(220, 53, 69)); // Rojo
            btnEliminar.setBorderPainted(false);
            btnEliminar.setContentAreaFilled(false);
            btnEliminar.setFocusPainted(false);
            btnEliminar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            // Efecto hover
            btnEliminar.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    btnEliminar.setForeground(new Color(180, 30, 45)); // Rojo más oscuro
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    btnEliminar.setForeground(new Color(220, 53, 69));
                }
            });

            btnEliminar.addActionListener(e -> {
                // Confirmar eliminación
                int confirmacion = JOptionPane.showConfirmDialog(
                        ventanaActual,
                        "¿Estás seguro de que deseas eliminar este comentario?\n" +
                                (numRespuestas > 0 ? "ADVERTENCIA: Este comentario tiene " + numRespuestas +
                                        " respuesta" + (numRespuestas > 1 ? "s" : "") + " que también se eliminarán." : ""),
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                if (confirmacion == JOptionPane.YES_OPTION) {
                    eliminarComentario(idComentario, context, ventanaActual);
                }
            });

            panelAcciones.add(btnEliminar);
        }

        return panelAcciones;
    }

    // ============================================
// CONTAR RESPUESTAS
// ============================================
    private int contarRespuestas(List<?> todosLosComentarios, Long idComentario) {
        int count = 0;
        for (Object obj : todosLosComentarios) {
            if (obj instanceof Map<?, ?> comentario) {
                Object idPadre = comentario.get("idcomentariopadre");
                if (idPadre != null && ((Number) idPadre).longValue() == idComentario) {
                    count++;
                }
            }
        }
        return count;
    }

    // ============================================
// VENTANA CREAR COMENTARIO (REFACTORIZADO)
// ============================================
    private void abrirVentanaCrearComentario(Long idReporte, Long idComentarioPadre, ComentarioContext context) {
        String nombreUsuario = usuarioLogueado.getNombreusuario() != null
                ? usuarioLogueado.getNombreusuario() : "Usuario";
        Boolean esVerificado = usuarioLogueado.getEmpleadogubverificado() != null
                ? usuarioLogueado.getEmpleadogubverificado() : false;

        JDialog ventanaCrear = new JDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                idComentarioPadre == null ? "Nuevo Comentario" : "Nueva Respuesta",
                true
        );
        ventanaCrear.setSize(700, 400);
        ventanaCrear.setLocationRelativeTo(this);
        ventanaCrear.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel nombre
        JPanel panelNombre = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelNombre.setBackground(Color.WHITE);

        JLabel lblNombreUsuario = new JLabel(nombreUsuario + " ");
        lblNombreUsuario.setFont(new Font("Arial", Font.BOLD, 13));
        panelNombre.add(lblNombreUsuario);

        if (esVerificado) {
            JLabel lblVerificado = new JLabel("<html><font face='Segoe UI Emoji'> ✓ </font><font face='Arial'>Gubernamental</font></html>");
            lblVerificado.setFont(new Font("Arial", Font.BOLD, 13));
            lblVerificado.setForeground(new Color(25, 135, 84));
            panelNombre.add(lblVerificado);
        }

        // Área de texto
        JTextArea txtContenido = new JTextArea();
        txtContenido.setLineWrap(true);
        txtContenido.setWrapStyleWord(true);
        txtContenido.setFont(new Font("Arial", Font.PLAIN, 14));
        txtContenido.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        txtContenido.setBackground(new Color(248, 249, 250));

        JScrollPane scrollContenido = new JScrollPane(txtContenido);
        scrollContenido.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // Panel inferior con botones
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelInferior.setBackground(Color.WHITE);

        JLabel lblContador = new JLabel("0 caracteres");
        lblContador.setFont(new Font("Arial", Font.PLAIN, 12));
        lblContador.setForeground(Color.GRAY);

        txtContenido.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { actualizar(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { actualizar(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { actualizar(); }

            private void actualizar() {
                int longitud = txtContenido.getText().length();
                lblContador.setText(longitud + " caracteres");
                lblContador.setForeground(longitud > 1000 ? Color.RED : Color.GRAY);
            }
        });

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Arial", Font.PLAIN, 13));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(e -> {
            ventanaCrear.dispose();
            // Volver a la ventana anterior
            cargarYMostrarComentarios(context.idReporte, context.idComentarioPadre);
        });

        JButton btnEnviar = new JButton("<html><font face='Arial'>Enviar</font><font face='Segoe UI Emoji'> ➤ </font></html>");
        btnEnviar.setFont(new Font("Arial", Font.BOLD, 13));
        btnEnviar.setBackground(new Color(13, 110, 253));
        btnEnviar.setForeground(Color.WHITE);
        btnEnviar.setFocusPainted(false);
        btnEnviar.setBorderPainted(false);
        btnEnviar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnEnviar.setPreferredSize(new Dimension(120, 35));

        btnEnviar.addActionListener(e -> {
            String contenido = txtContenido.getText().trim();

            if (contenido.isEmpty()) {
                JOptionPane.showMessageDialog(ventanaCrear,
                        "El contenido del comentario no puede estar vacío",
                        "Campo requerido",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (contenido.length() > 1000) {
                JOptionPane.showMessageDialog(ventanaCrear,
                        "El comentario no puede exceder los 1000 caracteres",
                        "Contenido muy largo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            btnEnviar.setEnabled(false);
            btnEnviar.setText("Enviando...");

            enviarComentario(idReporte, idComentarioPadre, contenido, ventanaCrear, btnEnviar, context);
        });

        panelInferior.add(lblContador);
        panelInferior.add(Box.createHorizontalStrut(10));
        panelInferior.add(btnCancelar);
        panelInferior.add(btnEnviar);

        panelPrincipal.add(panelNombre, BorderLayout.NORTH);
        panelPrincipal.add(scrollContenido, BorderLayout.CENTER);
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        ventanaCrear.add(panelPrincipal);
        ventanaCrear.setVisible(true);
    }

    // ============================================
// ENVIAR COMENTARIO (REFACTORIZADO)
// ============================================
    private void enviarComentario(Long idReporte, Long idComentarioPadre, String contenido,
                                  JDialog ventanaActual, JButton btnEnviar, ComentarioContext context) {
        new Thread(() -> {
            try {
                ClienteAPI api = new ClienteAPI();
                ApiResponse<?> response = api.crearComentario(
                        usuarioLogueado.getIdusuario(),
                        idReporte,
                        idComentarioPadre,
                        contenido
                );

                SwingUtilities.invokeLater(() -> {
                    if (response != null && response.isSuccess()) {
                        JOptionPane.showMessageDialog(ventanaActual,
                                "Comentario enviado exitosamente",
                                "Éxito",
                                JOptionPane.INFORMATION_MESSAGE);

                        ventanaActual.dispose();

                        // Recargar la ventana del nivel actual
                        cargarYMostrarComentarios(context.idReporte, context.idComentarioPadre);
                    } else {
                        btnEnviar.setEnabled(true);
                        btnEnviar.setText("<html><font face='Arial'>Enviar</font><font face='Segoe UI Emoji'> ➤ </font></html>");
                        JOptionPane.showMessageDialog(ventanaActual,
                                "Error al enviar el comentario: " +
                                        (response != null ? response.getMensaje() : "Error desconocido"),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    btnEnviar.setEnabled(true);
                    btnEnviar.setText("Enviar ➤");
                    JOptionPane.showMessageDialog(ventanaActual,
                            "Error al conectar con el servidor: " + ex.getMessage(),
                            "Error de Conexión",
                            JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }

    // ============================================
// VENTANA EDITAR COMENTARIO
// ============================================
    private void abrirVentanaEditarComentario(Long idComentario, ComentarioContext context) {
        // Primero, obtener el contenido actual del comentario
        String contenidoActual = "";
        Long idReporte = context.idReporte;
        Long idComentarioPadre = null;
        
        // Buscar el comentario en la lista para obtener su contenido
        for (Object obj : context.todosLosComentarios) {
            if (obj instanceof Map<?, ?> comentarioMap) {
                Object idComentarioObj = comentarioMap.get("idcomentario");
                if (idComentarioObj != null && ((Number) idComentarioObj).longValue() == idComentario) {
                    contenidoActual = comentarioMap.get("contenido") != null
                        ? comentarioMap.get("contenido").toString() : "";
                    Object idPadreObj = comentarioMap.get("idcomentariopadre");
                    idComentarioPadre = idPadreObj != null ? ((Number) idPadreObj).longValue() : null;
                    break;
                }
            }
        }

        String nombreUsuario = usuarioLogueado.getNombreusuario() != null
                ? usuarioLogueado.getNombreusuario() : "Usuario";
        Boolean esVerificado = usuarioLogueado.getEmpleadogubverificado() != null
                ? usuarioLogueado.getEmpleadogubverificado() : false;

        JDialog ventanaEditar = new JDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                "Editar Comentario",
                true
        );
        ventanaEditar.setSize(700, 400);
        ventanaEditar.setLocationRelativeTo(this);
        ventanaEditar.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel nombre
        JPanel panelNombre = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelNombre.setBackground(Color.WHITE);

        JLabel lblNombreUsuario = new JLabel(nombreUsuario + " ");
        lblNombreUsuario.setFont(new Font("Arial", Font.BOLD, 13));
        panelNombre.add(lblNombreUsuario);

        if (esVerificado) {
            JLabel lblVerificado = new JLabel("<html><font face='Segoe UI Emoji'> ✓ </font><font face='Arial'>Gubernamental</font></html>");
            lblVerificado.setFont(new Font("Arial", Font.BOLD, 13));
            lblVerificado.setForeground(new Color(25, 135, 84));
            panelNombre.add(lblVerificado);
        }

        // Área de texto con contenido actual
        JTextArea txtContenido = new JTextArea(contenidoActual);
        txtContenido.setLineWrap(true);
        txtContenido.setWrapStyleWord(true);
        txtContenido.setFont(new Font("Arial", Font.PLAIN, 14));
        txtContenido.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        txtContenido.setBackground(new Color(248, 249, 250));

        JScrollPane scrollContenido = new JScrollPane(txtContenido);
        scrollContenido.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // Panel inferior con botones
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelInferior.setBackground(Color.WHITE);

        JLabel lblContador = new JLabel(contenidoActual.length() + " caracteres");
        lblContador.setFont(new Font("Arial", Font.PLAIN, 12));
        lblContador.setForeground(contenidoActual.length() > 1000 ? Color.RED : Color.GRAY);

        txtContenido.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { actualizar(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { actualizar(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { actualizar(); }

            private void actualizar() {
                int longitud = txtContenido.getText().length();
                lblContador.setText(longitud + " caracteres");
                lblContador.setForeground(longitud > 1000 ? Color.RED : Color.GRAY);
            }
        });

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Arial", Font.PLAIN, 13));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(e -> {
            ventanaEditar.dispose();
            // Volver a la ventana anterior
            cargarYMostrarComentarios(context.idReporte, context.idComentarioPadre);
        });

        JButton btnGuardar = new JButton("<html><font face='Arial'>Guardar</font><font face='Segoe UI Emoji'> ✓ </font></html>");
        btnGuardar.setFont(new Font("Arial", Font.BOLD, 13));
        btnGuardar.setBackground(new Color(25, 135, 84));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setBorderPainted(false);
        btnGuardar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnGuardar.setPreferredSize(new Dimension(120, 35));

        final Long idComentarioPadreFinal = idComentarioPadre;

        btnGuardar.addActionListener(e -> {
            String contenido = txtContenido.getText().trim();

            if (contenido.isEmpty()) {
                JOptionPane.showMessageDialog(ventanaEditar,
                        "El contenido del comentario no puede estar vacío",
                        "Campo requerido",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (contenido.length() > 1000) {
                JOptionPane.showMessageDialog(ventanaEditar,
                        "El comentario no puede exceder los 1000 caracteres",
                        "Contenido muy largo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            btnGuardar.setEnabled(false);
            btnGuardar.setText("Guardando...");

            actualizarComentario(idComentario, idReporte, idComentarioPadreFinal, contenido, ventanaEditar, btnGuardar, context);
        });

        panelInferior.add(lblContador);
        panelInferior.add(Box.createHorizontalStrut(10));
        panelInferior.add(btnCancelar);
        panelInferior.add(btnGuardar);

        panelPrincipal.add(panelNombre, BorderLayout.NORTH);
        panelPrincipal.add(scrollContenido, BorderLayout.CENTER);
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        ventanaEditar.add(panelPrincipal);
        ventanaEditar.setVisible(true);
    }

    // ============================================
// ACTUALIZAR COMENTARIO
// ============================================
    private void actualizarComentario(Long idComentario, Long idReporte, Long idComentarioPadre,
                                      String contenido, JDialog ventanaActual, JButton btnGuardar,
                                      ComentarioContext context) {
        new Thread(() -> {
            try {
                ClienteAPI api = new ClienteAPI();
                ApiResponse<?> response = api.actualizarComentario(
                        idComentario,
                        usuarioLogueado.getIdusuario(),
                        idReporte,
                        idComentarioPadre,
                        contenido
                );

                SwingUtilities.invokeLater(() -> {
                    if (response != null && response.isSuccess()) {
                        JOptionPane.showMessageDialog(ventanaActual,
                                "Comentario actualizado exitosamente",
                                "Éxito",
                                JOptionPane.INFORMATION_MESSAGE);

                        ventanaActual.dispose();

                        // Recargar la ventana del nivel actual
                        cargarYMostrarComentarios(context.idReporte, context.idComentarioPadre);
                    } else {
                        btnGuardar.setEnabled(true);
                        btnGuardar.setText("<html><font face='Arial'>Guardar</font><font face='Segoe UI Emoji'> ✓ </font></html>");
                        JOptionPane.showMessageDialog(ventanaActual,
                                "Error al actualizar el comentario: " +
                                        (response != null ? response.getMensaje() : "Error desconocido"),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    btnGuardar.setEnabled(true);
                    btnGuardar.setText("<html><font face='Arial'>Guardar</font><font face='Segoe UI Emoji'> ✓ </font></html>");
                    JOptionPane.showMessageDialog(ventanaActual,
                            "Error al conectar con el servidor: " + ex.getMessage(),
                            "Error de Conexión",
                            JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }

    // --------------------------------

    // Métod0 auxiliar para obtener el nombre del usuario
    private String obtenerNombreUsuario(Long idUsuario) {
        try {
            ClienteAPI api = new ClienteAPI();
            ApiResponse<?> response = api.getUsuarioPorId(idUsuario);

            if (response != null && response.isSuccess() && response.getData() != null) {
                if (response.getData() instanceof Map<?, ?> usuarioMap) {
                    Object nombreObj = usuarioMap.get("nombreusuario");
                    if (nombreObj != null) {
                        return nombreObj.toString();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error al obtener usuario: " + e.getMessage());
        }
        return "Usuario #" + idUsuario; // Fallback si falla la llamada
    }

    private JPanel crearCampoInfo(String etiqueta, String valor) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(248, 249, 250));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblEtiqueta = new JLabel(etiqueta);
        lblEtiqueta.setFont(new Font("Arial", Font.BOLD, 11));
        lblEtiqueta.setForeground(new Color(100, 100, 100));
        lblEtiqueta.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea txtValor = new JTextArea(valor);
        txtValor.setFont(new Font("Arial", Font.PLAIN, 11));
        txtValor.setForeground(new Color(50, 50, 50));
        txtValor.setLineWrap(true);
        txtValor.setWrapStyleWord(true);
        txtValor.setEditable(false);
        txtValor.setBackground(new Color(248, 249, 250));
        txtValor.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
        txtValor.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(lblEtiqueta);
        panel.add(txtValor);

        return panel;
    }

    private void votar(int index) {
        new Thread(() -> {
            try {
                ClienteAPI api = new ClienteAPI();
                ItemReporte item = itemsVisibles.get(index);
                Map<String, Object> reporte = item.getData();

                Object idObj = reporte.get("idreporte");
                if (idObj == null) {
                    System.err.println("Error: No se encontró idreporte en el mapa");
                    return;
                }

                Long idReporte = ((Number) idObj).longValue();

                ApiResponse<?> response = api.votarReporte(idReporte, usuarioLogueado.getIdusuario());

                if ("OK".equals(response.getStatus())) {
                    SwingUtilities.invokeLater(() -> {
                        // Actualizar el contador visualmente
                        long votosActuales = ((Number) reporte.get("totalvotos")).longValue();
                        long nuevosVotos = votosActuales + 1;

                        item.getLblVotos().setText(String.valueOf(nuevosVotos));

                        // Actualizar el estado en el mapa
                        reporte.put("totalvotos", nuevosVotos);
                        reporte.put("yaVoto", true);

                        reportesVotadosPorUsuario.add(idReporte);

                        // Forzar repaint
                        item.getLblVotos().getParent().revalidate();
                        item.getLblVotos().getParent().repaint();
                    });
                } else {
                    System.err.println("Error al votar: " + response.getMensaje());
                    SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(this,
                                    "Error al votar: " + response.getMensaje(),
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE)
                    );
                }

            } catch (Exception ex) {
                System.err.println("Error al votar: " + ex.getMessage());
                System.out.println(ex.getMessage());
            }
        }).start();
    }

    private void quitarVoto(int index) {
        new Thread(() -> {
            try {
                ClienteAPI api = new ClienteAPI();
                ItemReporte item = itemsVisibles.get(index);
                Map<String, Object> reporte = item.getData();

                Object idObj = reporte.get("idreporte");
                if (idObj == null) {
                    System.err.println("Error: No se encontró idreporte en el mapa");
                    return;
                }

                Long idReporte = ((Number) idObj).longValue();

                ApiResponse<?> response = api.quitarVotoReporte(idReporte, usuarioLogueado.getIdusuario());

                if ("OK".equals(response.getStatus())) {
                    SwingUtilities.invokeLater(() -> {
                        // Actualizar el contador visualmente
                        long votosActuales = ((Number) reporte.get("totalvotos")).longValue();
                        long nuevosVotos = Math.max(0, votosActuales - 1); // No permitir negativos

                        item.getLblVotos().setText(String.valueOf(nuevosVotos));

                        // Actualizar el estado en el mapa
                        reporte.put("totalvotos", nuevosVotos);
                        reporte.put("yaVoto", false);

                        reportesVotadosPorUsuario.remove(idReporte);

                        // Forzar repaint
                        item.getLblVotos().getParent().revalidate();
                        item.getLblVotos().getParent().repaint();
                    });
                } else {
                    System.err.println("Error al quitar voto: " + response.getMensaje());
                }

            } catch (Exception ex) {
                System.err.println("Error al quitar voto: " + ex.getMessage());
                System.out.println(ex.getMessage());
            }
        }).start();
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