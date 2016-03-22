package controller;

import model.Setting;
import model.User;

public class SettingController {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public Setting findSetting(User currentUser){
		
		Setting st = currentUser.getSetting();
		
		return st;		
	}

}
