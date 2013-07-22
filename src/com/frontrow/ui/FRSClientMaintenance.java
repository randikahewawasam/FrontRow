package com.frontrow.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.frontrow.adapters.FrontRowClientActivityAdapter;
import com.frontrow.android.db.ClientDB;
import com.frontrow.android.db.FRSTermsDB;
import com.frontrow.android.db.UserDB;
import com.frontrow.android.pref.DataSerializer;
import com.frontrow.base.BaseActivity;
import com.frontrow.common.Constants;
import com.frontrow.common.SharedMethods;
import com.frontrow.common.Constants.AddressType;
import com.frontrow.core.ApplicationUser;
import com.frontrow.core.TempFRSUser;
import com.frontrow.datamanagers.AuthDataManager;
import com.frontrow.datamanagers.ClientDataManager;
import com.frontrow.datamanagers.ClientsNoteDataManager;
import com.frontrow.datamanagers.CompositeDatamanager;
import com.frontrow.datamanagers.ContactsDataManager;
import com.frontrow.datamanagers.CustomFieldDataManager;
import com.frontrow.datamanagers.FRSSearchDataManager;
import com.frontrow.interfaces.IFrontResponseReceiver;
import com.google.gson.Gson;
import com.memobile.connection.AsyncHttpClient;
import com.memobile.connection.ResponseHandler;
import com.row.mix.beans.Client;
import com.row.mix.beans.Contact;
import com.row.mix.beans.CustomField;
import com.row.mix.beans.Location;
import com.row.mix.beans.User;
import com.row.mix.request.NewAddress;
import com.row.mix.response.AddNewAddressRespone;
import com.row.mix.response.AuthenticationResponse;
import com.row.mix.response.CardSubmitResponse;
import com.row.mix.response.ClientsNotesResponse;
import com.row.mix.response.CompositeContactResponse;
import com.row.mix.response.ContactsResponse;
import com.row.mix.response.CustomFieldResponse;

