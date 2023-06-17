package ru.yandex.app.manager;

import org.junit.jupiter.api.Test;
import ru.yandex.app.tasks.Epic;
import ru.yandex.app.tasks.Status;
import ru.yandex.app.tasks.Subtask;
import ru.yandex.app.tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.app.tasks.Status.NEW;

abstract class TaskManagerTest<T extends TaskManager> {


    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    @Test
    public void checkCreateTask() {
        Task task = new Task("Проверка тасков", "а", 11, NEW);
        final int taskId = inMemoryTaskManager.createTask(task);

        final Task savedTask = inMemoryTaskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = inMemoryTaskManager.getAllTask();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void checkCreateEpic() {
        Epic epic = new Epic("Проверка эпиков", "а", 10, NEW);
        final int epicId = inMemoryTaskManager.createEpic(epic);

        final Epic savedEpic = inMemoryTaskManager.getEpic(epicId);

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");

        final List<Epic> epics = inMemoryTaskManager.getAllEpic();

        assertNotNull(epics, "Задачи на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.get(0), "Задачи не совпадают.");
    }

    @Test
    public void checkCreateSubtask() {
        Epic epic = new Epic("Проверка эпиков", "а", 1, NEW);
        inMemoryTaskManager.createEpic(epic);
        Subtask subtask = new Subtask("Проверка эпиков", "а", 12, NEW, 1);
        final int subtaskId = inMemoryTaskManager.createSubtask(subtask);

        final Subtask savedSubtask = inMemoryTaskManager.getSubtask(subtaskId);

        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subtask, savedSubtask, "Задачи не совпадают.");

        final List<Subtask> subtasks = inMemoryTaskManager.getAllSubtask();

        assertNotNull(subtasks, "Задачи на возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество задач.");
        assertEquals(subtask, subtasks.get(0), "Задачи не совпадают.");
        List<Integer> idList = epic.getSubtaskIds();
        int id = idList.get(0);

        assertEquals(inMemoryTaskManager.getSubtask(id), subtask, "Подзадачи не совпадают.");
        assertEquals(inMemoryTaskManager.getEpic(subtask.getIdEpic()), epic, "Эпики не совпадают.");

    }

    @Test
    public void checkUpdateTask() {
        Task task = new Task("Проверка тасков", "а", 1, NEW);
        final int taskId = inMemoryTaskManager.createTask(task);
        Task task150 = new Task("Проверка тасков150", "а", 1, NEW);
        inMemoryTaskManager.updateTask(task150);

        final Task savedTask = inMemoryTaskManager.getTask(taskId);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task150, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = inMemoryTaskManager.getAllTask();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task150, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void checkUpdateVoidTask() {
        Task task150 = new Task("Проверка тасков150", "а", 1, NEW);
        inMemoryTaskManager.updateTask(task150);

        assertNull(inMemoryTaskManager.getTask(task150.getId()), "Задача не найдена.");
    }

    @Test
    public void checkUpdateEpic() {
        Epic epic = new Epic("Проверка эпиков", "а", 1, NEW);
        final int epicId = inMemoryTaskManager.createEpic(epic);
        Epic epic150 = new Epic("Проверка эпиков150", "а", 1, NEW);
        inMemoryTaskManager.updateEpic(epic150);

        final Epic savedEpic = inMemoryTaskManager.getEpic(epicId);

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic150, savedEpic, "Задачи не совпадают.");

        final List<Epic> epics = inMemoryTaskManager.getAllEpic();

        assertNotNull(epics, "Задачи на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic150, epics.get(0), "Задачи не совпадают.");
    }

    @Test
    public void checkUpdateVoidEpic() {
        Epic epic150 = new Epic("Проверка эпиков150", "а", 1, NEW);
        inMemoryTaskManager.updateTask(epic150);

        assertNull(inMemoryTaskManager.getEpic(epic150.getId()), "Задача не найдена.");
    }

    @Test
    public void checkUpdateSubtask() {
        Epic epic = new Epic("Проверка эпиков", "а", 1, NEW);
        inMemoryTaskManager.createEpic(epic);
        Subtask subtask = new Subtask("Проверка эпиков", "а", 2, NEW, 1);
        final int subtaskId = inMemoryTaskManager.createSubtask(subtask);
        Subtask subtask150 = new Subtask("Проверка эпиков150", "а", 2, NEW, 1);
        inMemoryTaskManager.updateSubtask(subtask150);

        final Subtask savedSubtask = inMemoryTaskManager.getSubtask(subtaskId);
        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subtask150, savedSubtask, "Задачи не совпадают.");

        final List<Subtask> subtasks = inMemoryTaskManager.getAllSubtask();

        assertNotNull(subtasks, "Задачи на возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество задач.");
        assertEquals(subtask150, subtasks.get(0), "Задачи не совпадают.");
        List<Integer> idList = epic.getSubtaskIds();
        int id = idList.get(0);

        assertEquals(inMemoryTaskManager.getSubtask(id), subtask150, "Подзадачи не совпадают.");
        assertEquals(inMemoryTaskManager.getEpic(subtask.getIdEpic()), epic, "Эпики не совпадают.");
    }

    @Test
    public void checkUpdateVoidSubtask() {
        Epic epic = new Epic("Проверка эпиков", "а", 1, NEW);
        inMemoryTaskManager.createEpic(epic);
        Subtask subtask150 = new Subtask("Проверка эпиков150", "а", 2, NEW, 1);
        inMemoryTaskManager.updateSubtask(subtask150);

        assertNull(inMemoryTaskManager.getSubtask(subtask150.getId()), "Задача не найдена.");
    }

    @Test
    public void checkUpdateSubtaskVoidEpic() {
        Subtask subtask = new Subtask("Проверка эпиков", "а", 2, NEW, 1);
        final int subtaskId = inMemoryTaskManager.createSubtask(subtask);
        Subtask subtask150 = new Subtask("Проверка эпиков150", "а", 2, NEW, 1);
        inMemoryTaskManager.updateSubtask(subtask150);

        assertNull(inMemoryTaskManager.getSubtask(subtask150.getId()), "Задача не найдена.");
    }
    @Test
    public void deleteAllTask() {
        Task task = new Task("Проверка тасков", "а", 1, NEW);
        final int taskId = inMemoryTaskManager.createTask(task);
        Task task150 = new Task("Проверка тасков150", "а", 1, NEW);
        final int taskId150 = inMemoryTaskManager.createTask(task150);
        inMemoryTaskManager.deleteAllTask();

        assertNull(inMemoryTaskManager.getTask(taskId), "Задача не удалена.");
        assertNull(inMemoryTaskManager.getTask(taskId150), "Задача не удалена.");

        final List<Task> tasks = inMemoryTaskManager.getAllTask();

        assertEquals(0, tasks.size(), "Неверное количество задач.");
    }
    @Test
    public void deleteAllEpic() {
        Epic epic = new Epic("Проверка эпиков", "а", 1, NEW);
        final int epicId = inMemoryTaskManager.createEpic(epic);
        Epic epic150 = new Epic("Проверка эпиков150", "а", 1, NEW);
        final int epicId150 = inMemoryTaskManager.createEpic(epic150);

        inMemoryTaskManager.deleteAllEpic();

        assertNull(inMemoryTaskManager.getEpic(epicId), "Задача не удалена.");
        assertNull(inMemoryTaskManager.getEpic(epicId150), "Задача не удалена.");

        final List<Epic> epics = inMemoryTaskManager.getAllEpic();

        assertNotNull(epics, "Задачи на возвращаются.");
        assertEquals(0, epics.size(), "Неверное количество задач.");
    }
    @Test
    public void deleteAllSubtask() {
        Epic epic = new Epic("Проверка эпиков", "а", 1, NEW);
        inMemoryTaskManager.createEpic(epic);
        Subtask subtask = new Subtask("Проверка эпиков", "а", 2, NEW, 1);
        final int subtaskId = inMemoryTaskManager.createSubtask(subtask);
        Subtask subtask150 = new Subtask("Проверка эпиков150", "а", 3, NEW, 1);
        final int subtaskId150 = inMemoryTaskManager.createSubtask(subtask150);
        inMemoryTaskManager.deleteAllSubtask();


        assertNull(inMemoryTaskManager.getSubtask(subtaskId), "Подзадача не удалена.");
        assertNull(inMemoryTaskManager.getSubtask(subtaskId150), "Подзадача не удалена.");

        final List<Subtask> subtasks = inMemoryTaskManager.getAllSubtask();

        assertEquals(0, subtasks.size(), "Неверное количество подзадач.");
        List<Integer> idList = epic.getSubtaskIds();
        assertEquals(0, idList.size(), "Подзадачи у эпика не удалены");
    }
    @Test
    public void getAllTaskNorm() {
        Task task = new Task("Проверка тасков", "а", 1, NEW);
        final int taskId = inMemoryTaskManager.createTask(task);
        Task task150 = new Task("Проверка тасков150", "а", 2, NEW);
        final int taskId150 = inMemoryTaskManager.createTask(task150);
        final List<Task> tasks = inMemoryTaskManager.getAllTask();

        assertEquals(2, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
        assertEquals(task150, tasks.get(1), "Задачи не совпадают.");
    }
    @Test
    public void getAllTaskVoid() {
        final List<Task> tasks = inMemoryTaskManager.getAllTask();
        assertEquals(0, tasks.size(), "Неверное количество задач.");
    }
    @Test
    public void getAllEpicNorm() {
        Epic epic = new Epic("Проверка эпиков", "а", 1, NEW);
        inMemoryTaskManager.createEpic(epic);
        Epic epic150 = new Epic("Проверка эпиков150", "а", 2, NEW);
        inMemoryTaskManager.createEpic(epic150);
        final List<Epic> epics = inMemoryTaskManager.getAllEpic();

        assertEquals(2, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.get(0), "Задачи не совпадают.");
        assertEquals(epic150, epics.get(1), "Задачи не совпадают.");
    }
    @Test
    public void getAllEpicVoid() {
        final List<Epic> epics = inMemoryTaskManager.getAllEpic();
        assertEquals(0, epics.size(), "Неверное количество задач.");
    }
    @Test
    public void getAllSubtaskNorm() {
        Epic epic = new Epic("Проверка эпиков", "а", 1, NEW);
        inMemoryTaskManager.createEpic(epic);
        Subtask subtask = new Subtask("Проверка эпиков", "а", 2, NEW, 1);
        inMemoryTaskManager.createSubtask(subtask);
        Subtask subtask150 = new Subtask("Проверка эпиков150", "а", 3, NEW, 1);
        inMemoryTaskManager.createSubtask(subtask150);
        final List<Subtask> subtasks = inMemoryTaskManager.getAllSubtask();

        assertEquals(2, subtasks.size(), "Неверное количество подзадач.");

        List<Integer> idList = epic.getSubtaskIds();
        assertEquals(2, idList.size(), "Подзадачи у эпика не определены");
    }

    @Test
    public void getAllSubtaskVoid() {
        Epic epic = new Epic("Проверка эпиков", "а", 1, NEW);
        inMemoryTaskManager.createEpic(epic);
        final List<Subtask> subtasks = inMemoryTaskManager.getAllSubtask();
        assertEquals(0, subtasks.size(), "Неверное количество задач.");

        List<Integer> idList = epic.getSubtaskIds();
        assertEquals(0, idList.size(), "Подзадачи у эпика определены");
    }
    @Test
    public void deleteForIdTaskNorm() {
        Task task = new Task("Проверка тасков", "а", 1, NEW);
        final int taskId = inMemoryTaskManager.createTask(task);
        Task task150 = new Task("Проверка тасков150", "а", 1, NEW);
        final int taskId150 = inMemoryTaskManager.createTask(task150);

        inMemoryTaskManager.deleteForIdTask(taskId);

        assertNull(inMemoryTaskManager.getTask(taskId), "Задача не удалена.");
        assertNotNull(inMemoryTaskManager.getTask(taskId150), "Удалена не та задача.");

        final List<Task> tasks = inMemoryTaskManager.getAllTask();
        assertEquals(1, tasks.size(), "Неверное количество задач.");
    }
    @Test
    public void deleteForIdTaskNotNorm() { //не понятно нужен ли такой тест
        final List<Task> tasks = inMemoryTaskManager.getAllTask();
        assertEquals(0, tasks.size(), "Неверное количество задач.");
        int taskId = 100;
        inMemoryTaskManager.deleteForIdTask(taskId);

        assertNull(inMemoryTaskManager.getTask(taskId), "Задача не удалена.");
        final List<Task> tasks2 = inMemoryTaskManager.getAllTask();
        assertEquals(0, tasks2.size(), "Неверное количество задач.");
    }
    @Test
    public void deleteForIdEpicNorm() {
        Epic epic = new Epic("Проверка эпиков", "а", 1, NEW);
        final int epicId = inMemoryTaskManager.createEpic(epic);
        Epic epic150 = new Epic("Проверка эпиков150", "а", 1, NEW);
        final int epicId150 = inMemoryTaskManager.createEpic(epic150);

        inMemoryTaskManager.deleteForIdEpic(epicId);

        assertNull(inMemoryTaskManager.getEpic(epicId), "Задача не удалена.");
        assertNotNull(inMemoryTaskManager.getEpic(epicId150), "Удалена не та задача.");

        final List<Epic> epics = inMemoryTaskManager.getAllEpic();
        assertEquals(1, epics.size(), "Неверное количество задач.");
    }
    @Test
    public void deleteForIdSubtaskNorm() {
        Epic epic = new Epic("Проверка эпиков", "а", 1, NEW);
        inMemoryTaskManager.createEpic(epic);
        Subtask subtask = new Subtask("Проверка эпиков", "а", 2, NEW, 1);
        final int subtaskId = inMemoryTaskManager.createSubtask(subtask);
        Subtask subtask150 = new Subtask("Проверка эпиков150", "а", 3, NEW, 1);
        final int subtaskId150 = inMemoryTaskManager.createSubtask(subtask150);
        List<Integer> idList = epic.getSubtaskIds();
        assertEquals(2, idList.size(), "Подзадачи у эпика не добавлены");
        inMemoryTaskManager.deleteForIdSubtask(subtaskId);

        assertNull(inMemoryTaskManager.getSubtask(subtaskId), "Подзадача не удалена.");
        assertNotNull(inMemoryTaskManager.getSubtask(subtaskId150), "Удалена не та Подзадача.");

        final List<Subtask> subtasks = inMemoryTaskManager.getAllSubtask();

        assertEquals(1, subtasks.size(), "Неверное количество подзадач.");
        List<Integer> idList2 = epic.getSubtaskIds();
        assertEquals(1, idList2.size(), "Подзадачи у эпика не удалены");
    }
    @Test
    public void getEpicSubtasksNorm(){
        Epic epic = new Epic("Проверка эпиков", "а", 1, NEW);
        inMemoryTaskManager.createEpic(epic);
        Subtask subtask = new Subtask("Проверка эпиков", "а", 2, NEW, 1);
        final int subtaskId = inMemoryTaskManager.createSubtask(subtask);
        Subtask subtask150 = new Subtask("Проверка эпиков150", "а", 3, NEW, 1);
        final int subtaskId150 = inMemoryTaskManager.createSubtask(subtask150);
        List<Integer> idList = epic.getSubtaskIds();
        assertEquals(2, idList.size(), "Подзадачи у эпика не удалены");

        assertEquals(subtaskId, idList.get(0), "Задачи не совпадают.");
        assertEquals(subtaskId150, idList.get(1), "Задачи не совпадают.");
    }
    @Test
    public void getTaskNorm() {
        Task task = new Task("Проверка тасков", "а", 1, NEW);
        final int taskId = inMemoryTaskManager.createTask(task);
        Task task150 = new Task("Проверка тасков150", "а", 2, NEW);
        final int taskId150 = inMemoryTaskManager.createTask(task150);

        final List<Task> tasks = inMemoryTaskManager.getAllTask();
        Task chekTask =  inMemoryTaskManager.getTask(taskId);
        assertEquals(chekTask, tasks.get(0), "Задачи не совпадают.");
        assertEquals(chekTask, task, "Задачи не совпадают.");


        Task chekTask150 =  inMemoryTaskManager.getTask(taskId150);
        assertEquals(chekTask150, tasks.get(1), "Задачи не совпадают.");
        assertEquals(chekTask150, task150, "Задачи не совпадают.");
    }
    @Test
    public void getTaskVoid() {
        int taskId = 100;
        final List<Task> tasks = inMemoryTaskManager.getAllTask();
        Task chekTask =  inMemoryTaskManager.getTask(taskId);
        assertEquals(0, tasks.size(), "Неверное количество задач.");
        assertNull(chekTask, "Задача найдена.");
    }
    @Test
    public void getEpicNorm() {
        Epic epic = new Epic("Проверка эпиков", "а", 1, NEW);
        final int epicId = inMemoryTaskManager.createEpic(epic);
        Epic epic150 = new Epic("Проверка эпиков150", "а", 1, NEW);
        final int epicId150 = inMemoryTaskManager.createEpic(epic150);
        Epic chekEpic =  inMemoryTaskManager.getEpic(epicId);
        final List<Epic> epics = inMemoryTaskManager.getAllEpic();

        assertEquals(chekEpic, epics.get(0), "Задачи не совпадают.");
        assertEquals(chekEpic, epic, "Задачи не совпадают.");
        assertNotNull(chekEpic, "Задача не существует.");

        Epic chekEpic150 =  inMemoryTaskManager.getEpic(epicId150);

        assertEquals(chekEpic150, epics.get(1), "Задачи не совпадают.");
        assertEquals(chekEpic150, epic150, "Задачи не совпадают.");
        assertNotNull(chekEpic150, "Задача не существует.");
    }
    @Test
    public void getEpicVoid() {
        int epicId = 100;
        final List<Epic> epics = inMemoryTaskManager.getAllEpic();
        Epic chekEpic =  inMemoryTaskManager.getEpic(epicId);
        assertEquals(0, epics.size(), "Неверное количество задач.");
        assertNull(chekEpic, "Задача найдена.");
    }
    @Test
    public void getSubtaskNorm() {
        Epic epic = new Epic("Проверка эпиков", "а", 1, NEW);
        inMemoryTaskManager.createEpic(epic);
        Subtask subtask = new Subtask("Проверка эпиков", "а", 2, NEW, 1);
        int subtaskId = inMemoryTaskManager.createSubtask(subtask);
        Subtask subtask150 = new Subtask("Проверка эпиков150", "а", 3, NEW, 1);
        int subtaskId150 = inMemoryTaskManager.createSubtask(subtask150);
        Subtask chekSubtask =  inMemoryTaskManager.getSubtask(subtaskId);

        final List<Subtask> subtasks = inMemoryTaskManager.getAllSubtask();
        assertEquals(chekSubtask, subtasks.get(0), "Задачи не совпадают.");
        assertEquals(chekSubtask, subtask, "Задачи не совпадают.");
        assertNotNull(chekSubtask, "Задача не существует.");

        Subtask chekSubtask150 =  inMemoryTaskManager.getSubtask(subtaskId150);
        assertEquals(chekSubtask150, subtasks.get(1), "Задачи не совпадают.");
        assertEquals(chekSubtask150, subtask150, "Задачи не совпадают.");
        assertNotNull(chekSubtask150, "Задача не существует.");

    }
    @Test
    public void getSubtaskVoid() {
        int subtaskId = 100;
        final List<Subtask> subtasks = inMemoryTaskManager.getAllSubtask();
        Subtask chekSubtask =  inMemoryTaskManager.getSubtask(subtaskId);
        assertEquals(0, subtasks.size(), "Неверное количество задач.");
        assertNull(chekSubtask, "Задача найдена.");
    }

    @Test
    public void getHistoryNorm() {
        Task task = new Task("Проверка тасков", "а", 1, NEW);
        final int taskId = inMemoryTaskManager.createTask(task);
        Task task150 = new Task("Проверка тасков150", "а", 2, NEW);
        final int taskId150 = inMemoryTaskManager.createTask(task150);

        Task chekTask =  inMemoryTaskManager.getTask(taskId); //!!!!

        Epic epic = new Epic("Проверка эпиков", "а", 3, NEW);
        int epicId = inMemoryTaskManager.createEpic(epic);
        Epic chekEpic =  inMemoryTaskManager.getEpic(epicId); //!!!!

        Subtask subtask = new Subtask("Проверка эпиков", "а", 4, NEW, 3);
        int subtaskId = inMemoryTaskManager.createSubtask(subtask);
        Subtask subtask150 = new Subtask("Проверка эпиков150", "а", 5, NEW, 3);
        int subtaskId150 = inMemoryTaskManager.createSubtask(subtask150);
        Subtask chekSubtask =  inMemoryTaskManager.getSubtask(subtaskId); //!!!!!!

        List<Task> historyList = inMemoryTaskManager.getHistory();

        assertEquals(chekSubtask, historyList.get(2), "Задачи не совпадают.");
        assertEquals(chekTask, historyList.get(0), "Задачи не совпадают.");
        assertEquals(chekEpic, historyList.get(1), "Задачи не совпадают.");
        assertNotNull(historyList, "История задач пустая.");
    }

    @Test
    public void getHistoryVoid() {
        int subtaskId = 100;
        Subtask chekSubtask =  inMemoryTaskManager.getSubtask(subtaskId); //!!!!!!

        List<Task> historyList = inMemoryTaskManager.getHistory();
        assertEquals(historyList.size(), 0, "История задач не пустая.");
    }

}
