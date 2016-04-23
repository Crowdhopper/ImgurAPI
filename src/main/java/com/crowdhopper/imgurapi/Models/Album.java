package com.crowdhopper.imgurapi.Models;

import org.json.JSONObject;
import java.util.List;
import java.util.ArrayList;

public class Album implements Model {
	private String id;
	private String title;
	private String descript;
	private int datetime;
	private String cover;
	private int coverw; //The width of the cover image.
	private int coverh; //The height of the cover image
	private String acc_url;
	private int acc_id;
	private String privacy;
	private String layout;
	private int views;
	private String link;
	private boolean favorite;
	private boolean nsfw;
	private String section;
	private int order;
	private String deletehash;
	private int image_num;
	private List<Image> images;
	private boolean in_gallery;
	private JSONObject data;
	
	public Album(Basic<JSONObject> base) {
		this.populate(base);
	}
	
	@Override
	public void populate(Basic<JSONObject> base) {
		data = base.getData();
		id = data.getString("id");
		title = data.getString("title");
		descript = data.getString("description");
		datetime = data.getInt("datetime");
		cover = data.getString("cover");
		coverw = data.getInt("cover_width");
		coverh = data.getInt("cover_height");
		acc_url = data.getString("account_url");
		acc_id = data.getInt("account_id");
		privacy = data.getString("privacy");
		layout = data.getString("layout");
		views = data.getInt("views");
		link = data.getString("link");
		favorite = data.getBoolean("favorite");
		nsfw = data.getBoolean("nsfw");
		section = data.getString("section");
		order = data.getInt("order");
		if(data.has("deletehash"))
			deletehash = data.getString("deletehash");
		else
			deletehash = null;
		image_num = data.getInt("images_count");
		in_gallery = data.getBoolean("in_gallery");
		images = new ArrayList<Image>();
		String rawString = data.getString("images");
		rawString = rawString.substring(0, rawString.length() - 1).substring(1);
		for(String s: rawString.split(",")) 
			images.add(new Image(new JSONObject(s)));
	} 

	@Override
	public String toJson() {
		return data.toString();
	}
	
	public String getId() {return id;}
	public String getTitle() {return title;}
	public String getDescription() {return descript;}
	public int getTime() {return datetime;}
	public String getCover() {return cover;}
	public int getCoverWidth() {return coverw;}
	public int getCoverHeight() {return coverh;}
	public String getAccountUrl() {return acc_url;}
	public int getAccountId() {return acc_id;}
	public String getPrivacy() {return privacy;}
	public String getLayout() {return layout;}
	public int getViews() {return views;}
	public String getLink() {return link;}
	public boolean isFavorite() {return favorite;}
	public boolean isNSFW() {return nsfw;}
	public String getSection() {return section;}
	public int getOrder() {return order;}
	public String getDeleteHash() {return deletehash;}
	public int getNumImages() {return image_num;}
	public boolean inGallery() {return in_gallery;}
	public List<Image> getImages() {return images;}

}
