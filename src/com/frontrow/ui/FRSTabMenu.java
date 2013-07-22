package com.frontrow.ui;

import com.frontrow.common.Constants;
import com.frontrow.common.SharedMethods;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

public class FRSTabMenu extends TabActivity{

	private TabHost tabHost;
	private static int TOTAL_TABS = 0;
	private TabHost.TabSpec tabMessage;
	public static FRSTabMenu myTabLayoutDemo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		SharedMethods.logMessage(this.getClass(), "onCreate(): start activity");		
		super.onCreate(savedInstanceState);		

		setContentView(R.layout.tabmenu);
		FRSTabMenu.myTabLayoutDemo = this;
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		//tabHost.getTabWidget().setBackgroundDrawable(getResources().getDrawable(R.drawable.footer_bg));

		tabMessage = tabHost.newTabSpec(getResources().getString(R.string.MESSAGES));
		TOTAL_TABS++;

		TabHost.TabSpec tabClients = tabHost.newTabSpec(getResources().getString(R.string.CLIENTS));
		TOTAL_TABS++;		

		TabHost.TabSpec tabSettings = tabHost.newTabSpec(getResources().getString(R.string.SETTINGS));
		TOTAL_TABS++;
		Resources res = getResources();

		tabMessage.setIndicator(res.getString(R.string.MESSAGES),res.getDrawable(R.drawable.frs_message_tab)).setContent(
				new Intent(this, FRSTabNegotiator.class).putExtra(
						Constants.ACTIVITY_TYPE,
						Constants.MESSAGES_REF));


		tabClients.setIndicator(res.getString(R.string.CLIENTS),
				res.getDrawable(R.drawable.frs_client_tab)).setContent(
						new Intent(this, FRSTabNegotiator.class).putExtra(
								Constants.ACTIVITY_TYPE, Constants.CLIENTS_REF));

		tabSettings.setIndicator(res.getString(R.string.SETTINGS),
				res.getDrawable(R.drawable.frs_settings_tab)).setContent(new Intent(this,FRSTabNegotiator.class).putExtra(
						Constants.ACTIVITY_TYPE, Constants.SETTINGS_REF));

		try {
			tabHost.addTab(tabMessage);
			tabHost.addTab(tabClients);
			tabHost.addTab(tabSettings);
			tabHost.setCurrentTab(1);
			changeBackgroundColor();
		} catch (Exception e) {
			// TODO: handle exception
			SharedMethods.logError("Locale"+ e.getMessage());
		}

	}

	private void changeBackgroundColor() {

		/*Drawable backgroundDrawable = getResources().getDrawable(R.drawable.footer_bg); 
		tabHost.getTabWidget().setBackgroundDrawable(
				backgroundDrawable);*/
		//tabHost.getTabWidget().setBackgroundResource(R.drawable.footer_bg);
		for(int i=0;i< tabHost.getTabWidget().getChildCount();i++){
			tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
			

			/*int height = tabHost.getTabWidget().getHeight();
			SharedMethods.logError("Height"+height);
			if(i == 1){
				TextView title = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); 
				title.setTypeface(null,Typeface.BOLD);
				tabHost.getTabWidget().getChildAt(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.footer_middle_bg));
			}else if(i == 0){				
				tabHost.getTabWidget().getChildAt(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.footer_side_bg));
			}else if(i == 2){				
				tabHost.getTabWidget().getChildAt(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.footer_side_bg));
			}*/
		}		
	}

}
