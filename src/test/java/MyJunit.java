import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MyJunit {
    WebDriver driver;
    @BeforeAll
    public void setup() throws IOException, ParseException {
        driver=new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        driver.get("http://localhost:3000");
        Cookies utils=new Cookies(driver);
        utils.setCookie();
    }
    @AfterAll
    public void closeBrowser(){
        driver.quit();
    }

    @Test
    @Order(1)
    @DisplayName("User login")
    public void login() throws IOException, ParseException {
        driver.get("http://localhost:3000");
        driver.findElement(By.id("email")).sendKeys("testuser1@test.com");
        driver.findElement(By.id("password")).sendKeys("1234");
        driver.findElement(By.cssSelector("[type=submit]")).click();
        Cookies utils=new Cookies(driver);
        utils.setCookie();
    }

    @Test
    @Order(2)
    @DisplayName("Edit user profile")
    public void editProfile() throws IOException, ParseException {
        driver.get("http://localhost:3000/dashboard/profile");
        driver.findElement(By.name("Edit")).click();
        driver.findElement(By.id("photoInput")).sendKeys(System.getProperty("user.dir")+"/src/test/resources/human.jpg");
        driver.findElement(By.className("btn-success")).click();
        String successMessage= driver.findElement(By.className("alert-success")).getText();
        System.out.println(successMessage);
    }
    @Test
    @Order(3)
    @DisplayName("Retrieve user data from table")
    public void getUsersData() throws InterruptedException {
        driver.get("http://localhost:3000/dashboard/users");
        //Thread.sleep(2000);
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(40));
        wait.until(d -> driver.findElement(By.tagName("h2")).isDisplayed());
        WebElement table = driver.findElement(By.tagName("tbody"));
        List<WebElement> allRows = table.findElements(By.tagName("tr"));
        int i = 0;
        for (WebElement row : allRows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            for (WebElement cell : cells) {
                i++;
                System.out.println("num[" + i + "] " + cell.getText());

            }
        }

    }
}
