package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

import utils.Util;

@Entity
public class Interest {
	
	@Id
	private ObjectId id;
	private String name; 
	private String type;
	private int relevance;	
	@Embedded
	private List<Interest> children = new ArrayList<Interest>();	
	private long longValue; 
	private String stringValue;
	
	public Interest(){		
		
	}
	public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTipo() {
		return type;
	}
	public void setTipo(String tipo) {
		this.type = tipo;
	}
	
	public List<Interest> getChildren() {
		return children;
	}
	public void setChildren(List<Interest> children) {
		this.children = children;
	}
	public long getLongValue() {
		return longValue;
	}
	public void setLongValue(long longValue) {
		this.longValue = longValue;
	}
	public String getStringValue() {
		return stringValue;
	}
	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}
}
