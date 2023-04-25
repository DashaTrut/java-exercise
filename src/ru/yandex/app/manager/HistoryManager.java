package ru.yandex.app.manager;

import ru.yandex.app.tasks.Task;

import java.util.ArrayList;

public interface HistoryManager {
    void addHistory(Task task);

    ArrayList<Task> getHistory();

}
