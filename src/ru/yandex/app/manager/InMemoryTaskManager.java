package ru.yandex.app.manager;

import ru.yandex.app.tasks.Epic;
import ru.yandex.app.tasks.Status;
import ru.yandex.app.tasks.Subtask;
import ru.yandex.app.tasks.Task;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;


public class InMemoryTaskManager implements TaskManager {

    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>(); //хэшмапы по айди и виду задачи
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected int generatedId = 1;
    protected final HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();
    protected final Set<Task> prioritizedTasks = new TreeSet<>();

    @Override
    public int createTask(Task task) { // create - создать
        if ((task.getStartTime() != null) && (!timeCrossing(task)) || (task.getStartTime() == null)) {
            task.setId(generatedId++);
            int id = task.getId();
            task.setStatus(Status.NEW);
            tasks.put(id, task);

            prioritizedTasks.add(task);
            return id;
        } else System.out.println("Время выполнения совпадает");
        return -1;
    }

    private boolean timeCrossing(Task task) {
        if (task.getStartTime() != null) {
            final LocalDateTime startTime = task.getStartTime();
            final LocalDateTime endTime = task.getEndTime();
            for (Task t : prioritizedTasks) {
                if (t.getStartTime() != null) {
                    final LocalDateTime existStart = t.getStartTime();
                    final LocalDateTime existEnd = t.getEndTime();
                    if (!endTime.isAfter(existStart)) {// newTimeEnd <= existTimeStart
                        continue;
                    }
                    if (!existEnd.isAfter(startTime)) {// existTimeEnd <= newTimeStart
                        continue;
                    }
                    return true;//есть пересечение
                }
            }
        }
        return false;//нет пересения

    }


    @Override
    public int createEpic(Epic epic) { //создать большая задача
        epic.setId(generatedId++);
        epic.setStatus(Status.NEW);
        int id = epic.getId();
        epics.put(epic.getId(), epic);

        return id;
    }

