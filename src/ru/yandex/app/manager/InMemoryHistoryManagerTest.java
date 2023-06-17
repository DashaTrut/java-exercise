package ru.yandex.app.manager;

import org.junit.jupiter.api.Test;
import ru.yandex.app.tasks.Epic;
import ru.yandex.app.tasks.Status;
import ru.yandex.app.tasks.Subtask;
import ru.yandex.app.tasks.Task;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    final File file = new File("./BestDatabase.csv");
    InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
    FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
@Test
    public void addHistory(){
    Task task = new Task("Уборка", "а", 0, Status.NEW);
    int newId = fileBackedTasksManager.createTask(task);
    inMemoryHistoryManager.addHistory(task);
    List<Task> history = inMemoryHistoryManager.getHistory();
    Task checkTask = history.get(0);

    assertEquals(task,  checkTask, "Задачи не совпадают.");
    assertEquals(1, history.size(), "Количество историй не совпадает.");
    assertNotNull(history, "История не пустая.");
    assertNotNull(checkTask, "Задача не существует.");


    }

    @Test
    public void addHistoryDuplication(){
        Task task = new Task("Уборка", "а", 0, Status.NEW);
        int newId = fileBackedTasksManager.createTask(task);
        inMemoryHistoryManager.addHistory(task);

        Epic epic = new Epic("Готовка", "а", 0, Status.NEW);
        int newIdEpic = fileBackedTasksManager.createEpic(epic);
        inMemoryHistoryManager.addHistory(epic);
        inMemoryHistoryManager.addHistory(task);

        List<Task> history = inMemoryHistoryManager.getHistory();
        Task checkTask = history.get(1);

        assertEquals(task,  checkTask, "Задачи не совпадают.");
        assertEquals(2, history.size(), "Количество историй не совпадает.");
        assertNotNull(history, "История не пустая.");
        assertNotNull(checkTask, "Задача не существует.");
    }

    @Test
    public void removeHistoryStart(){
        Task task = new Task("Уборка", "а", 0, Status.NEW);
        int newId = fileBackedTasksManager.createTask(task);
        inMemoryHistoryManager.addHistory(task);
        Epic epic = new Epic("Готовка", "а", 0, Status.NEW);
        int newIdEpic = fileBackedTasksManager.createEpic(epic);
        inMemoryHistoryManager.addHistory(epic);

        Epic epic2 = new Epic("Готовка", "а", 0, Status.NEW);
        int newIdEpic2 = fileBackedTasksManager.createEpic(epic2);
        inMemoryHistoryManager.addHistory(epic2);
        Subtask subtask = new Subtask("Покупка продуктов", "а", 0, Status.NEW,
                epic.getId());
        int idSubtask = fileBackedTasksManager.createSubtask(subtask);
        inMemoryHistoryManager.addHistory(subtask);


        List<Task> history = inMemoryHistoryManager.getHistory();
        Task checkTask = history.get(0);
        inMemoryHistoryManager.remove(newId);
        List<Task> historyAfterRemove = inMemoryHistoryManager.getHistory();
        Task check = historyAfterRemove.get(0);
        assertNotEquals(check,  checkTask, "Задачи совпадают.");
        assertEquals(4, history.size(), "Количество историй не совпадает.");
        assertEquals(3, historyAfterRemove.size(), "Количество историй после удаления не совпадает.");
    }
    @Test
    public void removeHistoryMiddle(){
        Task task = new Task("Уборка", "а", 0, Status.NEW);
        int newId = fileBackedTasksManager.createTask(task);
        inMemoryHistoryManager.addHistory(task);
        Epic epic = new Epic("Готовка", "а", 0, Status.NEW);
        int newIdEpic = fileBackedTasksManager.createEpic(epic);
        inMemoryHistoryManager.addHistory(epic);

        Epic epic2 = new Epic("Готовка", "а", 0, Status.NEW);
        int newIdEpic2 = fileBackedTasksManager.createEpic(epic2);
        inMemoryHistoryManager.addHistory(epic2);
        Subtask subtask = new Subtask("Покупка продуктов", "а", 0, Status.NEW,
                epic.getId());
        int idSubtask = fileBackedTasksManager.createSubtask(subtask);
        inMemoryHistoryManager.addHistory(subtask);


        List<Task> history = inMemoryHistoryManager.getHistory();
        Task checkEpic = history.get(1);
        inMemoryHistoryManager.remove(newIdEpic);
        List<Task> historyAfterRemove = inMemoryHistoryManager.getHistory();
        Task check = historyAfterRemove.get(1);
        assertNotEquals(check,  checkEpic, "Задачи совпадают.");
        assertEquals(4, history.size(), "Количество историй не совпадает.");
        assertEquals(3, historyAfterRemove.size(), "Количество историй после удаления не совпадает.");
    }


    @Test
    public void removeHistoryLast(){
        Task task = new Task("Уборка", "а", 0, Status.NEW);
        int newId = fileBackedTasksManager.createTask(task);
        inMemoryHistoryManager.addHistory(task);
        Epic epic = new Epic("Готовка", "а", 0, Status.NEW);
        int newIdEpic = fileBackedTasksManager.createEpic(epic);
        inMemoryHistoryManager.addHistory(epic);

        Epic epic2 = new Epic("Готовка", "а", 0, Status.NEW);
        int newIdEpic2 = fileBackedTasksManager.createEpic(epic2);
        inMemoryHistoryManager.addHistory(epic2);
        Subtask subtask = new Subtask("Покупка продуктов", "а", 0, Status.NEW,
                epic.getId());
        int idSubtask = fileBackedTasksManager.createSubtask(subtask);
        inMemoryHistoryManager.addHistory(subtask);


        List<Task> history = inMemoryHistoryManager.getHistory();
        inMemoryHistoryManager.remove(idSubtask);
        List<Task> historyAfterRemove = inMemoryHistoryManager.getHistory();
        assertEquals(4, history.size(), "Количество историй не совпадает.");
        assertEquals(3, historyAfterRemove.size(), "Количество историй после удаления не совпадает.");
    }
}