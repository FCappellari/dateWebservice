package model;

public class Place {
	
	private long id;	
    private String created_time;
    GeoLocal location;
    
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCreated_time() {
		return created_time;
	}
	public void setCreated_time(String created_time) {
		this.created_time = created_time;
	}
	public GeoLocal getLocation() {
		return location;
	}
	public void setLocation(GeoLocal location) {
		this.location = location;
	}
	
}
