package ru.yandex.app.manager;

import ru.yandex.app.tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> historyTask = new ArrayList<>();
    private Map<Integer, Node> nodeMap = new HashMap<>();
    CustomLinkedList<Task> customLinkedList = new CustomLinkedList<>();
    //id, node
    //
    //
    // Напишите реализацию метода add(Task task). Теперь с помощью HashMap и метода удаления removeNode метод
    // add(Task task) будет быстро удалять задачу из списка, если она там есть,
    // а затем вставлять её в конец двусвязного списка. После добавления задачи не забудьте обновить значение узла в
    // HashMap.

    @Override
    public void addHistory(Task task) { // добавляет задачу в историю
        if (task != null) {
            Node newNode = customLinkedList.linkLast(task);
            if (nodeMap.containsKey(task.getId())) {
                Node thisDeleteNode = nodeMap.get(task.getId());
                customLinkedList.removeNode(thisDeleteNode);
                nodeMap.remove(task.getId(), thisDeleteNode);
            }
            nodeMap.put(task.getId(), newNode);
            historyTask.add(task);

            if (historyTask.size() > 10) {
                historyTask.remove(0);
            }
        }

    }

    @Override
    public void remove(int id) {
        for (Task task : historyTask) {
            if (task.getId() == id) {
                historyTask.remove(id);
                //customLinkedList.removeNode(nodeMap.get(id));
            }
        }
    }

    @Override
    public List<Task> getHistory() { //выдает эррей лист и проверяет что он не более 10позиций
        return historyTask;
    }


    public class CustomLinkedList<T extends Task> {

        private Node<T> head; //голова

        private Node<T> tail; //хвост


        public Node linkLast(T element) { //добавляет задачу  в конец списка
            final Node<T> oldLast = tail;
            final Node<T> newNode = new Node<>(null, element, oldLast);
            tail = newNode;
            if (oldLast == null)
                head = newNode;
            else
                oldLast.prev = newNode;
            return newNode;
        }

        public List<Node> getTasks() { //собирать все задачи из него в обычный ArrayList
            List<Node> nodeList = new ArrayList<>();
            Node<T> element = head;
            while (element.next != null) {
                nodeList.add(element);
                element = element.next;
            }
            return nodeList;
        }

        public void removeNode(Node node) {
            Node<T> next = node.next;
            Node<T> previous = node.prev;
            next.prev = previous;
            previous.next = next;
        }
    }

    ;
}


