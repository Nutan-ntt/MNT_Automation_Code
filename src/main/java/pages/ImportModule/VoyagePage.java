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

public class VoyagePage {

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
    private final By voyageInput = By.xpath(
            "//td[@class='voigeInp']//input[@placeholder='Type to search...']"
    );
    private final By searchButton = By.xpath("//button[normalize-space()='Search']");
    private final By parametersButton = By.xpath("//button[normalize-space()='Parameters']");

    public void openImportModule() {
        if (!DriverFactory.getDriver().findElements(importHeader).isEmpty()
                && DriverFactory.getDriver().findElement(importHeader).isDisplayed()) {
            return;
        }

        wait.until(ExpectedConditions.elementToBeClickable(covMainMenu)).click();
        wait.until(ExpectedConditions.elementToBeClickable(importLink)).click();
        wait.until(driver ->
                driver.findElements(importHeader).stream().anyMatch(WebElement::isDisplayed)
                        || driver.findElements(countryDropdown).stream()
                        .anyMatch(WebElement::isDisplayed)
        );
        pauseForObservation("Import module opened");
    }

    public void searchFirstVoyage(String voyageNumber) {
        WebElement country = wait.until(
                ExpectedConditions.elementToBeClickable(countryDropdown)
        );
        new Select(country).selectByVisibleText("VN");
        pauseForObservation("Selected country VN");

        WebElement input = wait.until(
                ExpectedConditions.visibilityOfElementLocated(voyageInput)
        );
        input.clear();
        input.sendKeys(voyageNumber);
        pauseForObservation("Entered voyage search: " + voyageNumber);

        String enteredVoyage = voyageNumber.trim();
        String currentVoyage = input.getDomProperty("value");
        if (currentVoyage == null || currentVoyage.trim().equals(enteredVoyage)) {
            input.clear();
            input.sendKeys("0HBF6N1MA");
            wait.until(driver -> "0HBF6N1MA".equals(
                    driver.findElement(voyageInput).getDomProperty("value")
            ));
            pauseForObservation("Entered default voyage 0HBF6N1MA directly");
        } else {
            pauseForObservation("Using auto-selected voyage: " + currentVoyage);
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
        pauseForObservation("Clicked Search");
        pauseForSearchResults();

        WebElement parameters = wait.until(
                ExpectedConditions.elementToBeClickable(parametersButton)
        );
        parameters.click();
        pauseForObservation("Opened Parameters");
    }

    private void pauseForObservation(String step) {
        int seconds = Integer.getInteger("import.voyage.pauseSeconds", 2);
        pause(step, seconds);
    }

    private void pauseForSearchResults() {
        int seconds = Integer.getInteger("import.voyage.resultsPauseSeconds", 7);
        pause("Waiting to inspect Import search results", seconds);
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
