package pages.CovFiles;

import base.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class CovFilesPage {

    private final WebDriverWait wait = new WebDriverWait(
            DriverFactory.getDriver(),
            Duration.ofSeconds(15)
    );

    // ---------------------------------------------------------
    // Navigation
    // ---------------------------------------------------------

    private final By covMainMenu = By.xpath(
            "//button[contains(@class,'bgnone') " +
                    "and contains(@class,'w100') " +
                    "and contains(normalize-space(.),'COV Main')]"
    );

    private final By covFileListLink = By.xpath(
            "//a[@href='/cov_file_list' " +
                    "and normalize-space()='COV File List']"
    );

    private final By fileListHeader = By.xpath(
            "//h1[normalize-space()='File List']"
    );

    // ---------------------------------------------------------
    // Search
    // ---------------------------------------------------------

    private final By fileNameInput = By.id(
            "formBasicInput"
    );

    private final By searchButton = By.xpath(
            "//button[@type='submit']" +
                    "//img[@alt='Search']" +
                    "/parent::button"
    );

    // ---------------------------------------------------------
    // Results
    // ---------------------------------------------------------

    private final By resultRows = By.cssSelector(
            "#grid_container table tbody tr"
    );

    // View detail button
    private final By viewDetailButton = By.xpath(
            "//div[normalize-space()='View detail']"
    );

    // ---------------------------------------------------------
    // Open COV File List
    // ---------------------------------------------------------

    public void openCovFileList() {

        // -----------------------------------------------------
        // Click COV Main dropdown
        // -----------------------------------------------------

        WebElement covMenu = wait.until(
                ExpectedConditions.elementToBeClickable(
                        covMainMenu
                )
        );

        scrollIntoView(covMenu);

        try {

            covMenu.click();

        } catch (ElementClickInterceptedException e) {

            System.out.println(
                    "COV Main click intercepted. " +
                            "Using JavaScript click."
            );

            ((JavascriptExecutor) DriverFactory.getDriver())
                    .executeScript(
                            "arguments[0].click();",
                            covMenu
                    );
        }

        System.out.println(
                "COV Main dropdown clicked."
        );

        // Pause so we can see dropdown
        pause(3);

        // -----------------------------------------------------
        // Click COV File List
        // -----------------------------------------------------

        WebElement covFileList = wait.until(
                ExpectedConditions.elementToBeClickable(
                        covFileListLink
                )
        );

        scrollIntoView(covFileList);

        try {

            covFileList.click();

        } catch (ElementClickInterceptedException e) {

            System.out.println(
                    "COV File List click intercepted. " +
                            "Using JavaScript click."
            );

            ((JavascriptExecutor) DriverFactory.getDriver())
                    .executeScript(
                            "arguments[0].click();",
                            covFileList
                    );
        }

        System.out.println(
                "COV File List option clicked."
        );

        // Pause so we can see navigation
        pause(3);

        // -----------------------------------------------------
        // Verify File List page opened
        // -----------------------------------------------------

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        fileListHeader
                )
        );

        System.out.println(
                "COV File List page opened successfully."
        );

        // Pause so we can see File List page
        pause(3);
    }

    // ---------------------------------------------------------
    // Search File
    // ---------------------------------------------------------

    public void searchFile(String fileName) {

        WebElement input = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        fileNameInput
                )
        );

        input.clear();

        input.sendKeys(fileName);

        System.out.println(
                "File name entered: " + fileName
        );

        // Pause so we can see entered file name
        pause(3);

        // Click Search
        clickSearchButton();

        System.out.println(
                "Search button clicked."
        );

        // Pause so we can see search results
        pause(3);
    }

    // ---------------------------------------------------------
    // Click Search
    // ---------------------------------------------------------

    private void clickSearchButton() {

        WebElement search = wait.until(
                ExpectedConditions.elementToBeClickable(
                        searchButton
                )
        );

        scrollIntoView(search);

        try {

            search.click();

        } catch (ElementClickInterceptedException e) {

            System.out.println(
                    "Search click intercepted. " +
                            "Using JavaScript click."
            );

            ((JavascriptExecutor) DriverFactory.getDriver())
                    .executeScript(
                            "arguments[0].click();",
                            search
                    );
        }

        /*
         * Do not use stalenessOf(search).
         *
         * The application may keep the same Search button
         * after performing the search.
         *
         * Wait for the result rows instead.
         */
        wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(
                        resultRows
                )
        );
    }

    // ---------------------------------------------------------
    // Get Search Result Count
    // ---------------------------------------------------------

    public int getSearchResultsCount() {

        List<WebElement> rows = DriverFactory.getDriver()
                .findElements(resultRows);

        System.out.println(
                "Number of search results displayed: "
                        + rows.size()
        );

        return rows.size();
    }

    // ---------------------------------------------------------
    // Click View Detail
    // ---------------------------------------------------------

    public void clickViewDetail() {

        WebElement viewDetail = wait.until(
                ExpectedConditions.elementToBeClickable(
                        viewDetailButton
                )
        );

        scrollIntoView(viewDetail);

        System.out.println(
                "View detail button located."
        );

        // Pause before clicking
        pause(3);

        try {

            viewDetail.click();

        } catch (ElementClickInterceptedException e) {

            System.out.println(
                    "View detail click intercepted. " +
                            "Using JavaScript click."
            );

            ((JavascriptExecutor) DriverFactory.getDriver())
                    .executeScript(
                            "arguments[0].click();",
                            viewDetail
                    );
        }

        System.out.println(
                "View detail clicked successfully."
        );

        // Pause so we can see the details page
        pause(3);
    }

    // ---------------------------------------------------------
    // Verify File List Page
    // ---------------------------------------------------------

    public boolean isFileListPageDisplayed() {

        try {

            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            fileListHeader
                    )
            ).isDisplayed();

        } catch (Exception e) {

            return false;
        }
    }

    // ---------------------------------------------------------
    // Scroll Helper
    // ---------------------------------------------------------

    private void scrollIntoView(WebElement element) {

        ((JavascriptExecutor) DriverFactory.getDriver())
                .executeScript(
                        "arguments[0].scrollIntoView(" +
                                "{block:'center', inline:'center'});",
                        element
                );
    }

    // ---------------------------------------------------------
    // Pause Helper
    // ---------------------------------------------------------

    private void pause(int seconds) {

        try {

            Thread.sleep(seconds * 1000L);

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
        }
    }
}