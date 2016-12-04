package moe.crowdhopper.imgurapi.Models;

import org.json.JSONObject;

//Ensures that all Models have populate and toJson methods, allowing for serialization and deserialization
public abstract class Model {
	
	public abstract void populate();

	public abstract String toString();

	public abstract void factory(JSONObject data);
}
