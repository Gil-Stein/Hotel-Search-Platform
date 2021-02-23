package hotelSearchPlatform.web;

/**
 * The HotelController class enables GET and POST request from web clients to read and update information
 * on the HotelData class via methods in the HotelFacade class.
 */

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeSet;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import hotelSearchPlatform.entities.Hotel;
import hotelSearchPlatform.entities.HotelAdvertiserWithStringDate;
import hotelSearchPlatform.entities.Offer;
import hotelSearchPlatform.exceptions.HotelAdvertiserException;
import hotelSearchPlatform.services.HotelFacade;

@RestController
public class HotelController {
	
	@Resource
	private HotelFacade hotelFacade;
	
	/**
	 * The searchHotelOffers() method enables GET requests by web clients to answer search queries regarding the 
	 * existing hotel data in memory.
	 * @param city - City name
	 * @param startDateString - String date
	 * @param endDateString - String date
	 * @return The method returns a sorted map of hotel advertisers by hotel according to given parameters. 
	 * In case there are no matched for the query sent the method returns an "ok" response with a relevant notice
	 * in its body. In case of bad requests (e.g. illogical dates, invalid date format, non-existing city name)
	 * the method returns a "bad request" response with a relevant notice in body. 
	 */
	@GetMapping("/search/{city}/{startDateString}/{endDateString}")
	public ResponseEntity<?> serachHotelOffers (@PathVariable String city, @PathVariable String startDateString, @PathVariable String endDateString) {	
		Map<Hotel, TreeSet<Offer>> hotelOffers = null;
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd", Locale.ENGLISH);
		try {
			LocalDate startDate = LocalDate.parse(startDateString, dateFormatter);
			LocalDate endDate = LocalDate.parse(endDateString, dateFormatter);
			if (startDate.isAfter(endDate))
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("End date must be after start date...");
			hotelOffers = hotelFacade.createSearchResult(city, startDate , endDate);
		} catch (DateTimeParseException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Date must be given in 'yyyyMMdd' format...");
		} catch (NullPointerException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("City name does not exist in database...");
		}
		if (hotelOffers.isEmpty())
			return ResponseEntity.ok("No offers found for entered query...");
		return ResponseEntity.ok(hotelOffers);
	}
	
	/**
	 * The updateAdvertiser() method enables POST updates to the hotel data existing in memory.
	 * @param hotelAdvertiserToUpdate - a HotelAdvertiserWithStringDate entity, with String dates.
	 * @return if the hotel advertiser id exists the method will update the data of the advertiser in memory.
	 * If the hotel advertiser id does not exist, the method will add the data to the hotel data in memory.
	 * In case of "bad requests" (e.g. illogical dates, invalid date format, negative price or cpc) the 
	 * method will return an appropriate response with a relevant notice in its body. 
	 */
	@PostMapping("/price/")
	public ResponseEntity<String> updateAdvertiser (@RequestBody List<HotelAdvertiserWithStringDate> hotelAdvertiserToUpdate) {
		try {
			hotelFacade.updateAdvertiser(hotelAdvertiserToUpdate);
		} catch (DateTimeParseException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("start and end dates must be given in 'yyyyMMdd' format...");
		} catch (HotelAdvertiserException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.ok("Hotel advertiser updated!");
	}

}
