package costco.car.rental;

import java.io.IOException;

public class CostcoRentalCarBookApp {
    
	public static void main( String[] args ) throws InterruptedException, IOException {
		
		SignInPage signInPage = new SignInPage();
		
		signInPage.startSession();
		signInPage.logIn();
		signInPage.getBookedCarDetails();
		
		System.out.println("Successful run");
    
	}

}
