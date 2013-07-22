package com.frontrow.datamanagers;

import java.util.ArrayList;

import com.frontrow.android.db.ClientDB;
import com.frontrow.android.db.MessagesDB;
import com.frontrow.android.json.JSONException;
import com.frontrow.android.json.JSONObject;
import com.frontrow.base.DataManagerBase;
import com.frontrow.common.Constants;
import com.frontrow.common.SharedMethods;
import com.frontrow.core.ApplicationUser;
import com.row.mix.beans.Client;
import com.row.mix.beans.OfflineM;
import com.row.mix.request.OfflineRequest;
import com.row.mix.response.NewClientResponse;
import com.row.mix.response.OfflineResponse;

public class FRSOfflineMessageDataManager extends DataManagerBase{

	public static FRSOfflineMessageDataManager instance;
	private ArrayList<Client> cList;
	private Client client;

	public static FRSOfflineMessageDataManager getSharedInstance(){
		if(instance == null){
			instance = new FRSOfflineMessageDataManager();
		}
		return instance;
	}

	public void sendOfflineMessages(OfflineM msg) {
		OfflineRequest offRequest = new OfflineRequest();
		offRequest.setMsg(msg.getMessage());
		offRequest.setSubmitType(msg.getSubmitType());
		offRequest.setType(msg.getType());
		offRequest.setToken(ApplicationUser.getSharedInstance().getToken());
		offRequest.setClientName(msg.getClientName());
		this.sendDataRequest(offRequest);
	}

	@Override
	public void priceResponseRecievedFromWeb(Object response) {
		// TODO Auto-generated method stub
		MessagesDB msgDb =new MessagesDB(context);
		OfflineResponse offResponse = (OfflineResponse) response;
		OfflineM msg = offResponse.getOffline();
		if(offResponse.getResponseCode()== Constants.NETWORK_NOT_AVAILABLE){
			SharedMethods.logError("No Network again.....");			
		}else if(offResponse.getResponseCode() == Constants.NETWORK_SUCCESS){			
			msg.setStatus(Constants.SUCCESS);		
			if(msg.submitType.equals(Constants.CLIENT_TYPE)){
				cList = offResponse.getClientList();
				client= offResponse.getClient();
				saveClientDetails();
			}
		}else {
			msg.setStatus(Constants.FAILED);
			
			if(msg.submitType.equals(Constants.CLIENT_TYPE)){
				String clientnumber="";
				JSONObject json =null ; 
				Client newclient=new Client();
				try {
					json= new JSONObject(msg.getMessage());
					clientnumber = json.getString("ClientNumber");
					newclient.setClientNumber(clientnumber);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				}
				deleteClientDetails(newclient);
			}
		}
		msgDb.addOfflineMessages(msg,ApplicationUser.getSharedInstance().getUserId());
		super.priceResponseRecievedFromWeb(response);
	}

	private void saveClientDetails() {
		ClientDB clientDb =new ClientDB(context);
		//clientDb.open();
		//clientDb.addClients(cList,ApplicationUser.getSharedInstance().getUserId());
		clientDb.updateClient(client,ApplicationUser.getSharedInstance().getUserId());
		
	}

	private void deleteClientDetails(Client nclient) {
		ClientDB clientDb =new ClientDB(context);
		//clientDb.open();
		//clientDb.addClients(cList,ApplicationUser.getSharedInstance().getUserId());
		clientDb.deleteClient(nclient,ApplicationUser.getSharedInstance().getUserId());
		
	}

	public ArrayList<Client> getcList() {
		return cList;
	}
}
