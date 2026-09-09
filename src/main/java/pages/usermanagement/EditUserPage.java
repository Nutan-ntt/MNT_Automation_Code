package pages.usermanagement;

import base.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class EditUserPage {

    private final WebDriverWait wait = new WebDriverWait(
            DriverFactory.getDriver(),
            Duration.ofSeconds(15)
    );

    // ---------------------------------------------------------
    // Edit User - Three Dots / More
    // ---------------------------------------------------------

    private final By moreButton = By.xpath(
            "//button[@type='button' and .//img[@alt='More']]"
    );

    // ---------------------------------------------------------
    // Edit option
    // ---------------------------------------------------------

    private final By editButton = By.xpath(
            "//button[@type='button' and normalize-space()='Edit']"
    );

    // ---------------------------------------------------------
    // Edit User fields
    // ---------------------------------------------------------

    private final By lastNameInput = By.id("lastName");

    // ---------------------------------------------------------
    // Save Changes
    // ---------------------------------------------------------

    private final By saveChangesButton = By.xpath(
            "//button[@type='submit' and normalize-space()='Save Changes']"
    );


    // ---------------------------------------------------------
    // Click three dots for the user
    // ---------------------------------------------------------

    // ---------------------------------------------------------
// Click three dots for the specified user
// ---------------------------------------------------------

    public void clickMoreForUser(String userName) {

        By userMoreButton = By.xpath(
                "//tr[.//td[contains(@class,'um_name')]"
                        + "//*[contains(normalize-space(),'"
                        + userName
                        + "')]]"
                        + "//td[contains(@class,'editClmn')]"
                        + "//button[.//img[@alt='More']]"
        );

        WebElement more = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        userMoreButton
                )
        );

        scrollIntoView(more);

        System.out.println(
                "More button found for user: " + userName
        );

        try {

            more.click();

        } catch (Exception e) {

            System.out.println(
                    "More button click intercepted. " +
                            "Using JavaScript click."
            );

            ((JavascriptExecutor) DriverFactory.getDriver())
                    .executeScript(
                            "arguments[0].click();",
                            more
                    );
        }

        System.out.println(
                "Three-dot menu clicked for user: " + userName
        );
    }
    // ---------------------------------------------------------
    // Click Edit
    // ---------------------------------------------------------

    public void clickEdit() {

        WebElement edit = wait.until(
                ExpectedConditions.elementToBeClickable(
                        editButton
                )
        );

        edit.click();

        System.out.println("Edit button clicked.");

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        lastNameInput
                )
        );

        System.out.println(
                "Edit User Account popup opened."
        );
    }


    // ---------------------------------------------------------
    // Change Last Name
    // ---------------------------------------------------------

    public void changeLastName(String lastName) {

        WebElement lastNameField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        lastNameInput
                )
        );

        lastNameField.clear();
        lastNameField.sendKeys(lastName);

        System.out.println(
                "Last Name changed to: " + lastName
        );
    }


    // ---------------------------------------------------------
    // Click Save Changes
    // ---------------------------------------------------------

    public void clickSaveChanges() {

        WebElement saveButton = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        saveChangesButton
                )
        );

        scrollIntoView(saveButton);

        System.out.println(
                "Save Changes button is visible."
        );

        try {

            saveButton.click();

        } catch (Exception e) {

            System.out.println(
                    "Save Changes click failed. Using JavaScript click."
            );

            ((JavascriptExecutor) DriverFactory.getDriver())
                    .executeScript(
                            "arguments[0].click();",
                            saveButton
                    );
        }

        System.out.println(
                "Save Changes button clicked."
        );
    }


    // ---------------------------------------------------------
    // Verify updated Last Name
    // ---------------------------------------------------------

    public boolean isUpdatedUserDisplayed(
            String firstName,
            String lastName
    ) {

        By updatedUser = By.xpath(
                "//*[normalize-space()='" + firstName + "']" +
                        "/ancestor::*[.//*[normalize-space()='" +
                        lastName +
                        "']][1]"
        );

        try {

            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            updatedUser
                    )
            );

            System.out.println(
                    "Updated user found: " +
                            firstName + " " + lastName
            );

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Updated user was not found: " +
                            firstName + " " + lastName
            );

            return false;
        }
    }


    // ---------------------------------------------------------
    // Scroll element into view
    // ---------------------------------------------------------

    private void scrollIntoView(WebElement element) {

        ((JavascriptExecutor) DriverFactory.getDriver())
                .executeScript(
                        "arguments[0].scrollIntoView({block:'center', inline:'center'});",
                        element
                );
    }
}