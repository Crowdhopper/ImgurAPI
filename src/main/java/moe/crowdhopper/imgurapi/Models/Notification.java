package moe.crowdhopper.imgurapi.Models;

import org.json.JSONObject;

public class Notification extends Model {
	private int id;
	private int account_id; // Account id of the notification recipient
	private boolean viewed;
	private Model content = null; // What the notification is for.
	private JSONObject data;

	public Notification(JSONObject data) {
		this.data = data;
		populate();
	}
	
	//Only to be used in conjunction with the factory method.
	public Notification() {}

	@Override
	public void populate() {
		id = data.getInt("id");
		account_id = data.getInt("account_id");
		viewed = data.getBoolean("viewed");
		JSONObject rawData = data.getJSONObject("content");
		if (rawData.has("children"))
			content = new Comment(rawData);
		else if (rawData.has("last_message"))
			content = new ConversationNotification(rawData);
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

	public int getId() {
		return id;
	}

	public int getAccountId() {
		return account_id;
	}

	public boolean isViewed() {
		return viewed;
	}

	public Model getContent() {
		return content;
	}

	/*
	 * The notification object for Conversation is different than both the
	 * Message metadata and the Conversation metadata, so a new object
	 * representing the metadata retrieved from Notification had to be made.
	 */
	class ConversationNotification extends Model {
		private int id;
		private String from; // Username of the other participant
		private String account_id; // The account id of the user
		private String with_account; // The account id of the other participant
										// in the conversation
		private String last_message; // The last_message in the conversation
		private int message_num; // The number of messages in the conversation
		private int datetime; // Date last_message was sent
		private JSONObject data;

		ConversationNotification(JSONObject data) {
			this.data = data;
			populate();
		}

		@Override
		public void populate() {
			id = data.getInt("id");
			from = data.getString("from");
			account_id = data.getString("account_id");
			with_account = data.getString("with_account");
			last_message = data.getString("last_message");
			message_num = data.getInt("message_num");
			datetime = data.getInt("datetime");
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

		public int getId() {
			return id;
		}

		public String getFrom() {
			return from;
		}

		public String getAccountId() {
			return account_id;
		}

		public String getOtherId() {
			return with_account;
		}

		public String getLastMessage() {
			return last_message;
		}

		public int getNumMessages() {
			return message_num;
		}

		public int getTime() {
			return datetime;
		}
	}
}
