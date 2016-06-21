package com.crowdhopper.imgurapi.Models;

import org.json.JSONObject;

public class Image extends Model {
	private String id;
	private String title;
	private String descript;
	private int datetime; // Upload time
	private String type; // The MIME type of the image, e.g. image/gif or
							// image/jpg
	private boolean animated;
	private int width;
	private int height;
	private int size; // The size of the image in bytes
	private int views;
	private int bandwidth; // The bandwidth consumed by the image in bytes
	private String deletehash = null;
	private String name = null; // The original filename, only given if logged
								// in as the uploader
	private String section;
	private String link;
	private String gifv = null; // Links to the video formats, only available if
	private String mp4 = null; // type is "image/gif" and animated is true
	private String webm = null;
	private int mp4_size = 0; // The size of the video formats
	private int webm_size = 0; // Zero if they haven't been created or can't
								// exist
	private Boolean looping = null;
	private boolean favorite;
	private Boolean nsfw = null;
	private String vote;
	private boolean in_gallery = true;
	protected JSONObject data;

	public Image(JSONObject data) {
		this.data = data;
		this.populate();
	}
	
	//Only to be used in conjunction with the factory method.
	public Image() {}

	@Override
	public void populate() {
		id = data.getString("id");
		title = data.optString("title");
		descript = data.optString("description");
		datetime = data.getInt("datetime");
		type = data.getString("type");
		animated = data.getBoolean("animated");
		width = data.getInt("width");
		height = data.getInt("height");
		size = data.getInt("size");
		views = data.getInt("views");
		bandwidth = data.getInt("bandwidth");
		if (data.has("deletehash"))
			deletehash = data.getString("deletehash");
		if (data.has("name"))
			name = data.getString("name");
		section = data.optString("section");
		if (section.equals("null"))
			section = null;
		link = data.optString("link");
		if (animated && type.equals("image/gif")) {
			gifv = data.getString("gifv");
			mp4 = data.getString("mp4");
			webm = data.optString("webm");
			mp4_size = data.getInt("mp4_size");
			webm_size = data.optInt("webm_size");
			looping = data.getBoolean("looping");
		}
		favorite = data.getBoolean("favorite");
		if (!data.isNull("nsfw"))
			nsfw = data.optBoolean("nsfw");
		vote = data.optString("vote");
		if (vote.equals("null"))
			vote = null;
		if (data.has("in_gallery"))
			in_gallery = data.getBoolean("in_gallery");
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

	public String getType() {
		return type;
	}

	public boolean isAnimated() {
		return animated;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public int getSize() {
		return size;
	}

	public int getViews() {
		return views;
	}

	public int getBandwidth() {
		return bandwidth;
	}

	public String getDeleteHash() {
		return deletehash;
	}

	public String getName() {
		return name;
	}

	public String getSection() {
		return section;
	}

	public String getLink() {
		return link;
	}

	public String getGifv() {
		return gifv;
	}

	public String getWebm() {
		return webm;
	}

	public String getMp4() {
		return mp4;
	}

	public int getMp4Size() {
		return mp4_size;
	}

	public int getWebmSize() {
		return webm_size;
	}

	public boolean isLooping() {
		return looping;
	}

	public boolean isFavorite() {
		return favorite;
	}

	public boolean isNsfw() {
		return nsfw;
	}

	public String getVote() {
		return vote;
	}

	public boolean inGallery() {
		return in_gallery;
	}

	public String getThumbnail(char size) {
		String[] split = link.split(".");
		return split[0] + split[1] + split[2] + size + split[3];
	}
}
