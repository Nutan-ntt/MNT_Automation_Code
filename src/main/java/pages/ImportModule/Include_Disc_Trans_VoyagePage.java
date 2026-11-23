package pages.ImportModule;

import base.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Include_Disc_Trans_VoyagePage {

    private final WebDriverWait wait = new WebDriverWait(
            DriverFactory.getDriver(),
            Duration.ofSeconds(20)
    );

    private final By covMainMenu = By.xpath(
            "//button[contains(@class,'bgnone') and contains(normalize-space(.),'COV Main')]"
    );
    private final By importLink = By.xpath(
            "//a[@data-testid='dExport'][normalize-space()='Import']"
    );
    private final By importHeader = By.xpath("//h1[normalize-space()='Import']");
    private final By countryDropdown = By.xpath(
            "//div[@class='container-fluid']//select[@name='mainCountry']"
    );
    private final By includeDischargeAndTranshipmentVoyage = By.xpath(
            "//div[@class='form-check']//input[@type='checkbox']"
    );
    private final By voyageInput = By.xpath(
            "//td[@class='voigeInp']//input[@placeholder='Type to search...']"
    );
    private final By voyageOptions = By.cssSelector("button");
    private final By searchButton = By.xpath("//button[normalize-space()='Search']");

    public void openImportAndEnableDischargeAndTranshipmentVoyages() {
        if (DriverFactory.getDriver().findElements(importHeader).stream()
                .noneMatch(WebElement::isDisplayed)) {
            wait.until(ExpectedConditions.elementToBeClickable(covMainMenu)).click();
            wait.until(ExpectedConditions.elementToBeClickable(importLink)).click();
            wait.until(driver ->
                    driver.findElements(importHeader).stream().anyMatch(WebElement::isDisplayed)
                            || driver.findElements(countryDropdown).stream()
                            .anyMatch(WebElement::isDisplayed)
            );
            pauseForObservation("Import module opened");
        }

        WebElement country = wait.until(
                ExpectedConditions.elementToBeClickable(countryDropdown)
        );
        new Select(country).selectByVisibleText("VN");
        pauseForObservation("Selected country VN");

        WebElement includeVoyagesCheckbox = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        includeDischargeAndTranshipmentVoyage
                )
        );
        if (!includeVoyagesCheckbox.isSelected()) {
            ((JavascriptExecutor) DriverFactory.getDriver()).executeScript(
                    "arguments[0].click();",
                    includeVoyagesCheckbox
            );
            wait.until(driver -> driver.findElement(
                    includeDischargeAndTranshipmentVoyage
            ).isSelected());
        }
        pauseForObservation("Enabled Include Discharge and Transhipment Voyages");
    }

    public void searchFirstVoyage(String voyageQuery) {
        WebElement input = wait.until(
                ExpectedConditions.visibilityOfElementLocated(voyageInput)
        );
        String currentVoyage = input.getDomProperty("value");
        if (currentVoyage != null
                && !currentVoyage.isBlank()
                && !currentVoyage.trim().equals(voyageQuery.trim())) {
            pauseForObservation("Using the already selected voyage: " + currentVoyage);
            clickSearchAndWait();
            return;
        }

        if (currentVoyage == null || currentVoyage.isBlank()) {
            input.sendKeys(voyageQuery);
            pauseForObservation("Entered voyage search: " + voyageQuery);
        } else {
            input.click();
        }

        WebElement firstVoyage = wait.withMessage(
                "No selectable voyage suggestions appeared for search: " + voyageQuery
        ).until(driver -> driver.findElements(voyageOptions).stream()
                .filter(WebElement::isDisplayed)
                .filter(WebElement::isEnabled)
                .filter(option -> option.getText().trim().startsWith(voyageQuery))
                .findFirst()
                .orElse(null));
        firstVoyage.click();
        pauseForObservation("Selected the first voyage suggestion");
        clickSearchAndWait();
    }

    private void clickSearchAndWait() {
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
        pauseForObservation("Clicked Search");
        int resultsPauseSeconds = Integer.getInteger(
                "import.voyage.resultsPauseSeconds",
                7
        );
        pause("Waiting to inspect Import search results", resultsPauseSeconds);
    }

    private void pauseForObservation(String step) {
        int seconds = Integer.getInteger("import.voyage.pauseSeconds", 2);
        pause(step, seconds);
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
                    "Interrupted while pausing to observe the Import voyage flow.",
                    interrupted
            );
        }
    }
}
