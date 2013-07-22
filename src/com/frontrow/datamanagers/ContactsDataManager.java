package com.frontrow.datamanagers;

import java.util.ArrayList;

import com.frontrow.base.DataManagerBase;
import com.row.mix.beans.ClientsNotes;
import com.row.mix.beans.Contact;
import com.row.mix.response.ClientsNotesResponse;
import com.row.mix.response.ContactsResponse;

public class ContactsDataManager extends DataManagerBase {
	private static ContactsDataManager instance;
	private ArrayList<Contact> contactslist;
	
	
	public static ContactsDataManager getSharedInstance(){
		if(instance == null){
			instance = new ContactsDataManager();
		}
		return instance;
	}
	public ContactsDataManager() {
		// TODO Auto-generated constructor stub
		contactslist = new ArrayList<Contact>();
	}
	
	public void setContactlist(ContactsResponse res) {
		
		contactslist=res.getContactslist();
		
	}
	
	public ArrayList<Contact> getContactslist() {
		return contactslist;

}
}
