package com.crowdhopper.imgurapi.Models;

import org.json.JSONObject;

public interface Model {
	public void populate(Basic<JSONObject> base);
	public String toJson();
}
