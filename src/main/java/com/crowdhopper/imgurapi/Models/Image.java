package com.crowdhopper.imgurapi.Models;

import org.json.JSONObject;

public class Image implements Model {
	
	public Image(Basic<JSONObject> base) {
		this.populate(base);
	}
	
	public Image(JSONObject data) {
		
	}

	@Override
	public void populate(Basic<JSONObject> base) {
		// TODO Auto-generated method stub

	}

	@Override
	public String toJson() {
		// TODO Auto-generated method stub
		return null;
	}

}
