package com.frontrow.datamanagers;

import java.util.ArrayList;

import com.frontrow.base.DataManagerBase;
import com.row.mix.beans.CompositeContact;

public class CompositeDatamanager extends DataManagerBase{
	private static CompositeDatamanager instance;
	private ArrayList<CompositeContact> compositeContactsList;
	
	public static CompositeDatamanager getSharedInstance(){
		if(instance == null){
			instance = new CompositeDatamanager();
		}
		return instance;
	}
	
	public CompositeDatamanager() {
		// TODO Auto-generated constructor stub
		compositeContactsList = new ArrayList<CompositeContact>();
	}

	public ArrayList<CompositeContact> getCompositeContactsList() {
		return compositeContactsList;
	}

	public void setCompositeContactsList(
			ArrayList<CompositeContact> compositeContactsList) {
		this.compositeContactsList = compositeContactsList;
	}
	
	

}
