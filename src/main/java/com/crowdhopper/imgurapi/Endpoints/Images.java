package com.crowdhopper.imgurapi.Endpoints;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.crowdhopper.imgurapi.ImgurApi;
import com.crowdhopper.imgurapi.Exceptions.*;
import com.crowdhopper.imgurapi.Models.Image;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;



public class Images extends Endpoint {
		private static ImgurApi api = null;
		
		public static void initialize(ImgurApi new_api) {
			api = new_api;
		}
		
		
		//Get information about an image.
		public static Image getImage(String id) 
				throws RateLimitException, HTTPRequestException{
			api.checkCredits();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.get(ImgurApi.API_URL + "image/{id}")
						.header("Authorization", api.getHeader())
						.routeParam("id", id)
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
			return new Image(raw.optJSONObject("data"));
		}
		
		
		/*Upload an image. Returns the deletehash of the image.
		Album is either the id of the album, or the deletehash if it's an anonymous album.
		The url parameter is a link to the file on the internet. 
		The path parameter is the absolute path to the image on your computer.
		The file parameter MUST BE an uploaded image.
		*/
		public static String uploadImageFromUrl(String url, String title, String album, String description)
				throws HTTPRequestException, RateLimitException {
			api.checkCredits(10);
			api.checkPosts();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("image", url);
			if(album != null)
				params.put("album", album);
			params.put("type", "URL");
			if(title != null)
				params.put("title", title);
			if(description != null)
				params.put("description", description);
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.post(ImgurApi.API_URL + "upload")
						.header("Authorization", api.getHeader())
						.fields(params)
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			api.refreshPost(response.getHeaders());
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
			return raw.optString("data");
		}
		
		public static String uploadImageFromFile(File file, String title, String album, String description)
				throws HTTPRequestException, RateLimitException {
			api.checkCredits(10);
			api.checkPosts();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("image", file);
			if(album != null)
				params.put("album", album);
			params.put("type", "file");
			if(title != null)
				params.put("title", title);
			if(description != null)
				params.put("description", description);
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.post(ImgurApi.API_URL + "upload")
						.header("Authorization", api.getHeader())
						.fields(params)
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			api.refreshPost(response.getHeaders());
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
			return raw.optString("data");
		}
		
		public static String uploadImageFromFile(String path, String title, String album, String description)
				throws HTTPRequestException, RateLimitException, InvalidParameterException {
			File file = new File(path);
			if(!file.isFile())
				throw new InvalidParameterException("Path must lead to a file.");
			String name = file.getName();
			String ext = name.split("\\.")[name.split("\\.").length - 1];
			System.out.println(ext);
			try {
				api.checkParameters(new String[] {"jpg",  "png", "jpeg", "gif"}, ext , "Path");
			} catch (InvalidParameterException e) {
				throw new InvalidParameterException("Path must point to a jpg, png, or gif.");
			}
			return uploadImageFromFile(file, title, album, description);
		}
		
		
		//Deletes the given image. id should be the deletehash for anonymous images.
		public static void deleteImage(String id)
				throws HTTPRequestException, RateLimitException {
			api.checkCredits();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.delete(ImgurApi.API_URL + "image/{id}")
						.header("Authorization", api.getHeader())
						.routeParam("id", id)
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
		}
		
		
		//Update image information. id should be the deletehash for anonymous images. Updated should be either "title" or "description,"
		//depending on which is being updated.
		public static void updateImage(String id, String updated, String updateTo)
				throws HTTPRequestException, RateLimitException, InvalidParameterException {
			api.checkParameters(new String[] {"title", "description"}, updated , "Updated");
			Map<String, Object> params = new HashMap<String, Object>();
			updateImage(id, params);
		}
		
		public static void updateImage(String id, Map<String, Object> params)
				throws HTTPRequestException, RateLimitException, InvalidParameterException {
			api.checkCredits();
			api.checkPosts();
			for(String updated: params.keySet()) {
				api.checkParameters(new String[] {"title", "description"}, updated , "Updated");
			}
			if(params.size() > 2)
				throw new InvalidParameterException("No more than two entries should be given.");
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.post(ImgurApi.API_URL + "image/{id}")
						.header("Authorization", api.getHeader())
						.routeParam("id", id)
						.fields(params)
						.asJson();
			} catch (UnirestException e)  {
				e.printStackTrace();
			}
			api.refreshPost(response.getHeaders());
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
		}
		
		
		//Favorite the given image. Requires being logged in.
		public static void favoriteImage(String id)
				throws HTTPRequestException, RateLimitException, AuthorizationException {
			api.checkCredits();
			api.checkAuthorization();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.post(ImgurApi.API_URL + "image/{id}/favorite")
						.header("Authorization", api.getHeader())
						.routeParam("id", id)
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
		}
}
