package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;


public class TestCart {
    private static final Duration implicitWait = Duration.ofSeconds(3);
    private static final Duration pageTimeout = Duration.ofSeconds(20);
    private static final Dimension windowSize = new Dimension(1280, 1280);
    private static final String  baseUrl = "https://www.dns-shop.ru/";

    ChromeDriver driver;

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(implicitWait);
        driver.manage().timeouts().pageLoadTimeout(pageTimeout);
        driver.manage().window().setSize(windowSize);
    }

    private void addItem() {
        driver.get(baseUrl);
        driver.manage().timeouts().pageLoadTimeout(pageTimeout);
        var downPricedItems = driver.findElement(By.xpath("//span[@class='catalog-menu__root-item-markdown']"));
        downPricedItems.click();

        var productNames =  driver.findElements(By.xpath("//a[@class='catalog-product__name ui-link ui-link_black']"));
        var firstHref = productNames.get(0);
        firstHref.click();

        var buyButton = driver.findElement(By.xpath("//button[@class='button-ui buy-btn button-ui_passive button-ui_brand']"));
        buyButton.click();
        driver.manage().timeouts().implicitlyWait(implicitWait);
    }

    private void openCart() {
        var cartButton = driver.findElement(By.xpath("//a[@class='buttons__link ui-link cart-link-counter']"));
        driver.manage().timeouts().implicitlyWait(implicitWait);
        cartButton.click();

        // Refresh the page to get actual items
        driver.manage().timeouts().implicitlyWait(implicitWait);
        driver.navigate().refresh();
    }

    @Test
    public void testAddItemToCart() {
        addItem();
        openCart();

        Assertions.assertEquals(
                1,
                driver.findElements(By.xpath(
                        "//div[@class='cart-items__product cart-items__product_markdown']")
                ).size());
    }

    @Test
    public void testRemoveItemFromCart() {
        addItem();
        openCart();

        var delButtons = driver.findElements(By.xpath("//button[@class='menu-control-button remove-button']"));
        for (var button: delButtons) {
            button.click();
            driver.navigate().refresh();
        }

        driver.manage().timeouts().implicitlyWait(implicitWait);
        driver.navigate().refresh();

        Assertions.assertEquals(
                0,
                driver.findElements(By.xpath(
                        "//div[@class='cart-items__product cart-items__product_markdown']"
                )).size());
    }

    @AfterEach
    public void closeBrowser() {
        driver.quit();
    }
}