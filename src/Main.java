public class Main {

    public static void main(String[] args) {

        System.out.println("Поехали!");
        Manager manager = new Manager();
        Task task = new Task("Уборка", "убрать всю квартиру", 0, "NEW");
        manager.createTask(task);
        manager.getAllTask();
        System.out.println(task.getId());

        Epic epic = new Epic("Готовка", "Готовка обедов ужинов", 0, "NEW");
        manager.createEpic(epic);
        Subtask subtask = new Subtask("Покупка продуктов", "Выбрать и оплатить продукты", 0, "новый",
                epic.getId());
        manager.createSubtask(subtask);
        Subtask subtask1 = new Subtask("Выбор рецептов", "Выбрать что готовить", 0, "новый",
                epic.getId());
        manager.createSubtask(subtask1);
        manager.updateEpic(epic);
        System.out.println(epic.status);
        subtask1.status = "DONE";
        subtask.status = "DONE";
        manager.updateEpic(epic);
        System.out.println(epic.status);
        manager.listSubtask(epic);
        Epic epic2 = new Epic("Проверка", "Проверка работоспособности", 0, "NEW");
        manager.createEpic(epic2);
        Subtask subtask2 = new Subtask("Создание сабтасков", "Создать и заполнить", 0, "новый",
                epic2.getId());
        manager.createSubtask(subtask2);
        Subtask subtask21 = new Subtask("Удаление", "Удалить сабтаск", 0, "новый",
                epic2.getId());

        manager.createSubtask(subtask21);
        System.out.println(epic2.subtaskIds);
        manager.deleteForIdSubtask(subtask21);
        manager.deleteForIdEpic(epic2);
        System.out.println(manager.epics);
        manager.getAllSubtask();

    }
}