package tests.exportmodule;

import base.BaseTest;
import org.testng.annotations.Test;
import pages.exportmodule.voyagecheck;

public class voyagetest extends BaseTest {

    @Test
    public void openExportAndSelectVN() {

        voyagecheck export = new voyagecheck();

        export.openExportModule();
        export.changeCountryToVN();

        export.searchVoyage("0B71PN1MA");

        export.selectResultAndSendReminder();
    }
}