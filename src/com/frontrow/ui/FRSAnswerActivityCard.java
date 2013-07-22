package com.frontrow.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;
import com.frontrow.android.db.ActivityCardDB;
import com.frontrow.android.db.ClientDB;
import com.frontrow.android.db.FRSTermsDB;
import com.frontrow.android.db.FrontRowDBHelper;
import com.frontrow.android.db.MessagesDB;
import com.frontrow.android.db.QuestionDB;
import com.frontrow.android.db.UserDB;
import com.frontrow.android.json.JSONException;
import com.frontrow.android.json.JSONObject;
import com.frontrow.base.BaseActivity;
import com.frontrow.common.Constants;
import com.frontrow.common.SharedMethods;
import com.frontrow.core.ApplicationUser;
import com.frontrow.datamanagers.AnswerActivityCardDataManager;
import com.frontrow.datamanagers.AuthDataManager;
import com.frontrow.interfaces.IFrontResponseReceiver;
import com.frontrow.language.LanguageManager;
import com.memobile.connection.AsyncHttpClient;
import com.memobile.connection.ResponseHandler;
import com.row.mix.beans.ActivityCard;
import com.row.mix.beans.Answers;
import com.row.mix.beans.Client;
import com.row.mix.beans.OfflineM;
import com.row.mix.beans.Questions;
import com.row.mix.beans.User;
import com.row.mix.common.MIXKeys;
import com.row.mix.response.CardSubmitResponse;

