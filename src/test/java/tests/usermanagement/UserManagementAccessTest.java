package tests.usermanagement;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.usermanagement.UserManagementPage;

public class UserManagementAccessTest extends BaseTest {

    @Test
    public void verifyAdminCanAccessUserManagement() {

        UserManagementPage userManagementPage =
                new UserManagementPage();

        userManagementPage.openUserManagement();

        Assert.assertTrue(
                userManagementPage.isUserManagementPageDisplayed(),
                "User Management page is not displayed"
        );

        System.out.println(
                "User Management page opened successfully."
        );
    }
}