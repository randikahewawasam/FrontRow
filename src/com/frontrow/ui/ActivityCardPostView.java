package com.frontrow.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.provider.*;
import com.frontrow.adapters.FrontRowActivity_PostView_Adapter;
import com.frontrow.adapters.FrontRowClientsNoteAdapter;
import com.frontrow.android.db.MessagesDB;
import com.frontrow.android.json.JSONException;
import com.frontrow.android.json.JSONObject;
import com.frontrow.base.BaseActivity;
import com.frontrow.common.Constants;
import com.frontrow.common.SharedMethods;

import com.frontrow.core.ApplicationUser;
import com.frontrow.datamanagers.AnswerActivityCardDataManager;
import com.frontrow.datamanagers.ClientsNoteDataManager;
import com.google.gson.Gson;
import com.memobile.connection.AsyncHttpClient;
import com.memobile.connection.ResponseHandler;
import com.row.mix.beans.Answers;
import com.row.mix.beans.ClientsNotes;
import com.row.mix.beans.OfflineM;
import com.row.mix.beans.Questions;
import com.row.mix.common.MIXKeys;
import com.row.mix.request.CardSubmitRequest;
import com.row.mix.response.AddNewClientsNoteResponse;
import com.row.mix.response.CardSubmitResponse;
import com.row.mix.response.ClientsNotesResponse;

public class ActivityCardPostView extends BaseActivity {
	private LinkedHashMap<Questions, String> questionMapping;
	private LinkedHashMap<Questions, LinkedList<Answers>> qAnsMAp;
	private HashMap<EditText, Questions> allEt;
	private ArrayList<Questions> qlist;
	private ArrayList<String> ansList;
	private ListView actvity_post_list;
	LinkedHashMap<Questions, String> sortedList ;
	private FrontRowActivity_PostView_Adapter adapter;
	private String clientNumber;
	private String CardType;
	private int cardtype;
	private OfflineM offMsg;
	private Object response;
	private Button btnAddCard;
	private String clientName;
	private TextView pv_clientname;
	private TextView pv_clientnumber;

