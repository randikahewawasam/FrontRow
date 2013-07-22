package com.frontrow.ui;

import java.util.List;

import com.frontrow.adapters.FrontRowContactAddressAdapter;
import com.frontrow.base.BaseActivity;
import com.frontrow.common.Constants;
import com.frontrow.common.Constants.AddressType;
import com.frontrow.common.Constants.ContactType;
import com.frontrow.datamanagers.ContactsDataManager;
import com.frontrow.interfaces.FRSAddNewAddressListener;
import com.row.mix.beans.Address;
import com.row.mix.beans.ClientsNotes;
import com.row.mix.beans.Contact;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class FRSAddressList extends BaseActivity implements FRSAddNewAddressListener {
	private static final boolean Contact = false;
	private ListView addressList;
	private String clientName;
	private int contactId;
	private Contact contact;
	private FrontRowContactAddressAdapter adapter;
	private Address primaryadd;
	private int primaryaddid;
	private int clientId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_frsaddress_list);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			clientName=bundle.getString(Constants.CLIENT_NAME);
			contactId = bundle.getInt(Constants.CONTACT_ID);
			primaryaddid=bundle.getInt("PRIMARY_ADDRESS_ID");
			clientId =bundle.getInt(Constants.CLIENT_ID);
		}
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void initializeUI() {
		// TODO Auto-generated method stub

		addressList = (ListView) findViewById(R.id.addresslist);
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

			primaryadd =contact.getPrimaryAddress();
			if (primaryadd!= null)
			{				 
				primaryaddid =primaryadd.getId();
			}
		}
		List <Address> address_list = contact.getAddressList();
		adapter = new FrontRowContactAddressAdapter(this, R.layout.address_list_row,
				address_list,primaryaddid);
		addressList.setAdapter(adapter);


		addressList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
				Constants.addressType = AddressType.Delete_Address;
				// parent.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.ListSelectorColor));
				Address address = (Address) addressList
						.getItemAtPosition(position);

				if (address.getId() == primaryaddid)
				{
					address.setIsPrimary(true);

				}
				else
				{
					address.setIsPrimary(false);
				}


				bundle.putInt(Constants.CONTACT_ID, contactId);
				bundle.putInt(Constants.ADDRESS_ID, address.getId());
				bundle.putSerializable(Constants.ADDRESS_TXT, address);
				bundle.putInt(Constants.CLIENT_ID, clientId);
				//bundle.putString(Constants.CLIENT_NAME, clientname);

				Intent intent;

				intent = new Intent(FRSAddressList.this,
						FRSAddressMaintenance.class);

				intent.putExtras(bundle);
				com.frontrow.ui.TabManager parentActivity = (TabManager) getParent();
				parentActivity.startChild("AddressMaintenance_Activity",
						intent);

			}
		});
	}

	@Override
	protected void refreshUI() {
		// TODO Auto-generated method stub

	}

	@Override
	public void addAddress(View v) {
		// TODO Auto-generated method stub
		Bundle bundle = new Bundle();
		Constants.addressType = AddressType.Add_Address;
		bundle.putBoolean(Constants.NEW_CONTACT_RECORD, true);
		bundle.putInt(Constants.CONTACT_ID, contactId);
		//bundle.putInt(Constants.CLIENT_ID, clientId);
		Intent intent;
		intent = new Intent(FRSAddressList.this,FRSAddressMaintenance.class);
		intent.putExtras(bundle);
		com.frontrow.ui.TabManager parentActivity = (TabManager) getParent();
		parentActivity.startChild("AddressMaintance_Activity",
				intent);
	}
	@Override
	public void onResume() {

		super.onResume();
		setAddAddressListener(this);
	}



}
