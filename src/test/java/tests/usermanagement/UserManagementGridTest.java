package tests.usermanagement;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UserManagementGridTest extends BaseTest {

    @Test(
            description = "Verify users are present in User Management"
    )
    public void verifyUsersArePresent() {

        userManagementPage.openUserManagement();

        userManagementPage.clickApplyButton();

        boolean usersPresent =
                userManagementPage.areUsersPresent();

        Assert.assertTrue(
                usersPresent,
                "No users are present in User Management."
        );

        System.out.println(
                "Users are present in User Management."
        );
    }
}