package pages.exportmodule;

import base.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class TemplatePage {

    WebDriverWait wait = new WebDriverWait(
            DriverFactory.getDriver(),
            Duration.ofSeconds(15)
    );

    private final By templateDropdown = By.id("template");

    private final By loadButton = By.id("load");

    private final By sendButton = By.id("save");


    public void selectTemplate(String templateName) {

        WebElement template = wait.until(
                ExpectedConditions.visibilityOfElementLocated(templateDropdown)
        );

        Select select = new Select(template);

        select.selectByVisibleText(templateName);

        System.out.println("Template selected: " + templateName);
    }


    public void clickLoad() {

        WebElement load = wait.until(
                ExpectedConditions.elementToBeClickable(loadButton)
        );

        load.click();

        System.out.println("Load button clicked.");

        // Wait for template content to load
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Template load wait completed.");
    }


    public void clickSend() {

        WebElement send = wait.until(
                ExpectedConditions.elementToBeClickable(sendButton)
        );

        send.click();

        System.out.println("Send button clicked.");
    }
}