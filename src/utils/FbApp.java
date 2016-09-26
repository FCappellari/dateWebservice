package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import javax.imageio.ImageIO;

import org.json.JSONException;
import org.json.JSONObject;

public class FbApp {
	
	private static long appId = 973158726110069L;
	private static String appSecret = "d6da159210c713c50c11164e759e2f5d";	
	private static String version = "v2.7";
	 
	private static FbApp instancia; //Declaramos um objeto privado e estático 'instancia'  
	  
	   private FbApp() {  
	   // O que o construtor faz vem aqui...  
	   }  
	  
	   public static FbApp getInstancia() { //Método (get) público e estático de controle e acesso ao objeto e sua instanciação  
	      if (instancia == null)// Teste: 'se a variável de referência estiver nula' > Instancie uma só vez o objeto "instancia"  
	         instancia = new FbApp();  
	      return instancia; // E a retorne!  
	   }

	public static long getAppId() {
		return appId;
	}

	public static void setAppId(long appId) {
		FbApp.appId = appId;
	}

	public static String getAppSecret() {
		return appSecret;
	}

	public static void setAppSecret(String appSecret) {
		FbApp.appSecret = appSecret;
	}
	
	public static String buildUrl(String param, String accessToken){		
			
		String url = "https://graph.facebook.com/" +
	                 version + "/" +
				     "me?fields=";		
		url = url + param;				
		
		url = url + "&access_token=" + accessToken;	                 
		
		return url;		
	}	
	
	public static String buildUrlToGetPhoto(String param, String accessToken){		
		
		String url = "https://graph.facebook.com/" +
	                 version + "/";
				     		
		url = url + param + "/?fields=images";				
		
		url = url + "&access_token=" + accessToken;	                 
		
		return url;		
	}
	
	public static String buildUrl(ArrayList<String> params, String accessToken){		
		
		boolean lgFirstParam = true;
		
		String url = "https://graph.facebook.com/" +
	                 version + "/" +
				     "me?fields=";
		
		for (String p : params) {
			if(lgFirstParam){
				url = url + p;
				lgFirstParam = false;
			} else {				
				url = url + "%2C" + p;				
			}			
		}
		
		url = url + "&access_token=" + accessToken;	                 
		
		return url;		
	}
	
	public static String buildUrl(String[] params, String accessToken, String id){		
		
		boolean lgFirstParam = true;
		
		String url = "https://graph.facebook.com/" +
	                 version + "/" +
				     ""+id+"?fields=";
		
		for (String p : params) {
			if(lgFirstParam){
				url = url + p;
				lgFirstParam = false;
			} else {				
				url = url + "%2C" + p;				
			}			
		}
		
		url = url + "&access_token=" + accessToken;	                 
		
		return url;		
	}
	
	public static String buildUrlDebug(String[] params, String accessToken, String id) throws IOException{		
		
		boolean lgFirstParam = true;
		
		String url = "https://graph.facebook.com/" +
	                 version + "/" +
				     ""+id+"?fields=";
		String url2;
		
		for (String p : params) {
			if(lgFirstParam){
				url = url + p;
				lgFirstParam = false;
				url2 = url + "&access_token=" + accessToken;
				httpGetRequest(url2);
			} else {				
				url = url + "%2C" + p;
				url2 = url  + "&access_token=" + accessToken;
				httpGetRequest(url2);
			}			
		}
		
		url = url + "&access_token=" + accessToken;	                 
		
		return url;		
	}
	
	public static JSONObject httpGetRequest(String urlString) throws IOException{
		
		JSONObject result = null;
		StringBuilder resultString = new StringBuilder();
	    URL url = new URL(urlString);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setRequestMethod("GET");
	    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	    String line;
	    while ((line = rd.readLine()) != null) {
	  	  resultString.append(line);
	    }
	    rd.close();	 		   
	    
		try {
			result = new JSONObject(resultString.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return result;
	}	
	
	public static File httpGetPicture(String urlString) throws IOException{
		
		File result = new File("pic.jpg");
		StringBuilder resultString = new StringBuilder();
	    URL url = new URL(urlString);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    	    
	    ImageIO.write(ImageIO.read(url), "jpg", result);
	    
	    return result;		
	}
	
	public static String userPictureAsUrl(String accessToken){
		
		String url = "https://graph.facebook.com/" +
                 	  version + "/" +
                 	 "me/picture?type=large";		
		
		url = url + "&access_token=" + accessToken;
		
		return url;
		
	}
	
	public static String biuldPhotosUrl(String accessToken){
		
		String url = "https://graph.facebook.com/" +
           	  version + "/" +
           	 "me?fields=albums{photos.limit(100){likes.limit(0).summary(true)},id}";		
	
		url = url + "&access_token=" + accessToken;
		
		return url;		
	}

	public static String biuldPhotosUrl(long id, String accessToken){
		
		String url = "https://graph.facebook.com/" +
           	  version + "/" + id +
           	  "?fields=albums{photos.limit(100){likes.limit(0).summary(true)},id}";		
	
		url = url + "&access_token=" + accessToken;
		
		return url;		
	}

	public static String buildUrlToGetPhoto(long param, String accessToken) {
		
		String url = "https://graph.facebook.com/" +
	                 version + "/";
				     		
		url = url + param + "/?fields=picture.width(800).height(800){url}";				
		
		url = url + "&access_token=" + accessToken;	                 
		
		return url;		
	}
	
	public static boolean testUrl(String urlString){
		int code = 0;
		
		try{
			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();

			code = connection.getResponseCode();
		}catch(IOException e){
			e.printStackTrace();
		}
		
		if(code!=200)	
			return false;
		return true;
	}


	
	
}

 