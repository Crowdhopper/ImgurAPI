package com.crowdhopper.imgurapi.Endpoints;



import java.util.List;

import org.json.JSONObject;

import com.crowdhopper.imgurapi.ImgurApi;
import com.crowdhopper.imgurapi.Exceptions.*;
import com.crowdhopper.imgurapi.Models.*;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

public class CustomGalleries extends Endpoint {
	private static ImgurApi api = null;

	public static void initialize(ImgurApi new_api) {
		api = new_api;
	}
	
	//Custom Gallery endpoint (All functions in this endpoint require a logged-in user to function.)
	
		/*Returns the user's custom gallery.
		 * Sort should be "top," "viral," or "time."
		 * Window should be "day," "week," "month," "year," or "all."
		 */
		public static CustomGallery getCustomGallery(String sort, int page, String window) throws RateLimitException, AuthorizationException, HTTPRequestException, IllegalArgumentException {
			if(sort == null)
				sort = "viral";
			if(window == null)
				window = "week";
			api.checkCredits();
			api.checkAuthorization();
			api.checkParameters(new String[] {"top", "viral", "time"}, sort, "Sort");
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.get(ImgurApi.API_URL + "g/custom/{sort}/{window}/{page}")
						.header("Authorization", api.getHeader())
						.routeParam("sort", sort)
						.routeParam("page", Integer.toString(page))
						.routeParam("window", window)
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
			return new CustomGallery(raw.optJSONObject("data"));
		}
		
		
		/*Returns the user's custom gallery.
		 * Sort should be "top," "viral," or "time."
		 * Window should be "day," "week," "month," "year," or "all."
		 */
		public static CustomGallery getFilteredGallery(String sort, int page, String window) throws RateLimitException, AuthorizationException, HTTPRequestException, IllegalArgumentException {
			if(sort == null)
				sort = "viral";
			if(window == null)
				window = "week";
			api.checkCredits();
			api.checkAuthorization();
			api.checkParameters(new String[] {"top", "viral", "time"}, sort, "Sort");
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.get(ImgurApi.API_URL + "g/filtered/{sort}/{window}/{page}")
						.header("Authorization", api.getHeader())
						.routeParam("sort", sort)
						.routeParam("page", Integer.toString(page))
						.routeParam("window", window)
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
			return new CustomGallery(raw.optJSONObject("data"));
		}
		
		
		//Returns a single image from the user's custom gallery.
		public static Gallery getCustomGalleryImage(String item_id) throws HTTPRequestException, RateLimitException, AuthorizationException {
			api.checkCredits();
			api.checkAuthorization();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.get(ImgurApi.API_URL + "g/custom/{item_id}")
						.header("Authorization", api.getHeader())
						.routeParam("item_id", item_id)
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
			JSONObject data = raw.optJSONObject("data");
			if(data.optBoolean("isAlbum"))
				return new GalleryAlbum(data);
			return new GalleryImage(data);
		}
		
		
		//Adds given tags to user's custom gallery.
		public static void addTagsCustomGallery(List<String> tags) throws RateLimitException, AuthorizationException, HTTPRequestException {
			api.checkCredits();
			api.checkAuthorization();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.put(ImgurApi.API_URL + "g/custom/add_tags")
						.header("Authorization", api.getHeader())
						.field("tags", tags)
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
		}
		
		
		//Removes given tags from a user's custom gallery.
		public static void removeTagsCustomGallery(List<String> tags) throws RateLimitException, AuthorizationException, HTTPRequestException {
			api.checkCredits();
			api.checkAuthorization();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.delete(ImgurApi.API_URL + "g/custom/remove_tags")
						.header("Authorization", api.getHeader())
						.field("tags", tags)
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
		}
		
		
		//Filters a tag out of the user's gallery.
		public static void blockTag(String tag) throws RateLimitException, AuthorizationException, HTTPRequestException {
			api.checkCredits();
			api.checkPosts();
			api.checkAuthorization();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.post(ImgurApi.API_URL + "g/block_tag")
						.header("Authorization", api.getHeader())
						.field("tag", tag)
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
		}
		
		
		//Removes a tag from the user's blacklist
		public static void unblockTag(String tag) throws RateLimitException, AuthorizationException, HTTPRequestException {
			api.checkCredits();
			api.checkPosts();
			api.checkAuthorization();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.post(ImgurApi.API_URL + "g/unblock_tag")
						.header("Authorization", api.getHeader())
						.field("tag", tag)
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
		}
}
