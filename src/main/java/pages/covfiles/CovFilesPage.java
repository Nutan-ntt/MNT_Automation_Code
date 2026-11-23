package pages.CovFiles;

import base.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.ConfigReader;

import java.time.Duration;
import java.util.Arrays;
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

    private final By firstResultFileName = By.xpath(
            "//tbody/tr[1]/td[1]/button[1]/div[1]/span[1]/button[1]/div[1]"
    );

    // View detail button
    private final By covMainTitle = By.xpath(
            "//*[contains(translate(normalize-space(.)," +
                    "'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')," +
                    "'CHANGE OF VESSEL')]"
    );

    private final By parametersHeading = By.xpath(
            "//*[normalize-space()='Parameters']"
    );

    private final By bookingPartyHeading = By.xpath(
            "//*[normalize-space()='Booking Party']"
    );

    private final By firstPartnerEmailEditButton = By.xpath(
            "//body[1]/div[1]/div[1]/div[3]/div[1]/div[1]/div[2]/div[1]/div[1]/table[1]/tbody[1]/tr[1]/td[10]/div[1]/button[1]/img[1]"
    );

    private final By partnerEmailInput = By.xpath(
            "//textarea[contains(@class,'textAreaCol')]"
    );

    private final By firstPartnerEmailSaveButton = By.xpath(
            "//body[1]/div[1]/div[1]/div[3]/div[1]/div[1]/div[2]/div[1]/div[1]/table[1]/tbody[1]/tr[1]/td[10]/div[1]/div[1]/button[2]/img[1]"
    );

    private final By firstPartnerEmailCancelButton = By.xpath(
            "//tbody//tr[1]//div[@class='covEdit']//div//button[1]//img[1]"
    );

    // ---------------------------------------------------------
    // Upload
    // ---------------------------------------------------------

    private final By UploadButton=By.cssSelector(
            "div[class='text-end col'] button[type='button']"
    );

    private final By uploadPopupHeader = By.cssSelector(
            ".dragbleHeady"
    );

    private final By fileInput = By.xpath(
            "//input[@id='formFile']"
    );

    private final By uploadConfirmButton = By.xpath(
            "//button[normalize-space()='Upload']"
    );

    // ---------------------------------------------------------
    // Open COV File List
    // ---------------------------------------------------------

    public void openCovFileList() {
        DriverFactory.getDriver().navigate().to(
                ConfigReader.getProperty("application.url")
        );

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

    public String clickViewDetail() {
        List<WebElement> rows = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(resultRows)
        );
        WebElement firstRow = rows.stream()
                .filter(row -> row.isDisplayed() && !row.getText().isBlank())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "No COV file rows are available to view."
                ));

        String fileName = firstRow.findElement(By.xpath("./td[1]")).getText().trim();
        if (fileName.isEmpty()) {
            throw new IllegalStateException("The first COV file row has no filename.");
        }

        WebElement viewDetail = firstRow.findElement(
                By.xpath(".//td[4]//*[normalize-space()='View detail']")
        );
        wait.until(ExpectedConditions.elementToBeClickable(viewDetail));
        scrollIntoView(viewDetail);

        try {
            viewDetail.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) DriverFactory.getDriver())
                    .executeScript("arguments[0].click();", viewDetail);
        }

        return fileName;
    }

    public void waitForCovMainFileDetails() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(covMainTitle));
        wait.until(ExpectedConditions.visibilityOfElementLocated(parametersHeading));
        wait.until(ExpectedConditions.visibilityOfElementLocated(bookingPartyHeading));
    }

    public String editFirstPartnerEmailIds(String emailIdsToAdd) {
        if (emailIdsToAdd == null || emailIdsToAdd.isBlank()
                || Arrays.stream(emailIdsToAdd.split(";", -1)).anyMatch(String::isBlank)) {
            throw new IllegalArgumentException(
                    "Provide non-empty email addresses to add, separated by semicolons."
            );
        }

        WebElement editButton = wait.until(
                ExpectedConditions.elementToBeClickable(firstPartnerEmailEditButton)
        );
        editButton.click();

        WebElement emailInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(partnerEmailInput)
        );
        if (!emailInput.isEnabled()) {
            throw new IllegalStateException("The partner email field is not enabled for editing.");
        }

        String existingEmailIds = emailInput.getDomProperty("value");
        List<String> existingEmails = Arrays.stream(existingEmailIds.split(";"))
                .map(String::trim)
                .filter(email -> !email.isEmpty())
                .toList();
        List<String> newEmails = Arrays.stream(emailIdsToAdd.split(";"))
                .map(String::trim)
                .filter(email -> existingEmails.stream().noneMatch(
                        existing -> existing.equalsIgnoreCase(email)
                ))
                .toList();

        String appendedEmailIds = String.join(";", newEmails);
        if (!appendedEmailIds.isEmpty()) {
            emailInput.sendKeys(
                    Keys.END,
                    (existingEmailIds.isBlank() ? "" : ";") + appendedEmailIds
            );
        }
        String updatedEmailIds = existingEmailIds.isBlank()
                ? appendedEmailIds
                : existingEmailIds + (appendedEmailIds.isEmpty() ? "" : ";" + appendedEmailIds);

        wait.until(ExpectedConditions.elementToBeClickable(firstPartnerEmailSaveButton)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(partnerEmailInput));
        return updatedEmailIds;
    }

    public void cancelFirstPartnerEmailEdit() {
        wait.until(ExpectedConditions.elementToBeClickable(firstPartnerEmailCancelButton)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(partnerEmailInput));
    }

    public boolean areFirstPartnerEmailIdsDisplayed(String emailIds) {
        List<String> expectedEmails = Arrays.stream(emailIds.split(";"))
                .map(String::trim)
                .map(String::toLowerCase)
                .toList();

        wait.until(ExpectedConditions.elementToBeClickable(firstPartnerEmailEditButton)).click();
        WebElement emailInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(partnerEmailInput)
        );
        String savedEmails = emailInput.getDomProperty("value");
        if (savedEmails == null) {
            cancelFirstPartnerEmailEdit();
            return false;
        }
        List<String> savedEmailList = Arrays.stream(savedEmails.split(";"))
                .map(String::trim)
                .map(String::toLowerCase)
                .filter(email -> !email.isEmpty())
                .toList();

        cancelFirstPartnerEmailEdit();
        return savedEmailList.containsAll(expectedEmails);
    }

    public void downloadFirstSearchResult() {
        WebElement fileName = wait.until(
                ExpectedConditions.elementToBeClickable(firstResultFileName)
        );

        scrollIntoView(fileName);

        try {
            fileName.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) DriverFactory.getDriver())
                    .executeScript("arguments[0].click();", fileName);
        }
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

    // ---------------------------------------------------------
    // Open Upload Popup
    // ---------------------------------------------------------

    public void clickUploadButton() {

        WebElement upload = wait.until(
                ExpectedConditions.elementToBeClickable(
                        UploadButton
                )
        );

        scrollIntoView(upload);

        try {

            upload.click();

        } catch (ElementClickInterceptedException e) {

            System.out.println(
                    "Upload button click intercepted. " +
                            "Using JavaScript click."
            );

            ((JavascriptExecutor) DriverFactory.getDriver())
                    .executeScript(
                            "arguments[0].click();",
                            upload
                    );
        }

        System.out.println(
                "Upload button clicked."
        );

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        uploadPopupHeader
                )
        );

        System.out.println(
                "Upload popup displayed."
        );

        // Pause so we can see the popup
        pause(3);
    }

    // ---------------------------------------------------------
    // Select File
    // ---------------------------------------------------------

    public void selectFile(String filePath) {

        WebElement fileUpload = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        fileInput
                )
        );

        fileUpload.sendKeys(filePath);

        System.out.println(
                "File selected: " + filePath
        );

        // Pause so we can see the selected file name displayed
        pause(3);
    }

    // ---------------------------------------------------------
    // Click Upload (confirm)
    // ---------------------------------------------------------

    public void clickConfirmUpload() {

        WebElement confirmUpload = wait.until(
                ExpectedConditions.elementToBeClickable(
                        uploadConfirmButton
                )
        );

        scrollIntoView(confirmUpload);

        try {

            confirmUpload.click();

        } catch (ElementClickInterceptedException e) {

            System.out.println(
                    "Upload confirm click intercepted. " +
                            "Using JavaScript click."
            );

            ((JavascriptExecutor) DriverFactory.getDriver())
                    .executeScript(
                            "arguments[0].click();",
                            confirmUpload
                    );
        }

        System.out.println(
                "Upload confirmed."
        );

        // Pause so we can see result of upload
        pause(3);
    }

    // ---------------------------------------------------------
    // Upload File (combined flow)
    // ---------------------------------------------------------

    public void uploadFile(String filePath) {

        clickUploadButton();

        selectFile(filePath);

        clickConfirmUpload();

        System.out.println(
                "File upload flow completed for: " + filePath
        );
    }
}