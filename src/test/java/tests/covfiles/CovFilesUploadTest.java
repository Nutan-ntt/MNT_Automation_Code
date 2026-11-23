package tests.CovFiles;

import base.BaseTest;
import org.testng.annotations.Test;
import pages.CovFiles.CovFilesPage;

public class CovFilesUploadTest extends BaseTest {

    @Test
    public void testUploadFile() {

        CovFilesPage covFilesPage = new CovFilesPage();

        covFilesPage.openCovFileList();

        String filePath = System.getProperty("user.home")
                +   "\\Downloads\\Test_File_V45_v107_3.xlsx";

        covFilesPage.uploadFile(filePath);
    }
}