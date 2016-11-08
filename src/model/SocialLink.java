package model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity
public class SocialLink {
	
    @Id 
    private ObjectId id;
	
	private long idSocialLink;	
    private String created_time;
    private String name;    
    private boolean openOnModal; 
    private String contentLabel;

	public String getCreated_time() {
		return created_time;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (idSocialLink ^ (idSocialLink >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SocialLink other = (SocialLink) obj;
		if (idSocialLink != other.idSocialLink)
			return false;
		return true;
	}

	public void setCreated_time(String created_time) {
		this.created_time = created_time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public long getIdSocialLink() {
		return idSocialLink;
	}

	public void setIdSocialLink(long idSocialLink) {
		this.idSocialLink = idSocialLink;
	}

	public boolean isOpenOnModal() {
		return openOnModal;
	}

	public void setOpenOnModal(boolean openOnModal) {
		this.openOnModal = openOnModal;
	}

	public String getContentLabel() {
		return contentLabel;
	}

	public void setContentLabel(String contentLabel) {
		this.contentLabel = contentLabel;
	}


}
