package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
		System.out.println("getUserPhotos " + new Date().toString());
		topTenPhotos = getTopTenPhotosUrls(topTenPhotos, accessToken);		
		System.out.println("getUserPhotos2 " + new Date().toString());
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
	
	public List<Photo> updatePhoto(int index, String base64, List<Photo> photos){
		int i = 0;		
		
		for (Photo p : photos) {
			if(i==index){
				p.setUrl("");
				p.setBase64(base64);
			}
			i++;
		}
		return photos;
	}
	
/*
 * Metodo responsavel por tratar as imagens de forma binaria
 * **Descontinuado**
 * 
    public byte[] LoadImage(String filePath) throws Exception {

    	File file = new File(filePath);
        int size = (int)file.length();
        byte[] buffer = new byte[size];
        FileInputStream in = new FileInputStream(file);
        in.read(buffer);
        in.close();
        return buffer;
        
    }
    
    public boolean saveImage(byte[] image) throws Exception {
    
    	byte[] imageBytes = image;	        	        
        
        //Create GridFS object
        GridFS fs = new GridFS( db );
        //Save image into database
        GridFSInputFile in = fs.createFile( imageBytes );
        in.save();
        //Find saved image
        GridFSDBFile out = fs.findOne( new BasicDBObject( "_id" , in.getId() ) );
        //Save loaded image from database into new image file
        FileOutputStream outputImage = new FileOutputStsream("C:/Temp/bearCopy.bmp");
        out.writeTo( outputImage );
        outputImage.close();
        
		return false;
    }
*/
	
}
