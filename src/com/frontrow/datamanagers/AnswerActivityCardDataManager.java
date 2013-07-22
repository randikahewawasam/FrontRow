package com.frontrow.datamanagers;

import com.frontrow.android.db.ClientDB;
import com.frontrow.android.db.MessagesDB;
import com.frontrow.base.DataManagerBase;
import com.frontrow.common.Constants;
import com.frontrow.core.ApplicationUser;
import com.row.mix.beans.OfflineM;
import com.row.mix.request.CardSubmitRequest;
import com.row.mix.response.CardSubmitResponse;

public class AnswerActivityCardDataManager extends DataManagerBase{

	public static AnswerActivityCardDataManager instance;

	public static AnswerActivityCardDataManager getSharedInstance(){
		if(instance == null){
			instance = new AnswerActivityCardDataManager();
		}
		return instance;
	}

	public AnswerActivityCardDataManager() {
		// TODO Auto-generated constructor stub
	}

	public void submitActivityCard(String smileId,String questionVal,String clientNotes,String cardType,String selectedCardType,String clientName) {
		CardSubmitRequest cardRequest =  new CardSubmitRequest();		
		cardRequest.setAnswers(questionVal);
		cardRequest.setClientNotes(clientNotes);
		cardRequest.setClientNumber(smileId);
		cardRequest.setCardType(cardType);
		cardRequest.setSelectedCardType(selectedCardType);
		cardRequest.setUserName(ApplicationUser.getSharedInstance().getUserName());
		cardRequest.setCompanyId(ApplicationUser.getSharedInstance().getCompanyId());
		cardRequest.setToken(ApplicationUser.getSharedInstance().getToken());
		cardRequest.setMobileNumber(ApplicationUser.getSharedInstance().getMobileNnumber());
		cardRequest.setClientName(clientName);
		sendDataRequest(cardRequest);
	}

	@Override
	public void priceResponseRecievedFromWeb(Object response) {
		// TODO Auto-generated method stub
		CardSubmitResponse cSubmitResponse = (CardSubmitResponse) response;
		int responseCode = cSubmitResponse.getResponseCode();
		OfflineM offlineMsg = cSubmitResponse.getOffline();
		if(responseCode == Constants.NETWORK_NOT_AVAILABLE){
			MessagesDB msgDb = new MessagesDB(context);
			msgDb.addOfflineMessages(cSubmitResponse.getOffline(),ApplicationUser.getSharedInstance().getUserId());
		}else if(responseCode == Constants.NETWORK_SUCCESS){
			offlineMsg.setStatus(Constants.SUCCESS);
			new MessagesDB(context).addOfflineMessages(cSubmitResponse.getOffline(), ApplicationUser.getSharedInstance().getUserId());
		}else if(responseCode == Constants.NETWORK_PARSE_ERROR){
			offlineMsg.setStatus(Constants.FAILED);
			new MessagesDB(context).addOfflineMessages(cSubmitResponse.getOffline(),ApplicationUser.getSharedInstance().getUserId());
		}
		super.priceResponseRecievedFromWeb(response);
	}

}
