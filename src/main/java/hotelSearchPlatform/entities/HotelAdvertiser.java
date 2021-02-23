package hotelSearchPlatform.entities;

import java.time.LocalDate;

public class HotelAdvertiser {

	private int advertiser_id, hotel_id, cpc, price;
	private String currency;
	private LocalDate availability_start_date, availability_end_date;

	public HotelAdvertiser(int advertiser_id, int hotel_id, int cpc, int price, String currency,
			LocalDate availability_start_date, LocalDate availability_end_date) {
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

	public LocalDate getAvailability_start_date() {
		return availability_start_date;
	}

	public LocalDate getAvailability_end_date() {
		return availability_end_date;
	}

	@Override
	public String toString() {
		return "HotelAdvertiser [advertiser_id=" + advertiser_id + ", hotel_id=" + hotel_id + ", cpc=" + cpc
				+ ", price=" + price + ", currency=" + currency + ", availability_start_date=" + availability_start_date
				+ ", availability_end_date=" + availability_end_date + "]";
	}

}
