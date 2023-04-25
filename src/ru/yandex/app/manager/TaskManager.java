package ru.yandex.app.manager;

import ru.yandex.app.tasks.Epic;
import ru.yandex.app.tasks.Subtask;
import ru.yandex.app.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    public int createTask(Task task);

    public int createEpic(Epic epic);

    public int createSubtask(Subtask subtask);

    public void updateTask(Task task);

    public void updateEpic(Epic epic);

    public void statusEpic(Epic epic);

    public void updateSubtask(Subtask subtask);

    public void deleteAllTask();

    public void deleteAllEpic();

    public void deleteAllSubtask();

    public List<Task> getAllTask();

    public List<Epic> getAllEpic();

    public List<Subtask> getAllSubtask();

    public void deleteForIdTask(int id);

    public void deleteForIdEpic(int id);

    public void deleteForIdSubtask(int id);

    public List<Subtask> getEpicSubtasks(int id);

    public Task getTask(int id);

    public Epic getEpic(int id);

    public Subtask getSubtask(int id);

    public List<Task> getHistory();
}

