package com.frontrow.ui;

import com.frontrow.android.db.FrontRowDBHelper;
import com.frontrow.android.db.UserDB;
import com.frontrow.android.pref.DataSerializer;
import com.frontrow.android.services.OfflineMessageService;
import com.frontrow.base.DataManagerBase;
import com.frontrow.common.Constants;
import com.frontrow.language.LanguageManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class FrontRowSplash extends Activity implements Runnable{
	private static final int STOPSPLASH = 0;
	private static final long SPLASHTIME = 3000;
	private ImageView splash;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//java.lang.System.setProperty("http.keepAlive", "false");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.frontrowsplash);
		splash = (ImageView) findViewById(R.id.dinesafesplash);
		Message msg = new Message();
		msg.what = STOPSPLASH;
		spHandler.sendMessageDelayed(msg, SPLASHTIME);
		new Thread(this).start();
	}

	private Handler spHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case STOPSPLASH:
				splash.setVisibility(View.GONE);			
				// TODO: Optimize here
				startActivity(new Intent(FrontRowSplash.this, FrontRowLogin.class));
				finish();
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public void run() {
		// TODO Auto-generated method stub
		LanguageManager.getSharedInstance().setContext(this);
		DataSerializer.setContext(this);
		DataSerializer.initPref(Constants.LOAD_CREDENTIALS);
		FrontRowDBHelper.getSharedInstance(this);
		FrontRowDBHelper.setContext(this);		
		DataManagerBase.setContext(this);
	}

}