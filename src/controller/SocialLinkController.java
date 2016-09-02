package controller;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import model.SocialLink;
import model.User;
import model.UserSocialLink;
import persistence.SocialLinkPersistence;
import persistence.UserPersistence;
import persistence.UserSocialLinkPersistence;

public class SocialLinkController {
	
	static MongoClientURI uri  = new MongoClientURI("mongodb://sa:sa@ds045054.mongolab.com:45054/teste"); 
	static MongoClient client = new MongoClient(uri);
	static Morphia morphia = new Morphia().map(SocialLink.class);
	static Morphia morphia2 = new Morphia().map(UserSocialLink.class);
	
	Datastore ds = morphia.createDatastore(client, "teste");
	Datastore dsUser = morphia2.createDatastore(client, "teste");
    private Gson gson = new Gson(); 
    private SocialLinkPersistence db = new SocialLinkPersistence(ds);
    private UserSocialLinkPersistence dbUser = new UserSocialLinkPersistence(dsUser);
    
	public List<SocialLink> getFirstSocialLink(){
		
		ArrayList<SocialLink> sll = new ArrayList<SocialLink>();
		if(db.findSocialLinkByName("facebook") != null){
			sll.add(db.findSocialLinkByName("facebook"));
		}
		return sll;		
	}
	
	public void saveSocialLinkTeste(){
		db.saveSocialLinkTeste();
	}

	private JSONObject socialLinksToJson(ArrayList<SocialLink> sll) throws JSONException{
		
		JSONObject j = new JSONObject(); 
		JSONArray jsArray = new JSONArray();
		
		for (SocialLink socialLink : sll) {
			JSONObject js = new JSONObject();
			js.put("id", socialLink.getIdSocialLink());
			js.put("name", socialLink.getName());			
			js.put("modal", socialLink.isOpenOnModal());			
			jsArray.put(js);
		}
		
		j.put("socialLinks", jsArray);
		
		return j;		
	}
	
	public String findAllSocialLinks() throws JSONException {
		
		ArrayList<SocialLink> sll  = new ArrayList<SocialLink>();
		sll.addAll(db.findAll());
		
		return socialLinksToJson(sll).toString();
	}

	public boolean createSocialLink(JSONObject j) {		
		
		return true;
	}

	public SocialLink findSocialLinkByName(String string) {
		return db.findSocialLinkByName(string);
		
	}

	public boolean connectSociallinkUser(UserSocialLink usl) {		
		
		db.createUserSocialLink(usl);
		
		return true;
	}

	public List<UserSocialLink> getUserSocialLinks(User user) {

		dbUser.findUserSocialLinks(user);
		
		return null;
	}
	

	
}