	private String calendarEventName;
	private long stime = 0;
	private long endTime;
	private String answer;
	private boolean isValidDate;
	private String calendarDescription;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		setContentView(R.layout.activity_activity_card_post_view);
		super.onCreate(savedInstanceState);
	}


	@Override
	protected void initializeUI() {

		actvity_post_list = (ListView) findViewById(R.id.actvity_post_list);
		pv_clientname =(TextView) findViewById(R.id.pv_clientname);
		pv_clientnumber =(TextView) findViewById(R.id.pv_clientnumber);


		btnAddCard = (Button) findViewById(R.id.btnAddCard);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			cardtype = bundle.getInt("Cardtype");
			clientNumber = bundle.getString("ClientNumber");
			CardType = bundle.getString("CardType");
			clientName =bundle.getString("ClientName");

			if (bundle.getSerializable("qustionMapping") != null) {
				questionMapping = (LinkedHashMap<Questions, String>) bundle
						.getSerializable("qustionMapping");
			}

			if (bundle.getSerializable("allEt") != null) {
				allEt = (HashMap<EditText, Questions>) bundle
						.getSerializable("allEt");
			}

			if (bundle.getSerializable("qAnsMap") != null) {
				qAnsMAp = (LinkedHashMap<Questions, LinkedList<Answers>>) bundle
						.getSerializable("qAnsMap");
			}

			if (allEt != null) {
				Set<EditText> editSet = allEt.keySet();
				Iterator<EditText> itrSet = editSet.iterator();
				while (itrSet.hasNext()) {
					EditText type = itrSet.next();
					String s = (type.getText().toString().trim().length() == 0) ? "#"
							: type.getText().toString().trim()+"#";
					if (questionMapping != null) {
						Questions qs = allEt.get(type);
						if (qs.getqType() == Constants.TYPE_DATE
								|| qs.getqType() == Constants.TYPE_DATE_TIME) {
							if (s.contains(" ")) {
								s = s.replace(" ", "");
							}
						}
						questionMapping.put(qs, s);
					}
				}
			}
			sortedList = SharedMethods
					.sortByComparator(questionMapping);

			Set<Questions> questions = sortedList.keySet();
			Collection<String> answers = sortedList.values();
			ansList = new ArrayList<String>();
			qlist = new ArrayList<Questions>();
			qlist.addAll(questions);
			ansList.addAll(answers);

			adapter = new FrontRowActivity_PostView_Adapter(this,
					R.layout.activity_post_list_row, qlist, ansList);
			actvity_post_list.setAdapter(adapter);

			String noteAnswer = null;
			for(Questions quest:questions){
				
				if(quest.getqType() == Constants.TYPE_DATE_TIME){
					answer = sortedList.get(quest);
					if(answer != null && answer.trim().length() > 0 ){
						calendarEventName = clientName +" : "+ quest.getQuestion();
						if(answer.contains("#")){
							answer = answer.replace("#", "");
							if(answer.length() > 10 ){
								StringBuffer sb = new StringBuffer(answer);
								sb.insert(10, " ");
								answer = sb.toString();
								isValidDate = true;								
							}else if(answer.length() == 10){
								answer = answer+" 08:00";
								isValidDate = true;
							}else{
								isValidDate = false;
								//break;								
							}
							SharedMethods.logError("Answer "+answer);
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm");
							try {
								Date d = sdf.parse(answer);
								stime = d.getTime();
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							endTime = stime+ Constants.ONE_HOUR;
							SharedMethods.logError("Answer "+stime);

							
						}
					}
					//break;
				}else if(quest.getqType() == Constants.TYPE_COMMENT){
					noteAnswer = sortedList.get(quest);
					if(noteAnswer.contains("#")){
						noteAnswer = noteAnswer.replace("#", "");
					}
					/* Preparing Calendar Description*/
					String s = null;
					if (isValidDate)
						if(noteAnswer != null && noteAnswer.trim().length() > 0)
							s = clientName +" : "+ answer+"<br/>"+noteAnswer;
					if(s != null)
						calendarDescription = Html.fromHtml(s).toString();
				}
			}
		}

		pv_clientname.setText(clientName);
		pv_clientnumber.setText(clientNumber);

		btnAddCard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendActivityCard();
			}
		});

	}


	@Override
	protected void refreshUI() {
		getParent().removeDialog(Constants.SHOW_DIALOG);
		if (response instanceof CardSubmitResponse) {
			// getInflatedProgress().setVisibility(View.GONE);
			int code = ((CardSubmitResponse) response).getResponseCode();
			if (code == Constants.NETWORK_SUCCESS) {

				offMsg.setStatus(Constants.SUCCESS);
				offMsg.setClientName(clientName+ " - "+CardType);
				new MessagesDB(this).addOfflineMessages(offMsg, appUsr.getUserId());


				Bundle bundle2 = new Bundle();
				bundle2.putString(Constants.ERROR_D,
						getResources().getString(R.string.CARD_SUMBIT_MSG));
				bundle2.putString(Constants.TYPE, Constants.CARD_SUBMIT_TYPE);
				if(isValidDate){
					addEvents(this, calendarEventName, stime,endTime);
				}
				getParent().showDialog(Constants.ERROR_DIALOG, bundle2);

			} else if (code==Constants.CONNECT_TO_SERVER_ERROR) {

				offMsg.setStatus(Constants.PENDING);
				offMsg.setClientName(clientName+ " - "+CardType);
				new MessagesDB(this).addOfflineMessages(offMsg, appUsr.getUserId());

				Bundle bundle2 = new Bundle();
				bundle2.putString(Constants.ERROR_D,
						getResources().getString(R.string.CARD_SUMBIT_MSG));
				bundle2.putString(Constants.TYPE, Constants.CARD_SUBMIT_TYPE);
				getParent().showDialog(Constants.ERROR_DIALOG, bundle2);
			}
			else{

				offMsg.setStatus(Constants.FAILED);
				offMsg.setClientName(clientName+ " - "+CardType);
				new MessagesDB(this).addOfflineMessages(offMsg, appUsr.getUserId());

				Bundle bundle2 = new Bundle();
				bundle2.putString(Constants.ERROR_D,
						getResources().getString(R.string.NETWOK_UNREACHABLE));
				bundle2.putString(Constants.TYPE, Constants.CARD_SUBMIT_TYPE);
				getParent().showDialog(Constants.ERROR_DIALOG, bundle2);

			}
		}
	}

	private void addEvents(Context ctx, final String title, final long dtstart, final long dtend) {
		final ContentResolver cr = getContentResolver();
		Cursor cursor ;
		if (Integer.parseInt(Build.VERSION.SDK) >= 14 ){
			cursor = cr.query(Uri.parse("content://com.android.calendar/calendars"), new String[]{ "_id", "calendar_displayName" }, null, null, null);
		}else{
			cursor = cr.query(Uri.parse("content://com.android.calendar/calendars"), new String[]{ "_id", "displayname" }, null, null, null);
		}

		/*else{
			cursor = cr.query(Uri.parse("content://calendar/calendars"), new String[]{ "_id", "displayname" }, null, null, null);
		}*/

		if ( cursor.moveToFirst() ) {
			final String[] calNames = new String[cursor.getCount()];
			int calIds =0;
			for (int i = 0; i < calNames.length; i++) {
				if(i == 1){
					calIds = cursor.getInt(0);
					calNames[i] = cursor.getString(1);
					cursor.moveToNext();
					break;
				}
			}	   
			//content://com.android.calendar/calendars
			TimeZone timeZone = TimeZone.getDefault();
			ContentValues cv = new ContentValues();
			cv.put("calendar_id", calIds);
			cv.put("title", title);
			cv.put("dtstart", dtstart);
			cv.put("hasAlarm", 1);
			cv.put("description", calendarDescription);
			cv.put("dtend", dtend);
			cv.put("eventTimezone", timeZone.getID());
			Uri finalUri = null;
			Uri newEvent ;
			if (Integer.parseInt(Build.VERSION.SDK) >= 8 )
				newEvent = cr.insert(Uri.parse("content://com.android.calendar/events"), cv);
			else
				newEvent = cr.insert(Uri.parse("content://calendar/events"), cv);

			if (newEvent != null) {
				long id = Long.parseLong( newEvent.getLastPathSegment() );
				ContentValues values = new ContentValues();
				values.put( "event_id", id );
				values.put( "method", 1 );
				values.put( "minutes", 60); // 60 minutes

				if (Integer.parseInt(Build.VERSION.SDK) >= 8 )
					finalUri = cr.insert( Uri.parse( "content://com.android.calendar/reminders" ), values);
				else
					finalUri = cr.insert( Uri.parse( "content://calendar/reminders" ), values);
			}
			long eventID = Long.parseLong(newEvent.getLastPathSegment());
			SharedMethods.logError("eventID "+eventID);

		}
		cursor.close();
	}

	private void sendActivityCard() {
		getParent().showDialog(Constants.SHOW_DIALOG);
		/* Disable Click event */
		int count = sortedList.size();

		String ansCode = "";
		LinkedHashMap<Questions, ArrayList<Answers>> qa = null;

		Set<Questions> qs = sortedList.keySet();

		Iterator<Questions> itrQ = qs.iterator();
		while (itrQ.hasNext()) {
			Questions q = itrQ.next();
			if (qAnsMAp != null) {
				Set<Questions> qsTmp = qAnsMAp.keySet();
				Iterator itrAns = qsTmp.iterator();
				OUTER: while (itrAns.hasNext()) {
					Questions tmpQuestions = (Questions) itrAns.next();
					if (q.equals(tmpQuestions)) {
						String selectedAnswer = sortedList.get(q).toString()
								.trim();
						if (q.getqType() == Constants.TYPE_SPINNER) {
							LinkedList<Answers> tmpAns = qAnsMAp
									.get(tmpQuestions);
							for (int i = 0; i < tmpAns.size(); i++) {
								Answers answer = tmpAns.get(i);
								if (answer.getAnswerText().trim()
										.equals(selectedAnswer)) {
									ansCode = ansCode + answer.getAnswerCode();
									break OUTER;
								}
							}
						} else {

							if (selectedAnswer.contains("/")) {
								selectedAnswer = selectedAnswer
										.replace("/", "");
							}
							if (selectedAnswer.contains(":")) {
								selectedAnswer = selectedAnswer
										.replace(":", "");
							}
							ansCode = ansCode + selectedAnswer;
							break OUTER;
						}
					}
				}
			}
		}


		/* SharedMethods.logError("Clinet Note :" + ansCode);
		  AnswerActivityCardDataManager.getSharedInstance()
		  .submitActivityCard(clientNumber, ansCode, "", CardType,
		  Integer.toString(cardtype));*/

		JSONObject card = new JSONObject();
		try {
			//String message = clientNumber+MIXKeys.MSG_CONSTANTS+selectedCardType+"."+answers+clientNotes+MIXKeys.MSG_APPEND; 
			String message = clientNumber+MIXKeys.MSG_CONSTANTS+ansCode; 
			card.put(MIXKeys.MOBILE_NO, appUsr.getMobileNnumber());
			card.put(MIXKeys.CARD_NAME, CardType);
			card.put(MIXKeys.MESSAGE, message);
			card.put(MIXKeys.COMPANY_CODE, appUsr.getCompanyId());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		String jstring = card.toString();

		offMsg = new OfflineM();
		offMsg.setMessage(jstring);
		offMsg.setDateMillies(java.lang.System.currentTimeMillis());
		offMsg.setType(Constants.MESSAGE);
		offMsg.setSubmitType(Constants.SUBMIT_TYPE);

		// String notstring=parameters();
		AsyncHttpClient httpClient = new AsyncHttpClient(
				Constants.BASE_URL+"messages/message");
		httpClient.setBasicAuthToken(appUsr.getToken());
		// httpClient.setMethod("PUT");
		httpClient.post(ActivityCardPostView.this, null, null,
				new ResponseHandler() {
			@Override
			public void onSuccess(String successMsg) {
				// TODO Auto-generated method stub
				super.onSuccess(successMsg);
				CardSubmitResponse cardRes = new CardSubmitResponse(
						successMsg, 200, "Success", offMsg);

				refreshView(cardRes.getResponseCode(), "", cardRes);
			}

			@Override
			public void onFailure(int statusCode, String content) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, content);

				CardSubmitResponse cardRes = new CardSubmitResponse(
						content, statusCode, getResources().getString(
								R.string.NETWOK_UNREACHABLE), offMsg);

				refreshView(
						statusCode,
						getResources().getString(
								R.string.NETWOK_UNREACHABLE), cardRes);
			}

			@Override
			public void onFailed(String errorMsg, int errorCode) {
				// TODO Auto-generated method stub
				super.onFailed(errorMsg, errorCode);
			}

			@Override
			public void onFailure(Throwable error, String content) {
				// TODO Auto-generated method stub
				super.onFailure(error, content);
				CardSubmitResponse cardRes = new CardSubmitResponse(
						content, 400, getResources().getString(
								R.string.NETWOK_UNREACHABLE), offMsg);

				refreshView(
						400,
						getResources().getString(
								R.string.NETWOK_UNREACHABLE), cardRes);
			}
		}, jstring);

	}

	private void refreshView(int code, String msg, CardSubmitResponse authRes) {
		// ClientsNoteDataManager.getSharedInstance().setClientsNote((authRes));
		// tempNote.setId(authRes.getNoteId());

		response = authRes;
		refreshViewInTheUIThread();
	}

	public void navigateClientListPage() {
		this.finish();
		finish();

		/*Intent intent = new Intent(this, FRSClientList.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		com.frontrow.ui.TabManager parentActivity = (TabManager) getParent();
		parentActivity.startChild("AnswerActivityCard_Activity",
				intent);*/

		Intent intent = new Intent(this, FRSClientList.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		com.frontrow.ui.TabManager parentActivity = (TabManager) getParent();
		parentActivity.setMain("Client_Activity",
				intent);

	}

	private String getCalendarUriBase() {
		String calendarUriBase = null;
		Uri calendars = Uri.parse("content://calendar/calendars");
		Cursor managedCursor = null;
		try {
			managedCursor = managedQuery(calendars, null, null, null, null);
		} catch (Exception e) {
			// eat
		}

		if (managedCursor != null) {
			calendarUriBase = "content://calendar/";
		} else {
			calendars = Uri.parse("content://com.android.calendar/calendars");
			try {
				managedCursor = managedQuery(calendars, null, null, null, null);
			} catch (Exception e) {
				// eat
			}

			if (managedCursor != null) {
				calendarUriBase = "content://com.android.calendar/";
			}

		}

		return calendarUriBase;
	}

}
