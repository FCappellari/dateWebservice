package controller;

import java.security.interfaces.DSAKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import model.Interest;
import model.Music;
import model.MusicGender;
import model.Sugestion;
import model.User;
import persistence.MongoDBHelper;
import persistence.UserPersistence;

public class SugestionController {

 
	
	static Datastore ds = MongoDBHelper.INSTANCE.getDatastore();
    private Gson gson = new Gson(); 
    private static UserPersistence db = new UserPersistence(ds);
    private SettingController st = new SettingController();
    private String accessToken;
    private ArrayList<String> interestParams;
    private static User current;
    private List<Sugestion> sugestionsList = new ArrayList<Sugestion>();
    
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
		List<MusicGender> currentMusicGenders = current.getMusicGenders();
		Sugestion sugestion = null;
		List<User> potentialUsers = new ArrayList<User>();
		ArrayList<Sugestion> resSugestionList = new ArrayList<Sugestion>();
		int sugestionLimit = 5;
		int k = 0;
		 
		
		if(users == null)
			return sugestionsList;
		
		for (User user : users) {
			if(user.getFbId()!=current.getFbId()){
				
				//cria objeto da sugestao
				sugestion = new Sugestion();								
				sugestion.setUser(user);
				k++;
				
				/*
				 * 	Musica
				 */
				List<Music> musics = user.getMusic();
							
				for (Music music : musics) {
					for (Music currentMusic : currentMusics) {						
						//Verifica musica igual
						if(music.getName().equals(currentMusic.getName())){														
							Interest i = new Interest();
							
							i.setName(music.getName());
							i.setRelevance(setpercentage(musics.size(), 10, currentMusics.size(), 10));
			     			i.setTipo("music");
							
							sugestion.setPreferencesInConnom(currentMusic.getName()+";");
							sugestion.setInterestsInConnom(i);
							
							potentialUsers.add(user);
							addToSugestionList(sugestion);
						}											
					}
				}
				
				/*
				 * Generos
				 */				
				List<MusicGender> sugestionMgs = user.getMusicGenders();
				if(sugestionMgs!=null){
					for (MusicGender mg : currentMusicGenders) {
						for (MusicGender smg : sugestionMgs) {
							if(mg.getName().equals(smg.getName())){														
								Interest i = new Interest();
								
								i.setName(smg.getName());
								i.setTipo("genre");
								i.setRelevance(setpercentage(sugestionMgs.size(), smg.getRelevance(), currentMusicGenders.size(), mg.getRelevance()));
								
								sugestion.setPreferencesInConnom(smg.getName()+";");						    
								sugestion.setInterestsInConnom(i);
								potentialUsers.add(user);
								addToSugestionList(sugestion);
															
							}
						}
					}				
				}
				addToSugestionList(sugestion);
			}			 
		}
		Collections.sort(sugestionsList, Collections.reverseOrder());		 
		
		for (int i = 0; i < sugestionsList.size(); i++) {
			if(i==sugestionLimit)
				break;
			resSugestionList.add(sugestionsList.get(i));
		}
		
		return resSugestionList;
	}


/*
	private void setMusicGenreRelevance(List<MusicGender> currentMusicGenders, List<MusicGender> sugestionMgs, User sugestion) {
        
		double qtdMusicGenresCurrentUser = 0, qtdMusicGenresSugestion =0;
		ArrayList<Interest> interestAux = new ArrayList<Interest>(); 
		ArrayList<Interest> sugestionInterests = new ArrayList<Interest>();
		
		qtdMusicGenresCurrentUser = currentMusicGenders.size();
		qtdMusicGenresSugestion = sugestionMgs.size();		
		
	}
*/
	private void addToSugestionList(Sugestion sugestion) {
		if(sugestionsList.contains(sugestion)){
			sugestionsList.set(sugestionsList.indexOf(sugestion), sugestion) ;
		}else{
			sugestionsList.add(sugestion);
		}		
	}

	private int setpercentage(int arraysizeSugestion, int sugestionRelavance, int arraysizeCurrentUser, int currentUserRelevance) {
		
		float r1 = 0;
		float r2 = 0;		
		float res = 0;
		float valorDoInteresseDaSugestao = 0;
		float valorDoInteresseDoUsuario = 0;		
		
		float arraysizeCurrentUserF = (float) arraysizeCurrentUser; 
		float arraysizeSugestionF = (float) arraysizeSugestion;
		float sugestionRelavanceF = (float) sugestionRelavance;
		float currentUserRelevanceF = (float) currentUserRelevance;
		
		valorDoInteresseDaSugestao = 100/arraysizeSugestionF;
		valorDoInteresseDoUsuario = 100/arraysizeCurrentUserF;
		
		if(sugestionRelavanceF==0) sugestionRelavanceF = 1;
		if(currentUserRelevanceF==0) currentUserRelevanceF = 1;
		
		r1 = valorDoInteresseDoUsuario * currentUserRelevanceF;
		// r1 = (100 * r1) / arraysizeSugestionF; // porcetagem
		
		r2 = valorDoInteresseDaSugestao * sugestionRelavanceF;
		//r2 = (100 * r2) / arraysizeCurrentUserF; // porcentagem 
		
				
		if(r2>r1){
            res = (r1/r2)*100;			
		}else{
			res = (r2/r1)*100;
		}
		
		return Math.round(res);
	}

}
