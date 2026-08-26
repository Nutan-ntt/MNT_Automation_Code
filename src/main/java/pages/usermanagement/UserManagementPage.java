package pages.usermanagement;

import base.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class UserManagementPage {

    private WebDriverWait wait = new WebDriverWait(
            DriverFactory.getDriver(),
            Duration.ofSeconds(15)
    );

    private By covMainMenu = By.xpath(
            "//button[contains(@class,'bgnone') and contains(@class,'w100') and contains(normalize-space(.),'COV Main')]"
    );

    private By userManagementLink = By.cssSelector(
            "a[data-testid='dExport'][href='/user_management']"
    );

    private By userManagementHeader = By.xpath(
            "//h1[normalize-space()='User Management']"
    );

    public void openUserManagement() {

        // Click COV Main dropdown
        wait.until(
                ExpectedConditions.elementToBeClickable(covMainMenu)
        ).click();

        System.out.println("COV Main dropdown clicked.");

        // Click User Management
        wait.until(
                ExpectedConditions.elementToBeClickable(userManagementLink)
        ).click();

        System.out.println("User Management option clicked.");

        // Verify page opened
        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        userManagementHeader
                )
        );

        System.out.println("User Management page opened successfully.");
    }
}