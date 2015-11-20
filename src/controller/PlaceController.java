package controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.view.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import model.*;

public class PlaceController {
	
	GeoLocationController gc = new GeoLocationController();

	public List<Place> buildPlaces(JSONObject places) throws JSONException {
		
		JSONArray data;	
		Interest interest = null;
	    List<Place> list = new ArrayList<Place>();
	    
	    data = places.getJSONObject("tagged_places").getJSONArray("data");
		
	    for (int i = 0; i < data.length(); i++) {
			Place place = new Place();
			JSONObject node = data.getJSONObject(i);			
			
			place.setId(Long.parseLong(node.getString("id")));
			place.setCreated_time(node.getString("created_time"));
			
			GeoLocal location = gc.biuldLocation(node.getJSONObject("place"));
			
			place.setLocation(location);
			
			
		}
	    
        return list;	
	}

}

