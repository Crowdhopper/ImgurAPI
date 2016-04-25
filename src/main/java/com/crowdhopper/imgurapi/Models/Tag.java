package com.crowdhopper.imgurapi.Models;

import java.util.List;
import java.util.ArrayList;
import org.json.JSONObject;
import org.json.JSONArray;

public class Tag implements Model {
	private String name;
	private int followers;
	private int total_items;
	private Boolean following = null;
	List<Gallery> items;
	JSONObject data;

	public Tag(Basic<JSONObject> base) {
		this.populate(base);
	}
	
	@Override
	public void populate(Basic<JSONObject> base) {
		data = base.getData();
		name = data.getString("name");
		followers = data.getInt("followers");
		total_items = data.getInt("total_items");
		if(data.has("following"))
			following = data.getBoolean("following");
		items = new ArrayList<Gallery>();
		JSONArray rawData = data.getJSONArray("items");
		for(int i = 0; i < rawData.length(); i++) {
			JSONObject item = rawData.getJSONObject(i);
			if(item.getBoolean("is_album"))
				items.add(new GalleryAlbum(item));
			else
				items.add(new GalleryImage(item));
		}
	}

	@Override
	public String toJson() {
		return data.toString();
	}
	
	public String getName() {return name;}
	public int getNumFollowers() {return followers;}
	public int getTotalItems() {return total_items;}
	public boolean isFollowing() {return following;}
	public List<Gallery> getItems() {return items;}
}
