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
    private final Gson gson = new Gson();

    public ClientKVS() {
        URI uri = URI.create("http://localhost:8078/register");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        try {
            this.token = String.valueOf(kvServerClient.send(request, HttpResponse.BodyHandlers.ofString()));
        } catch (IOException | InterruptedException e) {
            throw new KVSException("Ошибка при работе с сервером", e);
        }
    }

    public void serverSave(String key, String jsonValue) {
        URI uri = URI.create(" http://localhost:8078/save/" + key + "?API_TOKEN=" + token);

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.noBody()) //е помню что здесь
                .uri(uri)
                .build();
        try {
            HttpResponse<String> response = kvServerClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new KVSException("Ошибка при работе с сервером", e);
        }

    }

    public String serverLoad(String key) {
        URI uri = URI.create(" http://localhost:8078/load/" + key + "?API_TOKEN=" + token);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        try {
            HttpResponse<String> response = kvServerClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.toString();
        } catch (IOException | InterruptedException e) {
            throw new KVSException("Ошибка при работе с сервером", e);
        }


    }

    class TaskManagerKV {
        ClientKVS client = new ClientKVS();

        public void save() {
            client.serverSave("tasks", ""); //значение это список всех задач в формате джейсон
            client.serverSave("epics", ""); //значение это список всех эпиков в формате джейсон
            client.serverSave("subtasks", ""); //значение это список всех подзадач в формате джейсон
            client.serverSave("history", ""); //значение это список всех историй в формате джейсон
        }

        public void load() {
            String tasks = client.serverLoad("tasks");

          //  gson.fromJson(tasks, List <Task>);
        }

    }
}
