package moe.crowdhopper.imgurapi.Models;

import org.json.JSONObject;

public class Vote extends Model {
	private int ups;
	private int downs;
	JSONObject data;

	public Vote(JSONObject data) {
		this.data = data;
		populate();
	}
	
	//Only to be used in conjunction with the factory method.
	public Vote() {}

	@Override
	public void populate() {
		ups = data.getInt("ups");
		downs = data.getInt("downs");
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
}
