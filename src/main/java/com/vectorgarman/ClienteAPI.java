package com.vectorgarman;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.Usuario;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

public class ClienteAPI {
    public Usuario getUsuarioPorId(Long idusuario) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/api/usuarios/obtener/" + idusuario))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        // Gson capaz de parsear LocalDate
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class,
                        (com.google.gson.JsonDeserializer<LocalDate>) (json, t, ctx) ->
                                LocalDate.parse(json.getAsString())
                )
                .create();

        // Mapear al DTO Usuario
        return gson.fromJson(response.body(), Usuario.class);
    }
}
