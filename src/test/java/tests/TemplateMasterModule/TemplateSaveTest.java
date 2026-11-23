package tests.TemplateMasterModule;

import TemplateMasterModule.TemplateMasterPage;
import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TemplateSaveTest extends BaseTest {

    @Test
    public void testUpdateAndSaveFirstTemplate() {
        TemplateMasterPage templateMasterPage = new TemplateMasterPage();
        templateMasterPage.openTemplateMaster();
        String selectedTemplate = templateMasterPage.loadFirstTemplateForAnyDepartment();

        String pdfName = System.getProperty("template.save.pdfName", "");
        templateMasterPage.editAndSaveLoadedTemplate(
                System.getProperty("template.save.bccEmail", "test@gmail.com"),
                System.getProperty(
                        "template.save.topContent",
                        "Template save automation verification"
                ),
                System.getProperty(
                        "template.save.bottomContent",
                        "Template save automation verification"
                ),
                pdfName,
                System.getProperty("template.save.localTop", "Local template top "),
                System.getProperty("template.save.localBottom", "Local template bottom"),
                System.getProperty("template.save.localSignature", "Local template signature"),
                System.getProperty("template.save.englishTop", "English template top"),
                System.getProperty("template.save.englishBottom", "English template bottom")
        );

        Assert.assertFalse(selectedTemplate.isBlank(), "No template was loaded before saving.");
    }
}
