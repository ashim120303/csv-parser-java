package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private final ThreadLocal<WebPageScraper> scraperThreadLocal = ThreadLocal.withInitial(WebPageScraper::new);

    public void scrapeData(String inputCsv, String outputCsv) {
        FileHandler.createFileIfNotExists(outputCsv);

        try (BufferedReader reader = new BufferedReader(new FileReader(inputCsv))) {
            ExecutorService executor = Executors.newFixedThreadPool(10);

            String line;
            while ((line = reader.readLine()) != null) {
                final String query = line.trim();
                executor.submit(() -> processQuery(query, outputCsv));
            }

            executor.shutdown();
            while (!executor.isTerminated()) {
                // Подождите завершения всех потоков
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processQuery(String query, String outputCsv) {
        WebPageScraper webPageScraper = scraperThreadLocal.get();
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
