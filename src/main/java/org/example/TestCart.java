package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class TestCart {

    ChromeDriver driver = null;
    WebDriverWait wait = null;
    String baseUrl = "https://www.dns-shop.ru/";

    @BeforeEach
    public void setUpDriver() {

        //Set up driver
        ChromeOptions options = new ChromeOptions();
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        //Set implicit wait:
        //wait for WebElement
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

        //wait for loading page
        driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);

        driver.manage().window().setSize(new Dimension(1280, 1280));
        driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
    }

    @Test
    public void addItemToCart() {
        driver.get(baseUrl);

        driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);

        //Search an element
        var downPricedItems = driver.findElement(By.xpath("//span[@class='catalog-menu__root-item-markdown']"));
        downPricedItems.click();

        var productNames =  driver.findElements(By.xpath("//a[@class='catalog-product__name ui-link ui-link_black']"));
        var firstHref = productNames.get(0);
        firstHref.click();

        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        var buyButton = driver.findElement(By.xpath("//button[@class='button-ui buy-btn button-ui_passive button-ui_brand']"));
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        buyButton.click();

        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        var cartButton = driver.findElement(By.xpath("//a[@class='buttons__link ui-link cart-link-counter']"));
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        cartButton.click();

        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.navigate().refresh();
        var itemsInCart = driver.findElements(By.xpath("//div[@class='cart-items__product cart-items__product_markdown']"));

        Assertions.assertEquals(1, itemsInCart.size());
    }

    @Test
    public void removeItemToCart() {
        driver.get(baseUrl);

        driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);

        //Search an element
        var downPricedItems = driver.findElement(By.xpath("//span[@class='catalog-menu__root-item-markdown']"));
        downPricedItems.click();

        var productNames =  driver.findElements(By.xpath("//a[@class='catalog-product__name ui-link ui-link_black']"));
        var firstHref = productNames.get(0);
        firstHref.click();

        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        var buyButton = driver.findElement(By.xpath("//button[@class='button-ui buy-btn button-ui_passive button-ui_brand']"));
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        buyButton.click();

        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        var cartButton = driver.findElement(By.xpath("//a[@class='buttons__link ui-link cart-link-counter']"));
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        cartButton.click();

        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.navigate().refresh();

        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        var delButtons = driver.findElements(By.xpath("//button[@class='menu-control-button remove-button']"));
        for (var button: delButtons) {
            button.click();
            driver.navigate().refresh();
        }

        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        driver.navigate().refresh();
        var itemsInCart = driver.findElements(By.xpath("//div[@class='cart-items__product cart-items__product_markdown']"));

        Assertions.assertEquals(0, itemsInCart.size());
    }

    @AfterEach
    public void closeBrowser() {
        driver.quit();
    }
}