import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class EditFields {

    private WebDriver driver;
    private WebDriverWait wait;

    // Locators - defined once, used later
    private static final By CONSENT_BUTTON     = By.xpath("//button[@aria-label='Consent']");
    private static final By HEADING            = By.tagName("h1");
    private static final By FULL_NAME          = By.id("fullName");
    private static final By APPEND_TEXT       = By.id("join");
    private static final By INSIDE_TEXTBOX = By.id("getMe");
    private static final By CLEAR_TEXT         = By.id("clearMe");
    private static final By DISABLED_FIELD     = By.id("noEdit");
    private static final By READONLY           = By.id("dontwrite");
    private static final By CONSENT_OVERLAY = By.className("fc-dialog-overlay");

    // ---------- Setup ----------

    @BeforeMethod  // runs before EVERY test: new browser, open page, handle popup
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://letcode.in/edit");
        acceptConsentIfShown();
    }
    public void headingIsInput() {
        WebElement heading = wait.until(ExpectedConditions.visibilityOfElementLocated(HEADING));
        String actualHeading = heading.getText();
        System.out.println("H1: " + actualHeading);

        Assert.assertEquals(actualHeading, "Input", "H1 heading should be 'Input'");
    }

    // ---------- Tests ----------


    @Test
    public void enterFullName() {
        WebElement fullName = wait.until(ExpectedConditions.elementToBeClickable(FULL_NAME));
        fullName.clear();
        fullName.click();
        fullName.sendKeys("Ivo Ganchev");

        Assert.assertEquals(fullName.getDomProperty("value"), "Ivo Ganchev",
                "Full name field should contain the typed text");
    }


    @Test
    public void insideTextbox() {
        WebElement insideTextbox = wait.until(ExpectedConditions.visibilityOfElementLocated(INSIDE_TEXTBOX));
        System.out.println("Inside the textbox is: '" + insideTextbox + "'.");

        Assert.assertEquals(insideTextbox.getDomProperty("value"), "ortonikc",
                "Asserting that the textbox reads 'ortonikc'");
    }

    @Test
    public void appendText() {
        WebElement appendTextField = wait.until(ExpectedConditions.elementToBeClickable(APPEND_TEXT));
        appendTextField.sendKeys(" at writing code.");

        Assert.assertEquals(appendTextField.getDomProperty("value"), "I am good at writing code.",
                "The text field should say that I am good at writing code.");
    }

    @Test
    public void clearField() {
        WebElement clearField = wait.until(ExpectedConditions.visibilityOfElementLocated(CLEAR_TEXT));
        clearField.clear();

        Assert.assertEquals(clearField.getDomProperty("value"), "",
                "The text field should be empty.");
    }


    @Test
    public void disabledField() {
        WebElement disabledField = wait.until(ExpectedConditions.visibilityOfElementLocated(DISABLED_FIELD));

        Assert.assertFalse(disabledField.isEnabled(),
                "The field should be disabled.");
    }

    @Test
    public void readOnlyField() {
        WebElement readOnlyField = wait.until(ExpectedConditions.visibilityOfElementLocated(READONLY));
        String originalValue = readOnlyField.getDomProperty("value");

        readOnlyField.sendKeys("Test");

        Assert.assertEquals(readOnlyField.getDomProperty("readOnly"), "true",
                "This text field is readonly");
    }


    // ---------- Helper methods ----------

    // Clicks "Consent" if the popup appears; if it doesn't appear within 15 s, carries on
    private void acceptConsentIfShown() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(15));
            shortWait.until(ExpectedConditions.elementToBeClickable(CONSENT_BUTTON)).click();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(CONSENT_OVERLAY));
            System.out.println("Consent accepted");
        } catch (TimeoutException e) {
            System.out.println("No consent popup this time – continuing");
        }
    }

    // ---------- Teardown ----------

    @AfterMethod(alwaysRun = true)  // runs after EVERY test: close this browser
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}