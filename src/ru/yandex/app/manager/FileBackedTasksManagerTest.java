package ru.yandex.app.manager;

import org.junit.jupiter.api.Test;
import ru.yandex.app.tasks.Epic;
import ru.yandex.app.tasks.Status;
import ru.yandex.app.tasks.Subtask;
import ru.yandex.app.tasks.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.app.tasks.TypeTask.*;

class FileBackedTasksManagerTest extends TaskManagerTest{
    final File file = new File("./BestDatabase.csv");
    FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);

    @Test
    public void checkSaveAndLoadNorm() {
        Task task = new Task("Уборка", "а", 0, Status.NEW);
        int newId = fileBackedTasksManager.createTask(task);
        fileBackedTasksManager.getTask(newId);

        Epic epic = new Epic("Готовка", "а", 0, Status.NEW);
        int newIdEpic = fileBackedTasksManager.createEpic(epic);
        fileBackedTasksManager.getEpic(newIdEpic);

        Subtask subtask = new Subtask("Покупка продуктов", "а", 0, Status.NEW,
                epic.getId());
        int idSubtask = fileBackedTasksManager.createSubtask(subtask);
        fileBackedTasksManager.getSubtask(idSubtask);
        Subtask subtask1 = new Subtask("Выбор рецептов", "а", 0, Status.NEW,
                epic.getId());

        int idSubtask2 = fileBackedTasksManager.createSubtask(subtask1);
        List<Task> listHistory = fileBackedTasksManager.getHistory();
        fileBackedTasksManager.save();

        FileBackedTasksManager fileBackedTasksManager2 = FileBackedTasksManager.loadFromFile(file);
        List<Task> listLoadTask = fileBackedTasksManager2.getAllTask();
        List<Epic> listLoadEpic = fileBackedTasksManager2.getAllEpic();
        List<Subtask> listLoadSubtask = fileBackedTasksManager2.getAllSubtask();
        List<Task> listLoadHistory = fileBackedTasksManager2.getHistory();

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
        int newId = fileBackedTasksManager.createTask(task);

        Epic epic = new Epic("Готовка", "а", 0, Status.NEW);
        int newIdEpic = fileBackedTasksManager.createEpic(epic);

        Subtask subtask = new Subtask("Покупка продуктов", "а", 0, Status.NEW,
                epic.getId());
        int idSubtask = fileBackedTasksManager.createSubtask(subtask);
        Subtask subtask1 = new Subtask("Выбор рецептов", "а", 0, Status.NEW,
                epic.getId());

        int idSubtask2 = fileBackedTasksManager.createSubtask(subtask1);
        List<Task> listHistory = fileBackedTasksManager.getHistory();
        fileBackedTasksManager.save();


        FileBackedTasksManager fileBackedTasksManager2 = FileBackedTasksManager.loadFromFile(file);
        List<Task> listLoadTask = fileBackedTasksManager2.getAllTask();
        List<Epic> listLoadEpic = fileBackedTasksManager2.getAllEpic();
        List<Subtask> listLoadSubtask = fileBackedTasksManager2.getAllSubtask();
        List<Task> listLoadHistory = fileBackedTasksManager2.getHistory();

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
        int newId = fileBackedTasksManager.createTask(task);
        fileBackedTasksManager.getTask(newId);

        Epic epic = new Epic("Готовка", "а", 0, Status.NEW);
        int newIdEpic = fileBackedTasksManager.createEpic(epic);
        fileBackedTasksManager.getEpic(newIdEpic);

        List<Task> listHistory = fileBackedTasksManager.getHistory();
        fileBackedTasksManager.save();

        FileBackedTasksManager fileBackedTasksManager2 = FileBackedTasksManager.loadFromFile(file);
        List<Task> listLoadTask = fileBackedTasksManager2.getAllTask();
        List<Epic> listLoadEpic = fileBackedTasksManager2.getAllEpic();
        List<Subtask> listLoadSubtask = fileBackedTasksManager2.getAllSubtask();
        List<Task> listLoadHistory = fileBackedTasksManager2.getHistory();


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
        fileBackedTasksManager.save();

        FileBackedTasksManager fileBackedTasksManager2 = FileBackedTasksManager.loadFromFile(file);
        List<Task> listLoadTask = fileBackedTasksManager2.getAllTask();
        List<Epic> listLoadEpic = fileBackedTasksManager2.getAllEpic();
        List<Subtask> listLoadSubtask = fileBackedTasksManager2.getAllSubtask();
        List<Task> listLoadHistory = fileBackedTasksManager2.getHistory();
        assertEquals(0,  listLoadTask.size(), "Количество задач не совпадает.");
        assertEquals(0, listLoadEpic.size(), "Количество эпиков не совпадает.");
        assertEquals(0, listLoadSubtask.size(), "Количество подзадач не совпадает.");
        assertEquals(0, listLoadHistory.size(), "Количество историй не совпадает.");
    }
}