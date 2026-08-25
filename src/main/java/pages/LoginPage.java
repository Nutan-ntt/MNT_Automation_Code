package pages;

import base.DriverFactory;
import org.openqa.selenium.By;

public class LoginPage {

    private By loginButton = By.id("submit");

    public void clickLogin() {
        DriverFactory.getDriver().findElement(loginButton).click();
    }
}