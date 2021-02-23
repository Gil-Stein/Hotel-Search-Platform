package hotelSearchPlatform.comparators;

import java.util.Comparator;

import hotelSearchPlatform.entities.Hotel;

public class HotelComparator implements Comparator<Hotel>{
	
	@Override
	public int compare(Hotel hotel1, Hotel hotel2) {
		
		if (hotel1.getRating()<hotel2.getRating())
			return 1;
		if (hotel1.getRating()>hotel2.getRating())
			return -1;
		return 0;
	}
	
	

}
