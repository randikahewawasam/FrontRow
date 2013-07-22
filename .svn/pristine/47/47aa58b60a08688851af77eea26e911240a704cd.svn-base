package com.frontrow.datamanagers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.frontrow.base.DataManagerBase;
import com.frontrow.common.SharedMethods;
import com.frontrow.connectionservice.ConnectionManagerBase;
import com.frontrow.interfaces.IFrontResponseReceiver;
import com.frontrow.interfaces.ServerResponseReceiver;
import com.row.mix.core.RequestObject;

public abstract class FrontDataManagerBase extends DataManagerBase implements ServerResponseReceiver{

	public static final byte DATA_MANAGER_AUTH = 1;
	private List<IFrontResponseReceiver> recievers;

	public FrontDataManagerBase() {
		// TODO Auto-generated constructor stub
		super();
		recievers = Collections.synchronizedList(new ArrayList<IFrontResponseReceiver>());
	}

	public synchronized void registerForPriceData(IFrontResponseReceiver reciever) {
		if (reciever != null && !recievers.contains(reciever)) {
			recievers.add(reciever);
		} else {
			SharedMethods.logError("Price Data ALREADY EXISTS");
		}
	}


	public synchronized void unregisterForPriceData(IFrontResponseReceiver reciever) {
		if (reciever != null) {
			recievers.remove(reciever);
		}
	}

	@Override
	public void priceResponseRecievedFromWeb(Object response) {
		// TODO Auto-generated method stub
		for (IFrontResponseReceiver reciever : this.recievers) {
			reciever.webResponseRecievedFromDataManager(response);
		}

	}

	public void sendDataRequest(Object request){
		RequestObject reqObj = null;
		ConnectionManagerBase connectionBase = null;
		if(request instanceof RequestObject){
			reqObj = (RequestObject) request;
			connectionBase = (ConnectionManagerBase)reqObj.getConnection();
		}
		if(connectionBase != null){
			connectionBase.sendRequest(request);
		}else{
			SharedMethods.logError("$$$$$$$$$$ check here..");
		}

	}

}
