package com.crowdhopper.imgurapi.Models;

import org.json.JSONObject;

public class Topic implements Model {
	private int id;
	private String name;
	private String descript;
	private String css;
	private boolean ephemeral;
	private Gallery top_post;
	private Image hero_image;
	private boolean is_hero;
	private JSONObject data;

	@Override
	public void populate(Basic<JSONObject> base) {
		data = base.getData();
		id = data.getInt("id");
		name = data.getString("name");
		descript = data.getString("description");
		css = data.getString("css");
		ephemeral = data.getBoolean("ephemeral");
		is_hero = data.getBoolean("isHero");
		hero_image = new Image(data.getJSONObject("heroImage"));
		JSONObject rawData = data.getJSONObject("topPost");
		if(rawData.getBoolean("is_album"))
			top_post = new GalleryAlbum(rawData);
		else
			top_post = new GalleryImage(rawData);
	}

	@Override
	public String toJson() {
		return data.toString();
	}
	
	public int getId() {return id;}
	public String getName() {return name;}
	public String getDescription() {return descript;}
	public String getCss() {return css;}
	public boolean isEphemeral() {return ephemeral;}
	public Gallery getTopPost() {return top_post;}
	public Image getHeroImage() {return hero_image;}
	public boolean isHero() {return is_hero;}
}
