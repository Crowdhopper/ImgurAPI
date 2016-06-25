package com.crowdhopper.imgurapi.Endpoints;

import java.util.List;

import org.json.JSONObject;

import com.crowdhopper.imgurapi.ImgurApi;
import com.crowdhopper.imgurapi.Exceptions.RateLimitException;
import com.crowdhopper.imgurapi.Exceptions.HTTPRequestException;
import com.crowdhopper.imgurapi.Exceptions.InvalidParameterException;
import com.crowdhopper.imgurapi.Models.Gallery;
import com.crowdhopper.imgurapi.Models.Topic;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class Topics extends Endpoint {
	private static ImgurApi api = null;
	
	public static void initialize(ImgurApi new_api) {
		api = new_api;
	}
	
	
	//Get the list of default topics.
	public static List<Topic> getDefault()
			throws HTTPRequestException, RateLimitException {
		api.checkCredits();
		HttpResponse<JsonNode> response = null;
		try {
			response = Unirest.get(ImgurApi.API_URL + "topics/defaults")
					.header("Authorization", api.getHeader())
					.asJson();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
		JSONObject raw = response.getBody().getObject();
		api.checkStatus(raw);
		return api.dumpModels(raw.optJSONArray("data"), Topic.class);
	}
	
	
	//View gallery items for a topic.
	public static List<Gallery> getItems(String topic_id, String sort, int page, String window)
			throws InvalidParameterException, HTTPRequestException, RateLimitException {
		if(sort == null)
			sort = "viral";
		if(window == null)
			window = "week";
		api.checkParameters(new String[] {"top", "viral", "time"}, sort, "Sort");
		api.checkCredits();
		HttpResponse<JsonNode> response = null;
		try {
			response = Unirest.get(ImgurApi.API_URL + "topics/{topic_id}/{sort}/{window}/{page}")
					.header("Authorization", api.getHeader())
					.routeParam("topic_id", topic_id)
					.routeParam("sort", sort)
					.routeParam("window", window)
					.routeParam("page", Integer.toString(page))
					.asJson();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
		JSONObject raw = response.getBody().getObject();
		api.checkStatus(raw);
		return api.dumpGallery(raw.optJSONArray("data"));
	}
}


//Finally done.
