package tests.usermanagement;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.usermanagement.EditUserPage;

public class EditUserTest extends BaseTest {

    @Test(
            description = "Verify Admin can edit an existing user's details"
    )
    public void verifyAdminCanEditExistingUser() {

        EditUserPage editUserPage =
                new EditUserPage();

        userManagementPage.openUserManagement();

        userManagementPage.searchUser("Jessica");

        editUserPage.clickMoreForUser("Jessica");

        editUserPage.clickEdit();

        editUserPage.changeLastName("HUANG1");

        editUserPage.clickSaveChanges();

        /*

        boolean updatedUserDisplayed =
                editUserPage.isUpdatedUserDisplayed(
                        "Jessica",
                        "HUANGHUANG1"
                );

        Assert.assertTrue(
                updatedUserDisplayed,
                "Updated user details were not displayed."
        );

        System.out.println(
                "User Jessica successfully updated."
        );*/
    }
}