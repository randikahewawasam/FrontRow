package com.frontrow.ui;

import java.util.List;

import com.frontrow.adapters.FrontRowContactAdapter;
import com.frontrow.adapters.FrontRowCustomFileldsAdapter;
import com.frontrow.android.db.ClientDB;
import com.frontrow.android.db.UserDB;
import com.frontrow.base.BaseActivity;
import com.frontrow.common.Constants;
import com.frontrow.common.Constants.ContactType;
import com.frontrow.common.SharedMethods;
import com.frontrow.datamanagers.CompositeDatamanager;
import com.frontrow.datamanagers.ContactsDataManager;
import com.frontrow.datamanagers.CustomFieldDataManager;
import com.frontrow.interfaces.FRSAddNewContactListener;
import com.frontrow.interfaces.FRSContactListBtnListener;
import com.google.gson.Gson;
import com.memobile.connection.AsyncHttpClient;
import com.memobile.connection.ResponseHandler;
import com.row.mix.beans.ClientsNotes;
import com.row.mix.beans.CompositeContact;
import com.row.mix.beans.Contact;
import com.row.mix.beans.CustomField;
import com.row.mix.beans.Location;
import com.row.mix.request.NewAddress;
import com.row.mix.response.AddNewAddressRespone;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class ContactList extends BaseActivity implements
FRSContactListBtnListener,FRSAddNewContactListener  {
	private ListView contactList;
	FrontRowContactAdapter adapter;
	private String clientName;
	private int clientId;
	private List<Contact> contact_list;
	private List<CompositeContact> composite_contact_list;
	private TextView contactMsg ;
	private NewAddress newAddress;
	private Contact contact;
	private Location location;
	private CompositeContact compositeContact;
	
	/* Response */
	private Object response;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_contact_list);		
		super.onCreate(savedInstanceState);
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) {
	 * getMenuInflater().inflate(R.menu.activity_contact_list, menu); return
	 * true; }
	 */

	@Override
	protected void initializeUI() {
		// TODO Auto-generated method stub
		contactMsg =(TextView) findViewById(R.id.nocontactmsg);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			clientName=bundle.getString(Constants.CLIENT_NAME);
			clientId = bundle.getInt(Constants.CLIENT_ID);
			titlename.setText(clientName);
		}
		contactList = (ListView) findViewById(R.id.contactlist);
		UserDB udb = new UserDB(ContactList.this);
		if(!udb.getUserContactStyle(appUsr.getMobileNnumber())){
			contact_list = ContactsDataManager.getSharedInstance()
					.getContactslist();
			adapter = new FrontRowContactAdapter(this, R.layout.contactlistrow,
					contact_list,null);
		}else{
			composite_contact_list = CompositeDatamanager.getSharedInstance().getCompositeContactsList();
			adapter = new FrontRowContactAdapter(this, R.layout.contactlistrow,
					null,composite_contact_list);
		}

		contactList.setAdapter(adapter);
		if (contact_list != null && contact_list.size() == 0) {
			contactMsg.setVisibility(View.VISIBLE);
		}
		else{
			contactMsg.setVisibility(View.GONE);
			
		}
		/* else {
			Bundle bundle2 = new Bundle();
			bundle2.putString(Constants.ERROR_D,
					getResources().getString(R.string.NO_CONTACT_INFO_MSG));
			bundle2.putString(Constants.TYPE, Constants.GENARAL_ERROR_TYPE);
			getParent().showDialog(Constants.ERROR_DIALOG, bundle2);

		}*/

		contactList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				Constants.contactType = ContactType.Delete_Contact;
				Contact contact = null;
				CompositeContact compositeContact = null;
				Bundle bundle = new Bundle();
				UserDB udb = new UserDB(ContactList.this);
				if(!udb.getUserContactStyle(appUsr.getMobileNnumber())){
					contact = (Contact) contactList
							.getItemAtPosition(position);
				}else{
					compositeContact = (CompositeContact) contactList.getItemAtPosition(position);
					//SharedMethods.logError("OBj "+obj.toString());
				}

				Intent intent;
				intent = new Intent(ContactList.this,FRSContactMaintenance.class);
				bundle.putInt(Constants.CONTACT_ID, (contact == null)?compositeContact.getContact().getId():contact.getId());
				bundle.putInt(Constants.CLIENT_ID, clientId);
				bundle.putString(Constants.CLIENT_NAME, clientName);
				bundle.putSerializable(Constants.CONATCT, contact);
				bundle.putSerializable(Constants.COMPOSITE_CONTACT, compositeContact);
				intent.putExtras(bundle);
				com.frontrow.ui.TabManager parentActivity = (TabManager) getParent();
				parentActivity.startChild("ContactMaintance_Activity",
						intent);
			}
		});
		//addActionlisteners();
	}

	@Override
	protected void refreshUI() {
		// TODO Auto-generated method stub
		titlename.setText(clientName);

	 if(response instanceof AddNewAddressRespone){
			getParent().removeDialog(Constants.SHOW_DIALOG);
			AddNewAddressRespone cRes = (AddNewAddressRespone) response;
			if(cRes.getResponseCode() == Constants.NETWORK_SUCCESS){
				UserDB udb = new UserDB(ContactList.this);
			
				if (udb.getUserContactStyle(appUsr.getMobileNnumber()))
				{
					compositeContact.setAddress(cRes.getAddress());
				}
				else
				{
					contact.modifyItemInAddressList(cRes.getAddress(), true);
				}
					 //contact.modifyItemInAddressList(cRes.getAddress(),newAddress.getIsPrimary());
							
			}
			Bundle bundle3 = new Bundle();
			bundle3.putSerializable("LOCATION", location);
			bundle3.putString("TYPE", "LOCATION");
			Intent subCatList = new Intent(
					ContactList.this, FRSMap.class);
	        subCatList.putExtras(bundle3);
	        com.frontrow.ui.TabManager parentActivity = (TabManager) getParent();
	        parentActivity.startChild("MAP_Activity",
			subCatList);
			

		}
	}

	@Override
	public void onResume() {
		super.onResume();
		titlename.setText(clientName);
		adapter.setContactListBtnListener(this);
		setAddContactListener(this);
	}

	@Override
	public void doBtnWorks(View v, int type, Contact con) {
		contact = con;
		if (type == Constants.NUMBERS_BTN) {
			Bundle bundle = new Bundle();
			bundle.putSerializable(Constants.CONTACT, con);
			getParent().showDialog(Constants.SHOW_CONTACTS_PHONE, bundle);
		}else if (type == Constants.MAP_BTN) {
			Bundle bundle = new Bundle();
			bundle.putSerializable("Contact", con);
			bundle.putString("TYPE", "CONTACT");
			if (con.getPrimaryAddress().getLat() != 0.0
					&& con.getPrimaryAddress().getLon() != 0.0) {
				Intent subCatList = new Intent(ContactList.this,
						FRSMap.class);
				subCatList.putExtras(bundle);
				com.frontrow.ui.TabManager parentActivity = (TabManager) getParent();
				parentActivity.startChild("MAP_Activity", subCatList);
			}
			else{
			
			String street, city, postal, province, country;
			if (con.getPrimaryAddress().getStreet()!=null && !con.getPrimaryAddress().getStreet().equalsIgnoreCase("")) {
				street = con.getPrimaryAddress().getStreet() + ",";
			} else {
				street = "";
			}
			if (con.getPrimaryAddress().getCity()!=null && !con.getPrimaryAddress().getCity().equalsIgnoreCase("")) {

				city = con.getPrimaryAddress().getCity() + ",";
			} else {
				city = "";
			}
			if (con.getPrimaryAddress().getPostal() != null
					&& !con.getPrimaryAddress().getPostal()
					.equalsIgnoreCase("")) {
				postal = con.getPrimaryAddress().getPostal() + ",";
			} else {
				postal = "";
			}
			if (con.getPrimaryAddress().getProvince() !=null && !con.getPrimaryAddress().getProvince().equalsIgnoreCase("")) {
				province = con.getPrimaryAddress().getProvince() + ",";
			} else {
				province = "";
			}
			if (con.getPrimaryAddress().getCountry() !=null && !con.getPrimaryAddress().getCountry().equalsIgnoreCase("")) {
				country = con.getPrimaryAddress().getCountry() + ",";
			} else {
				country = "";
			}

			String address = street + city +"/n"+ postal + province +"/n"+ country;
			if(address.length()>5){
			Location loc = SharedMethods.getLocationInfo(address);
			if (loc.getStatus().equalsIgnoreCase("OK")) {

				Bundle bundle3 = new Bundle();
				bundle3.putSerializable("LOCATION", loc);
				bundle3.putString("TYPE", "LOCATION");
				if (loc.getLat() != 0.0 && loc.getLng() != 0.0) {
					
					
					if (loc.getLat() != 0.0 && loc.getLng() != 0.0) {
						location =loc;
						getParent().showDialog(Constants.SHOW_DIALOG);
					    newAddress = new NewAddress() ;
      					newAddress.setId(con.getPrimaryAddress().getId());
      					newAddress.setLatitude(loc.getLat());
      					newAddress.setLongitude(loc.getLng());
      					newAddress.setCompanyCode(appUsr.getCompanyId());
      					newAddress.setClientId(0);
      					newAddress.setContactId(con.getId());
      					newAddress.setSource("A");
      					
    					Gson gson = new Gson();
    					String str = gson.toJson(newAddress);
    					if (newAddress.getId() != 0){
    					AsyncHttpClient httpClient = new AsyncHttpClient(Constants.BASE_URL+Constants.SAVE_LOCATION);				
    					httpClient.setBasicAuthToken(appUsr.getToken());
    					httpClient.put(ContactList.this, null, null, new ResponseHandler(){

    						@Override
    						public void onSuccess(String successMsg) {
    							// TODO Auto-generated method stub
    							super.onSuccess(successMsg);
    							//SharedMethods.logError("Create Contact "+successMsg);
    							AddNewAddressRespone contactResponse = new AddNewAddressRespone(successMsg, 200, "");
    							response = contactResponse;
    							refreshViewInTheUIThread();
    						}

    						@Override
    						public void onFailure(int statusCode, String content) {
    							// TODO Auto-generated method stub
    							super.onFailure(statusCode, content);
    							//SharedMethods.logError("Create Contact Error "+content);
    							AddNewAddressRespone contactResponse = new AddNewAddressRespone(content, statusCode, getResources().getString(R.string.NO_NETWORK_TRY_AGAIN_MSG));
    							response = contactResponse;
    							refreshViewInTheUIThread();
    						}

    					}, str);
						
    					}
															
					
				/*	Intent subCatList = new Intent(ContactList.this,
							FRSMap.class);
					subCatList.putExtras(bundle3);
					com.frontrow.ui.TabManager parentActivity = (TabManager) getParent();
					parentActivity.startChild("MAP_Activity", subCatList);*/
				} else {
					Bundle bundle2 = new Bundle();
					bundle2.putString(Constants.ERROR_D, getResources()
							.getString(R.string.NO_LAT_LON_FOUND));
					bundle2.putString(Constants.TYPE,
							Constants.GENARAL_ERROR_TYPE);
					getParent().showDialog(Constants.ERROR_DIALOG, bundle2);
				}
			} else {
				Bundle bundle2 = new Bundle();
				bundle2.putString(Constants.ERROR_D,
						getResources().getString(R.string.NO_LAT_LON_FOUND));
				bundle2.putString(Constants.TYPE, Constants.GENARAL_ERROR_TYPE);
				getParent().showDialog(Constants.ERROR_DIALOG, bundle2);
			}
			
			}
			else {
				Bundle bundle2 = new Bundle();
				bundle2.putString(Constants.ERROR_D,
						getResources().getString(R.string.NO_LAT_LON_FOUND));
				bundle2.putString(Constants.TYPE, Constants.GENARAL_ERROR_TYPE);
				getParent().showDialog(Constants.ERROR_DIALOG, bundle2);
				
			}
			
			}
			else {
				Bundle bundle2 = new Bundle();
				bundle2.putString(Constants.ERROR_D,
						getResources().getString(R.string.NO_LAT_LON_FOUND));
				bundle2.putString(Constants.TYPE, Constants.GENARAL_ERROR_TYPE);
				getParent().showDialog(Constants.ERROR_DIALOG, bundle2);
				
			}
			}

		}else if (type == Constants.EMAIL_BTN) {

			if (con.getEmail() != "") {

				SharedMethods.sendEmail(this, con.getEmail());
			} else {
				Bundle bundle2 = new Bundle();
				bundle2.putString(Constants.ERROR_D,
						getResources().getString(R.string.NO_EMAIL));
				bundle2.putString(Constants.TYPE, Constants.GENARAL_ERROR_TYPE);
				getParent().showDialog(Constants.ERROR_DIALOG, bundle2);
			}

		}

	}

	private void addActionlisteners() {
		contactList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
				// parent.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.ListSelectorColor));
				Contact contact = (Contact) contactList
						.getItemAtPosition(position);
				bundle.putBoolean(Constants.NEW_CONTACT_RECORD, true);
				//bundle.putSerializable(Constants.NOTE_TEXT, contact);
				//bundle.putString(Constants.CLIENT_NAME, clientname);

				//Intent intent;

				Intent intent;
				intent = new Intent(ContactList.this,FRSContactMaintenance.class);
				intent.putExtras(bundle);
				com.frontrow.ui.TabManager parentActivity = (TabManager) getParent();
				parentActivity.startChild("ContactMaintance_Activity",
						intent);

			}
		});


	}

	@Override
	public void addContact(View v) {
		// TODO Auto-generated method stub
		Bundle bundle = new Bundle();
		Constants.contactType = ContactType.Add_Contact;
		bundle.putBoolean(Constants.NEW_CONTACT_RECORD, true);
		bundle.putInt(Constants.CLIENT_ID, clientId);
		bundle.putString(Constants.CLIENT_NAME, clientName);
		Intent intent;
		intent = new Intent(ContactList.this,FRSContactMaintenance.class);
		intent.putExtras(bundle);
		com.frontrow.ui.TabManager parentActivity = (TabManager) getParent();
		parentActivity.startChild("ContactMaintance_Activity",
				intent);
	}

	@Override
	public void doBtnWorks(View v, int type, CompositeContact con) {
		// TODO Auto-generated method stub
		if (type == Constants.NUMBERS_BTN) {
			Bundle bundle = new Bundle();
			bundle.putSerializable(Constants.CONTACT, con);
			getParent().showDialog(Constants.SHOW_CONTACTS_PHONE, bundle);
		}else if (type == Constants.MAP_BTN) {
			
			Bundle bundle = new Bundle();
			bundle.putSerializable("CompositeContact", con);
			bundle.putString("TYPE", "COMPOSITECONTACT");
			if (con.getAddress().getLat() != 0.0
					&& con.getAddress().getLon() != 0.0) {
				Intent subCatList = new Intent(ContactList.this,
						FRSMap.class);
				subCatList.putExtras(bundle);
				com.frontrow.ui.TabManager parentActivity = (TabManager) getParent();
				parentActivity.startChild("MAP_Activity", subCatList);
			}
		 
			else
			{
				
			
			String street, city, postal, province, country;
			if (con.getAddress().getStreet()!=null && !con.getAddress().getStreet().equalsIgnoreCase("")) {
				street = con.getAddress().getStreet() + ",";
			} else {
				street = "";
			}
			if (con.getAddress().getCity()!=null && !con.getAddress().getCity().equalsIgnoreCase("")) {

				city = con.getAddress().getCity() + ",";
			} else {
				city = "";
			}
			if (con.getAddress().getPostal() != null
					&& !con.getAddress().getPostal()
					.equalsIgnoreCase("")) {
				postal = con.getAddress().getPostal() + ",";
			} else {
				postal = "";
			}
			if (con.getAddress().getProvince() !=null && !con.getAddress().getProvince().equalsIgnoreCase("")) {
				province = con.getAddress().getProvince() + ",";
			} else {
				province = "";
			}
			if (con.getAddress().getCountry() !=null && !con.getAddress().getCountry().equalsIgnoreCase("")) {
				country = con.getAddress().getCountry() + ",";
			} else {
				country = "";
			}

			String address = street + city +"/n"+ postal + province +"/n"+ country;
			Location loc = SharedMethods.getLocationInfo(address);
			if (loc.getStatus().equalsIgnoreCase("OK")) 
			{

				Bundle bundle3 = new Bundle();
				bundle3.putSerializable("LOCATION", loc);
				bundle3.putString("TYPE", "LOCATION");
				if (loc.getLat() != 0.0 && loc.getLng() != 0.0) {
					compositeContact = con;
					location =loc;
					getParent().showDialog(Constants.SHOW_DIALOG);
				    newAddress = new NewAddress() ;
  					newAddress.setId(con.getAddress().getId());
  					newAddress.setLatitude(loc.getLat());
  					newAddress.setLongitude(loc.getLng());
  					newAddress.setCompanyCode(appUsr.getCompanyId());
  					newAddress.setClientId(0);
  					newAddress.setContactId(con.getContact().getId());
  					
					Gson gson = new Gson();
					String str = gson.toJson(newAddress);
					if (newAddress.getId() != 0){
					AsyncHttpClient httpClient = new AsyncHttpClient(Constants.BASE_URL+Constants.SAVE_LOCATION);				
					httpClient.setBasicAuthToken(appUsr.getToken());
					httpClient.put(ContactList.this, null, null, new ResponseHandler(){

						@Override
						public void onSuccess(String successMsg) {
							// TODO Auto-generated method stub
							super.onSuccess(successMsg);
							//SharedMethods.logError("Create Contact "+successMsg);
							AddNewAddressRespone contactResponse = new AddNewAddressRespone(successMsg, 200, "");
							response = contactResponse;
							refreshViewInTheUIThread();
						}

						@Override
						public void onFailure(int statusCode, String content) {
							// TODO Auto-generated method stub
							super.onFailure(statusCode, content);
							//SharedMethods.logError("Create Contact Error "+content);
							AddNewAddressRespone contactResponse = new AddNewAddressRespone(content, statusCode, getResources().getString(R.string.NO_NETWORK_TRY_AGAIN_MSG));
							response = contactResponse;
							refreshViewInTheUIThread();
						}

					}, str);
					
					/*Intent subCatList = new Intent(ContactList.this,
							FRSMap.class);
					subCatList.putExtras(bundle3);
					com.frontrow.ui.TabManager parentActivity = (TabManager) getParent();
					parentActivity.startChild("MAP_Activity", subCatList);*/
				} else {
					Bundle bundle2 = new Bundle();
					bundle2.putString(Constants.ERROR_D, getResources()
							.getString(R.string.NO_LAT_LON_FOUND));
					bundle2.putString(Constants.TYPE,
							Constants.GENARAL_ERROR_TYPE);
					getParent().showDialog(Constants.ERROR_DIALOG, bundle2);
				}
			} else {
				Bundle bundle2 = new Bundle();
				bundle2.putString(Constants.ERROR_D,
						getResources().getString(R.string.NO_LAT_LON_FOUND));
				bundle2.putString(Constants.TYPE, Constants.GENARAL_ERROR_TYPE);
				getParent().showDialog(Constants.ERROR_DIALOG, bundle2);
			}
			
		}

		}
		}
			else if (type == Constants.EMAIL_BTN) {
			if (con.getContact().getEmail() != "") {
				SharedMethods.sendEmail(this, con.getContact().getEmail());
			} else {
				Bundle bundle2 = new Bundle();
				bundle2.putString(Constants.ERROR_D,
						getResources().getString(R.string.NO_EMAIL));
				bundle2.putString(Constants.TYPE, Constants.GENARAL_ERROR_TYPE);
				getParent().showDialog(Constants.ERROR_DIALOG, bundle2);
			}

		}
		
	}
}
