package com.vectorgarman.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;
import com.vectorgarman.dto.*;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ClienteAPI {
<<<<<<< HEAD
        private static final String BASE_URL = "http://localhost:8080/api";
//    private static final String BASE_URL = "https://civiconnect-api.onrender.com/api";
=======

    private static final String BASE_URL = "http://localhost:8080/api";
    // private static final String BASE_URL = "https://civiconnect-api.onrender.com/api";
>>>>>>> 0b465f7 (se corrigió el problema cuando se edita un reporte, no cargaba su ubicacion, cargaba la ubicacion del usuario logeado)

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class,
                    (com.google.gson.JsonDeserializer<LocalDate>) (json, t, ctx) ->
                            LocalDate.parse(json.getAsString())
            )
            .setObjectToNumberStrategy(ToNumberPolicy.LAZILY_PARSED_NUMBER)
            .create();

    private static final HttpClient httpClient = HttpClient.newHttpClient();

    /**
     * Autentica un usuario con sus credenciales de email y contraseña.
     */
    public ApiResponse<?> login(String email, String contrasena) throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setContrasena(contrasena);

        String jsonBody = gson.toJson(loginRequest);

        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/usuarios/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
        }

        return gson.fromJson(response.body(), ApiResponse.class);
    }

    /**
     * Crea un nuevo comentario en un reporte, puede ser comentario principal o respuesta.
     */
    public ApiResponse<?> crearComentario(Long idusuario, Long idreporte, Long idcomentariopadre, String contenido) throws Exception {
        ComentarioRequest comentarioRequest = new ComentarioRequest();
        comentarioRequest.setIdusuario(idusuario);
        comentarioRequest.setIdreporte(idreporte);
        comentarioRequest.setIdcomentariopadre(idcomentariopadre);
        comentarioRequest.setContenido(contenido);

        String jsonBody = gson.toJson(comentarioRequest);

        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/reporte/comentario/crearActualizar"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
        }

        return gson.fromJson(response.body(), ApiResponse.class);
    }

    /**
     * Obtiene todas las evidencias asociadas a un reporte específico.
     */
    public ApiResponse<?> obtenerEvidenciaPorReporte(Long idreporte) throws Exception {
        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/reporte/evidencia/obtenerPorReporte/" + idreporte))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
        }

        return gson.fromJson(response.body(), ApiResponse.class);
    }

    /**
     * Obtiene el catálogo de categorías disponibles para clasificar reportes.
     */
    public ApiResponse<?> obtenerCategoriasDeReporte() throws Exception {
        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/reporte/categoria/obtener"))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
        }

        return gson.fromJson(response.body(), ApiResponse.class);
    }

    /**
     * Obtiene el catálogo de estados posibles para un reporte (pendiente, en proceso, resuelto, etc.).
     */
    public ApiResponse<?> obtenerEstadosDeReporte() throws Exception {
        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/reporte/estadoReporte/obtener"))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
        }

        return gson.fromJson(response.body(), ApiResponse.class);
    }

    /**
     * Obtiene el catálogo de niveles de prioridad para clasificar la urgencia de los reportes.
     */
    public ApiResponse<?> obtenerNivelesDePrioridad() throws Exception {
        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/reporte/nivelPrioridad/obtener"))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
        }

        return gson.fromJson(response.body(), ApiResponse.class);
    }

    /**
     * Actualiza un reporte existente con nueva información, evidencias adicionales y eliminación de evidencias.
     */
    public ApiResponse<?> editarReporte(
            Long idreporte, Long idusuario, Long idcolonia, Long idnivelprioridad,
            Long idestadoreporte, Long idcategoria, String titulo,
            String descripcion, String solucionpropuesta, String calle,
            String referencia, List<File> evidenciasAgregar,
            List<Long> evidenciasIdsEliminar) throws Exception {

        EditarReporteRequest requestData = new EditarReporteRequest();
        requestData.setIdreporte(idreporte);
        requestData.setIdusuario(idusuario);
        requestData.setIdcolonia(idcolonia);
        requestData.setIdnivelprioridad(idnivelprioridad);
        requestData.setIdestadoreporte(idestadoreporte);
        requestData.setIdcategoria(idcategoria);
        requestData.setTitulo(titulo);
        requestData.setDescripcion(descripcion);
        requestData.setSolucionpropuesta(solucionpropuesta);
        requestData.setCalle(calle);
        requestData.setReferencia(referencia);

        String jsonReporte = gson.toJson(requestData);
        String jsonEvidenciasEliminar = gson.toJson(evidenciasIdsEliminar);

        String boundary = "----CiviConnectBoundary" + System.currentTimeMillis();
        var parts = new ArrayList<byte[]>();

        parts.add((
                "--" + boundary + "\r\n" +
                        "Content-Disposition: form-data; name=\"reporte\"\r\n" +
                        "Content-Type: application/json\r\n\r\n" +
                        jsonReporte + "\r\n"
        ).getBytes());

        parts.add((
                "--" + boundary + "\r\n" +
                        "Content-Disposition: form-data; name=\"evidenciasIdsEliminar\"\r\n" +
                        "Content-Type: application/json\r\n\r\n" +
                        jsonEvidenciasEliminar + "\r\n"
        ).getBytes());

        if (evidenciasAgregar != null) {
            for (File evidencia : evidenciasAgregar) {
                parts.add((
                        "--" + boundary + "\r\n" +
                                "Content-Disposition: form-data; name=\"evidencia\"; filename=\"" + evidencia.getName() + "\"\r\n" +
                                "Content-Type: application/octet-stream\r\n\r\n"
                ).getBytes());

                parts.add(Files.readAllBytes(evidencia.toPath()));
                parts.add("\r\n".getBytes());
            }
        }

        parts.add(("--" + boundary + "--").getBytes());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "/reporte/crearActualizar"))
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(HttpRequest.BodyPublishers.ofByteArrays(parts))
                .build();

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        return gson.fromJson(response.body(), ApiResponse.class);
    }

    /**
     * Crea un nuevo reporte con su información básica y evidencias adjuntas.
     */
    public ApiResponse<?> crearReporte(
            Long idusuario, Long idcolonia, Long idnivelprioridad,
            Long idestadoreporte, Long idcategoria, String titulo,
            String descripcion, String solucionpropuesta, String calle,
            String referencia, List<File> evidencias) throws Exception {

        CrearReporteRequest crearReporteRequest = new CrearReporteRequest();
        crearReporteRequest.setIdusuario(idusuario);
        crearReporteRequest.setIdcolonia(idcolonia);
        crearReporteRequest.setIdnivelprioridad(idnivelprioridad);
        crearReporteRequest.setIdestadoreporte(idestadoreporte);
        crearReporteRequest.setIdcategoria(idcategoria);
        crearReporteRequest.setTitulo(titulo);
        crearReporteRequest.setDescripcion(descripcion);
        crearReporteRequest.setSolucionpropuesta(solucionpropuesta);
        crearReporteRequest.setCalle(calle);
        crearReporteRequest.setReferencia(referencia);

        String jsonReporte = gson.toJson(crearReporteRequest);

        String boundary = "----CiviConnectBoundary" + System.currentTimeMillis();

        var byteArrays = new ArrayList<byte[]>();

        byteArrays.add((
                "--" + boundary + "\r\n" +
                        "Content-Disposition: form-data; name=\"reporte\"\r\n" +
                        "Content-Type: application/json\r\n\r\n" +
                        jsonReporte + "\r\n"
        ).getBytes());

        for (File evidencia : evidencias) {
            byteArrays.add((
                    "--" + boundary + "\r\n" +
                            "Content-Disposition: form-data; name=\"evidencia\"; filename=\"" + evidencia.getName() + "\"\r\n" +
                            "Content-Type: application/octet-stream\r\n\r\n"
            ).getBytes());

            byteArrays.add(Files.readAllBytes(evidencia.toPath()));
            byteArrays.add("\r\n".getBytes());
        }

        byteArrays.add(("--" + boundary + "--").getBytes());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "/reporte/crearActualizar"))
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(HttpRequest.BodyPublishers.ofByteArrays(byteArrays))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return gson.fromJson(response.body(), ApiResponse.class);
    }

    /**
     * Modifica el contenido de un comentario existente.
     */
    public ApiResponse<?> actualizarComentario(Long idcomentario, Long idusuario, Long idreporte, Long idcomentariopadre, String contenido) throws Exception {
        EditarComentarioRequest editarComentarioRequest = new EditarComentarioRequest();
        editarComentarioRequest.setIdcomentario(idcomentario);
        editarComentarioRequest.setIdusuario(idusuario);
        editarComentarioRequest.setIdreporte(idreporte);
        editarComentarioRequest.setIdcomentariopadre(idcomentariopadre);
        editarComentarioRequest.setContenido(contenido);

        String jsonBody = gson.toJson(editarComentarioRequest);

        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/reporte/comentario/crearActualizar"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
        }

        return gson.fromJson(response.body(), ApiResponse.class);
    }

    /**
     * Registra un nuevo usuario en el sistema con sus datos básicos.
     */
    public ApiResponse<?> crearUsuario(Long idtipousuario, Long idcolonia, String email, String contrasena, String nombreusuario) throws Exception {
        UsuarioRequest usuarioRequest = new UsuarioRequest();
        usuarioRequest.setIdtipousuario(idtipousuario);
        usuarioRequest.setIdcolonia(idcolonia);
        usuarioRequest.setEmail(email);
        usuarioRequest.setContrasena(contrasena);
        usuarioRequest.setNombreusuario(nombreusuario);

        String jsonBody = gson.toJson(usuarioRequest);

        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/usuarios/crear"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
        }

        return gson.fromJson(response.body(), ApiResponse.class);
    }

    /**
     * Obtiene el catálogo de tipos de usuario disponibles en el sistema.
     */
    public ApiResponse<?> obtenerTiposDeUsuario() throws Exception {
        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/usuarios/tipoUsuario/obtener"))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
        }

        return gson.fromJson(response.body(), ApiResponse.class);
    }

    /**
     * Obtiene el catálogo de estados de la República Mexicana.
     */
    public ApiResponse<?> obtenerEstados() throws Exception {
        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/ubicacion/estado/obtener"))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
        }

        return gson.fromJson(response.body(), ApiResponse.class);
    }

    /**
     * Obtiene la lista completa de todos los reportes registrados en el sistema.
     */
    public ApiResponse<?> obtenerTodosLosReportes() throws Exception {
        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/reporte/obtener"))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
        }

        return gson.fromJson(response.body(), ApiResponse.class);
    }

    /**
     * Inicia el proceso de recuperación de contraseña enviando un token al email del usuario.
     */
    public ApiResponse<?> olvideContrasena(String email) throws Exception {
        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/usuarios/recuperarContrasena?email=" + email))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
        }

        return gson.fromJson(response.body(), ApiResponse.class);
    }

    /**
     * Cambia la contraseña de un usuario utilizando el token de recuperación.
     */
    public ApiResponse<?> cambiarContrasena(String token, String nuevaContrasena, String email) throws Exception {
        CambioContrasenaRequest cambioContrasenaRequest = new CambioContrasenaRequest();
        cambioContrasenaRequest.setToken(token);
        cambioContrasenaRequest.setNuevaContrasena(nuevaContrasena);
        cambioContrasenaRequest.setEmail(email);

        String jsonBody = gson.toJson(cambioContrasenaRequest);

        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/usuarios/cambiarContrasena"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
        }

        return gson.fromJson(response.body(), ApiResponse.class);
    }

    /**
     * Obtiene la lista de municipios pertenecientes a un estado específico.
     */
    public ApiResponse<?> obtenerMunicipios(Long idestado) throws Exception {
        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/ubicacion/municipio/obtenerPorEstado?idestado=" + idestado))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
        }

        return gson.fromJson(response.body(), ApiResponse.class);
    }

    /**
     * Obtiene la información de ubicación completa de un usuario específico.
     */
    public ApiResponse<?> obtenerUbicacionPorIdUsuario(Long idusuario) throws Exception {
        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/usuarios/ubicacion/" + idusuario))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
        }

        return gson.fromJson(response.body(), ApiResponse.class);
    }

    /**
     * Registra el voto de un usuario a favor de un reporte.
     */
    public ApiResponse<?> votarReporte(Long idReporte, Long idUsuario) throws Exception {
        VotarReporteRequest requestBody = new VotarReporteRequest();
        requestBody.setIdreporte(idReporte);
        requestBody.setIdusuario(idUsuario);

        String jsonBody = gson.toJson(requestBody);

        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/reporte/votar"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
        }

        return gson.fromJson(response.body(), ApiResponse.class);
    }

    /**
     * Elimina el voto previamente registrado por un usuario en un reporte.
     */
    public ApiResponse<?> quitarVotoReporte(Long idReporte, Long idUsuario) throws Exception {
        VotarReporteRequest requestBody = new VotarReporteRequest();
        requestBody.setIdreporte(idReporte);
        requestBody.setIdusuario(idUsuario);

        String jsonBody = gson.toJson(requestBody);

        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/reporte/eliminarVoto"))
                    .header("Content-Type", "application/json")
                    .method("DELETE", HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
        }

        return gson.fromJson(response.body(), ApiResponse.class);
    }

    /**
     * Elimina un comentario específico del sistema.
     */
    public ApiResponse<?> eliminarComentario(Long idcomentario, Long idusuario) throws Exception {
        EliminarComentarioRequest eliminarComentarioRequest = new EliminarComentarioRequest();
        eliminarComentarioRequest.setIdcomentario(idcomentario);
        eliminarComentarioRequest.setIdusuario(idusuario);

        String jsonBody = gson.toJson(eliminarComentarioRequest);

        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/reporte/eliminarComentario"))
                    .header("Content-Type", "application/json")
                    .method("DELETE", HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
        }

        return gson.fromJson(response.body(), ApiResponse.class);
    }

    /**
     * Obtiene la lista de reportes que han sido votados por un usuario específico.
     */
    public ApiResponse<?> obtenerReportesVotadosPorUsuario(Long idUsuario) throws Exception {
        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/reporte/obtenerVotadoPorUsuario/" + idUsuario))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
        }

        return gson.fromJson(response.body(), ApiResponse.class);
    }

    /**
     * Obtiene todos los reportes creados por un usuario específico.
     */
    public ApiResponse<?> obtenerReportesPorIdUsuario(Long idUsuario) throws Exception {
        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/reporte/obtenerPorIdUsuario/" + idUsuario))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
        }

        return gson.fromJson(response.body(), ApiResponse.class);
    }

    /**
     * Obtiene todos los comentarios realizados por un usuario específico.
     */
    public ApiResponse<?> obtenerComentariosPorIdUsuario(Long idUsuario) throws Exception {
        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/reporte/comentario/obtenerPorUsuario/" + idUsuario))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
        }

        return gson.fromJson(response.body(), ApiResponse.class);
    }

    /**
     * Obtiene todos los comentarios asociados a un reporte específico.
     */
    public ApiResponse<?> obtenerComentariosPorReporte(Long idReporte) throws Exception {
        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/reporte/comentario/obtenerPorReporte/" + idReporte))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
        }

        return gson.fromJson(response.body(), ApiResponse.class);
    }

    /**
     * Obtiene la lista de colonias pertenecientes a un municipio específico.
     */
    public ApiResponse<?> obtenerColonia(Long idmunicipio) throws Exception {
        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/ubicacion/colonia/obtenerPorMunicipio?idmunicipio=" + idmunicipio))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
        }

        return gson.fromJson(response.body(), ApiResponse.class);
    }

    /**
     * Obtiene la información completa de un usuario por su identificador.
     */
    public ApiResponse<?> getUsuarioPorId(Long idusuario) throws Exception {
        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/usuarios/obtener/" + idusuario))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
        }

        return gson.fromJson(response.body(), ApiResponse.class);
    }

    /**
     * Obtiene la información detallada de un reporte específico con datos relacionados.
     */
    public ApiResponse<?> obtenerReportePorId(Long idreporte) throws Exception {
        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/reporte/obtenerPorId/" + idreporte))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
        }

        return gson.fromJson(response.body(), ApiResponse.class);
    }

    /**
     * Obtiene la entidad completa de un reporte con todos sus identificadores de relaciones.
     */
    public ApiResponse<?> obtenerEntidadPorId(Long idreporte) throws Exception {
        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/reporte/obtenerEntidadPorId/" + idreporte))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
        }

        return gson.fromJson(response.body(), ApiResponse.class);
    }

    /**
     * Actualiza el nombre de usuario y la colonia de un usuario existente.
     */
    public ApiResponse<?> actualizarNombreUsuario(Long idusuario, String nuevoNombreUsuario, Long idcolonia) throws Exception {
        ActualizarNombreUsuarioRequest request = new ActualizarNombreUsuarioRequest();
        request.setIdusuario(idusuario);
        request.setNuevoNombreUsuario(nuevoNombreUsuario);
        request.setIdcolonia(idcolonia);

        String jsonBody = gson.toJson(request);

        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/usuarios/actualizarNombreUsuario"))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            response = client.send(
                    httpRequest,
                    HttpResponse.BodyHandlers.ofString()
            );
        }

        return gson.fromJson(response.body(), ApiResponse.class);
    }
}