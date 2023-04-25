package ru.yandex.app.tasks;

import java.util.Objects;

public class Subtask extends Task {


    private int idEpic; // поле содержит йд эпика, к которому привязано

    public Subtask(String title, String content, int id, Status status, int idEpic) {
        super(title, content, id, status);
        this.idEpic = idEpic;
    }
    public int getIdEpic() {
        return idEpic;
    }
    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Subtask otherSubtask = (Subtask) obj; // привели второй объект к классу Subtask
        return Objects.equals(title, otherSubtask.title) && Objects.equals(content, otherSubtask.content) &&
                Objects.equals(status, otherSubtask.status) && (id == otherSubtask.id) &&
                (idEpic == otherSubtask.idEpic);
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
        return "Subtask{" +
                "idEpic=" + idEpic +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}
