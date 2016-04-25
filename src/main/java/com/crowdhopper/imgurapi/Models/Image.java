package com.crowdhopper.imgurapi.Models;

import org.json.JSONObject;

public class Image implements Model {
	private String id;
	private String title;
	private String descript;
	private int datetime;
	private String type;
	private boolean animated;
	private int width;
	private int height;
	private int size;
	private int views;
	private int bandwidth;
	private String deletehash = null;
	private String name = null;
	private String section;
	private String link;
	private String gifv = null;
	private String mp4 = null;
	private String webm = null;
	private int mp4_size = 0;
	private int webm_size = 0;
	private Boolean looping = null;
	private boolean favorite;
	private Boolean nsfw = null;
	private String vote;
	private boolean in_gallery = true;
	protected JSONObject data;
	
	protected Image() {}
	
	public Image(Basic<JSONObject> base) {
		this.populate(base);
	}
	
	public Image(JSONObject data) {
		Basic<JSONObject> base = new Basic<JSONObject>();
		base.setData(data);
		this.populate(base);
	}

	@Override
	public void populate(Basic<JSONObject> base) {
		data = base.getData();
		id = data.getString("id");
		title = data.getString("title");
		descript = data.getString("description");
		datetime = data.getInt("datetime");
		type = data.getString("type");
		animated = data.getBoolean("animated");
		width = data.getInt("width");
		height = data.getInt("height");
		size = data.getInt("size");
		views = data.getInt("views");
		bandwidth = data.getInt("bandwidth");
		if(data.has("deletehash"))
			deletehash = data.getString("deletehash");
		if(data.has("name"))
			name = data.getString("name");
		section = data.getString("section");
		link = data.getString("link");
		if(animated && type.equals("image/gif")) {
			gifv = data.getString("gifv");
			mp4 = data.getString("mp4");
			webm = data.getString("webm");
			mp4_size = data.getInt("mp4_size");
			webm_size = data.getInt("webm_size");
			looping = data.getBoolean("looping");
		}
		favorite = data.getBoolean("favorite");
		if(!data.getString("nsfw").equals("null"))
			nsfw = data.getBoolean("nsfw");
		vote = data.getString("vote");
		if(vote.equals("null"))
			vote = null;
		if(data.has("in_gallery"))
			in_gallery = data.getBoolean("in_gallery");
	}

	@Override
	public String toJson() {
		return data.toString();
	}
	
	public String getId() {return id;}
	public String getTitle() {return title;}
	public String getDescription() {return descript;}
	public int getTime() {return datetime;}
	public String getType() {return type;}
	public boolean isAnimated() {return animated;}
	public int getHeight() {return height;}
	public int getWidth() {return width;}
	public int getSize() {return size;}
	public int getViews() {return views;}
	public int getBandwidth() {return bandwidth;}
	public String getDeleteHash() {return deletehash;}
	public String getName() {return name;}
	public String getSection() {return section;}
	public String getLink() {return link;}
	public String getGifv() {return gifv;}
	public String getWebm() {return webm;}
	public String getMp4() {return mp4;}
	public int getMp4Size() {return mp4_size;}
	public int getWebmSize() {return webm_size;}
	public boolean isLooping() {return looping;}
	public boolean isFavorite() {return favorite;}
	public boolean isNsfw() {return nsfw;}
	public String getVote() {return vote;}
	public boolean inGallery() {return in_gallery;}
	public String getThumbnail(char size) {
		String[] split = link.split(".");
		return split[0] + split[1] + split[2] + size + split[3];
	}
}
