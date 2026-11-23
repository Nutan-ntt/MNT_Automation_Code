package tests.TemplateMasterModule;

import TemplateMasterModule.TemplateMasterPage;
import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TemplatePDFPreviewTest extends BaseTest {

    @Test
    public void testGeneratePdfPreviewForLoadedTemplate() {
        TemplateMasterPage templateMasterPage = new TemplateMasterPage();
        templateMasterPage.openTemplateMaster();

        String department = System.getProperty("template.pdfPreview.department", "Import");
        String selectedTemplate = templateMasterPage.loadFirstTemplateForDepartment(department);
        int generatedPreviews = templateMasterPage.generatePdfPreviewsForLoadedTemplate();

        Assert.assertFalse(selectedTemplate.isBlank(), "No template was loaded for PDF preview.");
        Assert.assertTrue(generatedPreviews > 0, "No PDF preview was generated.");
    }
}
