package com.crowdhopper.imgurapi.Models;

import org.json.JSONObject;
import java.util.List;
import java.util.ArrayList;

public class Comment implements Model {
	private int id;
	private String image_id;
	private String content;
	private String author;
	private int author_id;
	private boolean on_album;
	private String album_cover;
	private int ups;
	private int downs;
	private double points;
	private int datetime;
	private int parent_id;
	private boolean deleted;
	private String vote;
	private List<Comment> children;
	private JSONObject data;

	public Comment(Basic<JSONObject> base) {
		this.populate(base);
	}
	
	public Comment(JSONObject data) {
		Basic<JSONObject> base = new Basic<JSONObject>();
		base.setData(data);
		this.populate(base);
	}
	
	@Override
	public void populate(Basic<JSONObject> base) {
		data = base.getData();
		id = data.getInt("id");
		image_id = data.getString("image_id");
		content = data.getString("comment");
		author = data.getString("author");
		author_id = data.getInt("author_id");
		on_album = data.getBoolean("on_album");
		if(on_album)
			album_cover = data.getString("album_cover");
		else
			album_cover = null;
		ups = data.getInt("ups");
		downs = data.getInt("downs");
		points = data.getDouble("points");
		datetime = data.getInt("datetime");
		if(data.getString("parent_id").equals("null"))
			parent_id = 0;
		else
			parent_id = data.getInt("parent_id");
		deleted = data.getBoolean("deleted");
		vote = data.getString("vote");
		if(vote.equals("null"))
			vote = null;
	}

	@Override
	public String toJson() {
		return null;
	}

}
