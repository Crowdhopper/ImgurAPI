package com.crowdhopper.imgurapi.Endpoints;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.crowdhopper.imgurapi.ImgurApi;
import com.crowdhopper.imgurapi.Exceptions.*;
import com.crowdhopper.imgurapi.Models.*;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

public class Accounts extends Endpoint {
	private static ImgurApi api = null;
	
	public static void initialize(ImgurApi new_api) {
		api = new_api;
	}

	//if is_username is false, name is interpreted as being an account ID instead
		//Returns the given user's account information
		public static Account accountBase(String name, boolean is_username) throws HTTPRequestException, RateLimitException {
			api.checkCredits();
			HttpResponse<JsonNode> response = null;
			if(is_username) {
				try {
					response = Unirest.get(ImgurApi.API_URL + "account/{username}")
					.routeParam("username", name)
					.header("Authorization", api.getHeader())
					.asJson();
				} catch (UnirestException e) {
					e.printStackTrace();
				}
			}
			else {
				try {
					response = Unirest.get(ImgurApi.API_URL + "account/")
					.header("Authorization", api.getHeader())
					.queryString("account_id", name)
					.asJson();
				} catch (UnirestException e) {
					e.printStackTrace();
				}
			}
			JSONObject data = response.getBody().getObject();
			api.checkStatus(data);
			return new Account(data.optJSONObject("data"));
		}
		
		
		/*Gives a list of the given user's gallery favorites.
		 * sort must be either "newest" or "oldest"	 */
		public static List<Gallery> accountGalleryFavorites(String username, Integer page, String sort) throws HTTPRequestException, RateLimitException {
			if(sort == null)
				sort = "newest";
			api.checkCredits();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.get(ImgurApi.API_URL + "account/{username}/gallery_favorites/{sort}/{page}")
						.header("Authorization", api.getHeader())
						.routeParam("username", username)
						.routeParam("sort", sort)
						.routeParam("page", Integer.toString(page))
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
			JSONArray body = raw.getJSONArray("data");
			return api.dumpGallery(body);
		}
		
		
		//Returns the favorites of the logged in user. Throws an error if not logged in.
		public static List<Gallery> accountFavorites() throws AuthorizationException, HTTPRequestException, RateLimitException {
			api.checkAuthorization();
			api.checkCredits();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.get(ImgurApi.API_URL + "account/me/favorites")
						.header("Authorization", api.getHeader())
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
			JSONArray body = raw.getJSONArray("data");
			return api.dumpGallery(body);
		}
		
		
		//Returns the list of all submissions the given user has made
		public static List<Gallery> accountSubmissions(String username, int page) throws HTTPRequestException, RateLimitException {
			api.checkCredits();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.get(ImgurApi.API_URL + "account/{username}/submissions/{page}")
						.header("Authorization", api.getHeader())
						.routeParam("username", username)
						.routeParam("page", Integer.toString(page))
						.asJson();
			} catch(UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
			JSONArray body = raw.getJSONArray("data");
			return api.dumpGallery(body);
		}
		
		
		//Returns the current account settings. Throws an error if not logged in.
		public static AccountSettings accountSettings() throws AuthorizationException, HTTPRequestException, RateLimitException {
			api.checkAuthorization();
			api.checkCredits();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.get(ImgurApi.API_URL + "account/me/settings")
				.header("Authorization", api.getHeader())
				.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			JSONObject data = response.getBody().getObject();
			api.checkStatus(data);
			return new AccountSettings(data.optJSONObject("data"));
		}
		

		//Changes a single setting.
		public static void changeAccountSettings(String setting, String new_value) throws RateLimitException, AuthorizationException, HTTPRequestException {
			Map<String, String> settings = new LinkedHashMap<String, String>();
			settings.put(setting, new_value);
			changeAccountSettings(settings);
		}
		
