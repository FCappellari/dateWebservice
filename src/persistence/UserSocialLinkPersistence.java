package persistence;

import java.util.ArrayList;
import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import model.SocialLink;
import model.User;
import model.UserSocialLink;

public class UserSocialLinkPersistence extends BasicDAO<UserSocialLink, String>{

    
    public UserSocialLinkPersistence(Datastore ds) {
		super(ds);
		// TODO Auto-generated constructor stub
	}
	
	public List<UserSocialLink> findUserSocialLinks(User u){
		
		ArrayList<UserSocialLink> result = new ArrayList<>();  
		
		for (UserSocialLink usl : u.getSocialLinks()) {
			result.add(getDs().get(UserSocialLink.class, usl.getId()));
		}
		
		return result;		
	}
    
}
