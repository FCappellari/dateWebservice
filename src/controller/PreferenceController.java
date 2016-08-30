package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import model.Interest;
import model.Music;
import utils.FbApp;

public class PreferenceController {

	public void getPreference(String preference, JSONObject interestsJson, String accessToken) {

	}

	public List<Music> getMusic(List<Interest> interests, String accessToken) throws IOException, JSONException {
		// TODO Auto-generated method stub
		
		Interest aux = new Interest();
		
		aux.setName("	");	
		
		String[] params;
		
		List<Music> listMusic = new ArrayList<Music>();
		
		params = "name%2Cartists_we_like%2Cawards%2Cband_interests%2Cband_members%2Cbio%2Cbirthday%2Cbooking_agent%2Ccan_post%2Ccategory%2Ccategory_list%2Ccompany_overview%2Cculinary_team%2Ccurrent_location%2Cdescription%2Cdescription_html%2Cfood_styles%2Cfounded%2Cgeneral_info%2Cgenre%2Chometown%2Chours%2Cid%2Cinfluences%2Ckeywords%2Clocation%2Cmission%2Cmpg%2Cnetwork%2Cnew_like_count%2Cfeatures%2Cemails%2Cparking%2Cpersonal_info%2Cpersonal_interests%2Cpharma_safety_info%2Cphone%2Cprice_range%2Cproduced_by%2Cproducts%2Cpublic_transit%2Crecord_label%2Crestaurant_services%2Crestaurant_specialties%2Cschedule%2Cscreenplay_by%2Cseason%2Cstarring%2Cstudio%2Ctalking_about_count%2Ccover%2Cdirected_by%2Cwritten_by%2Clink%2Calbums".split("%2C");
		List<String> ids = new ArrayList<String>();
		
		//FbApp.buildUrl(params, accessToken, id);
		System.out.println("PreferenceController " +new Date().toString());
		for (Interest i : interests) {
			if(i.getName().equals("music")){
				for (Interest j : i.getChildren()) {
					if(j.getName().equals("id"))
						ids.add(String.valueOf(j.getLongValue()));
					else continue;
				}
			}	
		}
		System.out.println("PreferenceController " +new Date().toString());
		for (String id : ids) {
			String url = FbApp.buildUrl(params, accessToken, id);
			listMusic.add(getMusic(FbApp.httpGetRequest(url)));
		}
		System.out.println("PreferenceController " +new Date().toString());
		return listMusic;		
	}

	private Music getMusic(JSONObject musicJson) throws JSONException {
		
		Music m =  new Music();
		
		if(!musicJson.isNull("name"))
			m.setName(musicJson.getString("name"));
		if(!musicJson.isNull("band_members"))
			m.setBand_interests(musicJson.getString("band_members"));
		if(!musicJson.isNull("bio"))
			m.setBio(musicJson.getString("bio"));
		if(!musicJson.isNull("category"))
			m.setCategory(musicJson.getString("category"));		
	 
		return m;
	}
}
