package persistence;

import com.google.gson.Gson;
import com.mongodb.*;
import com.mongodb.util.JSON;

import model.Interest;
import model.Setting;
import model.User;
import utils.GeoUtils;
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
import org.mongodb.morphia.aggregation.GeoNear.GeoNearBuilder;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.Shape;
import org.mongodb.morphia.query.Shape.Point;
import org.mongodb.morphia.query.UpdateOperations;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

public class UserPersistence extends BasicDAO<User, String> {	

    Datastore ds =  MongoDBHelper.INSTANCE.getDatastore();
    
    public UserPersistence(Datastore ds) {
		super(ds);
		// TODO Auto-generated constructor stub
	}
    
	public boolean CreateUser(User user){		
		
		if(hasUser(user.getFbId()))
			return false;
		
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
	
	
	@SuppressWarnings("unchecked")
	public List<User> findUserBySetting(Setting set){
		List<User> result = new ArrayList<User>();
		
		Point center = new Point(Double.parseDouble(set.getLongitude()), Double.parseDouble(set.getLatitude()));
		
		Shape circle = Shape.centerSphere(center, (GeoUtils.generateRadius(set.getRadius())));
		
		//111.12 earth curvature degree
		Query q = ds.createQuery(User.class).field("location")
				.within(circle);		
			
		result = q.asList();
		
		return result;	
	}

	public boolean updateUser(long id, User user) {	

		Query<User> updateQuery = ds.createQuery(User.class).field("fbId").equal(id);
		
		ds.findAndDelete(updateQuery);
		ds.save(user);
		
		return true;		
	}
	public boolean updateUserLastLogin(long id){
		UpdateOperations<User> ops;
		ops = ds.createUpdateOperations(User.class).set("lastLogin", new Date());
	
		// morphia default update is to update all the hotels
		ds.update(ds.createQuery(User.class).field("fbId").equal(id), ops);  //increments all hotels
		return false;
	}

	public boolean updateUser(User user) {
		Query<User> updateQuery = ds.createQuery(User.class).field("fbId").equal(user.getFbId());
		
		ds.findAndDelete(updateQuery);
		ds.save(user);
		
		return true;
		
	}

	public List<User> findById(long userId, long sugestionId) {
		
		Query<User> query = ds.find(User.class);
		query.or(query.criteria("fbId").equal(userId), query.criteria("fbId").equal(sugestionId));		
						
		return query.asList();
	}	
}


