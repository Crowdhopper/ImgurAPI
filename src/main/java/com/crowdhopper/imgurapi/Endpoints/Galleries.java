package com.crowdhopper.imgurapi.Endpoints;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.crowdhopper.imgurapi.ImgurApi;
import com.crowdhopper.imgurapi.Exceptions.*;
import com.crowdhopper.imgurapi.Models.Comment;
import com.crowdhopper.imgurapi.Models.Gallery;
import com.crowdhopper.imgurapi.Models.GalleryAlbum;
import com.crowdhopper.imgurapi.Models.GalleryImage;
import com.crowdhopper.imgurapi.Models.Tag;
import com.crowdhopper.imgurapi.Models.TagVote;
import com.crowdhopper.imgurapi.Models.Vote;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

public class Galleries extends Endpoint {
	private static ImgurApi api = null;
	
	public static void initialize(ImgurApi new_api) {
		api = new_api;
	}
	
	//Retrieves the gallery.
		public static List<Gallery> getGallery(String section, String sort, int page, String window, Boolean is_viral) 
				throws IllegalArgumentException, HTTPRequestException, RateLimitException {
			if(section == null)
				section = "hot";
			if(sort == null)
				sort = "viral";
			if(window == null)
				window = "day";
			if(is_viral == null)
				is_viral = true;
			api.checkCredits();
			api.checkParameters(new String[] {"hot", "top", "user"}, section, "Section");
			api.checkParameters(new String[] {"top", "viral", "time", "rising"}, sort, "Sort");
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.get(ImgurApi.API_URL + "gallery/{section}/{sort}/{window}/{page}?showViral={is_viral}")
						.header("Authorization", api.getHeader())
						.routeParam("section", section)
						.routeParam("sort", sort)
						.routeParam("window", window)
						.routeParam("page", Integer.toString(page))
						.routeParam("is_viral", Boolean.toString(is_viral))
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
			JSONArray raw_list = raw.optJSONArray("data");
			return api.dumpGallery(raw_list);
		}
		
		
		//Returns the meme subgallery.
		public static List<Gallery> getMemeSubgallery(String sort, int page, String window) 
				throws IllegalArgumentException, RateLimitException, HTTPRequestException {
			if(sort == null)
				sort = "viral";
			if(window == null)
				window = "week";
			api.checkParameters(new String[] {"top", "viral", "time"}, sort, "Sort");
			api.checkCredits();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.get(ImgurApi.API_URL + "g/memes/{sort}/{window}/{page}")
						.header("Authorization", api.getHeader())
						.routeParam("sort", sort)
						.routeParam("window", window)
						.routeParam("page", Integer.toString(page))
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
			JSONArray raw_list = raw.optJSONArray("data");
			return api.dumpGallery(raw_list);
		}
		
		
		@Deprecated
		//Deprecated due to the fact that it's in the endpoints from Imgur, but does not appear to work.
		//Returns an image from the meme subgallery
		public static GalleryImage getMemeImage(String image_id) throws RateLimitException, HTTPRequestException {
			api.checkCredits();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.get(ImgurApi.API_URL + "g/memes/{image_id}")
						.header("Authorization", api.getHeader())
						.routeParam("image_id", image_id)
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			System.out.println(raw);
			api.checkStatus(raw);
			JSONObject raw_data = raw.optJSONObject("data");
			return new GalleryImage(raw_data);
		}
		
		
		//Returns a subreddit's subgallery, given the subreddit.
		public static List<GalleryImage> getSubredditGallery(String subreddit, String sort, int page, String window) 
				throws RateLimitException, HTTPRequestException, IllegalArgumentException {
			if(sort == null)
				sort = "time";
			if(window == null)
				window = "week";
			api.checkParameters(new String[] {"top", "time"}, sort, "Sort");
			api.checkCredits();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.get(ImgurApi.API_URL + "gallery/r/{subreddit}/{sort}/{window}/{page}")
						.header("Authorization", api.getHeader())
						.routeParam("subreddit", subreddit)
						.routeParam("sort", sort)
						.routeParam("window", window)
						.routeParam("page", Integer.toString(page))
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
			JSONArray raw_list = raw.optJSONArray("data");
			List<GalleryImage> image_list = new ArrayList<GalleryImage>();
			for(int i = 0; i < raw_list.length(); i++)
				image_list.add(new GalleryImage(raw_list.optJSONObject(i)));
			return  image_list;
		}
		
		
		//Returns the given image from the given subreddit gallery.
		public static GalleryImage getSubredditImage(String subreddit, String image_id) throws RateLimitException, HTTPRequestException {
			api.checkCredits();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.get(ImgurApi.API_URL + "gallery/r/{subreddit}/{image_id}")
						.header("Authorization", api.getHeader())
						.routeParam("subreddit", subreddit)
						.routeParam("image_id", image_id)
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
			JSONObject data = raw.optJSONObject("data");
			return new GalleryImage(data);
		}
		
		
		//Gets the information for the given tag.
		public static Tag getTagged(String tag, String sort, int page, String window) 
				throws HTTPRequestException, RateLimitException, IllegalArgumentException {
			if(sort == null)
				sort = "viral";
			if(window == null)
				window = "week";
			api.checkParameters(new String[] {"viral", "top", "time"}, sort, "Sort");
			api.checkCredits();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.get(ImgurApi.API_URL + "gallery/t/{t_name}/{sort}/{window}/{page}")
						.header("Authorization", api.getHeader())
						.routeParam("t_name", tag)
						.routeParam("sort", sort)
						.routeParam("window", window)
						.routeParam("page", Integer.toString(page))
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
			return new Tag(raw.optJSONObject("data"));
		}
		
		
		//Gets a single image from the given tag.
		public static GalleryImage getTaggedImage(String tag, String image_id) throws HTTPRequestException, RateLimitException {
			api.checkCredits();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.get(ImgurApi.API_URL + "gallery/t/{t_name}/{image_id}")
						.header("Authorization", api.getHeader())
						.routeParam("t_name", tag)
						.routeParam("image_id", image_id)
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
			return new GalleryImage(raw.optJSONObject("data"));
		}
		
		
		//Gets all the tags on the given gallery object.
		public static List<TagVote> getImageTags(String id) throws HTTPRequestException, RateLimitException {
			api.checkCredits();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.get(ImgurApi.API_URL + "gallery/{id}/tags")
						.header("Authorization", api.getHeader())
						.routeParam("id", id)
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
			JSONArray raw_list = raw.optJSONArray("data");
			return api.dumpModels(raw_list, TagVote.class);
		}
		
		
		//Votes on the given tag for the image.
		public static void voteTag(String id, String tag, String vote) 
				throws HTTPRequestException, RateLimitException, IllegalArgumentException, AuthorizationException {
			api.checkCredits();
			api.checkPosts();
			api.checkAuthorization();
			api.checkParameters(new String[] {"up", "down"}, vote, "Vote");
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.post(ImgurApi.API_URL + "gallery/{id}/vote/tag/{t_name}/{vote}")
						.header("Authorization", api.getHeader())
						.routeParam("id", id)
						.routeParam("t_name", tag)
						.routeParam("vote", vote)
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			api.refreshPost(response.getHeaders());
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
		}
		
		
		//Searches the gallery using simple gallery search.
		// Supports boolean operators AND, OR, and NOT; as well as the indices tag:, user:, title:, ext:, subreddit:, album:, meme:
		public static List<Gallery> simpleSearch(String query, String sort, int page, String window) 
				throws HTTPRequestException, RateLimitException, IllegalArgumentException {
			if(sort == null)
				sort = "time";
			if(window == null)
				window = "all";
			api.checkParameters(new String[] {"viral", "top", "time"}, sort, "Sort");
			api.checkCredits();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.get(ImgurApi.API_URL + "gallery/search/{sort}/{window}/{page}")
						.header("Authorization", api.getHeader())
						.routeParam("sort", sort)
						.routeParam("window", window)
						.routeParam("page", Integer.toString(page))
						.queryString("q", query)
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
			JSONArray raw_list = raw.optJSONArray("data");
			return api.dumpGallery(raw_list);
		}
		
		
		/*Uses advanced search features.
		 * All of these do not need to be present in the Map, just the ones you need. If you don't need any of them, use simple search instead.
		 * Each of these can use boolean operators and indices like simple search can.
		 * q_all: Looks for all these
		 * q_any: Looks for any of these
		 * q_exactly: Looks for this exact phrase.
		 * q_not: Exclude these results
		 * q_type: Has to be one of jpg | png | gif | anigif (animated gif) | album
		 * q_size_px: Specifies a pixel size range, use one of 
		 * small (500 pixels square or less) | med (500 to 2,000 pixels square)
		 * big (2,000 to 5,000 pixels square) | lrg (5,000 to 10,000 pixels square) | huge (10,000 square pixels and above)
		 */
		public static List<Gallery> advanceSearch(Map<String, Object> query, String sort, int page, String window) 
				throws HTTPRequestException, RateLimitException, IllegalArgumentException {
			if(sort == null)
				sort = "time";
			if(window == null)
				window = "all";
			api.checkParameters(new String[] {"viral", "top", "time"}, sort, "Sort");
			api.checkCredits();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.get(ImgurApi.API_URL + "gallery/search/{sort}/{window}/{page}")
						.header("Authorization", api.getHeader())
						.routeParam("sort", sort)
						.routeParam("window", window)
						.routeParam("page", Integer.toString(page))
						.queryString(query)
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
			JSONArray raw_list = raw.optJSONArray("data");
			return api.dumpGallery(raw_list);
		}
		
		
		//Page from 1-50. Does exactly what it says. Refreshes every hour.
		public static List<Gallery> randomGallery(int page)
				throws HTTPRequestException, RateLimitException {
			api.checkCredits();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.get(ImgurApi.API_URL + "gallery/random/random/{page}")
						.header("Authorization", api.getHeader())
						.routeParam("page", Integer.toString(page))
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
			JSONArray raw_list = raw.optJSONArray("data");
			return api.dumpGallery(raw_list);
		}
		
		
		//Share an album or image to the gallery.
		public static void shareToGallery(String id, String title, String topic, boolean mature)
				throws HTTPRequestException, RateLimitException, IllegalArgumentException, AuthorizationException {
			api.checkCredits();
			api.checkPosts();
			api.checkAuthorization();
			if(title == null)
				throw new IllegalArgumentException("Title cannot be null.");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("title", title);
			if(topic != null)
				params.put("topic", topic);
			params.put("mature", mature ? 1 : 0);
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.post(ImgurApi.API_URL + "gallery/{id}")
						.header("Authorization", api.getHeader())
						.routeParam("id", id)
						.fields(params)
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			api.refreshPost(response.getHeaders());
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
		}
		
		
		//Deletes the given album/image from the gallery.
		public static void deleteFromGallery(String id)
				throws HTTPRequestException, RateLimitException, AuthorizationException {
			api.checkCredits();
			api.checkAuthorization();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.delete(ImgurApi.API_URL + "gallery/{id}")
						.header("Authorization", api.getHeader())
						.routeParam("id", id)
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
		}
		
		
		//Gives information about a specific album.
		public static GalleryAlbum getAlbum(String id)
				throws HTTPRequestException, RateLimitException {
			api.checkCredits();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.get(ImgurApi.API_URL + "gallery/album/{id}")
						.header("Authorization", api.getHeader())
						.routeParam("id", id)
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
			return new GalleryAlbum(raw.optJSONObject("data"));
		}
		
		
		//Gives information about a specific image.
		public static GalleryImage getImage(String id)
				throws HTTPRequestException, RateLimitException {
			api.checkCredits();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.get(ImgurApi.API_URL + "gallery/image/{id}")
						.header("Authorization", api.getHeader())
						.routeParam("id", id)
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
			return new GalleryImage(raw.optJSONObject("data"));
		}
		
		
		//Gives information about the given gallery object. Gives a generic object, use if you don't know whether it's an image or album.
		public static Gallery getItem(String id)
				throws HTTPRequestException, RateLimitException {
			api.checkCredits();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.get(ImgurApi.API_URL + "gallery/image/{id}")
						.header("Authorization", api.getHeader())
						.routeParam("id", id)
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
			JSONObject data = raw.optJSONObject("data");
			if(data.optBoolean("is_album"))
				return new GalleryAlbum(data);
			return new GalleryImage(data);
		}
		
		
		/*Report an item.
		 * Possible values for reason are:
		 * 0: No Reason
		 * 1: Doesn't belong on Imgur
		 * 2: Spam
		 * 3: Abusive
		 * 4: Mature content that isn't tagged as such.
		 * 5: Pornography
		 */
		public static void reportItem(String id, int reason)
				throws HTTPRequestException, RateLimitException, IllegalArgumentException {
			api.checkCredits();
			api.checkPosts();
			api.checkParameters(new String[] {"0", "1", "2", "3", "4", "5"}, Integer.toString(reason) , "Reason");
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.post(ImgurApi.API_URL + "gallery/{id}/report")
						.header("Authorization", api.getHeader())
						.routeParam("id", id)
						.field("reason", reason)
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			api.refreshPost(response.getHeaders());
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
		}
		
		
		//Gets the vote information on given post.
		public static Vote getVotes(String id)
				throws HTTPRequestException, RateLimitException {
			api.checkCredits();
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.get(ImgurApi.API_URL + "gallery/{id}/votes")
						.header("Authorization", api.getHeader())
						.routeParam("id", id)
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
			return new Vote(raw); 
		}
		
		
		//Vote on a post, vote should be "up" or "down"
		public static void vote(String id, String vote)
				throws HTTPRequestException, RateLimitException, IllegalArgumentException {
			api.checkCredits();
			api.checkPosts();
			api.checkParameters(new String[] {"up", "down"}, vote, "Vote");
			HttpResponse<JsonNode> response = null;
			try {
				response = Unirest.post(ImgurApi.API_URL + "gallery/{id}/vote/{vote}")
						.header("Authorization", api.getHeader())
						.routeParam("id", id)
						.routeParam("vote", vote)
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			api.refreshPost(response.getHeaders());
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
		}
		
		
		//Gets all of the comments on a post.
		public static List<Comment> getComments(String id, String sort)
				throws RateLimitException, HTTPRequestException {
			api.checkCredits();
			if(sort == null)
				sort = "best";
			HttpResponse<JsonNode> response = null;
			try {
				 response = Unirest.get(ImgurApi.API_URL + "gallery/{id}/comments/{sort}")
						 .header("Authorization", api.getHeader())
						 .routeParam("id", id)
						 .routeParam("sort", sort)
						 .asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			JSONObject raw = response.getBody().getObject();
			api.checkStatus(raw);
			JSONArray raw_list = raw.optJSONArray("data");
			return api.dumpModels(raw_list, Comment.class);
		}
		
		
		//Get the IDs for all the comments on the given post.
		public static List<Integer> getCommentIds(String id)
				throws RateLimitException, HTTPRequestException {
			List<Comment> comments = getComments(id, null);
			List<Integer> ids = new ArrayList<Integer>();
			for(Comment c: comments)
				ids.add(c.getId());
			return ids;
		}
		
		
		//Gets the number of comments on the given post.
		public static int getCommentCount(String id)
				throws RateLimitException, HTTPRequestException {
			return (getComments(id, null).size());
		}
}
