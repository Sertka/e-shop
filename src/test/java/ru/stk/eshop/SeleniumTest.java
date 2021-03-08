package ru.stk.eshop;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.List;

public class SeleniumTest {

    private WebDriver driver;

    @BeforeSuite
    public void init() {
        this.driver = new ChromeDriver();
        this.driver.manage().window().maximize();
    }

    @Test
    public void test1() {
        driver.get("http://localhost:8555/shop/");

        WebElement formLabel = driver
                .findElement(By.cssSelector(".form-label"));

        String txt = formLabel.getText();
        System.out.println(txt);
        Assert.assertTrue(txt.contains("Записей на странице:"));
    }

    @AfterSuite
    public void shutdown() {
       this.driver.quit();
    }
}
