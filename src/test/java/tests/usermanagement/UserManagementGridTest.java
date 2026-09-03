package tests.usermanagement;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.usermanagement.UserManagementPage;

public class UserManagementGridTest extends BaseTest {

    @Test(
            description = "Verify user management grid contents"
    )
    public void verifyUserManagementGridDisplaysUsers() {

        UserManagementPage userManagementPage =
                new UserManagementPage();

        userManagementPage.openUserManagement();

        int userCount = userManagementPage.getDisplayedUserCount();

        System.out.println(
                "Number of users displayed on current page: " + userCount
        );

        Assert.assertTrue(
                userCount > 0,
                "User Management grid does not display any users."
        );
    }
}