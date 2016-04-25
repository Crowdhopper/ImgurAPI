package com.crowdhopper.imgurapi.Models;

import org.json.JSONObject;
import org.json.JSONArray;

//This is the base class for all the data models, containing the raw data from the response from the HTTP requests, that is then
//used in the constructors of the other data models.

public class Basic<T> {
	private boolean success;
	private int status;
	private T data;
	private JSONObject json;
	
	public Basic() {}
	
	@SuppressWarnings("unchecked")
	public Basic(JSONObject response) {
		if(response.has("status"))
			status = response.getInt("status");
		if(response.has("success"))
			success = response.getBoolean("success");
		if(success == false)
			return;
			//TODO Make this throw an error instead, with the error code depending on the Status.
		if(response.has("data")) {
			if(data instanceof Integer) 
				data = (T)((Integer)response.getInt("data"));
			else if(data instanceof Boolean)
				data = (T)((Boolean)response.getBoolean("data"));
			else if(data instanceof JSONArray)
				data = (T)response.getJSONArray("data");
			else
				data = (T)response.getJSONObject("data");
		}
	}
	
	//Only to be used on Basic<JSONObject>
	@SuppressWarnings("unchecked")
	void setData(JSONObject data) {
		this.data = (T)data;
	}
	
	public String toJson() {
		return json.toString();
	}
	
	public boolean getSuccess() {return success;}
	public int getStatus() {return status;}
	public T getData() {return data;}
}
