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
    int counter = 0;

    public WebPageScraper() {
        // Установите путь к драйверу Chrome
        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");

        // Создайте объект ChromeOptions
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");  // Добавьте опцию для headless режима

        // Передайте объект ChromeOptions при создании ChromeDriver
        driver = new ChromeDriver(chromeOptions);

        wait = new WebDriverWait(driver, 5);
    }

    public int getSearchResultsCount(String query) {
        // Удаляем цифры после последней запятой
        query = query.replaceFirst(",\\d+$", "");

        driver.get("https://www.wildberries.ru/catalog/0/search.aspx?search=" + query);

        int resultCount = 0;
        counter++;

        try {
            // Проверка наличия элемента searching-results__count
            WebElement countElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("searching-results__count")));

            // Проверка видимости элемента searching-results__count
            wait.until(ExpectedConditions.visibilityOf(countElement));

            String countText = countElement.getText();
            resultCount = Integer.parseInt(countText.replaceAll("\\D", ""));
            System.out.println(counter + ". Результат поиска:  " + query + ": " + resultCount);
        } catch (TimeoutException e) {
            // Проверка наличия элемента с классом not-found-search__title
            List<WebElement> notFoundElements = driver.findElements(By.className("not-found-search__title"));
            if (!notFoundElements.isEmpty()) {
                System.out.println(counter + ". Результат поиска для запроса: " + query + " ничего не найдено.");
                return 0; // Возвращаем 0, когда на странице есть элемент not-found-search__title
            } else {
                System.out.println(counter + ". Результат поиска для запроса: " + query + " бренд или id продукта. Пропуск запроса.");
                return -1; // Возвращаем -1, на странице нет элемента not-found-search__title и searching-results__count
            }
        }
        return resultCount;
    }

    public void closeDriver() {
        driver.quit();
    }
}
