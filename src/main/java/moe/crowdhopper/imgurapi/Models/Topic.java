package moe.crowdhopper.imgurapi.Models;

import org.json.JSONException;
import org.json.JSONObject;

public class Topic extends Model {
	private int id;
	private String name;
	private String descript;
	private String css; // CSS class used on website to display the topic
	private boolean ephemeral; // Indicates whether it is an ephemeral topic,
								// e.g. current events
	private Gallery top_post; // The top post in this topic
	private Image hero_image; // The hero image chosen by the Imgur staff
	private boolean is_hero; // Whether the topic's hero_image should be used as
								// the overall hero image.
	private JSONObject data;

	public Topic(JSONObject data) {
		this.data = data;
		populate();
	}
	
	//Only to be used in conjunction with the factory method.
	public Topic() {}

	@Override
	public void populate() {
		id = data.getInt("id");
		name = data.getString("name");
		descript = data.getString("description");
		css = data.optString("css");
		ephemeral = data.getBoolean("ephemeral");
		is_hero = data.getBoolean("isHero");
		try {
			hero_image = new Image(data.getJSONObject("heroImage"));
		} catch (JSONException e) {}
		JSONObject raw_data = data.optJSONObject("topPost");
		if(raw_data == null)
			return;
		if (raw_data.getBoolean("is_album"))
			top_post = new GalleryAlbum(raw_data);
		else
			top_post = new GalleryImage(raw_data);
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

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return descript;
	}

	public String getCss() {
		return css;
	}

	public boolean isEphemeral() {
		return ephemeral;
	}

	public Gallery getTopPost() {
		return top_post;
	}

	public Image getHeroImage() {
		return hero_image;
	}

	public boolean isHero() {
		return is_hero;
	}
}
