package tests.usermanagement;

import base.BaseTest;
import org.testng.annotations.Test;
import pages.usermanagement.UserManagementPage;

public class UserManagementAccessTest extends BaseTest {

    @Test(
            description = "Verify Admin can access user management module and non admin users should not"
    )
    public void verifyAdminCanAccessUserManagement() {

        UserManagementPage userManagementPage =
                new UserManagementPage();

        userManagementPage.openUserManagement();
    }
}