package ru.yandex.app.manager;

import ru.yandex.app.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final ArrayList<Task> historyTask = new ArrayList<>();

    @Override
    public void addHistory(Task task) { // добавляет задачу в историю
        if (!task.equals(null)) {
            historyTask.add(task);
            if (historyTask.size() < 10) {
                historyTask.remove(0);
            }
        }

    }

    @Override
    public ArrayList<Task> getHistory() { //выдает эррей лист и проверяет что он не более 10позиций
        return historyTask;
    }
}
