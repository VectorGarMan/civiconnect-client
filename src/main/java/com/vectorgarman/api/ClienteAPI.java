package com.vectorgarman.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;
import com.vectorgarman.dto.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

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

    public ApiResponse<?> crearActualizarComentario(Long idusuario, Long idreporte, Long idcomentariopadre, String contenido) throws Exception {
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

    public ApiResponse<?> obtenerComentariosPorComentarioPadre(Long idComentarioPadre) throws Exception {
        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/reporte/comentario/obtenerPorComentarioPadre/" + idComentarioPadre))
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