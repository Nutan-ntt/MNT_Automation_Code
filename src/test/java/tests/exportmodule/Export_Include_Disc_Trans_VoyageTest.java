package tests.exportmodule;

import base.BaseTest;
import org.testng.annotations.Test;
import pages.exportmodule.Export_Include_Disc_Trans_VoyagePage;

public class Export_Include_Disc_Trans_VoyageTest extends BaseTest {

    @Test
    public void enableLoadingAndTranshipmentVoyages() {
        new Export_Include_Disc_Trans_VoyagePage()
                .enableLoadingAndTranshipmentVoyagesAndSearch();
    }
}
