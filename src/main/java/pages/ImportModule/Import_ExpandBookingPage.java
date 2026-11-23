package pages.ImportModule;

import base.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Import_ExpandBookingPage {

    private final WebDriverWait wait = new WebDriverWait(
            DriverFactory.getDriver(),
            Duration.ofSeconds(20)
    );

    private final By expandLatestBookingPartner = By.xpath(
            "//body[1]/div[1]/div[1]/div[3]/div[1]/div[2]/table[1]/tbody[1]" +
                    "/tr[1]/td[2]/button[1]/b[1]"
    );
    private final By firstBooking = By.xpath(
            "//tbody/tr[1]/td[2]/button[1]/div[1]/span[1]/button[1]/div[1]/div[1]"
    );
    private final By parametersButton = By.xpath(
            "//button[normalize-space()='Parameters']"
    );
    private final By bookingIdInput = By.id("jobRef");
    private final By searchButton = By.xpath("//button[normalize-space()='Search']");

    public String copyFirstBookingIdToParameters() {
        wait.until(ExpectedConditions.elementToBeClickable(expandLatestBookingPartner)).click();

        WebElement bookingIdElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(firstBooking)
        );
        String bookingId = bookingIdElement.getText().trim();
        if (bookingId.isEmpty()) {
            throw new IllegalStateException(
                    "The first nested booking element did not contain a booking ID."
            );
        }

        wait.until(ExpectedConditions.elementToBeClickable(parametersButton)).click();

        WebElement bookingInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(bookingIdInput)
        );
        bookingInput.clear();
        bookingInput.sendKeys(bookingId);
        wait.until(driver -> bookingId.equals(
                driver.findElement(bookingIdInput).getDomProperty("value")
        ));
        pause(
                "Booking ID entered in Parameters: " + bookingId,
                "import.expandBooking.beforeSearchPauseSeconds",
                5
        );

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
        pause(
                "Clicked Search; keeping booking results visible",
                "import.expandBooking.resultsPauseSeconds",
                15
        );
        wait.until(ExpectedConditions.elementToBeClickable(
                expandLatestBookingPartner
        )).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstBooking));
        pause(
                "Expanded the searched booking details",
                "import.expandBooking.detailsPauseSeconds",
                15
        );
        return bookingId;
    }

    private void pause(String step, String property, int defaultSeconds) {
        int seconds = Integer.getInteger(property, defaultSeconds);
        if (seconds <= 0) {
            return;
        }

        System.out.println(step);
        try {
            Thread.sleep(Duration.ofSeconds(seconds).toMillis());
        } catch (InterruptedException interrupted) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(
                    "Interrupted while pausing to observe the expanded booking flow.",
                    interrupted
            );
        }
    }
}