    @Override
    public int createSubtask(Subtask subtask) { //создать подзадача

        if (epics.get(subtask.getIdEpic()) != null) { //проверили чтосуществует эпик с таким айди
            if ((subtask.getStartTime() != null) && (!timeCrossing(subtask)) || (subtask.getStartTime() == null)) {
                subtask.setId(generatedId++);
                subtask.setStatus(Status.NEW);
                subtasks.put(subtask.getId(), subtask);
                Epic saveEpic = epics.get(subtask.getIdEpic());
                saveEpic.addSubtaskIds(subtask.getId()); //положили в эррейлист эпика подзадачу
                statusEpic(saveEpic);
                startAndEndTimeSubtaskForEpic(saveEpic);

                prioritizedTasks.add(subtask);
            } else {
                return -1;
            }
        } else {
            System.out.println("Такого эпика не существует");
            return -1;
        }
        return subtask.getId();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);

    }

    @Override
    public void updateTask(Task task) {  // обновление задачи
        if (tasks.containsKey(task.getId()) && (!timeCrossing(task))) {
            prioritizedTasks.remove(tasks.get(task.getId()));
            tasks.put(task.getId(), task); //положили по старому айди, новую задачу не
            prioritizedTasks.add(task);
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


    private void startAndEndTimeSubtaskForEpic(Epic epic) { //может быть войд
        int durationEpic = 0;
        int minIdStartSubtask = 0;
        int maxIdStartSubtask = 0;
        ArrayList<Integer> listThis = epic.getSubtaskIds();
        if (listThis.size() > 0) {

            for (int i = 0; i < listThis.size(); i++) {
                if ((subtasks.get(listThis.get(i))).getStartTime() != null) {
                    int list = listThis.get(i);
                    Subtask subtask = subtasks.get(list);

                    if (minIdStartSubtask == 0) {
                        minIdStartSubtask = list;

                    } else if (minIdStartSubtask != 0) {
                        if (subtask.getStartTime().isBefore(subtasks.get(minIdStartSubtask).getStartTime()))
                            minIdStartSubtask = subtask.getId();
                    }
                    if (maxIdStartSubtask == 0) {
                        maxIdStartSubtask = subtask.getId();

                    } else if (subtask.getEndTime().isAfter((subtasks.get(maxIdStartSubtask).getStartTime()))) {
                        maxIdStartSubtask = subtask.getId();
                    }


                    durationEpic = durationEpic + subtask.getDuration();
                }
            }
        }
        if (maxIdStartSubtask != 0) {
            epic.setDuration(durationEpic);
            epic.setEndTime((subtasks.get(maxIdStartSubtask)).getEndTime());
            epic.setStartTime((subtasks.get(minIdStartSubtask)).getStartTime());
        } else {
            epic.setStartTime(null);
            epic.setEndTime(null);
        }
    }

    public LocalDateTime endTimeSubtaskForEpic(Epic epic) { //только эндтайм
        int maxIdStartSubtask = 0;
        ArrayList<Integer> listThis = epic.getSubtaskIds();
        if ((listThis.size() > 0)) {

            for (int i = 0; i < listThis.size(); i++) {
                if (subtasks.get(listThis.get(i)).getEndTime() != null) {
                    int list = listThis.get(i);
                    Subtask subtask = subtasks.get(list);

                    if (maxIdStartSubtask == 0) {
                        maxIdStartSubtask = subtask.getId();

                    } else if (subtask.getEndTime().isAfter((subtasks.get(maxIdStartSubtask).getStartTime()))) {
                        maxIdStartSubtask = subtask.getId();
                    }

                    Subtask subtaskReturn = subtasks.get(maxIdStartSubtask);
                    epic.setEndTime(subtaskReturn.getEndTime());
                    return subtaskReturn.getEndTime();
                }
            }
        }
        epic.setEndTime(null);
        return null;
    }

    private void statusEpic(Epic epic) { //метод подсчета статуса эпика
        int statusNew = 0;
        int statusDone = 0;
        ArrayList<Integer> listThis = epic.getSubtaskIds();
        if (listThis.size() > 0) {
            for (int list : listThis) {
                Subtask subtask = subtasks.get(list);
                if (subtask.getStatus() == Status.NEW) {
                    statusNew++;
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
                (epics.containsKey(subtask.getIdEpic())) && (!timeCrossing(subtask))) {
            prioritizedTasks.remove(subtasks.get(subtask.getId()));
            subtasks.put(subtask.getId(), subtask);
            prioritizedTasks.add(subtask);
            //положили по старому айди, новую подзадачу // в эррейлист не добавляем, т.к. не меняется айди
            Epic epic = epics.get(subtask.getIdEpic()); //получили эпик по айди эпика привязанному
            statusEpic(epic);  //вызвали обновление статуса эпика
            startAndEndTimeSubtaskForEpic(epic);
        }
    }

    @Override
    public void deleteAllTask() { // удаление всех задач
        for (Task task : tasks.values()) {
            inMemoryHistoryManager.remove(task.getId());
            prioritizedTasks.remove(task);
        }
        tasks.clear();
    }

    @Override
    public void deleteAllEpic() {

        for (Epic epic : epics.values()) {
            inMemoryHistoryManager.remove(epic.getId());
            prioritizedTasks.remove(epic);
        }
        epics.clear();    //по идее когда удаляешь все эпики, должны удальться и все сабтаски и чистить
        //эррей лист, я хочу вызвать метод удаления сабтасков в методе удаления эпиков, но ошибка сивол эпик не определен
        for (Subtask subtask : subtasks.values()) {
            inMemoryHistoryManager.remove(subtask.getId());
            prioritizedTasks.remove(subtask);
        }
        subtasks.clear();

    }

    @Override
    public void deleteAllSubtask() { //

        for (Subtask subtask : subtasks.values()) {
            inMemoryHistoryManager.remove(subtask.getId());
        }

        for (Epic epic : epics.values()) {
            epic.cleanSubtaskIds();
            statusEpic(epic);
            startAndEndTimeSubtaskForEpic(epic);
        }
        subtasks.clear();
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
        if (tasks.containsKey(id)) {
            prioritizedTasks.remove(tasks.get(id));
            tasks.remove(id);
            inMemoryHistoryManager.remove(id);
        }
    }

    @Override
    public void deleteForIdEpic(int id) { // удаление  большой задачи по идентификатору
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            epics.remove(id);
            inMemoryHistoryManager.remove(id);
            if (epic.getSubtaskIds() != null) {
                for (int deleteId : epic.getSubtaskIds()) {
                    prioritizedTasks.remove(subtasks.get(deleteId));
                    subtasks.remove(deleteId);
                    inMemoryHistoryManager.remove(deleteId);
                }
            }
        }
    }

    @Override
    public void deleteForIdSubtask(int id) { // удаление подзадачи по идентификатору

        Subtask subtask = subtasks.get(id);
        Epic epic = epics.get(subtask.getIdEpic());
        if (subtasks.remove(id) != null) {
            prioritizedTasks.remove(subtask);
            epic.removeSubtaskId(subtask.getId());

            inMemoryHistoryManager.remove(id);

            statusEpic(epic);
            startAndEndTimeSubtaskForEpic(epic);
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
