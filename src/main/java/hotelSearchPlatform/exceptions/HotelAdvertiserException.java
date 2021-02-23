package hotelSearchPlatform.exceptions;

public class HotelAdvertiserException extends Exception {
	
	public HotelAdvertiserException() {
		super ("Cannot update hotel advertiser: price and cpc must be positive, end date must be after start date...");
		}
}
