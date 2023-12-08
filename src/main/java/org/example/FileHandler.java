package org.example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileHandler {

    public static void saveToFile(String data, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            // Разбиваем строку по запятой
            String[] parts = data.split(",");

            // Проверяем, что есть хотя бы две части
            if (parts.length >= 2) {
                // Записываем первую часть в первую колонку
                writer.write(parts[0].trim());
                writer.write(",");

                // Записываем остальные части во вторую колонку
                for (int i = 1; i < parts.length; i++) {
                    writer.write(parts[i].trim());
                    if (i < parts.length - 1) {
                        writer.write(",");
                    }
                }

                writer.newLine();
                System.out.println("Сохранено: " + data);
            } else {
                System.out.println("Неверный формат данных: " + data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void createFileIfNotExists(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
                System.out.println("Создан файл : " + filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
