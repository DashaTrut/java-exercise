public class Subtask extends Task {
    int idEpica; // поле содержит йд эпика, к которому привязано

    public Subtask(String title, String content, int id, String status, int idEpica) {
        super(title, content, id, status);
        this.idEpica = idEpica;
    }

}
