package ru.yandex.app.server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.app.manager.FileBackedTasksManager;
import ru.yandex.app.manager.TaskManager;
import ru.yandex.app.tasks.Epic;
import ru.yandex.app.tasks.Subtask;
import ru.yandex.app.tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;


public class TaskHandler implements HttpHandler {
    private final Gson gson = new Gson();

    protected final FileBackedTasksManager fileBackedTasksManager;

    public TaskHandler(FileBackedTasksManager fileBackedTasksManager) {
        this.fileBackedTasksManager = fileBackedTasksManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {  //разбивка по http-методу
        String metod = exchange.getRequestMethod();
        switch (metod) {
            case ("GET"):
                handlerGetRequest(exchange);
                break;
            case ("POST"):
                handlerPostRequest(exchange);
                break;
            case ("DELETE"):
                handlerDeleteRequest(exchange);
                break;
            default:
                writeResponse("Неправильный http метод", exchange, 405);
                break;
        }
    }
    //разбивка повторому слешу таск,сабтаск, эпик, история и все по времени
    private void handlerGetRequest(HttpExchange exchange) throws IOException {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        String task = pathParts[2];
        System.out.println(task);
        switch (task) {
            case ("task"):
                handlerGetTaskRequest(exchange);
                break;
            case ("epic"):
                handlerGetEpicRequest(exchange);
                break;
            case ("subtask"):
                handlerGetSubtaskRequest(exchange);
                break;
            case ("history"):
                fileBackedTasksManager.getHistory();
                break;
            case (""):
                fileBackedTasksManager.getPrioritizedTasks();
            default:
                writeResponse("Неправильный адрес", exchange, 400);
                break;
        }
    }
    //гет таск,  по айди
    private void handlerGetTaskRequest(HttpExchange exchange) throws IOException {
        int id;
        String query = exchange.getRequestURI().getQuery();
        if (query.equals(null)) {
            fileBackedTasksManager.getAllTask();
            writeResponse("Задачи получены", exchange, 200);
            return;
        } else {
            try {
                String[] strId = (query).split("=");
                id = Integer.parseInt(strId[1]);
            } catch (NumberFormatException exception) {
                writeResponse("Некорректный идентификатор поста ", exchange, 400);
                return;
            }
            Task task = fileBackedTasksManager.getTask(id);
            if (task.getTitle().equals(null)) {
                writeResponse("Некорректный идентификатор поста", exchange, 400);
            }
            writeResponse("Задача получена", exchange, 200);
        }
    }
//гет эпик по айди
    private void handlerGetEpicRequest(HttpExchange exchange) throws IOException {
        int id;
        String query = exchange.getRequestURI().getQuery();
        if (query.equals(null)) {
            fileBackedTasksManager.getAllEpic();
            writeResponse("Задачи получены", exchange, 200);
            return;
        } else {
            try {
                String[] strId = (query).split("=");
                id = Integer.parseInt(strId[1]);
            } catch (NumberFormatException exception) {
                writeResponse("Некорректный идентификатор поста", exchange, 400);
                return;
            }
            Epic epic = fileBackedTasksManager.getEpic(id);
            if (epic.equals(null)) {
                writeResponse("Некорректный идентификатор поста", exchange, 400);
            }
            writeResponse("Задача получена", exchange, 200);
        }
    }
//гет сабтаск по айди
    private void handlerGetSubtaskRequest(HttpExchange exchange) throws IOException {
        int id;
        String[] path = exchange.getRequestURI().getPath().split("/");
        String query = exchange.getRequestURI().getQuery();
        if (query.equals(null)) {
            fileBackedTasksManager.getAllSubtask();
            writeResponse("Задачи получены", exchange, 200);
            return;
        } else {
            if (path.length == 3) {
                try {
                    String[] strId = (query).split("=");
                    id = Integer.parseInt(strId[1]);
                } catch (NumberFormatException exception) {
                    writeResponse("Некорректный идентификатор поста", exchange, 400);
                    return;
                }
                fileBackedTasksManager.getEpicSubtasks(id);
                writeResponse("Задача получена", exchange, 200);
                return;
            }
        }
        try {
            String[] strId = (query).split("=");
            id = Integer.parseInt(strId[1]);
        } catch (NumberFormatException exception) {
            writeResponse("Некорректный идентификатор поста", exchange, 400);
            return;
        }
        Subtask subtask = fileBackedTasksManager.getSubtask(id);
        if (subtask.equals(null)) {
            writeResponse("Некорректный идентификатор поста", exchange, 400);
        }
        writeResponse("Задача получена", exchange, 200);
    }

//разбивка по второму слэшу с методом пост
    private void handlerPostRequest(HttpExchange exchange) throws IOException {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        String task = pathParts[2];
        switch (task) {
            case ("task"):
                handlerPostTaskRequest(exchange);
                break;
            case ("epic"):

                break;
            case ("subtask"):

                break;

            default:
                writeResponse("Неправильный адрес", exchange, 400);
                break;
        }
    }
//пост таск, создание задачи
    private void handlerPostTaskRequest(HttpExchange exchange) throws IOException {
        InputStream body = exchange.getRequestBody();
        Task gsonTask = gson.fromJson((new String(body.readAllBytes(), StandardCharsets.UTF_8)), Task.class);
        fileBackedTasksManager.createTask(gsonTask);

    }

    private void handlerDeleteRequest(HttpExchange exchange) {

    }


    private void writeResponse(String body, HttpExchange exchange, int code) throws IOException {
        byte[] responseBody = body.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(code, responseBody.length);
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(responseBody);
        } finally {
            exchange.close();
        }
    }
}
