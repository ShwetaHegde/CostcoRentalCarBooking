package costco.car.rental;
public class BookingRequest {
	
	private String pickUpLocation, airport, dropOffLocation, pickUpDate, pickUpTime, dropOffDate, dropOffTime;
	
	public BookingRequest(String pickUpLocation, String airport, String dropOffLocation, String pickUpDate, String pickUpTime, String dropOffDate, String dropOffTime) {
	    
		this.pickUpLocation = pickUpLocation;
		this.airport = airport;
		this.dropOffLocation = dropOffLocation;
		this.pickUpDate = pickUpDate;
		this.pickUpTime = pickUpTime;
		this.dropOffDate = dropOffDate;
		this.dropOffTime = dropOffTime;
	
	}
	
	public String getPickUpLocation() {
		
		return pickUpLocation;
	}
	
	public String getAirport() {
		
		return airport;
	}
	
	public String getDropOffLocation() {
		
		return dropOffLocation;
	}
	
	public String getPickUpDate() {
		
		return pickUpDate;
	}
	
	public String getPickUpTime() {
		
		return pickUpTime;
	}
	
	public String getDropOffDate() {
		
		return dropOffDate;
	}
	
	public String getDropOffTime() {
		
		return dropOffTime;
	}
	
	@Override
	public String toString() {
		
		StringBuilder booking = new StringBuilder();
		
		booking.append(airport).append(" ");
		
		booking.append(pickUpDate).append(" ");
		booking.append(pickUpTime).append(" ");
		
		booking.append(dropOffDate).append(" ");
		booking.append(dropOffTime) ;
		
		return booking.toString() ;
	}
	

	
}
