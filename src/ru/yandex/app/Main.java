package ru.yandex.app;
import ru.yandex.app.manager.InMemoryTaskManager;
import ru.yandex.app.manager.Managers;
import ru.yandex.app.manager.TaskManager;
import ru.yandex.app.tasks.Epic;
import ru.yandex.app.tasks.Subtask;
import ru.yandex.app.tasks.Task;
import ru.yandex.app.manager.Status;

public class Main {

    public static void main(String[] args) {

        System.out.println("Поехали!");

        TaskManager manager = Managers.getDefault();
        Task task = new Task("Уборка", "убрать всю квартиру", 0, Status.NEW);
        int newId = manager.createTask(task);
        manager.getTask(newId);
        //manager.getAllTask();
        System.out.println(task);

        Epic epic = new Epic("Готовка", "Готовка обедов ужинов", 0, Status.NEW);
        manager.createEpic(epic);
        Subtask subtask = new Subtask("Покупка продуктов", "Выбрать и оплатить продукты", 0, Status.NEW,
                epic.getId());
        manager.createSubtask(subtask);
        Subtask subtask1 = new Subtask("Выбор рецептов", "Выбрать что готовить", 0, Status.NEW,
                epic.getId());
        manager.createSubtask(subtask1);
        manager.getEpicSubtasks(epic.getId());
        System.out.println(manager.getHistory());
        manager.updateEpic(epic);
        System.out.println(epic);
        subtask1.setStatus(Status.DONE);
        subtask.setStatus(Status.DONE);
        manager.updateEpic(epic);
        System.out.println(epic);
        manager.getEpicSubtasks(epic.getId());
        Epic epic2 = new Epic("Проверка", "Проверка работоспособности", 0, Status.NEW);
        manager.createEpic(epic2);
        Subtask subtask2 = new Subtask("Создание сабтасков", "Создать и заполнить", 0, Status.NEW,
                epic2.getId());
        manager.createSubtask(subtask2);
        Subtask subtask21 = new Subtask("Удаление", "Удалить сабтаск", 0, Status.NEW,
                epic2.getId());

        manager.createSubtask(subtask21);
        System.out.println(epic2.getSubtaskIds());
        System.out.println(manager.getEpicSubtasks(epic2.getId()));
        manager.deleteForIdSubtask(subtask21.getId());
        manager.deleteForIdEpic(epic2.getId());
        System.out.println(manager.getAllEpic());
        System.out.println(manager.getAllSubtask());
        System.out.println(manager.getEpic(epic.getId()));
        System.out.println(manager.getHistory());

    }
}
