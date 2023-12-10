package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class WebPageScraper {

    private WebDriver driver;
    private WebDriverWait wait;

    public WebPageScraper() {
        // Установите путь к драйверу Chrome
        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");

        // Создайте объект ChromeOptions
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");  // Добавьте опцию для headless режима

        // Передайте объект ChromeOptions при создании ChromeDriver
        driver = new ChromeDriver(chromeOptions);

        wait = new WebDriverWait(driver, 10);
    }

    public int getSearchResultsCount(String query) {
        // Удаляем колличество запросов (цифры после последней запятой)
        query = query.replaceFirst(",\\d+$", "");

        // Сайт Wildberries меняет + на %2B в запросе
        if (query.contains("+")) {
            query = query.replace("+", "%2B");
        }

        String url = "https://www.wildberries.ru/catalog/0/search.aspx?search=" + query;

        driver.get(url);

        int resultCount = 0;


        boolean retry = true;

        while (retry) {
            try {
                // Ваш текущий код для проверки результатов поиска
                WebElement countElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("searching-results__count")));
                wait.until(ExpectedConditions.visibilityOf(countElement));

                String countText = countElement.getText();
                resultCount = Integer.parseInt(countText.replaceAll("\\D", ""));
                System.out.println("Результат поиска:  " + query + ": " + resultCount);

                // Если успешно завершили блок try, устанавливаем retry в false, чтобы выйти из цикла
                retry = false;

            } catch (TimeoutException e) {
                // Ваш текущий код для обработки исключений

                List<WebElement> notFoundElements = driver.findElements(By.className("not-found-search__title"));
                List<WebElement> brand = driver.findElements(By.className("brand-header"));
                List<WebElement> product = driver.findElements(By.className("product-page__header-wrap"));

                if (!notFoundElements.isEmpty()) {
                    System.out.println("Результат поиска для запроса: " + query + " ничего не найдено. URL запроса: " + url);
                    return 0;
                } else if (!brand.isEmpty() || !product.isEmpty()) {
                    System.out.println("Результат поиска для запроса: " + query + " бренд или id продукта. Пропуск запроса. URL запроса: " + url);
                    return -1;
                } else {
                    // Условие для продолжения цикла (повторного поиска)
                    System.out.println("Результат поиска:  " + query + ": " + resultCount + " Задержка сети. Повтор запроса.");
                }
            }
        }

        return resultCount;
    }


    public void closeDriver() {
        driver.quit();
    }
}
