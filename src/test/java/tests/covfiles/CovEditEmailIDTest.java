package tests.CovFiles;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CovFiles.CovFilesPage;

public class CovEditEmailIDTest extends BaseTest {

    private static final String DEFAULT_EMAIL_IDS =
            "covfiles.automation@example.com;covfiles.qa@example.com";

    @Test
    public void testEditFirstPartnerEmailIds() {
        CovFilesPage covFilesPage = new CovFilesPage();

        covFilesPage.openCovFileList();
        covFilesPage.clickViewDetail();
        covFilesPage.waitForCovMainFileDetails();

        String emailIdsToAdd = System.getProperty("cov.edit.emailIds", DEFAULT_EMAIL_IDS);
        String expectedEmailIds = covFilesPage.editFirstPartnerEmailIds(emailIdsToAdd);

        Assert.assertTrue(
                covFilesPage.areFirstPartnerEmailIdsDisplayed(expectedEmailIds),
                "The first partner row does not show the saved email addresses: "
                        + expectedEmailIds
        );
    }
}
