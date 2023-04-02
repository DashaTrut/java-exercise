import java.util.ArrayList;
import java.util.HashMap;

public class Manager {

    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>(); //хэшмапы по айди и виду задачи
    HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int generatedId = 1;

    public void createTask(Task task) { // create - создать
        task.setId(generatedId++);
        int id = task.getId();
        task.status = "NEW";
        tasks.put(id, task);
    }
    public void createEpic(Epic epic) { //создать большая задача
        epic.status = "NEW";
        epic.setId(generatedId++);
        epics.put(epic.getId(), epic);
    }
    public void createSubtask(Subtask subtask) { //создать подзадача
        // в сабтаск есть айди этого эпика, и мы по этому йд достаем
        // эпик из хешмапы и складываем ему туда в эрей лист подзадачу которою
        // которую только что создали
        // отмена записываем в аррай лист йд
// проверить что эпик заданный в сабтаск существует
        if (subtask.idEpica != 0 ) { //проверили чтосуществует эпик с таким айди
            subtask.setId(generatedId++);
            subtask.status = "NEW";
            int id = subtask.getId();
            subtasks.put(id, subtask);
            Epic saveEpic =  epics.get(subtask.idEpica);
            saveEpic.subtaskIds.add(id); //положили в эррейлист эпика подзадачу
        } else {
            return;
        }
    }
    //    public void updateTask(Task task). Если вы храните эпики в HashMap, где ключами являются идентификаторы,
//    то обновление — это запись нового эпика ).
    public void updateTask(Task task){  // обновление задачи
        tasks.put(task.getId(), task); //положили по старому айди, новую задачу не
        // статусы не нью
    }
    public void updateEpic (Epic epic) {  // обновление большой задачи
        epics.put(epic.getId(), epic);//положили по старому айди, новую задачу
        int statusNew = 0;
        int statusInProgress = 0;
        int statusDone = 0;
        ArrayList<Integer> listThis = epic.subtaskIds;
        for (int list : epic.subtaskIds) {
            Subtask subtask = subtasks.get(list);
            if (subtask.status == "NEW") {
                statusNew++;
            } else if (subtask.status == "IN_PROGRESS") {
                statusInProgress++;
            } else if (subtask.status == "DONE") {
                statusDone++;
            }
        }
        //если не все нью
        if (listThis.size() > 0) { //если есть подзадачи
            if (statusNew == listThis.size()) {
                epic.status = "NEW";
            } else if (statusDone == listThis.size()) {
                epic.status = "DONE";
            } else {
                epic.status = "IN_PROGRESS";
            }
        } else {
            epic.status = "NEW"; //если подзадач нет, то статус нью
        }
    }

    public void updateSubtask (Subtask subtask) { // обновление подзадачи
        subtasks.put(subtask.getId(), subtask);
        //положили по старому айди, новую подзадачу // в эррейлист не добавляем, т.к. не меняется айди
        Epic epic = epics.get(subtask.idEpica); //получили эпик по айди эпика привязанному
        updateEpic(epic);  //вызвали обновление эпика
    }
    public void deleteAllTask() { // удаление всех задач
        tasks.clear();
    }
    public void deleteAllEpic() {
        epics.clear();

        //по идее когда удаляешь все эпики, должны удальться и все сабтаски и чистить
        //эррей лист, я хочу вызвать метод удаления сабтасков в методе удаления эпиков, но ошибка сивол эпик не определен
        subtasks.clear();
    }
    public void deleteAllSubtask(Epic epic) { //
        epic.subtaskIds.clear();
        subtasks.clear();
    }
    public void getAllTask(){  //получение списка всех задач
        for(Task value : tasks.values()){
            System.out.println(value.title);
        }
    }
    public void getAllEpic(){ //получение списка всех больших задач
        for(Task value : epics.values()){
            System.out.println(value.title);
        }
    }
    public void getAllSubtask(){ //получение списка всех подзадач
        for(Task value : subtasks.values()){
            System.out.println(value.title);
        }
    }
    public void deleteForIdTask(Task task) { // удаление задачи по идентификатору
        int delId =  task.getId();
        tasks.remove(delId);
    }
    public void deleteForIdEpic(Epic epic) { // удаление  большой задачи по идентификатору
        int delId =  epic.getId();
        epics.remove(delId);
    }
    public void deleteForIdSubtask(Subtask subtask) { // удаление подзадачи по идентификатору
        int delId =  subtask.getId();
        subtasks.remove(delId);
        Epic epic = epics.get(subtask.idEpica);
        //ArrayList<Integer> subId = epic.subtaskIds;
        epic.subtaskIds.remove(subtask);


    }
    public void listSubtask (Epic epic) {  //Получение списка всех подзадач определённого эпика
        for(int list : epic.subtaskIds) {
            System.out.println(list);
        }

    }
}
