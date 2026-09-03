package tests.usermanagement;

import base.BaseTest;
import org.testng.annotations.Test;
import pages.usermanagement.CreateUserPage;

public class CreateUserTest extends BaseTest {

    @Test(
            description = "Admin can create a new user"
    )
    public void verifyAdminCanCreateNewUser() {

        // Navigate to User Management
        userManagementPage.openUserManagement();

        // Create User Page
        CreateUserPage createUserPage =
                new CreateUserPage();

        // Open Create User popup
        createUserPage.clickAddNewUser();

        // Enter user details
        createUserPage.enterFirstName("rama2");

        createUserPage.enterLastName("rama2");

        createUserPage.enterEmail(
                "rama2@cma-cgm.com"
        );

        // Select default country
        createUserPage.selectDefaultCountry("Taiwan");

        // Select Modify module
        createUserPage.selectModifyModule("COV");

        // Select View module
        createUserPage.selectViewModule("EXPORT");

        // Save the new user
        createUserPage.clickSaveChanges();

        System.out.println(
                "New user created successfully."
        );
    }
}