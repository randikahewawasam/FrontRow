package com.frontrow.ui;

import java.util.ArrayList;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.LinearGradient;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.LayoutInflater.Filter;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.frontrow.adapters.FrontRowClientAdapter;
import com.frontrow.android.db.ActivityCardDB;
import com.frontrow.android.db.ClientDB;
import com.frontrow.base.BaseActivity;
import com.frontrow.common.Constants;
import com.frontrow.common.SharedMethods;
import com.frontrow.core.ApplicationUser;
import com.frontrow.datamanagers.AuthDataManager;
import com.frontrow.datamanagers.ClientDataManager;
import com.frontrow.datamanagers.CustomFieldDataManager;
import com.frontrow.datamanagers.FRSSearchDataManager;
import com.frontrow.interfaces.FRSAddNewClientListener;
import com.frontrow.interfaces.IFrontResponseReceiver;
import com.memobile.connection.AsyncHttpClient;
import com.memobile.connection.ResponseHandler;
import com.row.mix.beans.ActivityCard;
import com.row.mix.beans.Client;
import com.row.mix.response.CustomFieldResponse;
import com.row.mix.response.NewClientResponse;
import com.row.mix.response.SearchResponse;

public class FRSClientList extends BaseActivity implements IFrontResponseReceiver,OnItemSelectedListener,FRSAddNewClientListener{
	private ArrayList<Client> clientList;
	private FrontRowClientAdapter cAdapter;
	private ListView clientRsltList;
	private EditText clientHint;
	//private ApplicationUser appUsr;
	private TextView cardLblName;
	private Spinner cardList;
	private ArrayList<ActivityCard> activityCardList;
	private ArrayList<CardSpinner> aCrdList;
	private String activityCardType;
	private ClientDB clientDB;
	private TextView notification;
	/*	private ArrayAdapter<CharSequence> searchType;
	private Spinner filtervalues;
	private LinearLayout searchLayout;*/
	private Button getMore;
	private String searchTerm;

	/* to find out search more results  */

	private boolean isSearchMore =false;

	/* Web Response  */
	Object response;

