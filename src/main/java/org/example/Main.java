package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private final ThreadLocal<WebPageScraper> scraperThreadLocal = ThreadLocal.withInitial(WebPageScraper::new);

    public void scrapeData(String inputCsv, String outputCsv, String errorsCsv) {
        FileHandler.createFileIfNotExists(outputCsv);
        FileHandler.createFileIfNotExists(errorsCsv);

        try (BufferedReader reader = new BufferedReader(new FileReader(inputCsv))) {
            ExecutorService executor = Executors.newFixedThreadPool(10);

            String line;
            while ((line = reader.readLine()) != null) {
                final String query = line.trim();
                executor.submit(() -> processQuery(query, outputCsv, errorsCsv));
            }

            executor.shutdown();
            while (!executor.isTerminated()) {
                // Подождите завершения всех потоков
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processQuery(String query, String outputCsv, String errorsCsv) {
        WebPageScraper webPageScraper = scraperThreadLocal.get();
        int resultCount = webPageScraper.getSearchResultsCount(query);

        if (resultCount != -2 && resultCount != -1 && resultCount <= 150) {
            FileHandler.saveToFile(query, resultCount, outputCsv);
        } else if (resultCount == -2) {
            FileHandler.saveToFile(query, resultCount, errorsCsv);
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.scrapeData("вб.csv", "output.csv", "errors.csv");
    }
}