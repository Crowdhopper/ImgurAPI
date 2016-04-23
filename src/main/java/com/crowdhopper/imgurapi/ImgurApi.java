package com.crowdhopper.imgurapi;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import org.json.JSONObject;
import java.util.LinkedHashMap;
import java.util.Map;

//To Do- Actual documentation (pre-/post-conditions for methods, that kind of thing.)

public class ImgurApi {
	private static final String AUTH_URL = "https://api.imgur.com/oauth2/token";
	private boolean auth; //Flag indicating use of authorized account.
	private String headerVal; // The value of the authorization header.
	private String refresh_token = null;
	private String username = null;
	private String ID;
	private String secret;
	
	//Constructs a new API object given only a client ID and secret
	public ImgurApi(String clientID, String clientSecret) {  //Shorter constructor for use in applications not using authentication
		this(clientID, clientSecret, null, null);
	}
	
	
	/*Constructs a new API object given an ID and secret, as well as an authorization token, and what the token corresponds to
	  	auth_type - "token", "pin", or "code", indicating that token is a refresh token, a pin number, or an authorization code.
	  	token - the refresh token, pin number, or authorization code needed to gain the access token.
	 */
	public ImgurApi(String clientID, String clientSecret, String token, String auth_type) {
		Unirest.setObjectMapper(new ModelMapper());
		this.ID = clientID;
		this.secret = clientSecret;
		if(auth_type == null) {
			this.auth = false;
			headerVal = String.format("Client-ID %s", ID);
			return;
		}
		this.auth = true;
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
		HttpResponse<JsonNode> httpResponse = null;
		try {
			httpResponse = 
					Unirest.post(AUTH_URL)
					.fields(fields)
					.asJson();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
		JSONObject jsonResponse = httpResponse.getBody().getObject();
		refresh_token = jsonResponse.getString("refresh_token");
		headerVal = String.format("Bearer %s", jsonResponse.getString("access_token"));
		username = jsonResponse.getString("account_username");
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
		headerVal = String.format("Bearer %s", response.getString("access_token"));
	}
	
	public boolean getAuth() {return auth;}
	public String getName() {return username;}
	public String getHeader() {return headerVal;}
	
	
}
