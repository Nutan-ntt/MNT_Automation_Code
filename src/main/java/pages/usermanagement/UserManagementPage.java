package pages.usermanagement;

import base.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
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

    private final By userRows = By.cssSelector(
            "#grid_container table#list2 tbody tr:has(td.um_name)"
    );

    public boolean areUsersDisplayed() {

        boolean usersDisplayed = !wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(userRows)
        ).isEmpty();

        System.out.println(
                "User Management grid displayed: " + usersDisplayed
        );

        return usersDisplayed;
    }

    public int getDisplayedUserCount() {

        return wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(userRows)
        ).size();
    }

    private By firstUserRow = By.cssSelector(
            "#grid_container table#list2 tbody > tr:not(.nested_row)"
    );

    private By userName = By.cssSelector("td.um_name");

    private By userStatus = By.cssSelector("td.um_status");

    private By userCountry = By.cssSelector("td:nth-child(3)");

    private By userEmail = By.cssSelector("td:nth-child(4)");

    public boolean isFirstUserDataDisplayed() {

        WebElement firstRow = wait.until(
                ExpectedConditions.visibilityOfElementLocated(userRows)
        );

        WebElement nameCell = firstRow.findElement(
                By.cssSelector("td.um_name")
        );

        String userName = nameCell.getText().trim();

        System.out.println("First user displayed: " + userName);

        return !userName.isEmpty();
    }
}