package pages.exportmodule;

import base.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class voyagecheck {

    WebDriverWait wait = new WebDriverWait(
            DriverFactory.getDriver(),
            Duration.ofSeconds(15)
    );

    // COV Main dropdown
    private final By covMainMenu = By.xpath(
            "//button[contains(@class,'bgnone') and contains(normalize-space(.),'COV Main')]"
    );

    // Export option
    private final By exportModule = By.cssSelector(
            "a[data-testid='dExport'][href='/mass_notification_tool_export']"
    );

    // Country dropdown
    private final By countryDropdown = By.name("mainCountry");

    public void openExportModule() {

        wait.until(
                ExpectedConditions.elementToBeClickable(covMainMenu)
        ).click();

        System.out.println("COV Main clicked.");

        WebElement export = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        exportModule
                )
        );

        System.out.println(
                "Export element found: " +
                        export.getAttribute("outerHTML")
        );

        // Click using JavaScript because the menu item
        // exists in the DOM but is reported as hidden.
        ((org.openqa.selenium.JavascriptExecutor)
                DriverFactory.getDriver())
                .executeScript(
                        "arguments[0].click();",
                        export
                );

        System.out.println("Export option clicked.");

        // Wait until Export page URL is loaded
        wait.until(
                ExpectedConditions.urlContains(
                        "/mass_notification_tool_export"
                )
        );

        System.out.println(
                "Export module opened successfully."
        );
    }

    public void changeCountryToVN() {

        WebElement country = wait.until(
                ExpectedConditions.visibilityOfElementLocated(countryDropdown)
        );

        Select select = new Select(country);
        select.selectByVisibleText("VN");

        System.out.println("Country changed to VN.");
    }

    private final By voyageSearchBox = By.xpath(
            "//input[@placeholder='Type to search...']"
    );

    private final By searchButton = By.xpath(
            "//button[@type='button' and normalize-space()='Search']"
    );
    private final By parametersButton = By.xpath(
            "//button[normalize-space()='Parameters']"
    );

    public void searchVoyage(String voyageNumber) {

        WebElement searchBox = wait.until(
                ExpectedConditions.visibilityOfElementLocated(voyageSearchBox)
        );

        searchBox.clear();
        searchBox.sendKeys(voyageNumber);

        System.out.println("Voyage number entered: " + voyageNumber);

        WebElement search = wait.until(
                ExpectedConditions.elementToBeClickable(searchButton)
        );

        search.click();

        System.out.println("Search button clicked.");

        // Wait 10 seconds for results to load
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("10 seconds wait completed. Results should be loaded.");

        wait.until(ExpectedConditions.elementToBeClickable(parametersButton)).click();
        System.out.println("Parameters opened.");
    }

    private final By resultCheckbox = By.xpath(
            "//input[@type='checkbox' and contains(@class,'form-check-input') and not(ancestor::*[contains(@style,'display: none')])]"
    );

    private final By sendRemindersButton = By.xpath(
            "//button[@type='button' and contains(@class,'btn-primary') and normalize-space()='Send Reminders']"
    );

    public void selectResultAndSendReminder() {

        java.util.List<WebElement> checkboxes =
                DriverFactory.getDriver().findElements(
                        resultCheckbox
                );

        System.out.println("Total checkboxes found: " + checkboxes.size());

        // Click the first visible result checkbox
        for (WebElement checkbox : checkboxes) {

            if (checkbox.isDisplayed() && checkbox.isEnabled()) {

                checkbox.click();

                System.out.println("First visible result checkbox selected.");
                break;
            }
        }

        // Wait 10 seconds before clicking Send Reminders
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("10 seconds wait completed.");

        // Click Send Reminders
        WebElement sendReminders = wait.until(
                ExpectedConditions.elementToBeClickable(sendRemindersButton)
        );

        sendReminders.click();

        System.out.println("Send Reminders button clicked.");
    }


}