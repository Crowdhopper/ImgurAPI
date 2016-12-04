package moe.crowdhopper.imgurapi.Models;

import org.json.JSONObject;
import org.json.JSONArray;
import java.util.List;
import java.util.ArrayList;

//Describes a given user's usage statistics on the gallery
public class GalleryProfile extends Model {
	private int comment_num;
	private int favorite_num;
	private int submission_num;
	private List<Trophy> trophies;
	private JSONObject data;

	public GalleryProfile(JSONObject data) {
		this.data = data;
		populate();
	}
	
	//Only to be used in conjunction with the factory method.
	public GalleryProfile() {}

	@Override
	public void populate() {
		comment_num = data.getInt("total_gallery_comments");
		favorite_num = data.getInt("total_gallery_favorites");
		submission_num = data.getInt("total_gallery_submissions");
		trophies = new ArrayList<Trophy>();
		JSONArray rawData = data.getJSONArray("trophies");
		for (int i = 0; i < rawData.length(); i++) {
			trophies.add(new Trophy(rawData.getJSONObject(i)));
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

	public int getNumComments() {
		return comment_num;
	}

	public int getNumFavorites() {
		return favorite_num;
	}

	public int getNumSubmissions() {
		return submission_num;
	}

	public List<Trophy> getTrophies() {
		return trophies;
	}

	public class Trophy {
		private int id;
		private String name;
		private String name_clean;
		private String descript;
		private String data;
		private String data_link;
		private int datetime;
		private String image;

		Trophy(JSONObject data) {
			id = data.optInt("id");
			name = data.optString("name");
			name_clean = data.optString("name_clean");
			descript = data.optString("description");
			this.data = data.optString("data");
			if (this.data.equals("null"))
				this.data = null;
			data_link = data.optString("data_link");
			if (data_link.equals("null"))
				data_link = null;
			datetime = data.optInt("datetime");
			image = data.optString("image");
		}

		public int getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public String getNameClean() {
			return name_clean;
		}

		public String getDescription() {
			return descript;
		}

		public String getData() {
			return data;
		}

		public String getDataLink() {
			return data_link;
		}

		public int getTimeReceived() {
			return datetime;
		}

		public String getImage() {
			return image;
		}
	}
}
