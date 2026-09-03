package pages.usermanagement;

import base.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CreateUserPage {

    private final WebDriverWait wait = new WebDriverWait(
            DriverFactory.getDriver(),
            Duration.ofSeconds(15)
    );

    // ---------------------------------------------------------
    // Add New User
    // ---------------------------------------------------------

    private final By addNewUserButton = By.xpath(
            "//button[normalize-space()='Add New User']"
    );

    // ---------------------------------------------------------
    // Create User Account fields
    // ---------------------------------------------------------

    private final By firstNameInput = By.id("firstName");

    private final By lastNameInput = By.id("lastName");

    private final By emailInput = By.id("email");

    private final By defaultCountryDropdown = By.id("country-1");

    private final By modifyModuleInput =
            By.id("react-select-2-input");

    private final By viewModuleInput =
            By.id("react-select-3-input");
    // ---------------------------------------------------------
    // Save / Cancel
    // ---------------------------------------------------------

    private final By saveChangesButton = By.xpath(
            "//button[@type='submit' and normalize-space()='Save Changes']"
    );

    private final By cancelButton = By.xpath(
            "//button[@type='button' " +
                    "and normalize-space()='Cancel']"
    );


    // =========================================================
    // Actions
    // =========================================================

    public void clickAddNewUser() {

        WebElement addUser = wait.until(
                ExpectedConditions.elementToBeClickable(
                        addNewUserButton
                )
        );

        addUser.click();

        System.out.println(
                "Add New User button clicked."
        );

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        firstNameInput
                )
        );

        System.out.println(
                "Create User Account popup opened."
        );
    }


    public void enterFirstName(String firstName) {

        WebElement firstNameField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        firstNameInput
                )
        );

        firstNameField.clear();
        firstNameField.sendKeys(firstName);

        System.out.println(
                "First Name entered: " + firstName
        );
    }


    public void enterLastName(String lastName) {

        WebElement lastNameField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        lastNameInput
                )
        );

        lastNameField.clear();
        lastNameField.sendKeys(lastName);

        System.out.println(
                "Last Name entered: " + lastName
        );
    }


    public void enterEmail(String email) {

        WebElement emailField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        emailInput
                )
        );

        emailField.clear();
        emailField.sendKeys(email);

        System.out.println(
                "Email entered: " + email
        );
    }


    public void selectDefaultCountry(String countryName) {

        WebElement countryDropdown = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        defaultCountryDropdown
                )
        );

        Select country = new Select(countryDropdown);

        country.selectByVisibleText(countryName);

        System.out.println(
                "Default Country selected: " + countryName
        );
    }


    // =========================================================
    // Module Selection
    // =========================================================

    public void selectModifyModule(String moduleName) {

        WebElement moduleInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        modifyModuleInput
                )
        );

        scrollIntoView(moduleInput);

        JavascriptExecutor js =
                (JavascriptExecutor) DriverFactory.getDriver();

        // Focus the React Select input
        js.executeScript(
                "arguments[0].focus();",
                moduleInput
        );

        // Type module name
        moduleInput.sendKeys(moduleName);

        System.out.println(
                "Typed Modify module: " + moduleName
        );

        // Press ENTER to select the displayed option
        moduleInput.sendKeys(Keys.ENTER);

        System.out.println(
                "Modify module selected: " + moduleName
        );
    }

    private void scrollIntoView(WebElement moduleInput) {
    }

    public void selectViewModule(String moduleName) {

        WebElement moduleInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        viewModuleInput
                )
        );

        scrollIntoView(moduleInput);

        JavascriptExecutor js =
                (JavascriptExecutor) DriverFactory.getDriver();

        // Focus the React Select input
        js.executeScript(
                "arguments[0].focus();",
                moduleInput
        );

        // Type module name
        moduleInput.sendKeys(moduleName);

        System.out.println(
                "Typed View module: " + moduleName
        );

        // Press ENTER to select the displayed option
        moduleInput.sendKeys(Keys.ENTER);

        System.out.println(
                "View module selected: " + moduleName
        );
    }

    // =========================================================
    // Save
    // =========================================================

    public boolean isSaveButtonEnabled() {

        WebElement saveButton = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        saveChangesButton
                )
        );

        return saveButton.isEnabled();
    }


    public void clickSaveChanges() {

        WebElement saveButton = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        saveChangesButton
                )
        );

        // Scroll the Save Changes button into view
        ((JavascriptExecutor) DriverFactory.getDriver())
                .executeScript(
                        "arguments[0].scrollIntoView({block:'center'});",
                        saveButton
                );

        System.out.println(
                "Save Changes button is visible."
        );

        // Click using JavaScript to avoid popup/overlay interception
        ((JavascriptExecutor) DriverFactory.getDriver())
                .executeScript(
                        "arguments[0].click();",
                        saveButton
                );

        System.out.println(
                "Save Changes button clicked."
        );
    }


    // =========================================================
    // Cancel
    // =========================================================

    public void clickCancel() {

        WebElement cancel = wait.until(
                ExpectedConditions.elementToBeClickable(
                        cancelButton
                )
        );

        cancel.click();

        System.out.println(
                "Cancel button clicked."
        );
    }
}