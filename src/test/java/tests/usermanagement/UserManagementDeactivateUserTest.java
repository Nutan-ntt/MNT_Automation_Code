package tests.usermanagement;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UserManagementDeactivateUserTest extends BaseTest {

    @Test
    public void verifyAdminCanDeactivateUser() {

        String userName = "rama1";

        // -----------------------------------------------------
        // Open User Management
        // -----------------------------------------------------

        userManagementPage.openUserManagement();

        // -----------------------------------------------------
        // Search active user
        // -----------------------------------------------------

        userManagementPage.searchUser(userName);

        // -----------------------------------------------------
        // Verify user is currently Active
        // -----------------------------------------------------

        String statusBefore =
                userManagementPage.getUserStatus(userName);

        System.out.println(
                "Status before deactivation: " + statusBefore
        );

        Assert.assertEquals(
                statusBefore,
                "Active",
                "User should be Active before deactivation."
        );

        // -----------------------------------------------------
        // Deactivate user
        // -----------------------------------------------------

        userManagementPage.deactivateUser(userName);

        // -----------------------------------------------------
        // IMPORTANT:
        //
        // After deactivation the default filter is still
        // Active, so rama1 will disappear.
        //
        // Change filter to ALL and search again.
        // -----------------------------------------------------

        userManagementPage.searchUserInAllStatus(userName);

        // -----------------------------------------------------
        // Verify status
        // -----------------------------------------------------

        String statusAfter =
                userManagementPage.getUserStatus(userName);

        System.out.println(
                "Status after deactivation: " + statusAfter
        );

        Assert.assertEquals(
                statusAfter,
                "Inactive",
                "User was not deactivated successfully."
        );

        System.out.println(
                "User " + userName +
                        " successfully deactivated."
        );
    }
}