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

	public GalleryAlbum(JSONObject data) {
		super(data);
	}
	
	//Only to be used in conjunction with the factory method.
	public GalleryAlbum() {}

	@Override
	public void populate() {
		super.populate();
		ups = data.getInt("ups");
		downs = data.getInt("downs");
		points = data.getInt("points");
		score = data.optInt("score");
		topic = data.optString("topic");
		topic_id = data.optInt("topic_id");
		comment_count = data.optInt("comment_count");
		if (data.has("meme_metadata"))
			meme = new MemeMetadata(data.getJSONObject("meme_metadata"));
	}

	@Override
	public void factory(JSONObject data) {
		super.factory(data);
	}

	public int getUpVotes() {
		return ups;
	}

	public int getDownVotes() {
		return downs;
	}

	public int getPoints() {
		return points;
	}

	public int getScore() {
		return score;
	}

	public String getTopic() {
		return topic;
	}

	public int getTopicID() {
		return topic_id;
	}

	public int getCommentCount() {
		return comment_count;
	}

	public MemeMetadata getMemeMetadata() {
		return meme;
	}

	@Override
	public boolean isAlbum() {
		return true;
	}
}
