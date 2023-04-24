package ru.yandex.app.manager;
import ru.yandex.app.tasks.Task;
import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static ArrayList<Task> historyTask = new ArrayList<>();
    @Override
    public void addHistory(Task task){ // добавляет задачу в историю
        historyTask.add(task);
    }
    @Override
    public ArrayList<Task> getHistory() { //выдает эррей лист и проверяет что он не более 10позиций
        if (historyTask.size() < 11){
            historyTask.remove(1);
        }
     return historyTask;
    }
}
