package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public class DriverFactory {

    private static WebDriver driver;

    public static void initializeDriver() {

        // Suppress Selenium CDP version warning
        java.util.logging.Logger.getLogger(
                "org.openqa.selenium.devtools.CdpVersionFinder"
        ).setLevel(
                java.util.logging.Level.OFF
        );

        ChromeOptions options = new ChromeOptions();
        //EdgeOptions options = new EdgeOptions();

        options.addArguments("--disable-gpu");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.setAcceptInsecureCerts(true);

        // Use a separate Chrome profile for automation
        options.addArguments("--user-data-dir=C:\\SeleniumChromeProfile");
        //options.addArguments("--user-data-dir=C:\\SeleniumEdgeProfile");

        driver = new ChromeDriver(options);
        //driver = new EdgeDriver(options);

        driver.manage().window().maximize();
    }

    public static WebDriver getDriver() {
        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}