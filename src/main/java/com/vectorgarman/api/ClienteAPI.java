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

    private static final String BASE_URL = "http://localhost:8080/api";

//    private static final String BASE_URL = "https://civiconnect-api.onrender.com/api";

    // Gson configurado para reutilizar
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class,
                    (com.google.gson.JsonDeserializer<LocalDate>) (json, t, ctx) ->
                            LocalDate.parse(json.getAsString())
            )
            .setObjectToNumberStrategy(ToNumberPolicy.LAZILY_PARSED_NUMBER)
            .create();

    private static final HttpClient httpClient = HttpClient.newHttpClient();

    /**
     * Realiza el login del usuario
     *
     * @param email      Email del usuario
     * @param contrasena Contraseña del usuario
     * @return ApiResponse con el usuario si las credenciales son correctas
     * @throws Exception Si hay algún error en la comunicación
     */
    public ApiResponse<?> login(String email, String contrasena) throws Exception {
        // Crear el objeto LoginRequest
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setContrasena(contrasena);

        // Convertir el objeto a JSON
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

        // Parsear la respuesta
        return gson.fromJson(response.body(), ApiResponse.class);
    }

    public ApiResponse<?> crearComentario(Long idusuario, Long idreporte, Long idcomentariopadre, String contenido) throws Exception {
        // Crear el objeto LoginRequest
        ComentarioRequest comentarioRequest = new ComentarioRequest();
        comentarioRequest.setIdusuario(idusuario);
        comentarioRequest.setIdreporte(idreporte);
        comentarioRequest.setIdcomentariopadre(idcomentariopadre);
        comentarioRequest.setContenido(contenido);

        // Convertir el objeto a JSON
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

        // Parsear la respuesta
        return gson.fromJson(response.body(), ApiResponse.class);
    }

    // -----------------------------------------------------------------

    // Este metodo, sirve para obtener las evidencias de un solo reporte para cuando se va a editar uno.
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

        // Parsear la respuesta
        return gson.fromJson(response.body(), ApiResponse.class);
    }
    // Este metodo sirve para obtener las categorías que van a ir en un combobox al crear reporte o editarlo
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

    // Este metodo sirve para obtener los estados de reporte que van a ir en un combobox al crear reporte o editarlo
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

    // Este metodo sirve para obtener los niveles de prioridad de reporte que van a ir en un combobox al crear reporte o editarlo
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

    // Este metodo sirve para editar un reporte.
    public ApiResponse<?> editarReporte(
            Long idreporte, Long idusuario, Long idcolonia, Long idnivelprioridad,
            Long idestadoreporte, Long idcategoria, String titulo,
            String descripcion, String solucionpropuesta, String calle,
            String referencia, List<File> evidenciasAgregar,
            List<Long> evidenciasIdsEliminar) throws Exception {

        // 1) Construir el objeto reporte con idreporte
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

        // Parte del JSON reporte
        parts.add((
                "--" + boundary + "\r\n" +
                        "Content-Disposition: form-data; name=\"reporte\"\r\n" +
                        "Content-Type: application/json\r\n\r\n" +
                        jsonReporte + "\r\n"
        ).getBytes());

        // Parte de IDs de evidencias a eliminar
        parts.add((
                "--" + boundary + "\r\n" +
                        "Content-Disposition: form-data; name=\"evidenciasIdsEliminar\"\r\n" +
                        "Content-Type: application/json\r\n\r\n" +
                        jsonEvidenciasEliminar + "\r\n"
        ).getBytes());

        // Nuevas evidencias (si existen)
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

        // Cierre del multipart
        parts.add(("--" + boundary + "--").getBytes());

        // Request HTTP
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

    // Este metodo sirve para crear un reporte.
    public ApiResponse<?> crearReporte(
            Long idusuario, Long idcolonia, Long idnivelprioridad,
            Long idestadoreporte, Long idcategoria, String titulo,
            String descripcion, String solucionpropuesta, String calle,
            String referencia, List<File> evidencias) throws Exception {

        // 1) Construir el objeto JSON como hace tu curl
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

        // 2) Generar límites y construir multipart
        String boundary = "----CiviConnectBoundary" + System.currentTimeMillis();

        var byteArrays = new ArrayList<byte[]>();

        // Parte del JSON
        byteArrays.add((
                "--" + boundary + "\r\n" +
                        "Content-Disposition: form-data; name=\"reporte\"\r\n" +
                        "Content-Type: application/json\r\n\r\n" +
                        jsonReporte + "\r\n"
        ).getBytes());

        // 3) Adjuntar archivos evidencia
        for (File evidencia : evidencias) {
            byteArrays.add((
                    "--" + boundary + "\r\n" +
                            "Content-Disposition: form-data; name=\"evidencia\"; filename=\"" + evidencia.getName() + "\"\r\n" +
                            "Content-Type: application/octet-stream\r\n\r\n"
            ).getBytes());

            byteArrays.add(Files.readAllBytes(evidencia.toPath()));
            byteArrays.add("\r\n".getBytes());
        }

        // Cierre del multipart
        byteArrays.add(("--" + boundary + "--").getBytes());

        // 4) Crear request HTTP
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "/reporte/crearActualizar"))
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(HttpRequest.BodyPublishers.ofByteArrays(byteArrays))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return gson.fromJson(response.body(), ApiResponse.class);
    }

    // ------------------------------------------------------------

    public ApiResponse<?> actualizarComentario(Long idcomentario, Long idusuario, Long idreporte, Long idcomentariopadre, String contenido) throws Exception {
        // Crear el objeto LoginRequest

        EditarComentarioRequest editarComentarioRequest = new EditarComentarioRequest();
        editarComentarioRequest.setIdcomentario(idcomentario);
        editarComentarioRequest.setIdusuario(idusuario);
        editarComentarioRequest.setIdreporte(idreporte);
        editarComentarioRequest.setIdcomentariopadre(idcomentariopadre);
        editarComentarioRequest.setContenido(contenido);

        // Convertir el objeto a JSON
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

        // Parsear la respuesta
        return gson.fromJson(response.body(), ApiResponse.class);
    }

    public ApiResponse<?> crearUsuario(Long idtipousuario, Long idcolonia, String email, String contrasena, String nombreusuario) throws Exception {
        // Crear el objeto UsuarioRequest
        UsuarioRequest usuarioRequest = new UsuarioRequest();
        usuarioRequest.setIdtipousuario(idtipousuario);
        usuarioRequest.setIdcolonia(idcolonia);
        usuarioRequest.setEmail(email);
        usuarioRequest.setContrasena(contrasena);
        usuarioRequest.setNombreusuario(nombreusuario);

        // Convertir el objeto a JSON
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

        // Parsear la respuesta
        return gson.fromJson(response.body(), ApiResponse.class);
    }

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

        // Parsear la respuesta
        return gson.fromJson(response.body(), ApiResponse.class);
    }

    public ApiResponse<?> cambiarContrasena(String token, String nuevaContrasena, String email) throws Exception {
        // Crear el objeto UsuarioRequest
        CambioContrasenaRequest cambioContrasenaRequest = new CambioContrasenaRequest();
        cambioContrasenaRequest.setToken(token);
        cambioContrasenaRequest.setNuevaContrasena(nuevaContrasena);
        cambioContrasenaRequest.setEmail(email);

        // Convertir el objeto a JSON
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

        // Parsear la respuesta
        return gson.fromJson(response.body(), ApiResponse.class);
    }

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

        // Parsear la respuesta
        return gson.fromJson(response.body(), ApiResponse.class);
    }

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

        // Parsear la respuesta
        return gson.fromJson(response.body(), ApiResponse.class);
    }

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

        // Parsear la respuesta
        return gson.fromJson(response.body(), ApiResponse.class);
    }

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
}