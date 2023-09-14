package ru.yandex.app.server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.app.manager.FileBackedTasksManager;
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

    private void handlerGetTaskRequest(HttpExchange exchange) throws IOException {
        int id;
        String query = exchange.getRequestURI().getQuery();
        if (query == (null)) {
            List<Task> taskList = fileBackedTasksManager.getAllTask();
            System.out.println(taskList);
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

    //гет сабтаск по айди и сабтаск эпик
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
        try { //тут ветка для subtask/epic/?id=1
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
        if ((fileBackedTasksManager.getTask(gsonTask.getId())) != null) {
            fileBackedTasksManager.updateTask(gsonTask);
            writeResponse("Изменена задача", exchange, 200);
        } else {
            int id = fileBackedTasksManager.createTask(gsonTask);
            writeResponse("Создана задача с id =" + id, exchange, 200);
        }

    }

    //пост эпик, создание эпика
    private void handlerPostEpicRequest(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        System.out.println("Тело запроса:\n" + body);
        Epic gsonEpic = gson.fromJson(body, Epic.class);
        if ((fileBackedTasksManager.getEpic(gsonEpic.getId())) != null) {
            fileBackedTasksManager.updateEpic(gsonEpic);
            writeResponse("Изменен эпик", exchange, 200);
        } else {
            int id = fileBackedTasksManager.createEpic(gsonEpic);
            writeResponse("Создан эпик с id =" + id, exchange, 200);
        }

    }

    //пост сабтаск, создание подзадачи
    private void handlerPostSubtaskRequest(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        System.out.println("Тело запроса:\n" + body);
        Subtask gsonSubtask = gson.fromJson(body, Subtask.class);
        if (fileBackedTasksManager.getSubtask(gsonSubtask.getId()) == null) {
            fileBackedTasksManager.updateSubtask(gsonSubtask);
            writeResponse("Изменен эпик", exchange, 200);
        } else {
            int id = fileBackedTasksManager.createSubtask(gsonSubtask);
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
        if (query.equals(null)) {
            fileBackedTasksManager.deleteAllTask();
            writeResponse("Задачи удалены", exchange, 200);
        } else {
            try {
                String[] strId = (query).split("=");
                id = Integer.parseInt(strId[1]);
            } catch (NumberFormatException exception) {
                writeResponse("Некорректный идентификатор задачи ", exchange, 400);
                return;
            }
            fileBackedTasksManager.deleteForIdTask(id);
            writeResponse("Задача удалена", exchange, 200);
        }
    }

    //удалить эпик
    private void handlerDeleteEpicRequest(HttpExchange exchange) throws IOException {
        int id;
        String query = exchange.getRequestURI().getQuery();
        if (query.equals(null)) {
            fileBackedTasksManager.deleteAllTask();
            writeResponse("Задачи удалены", exchange, 200);
        } else {
            try {
                String[] strId = (query).split("=");
                id = Integer.parseInt(strId[1]);
            } catch (NumberFormatException exception) {
                writeResponse("Некорректный идентификатор задачи ", exchange, 400);
                return;
            }
            fileBackedTasksManager.deleteForIdEpic(id);
            writeResponse("Задача удалена", exchange, 200);
        }
    }

    //удалить сабтаск
    private void handlerDeleteSubtaskRequest(HttpExchange exchange) throws IOException {
        int id;
        String query = exchange.getRequestURI().getQuery();
        if (query.equals(null)) {
            fileBackedTasksManager.deleteAllTask();
            writeResponse("Задачи удалены", exchange, 200);
        } else {
            try {
                String[] strId = (query).split("=");
                id = Integer.parseInt(strId[1]);
            } catch (NumberFormatException exception) {
                writeResponse("Некорректный идентификатор задачи ", exchange, 400);
                return;
            }
            fileBackedTasksManager.deleteForIdEpic(id);
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
