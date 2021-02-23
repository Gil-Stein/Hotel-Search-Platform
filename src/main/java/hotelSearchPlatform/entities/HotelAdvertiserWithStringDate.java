package hotelSearchPlatform.entities;

public class HotelAdvertiserWithStringDate {

	private int advertiser_id, hotel_id, cpc, price;
	private String currency, availability_start_date, availability_end_date;

	public HotelAdvertiserWithStringDate(int advertiser_id, int hotel_id, int cpc, int price, String currency,
			String availability_start_date, String availability_end_date) {
		super();
		this.advertiser_id = advertiser_id;
		this.hotel_id = hotel_id;
		this.cpc = cpc;
		this.price = price;
		this.currency = currency;
		this.availability_start_date = availability_start_date;
		this.availability_end_date = availability_end_date;
	}

	public int getAdvertiser_id() {
		return advertiser_id;
	}

	public int getHotel_id() {
		return hotel_id;
	}

	public int getCpc() {
		return cpc;
	}

	public int getPrice() {
		return price;
	}

	public String getCurrency() {
		return currency;
	}

	public String getAvailability_start_date() {
		return availability_start_date;
	}

	public String getAvailability_end_date() {
		return availability_end_date;
	}

	@Override
	public String toString() {
		return "HotelAdvertiser [advertiser_id=" + advertiser_id + ", hotel_id=" + hotel_id + ", availability_start_date=" + availability_start_date
				+ ", availability_end_date=" + availability_end_date + "]";
	}

}
