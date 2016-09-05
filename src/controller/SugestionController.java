package controller;

import java.security.interfaces.DSAKey;
import java.util.ArrayList;
import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import model.Music;
import model.Sugestion;
import model.User;
import persistence.UserPersistence;

public class SugestionController {

	static MongoClientURI uri  = new MongoClientURI("mongodb://sa:sa@ds045054.mongolab.com:45054/teste"); 
	static MongoClient client = new MongoClient(uri);
	static Morphia morphia = new Morphia().map(User.class);
	
	static Datastore ds = morphia.createDatastore(client, "teste");
    private Gson gson = new Gson(); 
    private static UserPersistence db = new UserPersistence(ds);
    private SettingController st = new SettingController();
    private String accessToken;
    private ArrayList<String> interestParams;
    private static User current;
    
    
	public List<Sugestion> getUserSugestion(User user) {
		// TODO Auto-generated method stub
		current = user;
		
		return getPotentialUsers();
		
	}

	private List<Sugestion> getPotentialUsers() {
		// TODO Auto-generated method stub
		//List<User> users = db.findUserBySetting(st.findSetting(current));
		List<User> users = db.findUserBySetting(current.getSetting());
		List<Music> currentMusics = current.getMusic();
		Sugestion sugestion = null;
		List<User> potentialUsers = new ArrayList<User>();
		List<Sugestion> sugestionsList = new ArrayList<Sugestion>(); 
		
		if(users == null)
			return sugestionsList;
		
		for (User user : users) {
			List<Music> musics = user.getMusic();
			if(user.getFbId()!=current.getFbId()){				 
				for (Music music : musics) {
					for (Music currentMusic : currentMusics) {
						if(music.getName().equals(currentMusic.getName())){
							
							if(!potentialUsers.contains(user)){
								
								sugestion = new Sugestion();
								
								sugestion.setUser(user);
							    sugestion.setPreferencesInConnom(currentMusic.getName()+";");
							    //sugestion.setPercentage(percentage);
								potentialUsers.add(user);
								sugestionsList.add(sugestion);	
							}
						}else{
							/* retirar tudo do else
							 * trecho implementado para pegar todos os usuarios
							 */
							if(!potentialUsers.contains(user)){
								
								sugestion = new Sugestion();
								
								sugestion.setUser(user);
							    sugestion.setPreferencesInConnom(currentMusic.getName()+";");
							    //sugestion.setPercentage(percentage);
								potentialUsers.add(user);
								sugestionsList.add(sugestion);	
							}
						}
							
					}
				}
			}
		}
		
		return sugestionsList;
	}

}
