package ru.yandex.app.manager;

import ru.yandex.app.tasks.Task;

import java.util.ArrayList;

public interface HistoryManager {
    public void addHistory(Task task);
    public ArrayList<Task> getHistory();

}
