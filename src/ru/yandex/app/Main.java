package ru.yandex.app;

import ru.yandex.app.manager.FileBackedTasksManager;
import ru.yandex.app.manager.Managers;
import ru.yandex.app.manager.TaskManager;
import ru.yandex.app.server.HttpTaskManager;
import ru.yandex.app.server.KVSException;
import ru.yandex.app.server.KVServer;
import ru.yandex.app.tasks.Epic;
import ru.yandex.app.tasks.Subtask;
import ru.yandex.app.tasks.Task;
import ru.yandex.app.tasks.Status;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        System.out.println("Поехали!");
        try {
            KVServer kvServer = new KVServer();
            kvServer.start();
            System.out.println("hy");
            File file = new File("./BestDatabase.csv");
            HttpTaskManager newManager = new HttpTaskManager(file, false);
            Task task666 = new Task("Повторяющаяся задача", "а", 0, Status.NEW);
            Epic task154 = new Epic("Проверка лучшего программис444та", "а", 0, Status.NEW);
            int t154 = newManager.createEpic(task154);
            newManager.getEpic(t154);
            System.out.println(newManager.getHistory());
            int newId666 = newManager.createTask(task666);
            System.out.println(newManager.getTask(newId666));
            System.out.println(newId666);
            newManager.getTask(newId666);
            Task task777 = new Task("Проверка квс", "а", 0, Status.NEW);
            int newId777 = newManager.createTask(task777);

            HttpTaskManager newManager15 = new HttpTaskManager(file, true);
            System.out.println(newManager15.getTask(newId666) + "абалдеть");
            System.out.println(newManager15.getTask(newId777) + "абалдеть2"); //не печатает

            Task taskUpdate777 = new Task("Проверка апдейта", "а", 2, Status.NEW);
            newManager.updateTask(taskUpdate777);
            System.out.println(newManager.getAllTask() + "!!!!!");
            //newManager.save();
            //newManager15.loadKVServer();
            System.out.println(newManager15.getTask(newId777) + "абалдеть33"); // не печатает null
            System.out.println(newManager15.getAllTask() + "абалдеть3");
            Epic task15 = new Epic("Проверка лучшего программиста", "а", 0, Status.NEW);
            int t15 = newManager15.createEpic(task15);
            newManager15.getEpic(t15);
            System.out.println(newManager15.getHistory());
            System.out.println(newManager15.getAllTask() + "абалдеть100");
            kvServer.stop();
        } catch (IOException e) {
            throw new KVSException("квс не запустился");
        }
    }
}
