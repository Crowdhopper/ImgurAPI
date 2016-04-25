package com.crowdhopper.imgurapi.Models;

import org.json.JSONObject;

public class Vote implements Model {
	private int ups;
	private int downs;
	JSONObject data;
	
	public Vote(Basic<JSONObject> base) {
		this.populate(base);
	}

	@Override
	public void populate(Basic<JSONObject> base) {
		data = base.getData();
		ups = data.getInt("ups");
		downs = data.getInt("downs");
	}

	@Override
	public String toJson() {
		return data.toString();
	}
	
	public int getUpVotes() {return ups;}
	public int getDownVotes() {return downs;}
}
