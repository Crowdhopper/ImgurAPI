package com.crowdhopper.imgurapi.Models;

import org.json.JSONObject;
import org.json.JSONException;

public class Account extends Model {
	private int id;
	private String url; // The account username
	private String bio;
	private double rep;
	private int created;
	private int pro_expiration; // The time the account's pro_membership will
								// run out, if they have it.
	JSONObject data;

	public Account(JSONObject data) {
		this.data = data;
		populate();
	}
	
	//Only to be used in conjunction with the factory method.
	public Account() {}

	@Override
	public void populate() {
		id = data.getInt("id");
		url = data.getString("url");
		try {
			bio = data.getString("bio");
		} catch (JSONException e) {
			bio = null;
		}
		rep = data.getDouble("reputation");
		created = data.getInt("created");
		pro_expiration = data.optInt("pro_expiration");
	}

	@Override
	public void factory(JSONObject data) {
		this.data = data;
		populate();
	}

	@Override
	public String toString() {
		return data.toString();
	}

	public int getId() {
		return id;
	}

	public String getUrl() {
		return url;
	}

	public String getBio() {
		return bio;
	}

	public double getRep() {
		return rep;
	}

	public int getCreated() {
		return created;
	}

	public int getProExpire() {
		return pro_expiration;
	}
}
