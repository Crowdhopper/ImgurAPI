package moe.crowdhopper.imgurapi.Endpoints;


import java.util.List;

import moe.crowdhopper.imgurapi.Exceptions.RateLimitException;
import moe.crowdhopper.imgurapi.ImgurApi;
import org.json.JSONObject;

import moe.crowdhopper.imgurapi.Exceptions.HTTPRequestException;
import moe.crowdhopper.imgurapi.Models.Image;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class MemeGen extends Endpoint {
	private static ImgurApi api = null;
	
	public static void initialize(ImgurApi new_api) {
		api = new_api;
	}
	
	//Provides a list of the default memes.
	public static List<Image> getMemes()
			throws RateLimitException, HTTPRequestException {
		api.checkCredits();
		HttpResponse<JsonNode> response = null;
		try {
			response = Unirest.get(ImgurApi.API_URL + "memegen/defaults")
					.header("Authorization", api.getHeader())
					.asJson();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
		JSONObject raw = response.getBody().getObject();
		api.checkStatus(raw);
		return api.dumpModels(raw.optJSONArray("data"), Image.class);
	}
}
