package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import model.Photo;
import model.Size;
import utils.CustomComparator;
import utils.FbApp;

public class PhotoController {

	public List<Photo> getUserPhotos(JSONObject json, String accessToken) throws JSONException, IOException {
		
		List<Photo> allPhotos = getAllPhotos(json);		
		List<Photo> topTenPhotos = new ArrayList<Photo>();
		
		Collections.sort(allPhotos, new CustomComparator());
		
		for (int i = 0; i < 10; i++){
			topTenPhotos.add(allPhotos.get(i));
		}
		
		//get the size and urls of the photos
		topTenPhotos = getTopTenPhotosUrls(topTenPhotos, accessToken);		
		
		return topTenPhotos;
	}
	
	private List<Photo> getTopTenPhotosUrls(List<Photo> topTenPhotos, String accessToken) throws IOException, JSONException{
		
		JSONObject sizesJson;
		List<Photo> result = new ArrayList<Photo>();
		
		for (Photo photo : topTenPhotos) {
			sizesJson = FbApp.httpGetRequest(FbApp.buildUrlToGetPhoto(String.valueOf(photo.getId()), accessToken));
			
			for (int i = 0; i < sizesJson.getJSONArray("images").length(); i++) {
				JSONObject node = sizesJson.getJSONArray("images").getJSONObject(i);
				
				List<Size> sizes = new ArrayList<Size>();
				sizes.add(new Size(node.getInt("height"), node.getInt("width"), node.getString("source")));
				
				photo.setSizes(sizes);
			}
			result.add(photo);
		}
		
		return result;				
	}
	
	private List<Photo> getAllPhotos(JSONObject json) throws JSONException{
		List<Photo> list = new ArrayList<Photo>();
		
		JSONArray albumsJson = json.getJSONObject("albums").getJSONArray("data");
				
		for (int i = 0; i < albumsJson.length(); i++) {
			
			//Se não encontrar um objeto "photos" no json do album 
			if(!albumsJson.getJSONObject(i).has("photos"))
				if(albumsJson.getJSONObject(i).has("id"))
					break;
				else throw new JSONException("NÃO EXISTE OBJETO AQUI");

			JSONArray photosJson = albumsJson.getJSONObject(i).getJSONObject("photos").getJSONArray("data");
		
			for (int j = 0; j < photosJson.length(); j++) {
				
				Photo p = new Photo();
				p.setId(photosJson.getJSONObject(j).getLong("id"));
				
				p.setLikes(photosJson.getJSONObject(j)
						.getJSONObject("likes").getJSONObject("summary").getInt("total_count"));
				
				list.add(p);				
			}			
		}		
		return list;		
	}

}
