package persistence;

import org.json.JSONObject;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

import model.Mail;

public class MailPersistence extends BasicDAO<Mail, String> {
    
	Datastore ds =  MongoDBHelper.INSTANCE.getDatastore();
	
	public MailPersistence(Datastore ds) {
		super(ds);
		// TODO Auto-generated constructor stub
	}
}
