package ru.yandex.app.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.app.manager.InMemoryTaskManager;

import ru.yandex.app.tasks.Epic;
import ru.yandex.app.tasks.Status;
import ru.yandex.app.tasks.Subtask;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.app.tasks.Status.*;

class InMemoryTasksManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager();

    }

    @Test
    public void checkStatusEpicNormNew() {
        Epic epic = new Epic("Проверка эпиков", "а", 1, NEW);
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Проверка эпиков", "а", 2, NEW, 1);
        taskManager.createSubtask(subtask);
        Subtask subtask150 = new Subtask("Проверка эпиков150", "а", 3, NEW, 1);
        taskManager.createSubtask(subtask150);

        assertEquals(epic.getStatus(), NEW, "Неверный статус.");

        List<Integer> idList = epic.getSubtaskIds();
        assertEquals(2, idList.size(), "Подзадачи у эпика не определены");
    }

    @Test
    public void checkStatusEpicNormDone() {
        Epic epic = new Epic("Проверка эпиков", "а", 1, NEW);
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Проверка эпиков", "а", 2, NEW, 1);
        taskManager.createSubtask(subtask);
        subtask.setStatus(DONE);
        taskManager.updateSubtask(subtask);
        Subtask subtask150 = new Subtask("Проверка эпиков150", "а", 3, NEW, 1);
        taskManager.createSubtask(subtask150);
        subtask150.setStatus(DONE);
        taskManager.updateSubtask(subtask150);

        assertEquals(epic.getStatus(), DONE, "Неверный статус.");

        List<Integer> idList = epic.getSubtaskIds();
        assertEquals(2, idList.size(), "Подзадачи у эпика не определены");
    }

    @Test
    public void checkStatusEpicNormNewAndDone() {
        Epic epic = new Epic("Проверка эпиков", "а", 1, NEW);
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Проверка эпиков", "а", 2, NEW, 1);
        taskManager.createSubtask(subtask);
        subtask.setStatus(DONE);
        taskManager.updateSubtask(subtask);
        Subtask subtask150 = new Subtask("Проверка эпиков150", "а", 3, NEW, 1);
        taskManager.createSubtask(subtask150);

        assertEquals(epic.getStatus(), IN_PROGRESS, "Неверный статус.");

        List<Integer> idList = epic.getSubtaskIds();
        assertEquals(2, idList.size(), "Подзадачи у эпика не определены");
    }

    @Test
    public void checkStatusEpicNormVoid() {
        Epic epic = new Epic("Проверка эпиков", "а", 1, NEW);
        taskManager.createEpic(epic);

        taskManager.updateEpic(epic);

        assertEquals(epic.getStatus(), NEW, "Неверный статус.");

        List<Integer> idList = epic.getSubtaskIds();
        assertEquals(0, idList.size(), "Подзадачи у эпика определены");
    }

}



