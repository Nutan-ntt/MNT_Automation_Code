package tests.TemplateMasterModule;

import TemplateMasterModule.TemplateMasterPage;
import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TemplateCreateTest extends BaseTest {

    @Test
    public void testCreateAndLoadTemplate() {
        TemplateMasterPage templateMasterPage = new TemplateMasterPage();
        templateMasterPage.openTemplateMaster();

        String defaultName = "Automation Template "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String templateName = System.getProperty("template.create.name", defaultName);
        String loadedTemplate = templateMasterPage.createTemplateAndLoad(
                System.getProperty("template.create.department", ""),
                templateName,
                System.getProperty("template.create.bccEmail", "test@gmail.com"),
                System.getProperty("template.create.pdfName", ""),
                System.getProperty(
                        "template.create.topContent",
                        "Template top create"
                ),
                System.getProperty(
                        "template.create.bottomContent",
                        "Template bottom create"
                ),
                System.getProperty(
                        "template.create.signature",
                        "Template Sign"
                )
        );

        Assert.assertEquals(
                loadedTemplate,
                templateName,
                "The newly created template was not loaded after refreshing."
        );
    }
}
