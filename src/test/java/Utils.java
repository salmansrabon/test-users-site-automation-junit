import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.util.Random;

public class Utils {
    public static int generateRandomNumber(int min, int max){
        return (int) Math.round(Math.random()*(max-min)+min);
    }
    public static String generateRandomPassword(){
        String letters="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890@#$";
        char[] chars=  letters.toCharArray();
        int password_length=8;
        StringBuilder sb=new StringBuilder();
        Random random=new Random();
        for(int i=0;i<password_length;i++){
            int randomIndex= random.nextInt(chars.length);
            sb.append(chars[randomIndex]);
        }
        return sb.toString();
    }
    public static void scrollDown(WebDriver driver){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,document.body.scrollHeight)");
    }
    public static void scrollDown(WebDriver driver,int px){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,"+px+")");
    }

    public static void main(String[] args) {
        System.out.println(generateRandomPassword());
    }
}
