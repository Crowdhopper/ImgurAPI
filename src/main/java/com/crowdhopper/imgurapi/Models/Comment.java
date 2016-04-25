package com.crowdhopper.imgurapi.Models;

import org.json.JSONArray;
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
		children = new ArrayList<Comment>();
		JSONArray rawData = new JSONArray(data.getString("children"));
		for(int i = 0; i < rawData.length(); i++)
			children.add(new Comment(rawData.getJSONObject(i)));
	}

	@Override
	public String toJson() {
		return data.toString();
	}
	
	public int getId() {return id;}
	public String getImageId() {return image_id;}
	public String getContent() {return content;}
	public String getAuthor() {return author;}
	public int getAuthorId() {return author_id;}
	public boolean onAlbum() {return on_album;}
	public String getAlbumCover() {return album_cover;}
	public int getUpCount() {return ups;}
	public int getDownCount() {return downs;}
	public double getPoints() {return points;}
	public int getTime() {return datetime;}
	public int getParentId() {return parent_id;}
	public boolean isDeleted() {return deleted;}
	public String getVote() {return vote;}
	public List<Comment> getChildren() {return children;}

}
