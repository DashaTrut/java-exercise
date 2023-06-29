package ru.yandex.app.server;

import com.sun.net.httpserver.HttpServer;
import ru.yandex.app.manager.FileBackedTasksManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private final int port;  //8080
    HttpServer httpServer = HttpServer.create();
    private final FileBackedTasksManager fileBackedTasksManager;

    public HttpTaskServer(int port, FileBackedTasksManager fileBackedTasksManager) throws IOException {
        this.port = port;
        this.fileBackedTasksManager = fileBackedTasksManager;
        // создали веб-сервер
    }


    public void startServer() throws IOException {
// привязали его к порту
        httpServer.bind(new InetSocketAddress(port), 0);

        httpServer.createContext("/tasks/task/?id=", new TaskHandler(fileBackedTasksManager));
        httpServer.createContext("/tasks/task", new TaskHandler(fileBackedTasksManager));
        httpServer.createContext("/tasks/history", new TaskHandler(fileBackedTasksManager)); // определили эндпоинт
        httpServer.createContext("/tasks/task/ Body:{task}", new TaskHandler(fileBackedTasksManager));
        httpServer.createContext("/tasks/subtask/ Body:{subtask}", new TaskHandler(fileBackedTasksManager));
        httpServer.createContext("/tasks/epic/ Body:{epic}", new TaskHandler(fileBackedTasksManager));
        httpServer.createContext("/tasks/", new TaskHandler(fileBackedTasksManager));
        httpServer.createContext("/tasks/subtask/?id=", new TaskHandler(fileBackedTasksManager));
        httpServer.createContext("/tasks/epic/?id=", new TaskHandler(fileBackedTasksManager));
        httpServer.createContext("/tasks/subtask/epic/?id=", new TaskHandler(fileBackedTasksManager));

        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + port + " порту!");
    }
}
