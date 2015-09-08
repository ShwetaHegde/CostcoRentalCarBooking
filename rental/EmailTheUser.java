package costco.car.rental;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class EmailTheUser {
	
	private static WebDriver driver;
	private static String URL = "https://mail.google.com";
	private static By composeEmail = By.xpath("//div[@class='z0']/div");
	private static By emailTo = By.className("vO");
	private static By emailSub = By.className("aoT");
	private static By emailSend = By.xpath("//div[text()='Send']");
	
	public static void emailMe(String emailSubject, StringBuilder emailBodyContent) {
		
		System.setProperty("webdriver.chrome.driver", "/Users/sandesh/Documents/Selenium/chromedriver");
		driver = new ChromeDriver();

		driver.get(URL);
		
		Properties prop = new Properties();
		try {
			
			InputStream input = SignInPage.class.getResourceAsStream("config.file");
			prop.load(input);
		}
		catch (IOException ex) {
			
			ex.printStackTrace();
		}
		driver.findElement(By.id("Email")).sendKeys(prop.getProperty("emailUserName"));
		driver.findElement(By.id("next")).click();
		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		driver.findElement(By.id("Passwd")).sendKeys(prop.getProperty("emailPassword"));
		driver.findElement(By.id("signIn")).click();
		
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		
		driver.findElement(composeEmail).click();
		
		driver.findElement(emailTo).sendKeys(prop.getProperty("emailId"));
        driver.findElement(emailSub).sendKeys(emailSubject);
       
        driver.findElement(By.xpath("//div[@aria-label='Message Body']")).sendKeys(emailBodyContent);
        
        driver.findElement(emailSend).click();
        
        driver.close();
	}


}
