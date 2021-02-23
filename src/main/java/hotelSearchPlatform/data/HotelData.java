package hotelSearchPlatform.data;

/**
 * The HotelData class load all the hotel data necessary for the functioning of the system / Case Study to memory.
 * The HotelFacade and Filters classes utilize this class as an in memory database to perform
 * queries and update information according to client requests via the HotelController class.
 * The data is loaded from *.csv files using the methods in the CSVLoader class 
 * that are invoked with the creation of the class by the constructor.  
 */

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import org.springframework.stereotype.Service;

import hotelSearchPlatform.entities.Advertiser;
import hotelSearchPlatform.entities.City;
import hotelSearchPlatform.entities.Hotel;
import hotelSearchPlatform.entities.HotelAdvertiser;

@Service
public class HotelData {
	
	private Map<String, City> cities = new HashMap<String, City>();
	private Map<Integer, Advertiser> advertisers = new HashMap<Integer, Advertiser>();
	private Map<Integer, Hotel> hotels = new HashMap<Integer, Hotel>();
	private SortedMap<Integer, TreeSet<HotelAdvertiser>> hotelAdvertisers = new TreeMap<Integer, TreeSet<HotelAdvertiser>>();
	private CSVLoader csvLoader = new CSVLoader();
	
	public HotelData() {
		loadDataFromCSV();
	}
	
	private void loadDataFromCSV() {
		cities = csvLoader.loadCities("src/main/resources/data/cities.csv");
		advertisers = csvLoader.loadAdvertisers("src/main/resources/data/advertisers.csv");
		hotels = csvLoader.loadHotels("src/main/resources/data/hotels.csv");
		hotelAdvertisers = csvLoader.loadHotelAdvertisers("src/main/resources/data/hotel_advertiser.csv");
	}
	
	public Map<String,City> getCities() {
		return cities;
	}
	
	public Map<Integer,Advertiser> getAdvertisers() {
		return advertisers;
	}
	
	public Map<Integer, Hotel> getHotels() {
		return hotels;
	}
	
	public SortedMap<Integer, TreeSet<HotelAdvertiser>> getHotelAdvertisers() {
		return hotelAdvertisers;
	}
	

}
