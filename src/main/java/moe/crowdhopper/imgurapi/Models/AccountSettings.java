package moe.crowdhopper.imgurapi.Models;

import org.json.JSONObject;
import org.json.JSONArray;
import java.util.List;
import java.util.ArrayList;

public class AccountSettings extends Model {
	private String account_url; // The account username
	private String email;
	private boolean high_quality; // Whether or not the user can upload
									// high_quality images.
	private boolean public_images; // Whether or not the user allows all images
									// to be publicly accessible.
	private String album_privacy; // The default album privacy for this user
	private int pro_expiration;
	private boolean accepted_terms; // Whether they've accepted the Terms of
									// uploading to the gallery.
	private List<String> active_emails;
	private boolean messaging_enabled;
	private JSONArray blocked_users;
	private boolean mature;
	private JSONObject data;

	public AccountSettings(JSONObject data) {
		this.data = data;
		populate();
	}
	
	//Only to be used in conjunction with the factory method.
	public AccountSettings() {}

	@Override
	public void populate() {
		account_url = data.getString("account_url");
		email = data.getString("email");
		high_quality = data.getBoolean("high_quality");
		public_images = data.getBoolean("public_images");
		album_privacy = data.getString("album_privacy");
		pro_expiration = data.optInt("pro_expiration");
		accepted_terms = data.optBoolean("accepted_terms");
		messaging_enabled = data.getBoolean("messaging_enabled");
		blocked_users = data.getJSONArray("blocked_users");
		mature = data.getBoolean("show_mature");
		active_emails = new ArrayList<String>();
		JSONArray emails = data.optJSONArray("active_emails");
		for (int i = 0; i < emails.length(); i++)
			active_emails.add(emails.optString(i));
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

	public String getUrl() {
		return account_url;
	}

	public String getEmail() {
		return email;
	}

	public boolean isHighQuality() {
		return high_quality;
	}

	public boolean isPublic() {
		return public_images;
	}

	public String getPrivacy() {
		return album_privacy;
	}

	public int getProExpire() {
		return pro_expiration;
	}

	public boolean hasAcceptedTerms() {
		return accepted_terms;
	}

	public JSONArray blockedUsers() {
		return blocked_users;
	}

	public boolean isMature() {
		return mature;
	}

	public List<String> getEmails() {
		return active_emails;
	}

	public boolean isMessageEnabled() {
		return messaging_enabled;
	}
}
