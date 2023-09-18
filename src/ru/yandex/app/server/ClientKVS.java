package ru.yandex.app.server;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ClientKVS {
    private final HttpClient kvServerClient = HttpClient.newHttpClient();
    private final String token;
    private final String url = "http://localhost:8078";

    public ClientKVS() {
        URI uri = URI.create(url + "/register");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri).version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> response = null;
        try {
            response = kvServerClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new KVSException("Не удалось обработать запрос");
            }
            this.token = response.body();
        } catch (IOException | InterruptedException e) {
            throw new KVSException("Ошибка при работе с сервером1", e);
        }
    }

    public void serverSave(String key, String jsonValue) {
        URI uri = URI.create(url + "/save/" + key + "?API_TOKEN=" + token); //"/?API_TOKEN="

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonValue)) //е помню что здесь
                .uri(uri).version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();
        try {
            HttpResponse<String> response = kvServerClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new KVSException("Не удалось обработать запрос");
            }
        } catch (IOException | InterruptedException e) {
            throw new KVSException("Ошибка при работе с сервером", e);
        }

    }

    public String serverLoad(String key) {
        URI uri = URI.create(url + "/load/" + key + "?API_TOKEN=" + token);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri).version(HttpClient.Version.HTTP_1_1)
                .build();
        try {
            HttpResponse<String> response = kvServerClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new KVSException("Не удалось обработать запрос");
            }
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new KVSException("Ошибка при работе с сервером", e);
        }


    }
}
