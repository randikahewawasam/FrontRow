package com.frontrow.ui;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.frontrow.android.db.ActivityCardDB;
import com.frontrow.android.db.ClientAccountTypeDB;
import com.frontrow.android.db.ClientDB;
import com.frontrow.android.db.FRSTermsDB;
import com.frontrow.android.db.MessagesDB;
import com.frontrow.android.json.JSONException;
import com.frontrow.android.json.JSONObject;
import com.frontrow.base.BaseActivity;
import com.frontrow.common.Constants;
import com.frontrow.common.SharedMethods;
import com.frontrow.core.ApplicationUser;
import com.frontrow.datamanagers.AuthDataManager;
import com.frontrow.datamanagers.ClientDataManager;
import com.frontrow.interfaces.IFrontResponseReceiver;
import com.memobile.connection.AsyncHttpClient;
import com.memobile.connection.ResponseHandler;
import com.row.mix.beans.ActivityCard;
import com.row.mix.beans.Client;
import com.row.mix.beans.ClientAccountType;
import com.row.mix.beans.OfflineM;
import com.row.mix.common.MIXKeys;
import com.row.mix.response.AddNewClientsNoteResponse;
import com.row.mix.response.AuthenticationResponse;
import com.row.mix.response.CardSubmitResponse;
import com.row.mix.response.NewClientResponse;

public class FRSNewClient extends BaseActivity implements IFrontResponseReceiver,OnItemSelectedListener{
	private EditText clientName;
	private EditText clientNumber;
	private Button create;
	private Spinner cardSpinner;
	private Spinner clientTypeSpinner;
	private int cardId;
	private int clientTypeId;
	private ArrayList<ActivityCard> cardTypeList;
	private ArrayList<CardSpinner> cardList;
	private ArrayList<ClientAccountType> clientAccountTypeList;
	private ArrayList<ClientTypeSpinner> ctypeList;

	/*errors */
	private ViewStub stubErrorMsg;
	private View inflatedErrorMsg;
	private ViewStub stubProgress;	
	private View inflatedProgress;	
	private TextView txtErrorMsg;
	
	private Client newclient;

	private TextView newClient,clientNumberLbl;

	/* Web Response  */
	Object response;
	private OfflineM offMsg;

