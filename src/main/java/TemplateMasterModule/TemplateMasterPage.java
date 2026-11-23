package TemplateMasterModule;

import base.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class TemplateMasterPage {

    private final WebDriverWait wait = new WebDriverWait(
            DriverFactory.getDriver(),
            Duration.ofSeconds(30)
    );

    private final By covMainMenu = By.xpath(
            "//button[contains(@class,'bgnone') and contains(normalize-space(.),'COV Main')]"
    );
    private final By templateMasterLink = By.xpath(" //a[@data-testid='dExport'][normalize-space()='Templates']"
    );
    private final By departmentDropdown = By.id("dept_type");
    private final By templateDropdown = By.id("template");
    private final By loadButton = By.id("load");
    private final By deleteButton = By.id("delete");
    private final By templateName = By.id("template_name");
    private final By templateTab = By.id("tabs-tab-template");
    private final By previewTab = By.id("tabs-tab-preview");

    private final By bccEmail = By.id("bccEmail");
    private final By sendPdf = By.id("sendPDF");
    private final By singlePdfDualLanguage = By.xpath(
            "//div[contains(@class,'siglePdf') and contains(@class,'pad20')]" +
                    "//input[@id='isDualLanguagePdf']"
    );
    private final By pdfNameDropdown = By.cssSelector("form button.dropdown-toggle");
    private final By visiblePdfNameMenu = By.xpath(
            "//div[contains(concat(' ', normalize-space(@class), ' '), ' dropdown-menu ')" +
                    " and contains(concat(' ', normalize-space(@class), ' '), ' show ')]"
    );
    private final By splitPdf = By.id("isSplitpdf");
    private final By multiplePdfDualLanguage = By.xpath(
            "//div[contains(@class,'multiPdf') and contains(@class,'pad20')]" +
                    "//input[@id='isDualLanguagePdf']"
    );
    private final By dualLanguageWarningTitle = By.xpath("//div[@class='modal-title h4']");
    private final By continueButton = By.xpath("//button[normalize-space()='Continue']");
    private final By validationOkButton = By.xpath("//button[normalize-space()='ok']");
    private final By emailContentTab = By.cssSelector("button[id$='-tab-emCnt']");
    private final By localLanguageTab = By.cssSelector("button[id$='-tab-local']");
    private final By englishTab = By.cssSelector("button[id$='-tab-en']");
    private final By selectAllEmailFields = By.xpath("//th[1]//div[1]//input[1]");
    private final By bookingPartyReferenceHeader = By.xpath("//th[@title='Bkg Pty Ref']");
    private final By loadEmailField = By.xpath("//th[@title='Load']//input[@type='checkbox']");
    private final By localTemplateTop = By.xpath(
            "//body[1]/div[1]/div[1]/div[3]/div[1]/div[1]/div[1]/div[1]/div[1]/form[1]" +
                    "/div[1]/div[2]/table[1]/tbody[1]/tr[2]/td[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]"
    );
    private final By localTemplateBottom = By.xpath(
            "//body[1]/div[1]/div[1]/div[3]/div[1]/div[1]/div[1]/div[1]/div[1]/form[1]" +
                    "/div[1]/div[2]/table[1]/tbody[1]/tr[4]/td[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]"
    );
    private final By localTemplateSignature = By.xpath(
            "//body[1]/div[1]/div[1]/div[3]/div[1]/div[1]/div[1]/div[1]/div[1]/form[1]" +
                    "/div[1]/div[2]/table[1]/tbody[1]/tr[6]/td[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]"
    );
    private final By englishTemplateTop = By.xpath(
            "//body[1]/div[1]/div[1]/div[3]/div[1]/div[1]/div[1]/div[1]/div[1]/form[1]" +
                    "/div[1]/div[3]/table[1]/tbody[1]/tr[2]/td[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]"
    );
    private final By englishTemplateBottom = By.xpath(
            "//body[1]/div[1]/div[1]/div[3]/div[1]/div[1]/div[1]/div[1]/div[1]/form[1]" +
                    "/div[1]/div[3]/table[1]/tbody[1]/tr[4]/td[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]"
    );
    private final By saveButton = By.id("save");
    private final By pdfPreviewTab = By.id("tabs-tab-pdfPreview");
    private final By generatePdfPreviewButton = By.xpath(
            "//button[normalize-space()='Generate PDF Preview']"
    );
    private final By localPdfPreviewTab = By.cssSelector(
            "button[id$='-tab-localPdfPreview']"
    );
    private final By englishPdfPreviewTab = By.cssSelector(
            "button[id$='-tab-enPdfPreview']"
    );

    public void openTemplateMaster() {
        if (!DriverFactory.getDriver().findElements(departmentDropdown).isEmpty()
                && DriverFactory.getDriver().findElement(departmentDropdown).isDisplayed()) {
            return;
        }

        WebElement menu = wait.until(
                ExpectedConditions.elementToBeClickable(covMainMenu)
        );
        menu.click();

        WebElement templateLink = wait.until(
                ExpectedConditions.elementToBeClickable(templateMasterLink)
        );
        templateLink.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(departmentDropdown));
        wait.until(ExpectedConditions.visibilityOfElementLocated(templateDropdown));
    }

    public String loadFirstTemplateForDepartment(String department) {
        WebElement departmentElement = wait.until(
                ExpectedConditions.elementToBeClickable(departmentDropdown)
        );
        new Select(departmentElement).selectByVisibleText(department);
        pauseForObservation("Selected department: " + department);

        new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(30))
                .withMessage("No selectable templates loaded for department: " + department)
                .until(driver -> !getTemplateOptionsSignature(driver).equals("[]"));

        String selectedTemplate = wait.until(this::selectFirstAvailableTemplate);
        pauseForObservation("Selected template: " + selectedTemplate);
        wait.until(ExpectedConditions.elementToBeClickable(loadButton)).click();
        wait.until(this::hasPopulatedTemplateData);
        pauseForObservation("Loaded template: " + selectedTemplate);
        return selectedTemplate;
    }

    public String loadFirstTemplateForAnyDepartment() {
        String department = wait.until(driver -> {
            Object result = ((JavascriptExecutor) driver).executeScript(
                    "const select = document.querySelector('#dept_type');" +
                            "if (!select) return null;" +
                            "const option = Array.from(select.options).find(item =>" +
                            " item.value.trim() && item.text.trim()" +
                            " && !item.text.toLowerCase().includes('select'));" +
                            "return option ? option.text.trim() : null;"
            );
            return result instanceof String value && !value.isBlank() ? value : null;
        });
        return loadFirstTemplateForDepartment(department);
    }

    public void deleteLoadedTemplate() {
        clickWhenUnobscured(deleteButton);
        pauseForObservation("Clicked Delete for the loaded template");
    }

    public void openPreviewTab() {
        clickWhenUnobscured(previewTab);
        wait.until(ExpectedConditions.attributeToBe(previewTab, "aria-selected", "true"));
        pauseForObservation("Opened template Preview tab");
    }

    public int generatePdfPreviewsForLoadedTemplate() {
        boolean generateAndAttachPdf = wait.until(
                ExpectedConditions.visibilityOfElementLocated(sendPdf)
        ).isSelected();
        if (!generateAndAttachPdf) {
            setCheckbox(sendPdf, true);
            setCheckbox(singlePdfDualLanguage, true);
            openPdfPreviewTab();
            generatePdfPreview("PDF");

            clickWhenUnobscured(templateTab);
            wait.until(ExpectedConditions.attributeToBe(templateTab, "aria-selected", "true"));
            pauseForObservation("Returned to template settings");
            setCheckbox(multiplePdfDualLanguage, true);
            wait.until(driver -> driver.findElement(multiplePdfDualLanguage).isSelected());
            pauseForObservation("Enabled separate Local Language and English PDFs");
            populateDualLanguagePdfSectionsIfEmpty();

            openPdfPreviewTab();
            generatePdfPreviewForLanguage(localPdfPreviewTab, "Local language");
            generatePdfPreviewForLanguage(englishPdfPreviewTab, "English");
            return 3;
        }

        boolean convertContentToPdf = wait.until(
                ExpectedConditions.visibilityOfElementLocated(singlePdfDualLanguage)
        ).isSelected();
        boolean separateLanguagePdfs = wait.until(
                ExpectedConditions.visibilityOfElementLocated(multiplePdfDualLanguage)
        ).isSelected();
        if (separateLanguagePdfs) {
            populateDualLanguagePdfSectionsIfEmpty();
        }

        openPdfPreviewTab();

        int generatedPreviews = 0;
        if (separateLanguagePdfs) {
            generatePdfPreviewForLanguage(localPdfPreviewTab, "Local language");
            generatedPreviews++;
            generatePdfPreviewForLanguage(englishPdfPreviewTab, "English");
            generatedPreviews++;
        } else if (convertContentToPdf) {
            generatePdfPreview("PDF");
            generatedPreviews++;
        } else {
            throw new IllegalStateException(
                    "PDF Preview is unavailable because neither PDF option is enabled."
            );
        }
        return generatedPreviews;
    }

    private void openPdfPreviewTab() {
        WebElement tab = wait.until(ExpectedConditions.elementToBeClickable(pdfPreviewTab));
        if (!"true".equalsIgnoreCase(tab.getAttribute("aria-selected"))) {
            clickWhenUnobscured(tab);
        }
        wait.until(ExpectedConditions.attributeToBe(pdfPreviewTab, "aria-selected", "true"));
        pauseForObservation("Opened PDF Preview tab");
    }

    private void generatePdfPreviewForLanguage(By languageTab, String language) {
        clickWhenUnobscured(languageTab);
        wait.until(ExpectedConditions.attributeToBe(languageTab, "aria-selected", "true"));
        pauseForObservation("Opened " + language + " PDF Preview");
        generatePdfPreview(language);
    }

    private void generatePdfPreview(String description) {
        WebElement generateButton = wait.until(
                ExpectedConditions.elementToBeClickable(generatePdfPreviewButton)
        );
        clickWhenUnobscured(generateButton);
        pauseForObservation("Generated " + description + " PDF preview");
    }

    private void populateDualLanguagePdfSectionsIfEmpty() {
        selectTab(localLanguageTab);
        populateEditableContentIfEmpty(
                localTemplateTop,
                System.getProperty("template.pdfPreview.localTop", "Local language PDF preview")
        );
        populateEditableContentIfEmpty(
                localTemplateBottom,
                System.getProperty("template.pdfPreview.localBottom", "Local language PDF preview")
        );
        populateEditableContentIfEmpty(
                localTemplateSignature,
                System.getProperty("template.pdfPreview.localSignature", "Local language PDF preview")
        );

        selectTab(englishTab);
        populateEditableContentIfEmpty(
                englishTemplateTop,
                System.getProperty("template.pdfPreview.englishTop", "English PDF preview")
        );
        populateEditableContentIfEmpty(
                englishTemplateBottom,
                System.getProperty("template.pdfPreview.englishBottom", "English PDF preview")
        );
    }

    private void populateEditableContentIfEmpty(By locator, String content) {
        WebElement editor = new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(30))
                .withMessage("Template editor was not visible for locator: " + locator)
                .until(driver -> driver.findElements(locator).stream()
                        .filter(WebElement::isDisplayed)
                        .findFirst()
                        .orElse(null));
        Object existingContent = ((JavascriptExecutor) DriverFactory.getDriver()).executeScript(
                "return arguments[0].value || arguments[0].innerText || arguments[0].textContent || '';",
                editor
        );
        if (existingContent == null || existingContent.toString().isBlank()) {
            replaceEditableContent(editor, content);
        }
    }

    public String createTemplateAndLoad(
            String department,
            String name,
            String bccEmailValue,
            String pdfName,
            String topContent,
            String bottomContent,
            String signatureContent
    ) {
        String selectedDepartment = selectDepartment(department);
        pauseForObservation("Selected department: " + selectedDepartment);
        WebElement nameInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(templateName)
        );
        nameInput.clear();
        nameInput.sendKeys(name);
        pauseForObservation("Entered template name: " + name);

        setCheckboxOrText(bccEmail, bccEmailValue);
        setCheckbox(sendPdf, true);
        setCheckbox(singlePdfDualLanguage, true);
        setCheckbox(splitPdf, false);
        boolean convertContentToPdf = wait.until(
                ExpectedConditions.visibilityOfElementLocated(singlePdfDualLanguage)
        ).isSelected();
        boolean onePdfPerBooking = wait.until(
                ExpectedConditions.visibilityOfElementLocated(splitPdf)
        ).isSelected();
        pauseForObservation("Configured BCC and PDF options");
        if (convertContentToPdf && !onePdfPerBooking) {
            if (pdfName == null || pdfName.isBlank()) {
                selectFirstPdfName();
            } else {
                selectPdfName(pdfName);
            }
            pauseForObservation("Selected PDF file name");
        }

        selectTab(emailContentTab);
        pauseForObservation("Opened Email Content tab");
        setCheckbox(selectAllEmailFields, true);
        wait.until(ExpectedConditions.visibilityOfElementLocated(bookingPartyReferenceHeader));
        setCheckbox(loadEmailField, true);
        replaceTabEditableContent(emailContentTab, 2, topContent);
        replaceTabEditableContent(emailContentTab, 4, bottomContent);
        replaceTabEditableContent(emailContentTab, 6, signatureContent);
        pauseForObservation("Updated Email Content top, bottom, and signature");
        clickWhenUnobscured(saveButton);
        pauseForObservation("Saved template: " + name);

        DriverFactory.getDriver().navigate().refresh();
        wait.until(ExpectedConditions.visibilityOfElementLocated(departmentDropdown));
        wait.until(ExpectedConditions.visibilityOfElementLocated(templateDropdown));
        pauseForObservation("Refreshed template page");
        new Select(wait.until(ExpectedConditions.elementToBeClickable(departmentDropdown)))
                .selectByVisibleText(selectedDepartment);

        wait.withMessage(
                "The newly created template was not listed after refresh: " + name
        ).until(driver -> hasTemplateOption(driver, name));
        new Select(wait.until(ExpectedConditions.elementToBeClickable(templateDropdown)))
                .selectByVisibleText(name);
        pauseForObservation("Selected newly created template: " + name);
        clickWhenUnobscured(loadButton);
        wait.withMessage("The newly created template did not load: " + name)
                .until(this::hasPopulatedTemplateData);
        pauseForObservation("Loaded newly created template: " + name);
        return name;
    }

    private String selectDepartment(String requestedDepartment) {
        WebElement departmentSelect = wait.until(
                ExpectedConditions.elementToBeClickable(departmentDropdown)
        );
        if (requestedDepartment != null && !requestedDepartment.isBlank()) {
            new Select(departmentSelect).selectByVisibleText(requestedDepartment);
            return requestedDepartment;
        }

        String department = wait.until(driver -> {
            Object result = ((JavascriptExecutor) driver).executeScript(
                    "const select = document.querySelector('#dept_type');" +
                            "if (!select) return null;" +
                            "const option = Array.from(select.options).find(item =>" +
                            " item.value.trim() && item.text.trim()" +
                            " && !item.text.toLowerCase().includes('select'));" +
                            "return option ? option.text.trim() : null;"
            );
            return result instanceof String value && !value.isBlank() ? value : null;
        });
        new Select(departmentSelect).selectByVisibleText(department);
        return department;
    }

    private boolean hasTemplateOption(WebDriver driver, String name) {
        return new Select(driver.findElement(templateDropdown)).getOptions().stream()
                .anyMatch(option -> name.equals(option.getText().trim()));
    }

    public void editAndSaveLoadedTemplate(
            String bccEmailValue,
            String topContent,
            String bottomContent,
            String pdfName,
            String localTop,
            String localBottom,
            String localSignature,
            String englishTop,
            String englishBottom
    ) {
        setCheckboxOrText(bccEmail, bccEmailValue);
        setCheckbox(sendPdf, true);
        setCheckbox(singlePdfDualLanguage, true);
        boolean convertContentToPdf = wait.until(
                ExpectedConditions.visibilityOfElementLocated(singlePdfDualLanguage)
        ).isSelected();
        if (convertContentToPdf && pdfName != null && !pdfName.isBlank()) {
            selectPdfName(pdfName);
        }
        setCheckbox(splitPdf, true);
        setCheckbox(multiplePdfDualLanguage, true);
        boolean onePdfPerBooking = wait.until(
                ExpectedConditions.visibilityOfElementLocated(splitPdf)
        ).isSelected();
        boolean separateLanguagePdfs = wait.until(
                ExpectedConditions.visibilityOfElementLocated(multiplePdfDualLanguage)
        ).isSelected();

        if (separateLanguagePdfs) {
            selectTab(localLanguageTab);
            replaceEditableContent(localTemplateTop, localTop);
            replaceEditableContent(localTemplateBottom, localBottom);
            replaceEditableContent(localTemplateSignature, localSignature);

            selectTab(englishTab);
            replaceEditableContent(englishTemplateTop, englishTop);
            replaceEditableContent(englishTemplateBottom, englishBottom);
        }

        selectTab(emailContentTab);
        setCheckbox(selectAllEmailFields, true);
        wait.until(ExpectedConditions.visibilityOfElementLocated(bookingPartyReferenceHeader));
        setCheckbox(loadEmailField, true);
        replaceTabEditableContent(emailContentTab, 2, topContent);
        replaceTabEditableContent(emailContentTab, 4, bottomContent);

        clickWhenUnobscured(saveButton);
        if (separateLanguagePdfs) {
            continueThroughDualLanguageWarning();
        }
        boolean pdfNameRequired = convertContentToPdf
                && !onePdfPerBooking
                && !separateLanguagePdfs
                && (pdfName == null || pdfName.isBlank());
        if (pdfNameRequired) {
            wait.withMessage(
                    "The application did not show a PDF name validation message after Save."
            ).until(this::getPdfNameValidationMessage);
        }
    }

    private void setCheckboxOrText(By locator, String value) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        if (!"checkbox".equalsIgnoreCase(element.getAttribute("type"))) {
            element.clear();
            element.sendKeys(value);
            return;
        }
        setCheckbox(locator, true);
    }

    private void setCheckbox(By locator, boolean checked) {
        WebElement control = wait.until(ExpectedConditions.elementToBeClickable(locator));
        if (control.isSelected() != checked) {
            if ("radio".equalsIgnoreCase(control.getAttribute("type"))) {
                ((JavascriptExecutor) DriverFactory.getDriver()).executeScript(
                        "arguments[0].click();",
                        control
                );
                wait.until(driver -> driver.findElement(locator).isSelected() == checked);
            } else {
                control.click();
            }
        }
    }

    private void selectPdfName(String pdfName) {
        openPdfNameMenu();
        WebElement option = wait.until(driver -> driver.findElements(visiblePdfNameMenu)
                .stream()
                .filter(WebElement::isDisplayed)
                .flatMap(menu -> menu.findElements(By.cssSelector(
                        ".dropdown-item, [role='option'], [role='menuitem'], a, button"
                )).stream())
                .filter(WebElement::isDisplayed)
                .filter(item -> item.getText().trim().equals(pdfName))
                .findFirst()
                .orElse(null));
        clickWhenUnobscured(option);
    }

    private void selectFirstPdfName() {
        openPdfNameMenu();
        WebElement firstOption = wait.until(driver -> driver.findElements(visiblePdfNameMenu)
                .stream()
                .filter(WebElement::isDisplayed)
                .flatMap(menu -> menu.findElements(By.cssSelector(
                        ".dropdown-item, [role='option'], [role='menuitem'], a, button"
                )).stream())
                .filter(WebElement::isDisplayed)
                .filter(item -> !item.getText().isBlank())
                .findFirst()
                .orElse(null));
        clickWhenUnobscured(firstOption);
    }

    private void openPdfNameMenu() {
        wait.until(ExpectedConditions.elementToBeClickable(pdfNameDropdown)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(visiblePdfNameMenu));
    }

    private String getPdfNameValidationMessage(WebDriver driver) {
        Object result = ((JavascriptExecutor) driver).executeScript(
                "const section = document.querySelector('div.siglePdf.pad20');" +
                        "if (!section) return null;" +
                        "const messages = Array.from(section.querySelectorAll(" +
                        "'[role=\"alert\"], .invalid-feedback, .text-danger, [class*=\"error\"]'))" +
                        ".filter(item => item.offsetParent !== null)" +
                        ".map(item => item.innerText.trim()).filter(Boolean);" +
                        "const text = (section.innerText + ' ' + messages.join(' ')).trim();" +
                        "return /pdf/i.test(text) && /(required|select|name)/i.test(text)" +
                        " ? (messages.join(' ') || text) : null;"
        );
        return result instanceof String message && !message.isBlank() ? message : null;
    }

    private void continueThroughDualLanguageWarning() {
        try {
            new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(3))
                    .until(ExpectedConditions.visibilityOfElementLocated(dualLanguageWarningTitle));
        } catch (TimeoutException noWarningDisplayed) {
            return;
        }

        clickWhenUnobscured(continueButton);
        try {
            new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(3))
                    .until(ExpectedConditions.visibilityOfElementLocated(validationOkButton));
        } catch (TimeoutException noPdfValidationDisplayed) {
            return;
        }
        clickWhenUnobscured(validationOkButton);
    }

    private void replaceEditableContent(By locator, String content) {
        WebElement editable = new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(30))
                .withMessage("Template editor was not visible for locator: " + locator)
                .until(driver -> driver.findElements(locator).stream()
                        .filter(WebElement::isDisplayed)
                        .findFirst()
                        .orElse(null));
        clickWhenUnobscured(editable);
        editable.sendKeys(Keys.CONTROL, "a");
        editable.sendKeys(content);
    }

    private void replaceTabEditableContent(By tabLocator, int rowNumber, String content) {
        WebElement tab = wait.until(ExpectedConditions.visibilityOfElementLocated(tabLocator));
        String panelId = tab.getAttribute("aria-controls");
        if (panelId == null || panelId.isBlank()) {
            throw new IllegalStateException(
                    "The selected tab does not identify its content panel: " + tabLocator
            );
        }

        WebElement panel = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id(panelId))
        );
        By editorLocator = By.xpath(
                ".//tbody/tr[" + rowNumber + "]/td[1]/div[1]/div[1]/div[1]/div[1]" +
                        "/div[1]/div[2]/div[1]/div[1]"
        );
        WebElement editor = new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(30))
                .withMessage(
                        "Email content editor was not visible in active tab panel for row "
                                + rowNumber
                )
                .until(driver -> panel.findElements(editorLocator).stream()
                        .filter(WebElement::isDisplayed)
                        .findFirst()
                        .orElse(null));
        replaceEditableContent(editor, content);
    }

    private void selectTab(By tabLocator) {
        WebElement tab = wait.until(ExpectedConditions.elementToBeClickable(tabLocator));
        if (!"true".equalsIgnoreCase(tab.getAttribute("aria-selected"))) {
            clickWhenUnobscured(tab);
        }
        wait.until(driver -> "true".equalsIgnoreCase(
                driver.findElement(tabLocator).getAttribute("aria-selected")
        ));
    }

    private void clickWhenUnobscured(By locator) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        clickWhenUnobscured(element);
    }

    private void replaceEditableContent(WebElement editable, String content) {
        clickWhenUnobscured(editable);
        editable.sendKeys(Keys.CONTROL, "a");
        editable.sendKeys(content);
    }

    private void clickWhenUnobscured(WebElement element) {
        WebDriver driver = DriverFactory.getDriver();
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});",
                element
        );
        wait.until(ExpectedConditions.elementToBeClickable(element));
        wait.until(currentDriver -> Boolean.TRUE.equals(
                ((JavascriptExecutor) currentDriver).executeScript(
                        "const element = arguments[0];" +
                                "const rect = element.getBoundingClientRect();" +
                                "const hit = document.elementFromPoint(" +
                                "rect.left + rect.width / 2, rect.top + rect.height / 2);" +
                                "return !!hit && (hit === element || element.contains(hit));",
                        element
                )
        ));
        element.click();
    }

    private void pauseForObservation(String step) {
        System.out.println(step);
        try {
            Thread.sleep(Duration.ofSeconds(3).toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(
                    "Interrupted while pausing to observe template loading.", e
            );
        }
    }

    private String getTemplateOptionsSignature(WebDriver driver) {
        Object result = ((JavascriptExecutor) driver).executeScript(
                "const select = document.querySelector('#template');" +
                        "if (!select) return '[]';" +
                        "return JSON.stringify(Array.from(select.options)" +
                        ".filter(option => option.value.trim() && option.text.trim()" +
                        " && !option.text.toLowerCase().includes('select'))" +
                        ".map(option => [option.value, option.text.trim()]));"
        );
        return result == null ? "[]" : result.toString();
    }

    private String selectFirstAvailableTemplate(WebDriver driver) {
        Object result = ((JavascriptExecutor) driver).executeScript(
                "const select = document.querySelector('#template');" +
                        "if (!select) return null;" +
                        "const option = Array.from(select.options).find(item =>" +
                        " item.value.trim() && item.text.trim()" +
                        " && !item.text.toLowerCase().includes('select'));" +
                        "if (!option) return null;" +
                        "select.value = option.value;" +
                        "select.dispatchEvent(new Event('input', { bubbles: true }));" +
                        "select.dispatchEvent(new Event('change', { bubbles: true }));" +
                        "return option.text.trim();"
        );
        return result instanceof String templateName && !templateName.isBlank()
                ? templateName
                : null;
    }

    private boolean hasPopulatedTemplateData(WebDriver driver) {
        String script = """
                return Array.from(document.querySelectorAll(
                    'input:not([type="hidden"]):not([type="button"]):not([type="submit"]), textarea'
                )).some(element =>
                    element.id !== 'dept_type' &&
                    element.id !== 'template' &&
                    element.offsetParent !== null &&
                    element.value.trim().length > 0
                ) || Array.from(document.querySelectorAll('tbody tr')).some(row =>
                    row.offsetParent !== null &&
                    Array.from(row.querySelectorAll('td'))
                        .some(cell => cell.innerText.trim().length > 0)
                );
                """;
        return Boolean.TRUE.equals(
                ((JavascriptExecutor) driver).executeScript(script)
        );
    }
}
