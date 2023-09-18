package ru.yandex.app.server;

import com.sun.net.httpserver.HttpServer;
import ru.yandex.app.manager.FileBackedTasksManager;
import ru.yandex.app.manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private final int port;  //8080
    HttpServer httpServer = HttpServer.create();
    private final TaskManager fileBackedTasksManager;

    public HttpTaskServer(int port, TaskManager fileBackedTasksManager) throws IOException {
        this.port = port;
        this.fileBackedTasksManager = fileBackedTasksManager;
        // создали веб-сервер
    }


    public void startServer() throws IOException {
// привязали его к порту
        httpServer.bind(new InetSocketAddress(port), 0);
        TaskHandler taskHandler = new TaskHandler(fileBackedTasksManager);

        httpServer.createContext("/tasks/task/?id=", taskHandler);
        httpServer.createContext("/tasks/task", new TaskHandler(fileBackedTasksManager));
        httpServer.createContext("/tasks/history", taskHandler); // определили эндпоинт
        httpServer.createContext("/tasks/task/ Body:{task}", taskHandler);
        httpServer.createContext("/tasks/subtask/ Body:{subtask}", taskHandler);
        httpServer.createContext("/tasks/epic/ Body:{epic}", taskHandler);
        httpServer.createContext("/tasks/", taskHandler);
        httpServer.createContext("/tasks/subtask/?id=", taskHandler);
        httpServer.createContext("/tasks/epic/?id=", taskHandler);
        httpServer.createContext("/tasks/subtask/epic/?id=", taskHandler);

        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + port + " порту!");
    }

    public void stopServer(){
        httpServer.stop(port);
    }
}
