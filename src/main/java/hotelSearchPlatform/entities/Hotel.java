package hotelSearchPlatform.entities;

public class Hotel {
	
	private int id,city_id,clicks,impressions, rating, stars;
	private String name;
	
	public Hotel(int id, int city_id, int clicks, int impressions, int rating, int stars, String name) {
		super();
		this.id = id;
		this.city_id = city_id;
		this.clicks = clicks;
		this.impressions = impressions;
		this.rating = rating;
		this.stars = stars;
		this.name = name;
	}

	public Hotel() {
	}

	public int getId() {
		return id;
	}

	public int getCity_id() {
		return city_id;
	}

	public int getClicks() {
		return clicks;
	}

	public int getImpressions() {
		return impressions;
	}

	public int getRating() {
		return rating;
	}

	public int getStars() {
		return stars;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Hotel [id=" + id + ", city_id=" + city_id + ", rating=" + rating+ "]";
	}
}
