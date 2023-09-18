package ru.yandex.app.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.yandex.app.manager.FileBackedTasksManager;
import ru.yandex.app.tasks.Epic;
import ru.yandex.app.tasks.Subtask;
import ru.yandex.app.tasks.Task;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {
    private final Gson gson = new Gson();
    ClientKVS client;

    public HttpTaskManager(File file, Boolean load) {
        super(file);
        client = new ClientKVS();
        if (load) {
            loadKVServer();
        }
    }


    @Override
    public void save() {
        client.serverSave("tasks", gson.toJson(new ArrayList<>(tasks.values())));
        client.serverSave("epics", gson.toJson(new ArrayList<>(epics.values()))); //значение это список всех эпиков в формате джейсон
        client.serverSave("subtasks", gson.toJson(new ArrayList<>(subtasks.values()))); //значение это список всех подзадач в формате джейсон
        client.serverSave("history", gson.toJson(getHistory())); //значение это список всех историй в формате джейсон
    }

    public void loadKVServer() {
        Type taskType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        ArrayList<Task> loadedTasks = gson.fromJson(client.serverLoad("tasks"), taskType);

        if (!loadedTasks.isEmpty()) {
            for (int i = 0; i < loadedTasks.size(); i++) {
                Task task = loadedTasks.get(i);
                int id = task.getId();
                if (id > generatedId) {
                    generatedId = id + 1;
                }
                tasks.put(id, task);
                prioritizedTasks.add(task);
            }
        }
        Type epicType = new TypeToken<ArrayList<Epic>>() {
        }.getType();
        ArrayList<Epic> loadedEpics = gson.fromJson(client.serverLoad("epics"), epicType);

        if (!loadedEpics.isEmpty()) {
            for (int i = 0; i < loadedEpics.size(); i++) {
                Epic epic = loadedEpics.get(i);
                int id = epic.getId();
                if (id > generatedId) {
                    generatedId = id + 1;
                }
                epics.put(id, epic);
            }
        }

        Type subtType = new TypeToken<ArrayList<Subtask>>() {
        }.getType();
        ArrayList<Subtask> loadedSubt = gson.fromJson(client.serverLoad("subtasks"), subtType);

        if (!loadedSubt.isEmpty()) {
            for (int i = 0; i < loadedSubt.size(); i++) {
                Subtask subt = loadedSubt.get(i);
                int id = subt.getId();
                if (id > generatedId) {
                    generatedId = id + 1;
                }
                subtasks.put(id, subt);
                prioritizedTasks.add(subt);
            }
        }

        Type historyType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        ArrayList<Task> loadedHistory = gson.fromJson(client.serverLoad("history"), historyType);

        if (!loadedHistory.isEmpty()) {
            for (int i = 0; i < loadedHistory.size(); i++) {
                Task task = loadedHistory.get(i);
                int id = task.getId();
                if (tasks.containsKey(id)) {
                    inMemoryHistoryManager.addHistory(tasks.get(id));
                }
                if (subtasks.containsKey(id)) {
                    inMemoryHistoryManager.addHistory(subtasks.get(id));
                }
                if (epics.containsKey(id)) {
                    inMemoryHistoryManager.addHistory(epics.get(id));
                }
            }

        }

    }
}