public class FRSAnswerActivityCard extends BaseActivity implements
IFrontResponseReceiver, OnItemSelectedListener {

	private Spinner salesGrp;
	private Spinner clients;
	private TextView smileId;
	private LinearLayout cardLayout;
	private ImageView exapnd;
	private LinearLayout questionLayout;
	private ArrayList<ActivityCard> questionTypes;
	private ArrayList<Client> clientList;
	private ArrayList<String> clientNames;
	private LinkedHashMap<Questions, ArrayList<Answers>> qsAnsList;
	private LinkedHashMap<ActivityCard, LinkedHashMap<Questions, ArrayList<Answers>>> cardQsAnsList;
	private boolean isExpandable = false;
	private LinkedHashMap<Questions, String> questionMapping;
	private Button submitActivityCard;
	private ArrayList<ClientSpinner> items;
	private ArrayList<CardSpinner> cardList;
	private int selectedCardId;
	private LinkedHashMap<Questions, LinkedList<Answers>> qAnsMAp;
	private EditText[] clientNotes;
	private Button[] timeBtn;
	private Button[] dateBtn;
	private HashMap<String, String> dateTimeMap;
	// private List<EditText> allEt;
	private HashMap<EditText, Questions> allEt;
	private boolean isHash = false;

	/* Configurable labels */
	private TextView selCard, selClient, selClientId, card;

	/* Date Dialogs */
	private DatePickerDialog datePickDiag;
	private TimePickerDialog timePicker;

	/* Assigned Values */
	private String activityCardType;
	private String clientNumber;
	private String cNotes = "";
	private String selectedDate = "";
	private User usr;
	private int userDefaultCardID;
	private String clientName;

	/* Displaying cardID according to user's cardsource and cardoverride */
	private int displayCardId;
	// private ApplicationUser appUsr;
	private OfflineM offMsg;
	private Object response;

	@Override
	public void webResponseRecievedFromDataManager(Object response) {
		// TODO Auto-generated method stub
		if (response instanceof CardSubmitResponse) {
			refreshViewInTheUIThread();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		removeDialog(Constants.SHOW_DIALOG);
		titlename.setText(activityCardType);
		AnswerActivityCardDataManager.getSharedInstance().registerForDineSafe(
				this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		AnswerActivityCardDataManager.getSharedInstance()
		.unregisterForDineSafe(this);
	}

	@Override
	protected void initializeUI() {
		titlename.setText("");
		// TODO Auto-generated method stub
		int userId = appUsr.getUserId();
		SharedMethods.logError("USER ID :" + Integer.toString(userId));

		if (this == null) {
			SharedMethods.logError("THis NUll");
		} else {
			SharedMethods.logError("THis NOT NUll");
		}
		usr = new UserDB(this).getUser(userId);

		userDefaultCardID = usr.getCardSource();

		dateTimeMap = new HashMap<String, String>();
		cardList = new ArrayList<CardSpinner>();
		salesGrp = (Spinner) findViewById(R.id.sGroup);
		clients = (Spinner) findViewById(R.id.sClient);
		smileId = (TextView) findViewById(R.id.cSmile);
		cardLayout = (LinearLayout) findViewById(R.id.actLayout);
		questionLayout = (LinearLayout) findViewById(R.id.questions);
		exapnd = (ImageView) findViewById(R.id.clps);
		submitActivityCard = (Button) findViewById(R.id.submit);

		selCard = (TextView) findViewById(R.id.selActivity);
		selClient = (TextView) findViewById(R.id.selClient);
		selClientId = (TextView) findViewById(R.id.selClientId);
		card = (TextView) findViewById(R.id.cardLbl);

		if (appUsr.getAllTerms() == null) {
			FRSTermsDB terms = new FRSTermsDB(this);
			HashMap<String, String> allTerms = terms
					.getAllTermsForCompanyID(appUsr.getCompanyId());
			if (allTerms != null) {
				this.appUsr.setAllTerms(allTerms);
			}
		}

		selCard.setText((appUsr != null) ? getResources().getString(
				R.string.SELECT)
				+ " " + appUsr.getAllTerms().get(Constants.ACTIVITY_CARD_KEY)
				: getResources().getString(R.string.SALES_GROUP));
		selClient.setText((appUsr != null) ? getResources().getString(
				R.string.SELECT)
				+ " " + appUsr.getAllTerms().get(Constants.CLIENT_KEY)
				: getResources().getString(R.string.SELECT_CLIENT));
		selClientId.setText((appUsr != null) ? appUsr.getAllTerms().get(
				Constants.CLIENT_KEY)
				+ " " + getResources().getString(R.string.ID) : getResources()
				.getString(R.string.CLIENT_SMILE));
		card.setText((appUsr != null) ? appUsr.getAllTerms().get(
				Constants.ACTIVITY_CARD_KEY) : getResources().getString(
						R.string.ACTIVITY_CARD));

		clientNames = new ArrayList<String>();
		questionMapping = new LinkedHashMap<Questions, String>();

		/* listeners */

		salesGrp.setOnItemSelectedListener(this);
		clients.setOnItemSelectedListener(this);

		/* set sspinner values */
		spinnerValues();

		/* if submiting through selected client */
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			validateBundle(bundle);
		}

		drawFeilds(0);

		addClickListeners();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		// this.finish();
	}

	private void validateBundle(Bundle bundle) {
		String clientNumber = bundle.getString(Constants.CLIENT_NUMBER);
		String clientName = bundle.getString(Constants.CLIENT_NAME);
		String cardType = bundle.getString(Constants.CARD_TYPE);
		for (int i = 0; i < questionTypes.size(); i++) {
			ActivityCard card = questionTypes.get(i);
			if (card.getCardType().trim()
					.equalsIgnoreCase(usr.getUserCardType().trim())) {
				salesGrp.setSelection(i);
			}
		}

		for (int j = 0; j < clientList.size(); j++) {
			Client client = clientList.get(j);
			if (client.getClientNumber().trim()
					.equalsIgnoreCase(clientNumber.trim())) {
				clients.setSelection(j);
			}

		}
	}

	@Override
	protected void refreshUI() {
		// TODO Auto-generated method stub
		submitActivityCard.setEnabled(true);
		getParent().removeDialog(Constants.SHOW_DIALOG);
		// startActivity(getIntent());
		// finish();

		if (response instanceof CardSubmitResponse) {
			// getInflatedProgress().setVisibility(View.GONE);
			int code = ((CardSubmitResponse) response).getResponseCode();
			if (code == Constants.NETWORK_SUCCESS) {

				offMsg.setStatus(Constants.SUCCESS);
				offMsg.setClientName(clientName + " - " + activityCardType);
				new MessagesDB(this).addOfflineMessages(offMsg,
						appUsr.getUserId());

				/*
				 * Bundle bundle2 = new Bundle();
				 * bundle2.putString(Constants.ERROR_D,
				 * getResources().getString(R.string.CARD_SUMBIT_MSG));
				 * bundle2.putString(Constants.TYPE,
				 * Constants.CARD_SUBMIT_TYPE);
				 * getParent().showDialog(Constants.ERROR_DIALOG, bundle2);
				 */

				Toast toast = Toast.makeText(this,
						getResources().getString(R.string.CARD_SUMBIT_MSG),
						5000);
				toast.show();

			} else if (code == Constants.CONNECT_TO_SERVER_ERROR) {

				offMsg.setStatus(Constants.PENDING);
				offMsg.setClientName(clientName + " - " + activityCardType);
				new MessagesDB(this).addOfflineMessages(offMsg,
						appUsr.getUserId());

				/*
				 * Bundle bundle2 = new Bundle();
				 * bundle2.putString(Constants.ERROR_D,
				 * getResources().getString(R.string.CARD_SUMBIT_MSG));
				 * bundle2.putString(Constants.TYPE,
				 * Constants.CARD_SUBMIT_TYPE);
				 * getParent().showDialog(Constants.ERROR_DIALOG, bundle2);
				 */

				Toast toast = Toast.makeText(this,
						getResources().getString(R.string.CARD_SUMBIT_MSG),
						5000);
				toast.show();
			} else {

				offMsg.setStatus(Constants.FAILED);
				offMsg.setClientName(clientName + " - " + activityCardType);
				new MessagesDB(this).addOfflineMessages(offMsg,
						appUsr.getUserId());

				/*
				 * Bundle bundle2 = new Bundle();
				 * bundle2.putString(Constants.ERROR_D,
				 * getResources().getString(R.string.NETWOK_UNREACHABLE));
				 * bundle2.putString(Constants.TYPE,
				 * Constants.CARD_SUBMIT_TYPE);
				 * getParent().showDialog(Constants.ERROR_DIALOG, bundle2);
				 */
				Toast toast = Toast.makeText(this,
						getResources().getString(R.string.NETWOK_UNREACHABLE),
						5000);
				toast.show();

			}
			finish();

			Intent intent = new Intent(this, FRSClientList.class);
			// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			com.frontrow.ui.TabManager parentActivity = (TabManager) getParent();
			parentActivity.setMain("Client_Activity", intent);
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		View fullViewToLoad = LayoutInflater.from(this.getParent()).inflate(
				R.layout.frontrowansweractivitycard, null);
		setContentView(fullViewToLoad);
		// appUsr= ApplicationUser.getSharedInstance();
		FrontRowDBHelper.setContext(this);
		super.onCreate(savedInstanceState);
	}

	private void spinnerValues() {
		// questionTypes = AuthDataManager.getSharedInstance().getCardTypes();
		ActivityCardDB cardDb = new ActivityCardDB(this);
		// cardDb.open();
		questionTypes = cardDb.getAllActivityCards();
		if (questionTypes != null) {
			for (ActivityCard card : this.questionTypes) {
				cardList.add(new CardSpinner(card.getCardType().trim(), card
						.getCardId()));
			}
			ArrayAdapter<CardSpinner> cardAdapter = new ArrayAdapter<CardSpinner>(
					this, R.layout.spinnertext, cardList);
			cardAdapter.setDropDownViewResource(R.layout.spinnerdropdown);
			salesGrp.setAdapter(cardAdapter);
			salesGrp.setSelected(true);
		}

		/* retrieving client list from current instance */
		// clientList = AuthDataManager.getSharedInstance().getClientList();

		ClientDB clientDB = new ClientDB(this);
		// clientDB.open();
		clientList = clientDB.getAllClinets(appUsr.getUserId());
		if (clientList != null) {
			for (int i = 0; i < clientList.size(); i++) {
				Client client = clientList.get(i);
				clientNames.add(client.getClientName());
			}
			items = new ArrayList<ClientSpinner>();
			for (Client client : clientList) {
				items.add(new ClientSpinner(client.getClientName(), client));
			}
			ArrayAdapter<ClientSpinner> cAdapter = new ArrayAdapter<ClientSpinner>(
					this, R.layout.spinnertext, items);
			cAdapter.setDropDownViewResource(R.layout.spinnerdropdown);
			clients.setAdapter(cAdapter);
		}
	}

	private void addClickListeners() {
		cardLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!isExpandable) {
					exapnd.setImageDrawable(getResources().getDrawable(
							R.drawable.answeractivitydropdownbuttonup));
					isExpandable = true;
					questionLayout.setVisibility(View.VISIBLE);
					submitActivityCard.setVisibility(View.VISIBLE);
				} else {
					submitActivityCard.setVisibility(View.GONE);
					exapnd.setImageDrawable(getResources().getDrawable(
							R.drawable.answeractivitydropdownbutton));
					isExpandable = false;
					questionLayout.setVisibility(View.GONE);
				}
			}
		});

		submitActivityCard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				
				Bundle bundle = new Bundle();
				bundle.putSerializable("allEt", allEt);
				bundle.putSerializable("qAnsMap", qAnsMAp);
				bundle.putSerializable("qustionMapping", questionMapping);
				bundle.putString("CardType",activityCardType);
				bundle.putInt("Cardtype", selectedCardId);
				bundle.putString("ClientNumber", clientNumber);
				bundle.putString("ClientName",clientName);
				Intent intent = new Intent(FRSAnswerActivityCard.this,ActivityCardPostView.class);
				intent.putExtras(bundle);
				com.frontrow.ui.TabManager parentActivity = (TabManager) getParent();
				parentActivity.startChild("Activity_PostView_Activity",intent);
				
				// TODO Auto-generated method stub
		/*		getParent().showDialog(Constants.SHOW_DIALOG);
				 Disable Click event 
				if (allEt != null) {
					Set<EditText> editSet = allEt.keySet();
					Iterator<EditText> itrSet = editSet.iterator();
					while (itrSet.hasNext()) {
						EditText type = itrSet.next();
						String s = (type.getText().toString().trim().length() == 0) ? "#"
								: type.getText().toString().trim() + "#";
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
				submitActivityCard.setEnabled(false);
				LinkedHashMap<Questions, String> sortedList = SharedMethods
						.sortByComparator(questionMapping);
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
								String selectedAnswer = sortedList.get(q)
										.toString().trim();
								if (q.getqType() == Constants.TYPE_SPINNER) {
									LinkedList<Answers> tmpAns = qAnsMAp
											.get(tmpQuestions);
									for (int i = 0; i < tmpAns.size(); i++) {
										Answers answer = tmpAns.get(i);
										if (answer.getAnswerText().trim()
												.equals(selectedAnswer)) {
											ansCode = ansCode
													+ answer.getAnswerCode();
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

				SharedMethods.logError("Clinet Note :" + ansCode);

				// AnswerActivityCardDataManager.getSharedInstance().submitActivityCard(clientNumber,
				// ansCode,
				// cNotes,activityCardType,Integer.toString(selectedCardId),clientName
				// + " - "+activityCardType);

				JSONObject card = new JSONObject();
				try {
					// String message =
					// clientNumber+MIXKeys.MSG_CONSTANTS+selectedCardType+"."+answers+clientNotes+MIXKeys.MSG_APPEND;
					String message = clientNumber + MIXKeys.MSG_CONSTANTS
							+ ansCode;
					card.put(MIXKeys.MOBILE_NO, appUsr.getMobileNnumber());
					card.put(MIXKeys.CARD_NAME, activityCardType);
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
						Constants.BASE_URL + "messages/message");
				httpClient.setBasicAuthToken(appUsr.getToken());
				// httpClient.setMethod("PUT");
				httpClient.post(FRSAnswerActivityCard.this, null, null,
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
								content, statusCode,
								getResources().getString(
										R.string.NETWOK_UNREACHABLE),
										offMsg);

						refreshView(
								statusCode,
								getResources().getString(
										R.string.NETWOK_UNREACHABLE),
										cardRes);
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
						CardSubmitResponse cardRes = new CardSubmitResponse(
								content, 400, getResources().getString(
										R.string.NETWOK_UNREACHABLE),
										offMsg);

						refreshView(
								cardRes.getResponseCode(),
								getResources().getString(
										R.string.NETWOK_UNREACHABLE),
										cardRes);
					}
				}, jstring);*/

			}
		});
	}

	private void refreshView(int code, String msg, CardSubmitResponse authRes) {
		// ClientsNoteDataManager.getSharedInstance().setClientsNote((authRes));
		// tempNote.setId(authRes.getNoteId());

		response = authRes;
		refreshViewInTheUIThread();
	}

	private void drawFeilds(int type) {
		this.questionLayout.removeAllViews();
		cardQsAnsList = AuthDataManager.getSharedInstance().getCardQesAns();
		int defCardId = new UserDB(this).getUserCardType(appUsr
				.getMobileNnumber());
		int cardtype = (type == 0) ? defCardId : type;
		SharedMethods.logError("cardtype " + cardtype);
		ArrayList<Questions> qLst = new ArrayList<Questions>();
		qAnsMAp = new QuestionDB(this).getQuestionsForSelectedCard(cardtype);
		Set<Questions> qes = qAnsMAp.keySet();
		Iterator<Questions> itrQ = qes.iterator();
		while (itrQ.hasNext()) {
			Questions questions = (Questions) itrQ.next();
			qLst.add(questions);
		}
		TableRow[] rowArr = new TableRow[qLst.size()];
		LinearLayout[] relative = new LinearLayout[qLst.size()];
		LinearLayout[] btnLayour = new LinearLayout[qLst.size()];

		int width = getWindowManager().getDefaultDisplay().getWidth() - 40;
		ViewGroup.LayoutParams vp = new ViewGroup.LayoutParams(width,
				LayoutParams.WRAP_CONTENT);

		int widthC = getWindowManager().getDefaultDisplay().getWidth() - 40;
		ViewGroup.LayoutParams vp1 = new ViewGroup.LayoutParams(widthC,
				LayoutParams.WRAP_CONTENT);

		TextView[] mreIns = new TextView[qLst.size()];
		Spinner sp;
		EditText ed;
		final List<Spinner> allSpinners = new ArrayList<Spinner>();
		allEt = new HashMap<EditText, Questions>();
		// allEt = new ArrayList<EditText>();
		Spinner[] arrySpinner = new Spinner[qLst.size()];
		clientNotes = new EditText[qLst.size()];
		timeBtn = new Button[qLst.size()];
		dateBtn = new Button[qLst.size()];

		if (qLst != null) {
			for (int i = 0; i < qLst.size(); i++) {
				final int x = i;
				// Questions qs = qLst.get(i);
				mreIns[i] = new TextView(this);
				mreIns[i].setPadding(0, 2, 10, 0);

				btnLayour[i] = new LinearLayout(this);
				btnLayour[i].setOrientation(LinearLayout.HORIZONTAL);

				mreIns[i].setEllipsize(null);
				mreIns[i].setLayoutParams(vp);
				relative[i] = new LinearLayout(this);
				relative[i].setOrientation(LinearLayout.VERTICAL);
				rowArr[i] = new TableRow(this);
				// rowArr[i].setLayoutParams(new
				// LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
				final Questions q = qLst.get(i);
				mreIns[i].setText(q.getQuestion());
				// mreIns[i].setTextSize(getResources().getDimension(R.dimen.Question_Text_Size));
				mreIns[i].setTypeface(null, Typeface.BOLD);
				mreIns[i].setTextColor(getResources().getColor(
						R.color.Default_Text_Color));

				relative[i].addView(mreIns[i]);
				// rowArr[i].addView(mreIns[i],
				// (getWindowManager().getDefaultDisplay().getWidth()/2)-10,LayoutParams.WRAP_CONTENT);
				if (q.getqType() == Constants.TYPE_SPINNER) {
					ArrayList<String> answers = new ArrayList<String>();
					sp = new Spinner(this.getParent());
					// sp.setLayoutParams(vp1);
					sp.setPadding(0, 2, 10, 0);
					// sp.setBackgroundDrawable(getResources().getDrawable(R.drawable.dropdownbg));

					allSpinners.add(sp);
					/* will get the selected value */
					sp.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {
							// TODO Auto-generated method stub
							Object o = arg0.getAdapter().getItem(arg2);
							if (questionMapping != null) {
								if (questionMapping.containsKey(x)) {
									questionMapping.remove(x);
								}
								questionMapping.put(q, o.toString());
							}
							SharedMethods.logError(o.toString() + "Index: "
									+ Integer.toString(x));
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							// TODO Auto-generated method stub

						}
					});

					LinkedList<Answers> ansList = qAnsMAp.get(q);
					if (ansList != null && ansList.size() > 0) {
						for (int j = 0; j < ansList.size(); j++) {
							Answers ans = ansList.get(j);
							answers.add(ans.getAnswerText());
						}
					}
					ArrayAdapter<String> ansAdapter = new ArrayAdapter(this,
							R.layout.spinnertext, answers);

					ansAdapter
					.setDropDownViewResource(R.layout.spinnerdropdown);

					sp.setClipChildren(true);
					sp.setAdapter(ansAdapter);
					sp.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.frs_spinner));

					relative[i].addView(sp);
					// rowArr[i].addView(sp,
					// (getWindowManager().getDefaultDisplay().getWidth()/2)-10
					// ,LayoutParams.WRAP_CONTENT);
				} else if (q.getqType() == Constants.TYPE_ALPHA_NUMERIC) {
					clientNotes[i] = new EditText(this);
					// clientNotes[i].setPadding(0, 0, 10, 0);
					clientNotes[i].setSingleLine();
					// clientNotes[i].setLayoutParams(vp1);
					clientNotes[i].setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.frs_edit_text));
					clientNotes[i].setMinHeight(45);
					clientNotes[i].setPadding(5, 0, 10, 0);

					clientNotes[i].addTextChangedListener(new TextWatcher() {

						@Override
						public void onTextChanged(CharSequence s, int start,
								int before, int count) {
							// TODO Auto-generated method stub
							SharedMethods.logError("SSSSSSSSSSSSS " + s
									+ Integer.toString(count));
							String searchTerm = s.toString();
							if (searchTerm.contains("#")) {
								// searchTerm = "";
								isHash = true;
							} else {
								isHash = false;
							}
						}

						@Override
						public void beforeTextChanged(CharSequence s,
								int start, int count, int after) {
							// TODO Auto-generated method stub

						}

						@Override
						public void afterTextChanged(Editable s) {
							// TODO Auto-generated method stub
							if (isHash) {
								s.delete(s.length() - 1, s.length());
							}

						}
					});
					// allEt.add(clientNotes[i]);
					if (allEt != null) {
						if (allEt.containsKey(clientNotes[i])) {
							allEt.remove(clientNotes[i]);
						}
						// String s =
						// (clientNotes[i].getText().toString().trim().length()
						// == 0)?"#":clientNotes[i].getText().toString().trim();
						allEt.put(clientNotes[i], q);
					}

					clientNotes[i].setImeOptions(EditorInfo.IME_ACTION_NEXT);
					relative[i].addView(clientNotes[i]);
					// rowArr[i].addView(clientNotes[i]);
				} else if (q.getqType() == Constants.TYPE_NUMERIC) {
					clientNotes[i] = new EditText(this);
					// allEt.add(clientNotes[i]);
					// clientNotes[i].setPadding(0, 0, 10, 0);
					clientNotes[i].setSingleLine();
					// clientNotes[i].setLayoutParams(vp1);
					clientNotes[i].setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.frs_edit_text));
					clientNotes[i].setMinHeight(45);
					clientNotes[i].setPadding(5, 0, 10, 0);
					clientNotes[i].setInputType(InputType.TYPE_CLASS_NUMBER);
					clientNotes[i].setImeOptions(EditorInfo.IME_ACTION_NEXT);
					if (allEt != null) {
						if (allEt.containsKey(clientNotes[i])) {
							allEt.remove(clientNotes[i]);
						}
						// String s =
						// (clientNotes[i].getText().toString().trim().length()
						// == 0)?"#":clientNotes[i].getText().toString().trim();
						allEt.put(clientNotes[i], q);
					}
					relative[i].addView(clientNotes[i]);
				} else if (q.getqType() == Constants.TYPE_FORECAST) {
					clientNotes[i] = new EditText(this);
					// allEt.add(clientNotes[i]);
					// clientNotes[i].setPadding(0, 0, 10, 0);
					// clientNotes[i].setLayoutParams(vp1);
					clientNotes[i].setSingleLine();
					clientNotes[i].setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.frs_edit_text));
					clientNotes[i].setMinHeight(45);
					clientNotes[i].setPadding(5, 0, 10, 0);
					clientNotes[i].setInputType(InputType.TYPE_CLASS_NUMBER);
					clientNotes[i].setImeOptions(EditorInfo.IME_ACTION_NEXT);

					if (allEt != null) {
						if (allEt.containsKey(clientNotes[i])) {
							allEt.remove(clientNotes[i]);
						}
						// String s =
						// (clientNotes[i].getText().toString().trim().length()
						// == 0)?"#":clientNotes[i].getText().toString().trim();
						allEt.put(clientNotes[i], q);
					}
					relative[i].addView(clientNotes[i]);
				} else if (q.getqType() == Constants.TYPE_DATE) {
					clientNotes[i] = new EditText(this);
					// allEt.add(clientNotes[i]);
					// clientNotes[i].setPadding(0, 0, 10, 0);
					// clientNotes[i].setLayoutParams(vp1);
					clientNotes[i].setSingleLine();
					clientNotes[i].setEnabled(false);
					clientNotes[i].setFocusable(false);
					clientNotes[i].setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.frs_edit_text));
					clientNotes[i].setMinHeight(45);
					clientNotes[i].setPadding(5, 0, 10, 0);

					if (allEt != null) {
						if (allEt.containsKey(clientNotes[i])) {
							allEt.remove(clientNotes[i]);
						}
						// String s =
						// (clientNotes[i].getText().toString().trim().length()
						// == 0)?"#":clientNotes[i].getText().toString().trim();
						allEt.put(clientNotes[i], q);
					}
					dateBtn[i] = new Button(this);
					dateBtn[i].setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.activitycardheaderbg));
					dateBtn[i].setImeOptions(EditorInfo.IME_ACTION_NEXT);
					dateBtn[i].setText("Select Date");
					dateBtn[i].setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							clientNotes[x].clearFocus();
							showDatePicker(x, q);
						}
					});
					relative[i].addView(clientNotes[i]);
					relative[i].addView(dateBtn[i]);
				} else if (q.getqType() == Constants.TYPE_DATE_TIME) {
					clientNotes[i] = new EditText(this);
					// allEt.add(clientNotes[i]);
					/*
					 * clientNotes[i].setPadding(0, 0, 10, 0);
					 * clientNotes[i].setLayoutParams(vp1);
					 */
					clientNotes[i].setSingleLine();
					clientNotes[i].setEnabled(false);
					clientNotes[i].setFocusable(false);
					clientNotes[i].setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.frs_edit_text));
					clientNotes[i].setMinHeight(45);
					clientNotes[i].setPadding(5, 0, 10, 0);

					if (allEt != null) {
						if (allEt.containsKey(clientNotes[i])) {
							allEt.remove(clientNotes[i]);
						}
						// String s =
						// (clientNotes[i].getText().toString().trim().length()
						// == 0)?"#":clientNotes[i].getText().toString().trim();
						allEt.put(clientNotes[i], q);
					}
					dateBtn[i] = new Button(this);
					dateBtn[i].setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.login_button));
					dateBtn[i].setText("Select Date");
					dateBtn[i].setTextColor(getResources().getColor(
							R.color.ColorPureWhite));
					dateBtn[i].setImeOptions(EditorInfo.IME_ACTION_NEXT);
					dateBtn[i].setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							clientNotes[x].clearFocus();
							showDatePicker(x, q);
						}
					});

					timeBtn[i] = new Button(this);
					timeBtn[i].setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.login_button));
					timeBtn[i].setText("Select Time");
					timeBtn[i].setTextColor(getResources().getColor(
							R.color.ColorPureWhite));
					timeBtn[i].setImeOptions(EditorInfo.IME_ACTION_NEXT);
					timeBtn[i].setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							clientNotes[x].clearFocus();
							showTimePicker(x, q);
						}
					});

					btnLayour[i].addView(dateBtn[i]);
					btnLayour[i].addView(timeBtn[i]);
					relative[i].addView(clientNotes[i]);
					relative[i].addView(btnLayour[i]);
				}else if(q.getqType() == Constants.TYPE_COMMENT){
					clientNotes[i] = new EditText(this);
					// clientNotes[i].setPadding(0, 0, 10, 0);
					clientNotes[i].setSingleLine();
					// clientNotes[i].setLayoutParams(vp1);
					clientNotes[i].setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.frs_edit_text));
					clientNotes[i].setMinHeight(45);
					clientNotes[i].setPadding(5, 0, 10, 0);

					clientNotes[i].addTextChangedListener(new TextWatcher() {

						@Override
						public void onTextChanged(CharSequence s, int start,
								int before, int count) {
							// TODO Auto-generated method stub
							SharedMethods.logError("SSSSSSSSSSSSS " + s
									+ Integer.toString(count));
							String searchTerm = s.toString();
							if (searchTerm.contains("#")) {
								// searchTerm = "";
								isHash = true;
							} else {
								isHash = false;
							}
						}

						@Override
						public void beforeTextChanged(CharSequence s,
								int start, int count, int after) {
							// TODO Auto-generated method stub

						}

						@Override
						public void afterTextChanged(Editable s) {
							// TODO Auto-generated method stub
							if (isHash) {
								s.delete(s.length() - 1, s.length());
							}

						}
					});
					// allEt.add(clientNotes[i]);
					if (allEt != null) {
						if (allEt.containsKey(clientNotes[i])) {
							allEt.remove(clientNotes[i]);
						}
						// String s =
						// (clientNotes[i].getText().toString().trim().length()
						// == 0)?"#":clientNotes[i].getText().toString().trim();
						allEt.put(clientNotes[i], q);
					}

					clientNotes[i].setImeOptions(EditorInfo.IME_ACTION_NEXT);
					relative[i].addView(clientNotes[i]);
				}
				this.questionLayout.addView(relative[i]);
			}
		}
	}

	private void addDateTme() {
		String s = "";
		if (dateTimeMap.containsKey(Constants.DATE)
				|| dateTimeMap.containsKey(Constants.TIME)) {
			s = dateTimeMap.get(Constants.DATE);
			String tm = dateTimeMap.get(Constants.TIME);
			if (tm != null) {
				s = s + dateTimeMap.get(Constants.TIME);
				SharedMethods.logError("Full : " + s);
				if (s.contains(" ")) {
					s = s.replace(" ", "");
				}

				SharedMethods.logError("Full1 : " + s);
			}

			dateTimeMap.put(Constants.DATE_TIME, s);
		}
	}

	private void showDatePicker(final int i, final Questions q) {

		final OnDateSetListener odsl = new OnDateSetListener() {
			public void onDateSet(DatePicker arg0, int year, int month,
					int dayOfMonth) {
				// TODO Auto-generated method stub
				String m = Integer.toString(month + 1);
				String dom = Integer.toString(dayOfMonth);
				m = (month + 1 <= 9) ? "0" + m : m;
				SharedMethods.logError("MONTH : " + m);

				dom = (dayOfMonth <= 9) ? "0" + dom : dom;
				String date = year + "/" + m + "/" + dom;
				if (dateTimeMap.containsKey(Constants.DATE)) {
					dateTimeMap.remove(Constants.DATE);
					dateTimeMap.put(Constants.DATE, date);
				} else {
					dateTimeMap.put(Constants.DATE, date);
				}

				addDateTme();

				String tmp = clientNotes[i].getText().toString();
				// String date = year+"/"+month+"/"+dayOfMonth;
				tmp = (!dateTimeMap.containsKey(Constants.TIME)) ? date : date
						+ " " + dateTimeMap.get(Constants.TIME);
				clientNotes[i].setText(tmp);
			}

		};
		// clientNotes[i].setEnabled(false);
		Calendar cal = Calendar.getInstance();
		// datePickDiag=new
		// DatePickerDialog(FRSAnswerActivityCard.this,odsl,cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));

		datePickDiag = new DatePickerDialog(this.getParent(), odsl,
				cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DAY_OF_MONTH));
		datePickDiag.show();

	}

	private void showTimePicker(final int i, final Questions q) {

		final OnTimeSetListener tsl = new OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				// TODO Auto-generated method stub
				String tmp = clientNotes[i].getText().toString();
				String hod = Integer.toString(hourOfDay);
				String min = Integer.toString(minute);
				hod = (hourOfDay <= 9) ? "0" + hod : hod;
				min = (minute <= 9) ? "0" + min : min;
				String time = hod + ":" + min;
				if (dateTimeMap.containsKey(Constants.TIME)) {
					dateTimeMap.remove(Constants.TIME);
					dateTimeMap.put(Constants.TIME, time);
				} else {
					dateTimeMap.put(Constants.TIME, time);
				}
				addDateTme();
				Date now = new Date();
				Date alsoNow = Calendar.getInstance().getTime();
				String nowAsString = new SimpleDateFormat("yyyy/MM/dd").format(now);
				tmp = (!dateTimeMap.containsKey(Constants.DATE)) ? nowAsString+" "+time
						: dateTimeMap.get(Constants.DATE) + " " + time;
				clientNotes[i].setText(tmp);
			}
		};
		Calendar cal = Calendar.getInstance();
		timePicker = new TimePickerDialog(this.getParent(), tsl,
				cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false);
		timePicker.show();

	}

	private int selectCardToDisplay(int defaultCardIdUser, int clientCardId) {
		int cardId = -1;
		if (defaultCardIdUser == Constants.DEFAULT_CARD
				|| defaultCardIdUser == Constants.CLIENT_CARD) {
			defaultCardIdUser = clientCardId;
		} else if (defaultCardIdUser == Constants.USER_CARD) {
			defaultCardIdUser = usr.getDefaultCardId();
		}
		ActivityCard ac = new ActivityCardDB(this)
		.getActivityCardForCardId(defaultCardIdUser);
		return ac.getCardId();
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		// TODO Auto-generated method stub
		if (arg0 == salesGrp) {
			activityCardType = arg0.getItemAtPosition(position).toString();
			titlename.setText(activityCardType);
			CardSpinner cs = cardList.get(position);
			selectedCardId = cs.getCardId();
			drawFeilds(selectedCardId);
		} else if (arg0 == clients) {
			ClientSpinner cS = items.get(position);
			Client c = cS.getCli();
			displayCardId = selectCardToDisplay(userDefaultCardID,
					c.getCardId());
			/* Assigning the CLient Card to Activitycard Spinner */

			/*
			 * for(int i=0;i<questionTypes.size();i++){ ActivityCard card =
			 * questionTypes.get(i); if(card.getCardId() == displayCardId){
			 * salesGrp.setSelection(i); } }
			 */
			if (!usr.isCardOverRide()) {
				salesGrp.setEnabled(false);
			} else {
				salesGrp.setEnabled(true);
			}

			clientNumber = c.getClientNumber();
			clientName = c.getClientName();
			smileId.setText(clientNumber);
			SharedMethods.logError(clientNumber + " client name");
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}
}

class ClientSpinner {
	String clientName = "";
	String clientNumber = "";
	Client cli;

	public ClientSpinner(String clientName, Client client) {
		// TODO Auto-generated constructor stub
		this.clientName = clientName;
		// this.clientNumber = clientNumber;
		this.cli = client;
	}

	public String getClientName() {
		return clientName;
	}

	public String getClientNumber() {
		return clientNumber;
	}

	public Client getCli() {
		return cli;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return clientName;
	}
}
