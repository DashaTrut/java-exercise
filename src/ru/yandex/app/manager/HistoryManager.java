package ru.yandex.app.manager;

import ru.yandex.app.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface HistoryManager {
    void addHistory(Task task);

    void remove(int id);

    List<Task> getHistory();


}
