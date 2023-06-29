package ru.yandex.app.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.app.manager.InMemoryTaskManager;
import ru.yandex.app.tasks.Epic;
import ru.yandex.app.tasks.Status;
import ru.yandex.app.tasks.Subtask;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
    Epic epic = new Epic("Проверка", "аа", 12, Status.NEW);


    @Test
    public void calculationStatusEpicNotExistingSubtask() {
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.updateEpic(epic);
        Assertions.assertEquals(Status.NEW, epic.status);
    }

    @Test
    public void calculationStatusEpicWithSubtaskNew() {
        inMemoryTaskManager.createEpic(epic);
        Subtask subtask1 = new Subtask("Проверка сабтасков1", "а", 13, Status.NEW, epic.getId());
        inMemoryTaskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Проверка сабтасков2", "а", 14, Status.NEW, epic.getId());
        inMemoryTaskManager.createSubtask(subtask2);
        inMemoryTaskManager.updateSubtask(subtask2);
        Assertions.assertEquals(Status.NEW, epic.status);
    }

    @Test
    public void calculationStatusEpicWithSubtaskDone() {
        inMemoryTaskManager.createEpic(epic);
        Subtask subtask1 = new Subtask("Проверка сабтасков3", "а", 13, Status.NEW, epic.getId());
        inMemoryTaskManager.createSubtask(subtask1);
        subtask1.setStatus(Status.DONE);
        Subtask subtask2 = new Subtask("Проверка сабтасков4", "а", 14, Status.NEW, epic.getId());
        inMemoryTaskManager.createSubtask(subtask2);
        subtask2.setStatus(Status.DONE);
        inMemoryTaskManager.updateSubtask(subtask2);//или апдейт эпик
        Assertions.assertEquals(Status.DONE, epic.status);
    }

    @Test
    public void calculationStatusEpicWithSubtaskNewAndDone() {
        inMemoryTaskManager.createEpic(epic);
        Subtask subtask1 = new Subtask("Проверка сабтасков1", "а", 13, Status.NEW, epic.getId());
        inMemoryTaskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Проверка сабтасков2", "а", 14, Status.NEW, epic.getId());
        inMemoryTaskManager.createSubtask(subtask2);
        subtask2.setStatus(Status.DONE);
        inMemoryTaskManager.updateSubtask(subtask2);//или апдейт эпик
        Assertions.assertEquals(Status.IN_PROGRESS, epic.status);
    }

    @Test
    public void calculationStatusEpicWithSubtaskNewAndInProcess() {
        inMemoryTaskManager.createEpic(epic);
        Subtask subtask1 = new Subtask("Проверка сабтасков1", "а", 13, Status.NEW, epic.getId());
        inMemoryTaskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Проверка сабтасков2", "а", 14, Status.NEW, epic.getId());
        inMemoryTaskManager.createSubtask(subtask2);
        subtask2.setStatus(Status.IN_PROGRESS);
        inMemoryTaskManager.updateSubtask(subtask2);//или апдейт эпик
        Assertions.assertEquals(Status.IN_PROGRESS, epic.status);
    }

    @Test
    public void checkEpicTimeNorm() {
        inMemoryTaskManager.createEpic(epic);
        Subtask subtask1 = new Subtask("Проверка сабтасков1", "а", 13, Status.NEW, epic.getId(), 20,
                LocalDateTime.of(2023, 4, 15, 14, 40));
        inMemoryTaskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Проверка сабтасков2", "а", 14, Status.NEW, epic.getId(), 40,
                LocalDateTime.of(2023, 4, 10, 14, 20));
        inMemoryTaskManager.createSubtask(subtask2);
        Assertions.assertEquals(subtask2.getStartTime(), epic.getStartTime());
        Assertions.assertEquals(subtask1.getEndTime(), epic.getEndTime());
        Assertions.assertEquals(subtask1.getDuration() + subtask2.getDuration(), epic.getDuration());
        Assertions.assertEquals(subtask2.getStartTime(), epic.getStartTime());
    }

    @Test
    public void checkEpicTimeVoid() {
        inMemoryTaskManager.createEpic(epic);
        Subtask subtask1 = new Subtask("Проверка сабтасков1", "а", 13, Status.NEW, epic.getId());
        inMemoryTaskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Проверка сабтасков2", "а", 14, Status.NEW, epic.getId());
        inMemoryTaskManager.createSubtask(subtask2);
        Assertions.assertEquals(subtask2.getStartTime(), epic.getStartTime());
        Assertions.assertEquals(subtask1.getEndTime(), epic.getEndTime());
        Assertions.assertEquals(subtask1.getDuration() + subtask2.getDuration(), epic.getDuration());
        Assertions.assertEquals(subtask2.getStartTime(), epic.getStartTime());
    }

    @Test
    public void checkEpicTimeIdentical() {
        inMemoryTaskManager.createEpic(epic);
        Subtask subtask1 = new Subtask("Проверка сабтасков1", "а", 13, Status.NEW, epic.getId(), 20,
                LocalDateTime.of(2023, 4, 15, 14, 40));
        inMemoryTaskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Проверка сабтасков2", "а", 14, Status.NEW, epic.getId(), 20,
                LocalDateTime.of(2023, 4, 15, 14, 40));
        inMemoryTaskManager.createSubtask(subtask2);
        List<Integer> subtaskList = epic.getSubtaskIds();
        Assertions.assertEquals(1, subtaskList.size());
        Assertions.assertEquals(subtask1.getStartTime(), epic.getStartTime());
        Assertions.assertEquals(subtask1.getEndTime(), epic.getEndTime());
        Assertions.assertEquals(subtask1.getDuration(), epic.getDuration());

    }
}

