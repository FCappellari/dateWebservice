package model;

public class MusicGender {

	private String name;
	private int relevance;
		
	public MusicGender(){}
	
	public MusicGender(String genre) {	
		if(genre == null)
			this.setName("Sem genero");
		else {
			this.setName(genre);
	    	this.setRelevance(1);
		}
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public int getRelevance() {
		return relevance;
	}
	public void setRelevance(int relevance) {
		this.relevance = relevance;
	}
}
