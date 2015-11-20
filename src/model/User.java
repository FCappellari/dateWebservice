/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.mongodb.util.Util;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Transient;
import org.bson.types.ObjectId;

@Entity
public class User{

    @Id 
    private ObjectId id;
    
    private long fbId;
    private String name;
    private String gender; 
    private long locationId;
    private String location;   
    
    private byte[] picture;  
    private String pictureUrl; 
    private String coverPictureUrl;
    @Embedded
    private List<Interest> interests;     
    
    @Embedded
    private List<Photo> photos;
    
    @Transient
    private String accessToken;
        
    public User(){};       
    
    public User(JSONObject basicData) throws JSONException{    	
    	
    	//Interest i = new Interest(json);
    	
    	fbId = basicData.getLong("id");
        name = basicData.getString("name");
        //gender = json.getString("gender");        
        //setLocation(json.getJSONObject("location"));
        //setInterests(interests);      
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

	public long getFbId() {
		return fbId;
	}

	public void setFbId(long fbId) {
		this.fbId = fbId;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public long getLocationId() {
		return locationId;
	}

	public void setLocationId(long locationId) {
		this.locationId = locationId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	


	public List<Interest> getInterests() {
		return interests;
	}

	public void setInterests(List<Interest> interests) {
		this.interests = interests;
	}

	public byte[] getPicture() {
		return picture;
	}

	public void setPicture(byte[] picture) {
		this.picture = picture;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public String getCoverPictureUrl() {
		return coverPictureUrl;
	}

	public void setCoverPictureUrl(String coverPictureUrl) {
		this.coverPictureUrl = coverPictureUrl;
	}
	
	public void setPhotos(List<Photo> photos){
		this.photos = photos;
	}

	public List<Photo> getPhotos() {
		return photos;
	}
}
