package com.crowdhopper.imgurapi;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.http.Headers;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

import org.reflections.Reflections;

import org.json.JSONObject;
import org.json.JSONArray;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import com.crowdhopper.imgurapi.Models.*;
import com.crowdhopper.imgurapi.Endpoints.*;
import com.crowdhopper.imgurapi.Exceptions.*;

//To Do- Actual documentation (pre-/post-conditions for methods, that kind of thing.)

public class ImgurApi {
	public static final String API_URL = "https://api.imgur.com/3/";
	public static final String AUTH_URL = "https://api.imgur.com/oauth2/token";
	private static long post_reset_time;
	private static int posts = 1250; //The number of post requests remaining for the application
	private static int credit_floor = 1;
	private boolean commercial = false;  //Indicates whether the application is for commercial purposes or not.
	private boolean auth; //Flag indicating use of authorized account.
	private String header_val; // The value of the authorization header.
	private String refresh_token = null;
	private String username = null;
	private String ID;
	private String secret;
	
	//Constructs a new API object given only a client ID and secret
	public ImgurApi(String client_id, String client_secret) {  //Shorter constructor for use in applications not using authentication
		this(client_id, client_secret, null, null);
	}
	
	/*Constructs a new API object given an ID and secret, as well as an authorization token, and what the token corresponds to
	  	auth_type - "token", "pin", or "code", indicating that token is a refresh token, a pin number, or an authorization code.
	  	token - the refresh token, pin number, or authorization code needed to gain the access token.
	 */	
	public ImgurApi(String client_id, String client_secret, String token, String auth_type) {
		this.ID = client_id;
		this.secret = client_secret;
		if(auth_type == null) {
			auth = false;
			header_val = String.format("Client-ID %s", ID);
			HttpResponse<JsonNode> response = null;
			try {
				response = 
						Unirest.post(AUTH_URL)
						.asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			initPost(response.getHeaders());
			refreshEndpoints();
			return;
		}
		auth = true;
		if(auth_type.equals("token")) {
			auth_type = "refresh_token";
		}
		Map<String, Object> fields = new LinkedHashMap<String, Object>();
		fields.put(auth_type, token);
		fields.put("client_id", ID);
		fields.put("client_secret", secret);
		if(auth_type.equals("code"))
			fields.put("grant_type", "authorization_code");
		else
			fields.put("grant_type", auth_type);
		HttpResponse<JsonNode> response = null;
		try {
			response = 
					Unirest.post(AUTH_URL)
					.fields(fields)
					.asJson();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
		
		JSONObject raw = response.getBody().getObject();
		refresh_token = raw.getString("refresh_token");
		header_val = String.format("Bearer %s", raw.getString("access_token"));
		username = raw.getString("account_username");
		initPost(response.getHeaders());
		refreshEndpoints();
	}
	
	public ImgurApi(String client_id, String client_secret, boolean commercial) {
		this(client_id, client_secret);
		this.commercial = commercial;
	}
	
	public ImgurApi(String client_id, String client_secret, String token, String auth_type, boolean commercial) {
		this(client_id, client_secret, token, auth_type);
		this.commercial = commercial;
	}
	
	//Uses the refresh token to gain a new access token.
	public void refreshAuth() {
		if(refresh_token == null)
			return;
		Map<String, Object> fields = new LinkedHashMap<String, Object>();
		fields.put("client_id", ID);
		fields.put("client_secret", secret);
		fields.put("refresh_token", refresh_token);
		fields.put("grant_type", "refresh_token");
		HttpResponse<JsonNode> refreshResponse = null;
		try {
			refreshResponse = 
					Unirest.post(AUTH_URL)
					.fields(fields)
					.asJson();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
		JSONObject response = refreshResponse.getBody().getObject();
		header_val = String.format("Bearer %s", response.getString("access_token"));
	}
	
	public void refreshEndpoints() {
		Reflections reflect = new Reflections("com.crowdhopper.imgurapi.Endpoints");
		Set<Class<? extends Endpoint>> endpoints = reflect.getSubTypesOf(Endpoint.class);
		for(Class<? extends Endpoint> c: endpoints) {
			try {
				Method init = c.getMethod("initialize", ImgurApi.class);
				init.invoke(null, this);
			} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean getAuth() {return auth;}
	public String getName() {return username;}
	public String getHeader() {return header_val;}
	
	public int getCredits() {
		HttpResponse<JsonNode> response = null;
		try {
			response = Unirest.get(API_URL + "credits")
					.header("Authorization", header_val)
					.asJson();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
		JSONObject body = response.getBody().getObject().optJSONObject("data");
		return body.optInt("ClientRemaining");
	}
	public int getUserCredits() {
		HttpResponse<JsonNode> response = null;
		try {
			response = Unirest.get(API_URL + "credits")
					.header("Authorization", header_val)
					.asJson();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
		JSONObject body = response.getBody().getObject().optJSONObject("data");
		return body.optInt("UserRemaining");
	}
	
	public void setFloor(int new_floor) {
		if(new_floor < 0)
			return;
		credit_floor = new_floor;
	}
	
	public void initPost(Headers headers) {
		post_reset_time = ((System.currentTimeMillis() / 1000) + Long.parseLong(headers.getFirst("X-Post-Rate-Limit-Reset")));
		posts = Integer.parseInt(headers.getFirst("X-Post-Rate-Limit-Remaining"));
	}
	
	//Automatically refreshes POST allotment
	public void refreshPost(Headers headers) {
		if(commercial)
			return;
		post_reset_time = (System.currentTimeMillis() / 1000) + Long.getLong(headers.getFirst("X-Post-Rate-Limit-Reset"));
		posts = Integer.parseInt(headers.getFirst("X-Post-Rate-Limit-Remaining"));
	}
	
	//Determines if there's enough credits left allotted to complete the given action
	public void checkCredits(int cred_change) throws RateLimitException {
		HttpResponse<JsonNode> response = null;
		try {
			response = Unirest.get(API_URL + "credits")
					.header("Authorization", header_val)
					.asJson();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
		JSONObject body = response.getBody().getObject().optJSONObject("data");
		int user_credits = body.optInt("UserRemaining");
		int total_credits = body.optInt("ClientRemaining");
		
		if(user_credits - cred_change < 1)
			throw new RateLimitException();
		if(total_credits - cred_change < credit_floor)
			throw new RateLimitException();
	}
	
	public void checkCredits() throws RateLimitException {
		checkCredits(1);
	}
	
	public void checkPosts() throws RateLimitException {
		if(commercial)
			return;
		if(posts <= 1 && System.currentTimeMillis() / 1000 < post_reset_time)
			throw new RateLimitException();
	}
	
	//Checks the http status code
	public void checkStatus(JSONObject raw) throws HTTPRequestException {
		if(!raw.optBoolean("success"))
			throw new HTTPRequestException(raw.optInt("status"));
	}
	
	public void checkAuthorization() throws AuthorizationException {
		if(!auth)
			throw new AuthorizationException();
	}
	
	public void checkParameters(String[] accepted, String param, String paramName) throws InvalidParameterException {
		String message = String.format("%s must be ", paramName);
		boolean acceptable = false;
		for(int i = 0; i < accepted.length; i++) {
			if(i != accepted.length - 1)
				message += String.format("\"%s,\" ", accepted[i]);
			if(param.equals(accepted[i]))
				acceptable = true;
		}
		message += String.format("or \"%s.\"", accepted[accepted.length - 1]);
		if(!acceptable)
			throw new InvalidParameterException(message);
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> dump(JSONArray raw_list, Class<T> type) {
		List<T> ret_list = new ArrayList<T>();
		if(raw_list == null)
			return ret_list;
		for(int i = 0; i < raw_list.length(); i++)
			ret_list.add((T)raw_list.opt(i));
		return ret_list;
	}
	
	public <T> List<T> dumpModels(JSONArray raw_list, Class<T> type) {
		List<T> ret_list = new ArrayList<T>();
		if(raw_list == null)
			return ret_list;
		for(int i = 0; i < raw_list.length(); i++) {
			try {
				T temp = type.newInstance();
				((Model)temp).factory(raw_list.optJSONObject(i));
				ret_list.add(temp);
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return ret_list;
	}
	
	public List<Gallery> dumpGallery(JSONArray raw_list) {
		List<Gallery> ret_list = new ArrayList<Gallery>();
		if(raw_list == null)
			return ret_list;
		for(int i = 0; i < raw_list.length(); i++) {
			JSONObject obj = raw_list.optJSONObject(i);
			if(obj.optBoolean("is_album"))
				ret_list.add(new GalleryAlbum(obj));
			else
				ret_list.add(new GalleryImage(obj));
		}
		return ret_list;
	}	
}