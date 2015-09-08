package costco.car.rental;

import java.util.HashMap;
import java.util.Map;

public class MonthMapping {
	
	static Map<String, Integer> months = new HashMap<String, Integer>();
	static boolean initialized = false ;
	
	public static void init() {
		
		if ( initialized ) return ;
		
		initialized = true ;
		
	    months.put("Jan", 1);
	    months.put("Feb", 2);
	    months.put("Mar", 3);
	    months.put("Apr", 4);
	    months.put("May", 5);
	    months.put("Jun", 6);
	    months.put("Jul", 7);
	    months.put("Aug", 8);
	    months.put("Sep", 9);
	    months.put("Oct", 10);
	    months.put("Nov", 11);
	    months.put("Dec", 12);
	}
	
    public static String getMonth(String month) {
    	
    	init() ;
    	return months.get(month).toString();
    }
    
    public static String formatMonth(String[] dateDetails) {
    	
    	init() ;
    
		String dateNeeded = dateDetails[0].substring(6);
		String formatMonth = dateNeeded.substring(0,  3);
		formatMonth = getMonth(formatMonth);
		dateNeeded = dateNeeded.replace(dateNeeded.substring(0, 3), formatMonth).replace(". ", "/").replace(", ", "/");
		
		return dateNeeded;
    }
    
}
