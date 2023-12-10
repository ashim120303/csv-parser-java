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

        try {
            // Проверка наличия элемента searching-results__count
            WebElement countElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("searching-results__count")));

            // Проверка видимости элемента searching-results__count
            wait.until(ExpectedConditions.visibilityOf(countElement));

            String countText = countElement.getText();
            resultCount = Integer.parseInt(countText.replaceAll("\\D", ""));
            System.out.println("Результат поиска:  " + query + ": " + resultCount);
        } catch (TimeoutException e) {
            // Проверка наличия элемента с классом not-found-search__title (если результатов поиска нет)
            List<WebElement> notFoundElements = driver.findElements(By.className("not-found-search__title"));
            if (!notFoundElements.isEmpty()) {
                System.out.println("Результат поиска для запроса: " + query + " ничего не найдено. URL запроса: " + url);
                return 0; // Возвращаем 0, когда на странице есть элемент not-found-search__title
            } else {
                System.out.println("Результат поиска для запроса: " + query + " бренд или id продукта. Пропуск запроса. URL запроса: " + url);
                return -1; // Возвращаем -1, на странице нет элемента not-found-search__title и searching-results__count (Бренд или id продукта)
            }
        }
        return resultCount;
    }


    public void closeDriver() {
        driver.quit();
    }
}
