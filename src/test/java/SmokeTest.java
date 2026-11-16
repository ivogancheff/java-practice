import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class SmokeTest {
    WebDriver driver;

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();   // opens Chrome
        driver.manage().window().maximize();
    }

    @Test
    public void pageOpens() {
        driver.get("https://letcode.in/test");
        System.out.println("Title: " + driver.getTitle());
        Assert.assertFalse(driver.getTitle().isEmpty(), "Page title should not be empty");
    }

    @AfterClass
    public void tearDown() {
        driver.quit();                 // closes the browser
    }
}