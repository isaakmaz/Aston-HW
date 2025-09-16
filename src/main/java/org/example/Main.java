package org.example;

    public class Main {
        public static void main(String[] args) {
            MyHashMap<String, Integer> map = new MyHashMap<>();
            map.put("Apple", 10);
            map.put("Lemon", 20);

            System.out.println("Значение для Lemon: " + map.get("Lemon")); // Ожидаем 20
            System.out.println("Значение для Apple: " + map.get("Apple")); // Ожидаем 10

            map.put("Apple", 99); // Обновляем значение
            System.out.println("Новое значение для Apple: " + map.get("Apple")); // Ожидаем 99

            Integer removedValue = map.remove("Lemon");
            System.out.println("Удалено значение: " + removedValue); // Ожидаем null
            System.out.println("Значение для Lemon после удаления: " + map.get("Lemon")); // Ожидаем null

            System.out.println("Содержит ключ 'Lemon' после удаления? " + map.containsKey("Lemon")); // Ожидаем false
            System.out.println("Содержит ключ 'Apple'? " + map.containsKey("Apple")); // Ожидаем true
        }
    }
