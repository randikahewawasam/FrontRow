package com.frontrow.datamanagers;


import java.util.ArrayList;

import com.frontrow.base.DataManagerBase;
import com.row.mix.beans.Client;
import com.row.mix.beans.CustomField;
import com.row.mix.response.AuthenticationResponse;
import com.row.mix.response.CustomFieldResponse;

public class CustomFieldDataManager extends DataManagerBase {

	private static CustomFieldDataManager instance;
	private ArrayList<CustomField> customfieldlist;
	
	public static CustomFieldDataManager getSharedInstance(){
		if(instance == null){
			instance = new CustomFieldDataManager();
		}
		return instance;
	}

	public CustomFieldDataManager() {
		// TODO Auto-generated constructor stub
		customfieldlist = new ArrayList<CustomField>();
	}
	
	public void setCustomFilelds(CustomFieldResponse res) {
		
		customfieldlist=res.getCustomfieldlist();
		
	}
	
	public ArrayList<CustomField> getCustomFields(){
		
		return customfieldlist;
	}
}
