package com.frontrow.android.pref;


import com.frontrow.common.AppStatus;
import com.frontrow.common.Constants;
import com.frontrow.common.SharedMethods;
import com.frontrow.core.ApplicationUser;
import com.frontrow.core.TempFRSUser;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.graphics.YuvImage;

public class DataSerializer {
	private static Context context;
	private static SharedPreferences preferances;
	private static SharedPreferences.Editor prefsEditor;


	public static void setContext(Context context) {
		DataSerializer.context = context;
	}

	public static Context getContext() {
		return context;
	}


	public static void initPref(int type) {

		getPreferance();

		switch (type) {
		case Constants.SAVE_CREDENTIALS:
			saveCredentials();
			break;
		case Constants.LOAD_CREDENTIALS:
			loadCredentials();
			break;
		case Constants.REMOVE_CREDENTIALS:
			removeCredentials();
		default:
			break;
		}
	}

	private static void getPreferance() {
		try {
			preferances = context
					.getSharedPreferences(Constants.AUTH_PREF, 0);
		} catch (Exception e) {
			// TODO: handle exception
			SharedMethods.logError(e.getMessage());
		}
		prefsEditor = preferances.edit();
	}

	public static void saveCredentials() {		
		prefsEditor.putString(Constants.COMPANY_NAME, ApplicationUser.getSharedInstance().getCompanyId());
		prefsEditor.putString(Constants.MOBILE_NO, ApplicationUser.getSharedInstance().getMobileNnumber());
		prefsEditor.putString(Constants.PASSWORD, ApplicationUser.getSharedInstance().getPassword());
		prefsEditor.putString(Constants.USER_NAME, ApplicationUser.getSharedInstance().getUserName());
		prefsEditor.putInt(Constants.USER_ID, ApplicationUser.getSharedInstance().getUserId());
		prefsEditor.putString(Constants.USER_TOKEN, ApplicationUser.getSharedInstance().getToken());		
		prefsEditor.putBoolean(Constants.IS_SAVED, true);
		prefsEditor.putBoolean(Constants.IS_SINGLE_VIEW, ApplicationUser.getSharedInstance().isSingleView());
		ApplicationUser.getSharedInstance().setSaved(true);
		prefsEditor.commit();	
	}	

	public static void loadCredentials() {
		ApplicationUser appUser = ApplicationUser.getSharedInstance();
		boolean isSaved = preferances.getBoolean(Constants.IS_SAVED, false);
		boolean isSingleView = preferances.getBoolean(Constants.IS_SINGLE_VIEW, false);
		appUser.setSaved(isSaved);
		if(isSaved){			
			String companyName = preferances.getString(Constants.COMPANY_NAME, null);
			String mob = preferances.getString(Constants.MOBILE_NO, null);
			String passw = preferances.getString(Constants.PASSWORD, null);
			String userName = preferances.getString(Constants.USER_NAME, null);
			int userId = preferances.getInt(Constants.USER_ID, 0);
			String token = preferances.getString(Constants.USER_TOKEN, null);
			if (companyName != null) {
				appUser.setCompanyId(companyName);
			}
			if (passw != null) {
				appUser.setPassword(passw);
			}
			if(mob != null){
				appUser.setMobileNnumber(mob);
			}
			if(userName != null){
				appUser.setUserName(userName);
			}
			if(userId != 0){
				appUser.setUserId(userId);
			}
			if(token != null){
				appUser.setToken(token);
			}
			appUser.setSingleView(isSingleView);
		}
	}

	public static void removeCredentials(){

		//prefsEditor.remove(Constants.COMPANY_NAME);
		//prefsEditor.remove(Constants.MOBILE_NO);
		prefsEditor.remove(Constants.PASSWORD);
		prefsEditor.remove(Constants.USER_NAME);
		prefsEditor.remove(Constants.USER_ID);
		prefsEditor.remove(Constants.USER_TOKEN);
		prefsEditor.remove(Constants.PASSWORD);
		//prefsEditor.remove(Constants.IS_SAVED);
		prefsEditor.remove(Constants.PASSWORD);
		prefsEditor.remove(Constants.IS_SINGLE_VIEW);
		prefsEditor.commit();	


	}

	public static void editQuestionValues(){
		if(prefsEditor == null){
			getPreferance();
		}
		prefsEditor.putBoolean(Constants.IS_SINGLE_VIEW, ApplicationUser.getSharedInstance().isSingleView());
		prefsEditor.commit();
	}

	public static void getQuestionValues(){
		if(prefsEditor == null){
			getPreferance();
		}
		boolean isSingleView = preferances.getBoolean(Constants.IS_SINGLE_VIEW, false);
		ApplicationUser.getSharedInstance().setSingleView(isSingleView);
	}

	public static Enum<AppStatus> isOfflineUser(){
		if(prefsEditor == null){
			getPreferance();
		}
		boolean isUser = false;
		TempFRSUser tempUsr = TempFRSUser.getSharedInstance();
		String tempCmp = tempUsr.getTempCmpId();
		String tempMobile = tempUsr.getTempMobile();
		String tempPwd = tempUsr.getTempPwd();
		ApplicationUser appUser = ApplicationUser.getSharedInstance();
		boolean isSaved = preferances.getBoolean(Constants.IS_SAVED, false);
		if(isSaved){
			String companyName = preferances.getString(Constants.COMPANY_NAME, null);
			String mob = preferances.getString(Constants.MOBILE_NO, null);
			String passw = preferances.getString(Constants.PASSWORD, null);

			if( companyName != null && mob != null && passw != null ){
				if(tempCmp.trim().equalsIgnoreCase(companyName.trim())){
					if(tempMobile.trim().equalsIgnoreCase(mob.trim())){
						if(tempPwd.trim().equalsIgnoreCase(passw.trim())){
							return AppStatus.sucess;
						}
						else{
							return AppStatus.credentialerr;
						}
					}
					else{
						return AppStatus.credentialerr;
					}
				}
				else
				{
					return AppStatus.credentialerr;
				}

			}
			else{
				return AppStatus.networkerr;

			}
		}
		return AppStatus.networkerr;

	}
}
