package model;

import org.mongodb.morphia.annotations.Reference;

public class Sugestion {
	
	@Reference
	private User user;
	private int percentage;
	private String preferencesInConnom;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public int getPercentage() {
		return percentage;
	}
	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}
	public String getPreferencesInConnom() {
		return preferencesInConnom;
	}
	public void setPreferencesInConnom(String preferencesInConnom) {
		this.preferencesInConnom = preferencesInConnom;
	}
	

}
