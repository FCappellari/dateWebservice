package controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import model.Chat;
import model.User;
import persistence.MongoDBHelper;
import persistence.UserPersistence;
import persistence.UserSocialLinkPersistence;

public class ChatController {
	
	UserController uc = new UserController();
	Datastore ds = MongoDBHelper.INSTANCE.getDatastore();
	
	public void saveMessage(JSONObject m){
		
		Chat c = new Chat();
		
		User to = uc.findUserById(m.getLong("to"));
		User from = uc.findUserById(m.getLong("from"));
		
		c.setTo(to);
		c.setFrom(from);
		c.setDate(new Date().toString());
		c.setMessage(m.getString("message"));
		
		ds.save(c);
	}

	public String getMessagesByUser(long matchId, long userId) {
		
		Query<Chat> query = ds.find(Chat.class);
		
		User match = uc.findUserById(matchId);
		User user = uc.findUserById(userId);
		
		query.or(
				query.and(
						  query.criteria("to").equal(match),
						  query.criteria("from").equal(user)
					),
				query.and(
						  query.criteria("to").equal(user),
						  query.criteria("from").equal(match)
					)
				);
		
		List<Chat> res = query.asList();
		
	    JSONObject messages = convertMessagesToJson(res);
		
		return messages.toString();
	}
	
	private JSONObject convertMessagesToJson(List<Chat> res) {

		JSONObject messages = new JSONObject();
		JSONArray messagesArray = new JSONArray();
		
		for (Chat m : res) {
			JSONObject mJson = new JSONObject();
			mJson.put("to", m.getTo().getFbId());
			mJson.put("from", m.getFrom().getFbId());
			mJson.put("date", m.getDate());
			mJson.put("message", m.getMessage());
			messagesArray.put(mJson);
		}
		
		messages.put("messages", messagesArray);
		
		return messages;
	}
}
