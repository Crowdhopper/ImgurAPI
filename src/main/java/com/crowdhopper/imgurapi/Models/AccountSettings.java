package com.crowdhopper.imgurapi.Models;

import org.json.JSONObject;
import org.json.JSONArray;
import java.util.List;
import java.util.ArrayList;

public class AccountSettings implements Model {
	private String account_url;
	private String email;
	private boolean high_quality;
	private boolean public_images;
	private String album_privacy;
	private int pro_expiration;
	private boolean accepted_terms;
	private List<String> active_emails;
	private boolean messaging_enabled;
	private JSONArray blocked_users;
	private boolean mature;
	private JSONObject data;
	
	public AccountSettings(Basic<JSONObject> base) {
		this.populate(base);
	}
	
	@Override
	public void populate(Basic<JSONObject> base) {
		data = base.getData();
		account_url = data.getString("account_url");
		email = data.getString("email");
		high_quality = data.getBoolean("high_quality");
		public_images = data.getBoolean("public_images");
		album_privacy = data.getString("album_privacy");
		if(data.getString("pro_expiration").equals("false"))
			pro_expiration = 0;
		else
			pro_expiration = data.getInt("pro_expiration");
		accepted_terms = data.getBoolean("accepted_terms");
		messaging_enabled = data.getBoolean("messaging_enabled");
		blocked_users = data.getJSONArray("blocked_users");
		mature = data.getBoolean("show_mature");
		active_emails = new ArrayList<String>();
		String emails = data.getString("active_emails");
		emails = emails.substring(1);
		emails = emails.substring(0, emails.length()-1);
		for(String s: emails.split(","))
			active_emails.add(s);
	}

	@Override
	public String toJson() {
		return data.toString();
	}
	
	public String getUrl() {return account_url;}
	public String getEmail() {return email;}
	public boolean isHighQuality() {return high_quality;}
	public boolean isPublic() {return public_images;}
	public String getPrivacy() {return album_privacy;}
	public int getProExpire() {return pro_expiration;}
	public boolean hasAcceptedTerms() {return accepted_terms;}
	public JSONArray blockedUsers() {return blocked_users;}
	public boolean isMature() {return mature;}
	public List<String> getEmails() {return active_emails;}
	public boolean isMessageEnabled() {return messaging_enabled;}
}
