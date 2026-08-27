package tests.usermanagement;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UserManagementUserDataTest extends BaseTest {

    @Test
    public void verifyUserDataIsDisplayed() {

        userManagementPage.openUserManagement();

        // Select "All" status so both Active and Inactive users are displayed
        userManagementPage.selectAllStatus();

        // Verify that at least one user is displayed
        Assert.assertTrue(
                userManagementPage.areUsersDisplayed(),
                "No users are displayed in the User Management grid."
        );

        System.out.println(
                "User data is displayed successfully in the User Management grid."
        );
    }
}