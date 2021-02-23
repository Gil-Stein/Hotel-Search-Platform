package hotelSearchPlatform.entities;

public class City {

	private int id;
	private String City;
	
	
	public City(int id, String city) {
		super();
		this.id = id;
		City = city;
	}

	public String getCity() {
		return City;
	}

	public int getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return "City [id=" + id + ", " + City + "]";
	}
}
