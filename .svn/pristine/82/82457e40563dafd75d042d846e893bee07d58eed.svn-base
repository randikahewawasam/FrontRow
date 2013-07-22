package com.frontrow.connectionservice;

import android.content.Context;
import com.frontrow.android.db.FrontRowDBHelper;
import com.frontrow.android.db.UserDB;
import com.frontrow.common.Constants;
import com.frontrow.common.SharedMethods;
import com.frontrow.datamanagers.AnswerActivityCardDataManager;
import com.frontrow.datamanagers.AuthDataManager;
import com.frontrow.datamanagers.ClientDataManager;
import com.frontrow.datamanagers.FRSOfflineMessageDataManager;
import com.frontrow.datamanagers.FRSSearchDataManager;
import com.frontrow.datamanagers.FRSTermsDataManager;
import com.frontrow.interfaces.IFrontResponseReceiver;
import com.frontrow.interfaces.ServerResponseReceiver;
import com.row.mix.beans.OfflineM;
import com.row.mix.beans.User;
import com.row.mix.core.RequestObject;
import com.row.mix.core.ResponseObject;
import com.row.mix.request.AuthenticationRequest;
import com.row.mix.request.CardSubmitRequest;
import com.row.mix.request.NewClientRequest;
import com.row.mix.request.OfflineRequest;
import com.row.mix.request.SearchRequest;
import com.row.mix.request.TermsRequest;
import com.row.mix.response.AuthenticationResponse;
import com.row.mix.response.CardSubmitResponse;
import com.row.mix.response.FRSTermResponse;
import com.row.mix.response.NewClientResponse;
import com.row.mix.response.OfflineResponse;
import com.row.mix.response.SearchResponse;


public class FrontRowConnectionManager extends FrontRowConnection{
	private Context ctx;

	public FrontRowConnectionManager() {
		// TODO Auto-generated constructor stub
		//baseURL = "http://mobile.solisit.net/MobileService.svc/";

		//baseURL ="http://beta3.frslogin.com/SalesProServiceTest/MobileService.svc/";
		//baseURL ="http://beta3.frslogin.com/SalesProServiceTestv1/MobileService.svc/api/v1/";
		//baseURL ="http://beta3.frslogin.com/SalesProMobileServiceTestv1.5/MobileService.svc/api/v1/";

		//baseURL ="http://beta3.frslogin.com/SalesProMobileServicev1.4/MobileService.svc/api/v1/";
		//http://beta3.frslogin.com/SalesProMobileServiceTestv1.4/MobileService.svc
	}
	@Override
	public void handleResponse(String strResponse, Object reqObj, int code,
			String msg,OfflineM m) {
		// TODO Auto-generated method stub
		ResponseObject responseObj = null;
		ServerResponseReceiver responseReceiver = null;
		if(reqObj instanceof AuthenticationRequest){
			responseObj = new AuthenticationResponse(strResponse, code, msg);			
			responseReceiver = AuthDataManager.getSharedInstance();
		}else if(reqObj instanceof CardSubmitRequest){
			responseObj = new CardSubmitResponse(strResponse, code, msg,m);
			responseReceiver = AnswerActivityCardDataManager.getSharedInstance();
		}else if(reqObj instanceof NewClientRequest){
			responseObj = new NewClientResponse(strResponse, code, msg,m);
			responseReceiver = ClientDataManager.getSharedInstance();
		}else if(reqObj instanceof OfflineRequest){
			responseObj =new OfflineResponse(strResponse, code, msg, m);
			responseReceiver = FRSOfflineMessageDataManager.getSharedInstance();
		}else if(reqObj instanceof TermsRequest){
			responseObj = new FRSTermResponse(strResponse, code, msg);
			responseReceiver = FRSTermsDataManager.getSharedInstance();
		}else if(reqObj instanceof SearchRequest){
			responseObj = new SearchResponse(strResponse, code, msg);
			responseReceiver = FRSSearchDataManager.getSharedInstance();
		}
		if(responseReceiver != null && responseObj != null ){
			responseReceiver.priceResponseRecievedFromWeb(responseObj);
		}
	}

	public synchronized void sendRequest(final Object request) {
		if(request instanceof RequestObject){
			String strMIX = ((RequestObject)request).buildMIXString();	

			final String finalUrl = Constants.BASE_URL + strMIX;
            
			//new Thread(this).start();

			Thread thread = new Thread(){

				public void run(){
					SharedMethods.logError("sendRequest ");
					sendHTTPRequest(finalUrl, request);
					//SendDataInBackground send  = new SendDataInBackground();					 
					//send.execute(finalUrl,request);
				}
			};
			thread.start();
		}
	}



}
