package model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

@Entity
public class UserSocialLink {
	
    @Id 
    private ObjectId id;
	
	@Reference
	private User user;
	
	@Reference
	private SocialLink socialLink;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public SocialLink getSocialLink() {
		return socialLink;
	}

	public void setSocialLink(SocialLink socialLink) {
		this.socialLink = socialLink;
	}

	public VISIBILITY getVisibility() {
		return visibility;
	}

	public void setVisibility(VISIBILITY visibility) {
		this.visibility = visibility;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	private VISIBILITY visibility;
	private String link;
    private String content;
	
	public enum VISIBILITY {
	    ONLYMATCHES, EVERYONE;	
    }	
	
}
