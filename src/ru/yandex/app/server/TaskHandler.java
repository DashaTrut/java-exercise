package ru.yandex.app.server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.app.manager.TaskManager;
import ru.yandex.app.tasks.Epic;
import ru.yandex.app.tasks.Subtask;
import ru.yandex.app.tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;


public class TaskHandler implements HttpHandler {
    private final Gson gson = new Gson();

    protected final TaskManager taskManager;

    public TaskHandler(TaskManager fileBackedTasksManager) {
        this.taskManager = fileBackedTasksManager;
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
        String[] pathParts = exchange.getRequestURI().getPath().split("/", -1);
        String task = pathParts[2];
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
                writeResponse(gson.toJson(taskManager.getHistory()), exchange, 200);
                break;
            case (""):
                String response = gson.toJson(taskManager.getPrioritizedTasks());
                writeResponse(response, exchange, 200);
                break;
            default:
                writeResponse("Неправильный адрес", exchange, 400);
                break;
        }
    }

    private void handlerGetTaskRequest(HttpExchange exchange) throws IOException {
        int id;

        String query = exchange.getRequestURI().getQuery();
        if (query == (null)) {
            List<Task> taskList = taskManager.getAllTask();
            writeResponse(gson.toJson(taskList), exchange, 200);
            return;
        } else {
            try {
                String[] strId = (query).split("=");
                id = Integer.parseInt(strId[1]);
            } catch (NumberFormatException exception) {
                writeResponse("Некорректный идентификатор поста ", exchange, 400);
                return;
            }
            Task task = taskManager.getTask(id);
            if (task.equals(null)) {
                writeResponse("Некорректный идентификатор поста", exchange, 400);
            }
            writeResponse(gson.toJson(task), exchange, 200);
        }
    }

    //гет эпик по айди
    private void handlerGetEpicRequest(HttpExchange exchange) throws IOException {
        int id;
        String query = exchange.getRequestURI().getQuery();
        if (query == (null)) {
            List<Epic> epicList = taskManager.getAllEpic();
            writeResponse(gson.toJson(epicList), exchange, 200);
            return;
        } else {
            try {
                String[] strId = (query).split("=");
                id = Integer.parseInt(strId[1]);
            } catch (NumberFormatException exception) {
                writeResponse("Некорректный идентификатор поста", exchange, 400);
                return;
            }
            Epic epic = taskManager.getEpic(id);
            if (epic.equals(null)) {
                writeResponse("Некорректный идентификатор поста", exchange, 400);
            }
            writeResponse(gson.toJson(epic), exchange, 200);
        }
    }

    //гет сабтаск по айди и сабтаск эпик
    private void handlerGetSubtaskRequest(HttpExchange exchange) throws IOException {
        int id;
        String[] path = exchange.getRequestURI().getPath().split("/");
        String query = exchange.getRequestURI().getQuery();
        if (query == (null)) {
            List<Subtask> subtaskList = taskManager.getAllSubtask();
            writeResponse(gson.toJson(subtaskList), exchange, 200);
            return;
        } else {
            if (path.length == 4) {
                try {
                    String[] strId = (query).split("=");
                    id = Integer.parseInt(strId[1]);
                } catch (NumberFormatException exception) {
                    writeResponse("Некорректный идентификатор поста", exchange, 400);
                    return;
                }
                List<Subtask> list = taskManager.getEpicSubtasks(id);
                writeResponse(gson.toJson(list), exchange, 200);
                return;
            }
        }
        try { //тут ветка для subtask/epic/?id=1
            String[] strId = (query).split("=");
            id = Integer.parseInt(strId[1]);
        } catch (NumberFormatException exception) {
            writeResponse("Некорректный идентификатор поста", exchange, 400);
            return;
        }
        Subtask subtask = taskManager.getSubtask(id);
        if (query == (null)) {
            writeResponse("Некорректный идентификатор поста", exchange, 400);
        }
        writeResponse(gson.toJson(subtask), exchange, 200);
    }

    //разбивка по второму слэшу с методом пост. создание задач
    private void handlerPostRequest(HttpExchange exchange) throws IOException {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        String task = pathParts[2];
        switch (task) {
            case ("task"):
                handlerPostTaskRequest(exchange);
                break;
            case ("epic"):
                handlerPostEpicRequest(exchange);
                break;
            case ("subtask"):
                handlerPostSubtaskRequest(exchange);
                break;

            default:
                writeResponse("Неправильный адрес", exchange, 400);
                break;
        }
    }

    //пост таск, создание задачи
    private void handlerPostTaskRequest(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        System.out.println("Тело запроса:\n" + body);
        Task gsonTask = gson.fromJson(body, Task.class);
        if ((taskManager.getTask(gsonTask.getId())) != null) {
            taskManager.updateTask(gsonTask);
            writeResponse("Изменена задача", exchange, 200);
        } else {
            int id = taskManager.createTask(gsonTask);
            writeResponse("Создана задача с id =" + id, exchange, 200);
        }

    }

    //пост эпик, создание эпика
    private void handlerPostEpicRequest(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        System.out.println("Тело запроса:\n" + body);
        Epic gsonEpic = gson.fromJson(body, Epic.class);
        if ((taskManager.getEpic(gsonEpic.getId())) != null) {
            taskManager.updateEpic(gsonEpic);
            writeResponse("Изменен эпик", exchange, 200);
        } else {
            int id = taskManager.createEpic(gsonEpic);
            writeResponse("Создан эпик с id =" + id, exchange, 200);
        }

    }

    //пост сабтаск, создание подзадачи
    private void handlerPostSubtaskRequest(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        System.out.println("Тело запроса:\n" + body);
        Subtask gsonSubtask = gson.fromJson(body, Subtask.class);
        if (taskManager.getSubtask(gsonSubtask.getId()) != null) {
            taskManager.updateSubtask(gsonSubtask);
            writeResponse("Изменен эпик", exchange, 200);
        } else {
            int id = taskManager.createSubtask(gsonSubtask);
            writeResponse("Создан эпик с id =" + id, exchange, 200);
        }
    }

    //разбивка по второму слешу с методом делит на таски, подзадачи, эпики
    private void handlerDeleteRequest(HttpExchange exchange) throws IOException {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        String task = pathParts[2];
        switch (task) {
            case ("task"):
                handlerDeleteTaskRequest(exchange);
                break;
            case ("epic"):
                handlerDeleteEpicRequest(exchange);
                break;
            case ("subtask"):
                handlerDeleteSubtaskRequest(exchange);
                break;

            default:
                writeResponse("Неправильный адрес", exchange, 400);
                break;
        }
    }

    //удалить таск
    private void handlerDeleteTaskRequest(HttpExchange exchange) throws IOException {
        int id;
        String query = exchange.getRequestURI().getQuery();
        if (query == (null)) {
            taskManager.deleteAllTask();
            writeResponse("Задачи удалены", exchange, 200);
        } else {
            try {
                String[] strId = (query).split("=");
                id = Integer.parseInt(strId[1]);
            } catch (NumberFormatException exception) {
                writeResponse("Некорректный идентификатор задачи ", exchange, 400);
                return;
            }
            taskManager.deleteForIdTask(id);
            writeResponse("Задача удалена", exchange, 200);
        }
    }

    //удалить эпик
    private void handlerDeleteEpicRequest(HttpExchange exchange) throws IOException {
        int id;
        String query = exchange.getRequestURI().getQuery();
        if (query == (null)) {
            taskManager.deleteAllEpic();
            writeResponse("Задачи удалены", exchange, 200);
        } else {
            try {
                String[] strId = (query).split("=");
                id = Integer.parseInt(strId[1]);
            } catch (NumberFormatException exception) {
                writeResponse("Некорректный идентификатор задачи ", exchange, 400);
                return;
            }
            taskManager.deleteForIdEpic(id);
            writeResponse("Задача удалена", exchange, 200);
        }
    }

    //удалить сабтаск
    private void handlerDeleteSubtaskRequest(HttpExchange exchange) throws IOException {
        int id;
        String query = exchange.getRequestURI().getQuery();
        if (query == (null)) {
            taskManager.deleteAllSubtask();
            writeResponse("Задачи удалены", exchange, 200);
        } else {
            try {
                String[] strId = (query).split("=");
                id = Integer.parseInt(strId[1]);
            } catch (NumberFormatException exception) {
                writeResponse("Некорректный идентификатор задачи ", exchange, 400);
                return;
            }

            taskManager.deleteForIdSubtask(id);
            writeResponse("Задача удалена", exchange, 200);
        }
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
