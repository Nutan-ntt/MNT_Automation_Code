package pages.ImportModule;

import base.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Import_DownloadExcelPage {

    private final WebDriverWait wait = new WebDriverWait(
            DriverFactory.getDriver(),
            Duration.ofSeconds(20)
    );
    private final By downloadAsExcelButton = By.xpath(
            "//button[normalize-space()='Download as Excel']"
    );

    public void downloadAsExcel() {
        wait.until(ExpectedConditions.elementToBeClickable(downloadAsExcelButton)).click();
    }
}
