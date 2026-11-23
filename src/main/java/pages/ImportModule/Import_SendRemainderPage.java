package pages.ImportModule;

import base.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

public class Import_SendRemainderPage {

    private final WebDriverWait wait = new WebDriverWait(
            DriverFactory.getDriver(),
            Duration.ofSeconds(20)
    );

    private final By bookingPartnerCheckbox = By.xpath(
            "//td[@class='checkBoxCol']//input[@type='checkbox']"
    );
    private final By sendRemindersButton = By.xpath(
            "//button[normalize-space()='Send Reminders']"
    );
    private final By prepareTemplateHeading = By.xpath(
            "//h1[normalize-space()='Prepare Template']"
    );
    private final By sentEmailsHeading = By.xpath(
            "//h1[normalize-space()='Sent Emails']"
    );
    private final By templateDropdown = By.id("template");
    private final By loadTemplateButton = By.id("load");
    private final By convertEmailToPdf = By.xpath(
            "//div[@class='siglePdf pad20']//input[@id='isDualLanguagePdf']"
    );
    private final By pdfNameDropdown = By.cssSelector("form button.dropdown-toggle");
    private final By pdfNameOption = By.xpath(
            "//span[normalize-space()='Vessel Schedule Change']"
    );
    private final By bccEmail = By.id("bccEmail");
    private final By emailContentTop = By.xpath(
            "//body[1]/div[1]/div[1]/div[3]/div[1]/div[1]/div[1]/div[1]/div[1]" +
                    "/form[1]/div[1]/div[1]/table[1]/tbody[1]/tr[2]/td[1]/div[1]" +
                    "/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]"
    );
    private final By emailContentTopImageButton = By.xpath(
            "//tbody/tr[2]/td[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/button[9]"
    );
    private final By emailContentBottom = By.xpath(
            "//body[1]/div[1]/div[1]/div[3]/div[1]/div[1]/div[1]/div[1]/div[1]" +
                    "/form[1]/div[1]/div[1]/table[1]/tbody[1]/tr[4]/td[1]" +
                    "/div[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]"
    );
    private final By emailFooter = By.xpath(
            "//body[1]/div[1]/div[1]/div[3]/div[1]/div[1]/div[1]/div[1]/div[1]" +
                    "/form[1]/div[1]/div[1]/table[1]/tbody[1]/tr[6]/td[1]/div[1]" +
                    "/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]"
    );
    private final By sendEmailButton = By.id("save");

    public void selectBookingsAndSendReminders() {
        System.out.println("Selecting main booking partner checkbox");
        selectIfUnchecked(bookingPartnerCheckbox, "booking partner");

        System.out.println("Clicking Send Reminders");
        wait.until(ExpectedConditions.elementToBeClickable(sendRemindersButton)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(prepareTemplateHeading));
        System.out.println("Prepare Template page opened");
    }

