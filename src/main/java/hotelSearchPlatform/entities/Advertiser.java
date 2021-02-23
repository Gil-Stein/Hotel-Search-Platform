package hotelSearchPlatform.entities;

public class Advertiser {
	
	private int id;
	private String advertiser_name;
	public Advertiser(int id, String advertiser_name) {
		super();
		this.id = id;
		this.advertiser_name = advertiser_name;
	}
	public int getId() {
		return id;
	}
	
	public String getAdvertiser_name() {
		return advertiser_name;
	}
	
	@Override
	public String toString() {
		return "Advertiser [id=" + id + ", name=" + advertiser_name + "]";
	}
	
	
}
