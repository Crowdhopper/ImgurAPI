package com.crowdhopper.imgurapi.Models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.ArrayList;

public class Comment extends Model {
	private int id;
	private String image_id; // The id of the image commented on
	private String content; // The comment itself
	private String author;
	private int author_id;
	private boolean on_album; // If the comment was on an album or not.
	private String album_cover = null; // The id of the album cover image, if it
										// was on an album
	private int ups;
	private int downs;
	private double points;
	private int datetime; // The time the comment was created
	private int parent_id; // The comment id of the comment this comment was in
							// reply to, if it was a reply.
	private boolean deleted;
	private String vote; // The user's current vote on the comment, null if not
							// signed in or the user hasn't voted.
	private List<Comment> children; // All of the replies to this comment.
	private JSONObject data;

	public Comment(JSONObject data) {
		this.data = data;
		populate();
	}
	
	//Only to be used in conjunction with the factory method.
	public Comment() {}

	@Override
	public void populate() {
		id = data.getInt("id");
		image_id = data.getString("image_id");
		content = data.getString("comment");
		author = data.getString("author");
		author_id = data.getInt("author_id");
		on_album = data.getBoolean("on_album");
		if (on_album)
			album_cover = data.getString("album_cover");
		ups = data.getInt("ups");
		downs = data.getInt("downs");
		points = data.getDouble("points");
		datetime = data.getInt("datetime");
		if (data.isNull("parent_id"))
			parent_id = 0;
		else
			parent_id = data.getInt("parent_id");
		deleted = data.getBoolean("deleted");
		vote = data.optString("vote");
		if (vote.equals("null"))
			vote = null;
		children = new ArrayList<Comment>();
		JSONArray rawData = new JSONArray(data.optString("children"));
		for (int i = 0; i < rawData.length(); i++)
			children.add(new Comment(rawData.getJSONObject(i)));
	}

	@Override
	public String toString() {
		return data.toString();
	}

	@Override
	public void factory(JSONObject data) {
		this.data = data;
		populate();
	}

	public int getId() {
		return id;
	}

	public String getImageId() {
		return image_id;
	}

	public String getContent() {
		return content;
	}

	public String getAuthor() {
		return author;
	}

	public int getAuthorId() {
		return author_id;
	}

	public boolean onAlbum() {
		return on_album;
	}

	public String getAlbumCover() {
		return album_cover;
	}

	public int getUpCount() {
		return ups;
	}

	public int getDownCount() {
		return downs;
	}

	public double getPoints() {
		return points;
	}

	public int getTime() {
		return datetime;
	}

	public int getParentId() {
		return parent_id;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public String getVote() {
		return vote;
	}

	public List<Comment> getChildren() {
		return children;
	}

}
