package com.crowdhopper.imgurapi;

import java.util.List;
import com.crowdhopper.imgurapi.Models.*;
import com.crowdhopper.imgurapi.Endpoints.*;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONObject;

//FLCLrox
//bpiq5uq

public class Test {
	public static void main(String[] args) {
		final String ID = "d5dbd6c530756ae";
		final String secret = "350f7fb4e5396dd011f49f281120a5523dea45a5";
		final String token = "8f2c459f3256e3fbe5b4d17bd04106e88067dc5f";
		new ImgurApi(ID, secret, token, "token");
		try {
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
