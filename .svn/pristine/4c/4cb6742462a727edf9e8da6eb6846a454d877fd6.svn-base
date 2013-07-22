package com.frontrow.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.content.Context;
import com.frontrow.common.SharedMethods;
import com.frontrow.connectionservice.ConnectionManagerBase;
import com.frontrow.connectionservice.FrontRowConnectionManager;
import com.frontrow.datamanagers.FrontDataManagerBase;
import com.frontrow.interfaces.IFrontResponseReceiver;
import com.frontrow.interfaces.ServerResponseReceiver;


public class DataManagerBase implements ServerResponseReceiver{
	protected byte  dataManagerID;
	protected ConnectionManagerBase connection;
	private List<IFrontResponseReceiver> recievers;	
	protected static Context context;

	public static void setContext(Context ctx){
		context = ctx;
	}

	public DataManagerBase(){
		dataManagerID = -1;
		recievers = Collections.synchronizedList(new ArrayList<IFrontResponseReceiver>());
	}

	public void sendDataRequest(Object request) {	
		FrontRowConnectionManager webConnection = new FrontRowConnectionManager();
		webConnection.sendRequest(request);
	}

	@Override
	public void priceResponseRecievedFromWeb(Object response) {
		// TODO Auto-generated method stub
		for (IFrontResponseReceiver reciever : this.recievers) {
			reciever.webResponseRecievedFromDataManager(response);
		}
	}


	public synchronized void registerForDineSafe(IFrontResponseReceiver reciever) {
		if (reciever != null && !recievers.contains(reciever)) {
			recievers.add(reciever);
		} else {
			SharedMethods.logError("Already registered for dinesafe");
		}
	}


	public synchronized void unregisterForDineSafe(IFrontResponseReceiver reciever) {
		if (reciever != null) {
			recievers.remove(reciever);
		}
	}

}
