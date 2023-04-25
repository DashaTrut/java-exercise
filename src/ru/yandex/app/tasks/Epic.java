package ru.yandex.app.tasks;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private final ArrayList<Integer> subtaskIds = new ArrayList<>(); // поле которое хранит подзадачи

    public Epic(String title, String content, int id, Status status) {
        super(title, content, id, status);
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Epic otherEpic = (Epic) obj; // привели второй объект к классу Task
        return Objects.equals(title, otherEpic.title) && Objects.equals(content, otherEpic.content) &&
                Objects.equals(status, otherEpic.status) && Objects.equals(subtaskIds, otherEpic.subtaskIds)
                && (id == otherEpic.id);
    }
    public void addSubtaskIds(Integer id) {
        subtaskIds.add(id);
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    } //получаем список при  удалении эпика

    public  void cleanSubtaskIds() {//очищаем когда удаляем все подзадачи !!! вроде просто очищаем
        subtaskIds.clear();
    }
    public void removeSubtaskId(Integer id) { //удаляем когда удаляется по id подзадача
        subtaskIds.remove(id);
    }
    @Override
    public int hashCode() {
        int hash = 17;
        if (title != null) {
            hash = title.hashCode();
        }
        hash = hash * 31;
        if (content != null) {
            hash = hash + content.hashCode();
        }
        if (status != null) {
            hash = hash + status.hashCode();
        }
        return hash;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtaskIds=" + subtaskIds +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}
