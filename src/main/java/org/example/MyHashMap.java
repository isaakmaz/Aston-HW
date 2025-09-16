package org.example;

@SuppressWarnings("unchecked")
public class MyHashMap<K, V> {

    // Это "чертеж" для нашей "коробочки", в которой лежат данные.
    static class Node<K, V> {
        final K key; // Ключ. "final", потому что ключ не должен меняться после создания.
        V value; // Значение. Оно может меняться.
        Node<K, V> next; // Ссылка на следующую (Node) в этом же ящике.

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private final Node<K, V>[] table; // Наш массив-хранилище ("шкаф")

    private static final int DEFAULT_CAPACITY = 16; // наша вместимость

    public MyHashMap() {

        this.table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY]; // Создаем массив ящиков (Node) размером 16

    }

    public void put(K key, V value) {
        int index = key.hashCode() & (table.length - 1); // вычисляем, в какой ящик класть.

        Node<K, V> nodeInBucket = table[index]; // Достаем из ящика то, что там лежит.

        if (nodeInBucket == null) {
            table[index] = new Node<>(key, value, null);

        } else {  // тогда вариант 2

            Node<K, V> currentNode = nodeInBucket; // Начинаем с первого узла, который уже был в ящике.

            while (true) {
                // Ключ текущего узла совпадает с тем, что мы кладем?
                if (currentNode.key.equals(key)) {
                    currentNode.value = value; // Значит, просто обновляем значение в найденной "коробочке".
                    return;  // После обновления работа метода put закончена, выходим.
                }

                if (currentNode.next == null) {

                    currentNode.next = new Node<>(key, value, null);

                    return;
                }

                currentNode = currentNode.next;
            }

        }

    }

    public V get(K key) {

        int index = key.hashCode() & (table.length - 1); // Вычисляем индекс

        Node<K, V> currentNode = table[index]; // Достаем первый узел из ящика

        while (currentNode != null) {
            // Если нашли нужный ключ...
            if (currentNode.key.equals(key)) {
                // ... то возвращаем его значение и выходим из метода!
                return currentNode.value;
            }

            currentNode = currentNode.next; // если ключ не тот, идём дальше
        }

        return null;

    }

    public V remove(K key) {
        int index = key.hashCode() & (table.length - 1);
        Node<K, V> currentNode = table[index]; // ползунки
        Node<K, V> previousNode = null;

        while (currentNode != null) {
            if (currentNode.key.equals(key)) {
                if (previousNode == null) {
                    // Удаляем первый элемент в цепочке
                    table[index] = currentNode.next;
                } else {
                    // Удаляем из середины или конца
                    previousNode.next = currentNode.next;
                }
                return currentNode.value;
            }
            previousNode = currentNode;
            currentNode = currentNode.next;
        }
        return null;
    }

    public boolean containsKey(K key) {
        int index = key.hashCode() & (table.length - 1); // Вычисляем индекс

        Node<K, V> currentNode = table[index]; // Достаем первый узел из ящика

        while (currentNode != null) {
            // Если нашли нужный ключ...
            if (currentNode.key.equals(key)) {
                // Ключ найден! Просто возвращаем true и выходим.
                return true;
            }

            currentNode = currentNode.next; // если ключ не тот, идём дальше
        }

        return false;
    }


}