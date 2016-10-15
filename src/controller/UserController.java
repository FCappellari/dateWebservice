package controller;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.Years;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mongodb.morphia.Datastore;

import model.Interest;
import model.Match;
import model.Music;
import model.MusicGender;
import model.Photo;
import model.Place;
import model.Setting;
import model.Setting.SexPreference;
import model.SocialLink;
import model.Sugestion;
import model.Sugestion.STATUS;
import model.User;
import model.UserSocialLink;
import model.UserSocialLink.VISIBILITY;
import persistence.MongoDBHelper;
import persistence.UserPersistence;
import persistence.UserSocialLinkPersistence;
import utils.FbApp;

public class UserController {
		
		Datastore ds = MongoDBHelper.INSTANCE.getDatastore();	     
	    private UserPersistence db = new UserPersistence(ds);
	    private UserSocialLinkPersistence dbUserSocialLink = new UserSocialLinkPersistence(ds);
	    private String accessToken;
	    private ArrayList<String> interestParams;
	    
	    public User findUserById(long id){	    		    	
	    	return db.findById(id); 	    	      
	    }  
	    
	    public UserPersistence getDB(){
	    	return db;
	    }
	    
	 /*   public String findAllOrderedByName() {	    	
	    	
	    	List<User> users = db.findAll();	    	
	    	return gson.toJson(users);	    	      
	    }	
*/
	    private User prepareUser(Long id, JSONObject location) throws IOException, JSONException{
	    	
	    	//SocialLinkController scc = new SocialLinkController();	    			
	    	
	    	//scc.saveSocialLinkTeste();
	    	
	    	System.out.println(new Date().toString());
	    	
	    	ArrayList<String> interestParams = setInterestsParams();		
			ArrayList<String> placeParams    = setPlaceParams();
			List<Music> music = null;
			List<Place> places = null;
			ArrayList<MusicGender> musicGenders = new ArrayList<MusicGender>(); 
			
			JSONObject basicDataJson = getUserBasicDataAsJson();
			JSONObject interestsJson = getUserInterestsAsJson(interestParams);			
			JSONObject placesJson    = getUserPlacesAsJson(placeParams);
			JSONObject pictureJson   = getUserPictureAsJson();
			JSONObject coverPicJson  = getUserPictureCoverAsJson("cover");
			
			
			InterestController    interestController =  new InterestController();
			PlaceController       placeController =  new PlaceController();
			PhotoController       photoController = new PhotoController();
			PreferenceController  preferenceController = new PreferenceController();
			
			System.out.println(new Date().toString());		
			
			System.out.println(new Date().toString());
			
			User user;
			//Id null significa usuario novo
			if(id == null){
				user = new User();
			}else{
				user = db.findById(id);
			}
			
			user.setName(basicDataJson.getString("name"));
			user.setFbId(basicDataJson.getLong("id"));
			user.setGender(basicDataJson.getString("gender"));
			user.setAge(generateAge(basicDataJson.getString("birthday")));
			
			if(location!=null){				
				user.setLocation(location.getDouble("latitude"),location.getDouble("longitude"));					
			}else{
				user.setLocation(-29.1690939, -51.1945756);				
			}
				
			
			System.out.println(new Date().toString());			
			
			user.setInterests(interestController.biuldInterests(placesJson.getJSONObject("tagged_places"), "tagged_places"));
			System.out.println(new Date().toString());
			for (int i = 0; i < interestParams.size(); i++) {
				try{
					user.getInterests().addAll(interestController.biuldInterests(interestsJson.getJSONObject(interestParams.get(i)), (interestParams.get(i))));
				}catch(JSONException e){
					System.out.print("AQUI È NOIS");
				}
			}			
			
						
			music = preferenceController.getMusic(user.getInterests(), accessToken);
			places = preferenceController.getPlace(placesJson);
			musicGenders = preferenceController.getMusicGenders(music);
			
			user.setMusic(music);		
			user.setPlaces(places);
			user.setMusicGenders(musicGenders);
			//user.setPicture(convertPicToByte(getUserPicture()));		
			
			user.setPictureUrl(getUserPictureAsUrl());
			user.setCoverPictureUrl(coverPicJson.getJSONObject("cover").getString("source"));			
			System.out.println(new Date().toString());			
			
			return user;	    	
	    }
	    
