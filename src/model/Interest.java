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
	
	@Override
	public boolean equals(Object object)
	{
	    boolean isEqual= false;

	    if (object != null && object instanceof Interest)
	    {
	        isEqual = (this.name.equals(((Interest) object).getName()));
	    }

	    return isEqual;
	}
	public int getRelevance() {
		return relevance;
	}
	public void setRelevance(int relevance) {
		this.relevance = relevance;
	}
	
	/*
	https://graph.facebook.com/v2.5/277727805571513?fields=name%2Cartists_we_like%2Caccess_token%2Caffiliation%2Capp_id%2Cattire%2Cawards%2Cband_interests%2Cband_members%2Cbest_page%2Cbio%2Cbirthday%2Cbooking_agent%2Cbusiness%2Cbuilt%2Ccan_post%2Ccategory%2Ccategory_list%2Ccompany_overview%2Cculinary_team%2Ccurrent_location%2Cdescription%2Cdescription_html%2Cfood_styles%2Cfounded%2Cgeneral_info%2Cgenre%2Chas_added_app%2Chometown%2Chours%2Cid%2Cinfluences%2Cis_always_open%2Cis_community_page%2Cis_permanently_closed%2Cis_published%2Cis_unclaimed%2Cis_verified%2Ckeywords%2Clocation%2Cmission%2Cmpg%2Cnetwork%2Cnew_like_count%2Cfeatures%2Cemails%2Cparking%2Cpayment_options%2Cpersonal_info%2Cpersonal_interests%2Cpharma_safety_info%2Cphone%2Cplot_outline%2Cpress_contact%2Cprice_range%2Cproduced_by%2Cproducts%2Cpublic_transit%2Crecord_label%2Crestaurant_services%2Crestaurant_specialties%2Cschedule%2Cscreenplay_by%2Cseason%2Cstarring%2Cstudio%2Ctalking_about_count%2Ccover%2Cdirected_by%2Cwritten_by%2Coffer_eligible%2Clink%2Calbums&access_token=CAACEdEose0cBAJ8YV5TuFzZAVWTZB0itVoFJOMga5ZB3dP1rbQQWNpqktHChI7TZA2v3ZAdoeuOHUWyVSLxRyZAJdfVqaJ7GccnqBtwtZANvQ9aeWI3Cipxqz86vpG4olopIWSKs5zLCXLUB6hmOV13kPwBs13gPMxGUGjwljMyI0uO8fkzCNwABZAHGOtz8w3VJ0wiYE9OXjAZDZD
	
	277727805571513?fields=name,artists_we_like,access_token,affiliation,app_id,attire,awards,band_interests,band_members,best_page,bio,birthday,booking_agent,business,built,can_post,category,category_list,company_overview,culinary_team,current_location,description,description_html,food_styles,founded,general_info,genre,has_added_app,hometown,hours,id,influences,is_always_open,is_community_page,is_permanently_closed,is_published,is_unclaimed,is_verified,keywords,location,mission,mpg,network,new_like_count,features,emails,parking,payment_options,personal_info,personal_interests,pharma_safety_info,phone,plot_outline,press_contact,price_range,produced_by,products,public_transit,record_label,restaurant_services,restaurant_specialties,schedule,screenplay_by,season,starring,studio,talking_about_count,cover,directed_by,written_by,offer_eligible,link,albums
    */
}
