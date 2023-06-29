package ru.yandex.app.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.app.manager.FileBackedTasksManager;

import ru.yandex.app.tasks.Epic;
import ru.yandex.app.tasks.Status;
import ru.yandex.app.tasks.Subtask;
import ru.yandex.app.tasks.Task;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.app.tasks.TypeTask.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    private File file;

    @BeforeEach
    public void setUp() {
        file = new File("./BestDatabase.csv");
        taskManager = new FileBackedTasksManager(file);

    }

    @Test
    public void checkSaveAndLoadNorm() {
        Task task = new Task("Уборка", "а", 0, Status.NEW);
        int newId = taskManager.createTask(task);
        taskManager.getTask(newId);

        Epic epic = new Epic("Готовка", "а", 0, Status.NEW);
        int newIdEpic = taskManager.createEpic(epic);
        taskManager.getEpic(newIdEpic);

        Subtask subtask = new Subtask("Покупка продуктов", "а", 0, Status.NEW,
                epic.getId());
        int idSubtask = taskManager.createSubtask(subtask);
        taskManager.getSubtask(idSubtask);
        Subtask subtask1 = new Subtask("Выбор рецептов", "а", 0, Status.NEW,
                epic.getId());

        int idSubtask2 = taskManager.createSubtask(subtask1);
        List<Task> listHistory = taskManager.getHistory();

        taskManager = taskManager.loadFromFile(file);
        List<Task> listLoadTask = taskManager.getAllTask();
        List<Epic> listLoadEpic = taskManager.getAllEpic();
        List<Subtask> listLoadSubtask = taskManager.getAllSubtask();
        List<Task> listLoadHistory = taskManager.getHistory();

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
        Task task = new Task("Уборка", "а", 0, Status.NEW);
        int newId = taskManager.createTask(task);

        Epic epic = new Epic("Готовка", "а", 0, Status.NEW);
        int newIdEpic = taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Покупка продуктов", "а", 0, Status.NEW,
                epic.getId());
        int idSubtask = taskManager.createSubtask(subtask);
        Subtask subtask1 = new Subtask("Выбор рецептов", "а", 0, Status.NEW,
                epic.getId());

        int idSubtask2 = taskManager.createSubtask(subtask1);
        List<Task> listHistory = taskManager.getHistory();


        taskManager = taskManager.loadFromFile(file);
        List<Task> listLoadTask = taskManager.getAllTask();
        List<Epic> listLoadEpic = taskManager.getAllEpic();
        List<Subtask> listLoadSubtask = taskManager.getAllSubtask();
        List<Task> listLoadHistory = taskManager.getHistory();

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
        Task task = new Task("Уборка", "а", 0, Status.NEW);
        int newId = taskManager.createTask(task);
        taskManager.getTask(newId);

        Epic epic = new Epic("Готовка", "а", 0, Status.NEW);
        int newIdEpic = taskManager.createEpic(epic);
        taskManager.getEpic(newIdEpic);

        List<Task> listHistory = taskManager.getHistory();

        taskManager = taskManager.loadFromFile(file);
        List<Task> listLoadTask = taskManager.getAllTask();
        List<Epic> listLoadEpic = taskManager.getAllEpic();
        List<Task> listLoadHistory = taskManager.getHistory();


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
        taskManager.save();

        taskManager = taskManager.loadFromFile(file);
        List<Task> listLoadTask = taskManager.getAllTask();
        List<Epic> listLoadEpic = taskManager.getAllEpic();
        List<Subtask> listLoadSubtask = taskManager.getAllSubtask();
        List<Task> listLoadHistory = taskManager.getHistory();
        assertEquals(0, listLoadTask.size(), "Количество задач не совпадает.");
        assertEquals(0, listLoadEpic.size(), "Количество эпиков не совпадает.");
        assertEquals(0, listLoadSubtask.size(), "Количество подзадач не совпадает.");
        assertEquals(0, listLoadHistory.size(), "Количество историй не совпадает.");
    }

    @Test
    public void checkSaveAndLoadingTime() {
        Task task = new Task("Уборка", "а", 0, Status.NEW);
        int newId = taskManager.createTask(task);
        taskManager.getTask(newId);

        Epic epic = new Epic("Готовка", "а", 0, Status.NEW);
        int newIdEpic = taskManager.createEpic(epic);
        taskManager.getEpic(newIdEpic);

        Subtask subtask = new Subtask("Покупка продуктов", "а", 0, Status.NEW,
                epic.getId());
        int idSubtask = taskManager.createSubtask(subtask);
        taskManager.getSubtask(idSubtask);
        Subtask subtask1 = new Subtask("Выбор рецептов", "а", 0, Status.NEW,
                epic.getId());
        int idSubtask2 = taskManager.createSubtask(subtask1);
        Subtask task666 = new Subtask("66Уборка6", "а", 0, Status.NEW, epic.getId(), 20,
                LocalDateTime.of(2023, 4, 15, 14, 40));

        int id666 = taskManager.createSubtask(task666);
        taskManager.getEpic(id666);


        List<Task> listHistory = taskManager.getHistory();
        taskManager.save();

        taskManager = taskManager.loadFromFile(file);
        List<Task> listLoadTask = taskManager.getAllTask();
        List<Epic> listLoadEpic = taskManager.getAllEpic();
        List<Subtask> listLoadSubtask = taskManager.getAllSubtask();
        List<Task> listLoadHistory = taskManager.getHistory();

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