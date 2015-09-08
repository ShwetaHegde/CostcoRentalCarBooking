package costco.car.rental;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Timeouts;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class SignInPage {

	private WebDriver driver;
	private static String URL = "https://www.costcotravel.com";
	private static By myAccount = By.name("yourItinerary");
	private By membershipID = By.id("membershipNumber");
	private By password = By.id("password");
	private By loginButton = By.id("loginButton_1");
	private By carRentalPrice = By.xpath("//input[@id='carRentalPrice']");
	private By currentRentalCarBookings = By.xpath("//tbody[@id='currentBookings']/tr/td/a");
	private By bookedCarInfo = By.xpath("//div[@class='transferResultComponent']");
	private By carPickUp = By.xpath("//td[@class='pb10']");
	private By carDropOff = By.xpath(".//*[@id='carBookingConfirmationDiv']/div[6]/div[1]/table/tbody/tr/td[2]/table/tbody/tr[2]/td[2]/b");
	private WebElement bookedCarDetails, bookedCarType, pickUpDetails, getpickUpDate, getDropOffDate, totalPrice;
	private String[] getPickUpLocation, getPickUpDateTime, getDropOffDateTime;
	private String pickUpLocation, pickUpDate, pickUpTime, dropOffDate, dropOffTime;
	private List<WebElement> currentBookings;

    public void startSession() {
		
		driver = new FirefoxDriver();
		driver.get(URL);
		driver.manage().window().maximize();
	}
    
    public void logIn() {
    	
	    driver.findElement(myAccount).click();
	    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		Properties prop = new Properties();
		try {
			
			InputStream input = SignInPage.class.getResourceAsStream("config.file");
			prop.load(input);
		}
		catch (IOException ex) {
			
			ex.printStackTrace();
		}
		driver.findElement(membershipID).sendKeys(prop.getProperty("costcoUser"));
		driver.findElement(password).sendKeys(prop.getProperty("password"));
		driver.findElement(loginButton).click();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }
	
    public void getBookedCarDetails() throws IOException, InterruptedException {
    	
	    currentBookings = driver.findElements(currentRentalCarBookings);
	
	    for (int i = 0; i < currentBookings.size(); ++i) {
	    	
	    	currentBookings = driver.findElements(currentRentalCarBookings);
	    	currentBookings.get(i).click();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			bookedCarDetails = driver.findElement(bookedCarInfo);
			
			bookedCarType = bookedCarDetails.findElement(By.tagName("h3"));
			
			pickUpDetails = bookedCarDetails.findElement(carPickUp);
			getPickUpLocation = pickUpDetails.getText().split("\\r?\\n");
			pickUpLocation = getPickUpLocation[1];
			
			getpickUpDate = pickUpDetails.findElement(By.xpath("//b"));
			getPickUpDateTime = getpickUpDate.getText().split("   ");
			pickUpDate = MonthMapping.formatMonth(getPickUpDateTime);
				
			pickUpTime = getPickUpDateTime[1];
			
			getDropOffDate = driver.findElement(carDropOff);
	    	getDropOffDateTime = getDropOffDate.getText().split("  ");
	    	dropOffDate = MonthMapping.formatMonth(getDropOffDateTime);

			dropOffTime = getDropOffDateTime[1];
			
			totalPrice = bookedCarDetails.findElement(carRentalPrice);
			    		
			String[] rentalCarDetails = {"An airport", pickUpLocation, "Same as Pick up location", pickUpDate, pickUpTime,
	                dropOffDate, dropOffTime, totalPrice.getAttribute("value"), bookedCarType.getText() };
			
			RentalCarBooking rentalCarBooking = new RentalCarBooking();
			rentalCarBooking.searchRentalCars(rentalCarDetails, driver);
			
			driver.findElement(myAccount).click();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);    
	    }
        driver.close();
    }

}
