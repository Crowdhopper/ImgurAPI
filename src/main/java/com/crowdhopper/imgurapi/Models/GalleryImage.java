package com.crowdhopper.imgurapi.Models;

import org.json.JSONObject;

public class GalleryImage extends Image implements Gallery {
	private int comment_count;
	private String topic;
	private int topic_id;
	private int ups;
	private int downs;
	private int points;
	private int score;
	private String reddit_comments = null;
	private MemeMetadata meme = null;

	public GalleryImage(Basic<JSONObject> base) {
		this.populate(base);
	}

	public GalleryImage(JSONObject data) {
		Basic<JSONObject> base = new Basic<JSONObject>();
		base.setData(data);
		this.populate(base);
	}
	
	public void populate(Basic<JSONObject> base) {
		super.populate(base);
		comment_count = data.getInt("comment_count");
		topic = data.getString("topic");
		topic_id = data.getInt("topic_id");
		ups = data.getInt("ups");
		downs = data.getInt("downs");
		points = data.getInt("points");
		score = data.getInt("score");
		if(data.has("reddit_comments"))
			reddit_comments = data.getString("reddit_comments");
		if(data.has("meme_metadata"))
			meme = new MemeMetadata(data.getJSONObject("meme_metadata"));
	}
	
	public int getCommentCount() {return comment_count;}
	public String getTopic() {return topic;}
	public int getTopicId() {return topic_id;}
	public int getUpVotes() {return ups;}
	public int getDownVotes() {return downs;}
	public int getPoints() {return points;}
	public int getScore() {return score;}
	public String getRedditComments() {return reddit_comments;}
	public MemeMetadata getMemeMetadata() {return meme;}
}
