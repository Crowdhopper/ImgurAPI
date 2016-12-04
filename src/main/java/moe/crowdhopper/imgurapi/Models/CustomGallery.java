package moe.crowdhopper.imgurapi.Models;

import org.json.JSONObject;
import org.json.JSONArray;
import java.util.List;
import java.util.ArrayList;

public class CustomGallery extends Model {
	private String account_url; // Username of the creator of the gallery
	private String link;
	private List<String> tags;
	private int item_count;
	private List<Gallery> items;
	private JSONObject data;

	public CustomGallery(JSONObject data) {
		this.data = data;
		populate();
	}
	
	//Only to be used in conjunction with the factory method.
	public CustomGallery() {}

	@Override
	public void populate() {
		account_url = data.getString("account_url");
		link = data.getString("link");
		item_count = data.getInt("item_count");
		tags = new ArrayList<String>();
		String rawString = data.optString("tags");
		rawString.substring(1);
		rawString.substring(0, rawString.length() - 1);
		for (String s : rawString.split(","))
			tags.add(s);
		items = new ArrayList<Gallery>();
		JSONArray rawData = data.getJSONArray("items");
		for (int i = 0; i < rawData.length(); i++) {
			JSONObject object = rawData.getJSONObject(i);
			if (object.getBoolean("is_album"))
				items.add(new GalleryAlbum(object));
			else
				items.add(new GalleryImage(object));
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

	public String getUser() {
		return account_url;
	}

	public String getLink() {
		return link;
	}

	public int getItemCount() {
		return item_count;
	}

	public List<String> getTags() {
		return tags;
	}

	public List<Gallery> getItems() {
		return items;
	}
}
