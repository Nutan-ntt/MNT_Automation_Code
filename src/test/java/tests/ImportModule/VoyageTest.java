package tests.ImportModule;

import base.BaseTest;
import org.testng.annotations.Test;
import pages.ImportModule.VoyagePage;

public class VoyageTest extends BaseTest {

    @Test
    public void searchFirstImportVoyage() {
        VoyagePage voyagePage = new VoyagePage();
        voyagePage.openImportModule();
        voyagePage.searchFirstVoyage(
                System.getProperty("import.voyage.query", "0")
        );
    }
}