public class FRSClientMaintenance extends BaseActivity implements
IFrontResponseReceiver {
	private Button callbtn;
	private Button mapbtn;
	private Button direcbtn;
	private TextView cname;
	private TextView caddress;
	private TextView ctpnumber;
	private TextView clnumber;
	private ListView clientactivityList;
	private ArrayList<Client> clientList;
	private Client aclient;
	private Boolean isShowMore;
	private NewAddress newAddress;
	private Location loc;
	private Boolean isDirect;
	
	/* Response */
	private Object response;

	// private ApplicationUser appUsr;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		setContentView(R.layout.activity_frsclient_maintenance);

		super.onCreate(savedInstanceState);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_frsclient_maintenance, menu);
		return true;
	}

	@Override
	protected void initializeUI() {

		clientactivityList = (ListView) findViewById(R.id.clintprofilelist);
		callbtn = (Button) findViewById(R.id.callbtn);
		mapbtn = (Button) findViewById(R.id.mapbtn);
		direcbtn = (Button) findViewById(R.id.directionbtn);
		callbtn.setText(getResources().getString(R.string.CALL_TXT));
		mapbtn.setText(getResources().getString(R.string.MAP_TXT));
		direcbtn.setText(getResources().getString(R.string.DIRECTION_TXT));
		String[] clint_activity = getResources().getStringArray(
				R.array.CLIENT_ACTIVITY);
		List<String> clint_activity_list = Arrays.asList(clint_activity);
		String[] clint_activity_images = getResources().getStringArray(
				R.array.CLIENT_ACTIVITY_IMAGE);
		List<String> clint_activity_images_list = Arrays
				.asList(clint_activity_images);
		FrontRowClientActivityAdapter adapter = new FrontRowClientActivityAdapter(
				this, R.layout.clientprofilelistrow, clint_activity_list,
				clint_activity_images_list);
		clientactivityList.setAdapter(adapter);
		// clientactivityList.setAdapter(new
		// ListAdapter(this,R.layout.clientprofilelistrow,R.id.clintactname,clint_activity
		// ));
		ClientDB clientDB = new ClientDB(this);
		clientList = clientDB.getAllClinets(appUsr.getUserId());
		cname = (TextView) findViewById(R.id.clname);
		caddress = (TextView) findViewById(R.id.claddress);
		ctpnumber = (TextView) findViewById(R.id.clmobilenumber);
		clnumber =(TextView) findViewById(R.id.clnumber);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			String clientNumber = bundle.getString(Constants.CLIENT_NUMBER);
			isShowMore = bundle.getBoolean(Constants.IS_SHOW_MORE);  
			if(isShowMore){
				clientList = ClientDataManager.getSharedInstance().getClientList();
			}
			for (int j = 0; j < clientList.size(); j++) {
				Client client = clientList.get(j);
				if (client.getClientNumber().trim()
						.equalsIgnoreCase(clientNumber.trim())) {
					aclient = client;
				}
			}
			if (aclient != null) {
				cname.setText(aclient.getClientName());
				ctpnumber.setText(aclient.getMobilenumber());
				String street, city, postal, province, country;
				if (aclient.getAddress().getStreet() !=null && !aclient.getAddress().getStreet().equalsIgnoreCase("")) {
					street = aclient.getAddress().getStreet() + ",";
				} else {
					street = "";
				}
				if (aclient.getAddress().getCity()!=null && !aclient.getAddress().getCity().equalsIgnoreCase("")) {

					city = aclient.getAddress().getCity() + ",";
				} else {
					city = "";
				}
				if (aclient.getAddress().getPostal() != null
						&& !aclient.getAddress().getPostal().equalsIgnoreCase("")) {
					postal = aclient.getAddress().getPostal() + ",";
				} else {
					postal = "";
				}
				if (aclient.getAddress().getProvince()!=null && !aclient.getAddress().getProvince().equalsIgnoreCase("")) {
					province = aclient.getAddress().getProvince() + ",";
				} else {
					province = "";
				}

				String address = street + city + postal + province;
				// String address =
				// aclient.getStreet()+","+aclient.getCity()+","+aclient.getProvince()+","+aclient.getPostal();
				caddress.setText(address);
				titlename.setText(aclient.getClientName());
				clnumber.setText(aclient.getClientNumber());
			}

		}

		callbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (aclient.getMobilenumber() != null
						&& aclient.getMobilenumber().length()>3) {
					SharedMethods.callPhone(getParent(),
							aclient.getMobilenumber());
				} else {

					Bundle bundle = new Bundle();
					bundle.putString(Constants.ERROR_D, getResources()
							.getString(R.string.NO_PHN));
					bundle.putString(Constants.TYPE,
							Constants.GENARAL_ERROR_TYPE);
					getParent().showDialog(Constants.ERROR_DIALOG, bundle);
				}

			}
		});

		mapbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isDirect = false;
				Bundle bundle = new Bundle();
				bundle.putSerializable("CLIENT", aclient);
				bundle.putString("TYPE", "LNG");
				if (aclient.getAddress().getLat() != 0.0
						&& aclient.getAddress().getLon() != 0.0) {
					Intent subCatList = new Intent(FRSClientMaintenance.this,
							FRSMap.class);
					subCatList.putExtras(bundle);
					com.frontrow.ui.TabManager parentActivity = (TabManager) getParent();
					parentActivity.startChild("MAP_Activity", subCatList);
				} else {

					String street, city, postal, province, country;
					if (aclient.getAddress().getStreet()!=null && !aclient.getAddress().getStreet().equalsIgnoreCase("")) {
						street = aclient.getAddress().getStreet() + ",";
					} else {
						street = "";
					}
					if (aclient.getAddress().getCity()!=null && !aclient.getAddress().getCity().equalsIgnoreCase("")) {

						city = aclient.getAddress().getCity() + ",";
					} else {
						city = "";
					}
					if (aclient.getAddress().getPostal() != null
							&& !aclient.getAddress().getPostal().equalsIgnoreCase("")) {
						postal = aclient.getAddress().getPostal() + ",";
					} else {
						postal = "";
					}
					if (aclient.getAddress().getProvince()!=null && !aclient.getAddress().getProvince().equalsIgnoreCase("")) {
						province = aclient.getAddress().getProvince() + ",";
					} else {
						province = "";
					}
					/*
					 * if(aclient.get!=""){
					 * country=con.getPrimaryAddress().getCountry()+","; } else{
					 * country=""; }
					 */

					String address = street + city + postal + province;
					if (address.length()>5){
					 loc = SharedMethods.getLocationInfo(address);
					
					if (loc.getStatus().equalsIgnoreCase("OK")) {

						
					if (loc.getLat() != 0.0 && loc.getLng() != 0.0) {
							
							getParent().showDialog(Constants.SHOW_DIALOG);
						    newAddress = new NewAddress() ;
	      					newAddress.setId(aclient.getAddress().getId());
	      					newAddress.setLatitude(loc.getLat());
	      					newAddress.setLongitude(loc.getLng());
	      					newAddress.setCompanyCode(appUsr.getCompanyId());
	      					newAddress.setClientId(aclient.getClientId());
	      					newAddress.setContactId(0);
	      					newAddress.setSource("A");
	      					
	    					Gson gson = new Gson();
	    					String str = gson.toJson(newAddress);
	    					if (newAddress.getId() != 0){
	    					AsyncHttpClient httpClient = new AsyncHttpClient(Constants.BASE_URL+Constants.SAVE_LOCATION);				
	    					httpClient.setBasicAuthToken(appUsr.getToken());
	    					httpClient.put(FRSClientMaintenance.this, null, null, new ResponseHandler(){

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
							
							
					
						} else {
							Bundle bundle2 = new Bundle();
							bundle2.putString(Constants.ERROR_D, getResources()
									.getString(R.string.NO_LAT_LON_FOUND));
							bundle2.putString(Constants.TYPE,
									Constants.GENARAL_ERROR_TYPE);
							getParent().showDialog(Constants.ERROR_DIALOG,
									bundle2);
						}
					}}
					else {
						Bundle bundle2 = new Bundle();
						bundle2.putString(Constants.ERROR_D, getResources()
								.getString(R.string.NO_LAT_LON_FOUND));
						bundle2.putString(Constants.TYPE,
								Constants.GENARAL_ERROR_TYPE);
						getParent().showDialog(Constants.ERROR_DIALOG, bundle2);
					}
				}

			}
		});

		direcbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				isDirect = true;
				if (aclient.getAddress().getLat() != 0.0
						&& aclient.getAddress().getLon() != 0.0) {
					double lat = aclient.getAddress().getLat();
					double lon = aclient.getAddress().getLon();
					String fullPath = Constants.NATIVE_MAP_PATH
							+ Constants.SADDR
							+ Double.toString(appUsr.getLat()) + ","
							+ Double.toString(appUsr.getLon()) + "&"
							+ Constants.DADDR + Double.toString(lat) + ","
							+ Double.toString(lon);
					final Intent intent = new Intent(Intent.ACTION_VIEW, Uri
							.parse(fullPath));
					intent.setClassName("com.google.android.apps.maps",
							"com.google.android.maps.MapsActivity");
					startActivity(intent);
				} else {

					String street, city, postal, province, country;
					if (aclient.getAddress().getStreet()!=null && !aclient.getAddress().getStreet().equalsIgnoreCase("")) {
						street = aclient.getAddress().getStreet() + ",";
					} else {
						street = "";
					}
					if (aclient.getAddress().getCity()!=null && !aclient.getAddress().getCity().equalsIgnoreCase("")) {

						city = aclient.getAddress().getCity() + ",";
					} else {
						city = "";
					}
					if (aclient.getAddress().getPostal() != null
							&& !aclient.getAddress().getPostal().equalsIgnoreCase("")) {
						postal = aclient.getAddress().getPostal() + ",";
					} else {
						postal = "";
					}
					if (aclient.getAddress().getProvince()!=null && !aclient.getAddress().getProvince().equalsIgnoreCase("")) {
						province = aclient.getAddress().getProvince() + ",";
					} else {
						province = "";
					}
					/*
					 * if(aclient.get!=""){
					 * country=con.getPrimaryAddress().getCountry()+","; } else{
					 * country=""; }
					 */

					String address = street + city + postal + province;
					if (address.length()>5)
					{
					 loc = SharedMethods.getLocationInfo(address);
					
					if (loc.getStatus().equalsIgnoreCase("OK")) {
						if (loc.getLat() != 0.0 && loc.getLng() != 0.0) {
							
							
							getParent().showDialog(Constants.SHOW_DIALOG);
						    newAddress = new NewAddress() ;
	      					newAddress.setId(aclient.getAddress().getId());
	      					newAddress.setLatitude(loc.getLat());
	      					newAddress.setLongitude(loc.getLng());
	      					newAddress.setCompanyCode(appUsr.getCompanyId());
	      					newAddress.setClientId(aclient.getClientId());
	      					newAddress.setContactId(0);
	      					newAddress.setSource("A");
	      					
	    					Gson gson = new Gson();
	    					String str = gson.toJson(newAddress);
	    					if (newAddress.getId() != 0){
	    					AsyncHttpClient httpClient = new AsyncHttpClient(Constants.BASE_URL+Constants.SAVE_LOCATION);				
	    					httpClient.setBasicAuthToken(appUsr.getToken());
	    					httpClient.put(FRSClientMaintenance.this, null, null, new ResponseHandler(){

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
							
			
						} else {
							Bundle bundle2 = new Bundle();
							bundle2.putString(Constants.ERROR_D, getResources()
									.getString(R.string.NO_LAT_LON_FOUND));
							bundle2.putString(Constants.TYPE,
									Constants.GENARAL_ERROR_TYPE);
							getParent().showDialog(Constants.ERROR_DIALOG, bundle2);
						}
					} 
				}else {
						Bundle bundle2 = new Bundle();
						bundle2.putString(Constants.ERROR_D,
								getResources().getString(R.string.NO_LAT_LON_FOUND));
						bundle2.putString(Constants.TYPE, Constants.GENARAL_ERROR_TYPE);
						getParent().showDialog(Constants.ERROR_DIALOG, bundle2);
					}

					/*		Bundle bundle2 = new Bundle();
					bundle2.putString(Constants.ERROR_D, getResources()
							.getString(R.string.NO_LAT_LON_FOUND));
					bundle2.putString(Constants.TYPE,
							Constants.GENARAL_ERROR_TYPE);
					getParent().showDialog(Constants.ERROR_DIALOG, bundle2);*/
				}

			}
		});

		addActionlisteners();

	}

	private void addActionlisteners() {
		clientactivityList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub

				if (position == 0) {
					Bundle bundle = new Bundle();

					bundle.putString(Constants.CLIENT_NUMBER,
							aclient.getClientNumber());
					bundle.putString(Constants.CARD_TYPE, aclient.getCardtype());
					bundle.putString(Constants.CLIENT_NAME,
							aclient.getClientName());
					Intent intent;
					if (appUsr.isSingleView() == true) {
						intent = new Intent(FRSClientMaintenance.this,
								FRSAnswerActivityCard.class);
					} else {

						intent = new Intent(FRSClientMaintenance.this,
								FRSQuestions.class);
					}
					intent.putExtras(bundle);
					com.frontrow.ui.TabManager parentActivity = (TabManager) getParent();
					parentActivity.startChild("AnswerActivityCard_Activity",
							intent);
					// startActivity(intent);

				} else if (position == 3) {

					if (aclient.getClientId()!=0){
					Bundle bundle2 = new Bundle();
					bundle2.putString(Constants.PROGRESS_MSG,
							getResources().getString(R.string.RETRIEVE_MSG));

					getParent().showDialog(Constants.SHOW_DIALOG, bundle2);

					AsyncHttpClient httpClient = new AsyncHttpClient(
							Constants.BASE_URL+"customfields/"
									+ aclient.getClientId());
					httpClient.setBasicAuthToken(appUsr.getToken());
					httpClient.get(FRSClientMaintenance.this, null, null,
							new ResponseHandler() {
						@Override
						public void onSuccess(String successMsg) {
							// TODO Auto-generated method stub
							super.onSuccess(successMsg);
							CustomFieldResponse authRes = new CustomFieldResponse(
									successMsg, 200, "Success");
							refreshView(200, "", authRes);
						}

						@Override
						public void onFailed(String errorMsg,
								int errorCode) {
							// TODO Auto-generated method stub
							super.onFailed(errorMsg, errorCode);
						}

						@Override
						public void onFailure(Throwable error,
								String content) {
							// TODO Auto-generated method stub
							super.onFailure(error, content);
							CustomFieldResponse authRes = new CustomFieldResponse(
									content,
									400,
									getResources()
									.getString(
											R.string.NETWOK_UNREACHABLE));
							refreshView(
									400,
									getResources()
									.getString(
											R.string.NETWOK_UNREACHABLE),
											authRes);
						}

						@Override
						public void onFailure(int statusCode,
								String content) {
							// TODO Auto-generated method stub
							super.onFailure(statusCode, content);

							CustomFieldResponse authRes = new CustomFieldResponse(
									content,
									statusCode,
									getResources()
									.getString(
											R.string.NETWOK_UNREACHABLE));

							refreshView(
									statusCode,
									getResources()
									.getString(
											R.string.NETWOK_UNREACHABLE),
											authRes);
						}
					});
				}
					else{
					    Bundle bundle2 = new Bundle();
						bundle2.putString(Constants.ERROR_D, getResources().getString(R.string.CLIENT_PARTIALLY_CREATE_MSG));
						bundle2.putString(Constants.TYPE, Constants.GENARAL_ERROR_TYPE);				
						getParent().showDialog(Constants.ERROR_DIALOG,bundle2);
					}
				}

				else if (position == 2) {
					if (aclient.getClientId()!=0){
					Bundle bundle2 = new Bundle();
					bundle2.putString(Constants.PROGRESS_MSG,
							getResources().getString(R.string.RETRIEVE_MSG));

					getParent().showDialog(Constants.SHOW_DIALOG, bundle2);
                     UserDB udb = new UserDB(FRSClientMaintenance.this);
                      
					if(!udb.getUserContactStyle(appUsr.getMobileNnumber())){
						AsyncHttpClient httpClient = new AsyncHttpClient(
								Constants.BASE_URL+"contacts/"
										+ aclient.getClientId());
						httpClient.setBasicAuthToken(appUsr.getToken());
						httpClient.get(FRSClientMaintenance.this, null, null,
								new ResponseHandler() {
							@Override
							public void onSuccess(String successMsg) {
								// TODO Auto-generated method stub
								super.onSuccess(successMsg);
								ContactsResponse authRes = new ContactsResponse(
										successMsg, 200, "Success");
								refreshView(200, "", authRes);
							}

							@Override
							public void onFailed(String errorMsg,
									int errorCode) {
								// TODO Auto-generated method stub
								super.onFailed(errorMsg, errorCode);
							}

							@Override
							public void onFailure(Throwable error,
									String content) {
								// TODO Auto-generated method stub
								super.onFailure(error, content);
								ContactsResponse authRes = new ContactsResponse(
										content,
										400,
										getResources()
										.getString(
												R.string.NETWOK_UNREACHABLE));
								refreshView(
										400,
										getResources()
										.getString(
												R.string.NETWOK_UNREACHABLE),
												authRes);
							}

							@Override
							public void onFailure(int statusCode,
									String content) {
								// TODO Auto-generated method stub
								super.onFailure(statusCode, content);

								ContactsResponse authRes = new ContactsResponse(
										content,
										statusCode,
										getResources()
										.getString(
												R.string.NETWOK_UNREACHABLE));

								refreshView(
										statusCode,
										getResources()
										.getString(
												R.string.NETWOK_UNREACHABLE),
												authRes);
							}
						});
					}
					else{

						AsyncHttpClient httpClient = new AsyncHttpClient(
								Constants.BASE_URL+"compositecontacts/"
										+ aclient.getClientId());
						httpClient.setBasicAuthToken(appUsr.getToken());
						httpClient.get(FRSClientMaintenance.this, null, null,
								new ResponseHandler() {
							@Override
							public void onSuccess(String successMsg) {
								// TODO Auto-generated method stub
								super.onSuccess(successMsg);
								CompositeContactResponse authRes = new CompositeContactResponse(
										successMsg, 200, "Success");
								refreshView(authRes.getResponseCode(), authRes.getResponseMessage(), authRes);
							}

							@Override
							public void onFailed(String errorMsg,
									int errorCode) {
								// TODO Auto-generated method stub
								super.onFailed(errorMsg, errorCode);
							}

							@Override
							public void onFailure(Throwable error,
									String content) {
								// TODO Auto-generated method stub
								super.onFailure(error, content);
								CompositeContactResponse authRes = new CompositeContactResponse(
										content,
										400,
										getResources()
										.getString(
												R.string.NETWOK_UNREACHABLE));
								refreshView(
										400,
										getResources()
										.getString(
												R.string.NETWOK_UNREACHABLE),
												authRes);
							}

							@Override
							public void onFailure(int statusCode,
									String content) {
								// TODO Auto-generated method stub
								super.onFailure(statusCode, content);

								CompositeContactResponse authRes = new CompositeContactResponse(
										content,
										statusCode,
										getResources()
										.getString(
												R.string.NETWOK_UNREACHABLE));

								refreshView(
										statusCode,
										getResources()
										.getString(
												R.string.NETWOK_UNREACHABLE),
												authRes);
							}
						});


					}
				}
					else {
						   Bundle bundle2 = new Bundle();
							bundle2.putString(Constants.ERROR_D, getResources().getString(R.string.CLIENT_PARTIALLY_CREATE_MSG));
							bundle2.putString(Constants.TYPE, Constants.GENARAL_ERROR_TYPE);				
							getParent().showDialog(Constants.ERROR_DIALOG,bundle2);
						
					}
				
				}

				else if (position == 1) {
					if (aclient.getClientId()!=0){
					Bundle bundle2 = new Bundle();
					bundle2.putString(Constants.PROGRESS_MSG,
							getResources().getString(R.string.RETRIEVE_MSG));

					getParent().showDialog(Constants.SHOW_DIALOG, bundle2);

					AsyncHttpClient httpClient = new AsyncHttpClient(
							Constants.BASE_URL+"notes/"
									+ aclient.getClientId());
					httpClient.setBasicAuthToken(appUsr.getToken());
					httpClient.get(FRSClientMaintenance.this, null, null,
							new ResponseHandler() {
						@Override
						public void onSuccess(String successMsg) {
							// TODO Auto-generated method stub
							super.onSuccess(successMsg);
							ClientsNotesResponse authRes = new ClientsNotesResponse(
									successMsg, 200, "Success");
							refreshView(200, "", authRes);
						}

						@Override
						public void onFailed(String errorMsg,
								int errorCode) {
							// TODO Auto-generated method stub
							super.onFailed(errorMsg, errorCode);
						}

						@Override
						public void onFailure(Throwable error,
								String content) {
							// TODO Auto-generated method stub
							super.onFailure(error, content);
							ClientsNotesResponse authRes = new ClientsNotesResponse(
									content,
									400,
									getResources()
									.getString(
											R.string.NETWOK_UNREACHABLE));
							refreshView(
									400,
									getResources()
									.getString(
											R.string.NETWOK_UNREACHABLE),
											authRes);
						}

						@Override
						public void onFailure(int statusCode,
								String content) {
							// TODO Auto-generated method stub
							super.onFailure(statusCode, content);

							ClientsNotesResponse authRes = new ClientsNotesResponse(
									content,
									statusCode,
									getResources()
									.getString(
											R.string.NETWOK_UNREACHABLE));

							refreshView(
									statusCode,
									getResources()
									.getString(
											R.string.NETWOK_UNREACHABLE),
											authRes);
						}
					});
				}
					else{
						
						 Bundle bundle2 = new Bundle();
							bundle2.putString(Constants.ERROR_D, getResources().getString(R.string.CLIENT_PARTIALLY_CREATE_MSG));
							bundle2.putString(Constants.TYPE, Constants.GENARAL_ERROR_TYPE);				
							getParent().showDialog(Constants.ERROR_DIALOG,bundle2);
					}
				
				}
			}
		});

	}

	private void refreshView(int code, String msg, CustomFieldResponse authRes) {
		CustomFieldDataManager.getSharedInstance().setCustomFilelds(authRes);
		response = authRes;
		refreshViewInTheUIThread();
	}

	private void refreshView(int code, String msg, ClientsNotesResponse authRes) {
		ClientsNoteDataManager.getSharedInstance().setClientsNote(authRes);
		response = authRes;
		refreshViewInTheUIThread();
	}

	private void refreshView(int code, String msg, ContactsResponse authRes) {
		ContactsDataManager.getSharedInstance().setContactlist(authRes);
		response = authRes;
		refreshViewInTheUIThread();
	}

	private void refreshView(int code, String msg, CompositeContactResponse authRes) {
		//ContactsDataManager.getSharedInstance().setContactlist(authRes);
		response = authRes;
		refreshViewInTheUIThread();
	}

	@Override
	protected void refreshUI() {
		if (response instanceof CustomFieldResponse) {
			getParent().removeDialog(Constants.SHOW_DIALOG);
			// getInflatedProgress().setVisibility(View.GONE);
			int code = ((CustomFieldResponse) response).getResponseCode();
			if (code == Constants.NETWORK_SUCCESS) {

				/* Start Menu Activity */
				Bundle bundle = new Bundle();
				bundle.putString(Constants.CLIENT_NUMBER,
						aclient.getClientNumber());
				bundle.putString(Constants.CLIENT_NAME, aclient.getClientName());
				// bundle.putString(Constants.CARD_TYPE, aclient.getCardtype());
				// Intent intent = new Intent(FRSClientList.this,
				// FRSAnswerActivityCard.class);

				List<CustomField> custom_field_list = CustomFieldDataManager.getSharedInstance().getCustomFields();

				if(custom_field_list != null && custom_field_list.size() != 0){

					Intent intent;
					intent = new Intent(FRSClientMaintenance.this,
							FRSCustomField.class);
					intent.putExtras(bundle);
					com.frontrow.ui.TabManager parentActivity = (TabManager) getParent();
					parentActivity.startChild("CustomField_Activity", intent);
				}
				else{
					Bundle bundle2 = new Bundle();
					bundle2.putString(Constants.ERROR_D, getResources().getString(R.string.NO_CUSTOM_FIELD_MSG));
					bundle2.putString(Constants.TYPE, Constants.GENARAL_ERROR_TYPE);				
					getParent().showDialog(Constants.ERROR_DIALOG,bundle2);					 
				}			
			}
			else {
				Bundle bundle2 = new Bundle();
				bundle2.putString(Constants.ERROR_D,
						getResources().getString(R.string.NETWOK_UNREACHABLE));
				bundle2.putString(Constants.TYPE, Constants.GENARAL_ERROR_TYPE);
				getParent().showDialog(Constants.ERROR_DIALOG, bundle2);
			}

		}else if (response instanceof ClientsNotesResponse) {
			getParent().removeDialog(Constants.SHOW_DIALOG);
			// getInflatedProgress().setVisibility(View.GONE);
			int code = ((ClientsNotesResponse) response).getResponseCode();
			if (code == Constants.NETWORK_SUCCESS) {

				/* Start Menu Activity */
				Bundle bundle = new Bundle();
				bundle.putInt(Constants.CLIENT_ID, aclient.getClientId());
				bundle.putString(Constants.CLIENT_NAME, aclient.getClientName());
				// bundle.putString(Constants.CARD_TYPE, aclient.getCardtype());
				// Intent intent = new Intent(FRSClientList.this,
				// FRSAnswerActivityCard.class);
				Intent intent;
				intent = new Intent(FRSClientMaintenance.this,
						FRSClientsNotes.class);
				intent.putExtras(bundle);
				com.frontrow.ui.TabManager parentActivity = (TabManager) getParent();
				parentActivity.startChild("ClientsNote_Activity", intent);
			} else {

				Bundle bundle2 = new Bundle();
				bundle2.putString(Constants.ERROR_D,
						getResources().getString(R.string.NETWOK_UNREACHABLE));
				bundle2.putString(Constants.TYPE, Constants.GENARAL_ERROR_TYPE);
				getParent().showDialog(Constants.ERROR_DIALOG, bundle2);

			}

		}else if (response instanceof ContactsResponse) {
			getParent().removeDialog(Constants.SHOW_DIALOG);
			// getInflatedProgress().setVisibility(View.GONE);
			int code = ((ContactsResponse) response).getResponseCode();
			if (code == Constants.NETWORK_SUCCESS) {


				List<Contact> contact_list = ContactsDataManager.getSharedInstance()
						.getContactslist();

			//	if (contact_list != null && contact_list.size() != 0) {

					Bundle bundle = new Bundle();
					bundle.putInt(Constants.CLIENT_ID, aclient.getClientId());
					bundle.putString(Constants.CLIENT_NAME, aclient.getClientName());
					Intent intent;
					intent = new Intent(FRSClientMaintenance.this,
							ContactList.class);
					intent.putExtras(bundle);
					com.frontrow.ui.TabManager parentActivity = (TabManager) getParent();
					parentActivity.startChild("ContactList_Activity", intent);
			//	}

			/*	else{
					Bundle bundle2 = new Bundle();
					bundle2.putString(Constants.ERROR_D,
							getResources().getString(R.string.NO_CONTACT_INFO_MSG));
					bundle2.putString(Constants.TYPE, Constants.GENARAL_ERROR_TYPE);
					getParent().showDialog(Constants.ERROR_DIALOG, bundle2);


				}*/

				/* Start Menu Activity */

			} else {

				Bundle bundle2 = new Bundle();
				bundle2.putString(Constants.ERROR_D,
						getResources().getString(R.string.NETWOK_UNREACHABLE));
				bundle2.putString(Constants.TYPE, Constants.GENARAL_ERROR_TYPE);
				getParent().showDialog(Constants.ERROR_DIALOG, bundle2);

			}

		}else if(response instanceof CompositeContactResponse){
			getParent().removeDialog(Constants.SHOW_DIALOG);
			int code = ((CompositeContactResponse) response).getResponseCode();
			if(code == Constants.NETWORK_SUCCESS){
				CompositeDatamanager.getSharedInstance().setCompositeContactsList(((CompositeContactResponse) response).getCompositeContactslist());
				Bundle bundle = new Bundle();
				bundle.putInt(Constants.CLIENT_ID, aclient.getClientId());
				bundle.putString(Constants.CLIENT_NAME, aclient.getClientName());
				Intent intent;
				intent = new Intent(FRSClientMaintenance.this,
						ContactList.class);
				intent.putExtras(bundle);
				com.frontrow.ui.TabManager parentActivity = (TabManager) getParent();
				parentActivity.startChild("Composite_Activity", intent);
			
			}else{
				Bundle bundle2 = new Bundle();
				bundle2.putString(Constants.ERROR_D,
						((CompositeContactResponse) response).getResponseMessage());
				bundle2.putString(Constants.TYPE, Constants.GENARAL_ERROR_TYPE);
				getParent().showDialog(Constants.ERROR_DIALOG, bundle2);
			}
		}
		
		else if(response instanceof AddNewAddressRespone){
			getParent().removeDialog(Constants.SHOW_DIALOG);
			AddNewAddressRespone cRes = (AddNewAddressRespone) response;
			if(cRes.getResponseCode() == Constants.NETWORK_SUCCESS){
				
				ClientDB clientDb = new ClientDB(this);
				clientDb.updateClientsLocation(cRes.getAddress(), aclient.getClientId(), appUsr.getUserId());
				aclient.setAddress(cRes.getAddress());
					 //contact.modifyItemInAddressList(cRes.getAddress(),newAddress.getIsPrimary());
							
			}
			if (isDirect)
			{
					double lat = loc.getLat();
				double lon = loc.getLng();
				String fullPath = Constants.NATIVE_MAP_PATH
						+ Constants.SADDR
						+ Double.toString(appUsr.getLat()) + ","
						+ Double.toString(appUsr.getLon()) + "&"
						+ Constants.DADDR + Double.toString(lat) + ","
						+ Double.toString(lon);
				final Intent intent = new Intent(Intent.ACTION_VIEW,
						Uri.parse(fullPath));
				intent.setClassName("com.google.android.apps.maps",
						"com.google.android.maps.MapsActivity");
				startActivity(intent);
			}
			else
			{
			Bundle bundle3 = new Bundle();
			bundle3.putSerializable("LOCATION", loc);
			bundle3.putString("TYPE", "LOCATION");
			Intent subCatList = new Intent(
			FRSClientMaintenance.this, FRSMap.class);
	        subCatList.putExtras(bundle3);
	        com.frontrow.ui.TabManager parentActivity = (TabManager) getParent();
	        parentActivity.startChild("MAP_Activity",
			subCatList);
	        
			}
			

		}
	}

	@Override
	public void webResponseRecievedFromDataManager(Object response) {
		// TODO Auto-generated method stub
		this.response = response;
		if (response instanceof CustomFieldResponse) {
			refreshViewInTheUIThread();
		}

	}
}
