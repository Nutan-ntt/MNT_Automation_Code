package pages.usermanagement;

import base.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class UserManagementPage {

    private final WebDriverWait wait = new WebDriverWait(
            DriverFactory.getDriver(),
            Duration.ofSeconds(15)
    );

    // ---------------------------------------------------------
    // Page locators
    // ---------------------------------------------------------

    private final By covMainMenu = By.xpath(
            "//button[contains(@class,'bgnone') " +
                    "and contains(@class,'w100') " +
                    "and contains(normalize-space(.),'COV Main')]"
    );

    private final By userManagementLink = By.cssSelector(
            "a[data-testid='dExport'][href='/user_management']"
    );

    private final By userManagementHeader = By.xpath(
            "//h1[normalize-space()='User Management']"
    );

    // Search field
    private final By nameEmailInput = By.id("nameEmail");

    // Apply button
    private final By applyButton = By.xpath(
            "//button[normalize-space()='Apply']"
    );

    // User rows
    private final By userRows = By.cssSelector(
            "#grid_container > table#list2 > tbody.tblBodyBlk > tr:not(.nested_row)"
    );

    // More / three-dot button inside a row
    private final By moreButton = By.cssSelector(
            "td.editClmn button"
    );

    // Deactivate option in action menu
    private final By deactivateButton = By.xpath(
            "//div[contains(@class,'actionBlock')]//button[normalize-space()='Deactivate']"
    );

    // Confirmation Yes button
    private final By confirmYesButton = By.cssSelector(
            "button.btnOk"
    );

    // ---------------------------------------------------------
    // Open User Management
    // ---------------------------------------------------------

    public void openUserManagement() {

        wait.until(
                ExpectedConditions.elementToBeClickable(covMainMenu)
        ).click();

        System.out.println("COV Main dropdown clicked.");

        wait.until(
                ExpectedConditions.elementToBeClickable(userManagementLink)
        ).click();

        System.out.println("User Management option clicked.");

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        userManagementHeader
                )
        );

        System.out.println(
                "User Management page opened successfully."
        );
    }

    // ---------------------------------------------------------
    // Verify users displayed
    // ---------------------------------------------------------

    public boolean areUsersDisplayed() {

        return !wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(userRows)
        ).isEmpty();
    }

    // ---------------------------------------------------------
    // Get displayed user count
    // ---------------------------------------------------------

    public int getDisplayedUserCount() {

        int count = DriverFactory.getDriver()
                .findElements(userRows)
                .size();

        System.out.println(
                "Number of users displayed: " + count
        );

        return count;
    }

    // ---------------------------------------------------------
    // Verify first user data
    // ---------------------------------------------------------

    public boolean isFirstUserDataDisplayed() {

        try {

            WebElement firstRow = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(userRows)
            );

            String userName = firstRow.findElement(
                    By.cssSelector("td.um_name")
            ).getText().trim();

            System.out.println(
                    "First user displayed: " + userName
            );

            return !userName.isEmpty();

        } catch (Exception e) {

            return false;
        }
    }

    // ---------------------------------------------------------
    // Get first user name
    // ---------------------------------------------------------

    public String getFirstUserName() {

        WebElement firstRow = wait.until(
                ExpectedConditions.visibilityOfElementLocated(userRows)
        );

        return firstRow.findElement(
                By.cssSelector("td.um_name")
        ).getText().trim();
    }

    // ---------------------------------------------------------
    // Search user
    // ---------------------------------------------------------

    public void searchUser(String userName) {

        WebElement nameInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        nameEmailInput
                )
        );

        nameInput.clear();
        nameInput.sendKeys(userName);

        System.out.println(
                "User search entered: " + userName
        );

        // Find Apply button
        WebElement apply = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        applyButton
                )
        );

        // Scroll Apply button into center of viewport
        ((JavascriptExecutor) DriverFactory.getDriver())
                .executeScript(
                        "arguments[0].scrollIntoView({block:'center', inline:'center'});",
                        apply
                );

        // Allow UI/layout to settle
        try {

            Thread.sleep(500);

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
        }

        try {

            wait.until(
                    ExpectedConditions.elementToBeClickable(
                            apply
                    )
            );

            apply.click();

        } catch (ElementClickInterceptedException e) {

            System.out.println(
                    "Apply button click intercepted. Using JavaScript click."
            );

            ((JavascriptExecutor) DriverFactory.getDriver())
                    .executeScript(
                            "arguments[0].click();",
                            apply
                    );
        }

        System.out.println(
                "Apply button clicked."
        );
    }

    // ---------------------------------------------------------
    // Find specific user row
    // ---------------------------------------------------------

    private WebElement getUserRow(String userName) {

        /*
         * Find the TR which contains a td.um_name
         * whose text contains the supplied userName.
         *
         * Example:
         *
         * Search = rama1
         *
         * Actual displayed name =
         * rama1 rama2 rama2
         *
         * contains() will still find the correct row.
         */

        By userRow = By.xpath(
                "//table[@id='list2']" +
                        "//tbody[contains(@class,'tblBodyBlk')]" +
                        "/tr[not(contains(@class,'nested_row'))]" +
                        "[.//td[contains(@class,'um_name')" +
                        " and contains(normalize-space(.),'" +
                        userName +
                        "')]]"
        );

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        userRow
                )
        );
    }

    // ---------------------------------------------------------
    // Deactivate user
    // ---------------------------------------------------------

    public void deactivateUser(String userName) {

        WebElement userRow = getUserRow(userName);

        System.out.println(
                "User found: " + userRow.getText()
        );

        // Scroll the user row into the center of the screen
        ((JavascriptExecutor) DriverFactory.getDriver())
                .executeScript(
                        "arguments[0].scrollIntoView({block:'center', inline:'center'});",
                        userRow
                );

        // -----------------------------------------------------
        // Find three-dot button inside THIS user's row
        // -----------------------------------------------------

        WebElement moreButtonElement = wait.until(
                ExpectedConditions.visibilityOf(
                        userRow.findElement(moreButton)
                )
        );

        System.out.println(
                "Three-dot button located."
        );

        // Scroll the three-dot button into view
        ((JavascriptExecutor) DriverFactory.getDriver())
                .executeScript(
                        "arguments[0].scrollIntoView({block:'center', inline:'center'});",
                        moreButtonElement
                );

        try {

            // Wait until clickable
            wait.until(
                    ExpectedConditions.elementToBeClickable(
                            moreButtonElement
                    )
            );

            moreButtonElement.click();

        } catch (ElementClickInterceptedException e) {

            System.out.println(
                    "Three-dot click intercepted. Using JavaScript click."
            );

            ((JavascriptExecutor) DriverFactory.getDriver())
                    .executeScript(
                            "arguments[0].click();",
                            moreButtonElement
                    );
        }

        System.out.println(
                "More button clicked."
        );

        // -----------------------------------------------------
        // Click Deactivate
        // -----------------------------------------------------

        WebElement deactivate = wait.until(
                ExpectedConditions.elementToBeClickable(
                        deactivateButton
                )
        );

        deactivate.click();

        System.out.println(
                "Deactivate option clicked."
        );

        // -----------------------------------------------------
        // Confirm Yes
        // -----------------------------------------------------

        WebElement confirmYes = wait.until(
                ExpectedConditions.elementToBeClickable(
                        confirmYesButton
                )
        );

        confirmYes.click();

        System.out.println(
                "Deactivate confirmation accepted."
        );
    }

    // ---------------------------------------------------------
    // Get user status
    // ---------------------------------------------------------

    public String getUserStatus(String userName) {

        WebElement userRow = getUserRow(userName);

        String status = userRow.findElement(
                By.cssSelector("td.um_status")
        ).getText().trim();

        System.out.println(
                "User status for " + userName + ": " + status
        );

        return status;
    }
}