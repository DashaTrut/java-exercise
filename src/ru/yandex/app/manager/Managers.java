package ru.yandex.app.manager;

import ru.yandex.app.server.HttpTaskManager;

import java.io.File;

public class Managers {
    public static HttpTaskManager getDefault() {
        return new HttpTaskManager(new File("./BestDatabase.csv"), false);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
