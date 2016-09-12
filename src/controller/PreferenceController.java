package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import model.Interest;
import model.Music;
import model.MusicGender;
import persistence.UserPersistence;
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
		
		String paramsString = "name%2C"		
				+ "band_interests%2C"
				+ "band_members%2C"
				+ "bio%2C"				
				+ "category%2C"
				+ "category_list%2C"				
				+ "description%2C"
				+ "genre%2C"
				+ "id%2C"
				+ "influences%2C"
				+ "keywords%2C";
		
		params = paramsString.split("%2C");
		
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
		if(!musicJson.isNull("genre"))
			m.setGenre(musicJson.getString("genre"));
	 
		return m;
	}
	
	public ArrayList<MusicGender> getMusicGenders(List<Music> music){
		
		ArrayList<MusicGender> musicgGendersUnrelevants = new ArrayList<MusicGender>();
		ArrayList<MusicGender> musicgGendersWithRelevance = new ArrayList<MusicGender>();
		
		for (Music m : music) {
			musicgGendersUnrelevants.add(new MusicGender(m.getGenre()));
		}
		MusicGender mg = null;
		
		genderOuterLoop:
		for (Iterator<MusicGender> iterator = musicgGendersUnrelevants.iterator(); iterator.hasNext(); ){
		
			mg = iterator.next();
			
			for (MusicGender mgwr : musicgGendersWithRelevance) {
				
				if(mgwr.getName().equals(mg.getName())){
					mgwr.setRelevance(mgwr.getRelevance() + 1);
					iterator.remove();
					continue genderOuterLoop;
				}
				
			}	
			
			musicgGendersWithRelevance.add(mg);
		}	
		
		return musicgGendersWithRelevance;
		
	}
	
}
