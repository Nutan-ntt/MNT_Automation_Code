package tests.TemplateMasterModule;

import TemplateMasterModule.TemplateMasterPage;
import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TemplateLoadTest extends BaseTest {

    @Test
    public void testLoadFirstTemplateForImportExportAndCovDepartments() {
        TemplateMasterPage templateMasterPage = new TemplateMasterPage();
        templateMasterPage.openTemplateMaster();

        String importTemplate =
                templateMasterPage.loadFirstTemplateForDepartment("Import");
        Assert.assertFalse(
                importTemplate.isBlank(),
                "No Import template was selected."
        );

        String exportTemplate =
                templateMasterPage.loadFirstTemplateForDepartment("Export");
        Assert.assertFalse(
                exportTemplate.isBlank(),
                "No Export template was selected."
        );

        String covTemplate = templateMasterPage.loadFirstTemplateForDepartment("COV Export");
        Assert.assertFalse(covTemplate.isBlank(), "No COV template was selected.");
    }
}
