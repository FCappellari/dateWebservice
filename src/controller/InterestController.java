package controller;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import model.Interest;

import utils.DateConvertJsonException;
import utils.LongConvertJsonException;
import utils.StringConvertJsonException;
import utils.Util;

public class InterestController {	
	
	public List<Interest> biuldInterests(JSONObject interests, String preference) throws JSONException {
		
		JSONArray data;	
		Interest interest = null;
	    List<Interest> list = new ArrayList<Interest>();
		data = interests.getJSONArray("data");
	   
		for (int i = 0; i < data.length(); i++) {        	
			JSONObject node = data.getJSONObject(i);
			Iterator<?> keys = node.keys();
			
			// representa cada intem do array data (blocao)
			Interest main = new Interest();
			main.setName(preference);
			//cada atributo do interesse 
	        while(keys.hasNext() ){	        	
	        	String key = (String)keys.next();	        	
	        	
	        	 main.getChildren().add(montarInteresse(node, key));
	            
	        }
	        list.add(main);
		}
		return list;
	}	
		
	private Interest montarInteresse(JSONObject json, String key) throws JSONException{
		Interest childInterest = new Interest();
		JSONObject child;
		
    	try{	           
    		child = json.getJSONObject(key);
    		//interest.getChildren().add(montarInteresse(child, key);
    		    		
    		Iterator<?> childKeys = child.keys();
    		childInterest.setName(key);
    		while(childKeys.hasNext() ){	        	
	        	String childKey = (String)childKeys.next();	        	
	        	
	        	childInterest.getChildren().add(montarInteresse(json.getJSONObject(key), childKey));
	            
	        }
    		
        }catch(JSONException e){
        	
        	String type = convertData(json, key);            
            
            Object value = json.get(key);        
            
            childInterest.setName(key);
            
            if(type.equals("Long"))
            	childInterest.setLongValue(convertLong(value.toString()));
            else if(type.equals("String"))	
            	childInterest.setStringValue(value.toString());
            
            childInterest.setTipo(type);
        }
		return childInterest;
	}


	private String convertData(JSONObject json, String key){
		
		Object data = null;
		boolean isDate = false, isLong = false;
		
		try {
			data = json.get(key);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String result = null;
		
		try {					
			Long id = convertLong(data.toString());
			isLong = true;
			result = "Long";
		} catch (LongConvertJsonException eL){
			isLong = false;
		}
		if(!isLong)
			try{
				Date d = convertDate(data.toString());
				isDate = true;
			} catch (DateConvertJsonException eD){
				isDate = false;
			}
		if((!isDate)&&(!isLong)){
			String string = data.toString();
			result = "String";
		}
		 	
		return result;
	}

	private Long convertLong(String data) throws LongConvertJsonException {
		
		Long l;
		
		try{
			l = new Long(data);
		}catch(NumberFormatException e){
			throw new LongConvertJsonException();
		}catch (Exception e) {
			throw new LongConvertJsonException();
		}
		return l;	
	}

	private Date convertDate(String data) throws DateConvertJsonException{
		
		Date d = null;
		try{
			d = new Date(data);
		}catch(Exception e){
			throw new DateConvertJsonException();
		}
		return d;	
	}
}
	
	
	
    /*
    
	Iterator<?> mainkeys = interests.keys();
    while(mainkeys.hasNext()){                      	
    	
    	String mainKey = mainkeys.next().toString();
    	
    	if(mainKey.equals("id"))
    		continue;
    	
        	        	
        
        HashMap<String, String> map = new HashMap<String, String>();            
		
        // cada interesse
        for (int i = 0; i < data.length(); i++) {
        	
        	interest = new Interest();
        	
			Iterator<?> keys = data.getJSONObject(i).keys();

			//cada atributo do interesse
	        while( keys.hasNext() ){
	            String key = (String)keys.next();
	            String value = data.getJSONObject(i).getString(key); 
	            map.put(key, value);
	        }	
	        
	        interest.setName(mainKey);
	        interest.setAtributos(map);
	        list.add(interest);
		}	        
      
    }
    */