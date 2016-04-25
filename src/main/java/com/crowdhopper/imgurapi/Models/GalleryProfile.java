package com.crowdhopper.imgurapi.Models;

import org.json.JSONObject;
import org.json.JSONArray;
import java.util.List;
import java.util.ArrayList;

public class GalleryProfile implements Model {
	private int comment_num;
	private int favorite_num;
	private int submission_num;
	private List<Trophy> trophies;
	private JSONObject data;

	public GalleryProfile(Basic<JSONObject> base) {
		this.populate(base);
	}
	
	@Override
	public void populate(Basic<JSONObject> base) {
		data = base.getData();
		comment_num = data.getInt("total_gallery_comments");
		favorite_num = data.getInt("total_gallery_favorites");
		submission_num = data.getInt("total_gallery_submissions");
		trophies = new ArrayList<Trophy>();
		JSONArray rawData = data.getJSONArray("trophies");
		for(int i = 0; i < rawData.length(); i++) {
			trophies.add(new Trophy(rawData.getJSONObject(i)));
		}
	}

	@Override
	public String toJson() {
		return data.toString();
	}
	
	public int getNumComments() {return comment_num;}
	public int getNumFavorites() {return favorite_num;}
	public int getNumSubmissions() {return submission_num;}
	public List<Trophy> getTrophies() {return trophies;}
	
	class Trophy {
		private int id;
		private String name;
		private String name_clean;
		private String descript;
		private String data;
		private String data_link;
		private int datetime;
		private String image;
		
		Trophy(JSONObject data) {
			id = data.getInt("id");
			name = data.getString("name");
			name_clean = data.getString("name_clean");
			descript = data.getString("description");
			this.data = data.getString("data");
			if(this.data.equals("null"))
				this.data = null;
			data_link = data.getString("data_link");
			if(data_link.equals("null"))
				data_link = null;
			datetime = data.getInt("datetime");
			image = data.getString("image");
		}
		
		public int getId() {return id;}
		public String getName() {return name;}
		public String getNameClean() {return name_clean;}
		public String getDescription() {return descript;}
		public String getData() {return data;}
		public String getDataLink() {return data_link;}
		public int getTimeReceived() {return datetime;}
		public String getImage() {return image;}
	}
}
