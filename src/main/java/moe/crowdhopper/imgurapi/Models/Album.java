package moe.crowdhopper.imgurapi.Models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.ArrayList;

public class Album extends Model {
	private String id;
	private String title;
	private String descript = null;
	private int datetime; // Time inserted into the gallery
	private String cover;
	private int coverw; // The width of the cover image.
	private int coverh; // The height of the cover image
	private String acc_url; // The account username, null if anonymously
							// uploaded.
	private Integer acc_id = null; // The account id.
	private String privacy = null; // The privacy level of the album
	private String layout = null;
	private int views;
	private String link;
	private boolean favorite;
	private boolean nsfw;
	private String section = null;
	private int order = 0;
	private String deletehash = null; // Only gotten if logged in as the
										// uploader
	private int image_num;
	private List<Image> images = null;
	private boolean in_gallery = true;
	protected JSONObject data;

	public Album(JSONObject data) {
		this.data = data;
		this.populate();
	}
	
	//Only to be used in conjunction with the factory method.
	public Album() {}

	@Override
	public void populate() {
		id = data.getString("id");
		title = data.optString("title");
		if (!data.isNull("description"))
			descript = data.getString("description");
		datetime = data.getInt("datetime");
		cover = data.optString("cover");
		coverw = data.optInt("cover_width");
		coverh = data.optInt("cover_height");
		acc_url = data.optString("account_url");
		if (acc_url.equals("null"))
			acc_url = null;
		if (!data.isNull("account_id"))
			acc_id = data.getInt("account_id");
		if (!data.isNull("privacy"))
			privacy = data.getString("privacy");
		if (!data.isNull("layout"))
			layout = data.getString("layout");
		views = data.getInt("views");
		link = data.getString("link");
		favorite = data.optBoolean("favorite");
		nsfw = data.optBoolean("nsfw");
		if (data.has("section") && !data.isNull("section"))
			section = data.getString("section");
		if (data.has("order"))
			order = data.getInt("order");
		if (data.has("deletehash"))
			deletehash = data.getString("deletehash");
		image_num = data.getInt("images_count");
		if (data.has("in_gallery"))
			in_gallery = data.getBoolean("in_gallery");
		if (data.has("images")) {
			images = new ArrayList<Image>();
			JSONArray rawData = new JSONArray(data.optString("images"));
			for (int i = 0; i < rawData.length(); i++)
				images.add(new Image(rawData.getJSONObject(i)));
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

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return descript;
	}

	public int getTime() {
		return datetime;
	}

	public String getCover() {
		return cover;
	}

	public int getCoverWidth() {
		return coverw;
	}

	public int getCoverHeight() {
		return coverh;
	}

	public String getAccountUrl() {
		return acc_url;
	}

	public int getAccountId() {
		return acc_id;
	}

	public String getPrivacy() {
		return privacy;
	}

	public String getLayout() {
		return layout;
	}

	public int getViews() {
		return views;
	}

	public String getLink() {
		return link;
	}

	public boolean isFavorite() {
		return favorite;
	}

	public boolean isNSFW() {
		return nsfw;
	}

	public String getSection() {
		return section;
	}

	public int getOrder() {
		return order;
	}

	public String getDeleteHash() {
		return deletehash;
	}

	public int getNumImages() {
		return image_num;
	}

	public boolean inGallery() {
		return in_gallery;
	}

	public List<Image> getImages() {
		return images;
	}
}
