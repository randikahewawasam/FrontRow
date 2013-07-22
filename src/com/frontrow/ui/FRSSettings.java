package com.frontrow.ui;

import java.util.HashMap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.frontrow.adapters.FrontRowMessageAdapter;
import com.frontrow.android.db.FRSTermsDB;
import com.frontrow.android.db.MessagesDB;
import com.frontrow.android.pref.DataSerializer;
import com.frontrow.base.BaseActivity;
import com.frontrow.common.Constants;
import com.frontrow.common.SharedMethods;
import com.frontrow.core.ApplicationUser;
import com.frontrow.core.TempFRSUser;
import com.frontrow.datamanagers.AuthDataManager;
import com.frontrow.datamanagers.FRSOfflineMessageDataManager;
import com.frontrow.language.LanguageManager;
import com.memobile.connection.AsyncHttpClient;
import com.memobile.connection.ResponseHandler;
import com.row.mix.beans.User;
import com.row.mix.response.AuthenticationResponse;
import com.row.mix.response.NewClientResponse;

public class FRSSettings extends BaseActivity {
	private CheckBox displayBox;
	private ApplicationUser applicationUsr;
	private String cardTerm;
	private HashMap<String, String> llTerms;
	private TextView lbl;
	private Button logoutbtn;
	private Button updatebtn;
	private Object response;
	private Button emaillogbtn;
	private TextView version;

