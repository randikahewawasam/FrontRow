package com.frontrow.datamanagers;

import com.frontrow.base.DataManagerBase;
import com.frontrow.core.ApplicationUser;
import com.row.mix.beans.User;
import com.row.mix.request.TermsRequest;
import com.row.mix.response.FRSTermResponse;

public class FRSTermsDataManager extends DataManagerBase{

	private static FRSTermsDataManager instance;

	public static FRSTermsDataManager getSharedInstance(){
		if(instance == null){
			instance = new FRSTermsDataManager();
		}
		return instance;
	}

	public void requestTerms(User user) {
		if(user !=null){
			TermsRequest tRequest = new TermsRequest();
			tRequest.setCompanyId(user.getCompanyId());
			tRequest.setToken(user.getToken());
			tRequest.setMobileNo(user.getMobileNumber());		
			sendDataRequest(tRequest);
		}
	}

	@Override
	public void priceResponseRecievedFromWeb(Object response) {
		// TODO Auto-generated method stub
		super.priceResponseRecievedFromWeb(response);
	}
}
