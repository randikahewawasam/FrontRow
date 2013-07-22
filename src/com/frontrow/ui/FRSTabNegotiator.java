package com.frontrow.ui;


import java.util.ArrayList;

import com.frontrow.adapters.FrontRowClientsNoteAdapter;
import com.frontrow.adapters.FrontRowContactPhoneAdapter;
import com.frontrow.common.Constants;
import com.frontrow.common.SharedMethods;
import com.row.mix.beans.CompositeContact;
import com.row.mix.beans.Contact;
import com.row.mix.beans.Phone;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class FRSTabNegotiator extends TabManager {
	public static final String INTENT_KEY = "id";
	private Bundle bundle;
	private ProgressDialog dialog;
	ListView phonelist;
	int refType = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		bundle = getIntent().getExtras();
		refType = bundle.getInt(Constants.ACTIVITY_TYPE);
		SharedMethods.logError("Ref Type  :- " + refType);
		redirect(refType);
	}

	private void redirect(int type) {
		switch (type) {
		case Constants.MESSAGES_REF:
			try {
				startChild("Message_Activity", new Intent(this,
						FRSMessages.class));
			} catch (Exception e) {
				// TODO: handle exception
				SharedMethods.logError("TabNe " + e.getMessage());
			}

			break;
		case Constants.CLIENTS_REF:
			startChild("Client_Activity", new Intent(this, FRSClientList.class));
			break;
		case Constants.SETTINGS_REF:
			startChild("Settings_Activity", new Intent(this, FRSSettings.class));
			break;

		default:
			break;
		}
	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {

		switch (id) {

		case Constants.ERROR_DIALOG:
			AlertDialog al = null;
			AlertDialog.Builder errDialog = new AlertDialog.Builder(getParent());
			String errr = args.getString(Constants.ERROR_D);
			final String type = args.getString(Constants.TYPE);
			SharedMethods.logError("Type : " + type);
			errDialog.setCancelable(false);
			errDialog.setMessage(errr);
			// String btnTxt =
			// (type.equals(Constants.VERIFY_MOBILE_TYPE))?getResources().getString(R.string.VERIFY):getResources().getString(R.string.OK);
			if (!type.equals(Constants.GENARAL_ERROR_TYPE)) {
				if (!type.equals(Constants.CARD_SUBMIT_TYPE)) {

					if (!type.equals(Constants.APP_EXIT_TYPE))
					{

						errDialog.setPositiveButton("yes",
								new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (type != null) {
									if (type.equals(Constants.NAVIGATE_TO_PREVIOUS_PAGE_TYPE)) {
										FRSClientsNoteDescription clientNoteDesc = (FRSClientsNoteDescription) getLocalActivityManager()
												.getCurrentActivity();
										clientNoteDesc
										.navigateToPreviousPage();
									} else if (type
											.equals(Constants.LOG_OUT_TYPE)) {
										FRSSettings setting = (FRSSettings) getLocalActivityManager()
												.getCurrentActivity();
										setting.logout();

									}
									else if (type
											.equals(Constants.DELETE_CONTACT_TYPE)) {
										FRSContactMaintenance contactMain = (FRSContactMaintenance) getLocalActivityManager()
												.getCurrentActivity();
										contactMain.deleteContact();

									}
									else if (type
											.equals(Constants.DELETE_ADDRESS_TYPE)) {
										FRSAddressMaintenance addresstMain = (FRSAddressMaintenance) getLocalActivityManager()
												.getCurrentActivity();
										addresstMain.deleteAddress();

									}
									
									else if (type
											.equals(Constants.DELETE_PHONE_TYPE)) {
										FRSPhoneMaintenance phonetMain = (FRSPhoneMaintenance) getLocalActivityManager()
												.getCurrentActivity();
										phonetMain.deletePhone();

									}

								}
								removeDialog(Constants.ERROR_DIALOG);
							}
						});

						errDialog.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (type != null) {
									if (type.equals(Constants.NAVIGATE_TO_PREVIOUS_PAGE_TYPE)) {
										removeDialog(Constants.ERROR_DIALOG);
									} else if (type
											.equals(Constants.LOG_OUT_TYPE)) {
										removeDialog(Constants.ERROR_DIALOG);

									}
									 else if (type
												.equals(Constants.DELETE_CONTACT_TYPE)) {
											removeDialog(Constants.ERROR_DIALOG);

										}
									 else if (type
												.equals(Constants.DELETE_ADDRESS_TYPE)) {
											removeDialog(Constants.ERROR_DIALOG);

										}
									 else if (type
												.equals(Constants.DELETE_PHONE_TYPE)) {
											removeDialog(Constants.ERROR_DIALOG);

										}

								}

							}
						});


					}

					else {

						errDialog.setPositiveButton("yes",
								new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								finish();				
								java.lang.System.exit(0);
								removeDialog(Constants.ERROR_DIALOG);
							}
						});

						errDialog.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								removeDialog(Constants.ERROR_DIALOG);
							}
						});


					}
				} else {

					errDialog.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							removeDialog(Constants.ERROR_DIALOG);
							ActivityCardPostView actpostview = (ActivityCardPostView) getLocalActivityManager()
									.getCurrentActivity();
							actpostview.navigateClientListPage();


						}
					});

				}
			} else {

				errDialog.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {

						removeDialog(Constants.ERROR_DIALOG);
					}
				});

			}

			al = errDialog.create();

			WindowManager.LayoutParams lpA = new WindowManager.LayoutParams();
			lpA.copyFrom(al.getWindow().getAttributes());
			lpA.width = WindowManager.LayoutParams.FILL_PARENT;
			lpA.height = WindowManager.LayoutParams.WRAP_CONTENT;
			lpA.dimAmount = 0.5f;
			al.setOwnerActivity((Activity) getParent());
			al.getWindow().setAttributes(lpA);
			al.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
			return al;
		case Constants.ADD_NOTE_DIALOG:

			Dialog addNote = new Dialog(this, R.style.customeAddNoteDialog);
			addNote.setCancelable(true);
			WindowManager.LayoutParams addNotelp = new WindowManager.LayoutParams();
			addNotelp.width = WindowManager.LayoutParams.FILL_PARENT;
			addNotelp.height = WindowManager.LayoutParams.WRAP_CONTENT;
			addNotelp.dimAmount = 0.5f;

			View sCriteriaView = LayoutInflater.from(this).inflate(
					R.layout.frsaddnote, null);
			addNote.getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
			addNote.requestWindowFeature(Window.FEATURE_NO_TITLE);
			addNote.setContentView(sCriteriaView);
			addNote.setTitle(getResources().getString(R.string.ADD_NOTE));
			final EditText note = (EditText) sCriteriaView
					.findViewById(R.id.addNoteText);
			Button search = (Button) sCriteriaView
					.findViewById(R.id.btnAddNote);
			ImageButton close = (ImageButton) sCriteriaView
					.findViewById(R.id.dismiss);
			search.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!note.getText().toString().equals("")) {
						FRSClientsNotes cnotes = (FRSClientsNotes) getLocalActivityManager()
								.getCurrentActivity();
						// FRSClientsNotes cnotes= (FRSClientsNotes)
						// getLocalActivityManager().getActivity("FRSClientsNoteActivity");
						cnotes.saveNote(note.getText().toString());
					} else {
						/*
						 * NearBy near= (NearBy)
						 * getLocalActivityManager().getActivity
						 * ("NearByActivity");
						 * near.getSearchResults(location.getText().toString(),
						 * merchant.getText().toString());
						 */
					}

				}
			});

			close.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					removeDialog(Constants.ADD_NOTE_DIALOG);

				}
			});

			addNote.getWindow().setAttributes(addNotelp);
			addNote.getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_DIM_BEHIND);

			return addNote;

		case Constants.LOCATION_SETTINGS_DIALOG:
			final Dialog locationSettingsDialog = new Dialog(this, R.style.customeDialog);
			locationSettingsDialog.setCancelable(false);
			WindowManager.LayoutParams lpLoc = new WindowManager.LayoutParams();
			lpLoc.width = WindowManager.LayoutParams.FILL_PARENT;
			lpLoc.height = WindowManager.LayoutParams.WRAP_CONTENT;
			lpLoc.dimAmount=0.5f;
			View locationSettingsView = LayoutInflater.from(this).inflate(R.layout.frs_location_service, null);
			locationSettingsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			locationSettingsDialog.setContentView(locationSettingsView);
			final Button locationSettingBtn = (Button) locationSettingsView.findViewById(R.id.location_settings);
			ImageButton close3 = (ImageButton) locationSettingsView
					.findViewById(R.id.dismiss);

			close3.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					removeDialog(Constants.LOCATION_SETTINGS_DIALOG);

				}
			});

			locationSettingBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					FRSClientList nearBy = (FRSClientList) getLocalActivityManager().getCurrentActivity();
					nearBy.openlocationsettings();
					removeDialog(Constants.LOCATION_SETTINGS_DIALOG);
				}
			});


			locationSettingsDialog.getWindow().setAttributes(lpLoc);
			locationSettingsDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
			return locationSettingsDialog;

		case Constants.SHOW_DIALOG:
			String msg ="";
			if (args != null){
				msg = args.getString(Constants.PROGRESS_MSG);
			}
			dialog = new ProgressDialog(this);
			if (msg!=null && msg.length() >2)
			{
				dialog.setMessage(msg);	
			}
			else{
				dialog.setMessage(getResources().getString(R.string.PROGRESS_MSG));	
			}

			dialog.setCancelable(false);
			dialog.setTitle("");
			dialog.setIndeterminate(true);
			return dialog;
		case Constants.DISMISS_DIALOG:
			dialog.dismiss();
			break;
		case Constants.UPDATE_ALERT:
			return SharedMethods.createSingleButtonAlert(this);
		case Constants.SEARCH_MORE:
			dialog = new ProgressDialog(this);
			dialog.setMessage(getResources()
					.getString(R.string.SEARCH_PROGRESS));
			dialog.setTitle("");
			dialog.setIndeterminate(true);
			return dialog;
		default:
			break;
		case Constants.SHOW_CONTACTS_PHONE:
			Contact con = null;
			CompositeContact compositeContact = null;
			try{
				con = (Contact) args.getSerializable(Constants.CONTACT);
			}catch (Exception e) {
				// TODO: handle exception
				compositeContact =(CompositeContact) args.getSerializable(Constants.CONTACT);
			}
			Dialog showcontactphones = new Dialog(this, R.style.customeDialog);
			showcontactphones.setCancelable(true);
			WindowManager.LayoutParams showcontactphoneslp = new WindowManager.LayoutParams();
			showcontactphoneslp.width = WindowManager.LayoutParams.FILL_PARENT;
			showcontactphoneslp.height = WindowManager.LayoutParams.WRAP_CONTENT;
			showcontactphoneslp.dimAmount = 0.5f;

			View showContactPhoneView = LayoutInflater.from(this).inflate(
					R.layout.contactsnumberlist, null);
			showcontactphones.getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
			showcontactphones.requestWindowFeature(Window.FEATURE_NO_TITLE);
			showcontactphones.setContentView(showContactPhoneView);
			showcontactphones.setTitle(getResources().getString(
					R.string.ADD_NOTE));
			// final EditText note = (EditText)
			// showContactPhoneView.findViewById(R.id.addNoteText);
			// Button search = (Button)
			// showContactPhoneView.findViewById(R.id.btnAddNote);
			// ImageButton close =(ImageButton )
			// showContactPhoneView.findViewById(R.id.dismiss);
			phonelist = (ListView) showcontactphones
					.findViewById(R.id.contactsphonelist);
			ArrayList<Phone> phnList = new ArrayList<Phone>();
			if(con == null){
				if(compositeContact.getBusinessPhone() != null && compositeContact.getBusinessPhone().getPhoneNumber() != null && compositeContact.getBusinessPhone().getPhoneNumber().length() > 0 ){
					phnList.add(compositeContact.getBusinessPhone());
				}
				if(compositeContact.getMobilePhone() != null && compositeContact.getMobilePhone().getPhoneNumber() != null && compositeContact.getMobilePhone().getPhoneNumber().length() > 0){
					phnList.add(compositeContact.getMobilePhone());
				}
			}

			FrontRowContactPhoneAdapter adapter = new FrontRowContactPhoneAdapter(
					this, R.layout.contactsnumberlistrow,(con == null)?phnList:con.getPhoneList());
			phonelist.setAdapter(adapter);

			ImageButton close2 = (ImageButton) showcontactphones
					.findViewById(R.id.dismissshowcontactlist);

			close2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					removeDialog(Constants.SHOW_CONTACTS_PHONE);

				}
			});

			phonelist.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View arg1,
						int position, long arg3) {
					// TODO Auto-generated method stub
					Bundle bundle = new Bundle();
					// parent.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.ListSelectorColor));
					Phone phone = (Phone) phonelist.getItemAtPosition(position);
					SharedMethods.callPhone(getParent(), phone.getPhoneNumber());

				}
			});

			showcontactphones.getWindow().setAttributes(showcontactphoneslp);
			showcontactphones.getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_DIM_BEHIND);

			return showcontactphones;

		}
		return null;
	}
}
