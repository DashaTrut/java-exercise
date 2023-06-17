package ru.yandex.app.manager;

import org.junit.jupiter.api.Test;
import ru.yandex.app.tasks.Epic;
import ru.yandex.app.tasks.Status;
import ru.yandex.app.tasks.Subtask;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.app.tasks.Status.*;

class InMemoryTasksManagerTest extends TaskManagerTest {

    @Test
    public void chekStatusEpicNormNew() {
        Epic epic = new Epic("Проверка эпиков", "а", 1, NEW);
        inMemoryTaskManager.createEpic(epic);
        Subtask subtask = new Subtask("Проверка эпиков", "а", 2, NEW, 1);
        inMemoryTaskManager.createSubtask(subtask);
        Subtask subtask150 = new Subtask("Проверка эпиков150", "а", 3, NEW, 1);
        inMemoryTaskManager.createSubtask(subtask150);

        assertEquals(epic.getStatus(), NEW, "Неверный статус.");

        List<Integer> idList = epic.getSubtaskIds();
        assertEquals(2, idList.size(), "Подзадачи у эпика не определены");
    }

    @Test
    public void chekStatusEpicNormDone() {
        Epic epic = new Epic("Проверка эпиков", "а", 1, NEW);
        inMemoryTaskManager.createEpic(epic);
        Subtask subtask = new Subtask("Проверка эпиков", "а", 2, NEW, 1);
        inMemoryTaskManager.createSubtask(subtask);
        subtask.setStatus(DONE);
        inMemoryTaskManager.updateSubtask(subtask);
        Subtask subtask150 = new Subtask("Проверка эпиков150", "а", 3, NEW, 1);
        inMemoryTaskManager.createSubtask(subtask150);
        subtask150.setStatus(DONE);
        inMemoryTaskManager.updateSubtask(subtask150);

        assertEquals(epic.getStatus(), DONE, "Неверный статус.");

        List<Integer> idList = epic.getSubtaskIds();
        assertEquals(2, idList.size(), "Подзадачи у эпика не определены");
    }

    @Test
    public void chekStatusEpicNormNewAndDone() {
        Epic epic = new Epic("Проверка эпиков", "а", 1, NEW);
        inMemoryTaskManager.createEpic(epic);
        Subtask subtask = new Subtask("Проверка эпиков", "а", 2, NEW, 1);
        inMemoryTaskManager.createSubtask(subtask);
        subtask.setStatus(DONE);
        inMemoryTaskManager.updateSubtask(subtask);
        Subtask subtask150 = new Subtask("Проверка эпиков150", "а", 3, NEW, 1);
        inMemoryTaskManager.createSubtask(subtask150);

        assertEquals(epic.getStatus(), IN_PROGRESS, "Неверный статус.");

        List<Integer> idList = epic.getSubtaskIds();
        assertEquals(2, idList.size(), "Подзадачи у эпика не определены");
    }
    @Test
    public void chekStatusEpicNormVoid() {
        Epic epic = new Epic("Проверка эпиков", "а", 1, NEW);
        inMemoryTaskManager.createEpic(epic);

        inMemoryTaskManager.updateEpic(epic);

        assertEquals(epic.getStatus(), NEW, "Неверный статус.");

        List<Integer> idList = epic.getSubtaskIds();
        assertEquals(0, idList.size(), "Подзадачи у эпика определены");
    }

}



