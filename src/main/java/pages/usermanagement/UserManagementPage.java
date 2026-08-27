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
    // Navigation
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

    // ---------------------------------------------------------
    // Search
    // ---------------------------------------------------------

    private final By nameEmailInput = By.id("nameEmail");

    private final By applyButton = By.xpath(
            "//button[normalize-space()='Apply']"
    );

    // ---------------------------------------------------------
    // Status dropdown
    // ---------------------------------------------------------

    /*
     * HTML:
     *
     * <div class="input-group">
     *     <input value="Active">
     *     <button type="button" ...>
     *         <button class="p-2 toolTipBlock bgnone">
     *             <svg>...</svg>
     *         </button>
     *     </button>
     * </div>
     *
     * We locate the button containing the SVG path.
     */
    private final By statusDropdownButton = By.xpath(
            "//input[@value='Active']" +
                    "/following-sibling::button//button[contains(@class,'toolTipBlock')]"
    );

    /*
     * All option:
     *
     * <button data-rr-ui-event-key="local-All"
     *         class="border-0 actMenuAuto cursor-pointer list-group-item list-group-item-action">
     *     All
     * </button>
     */
    private final By allStatusOption = By.xpath(
            "//button[@data-rr-ui-event-key='local-All' " +
                    "and normalize-space()='All']"
    );

    // ---------------------------------------------------------
    // User table
    // ---------------------------------------------------------

    private final By userRows = By.cssSelector(
            "#grid_container > table#list2 > tbody.tblBodyBlk > tr:not(.nested_row)"
    );

    // Three-dot / More button
    private final By moreButton = By.cssSelector(
            "td.editClmn button"
    );

    // ---------------------------------------------------------
    // Deactivate
    // ---------------------------------------------------------

    private final By deactivateButton = By.xpath(
            "//div[contains(@class,'actionBlock')]//button[normalize-space()='Deactivate']"
    );

    // Confirmation Yes
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

        clickApplyButton();

        System.out.println("Apply button clicked.");
    }

    // ---------------------------------------------------------
    // Click Apply
    // ---------------------------------------------------------

    private void clickApplyButton() {

        WebElement apply = wait.until(
                ExpectedConditions.elementToBeClickable(
                        applyButton
                )
        );

        scrollIntoView(apply);

        try {

            apply.click();

        } catch (ElementClickInterceptedException e) {

            System.out.println(
                    "Apply click intercepted. Using JavaScript click."
            );

            ((JavascriptExecutor) DriverFactory.getDriver())
                    .executeScript(
                            "arguments[0].click();",
                            apply
                    );
        }

        /*
         * DO NOT use stalenessOf(apply) here.
         *
         * The application does not replace the Apply button
         * after clicking it.
         *
         * Instead, wait until the table has finished updating.
         */
        wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(
                        userRows
                )
        );
    }

    // ---------------------------------------------------------
    // Change status filter to ALL
    // ---------------------------------------------------------

    public void selectAllStatus() {

        System.out.println(
                "Changing status filter from Active to All."
        );

        WebElement dropdown = wait.until(
                ExpectedConditions.elementToBeClickable(
                        statusDropdownButton
                )
        );

        scrollIntoView(dropdown);

        try {

            dropdown.click();

        } catch (ElementClickInterceptedException e) {

            System.out.println(
                    "Status dropdown click intercepted. " +
                            "Using JavaScript click."
            );

            ((JavascriptExecutor) DriverFactory.getDriver())
                    .executeScript(
                            "arguments[0].click();",
                            dropdown
                    );
        }

        System.out.println(
                "Status dropdown opened."
        );

        WebElement allOption = wait.until(
                ExpectedConditions.elementToBeClickable(
                        allStatusOption
                )
        );

        try {

            allOption.click();

        } catch (ElementClickInterceptedException e) {

            System.out.println(
                    "All option click intercepted. " +
                            "Using JavaScript click."
            );

            ((JavascriptExecutor) DriverFactory.getDriver())
                    .executeScript(
                            "arguments[0].click();",
                            allOption
                    );
        }

        System.out.println(
                "Status filter changed to All."
        );
    }

    // ---------------------------------------------------------
    // Search user with ALL status
    // ---------------------------------------------------------

    public void searchUserInAllStatus(String userName) {

        System.out.println(
                "Searching for user in ALL status: " + userName
        );

        selectAllStatus();

        /*
         * Click Apply after changing status.
         */
        clickApplyButton();

        System.out.println(
                "Apply clicked with status filter = All."
        );

        /*
         * Now wait for the requested user to appear.
         */
        getUserRow(userName);

        System.out.println(
                "User found with ALL status: " + userName
        );
    }

    // ---------------------------------------------------------
    // Find specific user row
    // ---------------------------------------------------------

    private WebElement getUserRow(String userName) {

        /*
         * contains() is used so this works even if the cell
         * contains additional whitespace or child elements.
         *
         * Example:
         *
         * <td class="um_name">
         *     <span>rama1</span>
         * </td>
         */
        String escapedUserName = escapeXPath(userName);

        By userRow = By.xpath(
                "//table[@id='list2']" +
                        "//tbody[contains(@class,'tblBodyBlk')]" +
                        "/tr[not(contains(@class,'nested_row'))]" +
                        "[.//td[contains(@class,'um_name') " +
                        "and contains(normalize-space(.), " +
                        escapedUserName +
                        ")]]"
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

        // Scroll row into view
        scrollIntoView(userRow);

        /*
         * IMPORTANT:
         *
         * Find the three-dot button INSIDE this particular row.
         */
        WebElement moreButtonElement = wait.until(
                ExpectedConditions.elementToBeClickable(
                        userRow.findElement(moreButton)
                )
        );

        System.out.println(
                "Three-dot button located."
        );

        try {

            moreButtonElement.click();

        } catch (ElementClickInterceptedException e) {

            System.out.println(
                    "Three-dot click intercepted. " +
                            "Using JavaScript click."
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

        try {

            deactivate.click();

        } catch (ElementClickInterceptedException e) {

            System.out.println(
                    "Deactivate click intercepted. " +
                            "Using JavaScript click."
            );

            ((JavascriptExecutor) DriverFactory.getDriver())
                    .executeScript(
                            "arguments[0].click();",
                            deactivate
                    );
        }

        System.out.println(
                "Deactivate option clicked."
        );

        // -----------------------------------------------------
        // Confirm
        // -----------------------------------------------------

        WebElement confirmYes = wait.until(
                ExpectedConditions.elementToBeClickable(
                        confirmYesButton
                )
        );

        try {

            confirmYes.click();

        } catch (ElementClickInterceptedException e) {

            System.out.println(
                    "Confirmation click intercepted. " +
                            "Using JavaScript click."
            );

            ((JavascriptExecutor) DriverFactory.getDriver())
                    .executeScript(
                            "arguments[0].click();",
                            confirmYes
                    );
        }

        System.out.println(
                "Deactivate confirmation accepted."
        );
    }

    // ---------------------------------------------------------
    // Get User Status
    // ---------------------------------------------------------

    public String getUserStatus(String userName) {

        WebElement userRow = getUserRow(userName);

        String status = userRow.findElement(
                By.cssSelector("td.um_status")
        ).getText().trim();

        System.out.println(
                "User '" + userName +
                        "' current status: " + status
        );

        return status;
    }

    // ---------------------------------------------------------
    // Verify users displayed
    // ---------------------------------------------------------

    public boolean areUsersDisplayed() {

        return !wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(
                        userRows
                )
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
    // First user data
    // ---------------------------------------------------------

    public boolean isFirstUserDataDisplayed() {

        try {

            WebElement firstRow = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            userRows
                    )
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
                ExpectedConditions.visibilityOfElementLocated(
                        userRows
                )
        );

        return firstRow.findElement(
                By.cssSelector("td.um_name")
        ).getText().trim();
    }

    // ---------------------------------------------------------
    // Scroll helper
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
    // XPath string helper
    // ---------------------------------------------------------

    private String escapeXPath(String value) {

        if (!value.contains("'")) {
            return "'" + value + "'";
        }

        if (!value.contains("\"")) {
            return "\"" + value + "\"";
        }

        String[] parts = value.split("'");

        StringBuilder result = new StringBuilder("concat(");

        for (int i = 0; i < parts.length; i++) {

            if (i > 0) {
                result.append(", \"'\", ");
            }

            result.append("'")
                    .append(parts[i])
                    .append("'");
        }

        result.append(")");

        return result.toString();
    }
}