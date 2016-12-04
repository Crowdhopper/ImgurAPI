package moe.crowdhopper.imgurapi.Models;

import java.util.List;
import java.util.ArrayList;
import org.json.JSONObject;
import org.json.JSONArray;

public class Tag extends Model {
	private String name; // Name of the tag
	private int followers; // Number of people following the tag
	private int total_items; // Number of gallery items with the tag
	private Boolean following = null; // Whether the current user is following
										// the tag or not
	List<Gallery> items; // All gallery items with that tag
	JSONObject data;

	public Tag(JSONObject data) {
		this.data = data;
		populate();
	}
	
	//Only to be used in conjunction with the factory method.
	public Tag() {}

	@Override
	public void populate() {
		name = data.getString("name");
		followers = data.getInt("followers");
		total_items = data.getInt("total_items");
		if (data.has("following"))
			following = data.getBoolean("following");
		items = new ArrayList<Gallery>();
		JSONArray rawData = data.getJSONArray("items");
		for (int i = 0; i < rawData.length(); i++) {
			JSONObject item = rawData.getJSONObject(i);
			if (item.getBoolean("is_album"))
				items.add(new GalleryAlbum(item));
			else
				items.add(new GalleryImage(item));
		}
	}

	@Override
	public String toString() {
		return data.toString();
	}

	@Override
	public void factory(JSONObject data) {
		this.data = data;
		populate();
	}

	public String getName() {
		return name;
	}

	public int getNumFollowers() {
		return followers;
	}

	public int getTotalItems() {
		return total_items;
	}

	public boolean isFollowing() {
		return following;
	}

	public List<Gallery> getItems() {
		return items;
	}
}
