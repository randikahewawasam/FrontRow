package com.frontrow.datamanagers;

import java.util.ArrayList;

import com.frontrow.base.DataManagerBase;
import com.row.mix.beans.ClientsNotes;
import com.row.mix.beans.CustomField;
import com.row.mix.response.ClientsNotesResponse;
import com.row.mix.response.CustomFieldResponse;

public class ClientsNoteDataManager extends DataManagerBase {
	private static ClientsNoteDataManager instance;
	private ArrayList<ClientsNotes> clientsnotelist;
	private ArrayList<ClientsNotes> clientsnotelistforsearch;
	
	public static ClientsNoteDataManager getSharedInstance(){
		if(instance == null){
			instance = new ClientsNoteDataManager();
		}
		return instance;
	}
	public ClientsNoteDataManager() {
		// TODO Auto-generated constructor stub
		clientsnotelist = new ArrayList<ClientsNotes>();
	}
	
	public void setClientsNote(ClientsNotesResponse res) {
		
		clientsnotelist=res.getClientsnoteslist();
		
	}
	
	public void setClientNoteByArrayList(ArrayList<ClientsNotes> arr){
		
		clientsnotelist=arr;
	}
	
	public ArrayList<ClientsNotes> getClientsNote(){
		
		return clientsnotelist;
	}
	
	public void setClientsNoteForSearch(ClientsNotesResponse res) {
		
		clientsnotelistforsearch=res.getClientsnoteslist();
		
	}
	
	public ArrayList<ClientsNotes> getClientsNoteForSearch(){
		
		return clientsnotelistforsearch;
	}
}
