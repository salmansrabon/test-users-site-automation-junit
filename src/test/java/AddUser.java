import com.github.javafaker.Faker;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AddUser {
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
    //@AfterAll
    public void closeBrowser(){
        driver.quit();
    }
    @Test
    @Order(1)
    @DisplayName("Add new user")
    public void addNewUser() throws InterruptedException {
        driver.get("http://localhost:3000/dashboard/add-user");
        List<WebElement> textFields= driver.findElements(By.className("form-control"));
        Faker faker=new Faker();
        String firstName=faker.name().firstName();
        String lastName=faker.name().lastName();
        textFields.get(0).sendKeys(firstName);
        textFields.get(1).sendKeys(lastName);
        textFields.get(2).sendKeys((firstName+lastName+"@test.com").toLowerCase());
        textFields.get(3).sendKeys("0130"+Utils.generateRandomNumber(1000000,9999999)); //phone number
        List<WebElement> dropdowns= driver.findElements(By.className("form-select"));
        dropdowns.get(0).click();
        Actions actions=new Actions(driver);
        for(int i=0;i<2;i++){
            actions.moveToElement(dropdowns.get(0)).sendKeys(Keys.ARROW_DOWN).perform();
            Thread.sleep(500);
        }
        actions.moveToElement(dropdowns.get(0)).sendKeys(Keys.ENTER).perform();
        textFields.get(4).sendKeys("1234"); //password
        textFields.get(5).sendKeys(Keys.CONTROL+"a",Keys.BACK_SPACE);
        textFields.get(5).sendKeys("05/10/1995",Keys.ENTER);
        Select dropdown=new Select(dropdowns.get(1));
        dropdown.selectByVisibleText("Rajshahi");
        driver.findElement(By.id("genderMale")).click();
        driver.findElement(By.id("agreement")).click();
        driver.findElement(By.cssSelector("[type=submit]")).click();
    }
    @Test
    @Order(2)
    @DisplayName("Update user")
    public void editUser() throws InterruptedException {
        driver.get("http://localhost:3000/dashboard/users");
        driver.findElement(By.cssSelector("[placeholder=Search]")).sendKeys("Isadora");
        driver.findElement(By.cssSelector(".btn.btn-sm.btn-outline-primary.me-2")).click();
        driver.findElement(By.name("editButton")).click();
        driver.findElement(By.cssSelector("[type=file]")).sendKeys(System.getProperty("user.dir")+"/src/test/resources/human.jpg");
        Utils.scrollDown(driver);
        WebDriverWait wait =new WebDriverWait(driver,Duration.ofSeconds(40));
        wait.until(ExpectedConditions.elementToBeClickable(By.id("saveButton")));
        Thread.sleep(2000);
        driver.findElement(By.id("saveButton")).click();
    }
    @Test
    @Order(3)
    @DisplayName("Delete user")
    public void deleteUser(){
        driver.get("http://localhost:3000/dashboard/users");
        driver.findElements(By.cssSelector(".btn.btn-sm.btn-outline-danger")).get(0).click();
        driver.switchTo().alert().dismiss();
    }
}
