package tests.TemplateMasterModule;

import TemplateMasterModule.TemplateMasterPage;
import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TemplatePreviewTest extends BaseTest {

    @Test
    public void testPreviewFirstTemplateForDepartment() {
        TemplateMasterPage templateMasterPage = new TemplateMasterPage();
        templateMasterPage.openTemplateMaster();

        String department = System.getProperty("template.preview.department", "Import");
        String selectedTemplate = templateMasterPage.loadFirstTemplateForDepartment(department);
        templateMasterPage.openPreviewTab();

        Assert.assertFalse(selectedTemplate.isBlank(), "No template was loaded for preview.");
    }
}
