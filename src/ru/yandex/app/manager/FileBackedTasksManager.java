package ru.yandex.app.manager;

import ru.yandex.app.tasks.Epic;
import ru.yandex.app.tasks.Status;
import ru.yandex.app.tasks.Subtask;
import ru.yandex.app.tasks.Task;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import static ru.yandex.app.tasks.Status.*;
import static ru.yandex.app.tasks.TypeTask.*;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    @Override
    public int createTask(Task task) {
        int id = super.createTask(task);
        save();
        return id;
    }

    @Override
    public int createEpic(Epic epic) {
        int id = super.createEpic(epic);
        save();
        return id;
    }

    @Override
    public int createSubtask(Subtask subtask) {
        int id = super.createSubtask(subtask);
        save();
        return id;
    }

    private int searchMaxId() {
        int maxId = 0;
        for (int id : tasks.keySet()) {
            if (id > maxId) {
                maxId = id;
            }
        }
        for (int id : subtasks.keySet()) {
            if (id > maxId) {
                maxId = id;
            }
        }
        for (int id : epics.keySet()) {
            if (id > maxId) {
                maxId = id;
            }
        }
        return ++maxId;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteAllTask() {
        super.deleteAllTask();
        save();
    }

    @Override
    public void deleteAllEpic() {
        super.deleteAllEpic();
        save();
    }

    @Override
    public void deleteAllSubtask() {
        super.deleteAllSubtask();
        save();
    }

    @Override
    public void deleteForIdTask(int id) {
        super.deleteForIdTask(id);
        save();
    }

    @Override
    public void deleteForIdEpic(int id) {
        super.deleteForIdEpic(id);
        save();
    }

    @Override
    public void deleteForIdSubtask(int id) {
        super.deleteForIdSubtask(id);
        save();
    }

    @Override
    public List<Subtask> getEpicSubtasks(int id) {
        List<Subtask> list = super.getEpicSubtasks(id);
        save();
        return list;
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = super.getSubtask(id);
        save();
        return subtask;
    }

    public void save() {
        try (Writer fileWriter = new FileWriter(file)) {
            fileWriter.write("id,type,name,status,description,epic,duration,startTime\n");
            for (Task task : tasks.values()) {
                fileWriter.write(toString(task));
            }
            for (Subtask subtask : subtasks.values()) {
                fileWriter.write(toString(subtask));
            }
            for (Epic epic : epics.values()) {
                fileWriter.write(toString(epic));
            }
            fileWriter.write(" \n"); //добавляет пустую строку
            if (!historyToString().isBlank()) {
                fileWriter.write(historyToString());
            } else {fileWriter.write(" ");}

            //вызвать метод записывающий историю
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи программы");
        }
    }

    private String toString(Task task) { //создает строку из задачи
        String line = String.format("%s,%s,%s,%s,%s,%s,%s\n", task.getId(), task.getType(), task.getTitle(),
                task.getStatus(), task.getContent(), task.getDuration(), task.getStartTime());

        return line;
    }

    private String toString(Subtask subtask) { //создает строку из подзадачи
        String line = String.format("%s,%s,%s,%s,%s,%s,%s,%s\n", subtask.getId(), subtask.getType(), subtask.getTitle(),
                subtask.getStatus(), subtask.getContent(), subtask.getIdEpic(), subtask.getDuration(), subtask.getStartTime());
        return line;
    }

    private String toString(Epic epic) { //создает строку из эпика
        String line = String.format("%s,%s,%s,%s,%s,%s,%s\n", epic.getId(), epic.getType(), epic.getTitle(),
                epic.getStatus(), epic.getContent(), epic.getDuration(), epic.getStartTime());
        return line;
    }


    private String historyToString() { //создает из истории просмотра строку
        List<Task> list = inMemoryHistoryManager.getHistory();
        StringBuilder sb = new StringBuilder();

        for (Task task : list) {
            if (!task.equals(" ")){
            sb.append(task.getId() + ",");
        } else {return sb.toString();}
        }
        return sb.toString();
    }

    private void historyFromString(String value) { //восстановление истории из строчки
        String[] strings = value.split(",");
        if (!value.isBlank()) {
            for (String s : strings) {
                int id = Integer.parseInt(s);
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

    public static FileBackedTasksManager loadFromFile(File file) { //загрузка из файла
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        List<String> lineList = new ArrayList<>();
        try (FileReader reader = new FileReader(file)) {
            BufferedReader br = new BufferedReader(reader);
            while (br.ready()) {
                String line = br.readLine();
                lineList.add(line);
            }
            //id,type,name,status,description,epic
            for (int i = 0; i < (lineList.size() - 2); i++) {

                String s = lineList.get(i);
                // for (String s : lineList) {
                String[] strings = s.split(",");
                if ((TASK.toString()).equals(strings[1])) {
                    Task task = new Task(strings[2], strings[4], Integer.parseInt(strings[0]),
                            Status.valueOf(strings[3]));
                    task.setDuration(Integer.parseInt(strings[5]));
                    if ((strings[6]).equals(null)){
                    task.setStartTime(LocalDateTime.of(Integer.parseInt(strings[6]), Integer.parseInt(strings[7]),
                            Integer.parseInt(strings[8]), Integer.parseInt(strings[9]),
                            Integer.parseInt(strings[10])));
                    } else {
                        task.setStartTime(null);
                    }
                    fileBackedTasksManager.taskPut(task);
                }
                if (strings[1].equals(SUB_TASK.toString())) {
                    Subtask subtask = new Subtask(strings[2], strings[4], Integer.parseInt(strings[0]),
                            Status.valueOf(strings[3]), Integer.parseInt(strings[5]));
                    subtask.setDuration(Integer.parseInt(strings[6]));
                    if ((strings[7]).equals(null)){
                        subtask.setStartTime(LocalDateTime.of(Integer.parseInt(strings[7]), Integer.parseInt(strings[8]),
                                Integer.parseInt(strings[9]), Integer.parseInt(strings[10]),
                                Integer.parseInt(strings[11])));
                    } else {
                        subtask.setStartTime(null);
                    }
                    fileBackedTasksManager.subtaskPut(subtask);
                }
                if (strings[1].equals(EPIC.toString())) {
                    Epic epic = new Epic(strings[2], strings[4], Integer.parseInt(strings[0]),
                            Status.valueOf(strings[3]));
                    epic.setDuration(Integer.parseInt(strings[5]));
                    if ((strings[6]).equals(null)){
                        epic.setStartTime(LocalDateTime.of(Integer.parseInt(strings[6]), Integer.parseInt(strings[7]),
                                Integer.parseInt(strings[8]), Integer.parseInt(strings[9]),
                                Integer.parseInt(strings[10])));
                    } else {
                        epic.setStartTime(null);
                    }


                    fileBackedTasksManager.epicPut(epic);
                }

            }
            List<Subtask> subtaskList = fileBackedTasksManager.getAllSubtask();
            if (subtaskList.size()>0){
            for (Subtask subtask : subtaskList) {
                Epic epic = fileBackedTasksManager.epics.get(subtask.getIdEpic());
                if (epic != null) {
                    epic.addSubtaskIds(subtask.getId());
                }
                }
            }
            int i = lineList.size() - 1; // если это последняя строка в файле делаем историю вызовов из строки
            fileBackedTasksManager.historyFromString(lineList.get(i));
            fileBackedTasksManager.generatedId = fileBackedTasksManager.searchMaxId();

        } catch (IOException e) {
            System.out.println("Error");

        }
        return fileBackedTasksManager;
    }

    private void epicPut(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    private void taskPut(Task task) {
        tasks.put(task.getId(), task);
    }

    private void subtaskPut(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
    }
    public List<Task> getPrioritizedTasks(){
        TreeSet<Task> set = new TreeSet<Task>();
        for (Task task: tasks.values()){
            set.add(task);
        }
        for (Epic epic : epics.values()){
            set.add(epic);
        }
        for (Subtask subtask : subtasks.values()){
            set.add(subtask);
        }
        ArrayList<Task> myList = new ArrayList<Task>(set);
        return  myList;

    }



    public static void main(String[] args) {
        File file = new File("./BestDatabase.csv");
        FileBackedTasksManager fileBackedTasksManager1 = new FileBackedTasksManager(file);
        Task task = new Task("Уборка", "а", 0, Status.NEW);

        int newId = fileBackedTasksManager1.createTask(task);
        System.out.println(task);
        fileBackedTasksManager1.getTask(newId);
        Epic epic = new Epic("Готовка", "а", 0, Status.NEW);
        fileBackedTasksManager1.createEpic(epic);
        System.out.println(epic);
        Subtask subtask = new Subtask("Покупка продуктов", "а", 0, Status.NEW,
                epic.getId());
        int idS = fileBackedTasksManager1.createSubtask(subtask);
        Subtask subtask1 = new Subtask("Выбор рецептов", "а", 0, Status.NEW,
                epic.getId());
        fileBackedTasksManager1.getSubtask(idS);
        int id1 = fileBackedTasksManager1.createSubtask(subtask1);
        fileBackedTasksManager1.getSubtask(id1);
        Task task666 = new Task("Уборка6", "а", 0, Status.NEW, 20,
                LocalDateTime.of(2023, 4, 15, 14, 40));
        int id666 = fileBackedTasksManager1.createTask(task666);
        fileBackedTasksManager1.getTask(id666);
        System.out.println("!!!!!!!" + fileBackedTasksManager1.getHistory());
        System.out.println("!!!!!!!" + fileBackedTasksManager1.getHistory());
        System.out.println("!!!!!!!" + fileBackedTasksManager1.getAllTask());
        System.out.println("!!!!!!!" + fileBackedTasksManager1.getAllSubtask());
        System.out.println("!!!!!!!" + fileBackedTasksManager1.getAllEpic());
        FileBackedTasksManager fileBackedTasksManager2 = FileBackedTasksManager.loadFromFile(file);
        System.out.println(task666.getStartTime());
        System.out.println("?!!!!!!" + fileBackedTasksManager2.getHistory());
        Task task2 = new Task("проверка", "а", 0, Status.NEW);
        int newId2 = fileBackedTasksManager2.createTask(task2);
        System.out.println(task2);
        System.out.println("?!!!!!!" + fileBackedTasksManager2.getAllTask());
        System.out.println("?!!!!!!" + fileBackedTasksManager2.getAllSubtask());
        System.out.println("?!!!!!!" + fileBackedTasksManager2.getAllEpic());
        System.out.println(fileBackedTasksManager2.getPrioritizedTasks());



    }

//    Comparator<Task> userComparator = new Comparator<>() {
//        @Override
//        public int compare(User user1, User user2) {
//            return  user1.name.compareTo(user2.name);
//        }
//    };
}

