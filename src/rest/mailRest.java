package rest;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

import controller.MailController;

@Path("/mail")
public class mailRest {
	
	@POST
	@Path("/message")	
	public Response crunchifyREST(String m) throws IOException, JSONException{		
		
		JSONObject message = new JSONObject(m);		
		new MailController().saveMessage(message);
		return Response.status(200).build();
	}
}
