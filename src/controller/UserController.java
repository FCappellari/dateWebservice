package controller;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import model.Music;
import model.Photo;
import model.Sugestion;
import model.User;
import persistence.UserPersistence;
import utils.FbApp;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class UserController {
		static MongoClientURI uri  = new MongoClientURI("mongodb://sa:sa@ds045054.mongolab.com:45054/teste"); 
		static MongoClient client = new MongoClient(uri);
		static Morphia morphia = new Morphia().map(User.class);
		
		Datastore ds = morphia.createDatastore(client, "teste");
	    private Gson gson = new Gson(); 
	    private UserPersistence db = new UserPersistence(ds);
	    private String accessToken;
	    private ArrayList<String> interestParams;
	    
	    public User findUserById(long id){	    		    	
	    	return db.findById(id); 	    	      
	    }  

	    public String findAllOrderedByName() {	    	
	    	
	    	List<User> users = db.findAll();	    	
	    	return gson.toJson(users);	    	      
	    }	

		public JSONObject createUserJson(String shortAccessToken) throws IOException, JSONException {
			  
			accessToken = extendAccessToken(shortAccessToken);
			//mudar para get
			ArrayList<String> interestParams = setInterestsParams();		
			ArrayList<String> placeParams    = setPlaceParams();
			
			JSONObject basicDataJson = getUserBasicDataAsJson();
			JSONObject interestsJson = getUserInterestsAsJson(interestParams);			
			JSONObject placesJson    = getUserPlacesAsJson(placeParams);
			JSONObject pictureJson   = getUserPictureAsJson();
			JSONObject coverPicJson  = getUserPictureCoverAsJson("cover");
			JSONObject photosJson    = getUserPhotosAsJson();						

			
			InterestController    interestController =  new InterestController();
			PlaceController       placeController =  new PlaceController();
			PhotoController       photoController = new PhotoController();
			PreferenceController  preferenceController = new PreferenceController();
			

			
			List<Photo> userPhotos = photoController.getUserPhotos(photosJson, accessToken);
			
			User user = new User();
			
			user.setName(basicDataJson.getString("name"));
			user.setFbId(basicDataJson.getLong("id"));
			user.setGender(basicDataJson.getString("gender"));			
			
			user.setPhotos(userPhotos);
			
			user.setInterests(interestController.biuldInterests(placesJson.getJSONObject("tagged_places"), "tagged_places"));
			
			for (int i = 0; i < interestParams.size(); i++) {
				try{
					user.getInterests().addAll(interestController.biuldInterests(interestsJson.getJSONObject(interestParams.get(i)), (interestParams.get(i))));
				}catch(JSONException e){
					System.out.print("AQUI È NOIS");
				}
			}		
			
			List<Music> music = preferenceController.getMusic(user.getInterests(), accessToken);
			
			user.setMusic(music);
			
			//user.setPicture(convertPicToByte(getUserPicture()));		
			
			user.setPictureUrl(getUserPictureAsUrl());
			user.setCoverPictureUrl(coverPicJson.getJSONObject("cover").getString("source"));
			
			db.CreateUser(user);
			
			return getProfile(user);			
		}

		private JSONObject getUserPictureCoverAsJson(String param) throws IOException {
			return FbApp.httpGetRequest(FbApp.buildUrl(param, accessToken));			
		}

		private String getUserPictureAsUrl() {
			String url = FbApp.userPictureAsUrl(accessToken);
			return url;
		}

		private JSONObject getUserPictureAsJson() throws IOException {			
			return FbApp.httpGetRequest(FbApp.buildUrl("picture", accessToken));			
		}	
		
		public JSONObject getProfile(User user) throws JSONException {
			
			JSONObject userProfileJson = new JSONObject();
			userProfileJson.put("name", user.getName());		
			userProfileJson.put("gender", user.getGender());
			userProfileJson.put("location", user.getLocation());
			userProfileJson.put("pictureUrl", user.getPictureUrl());			
			//userProfileJson.put("coverPicUrl", user.getCoverPictureUrl());			
			
			userProfileJson.put("photos", getUserPhotos(user));
			
			return userProfileJson;
			
		}
		
		private JSONArray getUserPhotos(User user){
			
			List<Photo> photos = user.getPhotos();
			JSONArray photosJson = new JSONArray();
			
			for (Photo photo : photos) {
				photosJson.put(photo.getSizes().get(0).getUrl());
			}
			
			return photosJson;
		}
		
		

		private ArrayList<String> setPlaceParams() {
			
			ArrayList<String> params = new ArrayList<String>();
			params = new ArrayList<String>(); 
		    params.add("tagged_places");		
			
			return params;
		}

		private ArrayList<String> setInterestsParams() {
			
			interestParams = new ArrayList<String>();			 
			interestParams.add("music");
			interestParams.add("movies");			
			interestParams.add("books");
			//interestParams.add("sports");
			
			return interestParams;
		}

		private byte[] convertPicToByte(File userPicture) {			
			return new byte[(int) userPicture.length()];
		}

		private JSONObject getUserPlacesAsJson(ArrayList<String> placeParams) throws IOException {			
			return FbApp.httpGetRequest(FbApp.buildUrl(placeParams, accessToken));			
		}
		
		private File getUserPicture() throws IOException, JSONException {
			JSONObject pictureJson =  FbApp.httpGetRequest(FbApp.buildUrl("picture", accessToken));			
			
			return FbApp.httpGetPicture(pictureJson.getJSONObject("picture").getJSONObject("data").getString("url"));
		}

		private JSONObject getUserInterestsAsJson(ArrayList<String> interestsParams) throws IOException {			
			
			String url;	
			
			url = FbApp.buildUrl(interestsParams, accessToken);
			JSONObject interests = FbApp.httpGetRequest(url);
			
			return interests;
		}

		private JSONObject getUserBasicDataAsJson() throws IOException {
			
			ArrayList<String> params = new ArrayList<String>(); 
			
			params.add("id");
			params.add("name");			
			params.add("gender");
			
			String url = FbApp.buildUrl(params, accessToken);
		
			JSONObject basicUserInformation = FbApp.httpGetRequest(url);
			
			return basicUserInformation;
		}

		public String getUserProfleById(long id) throws JSONException {
			
			User user = db.findById(id);
			SugestionController sugestionController = new SugestionController();
			
			List<Sugestion> sugestions = sugestionController.getUserSugestion(user);
			
						
			JSONObject profileJson = getProfile(user);
									
			return profileJson.toString();
		}
		
		public String extendAccessToken(String shortAccessToken) throws IOException, JSONException {	
			
			String urlString = "https://graph.facebook.com/v2.5"
			  		   + "/oauth/access_token?grant_type=fb_exchange_token&"
			           + "client_id=" + FbApp.getAppId() + "&"
			           + "client_secret=" + FbApp.getAppSecret() + "&"
			           + "fb_exchange_token=" + shortAccessToken;			
			
			return FbApp.httpGetRequest(urlString).getString("access_token");		
		}

		public boolean hasUser(long id) {
			return db.hasUser(id);	
		}
		
		private JSONObject getUserPhotosAsJson() throws IOException{
			return FbApp.httpGetRequest(FbApp.biuldPhotosUrl(accessToken));
		}
}
