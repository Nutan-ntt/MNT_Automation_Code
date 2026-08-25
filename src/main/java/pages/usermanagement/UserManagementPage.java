package pages.usermanagement;

import base.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class UserManagementPage {

    private By moduleDropdown =
            By.xpath("//button[contains(normalize-space(), 'COV Main')]");

    private By userManagementMenu =
            By.xpath("//a[@data-testid='dExport' and normalize-space()='User Management']");

    private By userManagementHeading =
            By.xpath("//h1[normalize-space()='User Management']");

    private WebDriverWait wait =
            new WebDriverWait(
                    DriverFactory.getDriver(),
                    Duration.ofSeconds(20)
            );

    public void openUserManagement() {

        // Click COV Main dropdown
        wait.until(
                ExpectedConditions.elementToBeClickable(moduleDropdown)
        ).click();

        // Click User Management
        wait.until(
                ExpectedConditions.elementToBeClickable(userManagementMenu)
        ).click();
    }

    public boolean isUserManagementPageDisplayed() {

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        userManagementHeading
                )
        ).isDisplayed();
    }
}