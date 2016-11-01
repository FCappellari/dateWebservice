package rest;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;
import org.mongodb.morphia.Datastore;

import controller.ChatController;
import persistence.MongoDBHelper;
import persistence.UserPersistence;
import persistence.UserSocialLinkPersistence;

@Path("/chat")
public class ChatRest {
	
	ChatController controller = new ChatController();
	
	@POST
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response crunchifyREST(String j) throws IOException, JSONException{		   
		  
		  JSONObject json = new JSONObject(j);	  
		  
	      controller.saveMessage(json);
		  
		  return Response.status(200).build();
	}
	
	@GET
	@Path("getMessageByUser")	
	public String getMessageByUser(@QueryParam("toId") long toId, @QueryParam("fromId") long fromId, @QueryParam("accessToken") String accessToken) throws JSONException, IOException{ 
		
		return controller.getMessagesByUser(toId,fromId);
	}
	
}
