package hotelSearchPlatform.data;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import hotelSearchPlatform.comparators.HotelAdvertiserComparator;
import hotelSearchPlatform.entities.Advertiser;
import hotelSearchPlatform.entities.City;
import hotelSearchPlatform.entities.Hotel;
import hotelSearchPlatform.entities.HotelAdvertiser;

/**
 * The CSVLoader class is a service that uses the Apache Common CVS Reader to read data from external *.csv 
 * files stored in the resources directory. The data is translated into relevant entities and returned as a map.   *
 */

@Service
public class CSVLoader {

	/**
	 * The records Iterator will hold data translated from relevant csv files. 
	 * The headers map will hold the relevant headers for each data entity. 
	 * The dateFormater will be used to parse String data from the csv file into LocalDate format.
	 * The CSVLoader constructor loads the header names of the relevant csv file and data entity
	 * into the headers map.   
	 */
	
	private Iterable<CSVRecord> records = null;
	private Map<String, String[]> headers = new HashMap<String, String[]>();
	private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd", Locale.ENGLISH);

	public CSVLoader() {
		String[] cityHeaders = {"id", "city_name"};
		String[] advertiserHeaders = {"id", "advertiser_name"};
		String[] hotelHeaders = {"id", "city_id", "clicks", "impressions", "name", "rating", "stars"};
		String[] hotelAdvertiserHeaders = {"advertiser_id", "hotel_id", "cpc", "price", "currency",
				"availability_start_date", "availability_end_date"};
		
		headers.put("cities", cityHeaders);
		headers.put("advertisers", advertiserHeaders);
		headers.put("hotels", hotelHeaders);
		headers.put("hotelAdvertisers", hotelAdvertiserHeaders);
	}

	/**
	 * The loadCities method creates a map of the cities.csv file, with the city name as a String key.
	 * The key is the city name as a String in order to find it easily when a query is sent by a web client
	 * via the HotelController class.  
	 * @param path - - location of the cities.csv file in the resource directory.
	 * @return - map of cities from the csv file with the city name as a String Key.
	 */
	public Map<String, City> loadCities(String path) {
		Map<String, City> cities = new HashMap<String, City>();
		readFromFile(path, "cities");
		for (CSVRecord record : records) {
			int id = Integer.parseInt(record.get("id"));
			cities.put(record.get("city_name"), new City(id, record.get("city_name")));
		}
		return cities;
	}

	/**
	 * The loadAdvertisers method creates a map of the advertisers.csv file, with the advertiser id as key.  
	 * @param path - location of the advertisers.csv file in the resource directory.
	 * @return - map of the advertisers from the csv file with the advertiser id as Key.
	 */
	public Map<Integer, Advertiser> loadAdvertisers(String path) {
		Map<Integer, Advertiser> advertisers = new HashMap<Integer, Advertiser>();
		readFromFile(path, "advertisers");
		for (CSVRecord record : records) {
			int id = Integer.parseInt(record.get("id"));
			advertisers.put(id, new Advertiser(id, record.get("advertiser_name")));
		}
		return advertisers;
	}

	/**
	 * The loadHotels method creates a map of the hotels.csv file, with the hotel id as key.  
	 * @param path - location of the hotels.csv file in the resource directory.
	 * @return - map of the hotels from the csv file with the hotel id as Key.
	 */
	public Map<Integer, Hotel> loadHotels(String path) {
		Map<Integer, Hotel> hotels = new HashMap<Integer, Hotel>();
		readFromFile(path, "hotels");
		for (CSVRecord record : records) {
			int id = Integer.parseInt(record.get("id"));
			int city_id = Integer.parseInt(record.get("city_id"));
			int clicks = Integer.parseInt(record.get("clicks"));
			int impressions = Integer.parseInt(record.get("impressions"));
			int rating = Integer.parseInt(record.get("rating"));
			int stars = Integer.parseInt(record.get("stars"));
			hotels.put(id, new Hotel(id, city_id, clicks, impressions, rating, stars, record.get("name")));
		}
		return hotels;
	}

	/**
	 * The loadHotelAdvertisers method creates a map of the hotel_advertiser.csv file, with a Hotel id as key.
	 * The map is sorted by hotel id and each hotel id has a set of hotel advertisers.
	 * the hotel advertisers are sorted by the hotelAdvertiserComparator class according to price increasing and cpc decreasing.   
	 * @param path - location of the hotel_advertiser.csv file in the resource directory.
	 * @return - sorted map of the hotel advertisers by hotel id from the csv file with the advertiser id as Key.
	 */
	public SortedMap<Integer, TreeSet<HotelAdvertiser>> loadHotelAdvertisers(String path) {
		SortedMap<Integer, TreeSet<HotelAdvertiser>> hotelAdvertisers = new TreeMap<Integer, TreeSet<HotelAdvertiser>>();
		readFromFile(path, "hotelAdvertisers");
		for (CSVRecord record : records) {
			int advertiser_id = Integer.parseInt(record.get("advertiser_id"));
			int hotel_id = Integer.parseInt(record.get("hotel_id"));
			int cpc = Integer.parseInt(record.get("cpc"));
			int price = Integer.parseInt(record.get("price"));

			if (hotelAdvertisers.get(hotel_id)==null) 
				hotelAdvertisers.put(hotel_id, new TreeSet<HotelAdvertiser>(new HotelAdvertiserComparator()));
			
			TreeSet<HotelAdvertiser> hotelAdvertiserSet = hotelAdvertisers.get(hotel_id);
			hotelAdvertiserSet.add(createHotelAdvertiser(record, advertiser_id,	hotel_id, cpc, price));	
		}
		
		return hotelAdvertisers;
	}

	/**
	 * The createAdvertiser method is used by the loadHotelAdvertiser method to create HotelAdvertiser entities
	 * and parse the String date in the csv file to a LocalDate using dateFormatter. 
	 * All parameters are taken from the csv file via the loadHotelAdvertiser method and passed to the entity's constructor.
	 * @param record
	 * @param advertiser_id
	 * @param hotel_id
	 * @param cpc
	 * @param price
	 * @return HotelAdvertiser entity.
	 */
	private HotelAdvertiser createHotelAdvertiser(CSVRecord record, int advertiser_id, int hotel_id, int cpc, int price) {
		return new HotelAdvertiser(advertiser_id, hotel_id, cpc, price, record.get("currency"),
				LocalDate.parse(record.get("availability_start_date"), dateFormatter),
				LocalDate.parse(record.get("availability_end_date"), dateFormatter));
	}

	/**
	 * The readFromFile method is used by loadCities, loadAdvertisers, loadHotels and loadHotelAdvertiser method
	 * to set the relevant data from a specific csv file in the records Iterator in this CSVLoader class, to 
	 * be used by the specific method to populate the relevant dataEntity map.
	 * @param path - location of the relevant csv file in the resources directory.
	 * @param dataEntity - type of entity to be mapped (i.e. City/Hotel/Advertiser/hoteAdvertiser)
	 */
	private void readFromFile(String path, String dataEntity) {
		try {
			Reader in = new FileReader(path);
			records = CSVFormat.EXCEL.withHeader(headers.get(dataEntity)).withFirstRecordAsHeader().parse(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
