package ru.yandex.app.manager;

import ru.yandex.app.tasks.Epic;
import ru.yandex.app.tasks.Subtask;
import ru.yandex.app.tasks.Task;

import java.util.List;

public interface TaskManager {
    int createTask(Task task);

    int createEpic(Epic epic);

    int createSubtask(Subtask subtask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    void deleteAllTask();

    void deleteAllEpic();

    void deleteAllSubtask();

    List<Task> getAllTask();

    List<Epic> getAllEpic();

    List<Subtask> getAllSubtask();

    void deleteForIdTask(int id);

    void deleteForIdEpic(int id);

    void deleteForIdSubtask(int id);

    List<Subtask> getEpicSubtasks(int id);

    Task getTask(int id);

    Epic getEpic(int id);

    Subtask getSubtask(int id);

    List<Task> getHistory();

    List<Task> getPrioritizedTasks();
}

