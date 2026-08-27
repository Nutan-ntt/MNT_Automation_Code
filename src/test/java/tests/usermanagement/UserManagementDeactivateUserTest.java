package tests.usermanagement;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UserManagementDeactivateUserTest extends BaseTest {

    @Test
    public void verifyAdminCanDeactivateUser() {

        // ---------------------------------------------------------
        // Test data
        // ---------------------------------------------------------

        String userName = "rama1";

        // ---------------------------------------------------------
        // Open User Management
        // ---------------------------------------------------------

        userManagementPage.openUserManagement();

        // ---------------------------------------------------------
        // Search for user
        // ---------------------------------------------------------

        userManagementPage.searchUser(userName);

        // ---------------------------------------------------------
        // Verify user is found and get current status
        // ---------------------------------------------------------

        String statusBefore =
                userManagementPage.getUserStatus(userName);

        System.out.println(
                "Status before deactivation: " + statusBefore
        );

        // Make sure the user was actually found
        Assert.assertNotNull(
                statusBefore,
                "User status should not be null."
        );

        // ---------------------------------------------------------
        // Deactivate user
        // ---------------------------------------------------------

        userManagementPage.deactivateUser(userName);

        // ---------------------------------------------------------
        // Verify status after deactivation
        // ---------------------------------------------------------

        String statusAfter =
                userManagementPage.getUserStatus(userName);

        System.out.println(
                "Status after deactivation: " + statusAfter
        );

        // ---------------------------------------------------------
        // Assertion
        // ---------------------------------------------------------

        Assert.assertEquals(
                statusAfter,
                "Inactive",
                "User was not deactivated successfully."
        );
    }
}