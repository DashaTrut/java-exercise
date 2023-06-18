package ru.yandex.app.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task implements Comparable<Task>{
    protected String title;


    protected String content;
    protected int id;
    protected Status status;
    protected TypeTask type = TypeTask.TASK;
    protected int duration ;//продолжительность задачи, оценка того, сколько времени она займёт в минутах (число);
    protected LocalDateTime startTime ;// дата, когда предполагается приступить к выполнению задачи.



    public Task(String title, String content, int id, Status status, int duration, LocalDateTime startTime) {
        this.title = title;
        this.content = content;
        this.id = id;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(String title, String content, int id, Status status) {
        this.title = title;
        this.content = content;
        this.id = id;
        this.status = status;
        this.duration = 0;
        this.startTime = null;
    }

    public TypeTask getType() {
        return type;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Task otherTask = (Task) obj; // привели второй объект к классу Task
        return Objects.equals(title, otherTask.title) && Objects.equals(content, otherTask.content) && Objects.equals(status, otherTask.status) && (id == otherTask.id);
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
        return "Task{" + "title='" + title + '\'' + ", content='" + content + '\'' + ", id=" + id + ", status='" + status + '\'' + '}';
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        if (getStartTime() != null) {
            Duration duration1 = Duration.ofMinutes(duration);
            LocalDateTime getEndTime = startTime.plus(duration1);
            return getEndTime;
        }
        return null;
    }

    @Override
    public int compareTo(Task task) {
    int a = 0;
    if (task.startTime.equals(null)){
        return 1;
    } else if (this.getStartTime().isBefore(task.getStartTime())){
        return 1;
    } else if (this.getStartTime().isAfter(task.getStartTime())){
        return -1;
    } else {
        return 0;
    }
    }
//    Интерфейс Comparable содержит один единственный метод int compareTo(E item), который сравнивает текущий объект с
//    объектом, переданным в качестве параметра. Если этот метод возвращает отрицательное число, то текущий объект будет
//    располагаться перед тем, который передается через параметр. Если метод вернет положительное число, то, наоборот, п
//    осле второго объекта. Если метод возвратит ноль, значит, оба объекта равны.
}
