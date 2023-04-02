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
}
