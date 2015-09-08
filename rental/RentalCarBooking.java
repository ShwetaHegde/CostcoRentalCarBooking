package costco.car.rental;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;


public class RentalCarBooking {
	
	private static By rentalCarMenu = By.xpath("//a[@menuid='rentalCarsMenu']");
	private static By pickUpLoc = By.xpath("//select[@id='pickupLocationTypeSearchWidget']");
	private static By airportPickUp = By.xpath("//input[@id='pickupAirportTextWidget']");
	private static By autoCompleteAirportLoc = By.xpath("//ul[@class='sayt-box']/li");
	private static By dropOffLoc = By.xpath("//select[@id='dropoffLocationTypeSearchWidget']");
	private static By pickUpDate = By.xpath("//input[@id='pickupDateWidget']");
	private static By pickUpTimeLoc = By.xpath("//select[@id='pickupTimeWidget']");
	private static By dropOffDate = By.xpath("//input[@id='dropoffDateWidget']");
	private static By dropOffTimeLoc = By.xpath("//select[@id='dropoffTimeWidget']");
	private static By rentalCarSearchButton = By.xpath("//a[@id='carSearchWidget_search']");
	private static By rentalCarMatrixLoc = By.xpath("//table[@class='carMatrixTable']");
	private static By cheapCarPriceLoc = By.xpath("//span[@class='tc09']");
	private static By cheapCarTypeLoc = By.xpath("//th[@class='tar selectedBottomBorder']");
	private WebElement pickUpLocation, dropOffLocation, pickUpTime, dropOffTime, tableElement, cheapCarPrice, cheapCarType;
	private String[] airportDetails;
	private String airportName;
	private BookingRequest bookingRequest ;

	public void searchRentalCars(String[] args, WebDriver driver) throws IOException, InterruptedException {
        
		bookingRequest = new BookingRequest(args[0], args[1], args[2], args[3], args[4], args[5], args[6]) ;
		
		driver.findElement(rentalCarMenu).click();
		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		pickUpLocation = driver.findElement(pickUpLoc);
		Select selectLocation = new Select(pickUpLocation);
		selectLocation.selectByVisibleText(bookingRequest.getPickUpLocation());
		
		airportDetails = bookingRequest.getAirport().split(" ");
		airportName = "";
		for (String airport : airportDetails) {
		    
			airportName += airport;
			driver.findElement(airportPickUp).clear();
			driver.findElement(airportPickUp).sendKeys(airportName);
			Thread.sleep(5000);;

			List<WebElement> getAirportFromList = driver.findElements(autoCompleteAirportLoc);
			Thread.sleep(5000);;

			if ( getAirportFromList.size() == 1 ) {
				
				driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

				getAirportFromList.get(0).click();
				break;
			}
			airportName += " ";
		}
		
		dropOffLocation = driver.findElement(dropOffLoc);
		Select selectDropOffLocation = new Select(dropOffLocation);
		selectDropOffLocation.selectByVisibleText(bookingRequest.getDropOffLocation());
		
		driver.findElement(pickUpDate).clear();
		driver.findElement(pickUpDate).sendKeys(bookingRequest.getPickUpDate());
		
		pickUpTime = driver.findElement(pickUpTimeLoc);
		Select selectPickUpTime = new Select(pickUpTime);
		selectPickUpTime.selectByVisibleText(bookingRequest.getPickUpTime());
		
		driver.findElement(dropOffDate).clear();
		driver.findElement(dropOffDate).sendKeys(bookingRequest.getDropOffDate());
		
		dropOffTime = driver.findElement(dropOffTimeLoc);
		Select selectDropOffTime = new Select(dropOffTime);
		selectDropOffTime.selectByVisibleText(bookingRequest.getDropOffTime());
		
		
		driver.findElement(rentalCarSearchButton).click();
		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		getRentalCarMatrix(driver, args[7], args[8]);
	}
	
	public void getRentalCarMatrix(WebDriver driver, String rentPrice, String bookedCar) {
		
		tableElement = driver.findElement(rentalCarMatrixLoc);
		cheapCarPrice = tableElement.findElement(cheapCarPriceLoc);
		cheapCarType = tableElement.findElement(cheapCarTypeLoc);
		Map<String, Double> carPriceDetailsMap = new HashMap<String, Double>();
		
		carPriceDetailsMap.put(cheapCarType.getText(), Double.parseDouble(cheapCarPrice.getText().substring(1)));
		
		List<WebElement> rowElements = tableElement.findElements(By.xpath("//tr"));
		Set<String> requiredCars = new HashSet<String>();
		requiredCars.add("Intermediate Car");
		requiredCars.add("Standard Car");
		requiredCars.add("Fullsize Car");
		String bookedRentalCar = (bookedCar.split("-"))[1].trim();
		requiredCars.add(bookedRentalCar);
		
		double minPrice = 0;

		while ( carPriceDetailsMap.size() < requiredCars.size()) {
	 		for ( WebElement row : rowElements ) {
				
				try {
					
					WebElement carType = row.findElement(By.className("tar"));
					if ( carType.getText().equals(cheapCarType.getText()) ) continue;
					if ( requiredCars.contains(carType.getText()) ) {
						
						List<WebElement> carPrice = row.findElements(By.className("u "));
						
						minPrice = Double.parseDouble(carPrice.get(0).getText().substring(1));
						for ( int i = 1; i < carPrice.size(); ++i ) {
							
							double carPriceAnother = Double.parseDouble(carPrice.get(i).getText().substring(1));
							
							if ( carPriceAnother < minPrice ) {
								
								minPrice = carPriceAnother;
							}
						}
						carPriceDetailsMap.put(carType.getText(), minPrice);
					}
					
				}
				catch (Exception ex) {
					
				}
					
			}
		}
		StringBuilder emailBody = new StringBuilder();
		
		emailBody.append( "Booked Car Type : " + bookedRentalCar + " Price Paid : " + rentPrice  + "\n\n") ;
		emailBody.append( String.format("%-30s", new String("\nCar Type"))  + String.format("%20s", "Now Price is : $ \n\n")  );
		
		
		boolean emailBodyContains = false;

		for (String car : requiredCars) {
			
			if (Double.parseDouble(rentPrice.substring(1)) > carPriceDetailsMap.get(car)) {
				
				emailBodyContains = true;
				emailBody.append( String.format("%-30s", car) + String.format("%20s",  carPriceDetailsMap.get(car) ) + "\n" );
			}
		}
		
		if (emailBodyContains) {
			
			EmailTheUser.emailMe(  bookingRequest.toString() , emailBody);
		}
	
	}
}
	


