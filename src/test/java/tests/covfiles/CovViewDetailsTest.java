package tests.CovFiles;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CovFiles.CovFilesPage;

public class CovViewDetailsTest extends BaseTest {

    @Test
    public void testViewDetailsForFirstFileInGrid() {
        CovFilesPage covFilesPage = new CovFilesPage();
        covFilesPage.openCovFileList();

        String firstFileName = covFilesPage.clickViewDetail();
        Assert.assertFalse(firstFileName.isBlank(), "The first grid row has no filename.");
        covFilesPage.waitForCovMainFileDetails();
    }
}
