package rest;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

import controller.SocialLinkController;

@Path("/socialLink")

public class SocialLinkRest {
	
	private SocialLinkController controller = new SocialLinkController();	

	@GET  
	@Path("/getSocialLinks")		
	public String findAllSocialLinks() throws JSONException{ 
		return controller.findAllSocialLinks().toString();
	} 
	
	@POST
	@Path("/create")	
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createSocialLink(String param) throws IOException, JSONException{ 
		JSONObject j = new JSONObject(param);
		
		if(controller.createSocialLink(j))
				return Response.status(200).build();
		else return Response.status(500).build();	
		
	}

}