		/*Changes account settings for the logged in user. Throws an exception if not logged in.
		 * 
		 * Valid values for setting are:
		 * bio: Any string is valid for new_value, sets your bio
		 * public static_images: Valid new_value is either "private" or "public static"
		 * messaging_enabled: "true" or "false"
		 * album_privacy: "public static," "hidden," or "secret"
		 * accepted_gallery_terms: "true" or "false"
		 * username: Alphanumeric string between 4 - 63 characters
		 * show_mature: "true" or "false"
		 * newsletter_subscribed: "true" or "false"
		 */
		public static void changeAccountSettings(Map<String, String> settings) throws RateLimitException, AuthorizationException, HTTPRequestException {
			api.checkAuthorization();
			api.checkCredits(10);
			Map<String, Object> fields = new LinkedHashMap<String, Object>();
			fields.putAll(settings);
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.post(ImgurApi.API_URL + "account/me/settings")
						.header("Authorization", api.getHeader())
						.fields(fields)
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			api.refreshPost(response.getHeaders());
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
		}
		
		
		//Returns the gallery profile for the given user.
		public static GalleryProfile accountGalleryProfile(String username) throws RateLimitException, HTTPRequestException {
			api.checkCredits();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.get(ImgurApi.API_URL + "account/{username}/gallery_profile")
						.routeParam("username", username)
						.header("Authorization", api.getHeader())
						.asJson();
			} catch(UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
			return new GalleryProfile(raw.optJSONObject("data"));
		}
		
		
		//Determines if the logged in user has verified their email.
		public static boolean verifyEmail(String username) throws RateLimitException, HTTPRequestException {
			api.checkCredits();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.get(ImgurApi.API_URL + "account/{username}/verifyemail")
						.routeParam("username", username)
						.header("Authorization", api.getHeader())
						.asJson();
			} catch(UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
			return raw.optBoolean("data");
		}
		
		
		//Sends a verification email to the logged in user.
		public static void sendVerificationEmail() throws RateLimitException, AuthorizationException, HTTPRequestException {
			api.checkCredits(10);
			api.checkAuthorization();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.post(ImgurApi.API_URL + "account/me/verifyemail")
						.header("Authorization", api.getHeader())
						.asJson();
			} catch(UnirestException e) {
				e.printStackTrace();
			}
			api.refreshPost(response.getHeaders());
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
		}
		
		
		//Returns a list of all albums from the given user.
		public static List<Album> getAlbums(String username, int page) throws RateLimitException, HTTPRequestException {
			api.checkCredits();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.get(ImgurApi.API_URL + "account/{username}/albums/{page}")
						.header("Authorization", api.getHeader())
						.routeParam("username", username)
						.routeParam("page", Integer.toString(page))
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
			JSONArray raw_list = raw.optJSONArray("data");
			return api.dumpModels(raw_list, Album.class);
		}
		

		//Returns a list of the ids of all the albums of the given user.
		public static List<String> getAlbumIds(String username, int page) throws RateLimitException, HTTPRequestException {
			api.checkCredits();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.get(ImgurApi.API_URL + "account/{username}/albums/ids/{page}")
						.header("Authorization", api.getHeader())
						.routeParam("username", username)
						.routeParam("page", Integer.toString(page))
						.asJson();
			} catch(UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
			JSONArray raw_list = raw.optJSONArray("data");
			return api.dump(raw_list, String.class);
		}
		
		
		//Returns the number of albums from the given user.
		public static int albumCount(String username) throws RateLimitException, HTTPRequestException {
			api.checkCredits();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.get(ImgurApi.API_URL + "account/{username}/albums/count")
						.header("Authorization", api.getHeader())
						.routeParam("username", username)
						.asJson();
			} catch(UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
			return raw.optInt("data");
		}
		
		
		public static void albumDelete(String id) throws RateLimitException, HTTPRequestException, AuthorizationException {
			api.checkCredits();
			api.checkAuthorization();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.delete(ImgurApi.API_URL + "account/me/album/{id}")
						.header("Authorization", api.getHeader())
						.routeParam("id", id)
						.asJson();
			} catch(UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
		}
		
		
		//Gives a list of all the comments from the given user.
		public static List<Comment> getComments(String username, String sort, int page) throws RateLimitException, HTTPRequestException {
			if(sort == null)
				sort = "newest";
			api.checkCredits();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.get(ImgurApi.API_URL + "account/{username}/comments/{sort}/{page}")
						.header("Authorization", api.getHeader())
						.routeParam("username", username)
						.routeParam("sort", sort)
						.routeParam("page", Integer.toString(page))
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
			JSONArray raw_list = raw.optJSONArray("data");
			return api.dumpModels(raw_list, Comment.class);
		}
		
		
		//Gives a list of all the ids of the comments authored by the given user.
		public static List<Integer> getCommentIds(String username, String sort, int page) throws RateLimitException, HTTPRequestException {
			if(sort == null)
				sort = "newest";
			api.checkCredits();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.get(ImgurApi.API_URL + "account/{username}/comments/ids/{sort}/{page}")
						.header("Authorization", api.getHeader())
						.routeParam("username", username)
						.routeParam("sort", sort)
						.routeParam("page", Integer.toString(page))
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
			JSONArray raw_list = raw.optJSONArray("data");
			return api.dump(raw_list, Integer.class);
		}
		
		
		//Gives the number of comments authored by the given user.
		public static int getCommentCount(String username) throws RateLimitException, HTTPRequestException {
			api.checkCredits();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.get(ImgurApi.API_URL + "account/{username}/comments/count")
						.header("Authorization", api.getHeader())
						.routeParam("username", username)
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
			return raw.optInt("data");
		}

		
		//Returns images uploaded by logged in user.
		public static List<Image> getImages(int page) throws RateLimitException, HTTPRequestException, AuthorizationException {
			api.checkCredits();
			api.checkAuthorization();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.get(ImgurApi.API_URL + "account/me/images/{page}")
						.header("Authorization", api.getHeader())
						.routeParam("page", Integer.toString(page))
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
			JSONArray raw_list = raw.optJSONArray("data");
			return api.dumpModels(raw_list, Image.class);
		}
		
		
		//Returns the ids of all images uploaded by logged in user.
		public static List<String> getImageIds(int page) throws RateLimitException, HTTPRequestException, AuthorizationException {
			api.checkCredits();
			api.checkAuthorization();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.get(ImgurApi.API_URL + "account/me/images/ids/{page}")
						.header("Authorization", api.getHeader())
						.routeParam("page", Integer.toString(page))
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
			JSONArray raw_list = raw.optJSONArray("data");
			return api.dump(raw_list, String.class);
		}
		
		
		//Returns the number of images uploaded by the user.
		public static int getImageCount() throws RateLimitException, HTTPRequestException, AuthorizationException {
			api.checkCredits();
			api.checkAuthorization();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.get(ImgurApi.API_URL + "account/me/images/count")
						.header("Authorization", api.getHeader())
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
			return raw.optInt("data");
		}
		
		
		//Deletes the image tied to the given deletehash.
		public static void deleteImage(String deletehash) throws RateLimitException, HTTPRequestException, AuthorizationException {
			api.checkCredits();
			api.checkAuthorization();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.delete(ImgurApi.API_URL + "account/me/image/{deletehash}")
						.header("Authorization", api.getHeader())
						.routeParam("deletehash", deletehash)
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
		}
		
		
		public static List<Notification> getReplies() throws RateLimitException, HTTPRequestException, AuthorizationException {
			return getReplies(true);
		}
		
		//Returns all the reply notifications for the logged in user. new_flag set to true for non-viewed notifications only, false for all notifications
		public static List<Notification> getReplies(boolean new_flag) throws RateLimitException, HTTPRequestException, AuthorizationException {
			api.checkCredits();
			api.checkAuthorization();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.get(ImgurApi.API_URL + "account/me/notifications/replies")
						.header("Authorization", api.getHeader())
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
			JSONArray raw_list = raw.optJSONArray("data");
			return api.dumpModels(raw_list, Notification.class);
		}
}