    public void prepareTemplateAndSendEmail() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(prepareTemplateHeading));
        System.out.println("Loading the first available email template");
        selectFirstTemplateAndLoad();
        System.out.println("Enabling PDF conversion and selecting Vessel Schedule Change");
        enablePdfAndSelectName();
        System.out.println("Adding reminder recipient to BCC");
        addRecipientToBcc(System.getProperty(
                "import.reminder.recipient",
                "ext.nbarkade@cma-cgm.com"
        ));
        System.out.println("Uploading an image into the email top section");
        insertImage(emailContentTop);
        System.out.println("Clicking email content bottom section");
        replaceContent(
                emailContentBottom,
                System.getProperty(
                        "import.reminder.emailBottom",
                        "Please review the booking reminder details."
                )
        );
        pauseForReview("Bottom email content updated");
        System.out.println("Clicking email content footer section");
        replaceContent(
                emailFooter,
                System.getProperty(
                        "import.reminder.emailFooter",
                        "Regards,\nCMA CGM"
                )
        );
        pauseForReview("Email footer updated");
        pauseForReview("Reviewing email before sending");
        WebElement sendButton = wait.until(
                ExpectedConditions.elementToBeClickable(sendEmailButton)
        );
        ((JavascriptExecutor) DriverFactory.getDriver()).executeScript(
                "arguments[0].scrollIntoView({block:'center', inline:'center'});",
                sendButton
        );
        pauseForReview("Ready to click Save");
        System.out.println("Clicking Save using a browser interaction");
        sendButton.click();
        System.out.println("Save clicked; verifying navigation to Sent Emails");
        wait.withMessage(
                "Save was clicked, but the Sent Emails page did not open."
        ).until(driver ->
                ExpectedConditions.invisibilityOfElementLocated(prepareTemplateHeading)
                        .apply(driver)
                        && driver.findElements(sentEmailsHeading).stream()
                        .anyMatch(WebElement::isDisplayed)
        );
        System.out.println(
                "Sent Emails page opened; UI navigation succeeded (email delivery is not verified)"
        );
    }

    private void pauseForReview(String step) {
        int seconds = Integer.getInteger("import.reminder.reviewPauseSeconds", 5);
        System.out.println(step + "; review pause: " + seconds + " seconds");
        if (seconds <= 0) {
            return;
        }
        try {
            Thread.sleep(Duration.ofSeconds(seconds).toMillis());
        } catch (InterruptedException interrupted) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(
                    "Interrupted while pausing to review the reminder email.",
                    interrupted
            );
        }
    }

    private void selectFirstTemplateAndLoad() {
        WebDriverWait templateWait = new WebDriverWait(
                DriverFactory.getDriver(),
                Duration.ofSeconds(20)
        );
        templateWait.withMessage("No templates are available on Prepare Template.")
                .until(driver -> new Select(driver.findElement(templateDropdown))
                        .getOptions().stream()
                        .anyMatch(option -> {
                            String value = option.getAttribute("value");
                            String text = option.getText().trim();
                            return value != null && !value.isBlank()
                                    && !text.isBlank()
                                    && !text.toLowerCase().contains("select");
                        }));
        Select templates = new Select(wait.until(
                ExpectedConditions.elementToBeClickable(templateDropdown)
        ));
        int firstTemplateIndex = -1;
        for (int i = 0; i < templates.getOptions().size(); i++) {
            WebElement option = templates.getOptions().get(i);
            String value = option.getAttribute("value");
            String text = option.getText().trim();
            if (value != null && !value.isBlank()
                    && !text.isBlank()
                    && !text.toLowerCase().contains("select")) {
                firstTemplateIndex = i;
                break;
            }
        }
        if (firstTemplateIndex < 0) {
            throw new IllegalStateException("No selectable template was found.");
        }
        templates.selectByIndex(firstTemplateIndex);
        wait.until(ExpectedConditions.elementToBeClickable(loadTemplateButton)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(convertEmailToPdf));
    }

    private void enablePdfAndSelectName() {
        WebElement convert = wait.until(
                ExpectedConditions.presenceOfElementLocated(convertEmailToPdf)
        );
        if (!convert.isSelected()) {
            ((JavascriptExecutor) DriverFactory.getDriver()).executeScript(
                    "arguments[0].click();",
                    convert
            );
            wait.until(driver -> driver.findElement(convertEmailToPdf).isSelected());
        }

        WebElement dropdown = wait.until(
                ExpectedConditions.elementToBeClickable(pdfNameDropdown)
        );
        if (!dropdown.getText().contains("Vessel Schedule Change")) {
            dropdown.click();
            WebElement option = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(pdfNameOption)
            );
            option.click();
            wait.until(driver -> driver.findElement(pdfNameDropdown)
                    .getText().contains("Vessel Schedule Change"));
        }
    }

    private void addRecipientToBcc(String recipient) {
        if (recipient == null || recipient.isBlank()) {
            throw new IllegalArgumentException("The reminder recipient email is required.");
        }
        WebElement bccInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(bccEmail)
        );
        String currentValue = bccInput.getDomProperty("value");
        if (currentValue == null || java.util.Arrays.stream(currentValue.split(";"))
                .map(String::trim)
                .noneMatch(address -> address.equalsIgnoreCase(recipient))) {
            bccInput.sendKeys(
                    Keys.END,
                    (currentValue == null || currentValue.isBlank() ? "" : ";") + recipient
            );
        }
        wait.until(driver -> {
            String value = driver.findElement(bccEmail).getDomProperty("value");
            return value != null && java.util.Arrays.stream(value.split(";"))
                    .map(String::trim)
                    .anyMatch(address -> address.equalsIgnoreCase(recipient));
        });
    }

    private void insertImage(By editorLocator) {
        Path imagePath = getReminderImagePath();
        JavascriptExecutor javascript = (JavascriptExecutor) DriverFactory.getDriver();
        WebElement toolbarButton = wait.until(
                ExpectedConditions.presenceOfElementLocated(emailContentTopImageButton)
        );
        javascript.executeScript(
                "const button = arguments[0];" +
                        "const originalClick = HTMLInputElement.prototype.click;" +
                        "HTMLInputElement.prototype.click = function() {" +
                        "if (this.type === 'file') return;" +
                        "return originalClick.apply(this, arguments);" +
                        "};" +
                        "try { button.click(); }" +
                        "finally { HTMLInputElement.prototype.click = originalClick; }",
                toolbarButton
        );

        WebDriverWait uploadWait = new WebDriverWait(
                DriverFactory.getDriver(),
                Duration.ofSeconds(20)
        );
        WebElement uploadInput = uploadWait
                .withMessage("The editor image button did not provide an image file input.")
                .until(driver -> findImageUploadInput(driver.findElements(
                        By.cssSelector("input[type='file']")
                )));

        uploadInput.sendKeys(imagePath.toString());

        uploadWait
                .withMessage("The uploaded image was not added to the email top editor.")
                .until(driver -> {
                    List<WebElement> editors = driver.findElements(editorLocator);
                    return !editors.isEmpty() && Boolean.TRUE.equals(javascript.executeScript(
                            "const host = arguments[0];" +
                                    "const editor = host.isContentEditable ? host" +
                                    " : host.querySelector('[contenteditable=\"true\"]');" +
                                    "return !!editor && Array.from(editor.querySelectorAll('img'))" +
                                    ".some(image => image.complete && image.naturalWidth > 0);",
                            editors.get(0)
                    ));
                });
        System.out.println("Uploaded image into email editor: " + imagePath);
    }

    private WebElement findImageUploadInput(List<WebElement> inputs) {
        List<WebElement> imageInputs = inputs.stream()
                .filter(input -> {
                    String accept = input.getAttribute("accept");
                    return accept != null && accept.toLowerCase(Locale.ROOT).contains("image");
                })
                .toList();
        if (!imageInputs.isEmpty()) {
            return imageInputs.get(0);
        }
        return inputs.size() == 1 ? inputs.get(0) : null;
    }

    private Path getReminderImagePath() {
        String configuredPath = System.getProperty("import.reminder.imagePath");
        if (configuredPath != null && !configuredPath.isBlank()) {
            Path imagePath = Path.of(configuredPath).toAbsolutePath().normalize();
            if (!Files.isRegularFile(imagePath)) {
                throw new IllegalArgumentException(
                        "Reminder image file does not exist: " + imagePath
                );
            }
            return imagePath;
        }

        Path downloads = Path.of(System.getProperty("user.home"), "Downloads");
        if (!Files.isDirectory(downloads)) {
            throw new IllegalStateException("Downloads folder was not found: " + downloads);
        }
        try (Stream<Path> files = Files.list(downloads)) {
            return files
                    .filter(Files::isRegularFile)
                    .filter(this::isSupportedImage)
                    .max(Comparator.comparingLong(path -> path.toFile().lastModified()))
                    .orElseThrow(() -> new IllegalStateException(
                            "No supported image was found in " + downloads
                                    + ". Set -Dimport.reminder.imagePath to an image path."
                    ));
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Could not search the Downloads folder: " + downloads,
                    e
            );
        }
    }

    private boolean isSupportedImage(Path path) {
        String fileName = path.getFileName().toString().toLowerCase(Locale.ROOT);
        return fileName.endsWith(".png")
                || fileName.endsWith(".jpg")
                || fileName.endsWith(".jpeg")
                || fileName.endsWith(".gif")
                || fileName.endsWith(".webp");
    }

    private void replaceContent(By editorLocator, String content) {
        WebDriverWait editorWait = new WebDriverWait(
                DriverFactory.getDriver(),
                Duration.ofSeconds(20)
        );
        WebElement host = editorWait
                .withMessage("Could not find the requested email content editor: " + editorLocator)
                .until(ExpectedConditions.visibilityOfElementLocated(editorLocator));
        Object editorObject = ((JavascriptExecutor) DriverFactory.getDriver()).executeScript(
                "const host = arguments[0];" +
                        "const editor = host.isContentEditable ? host : " +
                        "host.querySelector('[contenteditable=\"true\"]');" +
                        "return editor || null;",
                host
        );
        if (!(editorObject instanceof WebElement editor)) {
            throw new IllegalStateException(
                    "Could not find an editable rich-text area in: " + editorLocator
            );
        }
        ((JavascriptExecutor) DriverFactory.getDriver()).executeScript(
                "arguments[0].scrollIntoView({block:'center', inline:'nearest'});",
                editor
        );
        editor.click();
        editor.sendKeys(Keys.CONTROL, "a");
        editor.sendKeys(content);
        JavascriptExecutor javascript = (JavascriptExecutor) DriverFactory.getDriver();
        boolean updated = editorWait.until(driver -> Boolean.TRUE.equals(javascript.executeScript(
                "const normalize = value => value.replace(/\\s+/g, ' ').trim();" +
                        "return normalize(arguments[0].innerText)" +
                        ".includes(normalize(arguments[1]));",
                editor,
                content
        )));
        if (!updated) {
            throw new IllegalStateException(
                    "Could not update email content in editor: " + editorLocator
            );
        }
    }

    private void selectIfUnchecked(By checkboxLocator, String description) {
        WebElement checkbox = wait.withMessage(
                "Could not find the " + description + " checkbox."
        ).until(driver -> driver.findElements(checkboxLocator).stream()
                .filter(WebElement::isDisplayed)
                .findFirst()
                .orElseGet(() -> driver.findElements(checkboxLocator).stream()
                        .findFirst()
                        .orElse(null)));

        if (!checkbox.isSelected()) {
            ((JavascriptExecutor) DriverFactory.getDriver()).executeScript(
                    "arguments[0].click();",
                    checkbox
            );
            wait.withMessage(
                    "The " + description + " checkbox did not become selected."
            ).until(driver -> driver.findElements(checkboxLocator).stream()
                    .anyMatch(WebElement::isSelected));
        }
    }
}
