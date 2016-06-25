package com.crowdhopper.imgurapi.Endpoints;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.crowdhopper.imgurapi.ImgurApi;
import com.crowdhopper.imgurapi.Models.Conversation;
import com.crowdhopper.imgurapi.Exceptions.*;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

//Note: All Conversation endpoints require you to authorize as a user, not anonymously. 
public class Conversations extends Endpoint {
	private static ImgurApi api = null;
	
	public static void initialize(ImgurApi new_api) {
		api = new_api;
	}
	
	
	//Gets a list of all the conversations the user is in.
	public static List<Conversation> getConversations()
			throws AuthorizationException, HTTPRequestException, RateLimitException {
		api.checkCredits();
		api.checkAuthorization();
		HttpResponse<JsonNode> response = null;
		try {
			response = Unirest.get(ImgurApi.API_URL + "conversation")
					.header("Authorization", api.getHeader())
					.asJson();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
		JSONObject raw = response.getBody().getObject();
		api.checkStatus(raw);
		JSONArray raw_list = raw.optJSONArray("data");
		return api.dumpModels(raw_list, Conversation.class);
	}
	
	
	//Get information about a single conversation
	public static Conversation getConversation(int id, Integer page, Integer offset)
			throws HTTPRequestException, AuthorizationException, RateLimitException {
		api.checkAuthorization();
		api.checkCredits();
		if(page == null)
			page = 1;
		if(offset == null)
			offset = 0;
		HttpResponse<JsonNode> response = null;
		try {
			response = Unirest.get(ImgurApi.API_URL + "conversations/{id}/{page}/{offset}")
					.header("Authorization", api.getHeader())
					.routeParam("id", Integer.toString(id))
					.routeParam("page", Integer.toString(page))
					.routeParam("offset", Integer.toString(offset))
					.asJson();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
		JSONObject raw = response.getBody().getObject();
		api.checkStatus(raw);
		return new Conversation(raw.optJSONObject("data"));
	}
	
	
	//Create a new message
	public static void sendMessage(String recipient, String message)
			throws HTTPRequestException, AuthorizationException, RateLimitException {
		api.checkCredits();
		api.checkPosts();
		api.checkAuthorization();
		HttpResponse<JsonNode> response = null;
		try {
			response = Unirest.post(ImgurApi.API_URL + "conversations/{recipient}")
					.header("Authorization", api.getHeader())
					.routeParam("recipient", recipient)
					.field("message", message)
					.asJson();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
		api.refreshPost(response.getHeaders());
		JSONObject raw = response.getBody().getObject();
		api.checkStatus(raw);
	}
	
	
	//Delete the given conversation
	public static void deleteConversation(int id)
			throws HTTPRequestException, AuthorizationException, RateLimitException {
		api.checkCredits();
		api.checkAuthorization();
		HttpResponse<JsonNode> response = null;
		try {
			response = Unirest.delete(ImgurApi.API_URL + "conversations/{id}")
					.header("Authorization", api.getHeader())
					.routeParam("id", Integer.toString(id))
					.asJson();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
		JSONObject raw = response.getBody().getObject();
		api.checkStatus(raw);
	}
	
	
	//Report the given user.
	public static void reportUser(String username)
			throws HTTPRequestException, AuthorizationException, RateLimitException {
		api.checkCredits();
		api.checkPosts();
		api.checkAuthorization();
		HttpResponse<JsonNode> response = null;
		try {
			response = Unirest.post(ImgurApi.API_URL + "conversations/report/{username}")
					.header("Authorization", api.getHeader())
					.routeParam("username", username)
					.asJson();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
		JSONObject raw = response.getBody().getObject();
		api.checkStatus(raw);
	}
	
	
	//Block the given user.
	public static void blockUser(String username)
			throws HTTPRequestException, AuthorizationException, RateLimitException {
		api.checkCredits();
		api.checkPosts();
		api.checkAuthorization();
		HttpResponse<JsonNode> response = null;
		try {
			response = Unirest.post(ImgurApi.API_URL + "conversations/block/{username}")
					.header("Authorization", api.getHeader())
					.routeParam("username", username)
					.asJson();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
		JSONObject raw = response.getBody().getObject();
		api.checkStatus(raw);
	}
}
