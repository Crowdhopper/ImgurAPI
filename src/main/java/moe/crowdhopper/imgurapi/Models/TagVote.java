package moe.crowdhopper.imgurapi.Models;

import org.json.JSONObject;

public class TagVote extends Model {
	private int ups;
	private int downs;
	private String name; // Name of the tag
	private String author; // Author of the tag
	private JSONObject data;

	public TagVote(JSONObject data) {
		this.data = data;
		populate();
	}
	
	//Only to be used in conjunction with the factory method.
	public TagVote() {}

	@Override
	public void populate() {
		ups = data.getInt("ups");
		downs = data.getInt("downs");
		name = data.getString("name");
		author = data.getString("author");
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

	public int getUpVotes() {
		return ups;
	}

	public int getDownVotes() {
		return downs;
	}

	public String getName() {
		return name;
	}

	public String getAuthor() {
		return author;
	}
}
