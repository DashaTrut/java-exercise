package ru.yandex.app.test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.*;
import ru.yandex.app.manager.*;
import ru.yandex.app.server.HttpTaskServer;
import ru.yandex.app.tasks.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {
    private static Gson gson = new Gson();
    private static HttpTaskServer httpTaskServer;
    private TaskManager taskManager;
    private final String path = "http://localhost:8080/tasks";

    Task task1;
    Task task2;
    Epic epic1;
    Epic epic2;
    Epic epic3;
    Subtask subTask1;


    private void createTasks() {
        task1 = new Task("Тест для меня", "аа", 1, Status.NEW);
        taskManager.createTask(task1);
        task2 = new Task("Тест для лучшего дня", "аа", 2, Status.NEW);
        taskManager.createTask(task2);
        epic1 = new Epic("Тест лучшего тестера", "аа", 3, Status.NEW);
        taskManager.createEpic(epic1);
        epic2 = new Epic("Тест, чтобы быстрее пошить", "аа", 4, Status.NEW);
        taskManager.createEpic(epic2);
        subTask1 = new Subtask("Тест тестович", "аа", 5, Status.NEW, 3);
        taskManager.createSubtask(subTask1);
        epic3 = new Epic("Тест лежебоки", "аа", 6, Status.NEW);
        taskManager.createEpic(epic3);

    }


    @BeforeEach
    void setUp() throws IOException {
        taskManager = new InMemoryTaskManager();
        httpTaskServer = new HttpTaskServer(8080, taskManager);
        createTasks();

        httpTaskServer.startServer();
    }

    @AfterEach
    public void stop() {
        httpTaskServer.stopServer();
    }

    @Test
    public void getTasksTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(path + "/task");
        HttpRequest request0 = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        HttpResponse<String> response0 = client.send(request0, HttpResponse.BodyHandlers.ofString());
        Type taskType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        ArrayList<Task> loadedTasks = gson.fromJson(response0.body(), taskType);
        assertEquals(200, response0.statusCode(), "Неверный код ответа");
        assertNotNull(loadedTasks, "Задачи на возвращаются");
        assertEquals(2, loadedTasks.size(), "Количество всех задач не совпадает");
        assertEquals(task1, loadedTasks.get(0), "Задачи не совпадают");
        assertEquals(task2, loadedTasks.get(1), "Задачи не совпадают");
    }

    @Test
    public void getTasksEpic() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(path + "/epic");
        HttpRequest request0 = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> response0 = client.send(request0, HttpResponse.BodyHandlers.ofString());
        Type taskType = new TypeToken<ArrayList<Epic>>() {
        }.getType();
        ArrayList<Epic> loadedTasks = gson.fromJson(response0.body(), taskType);
        assertEquals(200, response0.statusCode(), "Неверный код ответа");
        assertNotNull(loadedTasks, "Задачи на возвращаются");
        assertEquals(3, loadedTasks.size(), "Количество всех задач не совпадает");
        assertEquals(epic1, loadedTasks.get(0), "Задачи не совпадают");
        assertEquals(epic2, loadedTasks.get(1), "Задачи не совпадают");
        assertEquals(epic3, loadedTasks.get(2), "Задачи не совпадают");
    }

    @Test
    public void getTasksSubtask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(path + "/subtask");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type taskType = new TypeToken<ArrayList<Subtask>>() {
        }.getType();
        ArrayList<Subtask> loadedTasks = gson.fromJson(response.body(), taskType);
        assertEquals(200, response.statusCode(), "Неверный код ответа");
        assertNotNull(loadedTasks, "Задачи на возвращаются");
        assertEquals(1, loadedTasks.size(), "Количество всех задач не совпадает");
        assertEquals(subTask1, loadedTasks.get(0), "Задачи не совпадают");
    }

    @Test
    public void postTasksTask() throws IOException, InterruptedException {
        Task task10 = new Task("Тест для лучшего ревьюера", "аа", 7, Status.NEW);

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(path + "/task");
        String taskJson = gson.toJson(task10);

        HttpRequest.BodyPublisher bodyJson1 = HttpRequest.BodyPublishers.ofString(taskJson);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(bodyJson1)
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Неверный код ответа");
        List<Task> taskList = taskManager.getAllTask();
        assertEquals(3, taskList.size(), "Количество всех задач не совпадает");
        assertEquals(taskList.get(2), task10, "Задачи не совпадают");
    }

    @Test
    public void postTasksEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Тест как я устал", "аа", 7, Status.NEW);

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(path + "/epic");
        String taskJson = gson.toJson(epic);

        HttpRequest.BodyPublisher bodyJson1 = HttpRequest.BodyPublishers.ofString(taskJson);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(bodyJson1)
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Неверный код ответа");
        List<Epic> taskList = taskManager.getAllEpic();
        assertEquals(4, taskList.size(), "Количество всех задач не совпадает");
        assertEquals(taskList.get(3), epic, "Задачи не совпадают");
    }

    @Test
    public void postTasksSubtask() throws IOException, InterruptedException {
        Subtask subtask = new Subtask("Тест для худшего тз", "аа", 7, Status.NEW, 6);

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(path + "/subtask");
        String taskJson = gson.toJson(subtask);

        HttpRequest.BodyPublisher bodyJson1 = HttpRequest.BodyPublishers.ofString(taskJson);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(bodyJson1)
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Неверный код ответа");
        List<Subtask> taskList = taskManager.getAllSubtask();
        System.out.println(taskList);
        assertEquals(2, taskList.size(), "Количество всех задач не совпадает");
        assertEquals(taskList.get(1), subtask, "Задачи не совпадают");
    }

    @Test
    public void deleteTasksTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(path + "/task/" + "?id=1");

        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> taskList = taskManager.getAllTask();
        assertEquals(200, response.statusCode(), "Неверный код ответа");
        assertEquals(1, taskList.size(), "Количество всех задач не совпадает");
        assertNull(taskManager.getTask(1), "Задача не удалена");
    }

    @Test
    public void deleteTasksEpic() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(path + "/epic/" + "?id=3");

        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Epic> taskList = taskManager.getAllEpic();
        assertEquals(200, response.statusCode(), "Неверный код ответа");
        assertEquals(2, taskList.size(), "Количество всех задач не совпадает");
        assertNull(taskManager.getTask(3), "Задача не удалена");
    }

    @Test
    public void deleteTasksSubtask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(path + "/subtask/" + "?id=5");

        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Subtask> taskList = taskManager.getAllSubtask();
        assertEquals(200, response.statusCode(), "Неверный код ответа");
        assertEquals(0, taskList.size(), "Количество всех задач не совпадает");
        assertNull(taskManager.getSubtask(5), "Задача не удалена");
    }

    @Test
    public void GetTasksTaskId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(path + "/task/" + "?id=1");

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Task taskResponse = gson.fromJson(response.body(), Task.class);
        assertEquals(200, response.statusCode(), "Неверный код ответа");
        assertEquals(taskManager.getTask(1), taskResponse, "Задачи не совпадают");
    }

    @Test
    public void getTasksEpicId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(path + "/epic/" + "?id=3");

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Epic epicResponse = gson.fromJson(response.body(), Epic.class);
        assertEquals(200, response.statusCode(), "Неверный код ответа");
        assertEquals(taskManager.getEpic(3), epicResponse, "Задачи не совпадают");
    }

    @Test
    public void getTasksSubtaskId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(path + "/subtask/" + "?id=5");

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Subtask taskResponse = gson.fromJson(response.body(), Subtask.class);
        assertEquals(200, response.statusCode(), "Неверный код ответа");
        assertEquals(taskManager.getSubtask(5), taskResponse, "Задачи не совпадают");
    }


    @Test
    public void getTasksEpicSubtask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(path + "/subtask/epic/" + "?id=3");

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type type = new TypeToken<ArrayList<Subtask>>() {
        }.getType();
        ArrayList<Subtask> loadedTasks = gson.fromJson(response.body(), type);
        assertEquals(200, response.statusCode(), "Неверный код ответа");
        assertEquals(taskManager.getSubtask(5), loadedTasks.get(0), "Задачи не совпадают");
    }

    @Test
    public void deleteTasksAllTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(path + "/task/");

        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> taskList = taskManager.getAllTask();
        assertEquals(200, response.statusCode(), "Неверный код ответа");
        assertEquals(0, taskList.size(), "Количество всех задач не совпадает");
        assertNull(taskManager.getTask(1), "Задачи не удалена");
    }

    @Test
    public void deleteTasksAllEpic() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(path + "/epic/");

        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Epic> taskList = taskManager.getAllEpic();
        assertEquals(200, response.statusCode(), "Неверный код ответа");
        assertEquals(0, taskList.size(), "Количество всех задач не совпадает");
        assertNull(taskManager.getTask(3), "Задача не удалена");
    }

    @Test
    public void deleteTasksAllSubtask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(path + "/subtask/");

        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Subtask> taskList = taskManager.getAllSubtask();
        assertEquals(200, response.statusCode(), "Неверный код ответа");
        assertEquals(0, taskList.size(), "Количество всех задач не совпадает");
        assertNull(taskManager.getSubtask(5), "Задача не удалена");
    }

    @Test
    public void getTasksHistory() throws IOException, InterruptedException {
        taskManager.getTask(2);
        taskManager.getEpic(3);
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(path + "/history");

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type historyType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        ArrayList<Task> loadedTasks = gson.fromJson(response.body(), historyType);
        ArrayList<Task> loadedTasksNew = new ArrayList<>();
        if (!loadedTasks.isEmpty()) {
            for (int i = 0; i < loadedTasks.size(); i++) {
                Task task = loadedTasks.get(i);
                int id = task.getId();
                if (taskManager.getTask(id) != null) {
                    loadedTasksNew.add(taskManager.getTask(id));
                }
                if (taskManager.getSubtask(id) != null) {
                    loadedTasksNew.add(taskManager.getSubtask(id));
                }
                if (taskManager.getEpic(id) != null) {
                    loadedTasksNew.add(taskManager.getEpic(id));
                }
            }

        }
        assertEquals(200, response.statusCode(), "Неверный код ответа");
        assertNotNull(loadedTasksNew, "Истории не возвращаются");
        assertEquals(2, loadedTasksNew.size(), "Количество историй не совпадает");
        assertEquals(task2, loadedTasksNew.get(0), "Истории не совпадают");
        assertEquals(epic1, loadedTasksNew.get(1), "Истории не совпадают");
    }


    @Test
    public void getTasksPrioritizedTasks() throws IOException, InterruptedException {
        taskManager.getTask(2);
        taskManager.getEpic(3);
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(path + "/");
        Task epic = new Task("не болеть", "а", 0, Status.NEW, 20,
                LocalDateTime.of(2023, 4, 10, 13, 40));
        taskManager.createTask(epic);
        Subtask task666 = new Subtask("выздороветь", "а", 0, Status.NEW, 3, 20,
                LocalDateTime.of(2023, 4, 15, 14, 40));
        taskManager.createSubtask(task666);
        taskManager.getSubtask(task666.getId());
        taskManager.getEpic(epic.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type historyType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        ArrayList<Task> loadedTasks = gson.fromJson(response.body(), historyType);

        ArrayList<Task> loadedTasksNew = new ArrayList<>();
        if (!loadedTasks.isEmpty()) {
            for (Task task : loadedTasks) {
                int id = task.getId();
                if (taskManager.getTask(id) != null) {
                    loadedTasksNew.add(taskManager.getTask(id));
                }
                if (taskManager.getSubtask(id) != null) {
                    loadedTasksNew.add(taskManager.getSubtask(id));
                }
                if (taskManager.getEpic(id) != null) {
                    loadedTasksNew.add(taskManager.getEpic(id));
                }
            }
        }
        assertEquals(200, response.statusCode(), "Неверный код ответа");
        assertNotNull(loadedTasks, "Задачи на возвращаются");
        assertEquals(5, loadedTasksNew.size(), "Количество всех задач не совпадает");
        assertEquals(epic, loadedTasksNew.get(0), "Задачи не совпадают");
        assertEquals(task666, loadedTasksNew.get(1), "Задачи не совпадают");

    }
}

