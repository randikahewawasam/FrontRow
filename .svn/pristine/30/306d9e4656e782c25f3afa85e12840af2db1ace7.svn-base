package com.frontrow.android.pref;

import com.frontrow.common.Constants;
import com.frontrow.common.SharedMethods;

import android.content.Context;
import android.content.SharedPreferences;

public class FRSState {
	private static Context context;
	private static SharedPreferences preferances;
	private static SharedPreferences.Editor prefsEditor;

	public static void setContext(Context context) {
		FRSState.context = context;
	}

	public static void initPref(int type) {

		getPreferance();

		switch (type) {
		case Constants.SAVE_CREDENTIALS:
			saveAppState();
			break;
		case Constants.LOAD_CREDENTIALS:
			retrieveAppState();
		default:
			break;
		}
	}


	private static void getPreferance() {
		try {
			preferances = context
					.getSharedPreferences(Constants.APP_STATE, 0);
		} catch (Exception e) {
			// TODO: handle exception
			SharedMethods.logError(e.getMessage());
		}
		prefsEditor = preferances.edit();
	}
	
	private static void saveAppState() {
		
	}
	
	private static void retrieveAppState() {
		
	}

}
