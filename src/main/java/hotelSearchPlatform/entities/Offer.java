package hotelSearchPlatform.entities;

public class Offer {

	private int advertiser_id, cpc, price;
	private String advertiser_name, currency;

	public Offer(int advertiser_id, int cpc, int price, String advertiser_name, String currency) {
		super();
		this.advertiser_id = advertiser_id;
		this.cpc = cpc;
		this.price = price;
		this.advertiser_name = advertiser_name;
		this.currency = currency;
	}

	public Offer() {
	}

	public int getAdvertiser_id() {
		return advertiser_id;
	}

	public int getCpc() {
		return cpc;
	}

	public int getPrice() {
		return price;
	}

	public String getAdvertiser_name() {
		return advertiser_name;
	}

	public String getCurrency() {
		return currency;
	}

	@Override
	public String toString() {
		return "Offer [advertiser_id=" + advertiser_id + ", cpc=" + cpc + ", price=" + price + ", advertiser_name="
				+ advertiser_name + ", currency=" + currency + "]";
	}

}
