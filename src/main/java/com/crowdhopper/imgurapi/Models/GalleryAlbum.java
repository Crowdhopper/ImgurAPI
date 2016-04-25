package com.crowdhopper.imgurapi.Models;

import org.json.JSONObject;

public class GalleryAlbum extends Album implements Gallery {
	private int ups;
	private int downs;
	private int points;
	private int score;
	private String topic;
	private int topic_id;
	private int comment_count;
	private MemeMetadata meme = null;
	
	public GalleryAlbum(Basic<JSONObject> base) {
		this.populate(base);
	}
	
	public GalleryAlbum(JSONObject data) {
		Basic<JSONObject> base = new Basic<JSONObject>();
		base.setData(data);
		this.populate(base);
	}

	@Override
	public void populate(Basic<JSONObject> base) {
		super.populate(base);
		ups = data.getInt("ups");
		downs = data.getInt("downs");
		points = data.getInt("points");
		score = data.getInt("score");
		topic = data.getString("topic");
		topic_id = data.getInt("topic_id");
		comment_count = data.getInt("comment_count");
		if(data.has("meme_metadata"))
			meme = new MemeMetadata(data.getJSONObject("meme_metadata"));
	}

	public int getUpVotes() {return ups;}
	public int getDownVotes() {return downs;}
	public int getPoints() {return points;}
	public int getScore() {return score;}
	public String getTopic() {return topic;}
	public int getTopicID() {return topic_id;}
	public int getCommentCount() {return comment_count;}
	public MemeMetadata getMemeMetadata() {return meme;}
}
