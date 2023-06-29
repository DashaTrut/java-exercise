package ru.yandex.app;

import ru.yandex.app.manager.Managers;
import ru.yandex.app.manager.TaskManager;
import ru.yandex.app.tasks.Epic;
import ru.yandex.app.tasks.Subtask;
import ru.yandex.app.tasks.Task;
import ru.yandex.app.tasks.Status;

public class Main {

    public static void main(String[] args) {

        System.out.println("Поехали!");


        TaskManager manager = Managers.getDefault();
        System.out.println("!!!!!!!" + manager.getHistory());
        Task task = new Task("Уборка", "а", 0, Status.NEW);
        int newId = manager.createTask(task);
        manager.getTask(newId);
        System.out.println(task);

        Epic epic = new Epic("Готовка", "а", 0, Status.NEW);
        manager.createEpic(epic);
        Subtask subtask = new Subtask("Покупка продуктов", "а", 0, Status.NEW,
                epic.getId());
        manager.createSubtask(subtask);
        Subtask subtask1 = new Subtask("Выбор рецептов", "а", 0, Status.NEW,
                epic.getId());
        manager.createSubtask(subtask1);
        manager.getEpicSubtasks(epic.getId());
        System.out.println("!!!!!!!" + manager.getHistory()); //добавила знаки,чтобы читаемо стало
        manager.updateEpic(epic);
        System.out.println(epic);
        subtask1.setStatus(Status.DONE);
        subtask.setStatus(Status.DONE);
        manager.updateEpic(epic);
        System.out.println(epic);
        manager.getEpicSubtasks(epic.getId());
        Epic epic2 = new Epic("Проверка", "а", 0, Status.NEW);
        manager.createEpic(epic2);
        Subtask subtask2 = new Subtask("Создание сабтасков", "а", 0, Status.NEW,
                epic2.getId());
        manager.createSubtask(subtask2);
        Subtask subtask21 = new Subtask("Удаление", "а", 0, Status.NEW,
                epic2.getId());

        manager.createSubtask(subtask21);
        System.out.println(epic2.getSubtaskIds());
        System.out.println(manager.getEpicSubtasks(epic2.getId()));
        System.out.println(manager.getSubtask(subtask21.getId()));
        System.out.println("!!!!!!!" + manager.getHistory());
        manager.deleteForIdSubtask(subtask21.getId());
        System.out.println("!!!!!!!" + manager.getHistory());
        manager.deleteForIdEpic(epic2.getId());
        System.out.println(manager.getAllEpic());
        System.out.println(manager.getAllSubtask());
        System.out.println(manager.getEpic(epic.getId()));
        System.out.println("!!!!!!!" + manager.getHistory());
        manager.deleteAllEpic();
        manager.deleteAllSubtask();
        manager.deleteAllTask();
        Epic epic15 = new Epic("Доделать код", "а", 0, Status.NEW);
        manager.createEpic(epic15);
        manager.getEpic(epic15.getId());
        System.out.println("!!!!!!!" + manager.getHistory());


    }
}
