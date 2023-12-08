package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    public void scrapeData(String inputCsv, String outputCsv) {
        FileHandler.createFileIfNotExists(outputCsv);

        WebPageScraper webPageScraper = new WebPageScraper();  // Создаем один экземпляр

        try (BufferedReader reader = new BufferedReader(new FileReader(inputCsv))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String query = line.trim();
                int resultCount = webPageScraper.getSearchResultsCount(query);

                if (resultCount != -1 && resultCount <= 150) {
                    FileHandler.saveToFile(line, outputCsv);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            webPageScraper.closeDriver();  // Закрываем драйвер после выполнения операций
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.scrapeData("вб.csv", "output.csv");
    }
}
