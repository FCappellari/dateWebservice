package controller;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mongodb.morphia.Datastore;


import com.google.gson.Gson;

import model.SocialLink;
import model.User;
import model.UserSocialLink;
import persistence.MongoDBHelper;
import persistence.SocialLinkPersistence;
import persistence.UserPersistence;
import persistence.UserSocialLinkPersistence;

public class SocialLinkController {
	
	Datastore ds = MongoDBHelper.INSTANCE.getDatastore();	
    private Gson gson = new Gson(); 
    private SocialLinkPersistence db = new SocialLinkPersistence(ds);
    private UserSocialLinkPersistence dbUser = new UserSocialLinkPersistence(ds);
    
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
			js.put("contentLabel", socialLink.getContentLabel());
			jsArray.put(js);
		}
		
		j.put("socialLinks", jsArray);
		
		return j;		
	}
	
	public String findAllSocialLinks(long id) throws JSONException {
		ArrayList<UserSocialLink> sle  = new ArrayList<UserSocialLink>();
		ArrayList<SocialLink> sll  = new ArrayList<SocialLink>();
		ArrayList<SocialLink> sll2  = new ArrayList<SocialLink>();
		UserController uc = new UserController();
		User u = uc.findUserById(id);
		
		sle.addAll(dbUser.findUserSocialLinks(u));
		
		sll2.addAll(db.findAll());
		
		if(u.getSocialLinks() == null)
			return socialLinksToJson(sll2).toString(); //caso o usuario n tenha nenhuma sociallink retorna todas para adicionar
		
		for (SocialLink socialLink : sll2) {
		     for (UserSocialLink userSocialLink : sle)   
			    if (socialLink.getIdSocialLink() != userSocialLink.getSocialLink().getIdSocialLink()) {
		            sll.add(socialLink);
		        }
		}
		
		return socialLinksToJson(sll).toString();
	}
	
	public String findAllUserSocialLinks(long id) throws JSONException {
		ArrayList<UserSocialLink> sle  = new ArrayList<UserSocialLink>();
		ArrayList<SocialLink> sll  = new ArrayList<SocialLink>();
		UserController uc = new UserController();
		User u = uc.findUserById(id);
		
		sle.addAll(dbUser.findUserSocialLinks(u));
			
		sll.addAll(db.findAll());

		for (SocialLink socialLink : sll) {
		     for (UserSocialLink userSocialLink : sle)   
			    if (socialLink.getIdSocialLink() != userSocialLink.getSocialLink().getIdSocialLink()) {
		            sll.remove(socialLink);
		        }
		}
		
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
