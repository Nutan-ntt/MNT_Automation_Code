package pages.usermanagement;

import base.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class UserPermissionPage {

    private final WebDriverWait wait = new WebDriverWait(
            DriverFactory.getDriver(),
            Duration.ofSeconds(15)
    );

    // ---------------------------------------------------------
    // Modify permission React Select
    // ---------------------------------------------------------

    private final By modifyModuleInput = By.id(
            "react-select-2-input"
    );

    // ---------------------------------------------------------
    // View permission React Select
    // ---------------------------------------------------------

    private final By viewModuleInput = By.id(
            "react-select-3-input"
    );


    // ---------------------------------------------------------
    // Select MODIFY permission
    // ---------------------------------------------------------

    public void selectModifyModule(String moduleName) {

        WebElement moduleInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        modifyModuleInput
                )
        );

        scrollIntoView(moduleInput);

        JavascriptExecutor js =
                (JavascriptExecutor) DriverFactory.getDriver();

        js.executeScript(
                "arguments[0].focus();",
                moduleInput
        );

        moduleInput.sendKeys(moduleName);

        System.out.println(
                "Typed Modify module: " + moduleName
        );

        moduleInput.sendKeys(Keys.ENTER);

        System.out.println(
                "Modify module selected: " + moduleName
        );
    }


    // ---------------------------------------------------------
    // Select VIEW permission
    // ---------------------------------------------------------

    public void selectViewModule(String moduleName) {

        WebElement moduleInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        viewModuleInput
                )
        );

        scrollIntoView(moduleInput);

        JavascriptExecutor js =
                (JavascriptExecutor) DriverFactory.getDriver();

        js.executeScript(
                "arguments[0].focus();",
                moduleInput
        );

        moduleInput.sendKeys(moduleName);

        System.out.println(
                "Typed View module: " + moduleName
        );

        moduleInput.sendKeys(Keys.ENTER);

        System.out.println(
                "View module selected: " + moduleName
        );
    }


    // ---------------------------------------------------------
    // Verify MODIFY permission
    // ---------------------------------------------------------

    public boolean isModifyModuleDisplayed(String moduleName) {

        By modifyModule = By.xpath(
                "//*[contains(@class,'react-select__multi-value')]"
                        + "[.//*[normalize-space()='"
                        + moduleName
                        + "']]"
        );

        try {

            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            modifyModule
                    )
            );

            System.out.println(
                    "Modify permission verified: " +
                            moduleName
            );

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Modify permission not found: " +
                            moduleName
            );

            return false;
        }
    }


    // ---------------------------------------------------------
    // Verify VIEW permission
    // ---------------------------------------------------------

    public boolean isViewModuleDisplayed(String moduleName) {

        By viewModule = By.xpath(
                "//*[contains(@class,'react-select__multi-value')]"
                        + "[.//*[normalize-space()='"
                        + moduleName
                        + "']]"
        );

        try {

            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            viewModule
                    )
            );

            System.out.println(
                    "View permission verified: " +
                            moduleName
            );

            return true;

        } catch (Exception e) {

            System.out.println(
                    "View permission not found: " +
                            moduleName
            );

            return false;
        }
    }


    // ---------------------------------------------------------
    // Scroll element into view
    // ---------------------------------------------------------

    private void scrollIntoView(WebElement element) {

        ((JavascriptExecutor) DriverFactory.getDriver())
                .executeScript(
                        "arguments[0].scrollIntoView({block:'center', inline:'center'});",
                        element
                );
    }
}