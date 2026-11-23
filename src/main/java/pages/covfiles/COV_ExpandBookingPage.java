package pages.CovFiles;

import base.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class COV_ExpandBookingPage {

    private final WebDriverWait wait = new WebDriverWait(
            DriverFactory.getDriver(),
            Duration.ofSeconds(20)
    );

    private final By expandBookingPartner = By.xpath(
            "//tbody/tr[1]/td[2]/button[1]/b[1]"
    );
    private final By firstBookingId = By.xpath(
            "//body[1]/div[1]/div[1]/div[3]/div[1]/div[1]/div[2]/div[1]/div[1]" +
                    "/table[1]/tbody[1]/tr[2]/td[3]/div[1]/div[1]/table[1]/tbody[1]" +
                    "/tr[1]/td[3]/button[1]/div[1]/span[1]/button[1]/div[1]/div[1]"
    );
    private final By parametersButton = By.xpath(
            "//button[normalize-space()='Parameters']"
    );
    private final By bookingIdInput = By.xpath(
            "//body[1]/div[1]/div[1]/div[3]/div[1]/div[1]/div[1]/div[1]/div[1]" +
                    "/div[1]/div[1]/form[1]/table[1]/tr[1]/td[1]/div[1]/div[2]/input[1]"
    );
    private final By searchButton = By.xpath("//img[@title='Search']");

    public String expandCopyAndSearchBookingId() {
        wait.until(ExpectedConditions.elementToBeClickable(expandBookingPartner)).click();

        WebElement bookingElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(firstBookingId)
        );
        String bookingId = bookingElement.getText().trim();
        if (bookingId.isEmpty()) {
            throw new IllegalStateException("The first expanded booking ID is empty.");
        }

        wait.until(ExpectedConditions.elementToBeClickable(parametersButton)).click();

        WebElement input = wait.until(
                ExpectedConditions.visibilityOfElementLocated(bookingIdInput)
        );
        input.clear();
        input.sendKeys(bookingId);
        wait.until(driver -> bookingId.equals(
                driver.findElement(bookingIdInput).getDomProperty("value")
        ));

        WebElement searchIcon = wait.until(
                ExpectedConditions.visibilityOfElementLocated(searchButton)
        );
        WebElement searchButtonElement = searchIcon.findElement(By.xpath("./ancestor::button[1]"));
        wait.until(ExpectedConditions.elementToBeClickable(searchButtonElement)).click();

        wait.until(ExpectedConditions.elementToBeClickable(expandBookingPartner)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstBookingId));
        return bookingId;
    }
}
