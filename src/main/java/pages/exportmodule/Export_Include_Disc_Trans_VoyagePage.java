package pages.exportmodule;

import base.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Export_Include_Disc_Trans_VoyagePage {

    private final WebDriverWait wait = new WebDriverWait(
            DriverFactory.getDriver(),
            Duration.ofSeconds(20)
    );

    private final By covMainMenu = By.xpath(
            "//button[contains(@class,'bgnone') and contains(normalize-space(.),'COV Main')]"
    );
    private final By exportModuleLink = By.cssSelector(
            "a[data-testid='dExport'][href='/mass_notification_tool_export']"
    );
    private final By includeLoadingTranshipmentVoyages = By.xpath(
            "//tbody/tr/td[2]/div[1]/input[1]"
    );
    private final By voyageInput = By.xpath(
            "//input[@placeholder='Type to search...']"
    );
    private final By searchButton = By.xpath("//button[normalize-space()='Search']");

    public void enableLoadingAndTranshipmentVoyagesAndSearch() {
        if (DriverFactory.getDriver().findElements(includeLoadingTranshipmentVoyages).isEmpty()) {
            wait.until(ExpectedConditions.elementToBeClickable(covMainMenu)).click();
            WebElement exportLink = wait.until(
                    ExpectedConditions.presenceOfElementLocated(exportModuleLink)
            );
            ((JavascriptExecutor) DriverFactory.getDriver()).executeScript(
                    "arguments[0].click();",
                    exportLink
            );
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    includeLoadingTranshipmentVoyages
            ));
            pause("Opened Export");
        }

        WebElement includeVoyages = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        includeLoadingTranshipmentVoyages
                )
        );
        if (!includeVoyages.isSelected()) {
            ((JavascriptExecutor) DriverFactory.getDriver()).executeScript(
                    "arguments[0].click();",
                    includeVoyages
            );
            wait.withMessage(
                    "Include Loading and Transhipment Voyages checkbox was not selected."
            ).until(driver -> driver.findElement(
                    includeLoadingTranshipmentVoyages
            ).isSelected());
        }
        pause("Enabled Include Loading and Transhipment Voyages");

        WebElement voyage = wait.until(
                ExpectedConditions.visibilityOfElementLocated(voyageInput)
        );
        String currentVoyage = voyage.getDomProperty("value");
        if (currentVoyage == null || currentVoyage.isBlank()) {
            throw new IllegalStateException(
                    "No voyage is selected in Export; cannot search the existing voyage."
            );
        }

        WebElement search = wait.until(
                ExpectedConditions.elementToBeClickable(searchButton)
        );
        ((JavascriptExecutor) DriverFactory.getDriver()).executeScript(
                "arguments[0].scrollIntoView({block:'center', inline:'center'});",
                search
        );
        try {
            search.click();
        } catch (ElementClickInterceptedException intercepted) {
            ((JavascriptExecutor) DriverFactory.getDriver()).executeScript(
                    "arguments[0].click();",
                    search
            );
        }
        pause("Searched existing voyage " + currentVoyage);
        pause("Waiting to inspect Export search results",
                Integer.getInteger("export.voyage.resultsPauseSeconds", 15));
    }

    private void pause(String step) {
        pause(step, Integer.getInteger("export.voyage.pauseSeconds", 2));
    }

    private void pause(String step, int seconds) {
        if (seconds <= 0) {
            return;
        }
        System.out.println(step);
        try {
            Thread.sleep(Duration.ofSeconds(seconds).toMillis());
        } catch (InterruptedException interrupted) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(
                    "Interrupted while observing Export voyage search.",
                    interrupted
            );
        }
    }
}
