package com.frontrow.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.frontrow.android.db.FRSTermsDB;
import com.frontrow.android.db.FrontRowDBHelper;
import com.frontrow.common.Constants;
import com.frontrow.common.SharedMethods;
import com.frontrow.core.ApplicationUser;
import com.frontrow.language.LanguageManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ExpandableListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class FrontRowMenu extends ExpandableListActivity implements ExpandableListView.OnChildClickListener{
	private ExpandableListAdapter mAdapter;

	private Context ctx;
	protected ApplicationUser appUsr;
	private HashMap<String, String> llTerms;
	private String clients,addNewClient,viewAllClients;
	private FRSTermsDB termsDB;
	private double currentVersion;
	private Dialog updateDialog;

	private boolean isOnline;

	/*public ApplicationControl getIdle(){
		return (ApplicationControl) this.getApplication();
	}*/
	
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//appUsr = ApplicationUser.getSharedInstance();
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		isNetworkAvailable();
		FrontRowDBHelper.setContext(this);
		LanguageManager.getSharedInstance().setContext(this);
		SharedMethods.logError("ONCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frontrowmenu);	
		appUsr = ApplicationUser.getSharedInstance();


		/* Restore values */
		if(savedInstanceState !=null){
			this.appUsr = savedInstanceState.getParcelable(Constants.PARCEBLE_KEY);
			SharedMethods.logError("Loadeed MEnu : "+ this.appUsr.getCompanyId());
			//appUsr = ApplicationUser.getSharedInstance();
			setrestoreValues();
			llTerms = this.appUsr.getAllTerms();
			//tempUsr = ApplicationUser.getSharedInstance();

		}else{
			termsDB = new FRSTermsDB(this);
			llTerms = termsDB.getAllTermsForCompanyID(appUsr.getCompanyId());
		}
		try {
			PackageInfo pinfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
			currentVersion = Double.parseDouble(pinfo.versionName);

		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(appUsr.getAppVerstion() != null){
			SharedMethods.logError("Version : "+ Double.parseDouble(appUsr.getAppVerstion()));
			if(Double.parseDouble(appUsr.getAppVerstion()) > currentVersion){
				showDialog(Constants.UPDATE_ALERT);
			}
		}

		//	llTerms = (appUsr == null)?tempUsr.getAllTerms():appUsr.getAllTerms();
		appUsr.setAllTerms(llTerms);
		clients = (llTerms != null)?llTerms.get(Constants.CLIENT_KEY)+"s" :getResources().getString(R.string.CLIENTS);
		addNewClient = (llTerms != null)?getResources().getString(R.string.ADD_NEW)+" "+llTerms.get(Constants.CLIENT_KEY):getResources().getString(R.string.ADD_NEW_CLIENT);
		viewAllClients =  (llTerms != null)?getResources().getString(R.string.VIEW_ALL)+" "+llTerms.get(Constants.CLIENT_KEY)+"s":getResources().getString(R.string.VIEW_ALL_CLIENTS);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.frscustomtitle);
		ctx = getApplicationContext();
	
		String name = getIntent().getExtras().getString(Constants.USER_NAME);
	
		mAdapter = new MyExpandableListAdapter();		
		setListAdapter(mAdapter);		
		registerForContextMenu(getExpandableListView());
		SharedMethods.logError("Menu :"+ Long.toString(System.currentTimeMillis()));
	}

	public class MyExpandableListAdapter extends BaseExpandableListAdapter{
		private int lastExpandedGroup;
		private String[] groups = {clients, "Settings"};
		//private String[] groups = { "Activity Card", "Clients", "Messages", "Settings" };
		private  String[][] children = {
				{ addNewClient, viewAllClients },
				{ "Messages","Sales Pro Settings"}
		};
		//{ "Messages","Help", "Change Password","Send Log FIle","About" }
		public Object getChild(int groupPosition, int childPosition) {
			return children[groupPosition][childPosition];
		}

		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		public int getChildrenCount(int groupPosition) {
			return children[groupPosition].length;
		}

		public TextView getGenericView() {
			// Layout parameters for the ExpandableListView
			AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, 64);

			TextView textView = new TextView(FrontRowMenu.this);
			textView.setLayoutParams(lp);
			// Center the text vertically
			textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
			// Set the text starting position
			textView.setPadding(36, 0, 0, 0);
			return textView;
		}

		public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
				View convertView, ViewGroup parent) {		

			if(convertView == null){
				LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.frsmenuchildrow, null);
			}			
			TextView textView = (TextView) convertView.findViewById(R.id.frschildrow);
			textView.setText(getChild(groupPosition, childPosition).toString());
			return convertView;
		}

		public Object getGroup(int groupPosition) {
			return groups[groupPosition];
		}

		public int getGroupCount() {
			return groups.length;
		}

		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
				ViewGroup parent) {	

			if (convertView == null) {
				LayoutInflater inflater =  (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.frsmenugrouprow, null);
			}
			convertView.setBackgroundDrawable(getResources().getDrawable(R.drawable.allclientsboxbg));

			TextView textView = (TextView) convertView.findViewById(R.id.frsgroupname);
			textView.setText(getGroup(groupPosition).toString());
			if(isExpanded){

			}
			return convertView;
		}

		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

		public boolean hasStableIds() {
			return true;
		}		

		@Override
		public void onGroupExpanded(int groupPosition) {
			// TODO Auto-generated method stub
			if(groupPosition != lastExpandedGroup){
				getExpandableListView().collapseGroup(lastExpandedGroup);
			}
			super.onGroupExpanded(groupPosition);           
			lastExpandedGroup = groupPosition;
		}		
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		menu.setHeaderTitle("Sample menu");
		menu.add(0, 0, 0, R.string.PASSWORD);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) item.getMenuInfo();
		String title = ((TextView) info.targetView).getText().toString();
		int type = ExpandableListView.getPackedPositionType(info.packedPosition);
		if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
			int groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition); 
			int childPos = ExpandableListView.getPackedPositionChild(info.packedPosition); 
			Toast.makeText(this, title + ": Child " + childPos + " clicked in group " + groupPos,
					Toast.LENGTH_SHORT).show();
			return true;
		} else if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
			int groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition); 
			Toast.makeText(this, title + ": Group " + groupPos + " clicked", Toast.LENGTH_SHORT).show();
			return true;
		}
		return false;
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		// TODO Auto-generated method stub


		if(groupPosition == 0 ){
			switch (childPosition) {
			case 0:
				startActivity(new Intent(this,FRSNewClient.class));
				break;

			case 1:
				startActivity(new Intent(this,FRSClientList.class));
				break;

			default:
				break;
			}
		}else if(groupPosition == 1){
			switch (childPosition) {
			case 0:
				SharedMethods.logError("TErmes Menu "+appUsr.getCompanyId() + " , "+ appUsr.getUserName());
				startActivity(new Intent(this,FRSMessages.class));
				break;
			case 1:
				SharedMethods.logError("TErmes Menu "+appUsr.getCompanyId() + " , "+ appUsr.getUserName());
				Intent i = new Intent(this,FRSSettings.class);
				Bundle b = new Bundle();
				b.putParcelable(Constants.APP_USR, appUsr);
				i.putExtras(b);
				startActivity(i);

			break;

			case 2:

				break;
			default:
				break;
			}

		}

		/*		else if(groupPosition == 3){
			switch (childPosition) {
			case 0:

				break;
			case 1:
				break;

			case 2:

				break;
			default:
				break;
			}
		}*/
		SharedMethods.logError(Integer.toString(groupPosition) + " Child Position " + Integer.toString(childPosition));
		return super.onChildClick(parent, v, groupPosition, childPosition, id);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			exitApp(LanguageManager.getString(R.string.EXIT_MSG), "SalesPro", this);
			break;
		default:
			break;
		}
		return true;
	}

	public void exitApp(String body, String header, Context context) {

		new AlertDialog.Builder(context).setTitle(header).setMessage(body).setCancelable(false).setNeutralButton(
				LanguageManager.getString(R.string.YES),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						finish();
						java.lang.System.exit(0);
					}
				}).setNegativeButton(context.getResources().getString(R.string.NO),new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,int which) {

					}
				}).show();

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		SharedMethods.logError("Application stored befored killed");
		outState.putParcelable(Constants.PARCEBLE_KEY, this.appUsr);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		SharedMethods.logError("Application Restored onRestoreInstanceState");
		if(savedInstanceState !=null)
			this.appUsr = savedInstanceState.getParcelable(Constants.PARCEBLE_KEY);
	}

	private void setrestoreValues(){
		appUsr = ApplicationUser.getSharedInstance();
		appUsr.setCompanyId(this.appUsr.getCompanyId());
		appUsr.setMobileNnumber(this.appUsr.getMobileNnumber());
		appUsr.setOnline(this.appUsr.isOnline());
		appUsr.setSaved(this.appUsr.isSaved());
		appUsr.setSingleView(this.appUsr.isSingleView());
		appUsr.setPassword(this.appUsr.getPassword());
		appUsr.setTempPwd(this.appUsr.getTempPwd());
		appUsr.setToken(this.appUsr.getToken());
		appUsr.setUserId(this.appUsr.getUserId());
		appUsr.setUserName(this.appUsr.getUserName());
		appUsr.setAllTerms(this.appUsr.getAllTerms());

		SharedMethods.logError("Saved USer "+ appUsr.getMobileNnumber()+"This : "+this.appUsr.getMobileNnumber()+", "+ this.appUsr.getCompanyId());
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {
		case Constants.UPDATE_ALERT:
			//return SharedMethods.createSingleButtonAlert(this);
			return showUpdateDialog(this);
		default:
			break;
		}
		return null;
	}
	
	
	public Dialog showUpdateDialog(final Context activity) {
		updateDialog = new Dialog(this,R.style.customeDialog);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.width = WindowManager.LayoutParams.FILL_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		View locationView = LayoutInflater.from(this).inflate(R.layout.frsupdate_dialog, null);
		updateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		updateDialog.setContentView(locationView);
		
		updateDialog.getWindow().setAttributes(lp);
		Button btn = (Button) locationView.findViewById(R.id.up);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setData(Uri.parse(Constants.MARKET_URL));
				activity.startActivity(intent);
				updateDialog.dismiss();
				
			}
		});
		return updateDialog;
	}
}
