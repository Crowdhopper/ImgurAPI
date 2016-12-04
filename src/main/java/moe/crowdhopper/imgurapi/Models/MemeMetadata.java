package moe.crowdhopper.imgurapi.Models;

import org.json.JSONObject;

public class MemeMetadata {
	private String meme_name; // The name of the meme, e.g. "Confession Bear"
	private String top_text;
	private String bottom_text;
	private String bg_image; // Image id of the background image

	MemeMetadata(JSONObject data) {
		meme_name = data.getString("meme_name");
		top_text = data.getString("top_text");
		bottom_text = data.getString("bottom_text");
		bg_image = data.getString("bg_image");
	}

	public String getName() {
		return meme_name;
	}

	public String getTopText() {
		return top_text;
	}

	public String getBottomText() {
		return bottom_text;
	}

	public String getBgImage() {
		return bg_image;
	}
}
