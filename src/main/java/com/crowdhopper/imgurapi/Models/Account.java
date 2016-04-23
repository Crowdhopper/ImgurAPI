package com.crowdhopper.imgurapi.Models;

import org.json.JSONObject;

public class Account implements Model {
	private int id;
	private String url;
	private String bio;
	private double rep;
	private int created;
	private int pro_expiration;
	JSONObject data;
	
	public Account(Basic<JSONObject> base) {
		this.populate(base);
	}
	
	@Override
	public void populate(Basic<JSONObject> base) {
		data = base.getData();
		id = data.getInt("id");
		url = data.getString("url");
		bio = data.getString("bio");
		rep = data.getDouble("reputation");
		created = data.getInt("created");
		if(data.getString("pro_expiration").equals("false"))
			pro_expiration = 0;
		else
			pro_expiration = data.getInt("pro_expiration");
	}
	
	@Override
	public String toJson() {
		return data.toString();
	}
	
	public int getId() {return id;}
	public String getUrl() {return url;}
	public String getBio() {return bio;}
	public double getRep() {return rep;}
	public int getCreated() {return created;}
	public int getProExpire() {return pro_expiration;}
}
