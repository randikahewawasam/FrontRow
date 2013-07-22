
package com.frontrow.language;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.Character.UnicodeBlock;
import java.nio.Buffer;
import java.util.Locale;
import java.util.regex.Pattern;
import com.frontrow.common.Constants;
import com.frontrow.common.SharedMethods;
import android.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.net.Uri;

public class LanguageManager {

	private String currentLanguage;
	private static LanguageManager manager;
	private static Context context;
	private static final String LAN_SETTINGS_PATH = "language/LanSettings.txt";
	public static final String LANGUAGES = "SUPPORTED_LANGUAGES";
	private static final String PREFERENCES_NAME = "lan_settings_manager";
	private SharedPreferences preferences;
	public static final String DEFAULT_LANGUAGE = "EN";

	private LanguageManager(){

		currentLanguage = "EN";
	}

	public static LanguageManager getSharedInstance(){

		if(manager == null){
			manager = new LanguageManager();
		}
		return manager;
	}    

	public static String getLanguages() {
		return LANGUAGES;
	}

	public void setContext(Context c){
		this.context = c;
	}

	public static String getString(int id){

		return context.getResources().getString(id);
	}


	public void loadSettings(Context context){
		this.context = context;
		this.preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_WORLD_WRITEABLE);

		try {
			Editor editor = preferences.edit();

			SharedMethods.logMessage("start loading the language settings.");
			
			//InputStreamReader(this.getResources.openRawResource(R.raw.textfile)))			
			//InputStream is = context.getAssets().open(LAN_SETTINGS_PATH);
			AssetManager am = context.getAssets();
			InputStream is = am.open(LAN_SETTINGS_PATH);
			BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));			
			String line = null;		
	
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if(line.length() == 0) continue;
				if(line.startsWith("#")) continue;				
				int index = line.indexOf("=");
				if(index > 0){
					String key = line.substring(0, index);
					String value = line.substring(index+1, line.length());					
					editor.putString(key, value);
				}
			}
			br.close();
			editor.commit();
		} catch (Exception e) {
			SharedMethods.logException(e, this.getClass());
		}
	}


	public static void setLangague(Context ctx,String langague) {
		Locale locale1 = Locale.getDefault();			
		if(!langague.equals(DEFAULT_LANGUAGE)){
			Locale locale2 = new Locale(langague);
			Locale l = locale2.getDefault();
			SharedMethods.logError(l.toString());
			if(!locale1.equals(locale2)){			
				Locale.setDefault(locale2);		
				Configuration config2 = new Configuration();		
				config2.locale = locale2;		
				ctx.getResources().updateConfiguration(config2,ctx.getResources().getDisplayMetrics());
			}
		}
	}

	public String[] getSupportedLanguages() {
		String val = getSettingsValueForKey(LANGUAGES);
		String[] languages_values = val.split(Constants.VALUE_SEPERATOR);
		String[] languages = new String[languages_values.length];
		for (int i = 0; i < languages_values.length; i++) {
			String s = languages_values[i].trim();
			String[] s1 = s.split(Pattern.quote(Constants.SEPERATOR_PIPE));
			languages[i] = Language.getNativeString(s1[0]);
			
			//languages[i] = Uri.decode(languages[i]);
		}
		return languages;
	}
	
	public String getLanguageLocale(String language) {
		String locale = null;
		String val = getSettingsValueForKey(LANGUAGES);
		String[] languages_values = val.split(Constants.VALUE_SEPERATOR);
		for (int i = 0; i < languages_values.length; i++) {
			String s = languages_values[i].trim();
			String[] s1 = s.split(Pattern.quote(Constants.SEPERATOR_PIPE));		
			if(Language.getNativeString(s1[0]).trim().equalsIgnoreCase(language)){
				locale = s1[1];
				return locale;
			}
		}
		return locale;
	}
	
	private String getSettingsValueForKey(String key){
		return preferences.getString(key, "");
	}
}