	@Override
	protected void initializeUI() {

		titlename.setText("Options");
		// TODO Auto-generated method stub
		// appUsr = ApplicationUser.getSharedInstance();

		displayBox = (CheckBox) findViewById(R.id.dspView);
		displayBox.setChecked(appUsr.isSingleView());
		lbl = (TextView) findViewById(R.id.questionLblId);
		version = (TextView) findViewById(R.id.versiontext);
		logoutbtn = (Button) findViewById(R.id.logout);
		updatebtn = (Button) findViewById(R.id.updatebtn);
		emaillogbtn = (Button) findViewById(R.id.emaillogbtn);
		version.setText("Version " + appUsr.getAppVerstion());
		/*
		 * PackageInfo pinfo; try { pinfo =
		 * this.getPackageManager().getPackageInfo(getPackageName(), 0);
		 * version.setText(pinfo.versionName);
		 * 
		 * } catch (NameNotFoundException e) { // TODO Auto-generated catch
		 * block e.printStackTrace(); }
		 */

		FRSTermsDB termsDB = new FRSTermsDB(this);
		SharedMethods.logError("TErmes " + appUsr + " , "
				+ appUsr.getCompanyId() + " , " + appUsr.getUserName());
		llTerms = new FRSTermsDB(this).getAllTermsForCompanyID(appUsr
				.getCompanyId());
		// llTerms = termsDB.getAllTermsForCompanyID(appUsr.getCompanyId());
		cardTerm = (llTerms != null) ? getResources().getString(
				R.string.QUESTION_VIEW)
				+ " "
				+ llTerms.get(Constants.ACTIVITY_CARD_KEY)
				+ getResources().getString(R.string.VIEW)
				: "Enable Full Card View";
				lbl.setText(cardTerm);
				addListeners();
				logoutbtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						Bundle bundle = new Bundle();
						bundle.putString(Constants.ERROR_D,
								getResources().getString(R.string.LOG_OUT_MSG));
						bundle.putString(Constants.TYPE, Constants.LOG_OUT_TYPE);
						// getParent().removeDialog(Constants.ERROR_DIALOG);
						getParent().showDialog(Constants.ERROR_DIALOG, bundle);

					}
				});

				emaillogbtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						SharedMethods.sendEmailLogFile(FRSSettings.this,
								Constants.LOG_EMAIL_ADDRESS, appUsr.getUserName());
						
						

					}
				});

				updatebtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						Bundle bundle2 = new Bundle();
						bundle2.putString(Constants.PROGRESS_MSG,
								getResources().getString(R.string.UPDATE_MSG));
					
						getParent().showDialog(Constants.SHOW_DIALOG, bundle2);
						// FRSTermsDB termsDb = new FRSTermsDB(FRSSettings.this);
						// boolean b =
						// termsDb.isTermsAvailableForCompany(appUsr.getCompanyId());
						//getParent().showDialog(Constants.SHOW_DIALOG);
						int i = 0;
						AsyncHttpClient httpClient = new AsyncHttpClient(
								Constants.BASE_URL+"getalldata/"
										+ i);
						// httpClient.setBasicAuth(appUsr.getCompanyId()+Constants.VAL_CONNECTOR+appUsr.getUserId(),
						// appUsr.getPassword());
						httpClient.setBasicAuthToken(appUsr.getToken());
						httpClient.get(FRSSettings.this, null, null,
								new ResponseHandler() {
							@Override
							public void onSuccess(String successMsg) {
								// TODO Auto-generated method stub
								super.onSuccess(successMsg);
								AuthenticationResponse authRes = new AuthenticationResponse(
										successMsg, 200, "Success");
								refreshView(200, "", authRes);
							}

							@Override
							public void onFailed(String errorMsg, int errorCode) {
								// TODO Auto-generated method stub
								super.onFailed(errorMsg, errorCode);
							}

							@Override
							public void onFailure(Throwable error,
									String content) {
								// TODO Auto-generated method stub
								super.onFailure(error, content);
								AuthenticationResponse authRes = new AuthenticationResponse(
										content, 400, getResources().getString(
												R.string.NETWOK_UNREACHABLE));
								refreshView(
										400,
										getResources().getString(
												R.string.NETWOK_UNREACHABLE),
												authRes);
							}
							
							@Override
							public void onFailure(int statusCode, String content) {
								// TODO Auto-generated method stub
								super.onFailure(statusCode, content);
								AuthenticationResponse authRes = new AuthenticationResponse(content, statusCode, getResources().getString(R.string.NETWOK_UNREACHABLE));
								refreshView(statusCode, getResources().getString(R.string.NETWOK_UNREACHABLE),authRes);
							}
						});

					}
				});

	}

	private void refreshView(int code, String msg,
			AuthenticationResponse authRes) {
		AuthDataManager.getSharedInstance().setResponseValues(authRes);
		response = authRes;
		refreshViewInTheUIThread();
	}

	@Override
	protected void refreshUI() {
		// TODO Auto-generated method stub
		// login.setEnabled(true);
		titlename.setText("Options");
		titlename.setText(getResources().getString(R.string.SETTINGS));

		if (response instanceof AuthenticationResponse) {
			getParent().removeDialog(Constants.SHOW_DIALOG);
			int code = ((AuthenticationResponse) response).getResponseCode();
			if (code == Constants.NETWORK_SUCCESS) {
				User usr = ((AuthenticationResponse) response).getUsr();
				// ApplicationUser appUsr = ApplicationUser.getSharedInstance();
				appUsr.setCompanyId(usr.getCompanyId());
				this.appUsr.setUserName(usr.getUserName());
				this.appUsr.setToken(usr.getToken());
				this.appUsr.setMobileNnumber(usr.getMobileNumber());
				this.appUsr.setUserId(usr.getUserId());
				this.appUsr.setAllClientsEnable(usr.isAllClientsEnable());
				this.appUsr.setPassword(TempFRSUser.getSharedInstance()
						.getTempPwd());
				this.appUsr.setAppVerstion(usr.getApplicationVersion());

				FRSTermsDB terms = new FRSTermsDB(this);
				HashMap<String, String> allTerms = terms
						.getAllTermsForCompanyID(appUsr.getCompanyId());
				if (allTerms != null) {
					this.appUsr.setAllTerms(allTerms);
				}
				// this.setDisplayname(usr.getUserName());
				// this.getUserName().setText(this.getDisplayname());
				/* Saving data to preference */
				DataSerializer.initPref(Constants.SAVE_CREDENTIALS);

				/* Start Menu Activity */

				Bundle bundle2 = new Bundle();
				bundle2.putString(Constants.ERROR_D,
						getResources().getString(R.string.UPDATE_SUCESS_MSG));
				bundle2.putString(Constants.TYPE, Constants.GENARAL_ERROR_TYPE);
				getParent().showDialog(Constants.ERROR_DIALOG, bundle2);

			} else {
				Bundle bundle2 = new Bundle();
				bundle2.putString(Constants.ERROR_D,
						getResources().getString(R.string.NO_NETWORK_TRY_AGAIN_MSG));
				bundle2.putString(Constants.TYPE, Constants.GENARAL_ERROR_TYPE);
				getParent().showDialog(Constants.ERROR_DIALOG, bundle2);
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.frssettings);
		// applicationUsr =
		// getIntent().getExtras().getParcelable(Constants.APP_USR);
		SharedMethods.logError("Before Null");
		if (DataSerializer.getContext() == null) {
			SharedMethods.logError("DataSerializer Null");
			DataSerializer.setContext(this);
			SharedMethods.logError("DataSerializer Not Null");
		}
		SharedMethods.logError("After Null");
		DataSerializer.getQuestionValues();
		super.onCreate(savedInstanceState);
	}

	private void addListeners() {
		displayBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				String s = (isChecked) ? "true" : "false";
				appUsr.setSingleView(isChecked);
				DataSerializer.editQuestionValues();
				SharedMethods.logError(s);
			}
		});
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		// this.finish();
	}

	public void logout() {
		// Constants.mAsyncRunner.logout(getParent().getApplicationContext(),
		// new LogoutRequestListener());

		try {
			DataSerializer.initPref(Constants.REMOVE_CREDENTIALS);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		appUsr.setToken(null);
		appUsr.setAllClientsEnable(false);
		appUsr.setAllTerms(null);
		appUsr.setAppVerstion(null);
		//appUsr.setCompanyId(null);
		appUsr.setLat(0.0);
		appUsr.setLon(0.0);
		//appUsr.setMobileNnumber(null);
		appUsr.setOnline(false);
		appUsr.setPassword(null);
		appUsr.setTempPwd(null);
		appUsr.setUserId(0);
		appUsr.setUserName(null);
		DataSerializer.initPref(Constants.SAVE_CREDENTIALS);
		this.finish();
		Intent intent = new Intent(FRSSettings.this, FrontRowLogin.class);
		startActivity(intent);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// TODO Auto-generated method stub
		titlename.setText(getResources().getString(R.string.SETTINGS));
	}
}
