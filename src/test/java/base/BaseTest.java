package base;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeMethod;
import pages.LoginPage;
import pages.usermanagement.UserManagementPage;
import utilities.ConfigReader;

public class BaseTest {

    protected UserManagementPage userManagementPage;
    private static boolean sessionInitialized;

    @BeforeSuite
    public void startBrowser() {
        DriverFactory.initializeDriver();
        if (!sessionInitialized) {
            DriverFactory.getDriver().get(
                    ConfigReader.getProperty("application.url")
            );
            new LoginPage().loginIfRequired();
            sessionInitialized = true;
        }
    }

    @BeforeMethod
    public void setUp() {
        userManagementPage = new UserManagementPage();
    }

    @AfterSuite
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}