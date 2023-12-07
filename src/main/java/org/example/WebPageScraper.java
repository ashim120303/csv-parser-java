package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebPageScraper {

    private WebDriver driver;

    public WebPageScraper() {
        // Установите путь к драйверу Chrome
        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
        driver = new ChromeDriver();
    }

    public int getSearchResultsCount(String query) {
        // Удаляем цифры после последней запятой
        query = query.replaceFirst(",\\d+$", "");

        driver.get("https://www.wildberries.ru/catalog/0/search.aspx?search=" + query);

        int resultCount = 0;

        try {
            WebDriverWait wait = new WebDriverWait(driver, 4);
            WebElement countElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("searching-results__count")));
            String countText = countElement.getText();
            resultCount = Integer.parseInt(countText.replaceAll("\\D", ""));
            System.out.println("Результат поиска:  " + query + ": " + resultCount);
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("Element with class 'searching-results__count' not found. Setting resultCount to 0.");
        } catch (NumberFormatException e) {
            System.err.println("Unable to parse search results count. Setting resultCount to 0.");
        }

        return resultCount;
    }

    public void closeDriver() {
        driver.quit();
    }
}
