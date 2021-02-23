package hotelSearchPlatform.services;

/**
 * The Filters class is used by the createSearchResult() method in HotelFacade class to filter relevant hotel data
 * based on client submitted queries sent via the HotelController class. 
 */

import java.time.LocalDate;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import hotelSearchPlatform.comparators.HotelAdvertiserComparator;
import hotelSearchPlatform.data.HotelData;
import hotelSearchPlatform.entities.City;
import hotelSearchPlatform.entities.HotelAdvertiser;


@Service
public class Filters {
	
	@Resource
	private HotelData hotelData;
	
	/**
	 * The filterByQuery method filters the hotel data first by date using the filterHotelAdvertisersByDate() method, 
	 * and then by city using filterHotelAdvetisersByCity().
	 * @param city
	 * @param startDate
	 * @param endDate
	 * @return a set of sorted hotel advertisers by hotel id, within a specific date range and city.
	 */
	public SortedMap<Integer, TreeSet<HotelAdvertiser>> filterByQuery(City city, LocalDate startDate, LocalDate endDate) {
		SortedMap<Integer, TreeSet<HotelAdvertiser>> hotelAdvertisersByQuery;
		hotelAdvertisersByQuery = filterHotelAdvertisersByDate(startDate, endDate, this.hotelData.getHotelAdvertisers());
		hotelAdvertisersByQuery = filterHotelAdvetisersByCity(city, hotelAdvertisersByQuery);
		return hotelAdvertisersByQuery;
	}

	/**
	 * The filterHotelAdvertisersByDate() method filters the hotel advertiser data by given start and end date. 
	 * The method uses the checkHotelAdvertiserDates() to check which hotel advertisers comply with given start and end dates.
	 * @param startDate
	 * @param endDate
	 * @param allHotelAdvertiserSetsByHotel
	 * @return a set of sorted hotel advertisers by hotel id, within a specific date range.
	 */
	public SortedMap<Integer, TreeSet<HotelAdvertiser>> filterHotelAdvertisersByDate(LocalDate startDate,
			LocalDate endDate, SortedMap<Integer, TreeSet<HotelAdvertiser>> allHotelAdvertiserSetsByHotel) {
		SortedMap<Integer, TreeSet<HotelAdvertiser>> filterResult = new TreeMap<Integer, TreeSet<HotelAdvertiser>>();
		for (Entry<Integer, TreeSet<HotelAdvertiser>> hotelAdvertiserSetByHotel : allHotelAdvertiserSetsByHotel.entrySet()) {
			TreeSet<HotelAdvertiser> hotelAdvertiserSetByDate = new TreeSet<HotelAdvertiser>(new HotelAdvertiserComparator());
			for (HotelAdvertiser hotelAdvertiser : hotelAdvertiserSetByHotel.getValue()) {
				if (checkHotelAdvertiserDates(startDate, endDate, hotelAdvertiser)) {
					hotelAdvertiserSetByDate.add(hotelAdvertiser);
				}
			}
			filterResult.put(hotelAdvertiserSetByHotel.getKey(), hotelAdvertiserSetByDate);
		}
		return filterResult;
	}

	
	/**
	 * The checkHotelAdvertiserDates() method checks if given start and end dates are within the range of a specific
	 * hotel advertiser's start and end dates.  
	 * @param startDate
	 * @param endDate
	 * @param hotelAdvertiser
	 * @return true if given dates are within the hotel advertisers range, false otherwise. 
	 */
	private boolean checkHotelAdvertiserDates(LocalDate startDate, LocalDate endDate, HotelAdvertiser hotelAdvertiser) {
		return (hotelAdvertiser.getAvailability_start_date().isBefore(startDate)
				|| hotelAdvertiser.getAvailability_start_date().isEqual(startDate))
				&& (hotelAdvertiser.getAvailability_end_date().isAfter(endDate)
				|| hotelAdvertiser.getAvailability_end_date().isEqual(endDate));
	}

	/**
	 * The filterHotelAdvertisersByCity() method filters the hotel advertiser data by a given city. 
	 * The method uses the HotelData class to check which hotels are in a specific city.
	 * @param city
	 * @param hotelAdvertiserSetsByHotelAndDate
	 * @return a set of sorted hotel advertisers by hotel id, within a specific city.
	 */
	public SortedMap<Integer, TreeSet<HotelAdvertiser>> filterHotelAdvetisersByCity(City city,
			SortedMap<Integer, TreeSet<HotelAdvertiser>> hotelAdvertiserSetsByHotelAndDate) {
		SortedMap<Integer, TreeSet<HotelAdvertiser>> filterResult = new TreeMap<Integer, TreeSet<HotelAdvertiser>>();
		for (Entry<Integer, TreeSet<HotelAdvertiser>> hotelId : hotelAdvertiserSetsByHotelAndDate.entrySet()) {
			if (this.hotelData.getHotels().get(hotelId.getKey()).getCity_id() == city.getId()) {
				filterResult.put(hotelId.getKey(), hotelId.getValue());
			}
		}
		return filterResult;
	}


}
