package ru.yandex.app.manager;

import ru.yandex.app.tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node> nodeMap = new HashMap<>();
    private final CustomLinkedList<Task> customLinkedList = new CustomLinkedList<>();

    @Override
    public void addHistory(Task task) { // добавляет задачу в историю
        if (task != null) {
            if (nodeMap.containsKey(task.getId())) {
                Node thisDeleteNode = nodeMap.get(task.getId());
                customLinkedList.removeNode(thisDeleteNode);
                nodeMap.remove(task.getId(), thisDeleteNode);
            }
            Node newNode = customLinkedList.linkLast(task);
            nodeMap.put(task.getId(), newNode);

        }

    }

    @Override
    public void remove(int id) {
        if (nodeMap.containsKey(id)) {
            customLinkedList.removeNode(nodeMap.get(id));
            nodeMap.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() { //выдает эррей лист и проверяет что он не более 10позиций
       return customLinkedList.getTasks();
    }


    public class CustomLinkedList<T extends Task> {

        private Node<T> head; //голова

        private Node<T> tail; //хвост


        public Node linkLast(T element) { //добавляет задачу  в конец списка
            final Node<T> oldLast = tail;
            final Node<T> newNode = new Node<>(null, element, null);
            tail = newNode;
            if (oldLast == null)
                head = newNode;
            else
                oldLast.next = newNode;
            tail.prev = oldLast;
            return newNode;
        }

        public List<Task> getTasks() { //собирать все задачи из него в обычный ArrayList
            // Выводим значения из хранящиеся в связанном списке, начиная с head
            List<Task> nodeList = new ArrayList<>();
            Node<Task> element = customLinkedList.head;
            while (element != null) {
                nodeList.add(element.data);
                element = element.next;
            }
            return nodeList;
        }


        public void removeNode(Node node) {
            if ((node.next != null) && (node.prev != null)) {
                Node<T> next = node.next;
                Node<T> previous = node.prev;

                next.prev = previous;
                previous.next = next;
            }  else if ((node.next == null)) { //удаляем хвост
                Node<T> previous = node.prev;
                if (previous == null) {
                    head = null;
                    tail = null;
                } else {
                    previous.next = null;
                    tail = previous;
                }
            } else { //удаляем голову
                Node<T> next = node.next;
                next.prev = null;
                head = next;
            }
            }
        }

    ;
}


