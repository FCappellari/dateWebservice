package model;

import java.util.ArrayList;
import java.util.List;

import org.mongodb.morphia.annotations.Reference;

public class Sugestion implements Comparable<Sugestion>{
	
	@Reference(ignoreMissing=true)
	private User user;
	private int percentage;
	private String preferencesInConnom;
	private List<Interest> interestsInConnom;
	private STATUS status;
	
	public enum STATUS {
	    UNSET, LIKED, DISLIKED;	
    }
	
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
	public List<Interest> getInterestsInConnom() {
		return interestsInConnom;
	}
	public void setInterestsInConnom(List<Interest> interestsInConnom) {
		this.interestsInConnom = interestsInConnom;
	}
	public void setInterestsInConnom(Interest interestsInConnom) {
		if(this.interestsInConnom==null)
			this.interestsInConnom = new ArrayList<Interest>();
		this.interestsInConnom.add(interestsInConnom);
	}
	@Override
	public int compareTo(Sugestion o) {

		if((interestsInConnom==null)&&(o.getInterestsInConnom()==null))
			return 0;
		
		if(interestsInConnom==null){
			return (0 - o.getInterestsInConnom().size());
		}
		
		if(o.getInterestsInConnom()==null){
			return interestsInConnom.size() - 0;
		}
		return (interestsInConnom.size() - o.getInterestsInConnom().size());
	}
	public STATUS getStatus() {
		return status;
	}
	public void setStatus(STATUS status) {
		this.status = status;
	}	

}
