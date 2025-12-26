import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Cookies {
    WebDriver driver;

    public Cookies(WebDriver driver) {
        this.driver = driver;
    }

    public void getCookie() throws IOException {
        JSONObject jsonObject = new JSONObject();
        for (Cookie c : driver.manage().getCookies()) {
            jsonObject.put(c.getName(), c.getValue());
            System.out.println(jsonObject);
        }
        FileWriter writer = new FileWriter(System.getProperty("user.dir") + "/src/test/resources/cookies.json");
        writer.write(jsonObject.toJSONString());
        writer.flush();
        writer.close();
    }

    public void setCookie() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("./src/test/resources/cookies.json"));

        for (Object name : jsonObject.keySet()) {
            driver.manage().addCookie(new Cookie(name.toString(), jsonObject.get(name).toString()));
        }
        System.out.println(jsonObject);
        driver.navigate().refresh();
    }



}
