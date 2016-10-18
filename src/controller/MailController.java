package controller;

import org.json.JSONObject;
import org.mongodb.morphia.Datastore;

import model.Mail;
import persistence.MailPersistence;
import persistence.MongoDBHelper;
import persistence.UserPersistence;
import persistence.UserSocialLinkPersistence;

public class MailController {
	
	Datastore ds = MongoDBHelper.INSTANCE.getDatastore();	     
    private MailPersistence db = new MailPersistence(ds);    
    
    public void saveMessage(JSONObject m){
    	Mail mail = new Mail(m); 
    	
    	db.save(mail);    	
    }
	
}
