package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder.In;

import org.mongodb.morphia.Datastore;

import com.google.gson.Gson;

import model.Interest;
import model.Music;
import model.MusicGender;
import model.Place;
import model.Setting.SexPreference;
import model.Sugestion;
import model.Sugestion.STATUS;
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
		//return getALLPotentialUsers(true);
		return getPotentialUsers();
		
	}

	private List<Sugestion> getPotentialUsers() {
		// TODO Auto-generated method stub
		//List<User> users = db.findUserBySetting(st.findSetting(current));
		List<User> users = db.findUserBySetting(current.getSetting());
		List<Music> currentMusics = current.getMusic();
		List<MusicGender> currentMusicGenders = current.getMusicGenders();
		List<Place> currentPlaces = current.getPlaces();
		Sugestion sugestion = null;
		List<User> potentialUsers = new ArrayList<User>();
		ArrayList<Sugestion> resSugestionList = new ArrayList<Sugestion>();
		int sugestionLimit = 10;
		int k = 0;
		
		users = filterByAgeAndGender(users);
		
		current.setSugestions(removeMissingReferencesByFbId(users));
				
		boolean existeSugestoes = false;
		
		if(users == null)
			return sugestionsList;
		
		System.out.println("BEGIN COMBINATION " + new Date());
		
		for (User user : users) {
			
			if(user.getFbId()!=current.getFbId()){
				
				if(hasSugestionAlready(user)){
					existeSugestoes = false;
					
					sugestion = getCurrentSugestion(user);
					
					if(sugestion==null){
						//cria objeto da sugestao
						sugestion = new Sugestion();								
						sugestion.setUser(user);
						sugestion.setStatus(STATUS.UNSET);						
					}					

					if(sugestion.getStatus()==null)
						sugestion.setStatus(STATUS.UNSET);
				}else{
					//cria objeto da sugestao
					sugestion = new Sugestion();								
					sugestion.setUser(user);
					sugestion.setStatus(STATUS.UNSET);
				}
				k++;
				
				/*
				 * 	Musica
				 */
				if(user.getMusic()!= null){
					List<Music> musics = user.getMusic();				
					for (Music music : musics) {
						for (Music currentMusic : currentMusics) {						    
						    if(music.getName().equals(currentMusic.getName())){	//Verifica musica igual
						    	
						       if (music.getName().equals("Sem genero"))
						    	   continue;
						     				
							   Interest i = new Interest();
								
							   i.setName(music.getName());
							   i.setRelevance(setpercentage(musics.size(), 10, currentMusics.size(), 10));
							   i.setTipo("music");
								
							   if(sugestion.getInterestsInConnom()!=null)			     			
								  if(sugestion.getInterestsInConnom().contains(i))
								     continue;						
								
							   sugestion.setPreferencesInConnom(currentMusic.getName()+";");
							   sugestion.setInterestsInConnom(i);
								
							   potentialUsers.add(user);
							   addToSugestionList(sugestion);
						   }
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
							{ //verifica se o registro e valido
							   if(mg.getName().equals(smg.getName())){
								  if(currentMusicGenders.contains(smg.getName()))
									 continue;
								  
								  if(mg.getName().equals("Sem genero"))
									  continue;
								
								  Interest i = new Interest();
								
								  i.setName(smg.getName());
								  i.setTipo("genre");
								  i.setRelevance(setpercentage(sugestionMgs.size(), smg.getRelevance(), currentMusicGenders.size(), mg.getRelevance()));
								
							      if(sugestion.getInterestsInConnom()!=null)			     			
				     				  if(sugestion.getInterestsInConnom().contains(i))
				     					  continue;						
								
								  sugestion.setPreferencesInConnom(smg.getName()+";");						    
								  sugestion.setInterestsInConnom(i);
								  potentialUsers.add(user);
								  addToSugestionList(sugestion);
								  						
							  }
						   } 	
						}
					}				
				}
				
				/*
				 * Lugares
				 */	
				List<Place> sugestionPls = user.getPlaces();
				if(sugestionPls!=null){
					for (Place pl : currentPlaces) {
						for (Place spl : sugestionPls) {
							if(pl.getName().equals(spl.getName())){								
								if(currentPlaces.contains(spl.getName()))
									continue;
								
								Interest i = new Interest();
								
								i.setName(spl.getName());
								i.setTipo("place");
								i.setRelevance(setpercentage(sugestionPls.size(), 15, currentPlaces.size(), 15));
								
								if(sugestion.getInterestsInConnom()!=null)			     			
				     				if(sugestion.getInterestsInConnom().contains(i))
				     					continue;						
								
								sugestion.setPreferencesInConnom(spl.getName()+";");						    
								sugestion.setInterestsInConnom(i);
								potentialUsers.add(user);
								addToSugestionList(sugestion);
															
							}
						}
					}				
				}
				
				
				sugestion.setPercentage(setSugestionPercetage(sugestion));
				
				addToSugestionList(sugestion);
			}			 
		}
		
		removeDuplicateInterests(sugestionsList);
		
		Collections.sort(sugestionsList, Collections.reverseOrder());		 
		
		for (int i = 0; i < sugestionsList.size(); i++) {
			if(i==sugestionLimit)
				break;
			
			//usuario ja deu like ou dislike na sugestao por isso nao deve aparecer
			if((sugestionsList.get(i).getStatus() == STATUS.LIKED)||(sugestionsList.get(i).getStatus() == STATUS.DISLIKED))
				continue;
			
			resSugestionList.add(sugestionsList.get(i));
		}
		
		//updateUserSugestions(sugestionsList);
		
		System.out.println("END COMBINATION " + new Date());
		
		if(!existeSugestoes){
			current.setSugestions(sugestionsList);
			new UserController().getDB().updateUser(current);
		}
		return resSugestionList;
	}
	
	private int setSugestionPercetage(Sugestion sugestion) {

		int total = 0;
		int interestsInCommon = 0;		
		
		if(sugestion.getUser().getMusic() != null)
			total = total + sugestion.getUser().getMusic().size();
		
		if(sugestion.getUser().getMusicGenders() != null)
			total = total + sugestion.getUser().getMusicGenders().size();
			
		if(sugestion.getInterestsInConnom() != null)
			interestsInCommon = sugestion.getInterestsInConnom().size();
		else interestsInCommon = 0;
		
		int percent = (int)((interestsInCommon * 100.0f) / total);
		
		return percent;
	}

	private List<Sugestion> removeMissingReferencesByFbId(List<User> users) {		 
		ArrayList<Sugestion> res = new ArrayList<Sugestion>();
		
		if(current.getSugestions()==null)
			return res;
		
		for (Sugestion s : current.getSugestions()) {
			if(s.getUser() == null){			
				continue;				
			}			
			res.add(s);
		}		
		
		return res;
	}

	private void updateUserSugestions(List<Sugestion> sugestionsList2) {
		
		ArrayList<Sugestion> updateSugestinos = new ArrayList<Sugestion>();
		
		for (Sugestion currentSugestnion : current.getSugestions()) {
			for (Sugestion sugestion : sugestionsList2) {
				//
				if(sugestion.getUser().getFbId() == currentSugestnion.getUser().getFbId()){
					
				}
			}
		}		
	}

	private void removeDuplicateInterests(List<Sugestion> sugestionsList2) {
		if(sugestionsList2 != null){
			for (Sugestion sugestion : sugestionsList2) {
				if((sugestion != null)&&(sugestion.getInterestsInConnom() != null))					
					sugestion.setInterestsInConnom(removeDuplicates(sugestion.getInterestsInConnom()));
			}		
		}
	}

	private List<Interest> removeDuplicates(List<Interest> listWithDuplicates) {
	    /* Set of all attributes seen so far */
	    Set<String> attributes = new HashSet<String>();
	    /* All confirmed duplicates go in here */
	    List duplicates = new ArrayList<Interest>();
	    
	    for(Interest x : listWithDuplicates) {
	        if(attributes.contains(x.getName())) {
	            duplicates.add(x);
	        }
	        attributes.add(x.getName());
	    }
	    /* Clean list without any dups */
	    listWithDuplicates.removeAll(duplicates);
	    
	    return listWithDuplicates;
	}
	
	private Sugestion getCurrentSugestion(User user) {
		List<Sugestion> currentSugestions = current.getSugestions();
		
		for (Sugestion s : currentSugestions) {
			if(s.getUser().getFbId() == user.getFbId()){
				return s;
			}
		}
		return null;
	}

	private boolean hasSugestionAlready(User user) {

		List<Sugestion> currentSugestions = current.getSugestions();
		
		//usuario nao possui nenhuma sugestao
		if(currentSugestions == null)
			return false;
		
		for (Sugestion s : currentSugestions) {
			if(s.getUser().getFbId() == user.getFbId()){
				return true;
			}
		}
		
		return false;
	}
	
	private List<User> filterByAgeAndGender(List<User> users){
		
		ArrayList<User> userSugestions = new ArrayList<User>();
		ArrayList<User> userSugestionsAux = new ArrayList<User>();
		String userGender;
		SexPreference currentUserPreference;
		User userSugestion = null;
		
		userGender = current.getGender();
		currentUserPreference = current.getSetting().getSexPreference();		
		
		for (User user : users){
			if(user.getFbId()==current.getFbId())
				continue;
			
			if (current.getSetting().getMaximumAge() > user.getAge() &&
			    current.getSetting().getMininumAge() < user.getAge())
				userSugestions.add(user);
		}
		
		for (User user : userSugestions) {
			if (userGender.equals("male")){
				userSugestion = getSugestionForMale(user, currentUserPreference);
				if (userSugestion != null)
					userSugestionsAux.add(userSugestion);
			}else {
				userSugestion = getSugestionForFemale(user, currentUserPreference);
				if (userSugestion != null)
					userSugestionsAux.add(userSugestion);
			}
		}        

		return userSugestionsAux;
	}

	private User getSugestionForFemale(User user, SexPreference currentUserPreference) {
		if (currentUserPreference == SexPreference.MALE){
			if (user.getGender().equals("male")){
				if (user.getSetting().getSexPreference() == SexPreference.FEMALE){
					return user;
				}else if (user.getSetting().getSexPreference() == SexPreference.BOTH){
					return user;
				}
			}
		}
		
		if (currentUserPreference == SexPreference.FEMALE){
			if (user.getGender().equals("female")){
				if (user.getSetting().getSexPreference() == SexPreference.FEMALE){
					return user;
				}else if (user.getSetting().getSexPreference() == SexPreference.BOTH){
					return user;
				}
			}
		}
		
		if (currentUserPreference == SexPreference.BOTH){
			if (user.getSetting().getSexPreference() == SexPreference.FEMALE){
				return user;
			}else if (user.getSetting().getSexPreference() == SexPreference.BOTH){
				return user;
			}
		}
		
		return null;
	}

	private User getSugestionForMale(User user, SexPreference currentUserPreference) {
		if (currentUserPreference == SexPreference.FEMALE){
			if (user.getGender().equals("female")){
				if (user.getSetting().getSexPreference() == SexPreference.MALE){
					return user;
				} else if (user.getSetting().getSexPreference() == SexPreference.BOTH){
					return user;
				}
			}
		}
		
		if (currentUserPreference == SexPreference.MALE){
			if (user.getGender().equals("male")){
				if (user.getSetting().getSexPreference() == SexPreference.MALE){
					return user;
				} else if (user.getSetting().getSexPreference() == SexPreference.BOTH){
					return user;
				}
			}
		}
		
		if (currentUserPreference == SexPreference.BOTH){
			if (user.getSetting().getSexPreference() == SexPreference.MALE){
				return user;
			}else if (user.getSetting().getSexPreference() == SexPreference.BOTH){
				return user;
			}
		}
		
		return null;
	}

	//	METODO PARA TESTES
	private List<Sugestion> getALLPotentialUsers(boolean g) {
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
		
		System.out.println("BEGIN COMBINATIO " + new Date());
		
		for (User user : users) {
			if(user.getFbId()!=current.getFbId()){
				
				//cria objeto da sugestao
				sugestion = new Sugestion();								
				sugestion.setUser(user);
				k++;
				
				addToSugestionList(sugestion);
			
			}
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
