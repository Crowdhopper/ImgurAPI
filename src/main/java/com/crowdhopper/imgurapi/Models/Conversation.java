package com.crowdhopper.imgurapi.Models;

import org.json.JSONObject;
import org.json.JSONArray;
import java.util.List;
import java.util.ArrayList;

public class Conversation implements Model {
	private int id;
	private String preview;
	private int datetime;
	private int other_id; //ID of the other participant in the conversation
	private String other_name; //Username of the other participant
	private int message_count;
	private List<Message> messages = null;
	private Boolean done = null;
	private Integer page = null;
	private JSONObject data;
	
	public Conversation(Basic<JSONObject> base) {
		this.populate(base);
	}
	 
	@Override
	public void populate(Basic<JSONObject> base) {
		data = base.getData();
		id = data.getInt("id");
		preview = data.getString("last_message_preview");
		datetime = data.getInt("datetime");
		other_id = data.getInt("with_account_id");
		other_name = data.getString("with_account");
		if(data.has("done")) {
			done = data.getBoolean("done");
			page = data.getInt("page");
			messages = new ArrayList<Message>();
			JSONArray rawData = new JSONArray(data.getString("messages"));
			for(int i = 0; i < rawData.length(); i++)
				messages.add(new Message(rawData.getJSONObject(i)));
		}
	}

	@Override
	public String toJson() {
		return data.toString();
	}
	
	public int getId() {return id;}
	public String getPreview() {return preview;}
	public int getTime() {return datetime;}
	public int getOtherId() {return other_id;}
	public String getOtherName() {return other_name;}
	public int getMessageCount() {return message_count;}
	public Boolean getDone() {return done;}
	public Integer getPage() {return page;}
	public List<Message> getMessages() {return messages;}
	
	class Message {
		private int id;
		private String from;
		private int receive_id;
		private int sender_id;
		private String content;
		private int convo_id;
		private int datetime;
		
		Message(JSONObject data) {
			id = data.getInt("id");
			from = data.getString("from");
			receive_id = data.getInt("account_id");
			sender_id = data.getInt("sender_id");
			content = data.getString("body");
			convo_id = data.getInt("conversation_id");
			datetime = data.getInt("datetime");
		}
		
		public int getId() {return id;}
		public String getSender() {return from;}
		public int getRecipientId() {return receive_id;}
		public int getSenderId() {return sender_id;}
		public String getContent() {return content;}
		public int getConversationId() {return convo_id;}
		public int getTimeSent() {return datetime;}
	}

}
