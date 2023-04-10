package tasks;

import java.util.Objects;

public class Task {
    protected String title;



    protected String content;
    protected int id;
    protected String status;

    public Task(String title, String content, int id, String status) {
        this.title = title;
        this.content = content;
        this.id = id;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
//            Статус, отображающий её прогресс. Мы будем выделять следующие этапы жизни задачи:
//    NEW — задача только создана, но к её выполнению ещё не приступили.
//            IN_PROGRESS — над задачей ведётся работа.
//    DONE — задача выполнена.
public String getTitle() {
    return title;
}

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Task otherTask = (Task) obj; // привели второй объект к классу Task
        return Objects.equals(title, otherTask.title) && Objects.equals(content, otherTask.content) &&
                Objects.equals(status, otherTask.status) && (id == otherTask.id);
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
        return "Task{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}
