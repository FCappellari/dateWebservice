package model;

import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.mongodb.morphia.annotations.Id;

public class Mail {

    @Id
    private ObjectId id;
    
	private String name;
	private String email;
	private String message;
	
	public Mail(JSONObject m) {
		this.name = m.getString("name");
		this.email = m.getString("email");
		this.message = m.getString("message");
	}

}
