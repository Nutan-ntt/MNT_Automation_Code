package tests.usermanagement;

import base.BaseTest;
import org.testng.annotations.Test;
import pages.usermanagement.UserManagementPage;

public class UserManagementAccessTest extends BaseTest {

    @Test
    public void verifyAdminCanAccessUserManagement() {

        UserManagementPage userManagementPage =
                new UserManagementPage();

        userManagementPage.openUserManagement();
    }
}