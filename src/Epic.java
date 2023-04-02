import java.util.ArrayList;

public class Epic extends Task {
    public ArrayList<Integer> subtaskIds  = new ArrayList<>(); // поле которое хранит подзадачи

    public Epic(String title, String content, int id, String status) {
        super(title, content, id, status);
    }


}
