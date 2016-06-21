package com.crowdhopper.imgurapi.Models;

import org.json.JSONObject;

public class GalleryImage extends Image implements Gallery {
	private int comment_count;
	private String topic = null;
	private int topic_id = 0;
	private int ups;
	private int downs;
	private int points;
	private Integer score = null;
	private String reddit_comments = null; // If the image was from a subreddit,
											// a link to the subreddit comments.
	private MemeMetadata meme = null;

	public GalleryImage(JSONObject data) {
		super(data);
	}
	
	//Only to be used in conjunction with the factory method.
	public GalleryImage() {}

	@Override
	public void populate() {
		super.populate();
		comment_count = data.optInt("comment_count");
		if (data.has("topic")) {
			topic = data.optString("topic");
			topic_id = data.optInt("topic_id");
		}
		ups = data.optInt("ups");
		downs = data.optInt("downs");
		points = data.optInt("points");
		score = data.optInt("score");
		if (data.has("reddit_comments"))
			reddit_comments = data.getString("reddit_comments");
		if (data.has("meme_metadata"))
			meme = new MemeMetadata(data.getJSONObject("meme_metadata"));
	}

	@Override
	public void factory(JSONObject data) {
		super.factory(data);
	}

	public int getCommentCount() {
		return comment_count;
	}

	public String getTopic() {
		return topic;
	}

	public int getTopicId() {
		return topic_id;
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

	public String getRedditComments() {
		return reddit_comments;
	}

	public MemeMetadata getMemeMetadata() {
		return meme;
	}

	@Override
	public boolean isAlbum() {
		return false;
	}
}
