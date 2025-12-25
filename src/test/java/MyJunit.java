import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;
import java.time.Duration;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MyJunit {
    WebDriver driver;
    @BeforeAll
    public void setup(){
        driver=new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
    }
    @AfterAll
    public void closeBrowser(){
        driver.quit();
    }

    @Test
    @Order(1)
    @DisplayName("User login")
    public void login() throws IOException {
        driver.get("http://localhost:3000");
        driver.findElement(By.id("email")).sendKeys("testuser1@test.com");
        driver.findElement(By.id("password")).sendKeys("1234");
        driver.findElement(By.cssSelector("[type=submit]")).click();
        Cookies utils=new Cookies(driver);
        utils.getCookie();
    }

    @Test
    @Order(2)
    @DisplayName("Edit user profile")
    public void editProfile() throws IOException, ParseException {
        driver.get("http://localhost:3000");
        Cookies utils=new Cookies(driver);
        utils.setCookie();
        driver.get("http://localhost:3000/dashboard/profile");
        driver.findElement(By.name("Edit")).click();
        driver.findElement(By.id("photoInput")).sendKeys(System.getProperty("user.dir")+"/src/test/resources/human.jpg");
        driver.findElement(By.className("btn-success")).click();
        String successMessage= driver.findElement(By.className("alert-success")).getText();
        System.out.println(successMessage);
    }
}
