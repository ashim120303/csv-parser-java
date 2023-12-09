package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public void scrapeData(String inputCsv, String outputCsv) {
        FileHandler.createFileIfNotExists(outputCsv);

        WebPageScraper webPageScraper = new WebPageScraper();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputCsv))) {
            ExecutorService executor = Executors.newFixedThreadPool(10);

            String line;
            while ((line = reader.readLine()) != null) {
                final String query = line.trim();
                executor.submit(() -> processQuery(query, webPageScraper, outputCsv));
            }

            executor.shutdown();
            while (!executor.isTerminated()) {
                // Подождите завершения всех потоков
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            webPageScraper.closeDriver();
        }
    }

    private void processQuery(String query, WebPageScraper webPageScraper, String outputCsv) {
        int resultCount = webPageScraper.getSearchResultsCount(query);

        if (resultCount != -1 && resultCount <= 150) {
            FileHandler.saveToFile(query, resultCount, outputCsv);
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.scrapeData("вб.csv", "output.csv");
    }
}
