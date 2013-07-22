package com.frontrow.android.services;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import com.frontrow.android.db.FrontRowDBHelper;
import com.frontrow.android.db.MessagesDB;
import com.frontrow.common.Constants;
import com.frontrow.common.SharedMethods;
import com.frontrow.core.ApplicationUser;
import com.frontrow.datamanagers.FRSOfflineMessageDataManager;
import com.row.mix.beans.OfflineM;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

public class OfflineMessageService extends Service{
	private Timer timer = new Timer();
	private static final long updateScheduler = 120000;
	protected Handler uiUpdateHandler;
	private final IBinder frsBinder = new NetworkBinder();
	private boolean isOnline;
	private MessagesDB messges;

	/*Notify BaseActivity */

	private static final String TAG = "BroadcastService";
	public static final String BROADCAST_ACTION = "com.frontrow.base.displayevent";	
	Intent intent;


	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub		
		return frsBinder;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		
		super.onCreate();
		//android.os.Debug.waitForDebugger();
		uiUpdateHandler = new Handler();
		intent = new Intent(BROADCAST_ACTION);	
		sendUpdates();
	}

	@Override
	public ComponentName startService(Intent service) {
		// TODO Auto-generated method stub
		return super.startService(service);
	}

	private void sendUpdates() {
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub				
				isOnline = isNetworkAvailable();				
				ApplicationUser.getSharedInstance().setOnline(isOnline);

				/*Send notification to Base Activity */
				//new Date().toLocaleString()
				intent.putExtra(Constants.NETWORK_STATUS, isOnline);		    	
				sendBroadcast(intent);

				if(isOnline){					
					SharedMethods.logError("Network Available");
					messges =new MessagesDB(getApplicationContext());
					//messges.open();
					if(ApplicationUser.getSharedInstance().getUserId() != 0){
						LinkedList<OfflineM> msgList = messges.getAllMessagesForUser(ApplicationUser.getSharedInstance().getUserId(),Constants.PENDING);

						if(msgList != null && !msgList.isEmpty()){

							final Iterator<OfflineM> itr = msgList.iterator();
							do {
								final OfflineM msg = itr.next();
								messges.deleteList(ApplicationUser.getSharedInstance().getUserId(),msg.getId());
								uiUpdateHandler.postDelayed(new Runnable() {
									@Override
									public void run() {
										// TODO Auto-generated method stub	
										SharedMethods.logError("Sending updates.......");
										sendOfflineRequest(msg);
									}
								},5000);

							} while (itr.hasNext());
						}
					}
				}
			}
		}, 0, updateScheduler);
	}

	public boolean isNetworkAvailable() {
		boolean isAvailable = false;
		ConnectivityManager connectivityManager 
		= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
			isAvailable = true;
		}
		isOnline = isAvailable;

		return isAvailable;		
	}

	public synchronized void sendOfflineRequest(final OfflineM msg) {
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {				
				SharedMethods.logError("Service ");
				FRSOfflineMessageDataManager.getSharedInstance().sendOfflineMessages(msg);
			}
		});
		t.start();
	}


	protected class SendData extends AsyncTask<Object, String, String>{

		@Override
		protected String doInBackground(Object... params) {
			// TODO Auto-generated method stub
			OfflineM msg = (OfflineM) params[0];
			FRSOfflineMessageDataManager.getSharedInstance().sendOfflineMessages(msg);
			return null;
		}
	}	

	public class NetworkBinder extends Binder{
		public OfflineMessageService getService(){
			return OfflineMessageService.this;
		}
	}

	public boolean isOnline() {
		return isOnline;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(messges !=null)
			messges.close();
	}
}


