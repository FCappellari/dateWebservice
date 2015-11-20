package utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.json.JSONException;
import org.json.JSONObject;

public class Util {
    
	public static String jsonConverter(Object toBeConverted){
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    	String json = null;
		try {
			json = ow.writeValueAsString(toBeConverted);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  			
		return json;	
	}	
	

	
	public static HashMap jsonToMap(JSONObject jObject) throws JSONException {

        HashMap<String, String> map = new HashMap<String, String>();        
        Iterator<?> keys = jObject.keys();

        while( keys.hasNext() ){
            String key = (String)keys.next();
            String value = jObject.getString(key); 
            map.put(key, value);
        }

        System.out.println("json : "+jObject);
        System.out.println("map : "+map);
        
        return map;
    }
	
}
