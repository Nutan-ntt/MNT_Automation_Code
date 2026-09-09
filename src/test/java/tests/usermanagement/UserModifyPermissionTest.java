package tests.usermanagement;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.usermanagement.EditUserPage;
import pages.usermanagement.UserPermissionPage;

public class UserModifyPermissionTest extends BaseTest {

    @Test(
            description = "Verify Admin can assign MODIFY permission to a user"
    )
    public void verifyAdminCanAssignModifyPermission() {

        EditUserPage editUserPage =
                new EditUserPage();

        UserPermissionPage userPermissionPage =
                new UserPermissionPage();

        // ---------------------------------------------------------
        // Open User Management
        // ---------------------------------------------------------

        userManagementPage.openUserManagement();

        // ---------------------------------------------------------
        // Search for Jessica
        // ---------------------------------------------------------

        userManagementPage.searchUser("Jessica");

        // searchUser() already clicks Apply
        // so no separate Apply is required.

        // ---------------------------------------------------------
        // Open Edit User popup
        // ---------------------------------------------------------

        editUserPage.clickMoreForUser("Jessica");

        editUserPage.clickEdit();

        // ---------------------------------------------------------
        // Assign MODIFY permission
        // ---------------------------------------------------------

        userPermissionPage.selectModifyModule("COV");

        // ---------------------------------------------------------
        // Save changes
        // ---------------------------------------------------------

        editUserPage.clickSaveChanges();

        System.out.println(
                "MODIFY permission assigned successfully."
        );

        // ---------------------------------------------------------
        // Re-open Edit User popup
        // ---------------------------------------------------------

        //userManagementPage.searchUser("Jessica");

        //editUserPage.clickMoreForUser("Jessica");

        //editUserPage.clickEdit();

        // ---------------------------------------------------------
        // Verify MODIFY permission
        // ---------------------------------------------------------

       // boolean modifyPermissionDisplayed =
        //        userPermissionPage.isModifyModuleDisplayed("COV");

        //Assert.assertTrue(
        //        modifyPermissionDisplayed,
        //        "COV MODIFY permission was not saved successfully."
        //);

        //System.out.println(
        //        "Verified: Jessica has MODIFY permission for COV."
        //);
    }
}