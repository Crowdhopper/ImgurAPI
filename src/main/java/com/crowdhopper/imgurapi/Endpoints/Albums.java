package com.crowdhopper.imgurapi.Endpoints;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.crowdhopper.imgurapi.ImgurApi;
import com.crowdhopper.imgurapi.Exceptions.*;
import com.crowdhopper.imgurapi.Models.*;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;


public class Albums extends Endpoint {
	private static ImgurApi api = null;

	public static void initialize(ImgurApi new_api) {
		api = new_api;
	}
	
	//Returns the album specified by the given id.
		public static Album getAlbum(String id) throws RateLimitException, HTTPRequestException {
			api.checkCredits();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.get(ImgurApi.API_URL + "album/{id}")
						.header("Authorization", api.getHeader())
						.routeParam("id", id)
						.asJson();
			} catch(UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
			return new Album(raw.optJSONObject("data"));
		}
		
		
		//Returns a list of the images in the album specified by the id.
		public static List<Image> getAlbumImages(String id) throws RateLimitException, HTTPRequestException {
			Album base = getAlbum(id);
			return base.getImages();
		}
		
		
		/*Creates an album given paramaters
		 * Valid settings are:
		 * ids: a comma delimited string containing the image ids to be included in the album
		 * title: the title of the album
		 * description: the description of the album
		 * privacy: must be public static, hidden, or secret. Defaults to the user default if logged in.
		 * layout: must be blog, grid, horizontal, or vertical
		 * cover: the id of an image to use as the album cover
		 */
		public static String createAlbum(Map<String, String> settings) throws RateLimitException, HTTPRequestException {
			api.checkCredits();
			Map<String, Object> fields = new LinkedHashMap<String, Object>();
			fields.putAll(settings);
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.post(ImgurApi.API_URL + "album")
						.header("Authorization", api.getHeader())
						.fields(fields)
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			api.refreshPost(response.getHeaders());
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
			return raw.optString("data");
		}
		
		
		public static String updateAlbum(String key, String value, String album) throws RateLimitException, HTTPRequestException {
			Map<String, String> settings = new LinkedHashMap<String, String>();
			settings.put(key, value);
			return updateAlbum(settings, album);
		}
		
		/*Updates an album given parameters. Parameters are the same as createAlbum
		 * If the album was uploaded anonymously, album should be the deletehash returned on creation, otherwise it should be the album ID.
		 */
		public static String updateAlbum(Map<String, String> settings, String album) throws RateLimitException, HTTPRequestException {
			api.checkCredits();
			Map<String, Object> fields = new LinkedHashMap<String, Object>();
			fields.putAll(settings);
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.post(ImgurApi.API_URL + "album/{album}")
						.header("Authorization", api.getHeader())
						.routeParam("album", album)
						.fields(fields)
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			api.refreshPost(response.getHeaders());
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
			return raw.optString("data");
		}
		
		
		//Deletes the given album. If it's an anonymous album, the album's deletehash should be used in place of an ID.
		public static String deleteAlbum(String album) throws RateLimitException, HTTPRequestException {
			api.checkCredits();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.delete(ImgurApi.API_URL + "album/{album}")
						.header("Authorization", api.getHeader())
						.routeParam("album", album)
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
			return raw.optString("data");
		}
		
		
		//Favorites the album if logged in.
		public static void favoriteAlbum(String id) throws RateLimitException, HTTPRequestException, AuthorizationException {
			api.checkCredits();
			api.checkAuthorization();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.get(ImgurApi.API_URL + "album/{id}/favorite")
						.header("Authorization", api.getHeader())
						.routeParam("id", id)
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
		}
		
		
		//Sets the photos of the given album to be the given images.
		public static String setPhotos(String album, String ids) throws RateLimitException, HTTPRequestException {
			api.checkCredits();
			api.checkPosts();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.post(ImgurApi.API_URL + "album/{album}")
						.header("Authorization", api.getHeader())
						.routeParam("album", album)
						.field("ids", ids)
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
			return raw.optString("data");
		}
		
		
		//Adds photos to the given album
		public static String addPhotos(String album, String ids) throws RateLimitException, HTTPRequestException {
			api.checkCredits();
			api.checkPosts();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.put(ImgurApi.API_URL + "album/{album}/add")
						.header("Authorization", api.getHeader())
						.routeParam("album", album)
						.field("ids", ids)
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
			return raw.optString("data");
		}
		
		
		//Deletes photos from the given album
		public static String deletePhotos(String album, String ids) throws RateLimitException, HTTPRequestException {
			api.checkCredits();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.delete(ImgurApi.API_URL + "album/{album}/remove_images")
						.header("Authorization", api.getHeader())
						.routeParam("album", album)
						.field("ids", ids)
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
			return raw.optString("data");
		}
}
