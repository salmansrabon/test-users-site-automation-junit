import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MyJunit {
    WebDriver driver;
    @BeforeAll
    public void setup() throws IOException, ParseException, InterruptedException {
        ChromeOptions chromeOptions=new ChromeOptions();
        chromeOptions.addArguments("--headed");

        driver=new ChromeDriver(chromeOptions);
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
    @Order(0)
    @DisplayName("Get title")
    public void getTitle(){
        String titleActual=driver.getTitle();
        String titleExpected="Practice Site";
        Assertions.assertTrue(titleActual.contains(titleExpected),"Title mismatched");
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
        utils.getCookie();
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
    @Test
    @Order(4)
    @DisplayName("Button click manipulation")
    public void buttonClick(){
        driver.get("http://localhost:3000/dashboard/practice-components");
        Utils.scrollDown(driver);
        Actions actions=new Actions(driver);
        actions.doubleClick(driver.findElements(By.tagName("button")).get(6)).click().perform();
        driver.switchTo().alert().accept();
        actions.contextClick(driver.findElements(By.tagName("button")).get(7)).click().perform();
        driver.switchTo().alert().accept();
    }
    @Test
    @Order(5)
    @DisplayName("NextJS datetime manipulation")
    public void dateTime(){
        driver.get("http://localhost:3000/dashboard/practice-components");
        WebElement dateTimeElem=driver.findElement(By.cssSelector("[type='datetime-local']"));
        dateTimeElem.click();
        dateTimeElem.sendKeys("24","Jan");
        dateTimeElem.sendKeys(Keys.ARROW_RIGHT,"2026");
        dateTimeElem.sendKeys(Keys.ARROW_RIGHT,"3","00","AM");

    }
    @Test
    @Order(6)
    @DisplayName("ReactJS readonly date manipulation")
    public void reactDateTimeReadonly(){
        driver.get("http://localhost:3000/dashboard/practice-components");
        WebElement dateInput= driver.findElements(By.className("form-control")).get(1);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].removeAttribute('readonly')", dateInput);
        // Set date value
        js.executeScript("arguments[0].value='11/24/2025'", dateInput);
    }
    @Test
    @Order(6)
    @DisplayName("ReactJS editable date manipulation")
    public void reactDateTimeEditable(){
        driver.get("http://localhost:3000/dashboard/practice-components");
        WebElement datePicker=driver.findElement(By.cssSelector("[placeholder=\"Select date\"]"));
        datePicker.sendKeys(Keys.CONTROL+"a",Keys.BACK_SPACE);
        datePicker.sendKeys("05/10/1995", Keys.ENTER);
    }
    @Test
    @Order(7)
    @DisplayName("Handle new tab")
    public void openNewTab(){
        driver.get("http://localhost:3000/dashboard/practice-components");
        driver.findElement(By.id("newTabBtn")).click();
        ArrayList<String> w = new ArrayList(driver.getWindowHandles());
        //switch to open tab
        driver.switchTo().window(w.get(1));
        String newTabTitle=driver.getTitle();
        Assertions.assertEquals(newTabTitle,"Example Domain");
        driver.close();
        driver.switchTo().window(w.get(0));
    }
    @Test
    @Order(8)
    @DisplayName("Handle new window")
    public void handleNewWindow(){
        driver.get("http://localhost:3000/dashboard/practice-components");
        driver.findElement(By.id(("newWindowBtn"))).click();
        String mainWindow = driver.getWindowHandle();
        Set<String> allWindows = driver.getWindowHandles();

        Assertions.assertTrue(allWindows.size() > 1, "New window did not open!");
        // Just find the child window
        for (String window : allWindows) {
            if (!window.equals(mainWindow)) {
                driver.switchTo().window(window);
                break;
            }
        }
        driver.switchTo().window(mainWindow);
    }
    @Test
    @Order(9)
    @DisplayName("Handle Modal")
    public void handleModal(){
        driver.get("http://localhost:3000/dashboard/practice-components");
        driver.findElement(By.id("openModalBtn")).click();
        driver.findElements(By.className("btn-primary")).get(1).click();
        String message= driver.findElement(By.cssSelector(".mt-2.text-success.fw-semibold")).getText();
        System.out.println(message);
    }
    @Test
    @Order(10)
    @DisplayName("Handle Iframe")
    public void handleIframe(){
        driver.get("http://localhost:3000/dashboard/practice-components");
        WebElement frame=driver.findElement(By.tagName("iframe"));
        driver.switchTo().frame(frame);
        //driver.switchTo().frame("embeddedExample");
        String text= driver.findElements(By.tagName("p")).get(0).getText();
        System.out.println(text);
        Assertions.assertTrue(text.contains("Login to access your dashboard"));
        driver.switchTo().defaultContent();
    }
}
