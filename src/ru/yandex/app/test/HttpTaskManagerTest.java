package ru.yandex.app.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.app.manager.FileBackedTasksManager;
import ru.yandex.app.server.HttpTaskManager;
import ru.yandex.app.server.HttpTaskServer;
import ru.yandex.app.server.KVSException;
import ru.yandex.app.server.KVServer;
import ru.yandex.app.tasks.Epic;
import ru.yandex.app.tasks.Status;
import ru.yandex.app.tasks.Subtask;
import ru.yandex.app.tasks.Task;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskManagerTest {
    private File file;

    private KVServer kvServer;

    @BeforeEach
    public void setUp() {
        file = new File("./BestDatabase.csv");
        try {
            kvServer = new KVServer();
            kvServer.start();
//            HttpTaskServer httpTaskServer = new HttpTaskServer(8080, httpTaskManager);
//            httpTaskServer.startServer();
        } catch (IOException e) {
            throw new KVSException(e.getMessage());
        }


    }

    @Test
    public void checkSaveAndLoadNorm() {
        HttpTaskManager httpTaskManager = new HttpTaskManager(file, false);
        Task task = new Task("Уборка", "а", 0, Status.NEW);
        int newId = httpTaskManager.createTask(task);
        httpTaskManager.getTask(newId);

        Epic epic = new Epic("Готовка", "а", 0, Status.NEW);
        int newIdEpic = httpTaskManager.createEpic(epic);
        httpTaskManager.getEpic(newIdEpic);

        Subtask subtask = new Subtask("Покупка продуктов", "а", 0, Status.NEW,
                epic.getId());
        int idSubtask = httpTaskManager.createSubtask(subtask);
        httpTaskManager.getSubtask(idSubtask);
        Subtask subtask1 = new Subtask("Выбор рецептов", "а", 0, Status.NEW,
                epic.getId());

        int idSubtask2 = httpTaskManager.createSubtask(subtask1);
        List<Task> listHistory = httpTaskManager.getHistory();
        httpTaskManager.save();
        HttpTaskManager httpTaskManagerNew = new HttpTaskManager(file, true);
        List<Task> listLoadTask = httpTaskManagerNew.getAllTask();
        List<Epic> listLoadEpic = httpTaskManagerNew.getAllEpic();
        List<Subtask> listLoadSubtask = httpTaskManagerNew.getAllSubtask();
        List<Task> listLoadHistory = httpTaskManagerNew.getHistory();

        assertEquals(subtask, listLoadSubtask.get(0), "Задачи не совпадают.");
        assertEquals(task, listLoadTask.get(0), "Задачи не совпадают.");
        assertEquals(epic, listLoadEpic.get(0), "Задачи не совпадают.");
        assertEquals(listHistory.get(0), listLoadHistory.get(0), "Истории не совпадают.");
        assertEquals(listHistory.get(1), listLoadHistory.get(1), "Истории не совпадают.");
        assertEquals(listHistory.size(), listLoadHistory.size(), "Количество историй не совпадает.");
        Epic checkEpic = listLoadEpic.get(0);
        assertNotNull(checkEpic, "Задача не существует.");
        List<Integer> epicSubtaskId = checkEpic.getSubtaskIds();

        assertEquals(epicSubtaskId.size(), 2, "Подзадачи не совпадают.");
        assertEquals(epicSubtaskId.get(0), idSubtask, "Задачи не совпадают.");
        assertEquals(epicSubtaskId.get(1), idSubtask2, "Задачи не совпадают.");

    }

    @Test
    public void checkSaveAndLoadVoid() {
        HttpTaskManager httpTaskManager = new HttpTaskManager(file, false);
        Task task = new Task("Уборка", "а", 0, Status.NEW);
        int newId = httpTaskManager.createTask(task);

        Epic epic = new Epic("Готовка", "а", 0, Status.NEW);
        int newIdEpic = httpTaskManager.createEpic(epic);

        Subtask subtask = new Subtask("Покупка продуктов", "а", 0, Status.NEW,
                epic.getId());
        int idSubtask = httpTaskManager.createSubtask(subtask);
        Subtask subtask1 = new Subtask("Выбор рецептов", "а", 0, Status.NEW,
                epic.getId());

        int idSubtask2 = httpTaskManager.createSubtask(subtask1);
        List<Task> listHistory = httpTaskManager.getHistory();


        HttpTaskManager httpTaskManagerNew = new HttpTaskManager(file, true);
        List<Task> listLoadTask = httpTaskManagerNew.getAllTask();
        List<Epic> listLoadEpic = httpTaskManagerNew.getAllEpic();
        List<Subtask> listLoadSubtask = httpTaskManagerNew.getAllSubtask();
        List<Task> listLoadHistory = httpTaskManagerNew.getHistory();

        assertEquals(subtask, listLoadSubtask.get(0), "Задачи не совпадают.");
        assertEquals(task, listLoadTask.get(0), "Задачи не совпадают.");
        assertEquals(epic, listLoadEpic.get(0), "Задачи не совпадают.");

        assertEquals(listHistory.size(), listLoadHistory.size(), "Количество историй не совпадает.");
        Epic checkEpic = listLoadEpic.get(0);
        assertNotNull(checkEpic, "Задача не существует.");
        List<Integer> epicSubtaskId = checkEpic.getSubtaskIds();

        assertEquals(epicSubtaskId.size(), 2, "Подзадачи не совпадают.");
        assertEquals(epicSubtaskId.get(0), idSubtask, "Задачи не совпадают.");
        assertEquals(epicSubtaskId.get(1), idSubtask2, "Задачи не совпадают.");

    }

    @Test
    public void checkSaveAndLoadEpicWithoutSubtask() {
        HttpTaskManager httpTaskManager = new HttpTaskManager(file, false);
        Task task = new Task("Уборка", "а", 0, Status.NEW);
        int newId = httpTaskManager.createTask(task);
        httpTaskManager.getTask(newId);

        Epic epic = new Epic("Готовка", "а", 0, Status.NEW);
        int newIdEpic = httpTaskManager.createEpic(epic);
        httpTaskManager.getEpic(newIdEpic);

        List<Task> listHistory = httpTaskManager.getHistory();

        HttpTaskManager httpTaskManagerNew = new HttpTaskManager(file, true);
        List<Task> listLoadTask = httpTaskManagerNew.getAllTask();
        List<Epic> listLoadEpic = httpTaskManagerNew.getAllEpic();
        List<Task> listLoadHistory = httpTaskManagerNew.getHistory();


        assertEquals(task, listLoadTask.get(0), "Задачи не совпадают.");
        assertEquals(epic, listLoadEpic.get(0), "Задачи не совпадают.");
        assertEquals(listHistory.get(0), listLoadHistory.get(0), "Истории не совпадают.");
        assertEquals(listHistory.get(1), listLoadHistory.get(1), "Истории не совпадают.");
        assertEquals(listHistory.size(), listLoadHistory.size(), "Количество историй не совпадает.");
        Epic checkEpic = listLoadEpic.get(0);
        assertNotNull(checkEpic, "Задача не существует.");
        List<Integer> epicSubtaskId = checkEpic.getSubtaskIds();
        assertEquals(epicSubtaskId.size(), 0, "Подзадачи не совпадают.");
    }

    @Test
    public void checkSaveAndLoadVoidAllTask() {
        HttpTaskManager httpTaskManager = new HttpTaskManager(file, false);
        httpTaskManager.save();

        HttpTaskManager httpTaskManagerNew = new HttpTaskManager(file, true);
        List<Task> listLoadTask = httpTaskManagerNew.getAllTask();
        List<Epic> listLoadEpic = httpTaskManagerNew.getAllEpic();
        List<Subtask> listLoadSubtask = httpTaskManagerNew.getAllSubtask();
        List<Task> listLoadHistory = httpTaskManagerNew.getHistory();
        assertEquals(0, listLoadTask.size(), "Количество задач не совпадает.");
        assertEquals(0, listLoadEpic.size(), "Количество эпиков не совпадает.");
        assertEquals(0, listLoadSubtask.size(), "Количество подзадач не совпадает.");
        assertEquals(0, listLoadHistory.size(), "Количество историй не совпадает.");
    }

    @Test
    public void checkSaveAndLoadingTime() {
        HttpTaskManager httpTaskManager = new HttpTaskManager(file, false);
        Task task = new Task("Уборка", "а", 0, Status.NEW);
        int newId = httpTaskManager.createTask(task);
        httpTaskManager.getTask(newId);

        Epic epic = new Epic("Готовка", "а", 0, Status.NEW);
        int newIdEpic = httpTaskManager.createEpic(epic);
        httpTaskManager.getEpic(newIdEpic);

        Subtask subtask = new Subtask("Покупка продуктов", "а", 0, Status.NEW,
                epic.getId());
        int idSubtask = httpTaskManager.createSubtask(subtask);
        httpTaskManager.getSubtask(idSubtask);
        Subtask subtask1 = new Subtask("Выбор рецептов", "а", 0, Status.NEW,
                epic.getId());
        int idSubtask2 = httpTaskManager.createSubtask(subtask1);
        Subtask task666 = new Subtask("66Уборка6", "а", 0, Status.NEW, epic.getId(), 20,
                LocalDateTime.of(2023, 4, 15, 14, 40));

        int id666 = httpTaskManager.createSubtask(task666);
        httpTaskManager.getEpic(id666);


        List<Task> listHistory = httpTaskManager.getHistory();
        httpTaskManager.save();

        HttpTaskManager httpTaskManagerNew = new HttpTaskManager(file, true);
        List<Task> listLoadTask = httpTaskManagerNew.getAllTask();
        List<Epic> listLoadEpic = httpTaskManagerNew.getAllEpic();
        List<Subtask> listLoadSubtask = httpTaskManagerNew.getAllSubtask();
        List<Task> listLoadHistory = httpTaskManagerNew.getHistory();

        assertEquals(subtask, listLoadSubtask.get(0), "Задачи не совпадают.");
        assertEquals(task, listLoadTask.get(0), "Задачи не совпадают.");
        assertEquals(epic, listLoadEpic.get(0), "Задачи не совпадают.");
        assertEquals(listHistory.get(0), listLoadHistory.get(0), "Истории не совпадают.");
        assertEquals(listHistory.get(1), listLoadHistory.get(1), "Истории не совпадают.");
        assertEquals(listHistory.size(), listLoadHistory.size(), "Количество историй не совпадает.");
        Epic checkEpic = listLoadEpic.get(0);
        assertNotNull(checkEpic, "Задача не существует.");
        List<Integer> epicSubtaskId = checkEpic.getSubtaskIds();

        assertEquals(epicSubtaskId.size(), 3, "Подзадачи не совпадают.");
        assertEquals(epicSubtaskId.get(0), idSubtask, "Задачи не совпадают.");
        assertEquals(epicSubtaskId.get(1), idSubtask2, "Задачи не совпадают.");
        assertEquals(epicSubtaskId.get(2), id666, "Задачи не совпадают.");
        assertNotNull(task666.getStartTime(), "У задачи не сохранилось время или дата.");
        assertNotNull(epic.getStartTime(), "У эпика не сохранилось время или дата.");
        assertEquals(epic.getDuration(), task666.getDuration(), "Duration не совпадают.");
    }


}
