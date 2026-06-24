package com.microservicio.gateway.core.standardConection;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class TokenConnection {

    public static boolean tokenValidation(String token) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder().
                uri(URI.create("http://localhost:8085/api/v1/token/{" + token + "}"))
                .GET()
                .timeout(Duration.ofHours(1))
                .build();
        HttpResponse<String> response = client.send(httpRequest,HttpResponse.BodyHandlers.ofString());
        return new Gson().fromJson(response.body(), Boolean.class);
    }
}
