package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebPageScraper {

    private WebDriver driver;
    private WebDriverWait wait;
    int counter = 0;

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
        // Удаляем цифры после последней запятой
        query = query.replaceFirst(",\\d+$", "");

        driver.get("https://www.wildberries.ru/catalog/0/search.aspx?search=" + query);

        int resultCount = 0;
        counter++;

        try {
            WebElement countElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("searching-results__count")));
            String countText = countElement.getText();
            resultCount = Integer.parseInt(countText.replaceAll("\\D", ""));
            System.out.println(counter + ". Результат поиска:  " + query + ": " + resultCount);
        } catch (TimeoutException e) {
            System.out.println(counter + ". Результат поиска для запроса: " + query + " не найден. Пропуск запроса.");
            return -1; // Возвращаем -1, чтобы показать, что результат не был найден
        }

        return resultCount;
    }


    public void closeDriver() {
        driver.quit();
    }
}
