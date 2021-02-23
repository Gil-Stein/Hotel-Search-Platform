package hotelSearchPlatform.services;

/**
 * The HotelFacade class implements the business logic to perform search and update functions to the HotelData class
 * as specified in the Case Study instructions. The HotelFacade is used by the HotelControll class to enable clients
 * the ability to perform said search and update functions.
 */

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import hotelSearchPlatform.comparators.HotelComparator;
import hotelSearchPlatform.comparators.OfferComparator;
import hotelSearchPlatform.data.HotelData;
import hotelSearchPlatform.entities.City;
import hotelSearchPlatform.entities.Hotel;
import hotelSearchPlatform.entities.HotelAdvertiser;
import hotelSearchPlatform.entities.HotelAdvertiserWithStringDate;
import hotelSearchPlatform.entities.Offer;
import hotelSearchPlatform.exceptions.HotelAdvertiserException;

@Service
public class HotelFacade {

	@Resource
	private HotelData hotelData;
	@Resource
	private Filters filter;
	
	private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd", Locale.ENGLISH);

	/** The createSearchResult method performs a query on the hotel data loaded to the HotelData class according
	 * to city name, end and start dates. The method relies on the sorted map of hotel id to hotel advertiser set in
	 * the HotelData class and the filterbyQuery method() in the Filters class. The method creates results 
	 * with relevant hotel information and an offer set of hotel advertisers that includes the hotel advertiser 
	 * name from the advertisers map in HotelData class. Hotels are sorted by rating based on the HotelComparator class. 
	 * Offers are sorted by price increasing and cpc decreasing based on the OfferComparator class. 
	 * @param cityName
	 * @param startDate
	 * @param endDate
	 * @return a sorted map of 
	 * @throws NullPointerException - in case the City doesn't exist in HotelData.
	 */
	
	public SortedMap<Hotel, TreeSet<Offer>> createSearchResult(String cityName, LocalDate startDate,LocalDate endDate) 
		throws NullPointerException {	
		City city = this.hotelData.getCities().get(cityName);
		if (city.equals(null))
			throw new NullPointerException();
		
		SortedMap<Hotel, TreeSet<Offer>> searchResult = new TreeMap<Hotel, TreeSet<Offer>>(new HotelComparator());
		for (Entry<Integer, TreeSet<HotelAdvertiser>> hotelAdvertiserSetByHotel : filter.filterByQuery(city, startDate, endDate).entrySet()) {
			TreeSet<Offer> offers = new TreeSet<Offer>(new OfferComparator());
			for (HotelAdvertiser hotelAdvertiser : hotelAdvertiserSetByHotel.getValue()) {
				Offer offer = createOffer(hotelAdvertiser);
				offers.add(offer);
			}
			if (!offers.isEmpty())
			searchResult.put(this.hotelData.getHotels().get(hotelAdvertiserSetByHotel.getKey()), offers);
		}
		return searchResult;
	}

	/**
	 * The createOffer method creates an offer based on the given hotel advertiser, and includes the advertiser
	 * name fetched from the HotelData class.
	 * @param hotelAdvertiser - not including advertiser name. 
	 * @return - an Offer entity with advertiser name. 
	 */
	private Offer createOffer(HotelAdvertiser hotelAdvertiser) {
		Offer offer = new Offer(hotelAdvertiser.getAdvertiser_id(), hotelAdvertiser.getCpc(), hotelAdvertiser.getPrice(),
				this.hotelData.getAdvertisers().get(hotelAdvertiser.getAdvertiser_id()).getAdvertiser_name(),
				hotelAdvertiser.getCurrency());
		return offer;
	}

