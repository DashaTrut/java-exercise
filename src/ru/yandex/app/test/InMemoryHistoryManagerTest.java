package ru.yandex.app.test;

import org.junit.jupiter.api.Test;
import ru.yandex.app.manager.InMemoryHistoryManager;
import ru.yandex.app.tasks.Epic;
import ru.yandex.app.tasks.Status;
import ru.yandex.app.tasks.Subtask;
import ru.yandex.app.tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();

    @Test
    public void addHistory() {
        Task task = new Task("Уборка", "а", 1, Status.NEW);
        inMemoryHistoryManager.addHistory(task);
        List<Task> history = inMemoryHistoryManager.getHistory();
        Task checkTask = history.get(0);

        assertEquals(task, checkTask, "Задачи не совпадают.");
        assertEquals(1, history.size(), "Количество историй не совпадает.");
        assertNotNull(history, "История не пустая.");
        assertNotNull(checkTask, "Задача не существует.");


    }

    @Test
    public void addHistoryDuplication() {
        Task task = new Task("Уборка", "а", 1, Status.NEW);
        inMemoryHistoryManager.addHistory(task);

        Epic epic = new Epic("Готовка", "а", 2, Status.NEW);
        inMemoryHistoryManager.addHistory(epic);
        inMemoryHistoryManager.addHistory(task);

        List<Task> history = inMemoryHistoryManager.getHistory();
        Task checkTask = history.get(1);

        assertEquals(task, checkTask, "Задачи не совпадают.");
        assertEquals(2, history.size(), "Количество историй не совпадает.");
        assertNotNull(history, "История не пустая.");
        assertNotNull(checkTask, "Задача не существует.");
    }

    @Test
    public void removeHistoryStart() {
        Task task = new Task("Уборка", "а", 1, Status.NEW);
        inMemoryHistoryManager.addHistory(task);
        Epic epic = new Epic("Готовка", "а", 2, Status.NEW);
        inMemoryHistoryManager.addHistory(epic);

        Epic epic2 = new Epic("Готовка", "а", 3, Status.NEW);
        inMemoryHistoryManager.addHistory(epic2);
        Subtask subtask = new Subtask("Покупка продуктов", "а", 4, Status.NEW,
                epic.getId());
        inMemoryHistoryManager.addHistory(subtask);


        List<Task> history = inMemoryHistoryManager.getHistory();
        Task checkTask = history.get(0);
        inMemoryHistoryManager.remove(task.getId());
        List<Task> historyAfterRemove = inMemoryHistoryManager.getHistory();
        Task check = historyAfterRemove.get(0);
        assertNotEquals(check, checkTask, "Задачи совпадают.");
        assertEquals(4, history.size(), "Количество историй не совпадает.");
        assertEquals(3, historyAfterRemove.size(), "Количество историй после удаления не совпадает.");
    }

    @Test
    public void removeHistoryMiddle() {
        Task task = new Task("Уборка", "а", 1, Status.NEW);
        inMemoryHistoryManager.addHistory(task);
        Epic epic = new Epic("Готовка", "а", 2, Status.NEW);
        inMemoryHistoryManager.addHistory(epic);

        Epic epic2 = new Epic("Готовка", "а", 3, Status.NEW);
        inMemoryHistoryManager.addHistory(epic2);
        Subtask subtask = new Subtask("Покупка продуктов", "а", 4, Status.NEW,
                epic.getId());
        inMemoryHistoryManager.addHistory(subtask);


        List<Task> history = inMemoryHistoryManager.getHistory();
        Task checkEpic = history.get(2);
        inMemoryHistoryManager.remove(epic2.getId());
        List<Task> historyAfterRemove = inMemoryHistoryManager.getHistory();
        Task check = historyAfterRemove.get(2);
        assertNotEquals(check, checkEpic, "Задачи совпадают.");
        assertEquals(4, history.size(), "Количество историй не совпадает.");
        assertEquals(3, historyAfterRemove.size(), "Количество историй после удаления не совпадает.");
    }


    @Test
    public void removeHistoryLast() {
        Task task = new Task("Уборка", "а", 1, Status.NEW);
        inMemoryHistoryManager.addHistory(task);
        Epic epic = new Epic("Готовка", "а", 2, Status.NEW);
        inMemoryHistoryManager.addHistory(epic);

        Epic epic2 = new Epic("Готовка", "а", 3, Status.NEW);
        inMemoryHistoryManager.addHistory(epic2);
        Subtask subtask = new Subtask("Покупка продуктов", "а", 4, Status.NEW,
                epic.getId());
        inMemoryHistoryManager.addHistory(subtask);


        List<Task> history = inMemoryHistoryManager.getHistory();
        inMemoryHistoryManager.remove(subtask.getId());
        List<Task> historyAfterRemove = inMemoryHistoryManager.getHistory();
        assertEquals(4, history.size(), "Количество историй не совпадает.");
        assertEquals(3, historyAfterRemove.size(), "Количество историй после удаления не совпадает.");
    }


}