package ru.yandex.app.manager;

import ru.yandex.app.tasks.Epic;
import ru.yandex.app.tasks.Status;
import ru.yandex.app.tasks.Subtask;
import ru.yandex.app.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>(); //хэшмапы по айди и виду задачи
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int generatedId = 1;
    private final HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();

    @Override
    public int createTask(Task task) { // create - создать
        task.setId(generatedId++);
        int id = task.getId();
        task.setStatus(Status.NEW);
        tasks.put(id, task);
        return id;
    }

    @Override
    public int createEpic(Epic epic) { //создать большая задача
        epic.setStatus(Status.NEW);
        int id = epic.getId();
        epic.setId(generatedId++);
        epics.put(epic.getId(), epic);
        return id;
    }

    @Override
    public int createSubtask(Subtask subtask) { //создать подзадача


        // в сабтаск есть айди этого эпика, и мы по этому йд достаем
        // эпик из хешмапы и складываем ему туда в эрей лист подзадачу которою
        // которую только что создали
        // отмена записываем в аррай лист йд
// проверить что эпик заданный в сабтаск существует
        if (epics.get(subtask.getIdEpic()) != null) { //проверили чтосуществует эпик с таким айди
            subtask.setId(generatedId++);
            subtask.setStatus(Status.NEW);
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
    @Override
    public void updateTask(Task task) {  // обновление задачи
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task); //положили по старому айди, новую задачу не
            // статусы не нью
        } else {
            System.out.println("Невозможно обновить задачу");
        }
    }

    @Override
    public void updateEpic(Epic epic) {  // обновление большой задачи
        if (epics.containsKey(epic.getId())) {
            Epic oldEpic = epics.get(epic.getId());
            oldEpic.setTitle(epic.getTitle());
            oldEpic.setContent(epic.getContent());
        } else {
            System.out.println("Невозможно обновить подзадачу");
        }
    }

    @Override
    public void statusEpic(Epic epic) { //метод подсчета статуса эпика
        int statusNew = 0;
        int statusInProgress = 0;
        int statusDone = 0;
        ArrayList<Integer> listThis = epic.getSubtaskIds();
        if (listThis.size() > 0) {
            for (int list : listThis) {
                Subtask subtask = subtasks.get(list);
                if (subtask.getStatus() == Status.NEW) {
                    statusNew++;
                } else if (subtask.getStatus() == Status.IN_PROGRESS) {
                    statusInProgress++;
                } else if (subtask.getStatus() == Status.DONE) {
                    statusDone++;
                }
            }
            //если не все нью
            //если есть подзадачи
            if (statusNew == listThis.size()) {
                epic.setStatus(Status.NEW);
            } else if (statusDone == listThis.size()) {
                epic.setStatus(Status.DONE);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        } else {
            epic.setStatus(Status.NEW); //если подзадач нет, то статус нью
        }

    }

    @Override
    public void updateSubtask(Subtask subtask) {// обновление подзадачи
        if ((subtasks.containsKey(subtask.getId())) &&
                (epics.containsKey(subtask.getIdEpic()))) {
            subtasks.put(subtask.getId(), subtask);
            //положили по старому айди, новую подзадачу // в эррейлист не добавляем, т.к. не меняется айди
            Epic epic = epics.get(subtask.getIdEpic()); //получили эпик по айди эпика привязанному
            statusEpic(epic);  //вызвали обновление статуса эпика
        }
    }

    @Override
    public void deleteAllTask() { // удаление всех задач
        tasks.clear();
    }

    @Override
    public void deleteAllEpic() {
        epics.clear();

        //по идее когда удаляешь все эпики, должны удальться и все сабтаски и чистить
        //эррей лист, я хочу вызвать метод удаления сабтасков в методе удаления эпиков, но ошибка сивол эпик не определен
        subtasks.clear();
    }

    @Override
    public void deleteAllSubtask() { //
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.cleanSubtaskIds();
            statusEpic(epic);
        }
    }

    @Override
    public List<Task> getAllTask() {  //получение списка всех задач
        ArrayList<Task> listTask = new ArrayList<>();
        for (Task value : tasks.values()) {
            listTask.add(value);
        }
        return listTask;
    }

    @Override
    public List<Epic> getAllEpic() { //получение списка всех больших задач
        ArrayList<Epic> listEpic = new ArrayList<>();
        for (Epic value : epics.values()) {
            listEpic.add(value);
        }
        return listEpic;
    }

    @Override
    public List<Subtask> getAllSubtask() { //получение списка всех подзадач
        ArrayList<Subtask> listSubtask = new ArrayList<>();
        for (Subtask value : subtasks.values()) {
            listSubtask.add(value);
        }
        return listSubtask;
    }

    @Override
    public void deleteForIdTask(int id) { // удаление задачи по идентификатору
        tasks.remove(id);
    }

    @Override
    public void deleteForIdEpic(int id) { // удаление  большой задачи по идентификатору
        Epic epic = epics.get(id);
        epics.remove(id);
        for (int deleteId : epic.getSubtaskIds()) {
            subtasks.remove(deleteId);
        }
    }

    @Override
    public void deleteForIdSubtask(int id) { // удаление подзадачи по идентификатору
        Subtask subtask = subtasks.get(id);
        Epic epic = epics.get(subtask.getIdEpic());
        if (subtasks.remove(id) != null) {
            epic.removeSubtaskId(subtask.getId());
            statusEpic(epic);
        }
    }

    @Override
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

    @Override
    public Task getTask(int id) {
        inMemoryHistoryManager.addHistory(tasks.get(id)); //получение таска по айди
        return tasks.get(id);
    }

    @Override
    public Epic getEpic(int id) { //получение эпика по айди
        inMemoryHistoryManager.addHistory(epics.get(id));
        return epics.get(id);
    }
    @Override
    public Subtask getSubtask(int id) {
        inMemoryHistoryManager.addHistory(subtasks.get(id)); //получение таска по айди
        return subtasks.get(id);
    }

    @Override
    public List<Task> getHistory() { //отдает список истории
        return inMemoryHistoryManager.getHistory();
    }
}
