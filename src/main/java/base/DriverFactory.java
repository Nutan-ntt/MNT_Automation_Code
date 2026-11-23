package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class DriverFactory {

    private static WebDriver driver;

    public static void initializeDriver() {
        if (driver != null) {
            return;
        }

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

        Map<String, Object> preferences = new HashMap<>();
        preferences.put(
                "download.default_directory",
                Paths.get(System.getProperty("user.home"), "Downloads").toString()
        );
        preferences.put("download.prompt_for_download", false);
        preferences.put("download.directory_upgrade", true);
        options.setExperimentalOption("prefs", preferences);

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