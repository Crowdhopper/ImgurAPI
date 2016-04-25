package com.crowdhopper.imgurapi.Models;

import org.json.JSONObject;

public class Notification implements Model {
	private int id;
	private int account_id;
	private boolean viewed;
	private Model content = null;
	private JSONObject data;
	
	public Notification(Basic<JSONObject> base) {
		this.populate(base);
	}
	
	public Notification(JSONObject data) {
		Basic<JSONObject> base = new Basic<JSONObject>();
		base.setData(data);
		this.populate(base);
	}
 
	@Override
	public void populate(Basic<JSONObject> base) {
		data = base.getData();
		id = data.getInt("id");
		account_id = data.getInt("account_id");
		viewed = data.getBoolean("viewed");
		JSONObject rawData = data.getJSONObject("content");
		if(rawData.has("children"))
			content = new Comment(rawData);
		else if(rawData.has("last_message"))
			content = new ConversationNotification(rawData);
	}

	@Override
	public String toJson() {
		return data.toString();
	}
	
	public int getId() {return id;}
	public int getAccountId() {return account_id;}
	public boolean isViewed() {return viewed;}
	public Model getContent() {return content;}
	
	class ConversationNotification implements Model {
		private int id;
		private String from;
		private String account_id;
		private String with_account;
		private String last_message;
		private int message_num;
		private int datetime;
		private JSONObject data;
		
		ConversationNotification(JSONObject data) {
			this.data = data;
			Basic<JSONObject> base = new Basic<JSONObject>();
			base.setData(data);
			this.populate(base);
		}
		
		@Override
		public void populate(Basic<JSONObject> base) {
			id = data.getInt("id");
			from = data.getString("from");
			account_id = data.getString("account_id");
			with_account = data.getString("with_account");
			last_message = data.getString("last_message");
			message_num = data.getInt("message_num");
			datetime = data.getInt("datetime");
		}

		@Override
		public String toJson() {
			return data.toString();
		}
		
		public int getId() {return id;}
		public String getFrom() {return from;}
		public String getAccountId() {return account_id;}
		public String getOtherId() {return with_account;}
		public String getLastMessage() {return last_message;}
		public int getNumMessages() {return message_num;}
		public int getTime() {return datetime;}
	}
}
