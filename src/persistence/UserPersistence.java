package persistence;

import com.google.gson.Gson;
import com.mongodb.*;
import com.mongodb.util.JSON;

import model.Interest;
import model.User;
import utils.Util;

import java.util.List;
import java.util.Set;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.json.JSONException;
import org.json.JSONObject;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

public class UserPersistence extends BasicDAO<User, String> {
	
	static MongoClientURI uri  = new MongoClientURI("mongodb://sa:sa@ds045054.mongolab.com:45054/teste"); 
    static MongoClient client = new MongoClient(uri);
    static Morphia morphia = new Morphia().map(User.class);
    Datastore ds = morphia.createDatastore(client, "teste");
    
    public UserPersistence(Datastore ds) {
		super(ds);
		// TODO Auto-generated constructor stub
	}
    
	public boolean CreateUser(User user){	
		
		Key<User> save = ds.save(user);
		
		if(save==null)
			return false;
		return true;
	}
	
	private User findLast() {
		 
		User latest = (User) ds.find(User.class).order("-id").limit(1);		
		return latest;
		
	}
	public List<User> findAll(){		
		return ds.find(User.class).asList();		
	}
	
	private String update(User user){
		
		
		return "";
		
	}
	
	private boolean delete(int id){
		
		DBObject basicDBObject = new BasicDBObject();
		basicDBObject.put("id", id); 
		//collection.remove(basicDBObject);
		
		return false;		
	}
	
	public User findById(long id){
		String query = "fbId =";
		long value = id;		
		
		return ds.find(User.class, query, value ).asList().get(0);
	}

	public boolean hasUser(long id) {	
		User a = ds.createQuery(User.class).field("fbId").equal(id).get();		
		
		if(a==null)
			return false;
		return true;				
	}
}


