package model;

import org.mongodb.morphia.annotations.*;

@Embedded
public class Setting {
		
	private long id;
	private int mininumAge;
	private int maximumAge;
	private String latitude;
	private String longitude;
	private SexPreference sexPreference;
	
	public enum SexPreference {
	    HOMEM, MULHER, AMBOS;	
    }
	
	public void setSexPreference(SexPreference value){
		this.sexPreference = value;
	}
	
	public SexPreference getSexPreference(){
		return sexPreference;
	}
	
	public int getMininumAge() {
		return mininumAge;
	}
	public void setMininumAge(int mininumAge) {
		this.mininumAge = mininumAge;
	}
	public int getMaximumAge() {
		return maximumAge;
	}
	public void setMaximumAge(int maximumAge) {
		this.maximumAge = maximumAge;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
}