	    public List<Photo> getTopTenUserPhotosFromFb() throws JSONException, IOException{
	    	
	    	JSONObject photosJson  = getUserPhotosAsJson();
	    	
	    	List<Photo> userPhotos = new PhotoController().getUserPhotos(photosJson, accessToken);
	    	
	    	return userPhotos;	    	
	    }
	    
		public boolean createUserJson(String shortAccessToken, JSONObject location) throws IOException, JSONException {
			  
			accessToken = extendAccessToken(shortAccessToken);
			
			/* 
			 * prepareUser()
			 * metodo comum para processos de criacao e atualizacao do usuario
			 * Processos especificos de criação ou atualização devem ser colocados 
			 * fora do metodo prepareUser 
			 */			
			User user = prepareUser(null, location);
			
			List<SocialLink> socialLinks;
			SocialLinkController scc = new SocialLinkController();
			user.setPhotos(getTopTenUserPhotosFromFb());
			//user.setSocialLinks(scc.getFirstSocialLink());
			
			boolean hasCreated = db.CreateUser(user);
			
			updateUserLastLogin(user.getFbId());
			
			return hasCreated;
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
		
		public JSONObject getProfile(User user, List<Interest> list) throws JSONException {
			
			String interestsAux = "";
			
			JSONObject userProfileJson = new JSONObject();
			userProfileJson.put("name", user.getName());		
			userProfileJson.put("gender", user.getGender());
			userProfileJson.put("location", user.getLocation());
			userProfileJson.put("pictureUrl", user.getPictureUrl());			
			userProfileJson.put("coverPicUrl", user.getCoverPictureUrl());					
			userProfileJson.put("photos", getUserPhotos(user));
			userProfileJson.put("bio", user.getBio());
			userProfileJson.put("latitude", user.getLocation().getLatitude());
			userProfileJson.put("longitude", user.getLocation().getLongitude());
			
			if(list != null){	
				JSONArray arrayIc = new JSONArray();				
				
				for (Interest interest : list) {
					JSONObject jic = new JSONObject();
					jic.put("name", interest.getName());
					jic.put("relevance", interest.getRelevance());
					arrayIc.put(jic);
				}				
				userProfileJson.put("interestsInConnom", arrayIc);
			}
			
			JSONArray arraySc = new JSONArray();			
			if(user.getSocialLinks() != null){			
			
				for (UserSocialLink sc : user.getSocialLinks()) {	
					JSONObject jsc  = new JSONObject();
					jsc.put("name", sc.getSocialLink().getName());
					jsc.put("id", sc.getSocialLink().getIdSocialLink());
					jsc.put("link", sc.getLink());
					jsc.put("visibility", sc.getVisibility());
					arraySc.put(jsc);
				}
				
				userProfileJson.put("socialLinks", arraySc);
				
			}
			return userProfileJson;
			
		}
		
		private JSONArray getUserPhotos(User user){
			
			List<Photo> photos = user.getPhotos();
			JSONArray photosJson = new JSONArray();
			List<Photo> aux = null;
			boolean pegarFotosFacebook = false;

			//MELHORA A PERFORMANCE MAS TRAS FOTOS DESATUALIZADAS
			boolean considerarBase = true; 
			if(considerarBase)
				for (Photo photo : photos) {
				
					/*
					 * Se o campo base64 esta nao vazio quer dizer que o usuario ja
					 * atualizaou a foto entao deve considerar a foto que o usuario atualizou 
					 * e nao a URL da foto do facebook
					 */		
					if((photo.getBase64()!= null)&&(!photo.getBase64().equals(""))){
						photosJson.put(photo.getBase64());
					}else{
						if(FbApp.testUrl(photo.getSizes().get(0).getUrl())){
							photosJson.put(photo.getSizes().get(0).getUrl());
						}else{
							pegarFotosFacebook = true;
							break;
						}					
					}
				}
			else pegarFotosFacebook = true;
			
			if(pegarFotosFacebook){
				try {
					photos = new PhotoController().getUserPhotos(getUserPhotosAsJson(user.getFbId()), accessToken);
				} catch (JSONException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				for (Photo photo : photos) {
					
					/*
					 * Se o campo base64 esta nao vazio quer dizer que o usuario ja
					 * atualizaou a foto entao deve considerar a foto que o usuario atualizou 
					 * e nao a URL da foto do facebook
					 */		
					if((photo.getBase64()!= null)&&(!photo.getBase64().equals(""))){
						photosJson.put(photo.getBase64());
					}else{				
						photosJson.put(photo.getSizes().get(0).getUrl());
					}
				}
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

			//nao colocar espaço nos parametros
			interestParams = new ArrayList<String>();
			interestParams.add("music"); 
			//interestParams.add("music");			
			/*interestParams.add("movies");			
			interestParams.add("books");*/
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
			params.add("birthday");
			
			String url = FbApp.buildUrl(params, accessToken);
		
			JSONObject basicUserInformation = FbApp.httpGetRequest(url);
			
			return basicUserInformation;
		}

		public String getUserProfleById(long sugestionId, long userId, String shortAccessToken) throws JSONException, IOException {
			System.out.println("begin getUserProfleById = " + new Date());
			
			accessToken = extendAccessToken(shortAccessToken);
			
			List<User> res = db.findById(userId, sugestionId); 
			User user = null;
			User sugestion = null;
			
			for (User u : res) {
				if(u.getFbId()==userId){
					user = u;					
				}else if(u.getFbId()==sugestionId){
					sugestion = u;
				}
			}			
			
			if(new SocialLinkController().getUserSocialLinks(sugestion) != null)
				user.setSocialLinks(new SocialLinkController().getUserSocialLinks(sugestion));			
			
			//user.setSocialLinks(socialLinks);
			SugestionController sugestionController = new SugestionController();
			
			//List<Sugestion> sugestions = sugestionController.getUserSugestion(user);
			
			JSONObject profileJson = getProfile(sugestion, getSugestionINterestsInConnom(user, sugestion));
			
			System.out.println("end getUserProfleById = " + new Date());
			
			return profileJson.toString();
		}
		
		private List<Interest> getSugestionINterestsInConnom(User user, User sugestion) {
			
			ArrayList<Interest> res = new ArrayList<Interest>();
			
			for (Sugestion s : user.getSugestions()) {
				if(s.getUser()==null) continue;
				if(sugestion.getFbId()==s.getUser().getFbId()){
					return s.getInterestsInConnom();
				}					
			}	
			return null;
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
		
		private JSONObject getUserPhotosAsJson(long id) throws IOException{
			return FbApp.httpGetRequest(FbApp.biuldPhotosUrl(id, accessToken));
		}		

		/* sugestao que vai pra tela*/
		public JSONArray getUserSugestoinsById(long id, String accessToken2) throws JSONException, IOException {

			accessToken = extendAccessToken(String.valueOf(accessToken2));
			User current = db.findById(id);
			SugestionController sugestionController = new SugestionController();
			List<Sugestion> sugestions = null;
			List<String> sugestionResult = new ArrayList<String>();
						
			if((current.getSugestions()==null)||current.getSugestions().isEmpty()){
				sugestions = sugestionController.getUserSugestion(current);
			}else{
				sugestions = sugestionController.getUserSugestion(current);
			}
			
			JSONArray sugestionsJson = new JSONArray();	
			
			for (Sugestion sugestion : sugestions) {				
				JSONObject my_obj = new JSONObject();
				my_obj.put("id", sugestion.getUser().getFbId());
				my_obj.put("name", sugestion.getUser().getName().split(" ")[0]); // primeiro nome
				my_obj.put("interestsInCommon", sugestion.getPreferencesInConnom());
				my_obj.put("photos", sugestion.getUser().getPhotos().get(0).getSizes().get(0).getUrl());
				my_obj.put("profilePic", sugestion.getUser().getPictureUrl(accessToken));
				my_obj.put("status", checkIfSugestionLikedCurrentUser(sugestion, current));
				my_obj.put("percentage", sugestion.getPercentage());
				sugestionsJson.put(my_obj);
			}		
			
			System.out.println("Antes UpdateLastLogin" + new Date().toString());
			
			updateUserLastLogin(current.getFbId());

			System.out.println("Depois UpdateLastLogin" + new Date().toString());
			return sugestionsJson;
		}

		private String checkIfSugestionLikedCurrentUser(Sugestion sugestion, User current) {
			
			List<Sugestion> ss = sugestion.getUser().getSugestions();
			
			if(ss==null)
				return STATUS.UNSET.toString();
			
			for (Sugestion s : ss) {		
				if(s.getUser() == null) continue;
				if(s.getUser().getFbId()==current.getFbId()){
					if(s.getStatus() == null)
						return STATUS.UNSET.toString();
					return s.getStatus().toString();
				}
			}
			
			return STATUS.UNSET.toString();
		}

		private void updateUserLastLogin(long id) {			
			
			db.updateUserLastLogin(id);
		}

		public boolean updateUser(long idUser, String shortAccessToken, JSONObject location) throws JSONException, IOException {			
			
			accessToken = extendAccessToken(shortAccessToken);
			
			if(true){
			//if(hasToUpdateBasedOnLastLogin(idUser)){						
				try {
					User user = prepareUser(idUser, location);
					
					return db.updateUser(idUser, user);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;		
				}
			}else return true;				
		}

		private boolean hasToUpdateBasedOnLastLogin(long idUser) {
			
			User u = db.findById(idUser);
			
			Date lastlogin = u.getLastLogin();			
			
			// 86400000 = 1 dia
		    if((lastlogin.getTime() - new Date().getTime()) > 86400000) 
		    	return true;
		    return false;
		}	
		
		public boolean updateUserSettings(JSONObject j, String string) throws JSONException {
			// TODO Auto-generated method stub
			Setting setting = new Setting();
			JSONObject Jsettings = j.getJSONObject("settings");
			
			if(Jsettings.getString("choice").equalsIgnoreCase("MALE"))		
				setting.setSexPreference(SexPreference.MALE);
			else if(Jsettings.getString("choice").equalsIgnoreCase("FEMALE"))
				setting.setSexPreference(SexPreference.FEMALE);
			else if(Jsettings.getString("choice").equalsIgnoreCase("BOTH"))
				setting.setSexPreference(SexPreference.BOTH);
			else return false;
				
			setting.setMininumAge(Jsettings.getInt("beginAge"));
			setting.setMaximumAge(Jsettings.getInt("finalAge"));
			setting.setLatitude(Jsettings.getString("latitude"));
			setting.setLongitude(Jsettings.getString("longitude"));			
			setting.setRadius(Jsettings.getInt("distance"));
			
			User u = db.findById(j.getLong("id"));
			
			u.setSetting(setting);
			
			db.updateUser(u.getFbId(), u);
			
			return true;
		}

		public String getUserSettings(long id) throws JSONException {
			
			User u = db.findById(id);
			Setting s = u.getSetting();
			//primeira vez
			JSONObject my_obj = new JSONObject();
			if(s==null){				
				my_obj.put("choice", "Both");
				my_obj.put("beginAge", "18");
				my_obj.put("finalAge", "95");
				my_obj.put("distance", "50");
			}else{				
				my_obj.put("choice", s.getSexPreference());
				my_obj.put("beginAge", String.valueOf(s.getMininumAge()));
				my_obj.put("finalAge", String.valueOf(s.getMaximumAge()));
				my_obj.put("distance", String.valueOf(s.getRadius()));
			}
			
			return my_obj.toString();
			
		}

		public boolean createSocialLink(JSONObject j) throws JSONException {
			
			SocialLinkController scc = new SocialLinkController();
			
			User u = db.findById(j.getLong("userId"));
			
			SocialLink sc = scc.findSocialLinkByName(j.getJSONObject("socialLink").getString("name"));
			
			UserSocialLink usl = new UserSocialLink();
			usl.setContent(j.getJSONObject("socialLink").getString("content"));
			
			if(j.getJSONObject("socialLink").getBoolean("onlytomatches"))
				usl.setVisibility(VISIBILITY.ONLYMATCHES);
			else if(j.getJSONObject("socialLink").getBoolean("everyone"))
				usl.setVisibility(VISIBILITY.EVERYONE);
			
			usl.setUser(u);
			usl.setSocialLink(sc);
			
			scc.connectSociallinkUser(usl);
			
			if(u.getSocialLinks() == null){
				ArrayList<UserSocialLink> usl2 = new ArrayList<UserSocialLink>();
				usl2.add(usl);
				u.setSocialLinks(usl2);
			}else{
				u.getSocialLinks().add(usl);
			}
			if(db.updateUser(u))
				return true;
			return false;
		}
		
		public boolean editSocialLink(JSONObject j) throws JSONException {			
			User u = db.findById(j.getLong("userId"));
			UserSocialLink usl = dbUserSocialLink.findUserSocialLinkById(u,j.getJSONObject("socialLink").getLong("id"));
			
			u.getSocialLinks().remove(usl);
			
			if(j.getJSONObject("socialLink").getBoolean("onlytomatches"))
				usl.setVisibility(VISIBILITY.ONLYMATCHES);
			else if(j.getJSONObject("socialLink").getBoolean("everyone"))
				usl.setVisibility(VISIBILITY.EVERYONE);
			
			u.getSocialLinks().add(usl);
			
			if(db.updateUser(u))
				return true;
			return false;
		}
		
		public boolean deleteSocialLink(JSONObject j) throws JSONException {			
			User u = db.findById(j.getLong("userId"));
			UserSocialLink usl = dbUserSocialLink.findUserSocialLinkById(u,j.getJSONObject("socialLink").getLong("id"));
			
			u.getSocialLinks().remove(usl);
			
			if(db.updateUser(u))
				return true;
			return false;
		}
		
		private JSONObject socialLinksToJson(ArrayList<UserSocialLink> socialLinks) throws JSONException{
			
			JSONObject j = new JSONObject(); 
			JSONArray jsArray = new JSONArray();
			
			for (UserSocialLink socialLink : socialLinks) {
				JSONObject js = new JSONObject();
				js.put("id", socialLink.getSocialLink().getIdSocialLink());
				js.put("name", socialLink.getSocialLink().getName());
				js.put("link", socialLink.getLink());
				js.put("content", socialLink.getContent());
				js.put("modal", socialLink.getSocialLink().isOpenOnModal());
				js.put("visibility", socialLink.getVisibility());
				jsArray.put(js);
			}
			
			j.put("socialLink", jsArray);
			
			return j;		
		}

		public String getSocialLinks(long id) throws JSONException {
			User u = db.findById(id);
			
			ArrayList<UserSocialLink> socialLinks = new ArrayList<UserSocialLink>();
			if(u.getSocialLinks() == null)
				return "";
			socialLinks.addAll(u.getSocialLinks());
			
			return socialLinksToJson(socialLinks).toString();						
		}

		public boolean updatePhoto(JSONObject j) throws Exception{
			
			PhotoController pcc = new PhotoController();

			User u = db.findById(j.getLong("userId"));			
			
			int index = j.getInt("index");
			String base64 = j.getString("imageBase64");
			
			u.setPhotos(pcc.updatePhoto(index, base64, u.getPhotos()));
			
			db.updateUser(u);
			
			return true;
		}

		public boolean updateUserBio(JSONObject j) throws JSONException {
			// TODO Auto-generated method stub
			
			User u = db.findById(j.getLong("userId"));
			
			u.setBio(j.getJSONObject("profile").getString("bio"));
			
			db.updateUser(u);
			
			return true;
		}

		public String getUserProfleById(long id) {
			// TODO Auto-generated method stub
			User user = db.findById(id);			
			
			if(new SocialLinkController().getUserSocialLinks(user) != null)
				user.setSocialLinks(new SocialLinkController().getUserSocialLinks(user));			

			JSONObject profileJson = getProfile(user, null);
									
			return profileJson.toString();
		}

		public boolean like(JSONObject j) throws IllegalAccessException, InvocationTargetException {
			
			long userId;
			long sugestionId;
			
			userId = j.getLong("userId");
			sugestionId = j.getLong("sugestionId");
			STATUS statusFromClient = STATUS.valueOf(j.getString("status")); 	
			
			List<User> res = db.findById(userId, sugestionId); 
			User user = null;
			User sugestion = null;
			
			for (User u : res) {
				if(u.getFbId()==userId){
					user = u;					
				}else if(u.getFbId()==sugestionId){
					sugestion = u;
				}
			}
			
			List<Sugestion> userSugestions = user.getSugestions();
			
			for (Sugestion s : userSugestions) {
				if(s.getUser().getFbId() == sugestionId){
					s.setStatus(STATUS.LIKED);
					
					if(statusFromClient == STATUS.LIKED){
						createMatch(user, s);
					}
				}
			}
			
			user.setSugestions(userSugestions);
			
			db.updateUser(user);
			
			return true;
		}

		private void createMatch(User user, Sugestion s) throws IllegalAccessException, InvocationTargetException {
			
			Match m = new Match();						
			BeanUtils.copyProperties(m, s);
			user.setMatches(m);
			
			Match m2 = new Match();
			Sugestion s2 = s.getUser().getSugestionByFbId(user.getFbId());
			
			BeanUtils.copyProperties(m2, s2);
			s.getUser().setMatches(m2);			
			
			db.updateUsers(user, s.getUser());
		}

		public String getUserMatches(long userId, String shortAccessToken) throws JSONException, IOException {
			
			accessToken = extendAccessToken(shortAccessToken);
			String res = "";
			
			User u = db.findById(userId);
			
			if (u.getMatches() != null)
				res = buildClientMatchJson(u.getMatches()).toString();
			
			return res;
		}

		/* match que vai pra tela */
		private JSONObject buildClientMatchJson(List<Match> matches) {
			
			JSONObject res = new JSONObject();
			JSONArray matchesJson = new JSONArray();	
			
			for (Match m : matches) {				
				JSONObject my_obj = new JSONObject();
				my_obj.put("id", m.getUser().getFbId());
				my_obj.put("name", m.getUser().getName()); 
				my_obj.put("interestsInCommon", getInterestsInCommon(m));
				my_obj.put("photos", m.getUser().getPhotos().get(0).getSizes().get(0).getUrl());
				my_obj.put("profilePic", m.getUser().getPictureUrl(accessToken));
				my_obj.put("percentage", m.getPercentage());				
				matchesJson.put(my_obj);
			}			
				
			res.put("matches", matchesJson);
			
			return res;
		}

		private JSONArray getInterestsInCommon(Match m) {			 
			
			JSONArray interestsJson = new JSONArray();
			
			for (Interest i : m.getInterestsInConnom()) {
				JSONObject my_obj = new JSONObject();
				my_obj.put("name", i.getName());
				my_obj.put("relevance", i.getRelevance());
				my_obj.put("type", i.getTipo());
				interestsJson.put(my_obj);
			} 
			
			return interestsJson;
		}

		public boolean hasSettings(long userId, String accessToken2) {
			
			User u = findUserById(userId);
			
			if(u.getSetting() == null)
				return false;			
			return true;
		}
		
		public int generateAge(String birthday){
			
			int month = Integer.valueOf(birthday.substring(0,2));
			int day = Integer.valueOf(birthday.substring(3,5));
			int year = Integer.valueOf(birthday.substring(6,10));
			
			LocalDate birth = new LocalDate(year,month, day);
			LocalDate now = new LocalDate();
			
			return Years.yearsBetween(birth, now).getYears();
		}

		public String updateAll(String shortAccessToken) throws JSONException, IOException {
			
			//extendAccessToken(shortAccessToken);
			
			List<User> us = db.findAll();		

			for (User u : us) {
				updateUser(u.getFbId(), shortAccessToken, null);
			}
			
			return "foi";			
		}
		
		
}
