package tests.CovFiles;

import base.BaseTest;
import org.testng.annotations.Test;
import pages.CovFiles.CovFilesPage;

public class CovSearchFilesTest extends BaseTest {

    @Test
    public void testSearchAndViewFileDetails() {

        CovFilesPage covFilesPage =
                new CovFilesPage();

        // COV Main → COV File List
        covFilesPage.openCovFileList();

        // Search file
        covFilesPage.searchFile(
                "test"
        );

        // Click View detail
        covFilesPage.clickViewDetail();
    }
}