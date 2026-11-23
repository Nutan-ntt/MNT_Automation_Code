package tests.ImportModule;

import base.BaseTest;
import org.testng.annotations.Test;
import pages.ImportModule.Include_Disc_Trans_VoyagePage;

public class Include_Disc_Trans_VoyageTest extends BaseTest {

    @Test
    public void enableDischargeAndTranshipmentVoyages() {
        Include_Disc_Trans_VoyagePage importPage = new Include_Disc_Trans_VoyagePage();
        importPage.openImportAndEnableDischargeAndTranshipmentVoyages();
        importPage.searchFirstVoyage(
                System.getProperty("import.voyage.query", "0")
        );
    }
}
