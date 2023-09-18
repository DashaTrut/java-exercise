package ru.yandex.app.test;

import com.google.gson.Gson;
import org.junit.jupiter.api.*;
import ru.yandex.app.manager.FileBackedTasksManager;
import ru.yandex.app.manager.InMemoryTaskManager;
import ru.yandex.app.manager.Managers;
import ru.yandex.app.manager.TaskManager;
import ru.yandex.app.server.HttpTaskServer;
import ru.yandex.app.server.KVSException;
import ru.yandex.app.server.KVServer;
import ru.yandex.app.tasks.Epic;
import ru.yandex.app.tasks.Status;
import ru.yandex.app.tasks.Subtask;
import ru.yandex.app.tasks.Task;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskServerTest {
  //  private static KVServer kvServer;
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
//        task1 = new Task("Тест для меня", "аа",
//                1, Status.NEW);
//        taskManager.createTask(task1);
//        task2 = new Task("Тест для лучшего дня", "аа",
//                2, Status.NEW);
//        taskManager.createTask(task2);
//        epic1 = new Epic("Тест лучшего тестера", "аа",
//                3, Status.NEW);
//        taskManager.createEpic(epic1);
//        epic2 = new Epic("Тест, чтобы быстрее пошить", "аа",
//                4, Status.NEW);
//        taskManager.createEpic(epic2);
//        epic3 = new Epic("Тест лежебоки", "аа",
//                5, Status.NEW);
//        taskManager.createEpic(epic3);
//        subTask1 = new Subtask("Тест тестович", "аа",
//                6, Status.NEW, 3);
//        taskManager.createSubtask(subTask1);
    }

    @BeforeAll
    static void start() {
//        try {
//            kvServer = new KVServer();
//            kvServer.start();
//        } catch (IOException e) {
//            throw new KVSException(e.getMessage());
//        }
    }



    @BeforeEach
    void setUp() throws IOException {
        taskManager = new InMemoryTaskManager();

//        File file = new File("./BestDatabase.csv");
//         taskManager1 = new FileBackedTasksManager(file);
        httpTaskServer = new HttpTaskServer(8080, taskManager);
        createTasks();

        httpTaskServer.startServer();
    }

    @AfterEach
    public void tearDown() {
        httpTaskServer.stopServer();
    }

    @AfterAll
    static void stop() {
        //kvServer.stop();
        //  httpTaskServer.stopServer();
    }
    @Test
    public void tasksTask() throws IOException, InterruptedException {
        task1 = new Task("Тест для меня", "аа",
                1, Status.NEW);
        //taskManager.createTask(task1);
        task2 = new Task("Тест для лучшего дня", "аа",
                2, Status.NEW);
        //taskManager.createTask(task2);
        epic1 = new Epic("Тест лучшего тестера", "аа",
                3, Status.NEW);
       // taskManager.createEpic(epic1);
        epic2 = new Epic("Тест, чтобы быстрее пошить", "аа",
                4, Status.NEW);
        //taskManager.createEpic(epic2);
        epic3 = new Epic("Тест лежебоки", "аа",
                5, Status.NEW);
        //taskManager.createEpic(epic3);
        subTask1 = new Subtask("Тест тестович", "аа",
                6, Status.NEW, 3);
        //taskManager.createSubtask(subTask1);
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(path+"/task");

        String taskJson1 = gson.toJson(task1);
        String taskJson2 = gson.toJson(task2);

        HttpRequest.BodyPublisher bodyJson1 = HttpRequest.BodyPublishers.ofString(taskJson1);
        HttpRequest.BodyPublisher bodyJson2 = HttpRequest.BodyPublishers.ofString(taskJson2);

        HttpRequest request0 = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

//        HttpRequest request1 = HttpRequest.newBuilder()
//                .POST(bodyJson1)
//                .uri(uri)
//                .build();
//        HttpRequest request2 = HttpRequest
//                .newBuilder()
//                .POST(bodyJson2)
//                .uri(uri)
//                .build();

//        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
//        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response0 = client.send(request0, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response0.statusCode());
        //assertEquals(200, response2.statusCode());
        System.out.println(response0.body());
        System.out.println(response0.statusCode());
       // assertEquals(task1, response1.body(), "Задачи не совпадают.");
       // assertEquals(epic, listLoadEpic.get(0), "Задачи не совпадают.");

    }

    }

