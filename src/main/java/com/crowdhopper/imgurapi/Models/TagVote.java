package com.crowdhopper.imgurapi.Models;

import org.json.JSONObject;

public class TagVote implements Model {
	private int ups;
	private int downs;
	private String name;
	private String author;
	private JSONObject data;
	
	public TagVote(Basic<JSONObject> base) {
		this.populate(base);
	}
	
	@Override
	public void populate(Basic<JSONObject> base) {
		data = base.getData();
		ups = data.getInt("ups");
		downs = data.getInt("downs");
		name = data.getString("name");
		author = data.getString("author");
	}

	@Override
	public String toJson() {
		return data.toString();
	}
	
	public int getUpVotes() {return ups;}
	public int getDownVotes() {return downs;}
	public String getName() {return name;}
	public String getAuthor() {return author;}
}