	@Override
	protected void initializeUI() {
		titlename.setText("");
		// TODO Auto-generated method stub
		cardList = new ArrayList<CardSpinner>();
		ctypeList =new ArrayList<ClientTypeSpinner>();
		clientName = (EditText) findViewById(R.id.cname);
		clientNumber = (EditText) findViewById(R.id.cnumber);
		create = (Button) findViewById(R.id.createClient);
		cardSpinner = (Spinner) findViewById(R.id.cards);
		newClient = (TextView) findViewById(R.id.newClient);
		clientNumberLbl = (TextView) findViewById(R.id.clientNumber);
		clientTypeSpinner =(Spinner) findViewById(R.id.clienttypes);

		if(appUsr.getAllTerms() == null){
			FRSTermsDB terms = new FRSTermsDB(this);
			HashMap<String, String> allTerms = terms.getAllTermsForCompanyID(appUsr.getCompanyId());
			if(allTerms != null){
				this.appUsr.setAllTerms(allTerms);
			}
		}
		String clentText = (appUsr == null)?getResources().getString(R.string.CLIENT_NAME):appUsr.getAllTerms().get(Constants.CLIENT_KEY)+" "+getResources().getString(R.string.NAME);
		String clientNo =  (appUsr == null)?getResources().getString(R.string.CLIENT_NAME):appUsr.getAllTerms().get(Constants.CLIENT_KEY)+" "+getResources().getString(R.string.NUMBER);
		newClient.setText(clentText);
		clientNumberLbl.setText(clientNo);
		ActivityCardDB cardDB = new ActivityCardDB(this);
		//cardDB.open();
		cardTypeList = cardDB.getAllActivityCards();
		
		ClientAccountTypeDB clientTypeDB = new ClientAccountTypeDB(this);
		clientAccountTypeList = clientTypeDB.getAllClientAccountTypes();

		//cardTypeList = AuthDataManager.getSharedInstance().getCardTypes();

		for(ActivityCard card : this.cardTypeList){
			cardList.add(new CardSpinner(card.getCardType(), card.getCardId()));
		}
		ArrayAdapter<CardSpinner> cAdapter = new ArrayAdapter<CardSpinner>(this, R.layout.spinnertext, cardList);
		cAdapter.setDropDownViewResource(R.layout.spinnerdropdown);
		cardSpinner.setAdapter(cAdapter);

		cardSpinner.setOnItemSelectedListener(this);

		for(ClientAccountType type : this.clientAccountTypeList){
			ctypeList.add(new ClientTypeSpinner(type.getType(), type.getId()));
		}
		ArrayAdapter<ClientTypeSpinner> cTypeAdapter = new ArrayAdapter<ClientTypeSpinner>(this, R.layout.spinnertext, ctypeList);
		cTypeAdapter.setDropDownViewResource(R.layout.spinnerdropdown);
		clientTypeSpinner.setAdapter(cTypeAdapter);
		
		clientTypeSpinner.setOnItemSelectedListener(this);

		if (stubErrorMsg == null) {
			stubErrorMsg = (ViewStub) findViewById(R.id.loginStubErrorMsg);
			inflatedErrorMsg = stubErrorMsg.inflate();
			txtErrorMsg = (TextView) inflatedErrorMsg.findViewById(R.id.loginTVError);
		}

		if (stubProgress == null) {
			stubProgress = (ViewStub) findViewById(R.id.loginStubProgress);
			inflatedProgress = stubProgress.inflate();
			inflatedProgress.setVisibility(View.GONE);
		}

		addActionListeners();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ClientDataManager.getSharedInstance().registerForDineSafe(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		ClientDataManager.getSharedInstance().unregisterForDineSafe(this);
	}

	@Override
	protected void refreshUI() {
		// TODO Auto-generated method stub
		/*		if(response instanceof NewClientResponse){
			int code =	((NewClientResponse) response).getResponseCode();
			if(code ==Constants.NETWORK_SUCCESS ){
				Toast toast = Toast.makeText(this, getResources().getString(R.string.CLIENT_ADD_SUCCESS), 5000);
				toast.show();
				getTxtErrorMsg().setText("");
				clientName.setText("");
				clientNumber.setText("");
				String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
				SharedMethods.saveFromFile("sucessfully Created New Client - "+ currentDateTimeString,false,this);
			}else{
				getTxtErrorMsg().setText(((NewClientResponse) response).getResponseMessage());
			}
			getInflatedProgress().setVisibility(View.GONE);
		}
		 */

		if (response instanceof NewClientResponse) {
			getInflatedProgress().setVisibility(View.GONE);
			ClientDB clientDb = new ClientDB(this);
			int code = ((NewClientResponse) response).getResponseCode();
			if (code == Constants.NETWORK_SUCCESS) {

				
				//clientDb.open();
				clientDb.addClient(((NewClientResponse) response).getClient(), appUsr.getUserId());
				//clientDb.updateClient(((NewClientResponse) response).getClient(),ApplicationUser.getSharedInstance().getUserId());


				Toast toast = Toast.makeText(getParent(), getResources().getString(R.string.CLIENT_ADD_SUCCESS), 5000);
				toast.show();
				getTxtErrorMsg().setText("");
				clientName.setText("");
				clientNumber.setText("");
				String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
				SharedMethods.saveFromFile("sucessfully Created New Client - "+ currentDateTimeString,false,this);

				offMsg.setStatus(Constants.SUCCESS);
				new MessagesDB(this).addOfflineMessages(offMsg, appUsr.getUserId());
			} else if (code==Constants.CONNECT_TO_SERVER_ERROR) {
				Toast toast = Toast.makeText(getParent(), getResources().getString(R.string.CLIENT_ADD_SUCCESS), 5000);
				toast.show();
				getTxtErrorMsg().setText("");
				clientName.setText("");
				clientNumber.setText("");
				offMsg.setStatus(Constants.PENDING);
				new MessagesDB(this).addOfflineMessages(offMsg, appUsr.getUserId());
				clientDb.addClient(newclient, appUsr.getUserId());
			}
			else {
				offMsg.setStatus(Constants.FAILED);
				new MessagesDB(this).addOfflineMessages(offMsg, appUsr.getUserId());
				Toast toast = Toast.makeText(getParent(),((NewClientResponse) response).getResponseMessage(), 5000);
				toast.show();
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//android.os.Debug.waitForDebugger(); 
		View newClientView = LayoutInflater.from(this.getParent()).inflate(R.layout.newclient, null);
		setContentView(newClientView);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		// TODO Auto-generated method stub
		if(arg0 == cardSpinner){
			CardSpinner cs = cardList.get(position);
			cardId = cs.getCardId();
		}
		else if (arg0 ==clientTypeSpinner){
			ClientTypeSpinner ct=ctypeList.get(position);
			clientTypeId=ct.clientTypeId;
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

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
	public void webResponseRecievedFromDataManager(Object response) {
		// TODO Auto-generated method stub
		if(response != null){
			this.response = response;
			refreshViewInTheUIThread();
		}
	}

	private void addActionListeners() {
		create.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String name = clientName.getText().toString();
				String number = clientNumber.getText().toString();
				if((name != null && name.trim().length() > 0) && (number != null && number.trim().length() > 0)){

					JSONObject clientObj = new JSONObject();
					try {
						//String message = clientNumber+MIXKeys.MSG_CONSTANTS+selectedCardType+"."+answers+clientNotes+MIXKeys.MSG_APPEND; 
						clientObj.put(MIXKeys.CLIENT_NAME, name);
						clientObj.put(MIXKeys.CARD_ID, cardId);
						clientObj.put(MIXKeys.MOBILE_NO, appUsr.getMobileNnumber());
						clientObj.put(MIXKeys.COMPANY_CODE, appUsr.getCompanyId());
						clientObj.put(MIXKeys.CLIENT_NUMBER_FOR_ADD_NEWCLIENT,number);
						//SharedMethods.l
						clientObj.put(MIXKeys.CLIENT_TYPE_ID, clientTypeId);
						newclient = new Client();
						newclient.setClientName(name);
						newclient.setCardId(cardId);
						newclient.setClientNumber(number);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                       ClientDB cd=new ClientDB(FRSNewClient.this);
					String jstring= clientObj.toString();
					
					if(!cd.isClientAlreadyExist(number, appUsr.getUserId()))			
					{
					getInflatedProgress().setVisibility(View.VISIBLE);
					getTxtErrorMsg().setText("");
					//ClientDataManager.getSharedInstance().addNewClientrequest(name, number, cardId);
					AsyncHttpClient httpClient = new AsyncHttpClient(
							Constants.BASE_URL+"clients/client");
					httpClient.setBasicAuthToken(appUsr.getToken());

					offMsg = new OfflineM();
					offMsg.setMessage(jstring);
					offMsg.setDateMillies(java.lang.System.currentTimeMillis());
					offMsg.setType(Constants.NEW_CLIENT);
					offMsg.setSubmitType(Constants.CLIENT_TYPE);
					offMsg.setClientName(name);



					// httpClient.setMethod("PUT");
					httpClient.post(FRSNewClient.this, null, null,
							new ResponseHandler() {
						@Override
						public void onSuccess(String successMsg) {
							// TODO Auto-generated method stub
							super.onSuccess(successMsg);
							NewClientResponse authRes = new NewClientResponse(
									successMsg, 200, "Success",offMsg);
							refreshView(authRes.getResponseCode(), authRes.getResponseMessage(), authRes);
						}

						@Override
						public void onFailed(String errorMsg, int errorCode) {
							// TODO Auto-generated method stub
							super.onFailed(errorMsg, errorCode);
						}

						@Override
						public void onFailure(Throwable error, String content) {
							// TODO Auto-generated method stub
							super.onFailure(error, content);
							NewClientResponse authRes = new NewClientResponse(
									content, 400, getResources().getString(
											R.string.NETWOK_UNREACHABLE),offMsg);

							refreshView(
									400,
									getResources().getString(
											R.string.NETWOK_UNREACHABLE), authRes);
						}

						@Override
						public void onFailure(int statusCode, String content) {
							// TODO Auto-generated method stub
							super.onFailure(statusCode, content);
							NewClientResponse authRes = new NewClientResponse(content, statusCode, getResources().getString(R.string.NETWOK_UNREACHABLE),offMsg);
							refreshView(statusCode, getResources().getString(R.string.NETWOK_UNREACHABLE),authRes);
						}
					}, jstring);
				}else{
					Bundle bundle2 = new Bundle();
					bundle2.putString(Constants.ERROR_D, getResources()
							.getString(R.string.CLIENT_ALREADY_EXIST));
					bundle2.putString(Constants.TYPE,
							Constants.GENARAL_ERROR_TYPE);
					getParent().showDialog(Constants.ERROR_DIALOG, bundle2);
					
				}
				}else{
					getTxtErrorMsg().setText(getResources().getString(R.string.LOGIN_DETAILS_EMPTY));
				}

			}
		});
	}

	private void refreshView(int code, String msg,
			NewClientResponse authRes) {
		// ClientsNoteDataManager.getSharedInstance().setClientsNote((authRes));

		response = authRes;
	
		
		refreshViewInTheUIThread();
	}

	public View getInflatedProgress() {
		return inflatedProgress;
	}

	public TextView getTxtErrorMsg() {
		return txtErrorMsg;
	}

}

class CardSpinner{
	String cardName ="";
	int cardId;

	public CardSpinner(String cardname,int cardId) {
		// TODO Auto-generated constructor stub
		this.cardName = cardname;
		this.cardId = cardId;
	}
	public String getCardName() {
		return cardName;
	}
	public int getCardId() {
		return cardId;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return cardName;
	}
}

class ClientTypeSpinner{
	String typeName ="";
	int clientTypeId;

	public ClientTypeSpinner(String typeName,int clientTypeId) {
		// TODO Auto-generated constructor stub
		this.typeName = typeName;
		this.clientTypeId = clientTypeId;
	}
	public String getTypeName() {
		return typeName;
	}
	public int getClientTypeId() {
		return clientTypeId;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return typeName;
	}
}