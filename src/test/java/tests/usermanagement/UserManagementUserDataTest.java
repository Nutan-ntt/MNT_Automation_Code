package tests.usermanagement;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.usermanagement.UserManagementPage;

public class UserManagementUserDataTest extends BaseTest {

    @Test
    public void verifyUserDataIsDisplayed() {

        UserManagementPage userManagementPage =
                new UserManagementPage();

        userManagementPage.openUserManagement();

        Assert.assertTrue(
                userManagementPage.isFirstUserDataDisplayed(),
                "No user data is displayed in the User Management grid."
        );
    }
}