package tests.ImportModule;

import base.BaseTest;
import org.testng.annotations.Test;
import pages.ImportModule.Import_SendRemainderPage;

public class Import_SendremainderTest extends BaseTest {

    @Test
    public void selectBookingsAndSendReminders() {
        Import_SendRemainderPage reminderPage = new Import_SendRemainderPage();
        reminderPage.selectBookingsAndSendReminders();
        reminderPage.prepareTemplateAndSendEmail();
    }
}
