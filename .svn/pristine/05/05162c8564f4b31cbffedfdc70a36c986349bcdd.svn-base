package com.frontrow.ui;

import java.util.List;

import com.frontrow.adapters.FrontRowContactAddressAdapter;
import com.frontrow.adapters.FrontRowContactPhoneListAdapter;
import com.frontrow.base.BaseActivity;
import com.frontrow.common.Constants;
import com.frontrow.common.Constants.AddressType;
import com.frontrow.common.Constants.PhoneType;
import com.frontrow.datamanagers.ContactsDataManager;
import com.frontrow.interfaces.FRSAddNewPhoneListener;
import com.row.mix.beans.Address;
import com.row.mix.beans.Contact;
import com.row.mix.beans.Phone;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class FRSPhoneList extends BaseActivity implements FRSAddNewPhoneListener {

	private static final boolean Contact = false;
	private ListView phoneList;
	private String clientName;
	private int contactId;
	private Contact contact;
	private FrontRowContactPhoneListAdapter adapter;
	private Phone primaryphn;
	private int primaryaddid;
	private int clientId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_frsphone_list);
                        
    	 setContentView(R.layout.activity_frsphone_list);
    	 Bundle bundle = getIntent().getExtras();
 		if (bundle != null) {
 			clientName=bundle.getString(Constants.CLIENT_NAME);
 			contactId = bundle.getInt(Constants.CONTACT_ID);
 		
 			clientId =bundle.getInt(Constants.CLIENT_ID);
 		}
        super.onCreate(savedInstanceState);
    }

	@Override
	protected void initializeUI() {
		// TODO Auto-generated method stub
		phoneList = (ListView) findViewById(R.id.phonelist);
		List<Contact> contact_list = ContactsDataManager.getSharedInstance().getContactslist();
		titlename.setText(clientName);
		for (Contact con : contact_list){
			
			if(con.getId() == contactId) 
			{
				contact = con;
				break;
			}
			
		}
		 
		 
		 if (primaryaddid == 0)
		 {
			 
			 primaryphn =contact.getPrimaryPhone();
			 if (primaryphn!= null)
			 {				 
			 primaryaddid =primaryphn.getPhoneID();
			 }
		 }
		List <Phone> phone_list = contact.getPhoneList();
		adapter = new FrontRowContactPhoneListAdapter(this, R.layout.phone_list_row,
				phone_list,primaryaddid);
		phoneList.setAdapter(adapter);
		
		
		phoneList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
			Bundle bundle = new Bundle();
				Constants.phoneType = PhoneType.Delete_Phone;
				// parent.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.ListSelectorColor));
				Phone phone = (Phone) phoneList
						.getItemAtPosition(position);
			
					if (phone.getPhoneID() == primaryaddid)
					{
						phone.setIsPrimary(true);
						
					}
					else
					{
						phone.setIsPrimary(false);
					}
					
				
			    bundle.putInt(Constants.CONTACT_ID, contactId);
				bundle.putInt(Constants.PHONE_ID, phone.getPhoneID());
				bundle.putSerializable(Constants.PHONE_TXT, phone);
				bundle.putInt(Constants.CLIENT_ID, clientId);
				//bundle.putString(Constants.CLIENT_NAME, clientname);

				Intent intent;

				intent = new Intent(FRSPhoneList.this,
						FRSPhoneMaintenance.class);

				intent.putExtras(bundle);
				com.frontrow.ui.TabManager parentActivity = (TabManager) getParent();
				parentActivity.startChild("PhoneMaintenance_Activity",
						intent);

			}
		});

	}
	@Override
	public void onResume() {
	
		super.onResume();
		setAddPhoneListener(this);
	}

	@Override
	protected void refreshUI() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addPhone(View v) {
		// TODO Auto-generated method stub
		
		Bundle bundle = new Bundle();
		Constants.phoneType = PhoneType.Add_Phone;
		bundle.putBoolean(Constants.NEW_CONTACT_RECORD, true);
		bundle.putInt(Constants.CONTACT_ID, contactId);
		//bundle.putInt(Constants.CLIENT_ID, clientId);
		Intent intent;
		intent = new Intent(FRSPhoneList.this,FRSPhoneMaintenance.class);
		intent.putExtras(bundle);
		com.frontrow.ui.TabManager parentActivity = (TabManager) getParent();
		parentActivity.startChild("AddressMaintance_Activity",
				intent);
	}
}
