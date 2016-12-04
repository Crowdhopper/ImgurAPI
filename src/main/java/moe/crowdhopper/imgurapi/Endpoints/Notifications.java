package moe.crowdhopper.imgurapi.Endpoints;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import moe.crowdhopper.imgurapi.Exceptions.AuthorizationException;
import moe.crowdhopper.imgurapi.Exceptions.HTTPRequestException;
import moe.crowdhopper.imgurapi.Exceptions.RateLimitException;
import moe.crowdhopper.imgurapi.ImgurApi;
import moe.crowdhopper.imgurapi.Models.Notification;
import org.json.JSONObject;

import com.crowdhopper.imgurapi.Exceptions.*;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

//Note: All Notification endpoints require you to authorize as a user, not anonymously. 
public class Notifications extends Endpoint {
	private static ImgurApi api = null;
	
	public static void initialize(ImgurApi new_api) {
		api = new_api;
	}
	
	
	//Returns the notifications tied to this account. If only_new is set to false, it returns all notifications; if true, only non-viewed
	//notifications are returned.
	public static Map<String, List<Notification>> getNotifications(boolean only_new)
			throws HTTPRequestException, RateLimitException, AuthorizationException {
		api.checkCredits();
		api.checkAuthorization();
		HttpResponse<JsonNode> response = null;
		try {
			response = Unirest.get(ImgurApi.API_URL + "notification")
					.header("Authorization", api.getHeader())
					.queryString("new", Boolean.toString(only_new))
					.asJson();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
		JSONObject raw = response.getBody().getObject();
		api.checkStatus(raw);
		JSONObject things = raw.optJSONObject("data");
		Map<String, List<Notification>> ret_map = new HashMap<String, List<Notification>>();
		ret_map.put("replies", api.dumpModels(things.optJSONArray("replies"), Notification.class));
		ret_map.put("messages", api.dumpModels(things.optJSONArray("messages"), Notification.class));
		return ret_map;
	}
	
	
	//Get information about a specific notification.
	public static Notification getNotification(int id)
			throws HTTPRequestException, RateLimitException, AuthorizationException {
		api.checkCredits();
		api.checkAuthorization();
		HttpResponse<JsonNode> response = null;
		try {
			response = Unirest.get(ImgurApi.API_URL + "notification/{id}")
					.header("Authorization", api.getHeader())
					.routeParam("id", Integer.toString(id))
					.asJson();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
		JSONObject raw = response.getBody().getObject();
		api.checkStatus(raw);
		return new Notification(raw.optJSONObject("data"));
	}
	
	
	//Mark the given notification(s) as read.
	public static void markRead(int id)
			throws RateLimitException, HTTPRequestException, AuthorizationException {
		api.checkAuthorization();
		api.checkCredits();
		HttpResponse<JsonNode> response = null;
		try {
			response = Unirest.delete(ImgurApi.API_URL + "notification/{id}")
					.header("Authorization", api.getHeader())
					.routeParam("id", Integer.toString(id))
					.asJson();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
		JSONObject raw = response.getBody().getObject();
		api.checkStatus(raw);
	}
	
	public static void markRead(List<Integer> ids)
			throws RateLimitException, HTTPRequestException, AuthorizationException {
		api.checkAuthorization();
		api.checkCredits();
		for(Integer i: ids)
			if(i == null)
				throw new NullPointerException();
		HttpResponse<JsonNode> response = null;
		try {
			response = Unirest.delete(ImgurApi.API_URL + "notification")
					.header("Authorization", api.getHeader())
					.field("ids", ids)
					.asJson();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
		JSONObject raw = response.getBody().getObject();
		api.checkStatus(raw);
	}
}
