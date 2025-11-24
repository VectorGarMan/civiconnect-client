package com.vectorgarman.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;
import com.vectorgarman.dto.ApiResponse;
import com.vectorgarman.dto.Usuario;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

public class ClienteAPI {
    public ApiResponse<?> getUsuarioPorId(Long idusuario) throws Exception {
        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/usuarios/obtener/" + idusuario))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
        }

        // Gson capaz de parsear LocalDate
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class,
                        (com.google.gson.JsonDeserializer<LocalDate>) (json, t, ctx) ->
                                LocalDate.parse(json.getAsString())
                )
                .setObjectToNumberStrategy(ToNumberPolicy.LAZILY_PARSED_NUMBER)
                .create();

        return gson.fromJson(response.body(), ApiResponse.class);
    }
}
