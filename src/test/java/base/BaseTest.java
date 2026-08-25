package base;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import pages.LoginPage;
import utilities.ConfigReader;

public class BaseTest {

    @BeforeMethod
    public void setUp() {

        DriverFactory.initializeDriver();

        DriverFactory.getDriver().get(
                ConfigReader.getProperty("application.url")
        );

        // Create Login Page object
        LoginPage loginPage = new LoginPage();

        // Click Login
        loginPage.clickLogin();
    }

    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}