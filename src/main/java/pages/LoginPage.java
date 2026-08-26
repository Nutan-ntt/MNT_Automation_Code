package pages;

import base.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private By loginButton = By.id("submit");

    public void loginIfRequired() {

        WebDriverWait wait = new WebDriverWait(
                DriverFactory.getDriver(),
                Duration.ofSeconds(10)
        );

        try {
            // If Login button is available, authentication is required
            wait.until(
                    ExpectedConditions.elementToBeClickable(loginButton)
            ).click();

            System.out.println("Login required - Login button clicked.");

        } catch (TimeoutException e) {

            // Login button did not appear, so we assume
            // the existing Chrome session is already logged in.
            System.out.println("Already logged in - skipping login.");
        }
    }
}