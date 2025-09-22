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

    private Node<K, V>[] table; // Наш массив-хранилище ("шкаф")

    private static final int DEFAULT_CAPACITY = 16; // наша вместимость

    private int size = 0; // Счетчик количества элементов
    private static final float LOAD_FACTOR = 0.75f; // Коэффициент загрузки

    public MyHashMap() {

        this.table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY]; // Создаем массив ящиков (Node) размером 16

    }

    public void put(K key, V value) {
        if (key == null) {
            putForNullKey(value);
            // Не забываем проверить resize и здесь!
            if ((float)size / table.length > LOAD_FACTOR) {
                resize();
            }
            return;
        }
        int index = key.hashCode() & (table.length - 1); // вычисляем, в какой ящик класть.

        Node<K, V> nodeInBucket = table[index]; // Достаем из ящика то, что там лежит.

        if (nodeInBucket == null) {
            table[index] = new Node<>(key, value, null);
            size++; // Увеличиваем размер
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
                    size++;
                    return;
                }

                if ((float)size / table.length > LOAD_FACTOR) {
                    resize();
                }

                currentNode = currentNode.next;
            }
        }

    }

    private void putForNullKey(V value) {
        Node<K, V> currentNode = table[0]; // Всегда смотрим в 0-й ящик

        // Идем по цепочке и ищем узел, где key == null
        while (currentNode != null) {
            if (currentNode.key == null) {
                currentNode.value = value; // Нашли -> обновили -> вышли
                return;
            }
            currentNode = currentNode.next;
        }

        // Создаем новый и добавляем его в начало цепочки в 0-м ящике.
        Node<K, V> newNode = new Node<>(null, value, table[0]);
        table[0] = newNode;
        size++; // Не забываем увеличить размер
    }

    public V get(K key) {

        if (key == null) {
            // Логика поиска для null-ключа
            Node<K, V> currentNode = table[0];
            while (currentNode != null) {
                if (currentNode.key == null) {
                    return currentNode.value; // Нашли и вернули значение
                }
                currentNode = currentNode.next;
            }
            return null; // Не нашли
        }

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
        if (key == null) {
            Node<K, V> currentNode = table[0];
            Node<K, V> previousNode = null;
            while (currentNode != null) {
                if (currentNode.key == null) {
                    if (previousNode == null) {
                        table[0] = currentNode.next;
                    } else {
                        previousNode.next = currentNode.next;
                    }
                    size--; // УМЕНЬШАЕМ, А НЕ УВЕЛИЧИВАЕМ
                    return currentNode.value;
                }
                previousNode = currentNode;
                currentNode = currentNode.next;
            }
            return null;
        }
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
                size--;
                return currentNode.value;
            }
            previousNode = currentNode;
            currentNode = currentNode.next;
        }
        return null;
    }

    public boolean containsKey(K key) {
        if (key == null) {
            Node<K, V> currentNode = table[0];
            while (currentNode != null) {
                if (currentNode.key == null) {
                    return true; // ВОЗВРАЩАЕМ TRUE
                }
                currentNode = currentNode.next;
            }
            return false; // ВОЗВРАЩАЕМ FALSE
        }
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

    private void resize() {
        // Сохраняем старый массив и создаем новый, вдвое больше
        Node<K, V>[] oldTable = table;
        int newCapacity = oldTable.length * 2;
        table = (Node<K, V>[]) new Node[newCapacity];

        // Проходим по каждому ящику старого массива
        for (Node<K, V> nodeInOldBucket : oldTable) {
            // Берем первый узел из цепочки в старом ящике
            Node<K, V> currentNode = nodeInOldBucket;

            // Если в ящике есть цепочка, проходим по ней
            while (currentNode != null) {
                // Сохраняем ссылку на следующий узел, т.к. мы сейчас "оборвем" связь
                Node<K, V> nextNode = currentNode.next;

                // Вычисляем новый индекс для текущего узла в новом массиве
                int newIndex;
                if (currentNode.key == null) {
                    newIndex = 0;
                } else {
                    newIndex = currentNode.key.hashCode() & (newCapacity - 1);
                }

                // Ставим `next` нашего узла на то, что уже было в новом ящике.
                currentNode.next = table[newIndex];
                // А "головой" цепочки в новом ящике делаем наш узел.
                table[newIndex] = currentNode;

                // Переходим к следующему узлу из старой цепочки
                currentNode = nextNode;
            }
        }
    }
}