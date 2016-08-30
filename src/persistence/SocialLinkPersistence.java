package persistence;

import java.util.ArrayList;
import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;

import model.SocialLink;
import model.User;
import model.UserSocialLink;


public class SocialLinkPersistence extends BasicDAO<SocialLink, String>{

    public SocialLinkPersistence(Datastore ds) {
		super(ds);
		// TODO Auto-generated constructor stub
	}
	
	public SocialLink findSocialLinkByName(String param) {
		String query = "name =";
		String value = param;		
		
		return getDs().find(SocialLink.class, query, value ).asList().get(0);				
	}
	
	public void saveSocialLinkTeste(){
		SocialLink teste = new SocialLink();  
		
		teste.setCreated_time("teste2");
		teste.setIdSocialLink(2);		
		teste.setName("whatsapp");		
		teste.setOpenOnModal(true);
		
		
		getDs().save(teste);
	}

	public List<SocialLink> findAll() {		
		return getDs().find(SocialLink.class).asList();
	}

	public void createUserSocialLink(SocialLink sc, User u) {
		
		
	}

	public boolean createUserSocialLink(UserSocialLink usl) {
		getDs().save(usl);
		
		return true;
	}	


}
