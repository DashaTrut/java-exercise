package ru.yandex.app.tasks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.app.manager.InMemoryTaskManager;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
    Epic epic = new Epic("Проверка", "аа", 12, Status.NEW);
   // inMemoryTaskManager.cre

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
        System.out.println(epic.getSubtaskIds());
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
        inMemoryTaskManager.updateEpic(epic);//или апдейт эпик
        System.out.println(epic.getSubtaskIds());
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
        inMemoryTaskManager.updateEpic(epic);//или апдейт эпик
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
        inMemoryTaskManager.updateEpic(epic);//или апдейт эпик
        Assertions.assertEquals(Status.IN_PROGRESS, epic.status);
    }
}

