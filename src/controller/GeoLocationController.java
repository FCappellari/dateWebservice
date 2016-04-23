package controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import model.GeoLocal;

public class GeoLocationController {

	public GeoLocal buildLocation(JSONObject json) throws JSONException {
		
		GeoLocal local =  new GeoLocal();			
		JSONArray data = json.getJSONArray("place");
		
		//comentario teste branch
		
		for (int i = 0; i < data.length(); i++) {
			
		}
		
		return local;
	}

}