	/**
	 * The updateAdvertiser method translates a list of hotel advertisers with String dates, sent by a client via
	 * the HotelController class, and updates or adds the information on the HotelData class. If the hotel advertiser
	 * to be updated exists in the HotelData class, the method deletes the old hotel advertiser using the
	 * removeHotelAdvertiserFromSet() method and replaces it with an updated hotel advertiser using the 
	 * createHotelAdvertiser() method.
	 * @param hotelAdvertisersWithStringDate - sent by client via HotelController.
	 * @throws DateTimeParseException - in case date sent by client is not in the 'yyyyMMdd' format.
	 * @throws HotelAdvertiserException - in case data sent by client is invalid 
	 * (i.e. negative price or cpc, end date is before start date).
	 */
	public void updateAdvertiser(List<HotelAdvertiserWithStringDate> hotelAdvertisersWithStringDate) 
			throws DateTimeParseException, HotelAdvertiserException {
		for (HotelAdvertiserWithStringDate hotelAdvWithStringDate : hotelAdvertisersWithStringDate) {
			checkHotelAdvWithStingDate(hotelAdvWithStringDate);
			int advertiserIdToUpdate = hotelAdvWithStringDate.getAdvertiser_id();
			int hotelIdtoUpdate = hotelAdvWithStringDate.getHotel_id();
			HotelAdvertiser updatedHotelAdvertiser = createHotelAdvertiser(hotelAdvWithStringDate, advertiserIdToUpdate);
			TreeSet<HotelAdvertiser> hotelAdvertiserSetToUpdate = this.hotelData.getHotelAdvertisers().get(hotelAdvWithStringDate.getHotel_id());
			hotelAdvertiserSetToUpdate = removeHotelAdvertiserFromSet(hotelAdvertiserSetToUpdate, advertiserIdToUpdate);
			hotelAdvertiserSetToUpdate.add(updatedHotelAdvertiser);
			this.hotelData.getHotelAdvertisers().put(hotelIdtoUpdate, hotelAdvertiserSetToUpdate);
			System.out.println();
		}
	}

	/**
	 * The checkHotelAdvWithStingDate is used by the updateAdvertiser() method to ensure the given 
	 * HotelAdvertiserWithStringDate entity can be translated to a HotelAdvertiser entity.  
	 * @param hotelAdvWithStringDate - sent by client via HotelController.
	 * @throws DateTimeParseException - in case date sent by client is not in the 'yyyyMMdd' format.
	 * @throws HotelAdvertiserException - in case data sent by client is invalid 
	 * (i.e. negative price or cpc, end date is before start date).
	 */
	private void checkHotelAdvWithStingDate(HotelAdvertiserWithStringDate hotelAdvWithStringDate) 
			throws DateTimeParseException, HotelAdvertiserException{
			LocalDate startDate = LocalDate.parse(hotelAdvWithStringDate.getAvailability_start_date(), dateFormatter);
			LocalDate endDate = LocalDate.parse(hotelAdvWithStringDate.getAvailability_end_date(), dateFormatter);
			if (hotelAdvWithStringDate.getPrice()<0 || hotelAdvWithStringDate.getCpc()<0 || endDate.isBefore(startDate))
				throw new HotelAdvertiserException();
	}

	/**
	 * The createHotelAdvertiser method creates a new HotelAdvertiser entity from a valid HotelAdvertiserWithStringDate entity.
	 * @param hotelAdvWithStringDate
	 * @param idToUpdate
	 * @return
	 */
	private HotelAdvertiser createHotelAdvertiser(HotelAdvertiserWithStringDate hotelAdvWithStringDate, int idToUpdate) {
		HotelAdvertiser hotelAdv = new HotelAdvertiser(idToUpdate, hotelAdvWithStringDate.getHotel_id(),
				hotelAdvWithStringDate.getCpc(), hotelAdvWithStringDate.getPrice(), hotelAdvWithStringDate.getCurrency(),
				LocalDate.parse(hotelAdvWithStringDate.getAvailability_start_date(), dateFormatter),
				LocalDate.parse(hotelAdvWithStringDate.getAvailability_end_date(), dateFormatter));
		return hotelAdv;
	}

	/**
	 * The removeHotelAdvertiserFromSet method is used by the updateAdvertiser() method to find and remove an
	 * existing HotelAdvertiser that needs to be updated from a set of hotel advertisers of a specific hotel.
	 * @param hotelAdvertisersToUpdate
	 * @param advertiser_id
	 */
	private TreeSet<HotelAdvertiser> removeHotelAdvertiserFromSet(TreeSet<HotelAdvertiser> hotelAdvertisersToUpdate, int advertiser_id) {
		HotelAdvertiser hotelAdvertiserToRemove = null;
		for (HotelAdvertiser hotelAdvertiserToUpdate : hotelAdvertisersToUpdate) {
			if (hotelAdvertiserToUpdate.getAdvertiser_id() == advertiser_id)
				hotelAdvertiserToRemove = hotelAdvertiserToUpdate;
		}
		if (hotelAdvertiserToRemove != null)
			hotelAdvertisersToUpdate.remove(hotelAdvertiserToRemove);
		
		return hotelAdvertisersToUpdate;

	}
}
