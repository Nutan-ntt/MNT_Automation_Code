package base;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import pages.LoginPage;
import pages.usermanagement.UserManagementPage;
import utilities.ConfigReader;

public class BaseTest {

    protected UserManagementPage userManagementPage;
    @BeforeMethod
    public void setUp() {

        DriverFactory.initializeDriver();
        userManagementPage = new UserManagementPage();

        DriverFactory.getDriver().get(
                ConfigReader.getProperty("application.url")
        );

        // Create Login Page object
        LoginPage loginPage = new LoginPage();

        // Click Login
        loginPage.loginIfRequired();;
    }

    @AfterMethod
    public void tearDown()
    {
        //DriverFactory.quitDriver();
    }
}