package org.example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileHandler {

    public static void saveToFile(String data, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(data);
            writer.newLine();
            System.out.println("Сохранено: " + data);
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
