package ru.yandex.app.manager;

import ru.yandex.app.tasks.Epic;
import ru.yandex.app.tasks.Subtask;
import ru.yandex.app.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Manager {

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>(); //хэшмапы по айди и виду задачи
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int generatedId = 1;

    public int createTask(Task task) { // create - создать
        task.setId(generatedId++);
        int id = task.getId();
        task.setStatus("NEW");
        tasks.put(id, task);
        return id;
    }

    public int createEpic(Epic epic) { //создать большая задача
        epic.setStatus("NEW");
        int id = epic.getId();
        epic.setId(generatedId++);
        epics.put(epic.getId(), epic);
        return id;
    }

    public int createSubtask(Subtask subtask) { //создать подзадача


        // в сабтаск есть айди этого эпика, и мы по этому йд достаем
        // эпик из хешмапы и складываем ему туда в эрей лист подзадачу которою
        // которую только что создали
        // отмена записываем в аррай лист йд
// проверить что эпик заданный в сабтаск существует
        if (epics.get(subtask.getIdEpic()) != null) { //проверили чтосуществует эпик с таким айди
            subtask.setId(generatedId++);
            subtask.setStatus("NEW");
            subtasks.put(subtask.getId(), subtask);
            Epic saveEpic = epics.get(subtask.getIdEpic());
            saveEpic.addSubtaskIds(subtask.getId()); //положили в эррейлист эпика подзадачу
            statusEpic(saveEpic);
        } else {
            System.out.println("Такого эпика не существует");
        }
        return subtask.getId();
    }

    //    public void updateTask(tasks.Task task). Если вы храните эпики в HashMap, где ключами являются идентификаторы,
//    то обновление — это запись нового эпика ).
    public void updateTask(Task task) {  // обновление задачи
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task); //положили по старому айди, новую задачу не
            // статусы не нью
        } else {
            System.out.println("Невозможно обновить задачу");
        }
    }

    public void updateEpic(Epic epic) {  // обновление большой задачи
        if (epics.containsKey(epic.getId())) {
            Epic oldEpic = epics.get(epic.getId());
            oldEpic.setTitle(epic.getTitle());
            oldEpic.setContent(epic.getContent());
        } else {
            System.out.println("Невозможно обновить подзадачу");
        }
    }

    private void statusEpic(Epic epic) { //метод подсчета статуса эпика
        int statusNew = 0;
        int statusInProgress = 0;
        int statusDone = 0;
        ArrayList<Integer> listThis = epic.getSubtaskIds();
        if (listThis.size() > 0) {
            for (int list : listThis) {
                Subtask subtask = subtasks.get(list);
                if (subtask.getStatus() == "NEW") {
                    statusNew++;
                } else if (subtask.getStatus() == "IN_PROGRESS") {
                    statusInProgress++;
                } else if (subtask.getStatus() == "DONE") {
                    statusDone++;
                }
            }
            //если не все нью
            //если есть подзадачи
            if (statusNew == listThis.size()) {
                epic.setStatus("NEW");
            } else if (statusDone == listThis.size()) {
                epic.setStatus("DONE");
            } else {
                epic.setStatus("IN_PROGRESS");
            }
        } else {
            epic.setStatus("NEW"); //если подзадач нет, то статус нью
        }

    }

    public void updateSubtask(Subtask subtask) {// обновление подзадачи
        if ((subtasks.containsKey(subtask.getId())) &&
                (epics.containsKey(subtask.getIdEpic()))) {
            subtasks.put(subtask.getId(), subtask);
            //положили по старому айди, новую подзадачу // в эррейлист не добавляем, т.к. не меняется айди
            Epic epic = epics.get(subtask.getIdEpic()); //получили эпик по айди эпика привязанному
            statusEpic(epic);  //вызвали обновление статуса эпика
        }
    }

    public void deleteAllTask() { // удаление всех задач
        tasks.clear();
    }

    public void deleteAllEpic() {
        epics.clear();

        //по идее когда удаляешь все эпики, должны удальться и все сабтаски и чистить
        //эррей лист, я хочу вызвать метод удаления сабтасков в методе удаления эпиков, но ошибка сивол эпик не определен
        subtasks.clear();
    }

    public void deleteAllSubtask() { //
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.cleanSubtaskIds();
            statusEpic(epic);
        }
    }

    public ArrayList<Task> getAllTask() {  //получение списка всех задач
        ArrayList<Task> listTask = new ArrayList<>();
        for (Task value : tasks.values()) {
            listTask.add(value);
        }
        return listTask;
    }

    public ArrayList<Epic> getAllEpic() { //получение списка всех больших задач
        ArrayList<Epic> listEpic = new ArrayList<>();
        for (Epic value : epics.values()) {
            listEpic.add(value);
        }
        return listEpic;
    }

    public ArrayList<Subtask> getAllSubtask() { //получение списка всех подзадач
        ArrayList<Subtask> listSubtask = new ArrayList<>();
        for (Subtask value : subtasks.values()) {
            listSubtask.add(value);
        }
        return listSubtask;
    }

    public void deleteForIdTask(int id) { // удаление задачи по идентификатору
        tasks.remove(id);
    }

    public void deleteForIdEpic(int id) { // удаление  большой задачи по идентификатору
        Epic epic = epics.get(id);
        epics.remove(id);
        for (int deleteId : epic.getSubtaskIds()) {
            subtasks.remove(deleteId);
        }
    }

    public void deleteForIdSubtask(int id) { // удаление подзадачи по идентификатору
        Subtask subtask = subtasks.get(id);
        Epic epic = epics.get(subtask.getIdEpic());
        if (subtasks.remove(id) != null) {
            epic.removeSubtaskId(subtask.getId());
            statusEpic(epic);
        }
    }

    public List<Subtask> getEpicSubtasks(int id) {  //Получение списка всех подзадач определённого эпика
        ArrayList<Subtask> epicSubtasks = new ArrayList<>();
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            for (int subtaskId : epic.getSubtaskIds()) {
                epicSubtasks.add(subtasks.get(subtaskId));
            }
        }
        return epicSubtasks;
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }
}
