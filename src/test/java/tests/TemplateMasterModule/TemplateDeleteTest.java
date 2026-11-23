package tests.TemplateMasterModule;

import TemplateMasterModule.TemplateMasterPage;
import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TemplateDeleteTest extends BaseTest {

    @Test
    public void testDeleteFirstTemplateForDepartment() {
        TemplateMasterPage templateMasterPage = new TemplateMasterPage();
        templateMasterPage.openTemplateMaster();

        String department = System.getProperty("template.delete.department", "");
        String selectedTemplate = department.isBlank()
                ? templateMasterPage.loadFirstTemplateForAnyDepartment()
                : templateMasterPage.loadFirstTemplateForDepartment(department);

        Assert.assertFalse(selectedTemplate.isBlank(), "No template was loaded for deletion.");
        templateMasterPage.deleteLoadedTemplate();
    }
}
