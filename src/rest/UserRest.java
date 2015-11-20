package rest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import controller.*;
import utils.FbApp;
@Path("/users")
public class UserRest { 
	
	private UserController controller = new UserController();
	
	
	@GET  
	@Produces({MediaType.APPLICATION_JSON})	
	public String findAllUsers(){ 
		return controller.findAllOrderedByName().toString();
	} 
	
	
	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces({MediaType.APPLICATION_JSON})	
	public String getUserById(@PathParam("id") long id) throws JsonGenerationException, JsonMappingException, IOException{ 
		
		return new Gson().toJson(controller.findUserById(id)).toString();
	}
	
	@GET
	@Path("/{id:[0-9][0-9]*}/profile")	
	public String getUserProfleById(@PathParam("id") long id) throws JSONException{ 
		
		return controller.getUserProfleById(id);
	}
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response crunchifyREST(String j) throws IOException, JSONException{		
		
		  Login login = new Gson().fromJson(j, Login[].class); 
		  
		  
		  
		  if(!controller.hasUser(login.getId("id")))
			  controller.createUserJson(json.getString("accessToken"));
		  
		  return Response.status(200).build();
	}	
} 


