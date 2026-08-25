package tests;

import base.BaseTest;
import base.DriverFactory;
import org.testng.annotations.Test;
import utilities.ConfigReader;

public class FrameworkTest extends BaseTest {

    @Test
    public void verifyApplicationLaunch() throws InterruptedException {

        DriverFactory.getDriver().get(
                ConfigReader.getProperty("application.url")
        );

        System.out.println(
                "Page title: " + DriverFactory.getDriver().getTitle()
        );
        Thread.sleep(150000);
        System.out.println("Application opened successfully.");
    }
}