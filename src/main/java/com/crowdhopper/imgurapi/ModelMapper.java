package com.crowdhopper.imgurapi;

import com.mashape.unirest.http.ObjectMapper;
import org.json.JSONObject;

import com.crowdhopper.imgurapi.Models.Basic;
import com.crowdhopper.imgurapi.Models.Model;

class ModelMapper implements ObjectMapper {
	@Override
	public String writeValue(Object value) {
		if(value instanceof Basic)
			return ((Basic<?>)value).toJson();
		else
			return ((Model)value).toJson();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T readValue(String value, Class<T> valueType) {
		JSONObject val = new JSONObject(value);
		Basic<JSONObject> base = new Basic<JSONObject>(val);
		if(valueType == Basic.class)
			return (T)base;
		else {
			try {
				Model retModel = (Model)valueType.newInstance();
				retModel.populate(base);
				return (T)retModel;
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
