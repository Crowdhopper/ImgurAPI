package com.crowdhopper.imgurapi.Endpoints;


import org.json.JSONObject;

import com.crowdhopper.imgurapi.ImgurApi;
import com.crowdhopper.imgurapi.Exceptions.*;
import com.crowdhopper.imgurapi.Models.*;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

public class Comments extends Endpoint {
	private static ImgurApi api = null;

	public static void initialize(ImgurApi new_api) {
		api = new_api;
	}

	//Get information about the comment.
		public static Comment getComment(int id) throws RateLimitException, HTTPRequestException {
			api.checkCredits();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.get(ImgurApi.API_URL + "comment/{id}")
						.header("Authorization", api.getHeader())
						.routeParam("id", Integer.toString(id))
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
			return new Comment(raw.optJSONObject("data"));
		}
		
		
		//Creates a new comment, returns the new comment's id
		public static int createComment(String image_id, String comment) throws RateLimitException, HTTPRequestException, AuthorizationException {
			api.checkCredits();
			api.checkPosts();
			api.checkAuthorization();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.post(ImgurApi.API_URL + "comment")
						.header("Authorization", api.getHeader())
						.field("image_id", image_id)
						.field("comment", comment)
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			api.refreshPost(response.getHeaders());
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
			return raw.optInt("data");
		}
		
		
		//Deletes the comment given by the id
		public static void deleteComment(int id) throws RateLimitException, HTTPRequestException, AuthorizationException {
			api.checkCredits();
			api.checkAuthorization();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.delete(ImgurApi.API_URL + "comment/{id}")
						.header("Authorization", api.getHeader())
						.routeParam("id", Integer.toString(id))
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
		}
		
		
		//Gets the given comment with all replies attached
		public static Comment getReplies(int id) throws RateLimitException, HTTPRequestException {
			api.checkCredits();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.get(ImgurApi.API_URL + "comment/{id}/replies")
						.header("Authorization", api.getHeader())
						.routeParam("id", Integer.toString(id))
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
			return new Comment(raw.optJSONObject("data"));
		}
		
		
		//Creates a reply for the given comment
		public static int createReply(int id, String image_id, String comment) throws RateLimitException, HTTPRequestException, AuthorizationException {
			api.checkCredits();
			api.checkPosts();
			api.checkAuthorization();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.post(ImgurApi.API_URL + "comment/{id}")
						.header("Authorization", api.getHeader())
						.routeParam("id", Integer.toString(id))
						.field("image_id", image_id)
						.field("comment", comment)
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			api.refreshPost(response.getHeaders());
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
			return raw.optInt("data");
		}
		
		
		//Vote on a comment. Vote must be either "up" or "down." Must be logged in.
		public static void commentVote(int id, String vote) throws RateLimitException, HTTPRequestException, AuthorizationException, IllegalArgumentException {
			api.checkCredits();
			api.checkPosts();
			api.checkAuthorization();
			api.checkParameters(new String[] {"up", "down"}, vote, "Vote");
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.post(ImgurApi.API_URL + "comment/{id}/vote/{vote}")
						.header("Authorization", api.getHeader())
						.routeParam("id", Integer.toString(id))
						.routeParam("vote", vote)
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			api.refreshPost(response.getHeaders());
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
		}
		
		
		public static void reportComment(int id) throws RateLimitException, HTTPRequestException {
			reportComment(id, -1);
		}
		/*Report a comment. Possible values for reason are:
		 * 1: Doesn't belong on Imgur
		 * 2: Spam
		 * 3: Abusive
		 * 4: Mature content that isn't tagged as such.
		 * 5: Pornography
		 */
		public static void reportComment(int id, int reason) throws RateLimitException, HTTPRequestException {
			api.checkCredits();
			api.checkPosts();
			HttpResponse<JsonNode> response = null;
			try {
				if(reason == -1) {
					response = Unirest.post(ImgurApi.API_URL + "comment/{id}/report")
							.header("Authorization", api.getHeader())
							.routeParam("id", Integer.toString(id))
							.asJson();
				}
				else {
					response = Unirest.post(ImgurApi.API_URL + "comment/{id}/report")
							.header("Authorization", api.getHeader())
							.routeParam("id", Integer.toString(id))
							.field("reason", reason)
							.asJson();
				}
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			api.refreshPost(response.getHeaders());
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
		}
}
