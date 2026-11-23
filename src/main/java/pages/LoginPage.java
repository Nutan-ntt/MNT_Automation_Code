package pages;

import base.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URI;
import java.time.Duration;
import java.util.List;

public class LoginPage {

    private final By loginButton = By.id("submit");
    private final By emailInput = By.cssSelector(
            "input#i0116, input[name='loginfmt'], input[type='email']"
    );
    private final By passwordInput = By.cssSelector(
            "input#i0118, input[name='passwd'], input[type='password']"
    );
    private final By microsoftNextButton = By.id("idSIButton9");
    private final By staySignedInYesButton = By.xpath(
            "//input[@type='submit' and @value='Yes']" +
                    " | //button[normalize-space()='Yes']" +
                    " | //*[@id='idSIButton9' and @value='Yes']"
    );
    private final By covMainMenu = By.xpath(
            "//button[contains(@class,'bgnone') " +
                    "and contains(normalize-space(.),'COV Main')]"
    );

    public void loginIfRequired() {

        WebDriver driver = DriverFactory.getDriver();
        WebDriverWait wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        );
        String initialUrl = driver.getCurrentUrl();
        boolean loginButtonClicked = false;

        if (!isMicrosoftAuthenticationUrl(initialUrl)
                && !isVisible(emailInput) && !isVisible(passwordInput)) {
            try {
                new WebDriverWait(driver, Duration.ofSeconds(5)).until(
                        ExpectedConditions.elementToBeClickable(loginButton)
                ).click();
                loginButtonClicked = true;
                System.out.println("Login button clicked.");
            } catch (TimeoutException e) {
                // The browser may already be authenticated.
            }
        }

        if (!isVisible(emailInput) && !isVisible(passwordInput)
                && isMicrosoftAuthenticationUrl(driver.getCurrentUrl())) {
            waitForMicrosoftForm(wait);
        } else if (!isMicrosoftAuthenticationUrl(driver.getCurrentUrl())
                && !loginButtonClicked) {
            waitForApplicationReady(driver);
            System.out.println("Already logged in - skipping login.");
            return;
        }

        if (isVisible(emailInput)) {
            enterEmail(wait);
        }

        if (isVisible(passwordInput)) {
            enterPassword(wait, driver);
            return;
        }

        if (!isMicrosoftAuthenticationUrl(driver.getCurrentUrl())) {
            if (loginButtonClicked && driver.getCurrentUrl().equals(initialUrl)) {
                throw new IllegalStateException(
                        "Clicking the application login button did not open Microsoft sign-in."
                );
            }
            waitForApplicationReady(driver);
            System.out.println("Microsoft sign-in completed through SSO.");
            return;
        }

        throw new IllegalStateException(
                "Microsoft sign-in did not show a supported email or password prompt. " +
                        "Check whether the account requires a different sign-in method."
        );
    }

    private void enterEmail(WebDriverWait wait) {
        String username = requiredEnvironmentVariable("MNT_USERNAME");
        WebElement email = wait.until(
                ExpectedConditions.visibilityOfElementLocated(emailInput)
        );
        email.clear();
        email.sendKeys(username);
        wait.until(ExpectedConditions.elementToBeClickable(microsoftNextButton)).click();
        System.out.println("Microsoft email submitted.");

        try {
            wait.until(currentDriver ->
                    isVisible(passwordInput)
                            || !isMicrosoftAuthenticationUrl(currentDriver.getCurrentUrl())
            );
        } catch (TimeoutException e) {
            throw new TimeoutException(
                    "Microsoft did not display a password prompt after the email was submitted. " +
                            "The account may require a different sign-in method.",
                    e
            );
        }
    }

    private void enterPassword(WebDriverWait wait, WebDriver driver) {
        String password = requiredEnvironmentVariable("MNT_PASSWORD");
        WebElement passwordField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(passwordInput)
        );
        passwordField.clear();
        passwordField.sendKeys(password);
        wait.until(ExpectedConditions.elementToBeClickable(microsoftNextButton)).click();

        clickStaySignedInYesIfPrompted(driver);

        waitForApplicationReady(driver);
        System.out.println("Microsoft sign-in completed.");
    }

    private void clickStaySignedInYesIfPrompted(WebDriver driver) {
        try {
            WebElement yesButton = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.elementToBeClickable(staySignedInYesButton));
            yesButton.click();
            System.out.println("Selected 'Yes' on Microsoft's stay-signed-in prompt.");
        } catch (TimeoutException e) {
            // The prompt is optional and may be skipped by the organization's sign-in policy.
        }
    }

    private void waitForApplicationReady(WebDriver driver) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(90))
                    .until(currentDriver ->
                            !isMicrosoftAuthenticationUrl(currentDriver.getCurrentUrl())
                                    && isVisible(covMainMenu)
                    );
        } catch (TimeoutException e) {
            throw new TimeoutException(
                    "Microsoft sign-in did not return to the application, or the COV Main menu " +
                            "did not load. Complete any MFA prompt and verify the account has " +
                            "access to the application. Current page: " +
                            safePageLocation(driver.getCurrentUrl()) +
                            "; title: '" + driver.getTitle() +
                            "'; COV Main visible: " + isVisible(covMainMenu) + ".",
                    e
            );
        }
    }

    private String safePageLocation(String url) {
        try {
            URI uri = URI.create(url);
            return uri.getScheme() + "://" + uri.getAuthority() + uri.getPath();
        } catch (IllegalArgumentException e) {
            return "unavailable";
        }
    }

    private void waitForMicrosoftForm(WebDriverWait wait) {
        try {
            wait.until(currentDriver ->
                    isVisible(emailInput) || isVisible(passwordInput)
                            || !isMicrosoftAuthenticationUrl(currentDriver.getCurrentUrl())
            );
        } catch (TimeoutException e) {
            throw new TimeoutException(
                    "Microsoft sign-in page did not display an email or password field.", e
            );
        }
    }

    private boolean isVisible(By locator) {
        List<WebElement> elements = DriverFactory.getDriver().findElements(locator);
        for (WebElement element : elements) {
            try {
                if (element.isDisplayed()) {
                    return true;
                }
            } catch (StaleElementReferenceException e) {
                // Microsoft replaces sign-in fields between steps; retry on the next wait poll.
            }
        }
        return false;
    }

    private String requiredEnvironmentVariable(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Set the " + name + " environment variable in the test run configuration."
            );
        }
        return value;
    }

    private boolean isMicrosoftAuthenticationUrl(String url) {
        if (url == null) {
            return false;
        }
        String lowerCaseUrl = url.toLowerCase();
        return lowerCaseUrl.contains("login.microsoftonline.")
                || lowerCaseUrl.contains("login.microsoft.com")
                || lowerCaseUrl.contains("login.live.com")
                || lowerCaseUrl.contains("login.windows.net");
    }
}