	@Override
	protected void initializeUI() {
		init();

		titlename.setText("");
		// TODO Auto-generated method stub
		aCrdList = new ArrayList<CardSpinner>();
		//appUsr = ApplicationUser.getSharedInstance();
		clientRsltList = (ListView) findViewById(R.id.cList);
		clientRsltList.setTextFilterEnabled(true);
		clientHint = (EditText) findViewById(R.id.clientHInt);
		clientHint.addTextChangedListener(filterTextWatcher);
		notification = (TextView) findViewById(R.id.notification);
		/*		filtervalues = (Spinner) findViewById(R.id.searchTypes);
		searchLayout = (LinearLayout) findViewById(R.id.searchTypeLayout);
		searchType = ArrayAdapter.createFromResource(this, R.array.FILTER_TYPES, R.layout.spinnertext);
		searchType.setDropDownViewResource(R.layout.spinnerdropdown);
		filtervalues.setAdapter(searchType);
		filtervalues.setOnItemSelectedListener(this);*/

		cardLblName = (TextView) findViewById(R.id.clientViewText);
		cardList = (Spinner) findViewById(R.id.clientViewSelectCards);

		ActivityCardDB cardDb= new ActivityCardDB(this);
		SharedMethods.logError("Client List :"+cardDb + "This : "+ this);
		ActivityCard crd = new ActivityCard();
		crd.setCardId(-1);
		crd.setCardType(Constants.ALL_ACTIVITY_CARD);
		activityCardList = cardDb.getAllActivityCards();
		activityCardList.add(0,crd);
		setSpinnerValues();

		/* Footer View */
		//if(appUsr.isAllClientsEnable()){
		LayoutInflater inflater = this.getLayoutInflater();
		LinearLayout listFooterView = (LinearLayout)inflater.inflate(
				R.layout.frsclientfooterview, null);

		clientRsltList.addFooterView(listFooterView);

		getMore = (Button) listFooterView.findViewById(R.id.getMore);

		//}

		/* Retrieving data from Current Instance */

		//clientList = AuthDataManager.getSharedInstance().getClientList();
		clientDB =new ClientDB(this);
		cardList.setOnItemSelectedListener(this);
		//clientDB.open();
		/*clientList = clientDB.getAllClinets(appUsr.getUserId());
		if(clientList != null){
			cAdapter = new FrontRowClientAdapter(this, R.layout.clientdesclistrow, clientList);
			clientRsltList.setAdapter(cAdapter);
			clientRsltList.setTextFilterEnabled(true);
		}	*/

		addActionlisteners();
	}
	@Override
	protected void refreshUI() {
		titlename.setText("");
		// TODO Auto-generated method stub
		getParent().removeDialog(Constants.SEARCH_MORE);
		if(response instanceof SearchResponse){
			notification.setVisibility(View.GONE);
			//clientHint.getText().clear();
			clientHint.addTextChangedListener(filterTextWatcher);
			SharedMethods.logError("New Cliet List received");
			ArrayList<Client> temp  =((SearchResponse) response).getClientList();
			SharedMethods.logError("New Cliet List received" + temp.size());
			if(temp != null ){
				//cAdapter.setClientList(temp);
				//cAdapter.notifyDataSetChanged();

				cAdapter = new FrontRowClientAdapter(this, R.layout.clientdesclistrow, temp);
				clientRsltList.setAdapter(cAdapter);
				if(temp.isEmpty())
					notification.setVisibility(View.VISIBLE);
			}	
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setAddClientListener(this);
		titlename.setText("");
		clientHint.addTextChangedListener(filterTextWatcher);
		FRSSearchDataManager.getSharedInstance().registerForDineSafe(this);
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		clientHint.removeTextChangedListener(filterTextWatcher);
		FRSSearchDataManager.getSharedInstance().unregisterForDineSafe(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View viewToLoad = LayoutInflater.from(this.getParent()).inflate(R.layout.clientlist, null);
		setContentView(viewToLoad);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void webResponseRecievedFromDataManager(Object response) {
		// TODO Auto-generated method stub
		if(response != null){
			this.response = response;
			refreshViewInTheUIThread();
		}
	}


	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void openlocationsettings() {
		// TODO Auto-generated method stub
		super.openlocationsettings();
	}


	private void addActionlisteners() {
		clientRsltList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
				//parent.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.ListSelectorColor));
				Client client = (Client) clientRsltList.getItemAtPosition(position);
				bundle.putString(Constants.CLIENT_NUMBER, client.getClientNumber());
				bundle.putString(Constants.CARD_TYPE, client.getCardtype());
				bundle.putBoolean(Constants.IS_SHOW_MORE, isSearchMore);
				//bundle.putString(Constants.CLIENT_NAME, client.getClientName());
				//Intent intent = new Intent(FRSClientList.this, FRSAnswerActivityCard.class);
				Intent intent;

				intent = new Intent(FRSClientList.this, FRSClientMaintenance.class);

				intent.putExtras(bundle);
				com.frontrow.ui.TabManager parentActivity = (TabManager) getParent();
				parentActivity .startChild("ClientMaintanace_Activity", intent);
				//startActivity(intent);
			}
		});

		/*		filtervalues.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				//clientHint
				String value = searchType.getItem(position).toString();
				clientHint.setHint(value);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});*/

		//if(appUsr.isAllClientsEnable()){


		getMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				isSearchMore = true;

				if(searchTerm != null && searchTerm.trim().length() != 0){
					getParent().showDialog(Constants.SEARCH_MORE);
					notification.setVisibility(View.GONE);
					SharedMethods.logError("Search term 1:"+ searchTerm);
					//FRSSearchDataManager.getSharedInstance().searchClientDetails(searchTerm);

					AsyncHttpClient httpClient = new AsyncHttpClient(
							Constants.BASE_URL+"clients/"
									+ searchTerm);
					httpClient.setBasicAuthToken(appUsr.getToken());
					httpClient.get(FRSClientList.this, null, null,
							new ResponseHandler() {
						@Override
						public void onSuccess(String successMsg) {
							// TODO Auto-generated method stub
							super.onSuccess(successMsg);
							SearchResponse authRes = new SearchResponse(
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
							SearchResponse authRes = new SearchResponse(
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

							SearchResponse authRes = new SearchResponse(
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

				}else{
					showNotification();
				}
			}
		});
		//}
	}

	private void refreshView(int code, String msg, SearchResponse authRes) {
		//CustomFieldDataManager.getSharedInstance().setCustomFilelds(authRes);
		ClientDataManager.getSharedInstance().setClientList(authRes);
		response = authRes;
		refreshViewInTheUIThread();
	}

	private void showNotification() {
		Toast toast = Toast.makeText(this, getResources().getString(R.string.SEARCH_TOAST), 10000);
		toast.show();
	}



	private TextWatcher filterTextWatcher = new TextWatcher() {

		public void afterTextChanged(Editable s) {
			if (s.length()==0){
				isSearchMore = false;
				clientDB =new ClientDB(FRSClientList.this);
				clientList = clientDB.getAllClinets(appUsr.getUserId());
				if(clientList != null ){
					cAdapter = new FrontRowClientAdapter(FRSClientList.this, R.layout.clientdesclistrow, clientList);
					//cAdapter.setClientList(clientList);
					clientRsltList.setAdapter(cAdapter);
					clientRsltList.setTextFilterEnabled(true);
					cAdapter.notifyDataSetChanged();
				}


			}
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			SharedMethods.logError("SSSSSSSSSSSSS " + s + Integer.toString(count));
			searchTerm = s.toString();
			if(cAdapter != null){
				//if(cAdapter != null && !cAdapter.isEmpty()){
				cAdapter.getFilter().filter(s); //Filter from my adapter
				cAdapter.notifyDataSetChanged();
			}
			//}
			/*if(cAdapter.isEmpty()){
				SharedMethods.logError("Empty Adapter");
			}*/
		}
	};

	private void setSpinnerValues() {
		if(activityCardList != null){
			for(ActivityCard card : this.activityCardList){
				aCrdList.add(new CardSpinner(card.getCardType(), card.getCardId()));
			}
			ArrayAdapter<CardSpinner> cardAdapter = new ArrayAdapter<CardSpinner>(this, R.layout.spinnertext, aCrdList);
			cardAdapter.setDropDownViewResource(R.layout.spinnerdropdown);
			cardList.setAdapter(cardAdapter);
			cardList.setSelected(true);
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		// TODO Auto-generated method stub
		if(arg0 == cardList){
			SharedMethods.logError("Adapter Null");
			notification.setVisibility(View.GONE);
			activityCardType = arg0.getItemAtPosition(position).toString();
			CardSpinner cs = aCrdList.get(position);
			activityCardType = cs.getCardName();
			clientList = clientDB.getClientsForActivityCard(activityCardType,appUsr.getUserId());
			if(clientList != null && !clientList.isEmpty()){
				cAdapter = new FrontRowClientAdapter(this, R.layout.clientdesclistrow, clientList);
				clientRsltList.setAdapter(cAdapter);
				//clientRsltList.setTextFilterEnabled(true);
			}else{
				if(cAdapter != null){
					SharedMethods.logError("Adapter not Null");
					cAdapter.clear();
					cAdapter.notifyDataSetChanged();
				}

			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addClient(View v) {
		// TODO Auto-generated method stub

		Intent intent;	
		intent = new Intent(FRSClientList.this, FRSNewClient.class);		
		com.frontrow.ui.TabManager parentActivity = (TabManager) getParent();
		parentActivity .startChild("AddNewClient_Activity", intent);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return getParent().onKeyDown(keyCode, event);
	}


}
