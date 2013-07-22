package com.frontrow.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import com.frontrow.android.db.ActivityCardDB;
import com.frontrow.android.db.QuestionDB;
import com.frontrow.android.db.UserDB;
import com.frontrow.base.BaseActivity;
import com.frontrow.common.Constants;
import com.frontrow.common.SharedMethods;
import com.frontrow.core.ApplicationUser;
import com.frontrow.datamanagers.AnswerActivityCardDataManager;
import com.frontrow.interfaces.IFrontResponseReceiver;
import com.row.mix.beans.ActivityCard;
import com.row.mix.beans.Answers;
import com.row.mix.beans.Questions;
import com.row.mix.beans.User;
import com.row.mix.response.CardSubmitResponse;

public class FRSQuestions extends BaseActivity implements
IFrontResponseReceiver, OnItemSelectedListener {
	private String clientNumber;
	private String cardType;
	private LinearLayout questionLayout;
	private LinkedHashMap<Questions, LinkedList<Answers>> qAnsMAp;
	private EditText[] clientNotes;
	private Button[] timeBtn;
	private Button[] dateBtn;
	private LinkedHashMap<Questions, String> questionMapping;
	private int y = 0;
	private TextView[] mreIns;
	private ArrayList<Questions> qLst;
	private LinearLayout[] btnLayour;
	private LinearLayout[] relative;
	private TableRow[] rowArr;
	private ViewGroup.LayoutParams vp;
	private RadioGroup arryRdBtnGrp;
	private HashMap<EditText, Questions> allEt;
	private HashMap<Questions, String> temp;
	ViewGroup.LayoutParams vp1;
	private HashMap<String, String> dateTimeMap;
	private String btnName;
	private int cardtype;
	private Spinner salesGrp;
	private ArrayList<ActivityCard> questionTypes;
	private ArrayList<CardSpinner> cardList;
	private String activityCardType;
	private int selectedCardId;
	private LinearLayout changeCardlayout;
	private boolean isHash = false;
	private String clientName;


	/*Date Dialogs */
	private DatePickerDialog datePickDiag;
	private TimePickerDialog timePicker;
	private boolean isLast = false;
	private User usr;

	@Override
	protected void initializeUI() {
		titlename.setText("");
		// TODO Auto-generated method stub
		questionLayout = (LinearLayout) findViewById(R.id.questions);
		questionMapping = new LinkedHashMap<Questions, String>();
		dateTimeMap = new HashMap<String, String>();
		salesGrp = (Spinner) findViewById(R.id.sGroup);
		salesGrp.setOnItemSelectedListener(this);
		usr = new UserDB(this).getUser(appUsr.getUserId());
		if(!usr.isCardOverRide()){
			salesGrp.setEnabled(false);
		}else{
			salesGrp.setEnabled(true);
		}
		cardList = new ArrayList<CardSpinner>();
		changeCardlayout = (LinearLayout) findViewById(R.id.changeCard);
		spinnerValues();

		/*if submiting through selected client */
		Bundle bundle = getIntent().getExtras();
		if(bundle != null){
			validateBundle(bundle);
		}
		drawFeilds(0);
	}

	@Override
	protected void refreshUI() {
		// TODO Auto-generated method stub
		getParent().removeDialog(Constants.SHOW_DIALOG);
		//startActivity(getIntent());
		//finish();
		Toast toast = Toast.makeText(this, getResources().getString(R.string.ANSWER_CARD_SUBMIT_SUCCESS), 5000);
		toast.show();
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		
		View viewToLoad = LayoutInflater.from(this.getParent()).inflate(R.layout.frsquestions, null);
		setContentView(viewToLoad);
		//setContentView(R.layout.frsquestions);
		if(getIntent() != null){
			SharedMethods.logError("INtent : "+ getIntent());
			cardType = getIntent().getExtras().getString(Constants.CARD_TYPE);
			clientNumber = getIntent().getExtras().getString(Constants.CLIENT_NUMBER);
			clientName =getIntent().getExtras().getString(Constants.CLIENT_NAME);

			SharedMethods.logError("Details : "+ getIntent().getExtras().getString(Constants.CARD_TYPE) +" , "+ getIntent().getExtras().getString(Constants.CLIENT_NUMBER)+", "+appUsr);
		}
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		titlename.setText(activityCardType);
		AnswerActivityCardDataManager.getSharedInstance().registerForDineSafe(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		AnswerActivityCardDataManager.getSharedInstance().unregisterForDineSafe(this);
	}


	private void validateBundle(Bundle bundle) {		
		for(int i=0;i<questionTypes.size();i++){
			ActivityCard card = questionTypes.get(i);
			if(card.getCardType().trim().equalsIgnoreCase(usr.getUserCardType().trim())){
				salesGrp.setSelection(i);
			}
		}
	}

	InputFilter alphaNumericFilter = new InputFilter() {   
		@Override  
		public CharSequence filter(CharSequence arg0, int arg1, int arg2, Spanned arg3, int arg4, int arg5)  
		{  
			for (int k = arg1; k < arg2; k++) {   
				if (!Character.isLetterOrDigit('#')) {   
					return "";   
				}   
			}   
			return null;   
		}   
	}; 

	private void drawFeilds(int crdId) {	
		this.questionLayout.removeAllViews();	
		SharedMethods.logError("Draw Fileds" + appUsr.getMobileNnumber());
		int defCardId = new UserDB(this).getUserCardType(appUsr.getMobileNnumber());
		cardtype= (crdId == 0)? defCardId:crdId;
		SharedMethods.logError("cardtype "+ cardtype);	
		qLst = new ArrayList<Questions>();
		qAnsMAp =new QuestionDB(this).getQuestionsForSelectedCard(cardtype);
		allEt = new HashMap<EditText, Questions>();
		temp = new HashMap<Questions, String>();
		for(Questions questions: qAnsMAp.keySet()){					
			qLst.add(questions);
		}

		rowArr = new TableRow[qLst.size()];
		relative = new LinearLayout[qLst.size()];
		btnLayour = new LinearLayout[qLst.size()];

		int width = getWindowManager().getDefaultDisplay().getWidth()-40;
		vp = new ViewGroup.LayoutParams(width,LayoutParams.WRAP_CONTENT);

		int widthC = getWindowManager().getDefaultDisplay().getWidth()-40;
		vp1 = new ViewGroup.LayoutParams(widthC,LayoutParams.WRAP_CONTENT);

		mreIns = new TextView[qLst.size()];


		clientNotes = new EditText[qLst.size()];
		timeBtn = new Button[qLst.size()];
		dateBtn = new Button[qLst.size()];

		if(qLst != null){
			displayQuestions();
		}
	}


	private void displayQuestions() {
		ViewGroup.LayoutParams defaultView = new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT,10);
		int size = qLst.size();
		if(y == size)return;
		SharedMethods.logError("YYYYYYYYYYYYYYYY :"+y);
		if(y == 0){
			changeCardlayout.setVisibility(View.VISIBLE);
		}else{
			changeCardlayout.setVisibility(View.GONE);
		}
		//for(int i=0;i<qLst.size();i++){
		//final int x = i;
		//if(x != y)return;

		mreIns[y] = new TextView(this);
		mreIns[y].setPadding(0, 2, 10, 0);
		btnLayour[y] = new LinearLayout(this); 
		btnLayour[y].setOrientation(LinearLayout.HORIZONTAL);
		mreIns[y].setEllipsize(null);
		//mreIns[y].setLayoutParams(vp);
		relative[y] = new LinearLayout(this);
		relative[y].setOrientation(LinearLayout.VERTICAL);
		rowArr[y] = new TableRow(this);
		final Questions q =qLst.get(y);				
		mreIns[y].setText(q.getQuestion());
		mreIns[y].setTextColor(getResources().getColor(R.color.Default_Text_Color));
		mreIns[y].setBackgroundDrawable(getResources().getDrawable(R.drawable.text_bg));
		mreIns[y].setTypeface(Typeface.DEFAULT_BOLD);
		relative[y].addView(mreIns[y]);		

		int c = size -1;
		SharedMethods.logError("Last Index : "+c + "yyy :"+ y);
		if(c == y){
			isLast = true;
			btnName = getResources().getString(R.string.NEXT);
			//btnName = getResources().getString(R.string.SUBMIT_CARD_BTN);
		}else{
			btnName = getResources().getString(R.string.NEXT);
		}

		if(q.getqType() == Constants.TYPE_SPINNER){
			ArrayList<String> answers = new ArrayList<String>();					
			LinkedList<Answers> ansList = qAnsMAp.get(q);
			RadioButton[] rd = new RadioButton[ansList.size()];
			arryRdBtnGrp = new RadioGroup(this);
			if(ansList != null && ansList.size() > 0){					
				for(int j=0;j< ansList.size();j++){
					rd[j] = new RadioButton(this);								
					Answers ans = ansList.get(j);
					rd[j].setText(ans.getAnswerText());
					rd[j].setTypeface(Typeface.DEFAULT_BOLD);
					rd[j].setTextColor(getResources().getColor(R.color.Default_Text_Color));
					arryRdBtnGrp.addView(rd[j]);
					if(questionMapping.containsKey(q)){
						if(ans.getAnswerText().equalsIgnoreCase(questionMapping.get(q)))
							rd[j].setChecked(true);
					}
					//answers.add(ans.getAnswerText());
				}
			}	
			arryRdBtnGrp.setOrientation(RadioGroup.VERTICAL);
			//arryRdBtnGrp.setLayoutParams(vp1);
			//arryRdBtnGrp.setPadding(0, 2, 10, 0);
			relative[y].addView(arryRdBtnGrp);

			arryRdBtnGrp.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					// TODO Auto-generated method stub
					int checked = group.getCheckedRadioButtonId();	
					RadioButton rdb = (RadioButton) findViewById(checked);
					//SharedMethods.logError("Radio Button"+rdb.getText().toString());
					if(questionMapping != null){
						if(questionMapping.containsKey(y)){
							questionMapping.remove(y);
						}
						questionMapping.put(q, rdb.getText().toString());
					}

					if(isLast){
						Bundle bundle = new Bundle();
						bundle.putSerializable("allEt", allEt);
						bundle.putSerializable("qAnsMap", qAnsMAp);
						bundle.putSerializable("qustionMapping", questionMapping);
						bundle.putString("CardType",cardType);
						bundle.putInt("Cardtype", cardtype);
						bundle.putString("ClientNumber", clientNumber);
						bundle.putString("ClientName",clientName);
						Intent intent = new Intent(FRSQuestions.this,ActivityCardPostView.class);
						intent.putExtras(bundle);
						com.frontrow.ui.TabManager parentActivity = (TabManager) getParent();
						parentActivity.startChild("Activity_PostView_Activity",intent);

						//sendActivityCard();
					
						return;
					}
					y++;
					questionLayout.removeAllViews();
					displayQuestions();
				}
			});


		}else if(q.getqType() == Constants.TYPE_ALPHA_NUMERIC){
			clientNotes[y] = new EditText(this);
			clientNotes[y].setSingleLine();
			clientNotes[y].setBackgroundDrawable(getResources().getDrawable(R.drawable.frs_edit_text));
			clientNotes[y].setMinHeight(45);
			clientNotes[y].setPadding(5, 0, 10, 0);

			//clientNotes[y].setHint("Testing");
			clientNotes[y].addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					// TODO Auto-generated method stub
					SharedMethods.logError("SSSSSSSSSSSSS " + s + Integer.toString(count));
					String searchTerm = s.toString();
					if(searchTerm.contains("#")){
						//searchTerm = "";
						isHash = true;
					}else{
						isHash = false;
					}
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					// TODO Auto-generated method stub

				}

				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					if(isHash){
						s.delete(s.length()-1, s.length());
					}

				}
			});
			//clientNotes[y].setLayoutParams(vp1);
			//allEt.add(clientNotes[i]);
			if(allEt != null){
				if(allEt.containsKey(clientNotes[y])){
					allEt.remove(clientNotes[y]);
				}
				//String s = (clientNotes[i].getText().toString().trim().length() == 0)?"#":clientNotes[i].getText().toString().trim();
				allEt.put(clientNotes[y], q);


			}

			if(temp != null){			
				for (Questions qs:temp.keySet()) {
					if(qs.getQuestion().equalsIgnoreCase(q.getQuestion())){
						String tx = temp.get(qs);

						clientNotes[y].setText(tx);
					}
				}
			}
			View v = new View(this);
			v.setLayoutParams(defaultView);
			relative[y].addView(v);
			relative[y].addView(clientNotes[y]);


			Button next = new Button(this);
			next.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_button));
			next.setText(btnName);
			next.setTypeface(null, Typeface.BOLD);
			//next.setPadding(0, 10, 0, 10);
			next.setTextColor(getResources().getColor(R.color.ColorPureWhite));
			next.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(temp != null){
						if(temp.containsKey(q)){
							temp.remove(q);
						}

						//clientNotes[y].setFilters(new InputFilter[]{alphaNumericFilter});
						String sq = clientNotes[y].getText().toString();

						temp.put(q,sq);				
					}

					if(isLast){
						Bundle bundle = new Bundle();
						bundle.putSerializable("allEt", allEt);
						bundle.putSerializable("qAnsMap", qAnsMAp);
						bundle.putSerializable("qustionMapping", questionMapping);
						bundle.putString("CardType",cardType);
						bundle.putInt("Cardtype", cardtype);
						bundle.putString("ClientNumber", clientNumber);
						bundle.putString("ClientName",clientName);
						Intent intent = new Intent(FRSQuestions.this,ActivityCardPostView.class);
						intent.putExtras(bundle);
						com.frontrow.ui.TabManager parentActivity = (TabManager) getParent();
						parentActivity.startChild("Activity_PostView_Activity",intent);

						//sendActivityCard();
					
						return;
					}

					y++;
					questionLayout.removeAllViews();
					displayQuestions();
				}
			});
			View v1 = new View(this);
			v1.setLayoutParams(defaultView);
			relative[y].addView(v1);
			relative[y].addView(next);

		}else if(q.getqType() == Constants.TYPE_NUMERIC ){
			clientNotes[y] = new EditText(this);
			//allEt.add(clientNotes[i]);
			clientNotes[y].setSingleLine();
			//clientNotes[y].setLayoutParams(vp1);
			clientNotes[y].setInputType(InputType.TYPE_CLASS_NUMBER);
			clientNotes[y].setImeOptions(EditorInfo.IME_ACTION_NEXT);			
			clientNotes[y].setBackgroundDrawable(getResources().getDrawable(R.drawable.frs_edit_text));
			clientNotes[y].setMinHeight(45);
			clientNotes[y].setPadding(5, 0, 10, 0);
			if(allEt != null){
				if(allEt.containsKey(clientNotes[y])){
					allEt.remove(clientNotes[y]);
				}
				//String s = (clientNotes[i].getText().toString().trim().length() == 0)?"#":clientNotes[i].getText().toString().trim();
				allEt.put(clientNotes[y], q);
			}

			if(temp != null){			
				for (Questions qs:temp.keySet()) {
					if(qs.getQuestion().equalsIgnoreCase(q.getQuestion())){
						String tx = temp.get(qs);
						clientNotes[y].setText(tx);
					}
				}
			}
			View v = new View(this);
			v.setLayoutParams(defaultView);
			relative[y].addView(v);
			relative[y].addView(clientNotes[y]);
			//relative[y].setPadding(0, 10, 0, 10);

			Button next = new Button(this);
			next.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_button));
			next.setText(btnName);
			//next.setPadding(0, 10, 0, 10);
			next.setTextColor(getResources().getColor(R.color.ColorPureWhite));
			next.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub


					if(temp != null){
						if(temp.containsKey(q)){
							temp.remove(q);
						}
						temp.put(q,clientNotes[y].getText().toString());				
					}

					if(isLast){
						Bundle bundle = new Bundle();
						bundle.putSerializable("allEt", allEt);
						bundle.putSerializable("qAnsMap", qAnsMAp);
						bundle.putSerializable("qustionMapping", questionMapping);
						bundle.putString("CardType",cardType);
						bundle.putInt("Cardtype", cardtype);
						bundle.putString("ClientNumber", clientNumber);
						bundle.putString("ClientName",clientName);
						Intent intent = new Intent(FRSQuestions.this,ActivityCardPostView.class);
						intent.putExtras(bundle);
						com.frontrow.ui.TabManager parentActivity = (TabManager) getParent();
						parentActivity.startChild("Activity_PostView_Activity",intent);

						//sendActivityCard();
					
						return;
					}

					y++;
					questionLayout.removeAllViews();
					displayQuestions();
				}
			});
			View v1 = new View(this);
			v1.setLayoutParams(defaultView);
			relative[y].addView(v1);
			relative[y].addView(next);
			//relative[y].addView(v);
		}

		else if(q.getqType() == Constants.TYPE_FORECAST ){
			clientNotes[y] = new EditText(this);
			//allEt.add(clientNotes[i]);
			//clientNotes[y].setLayoutParams(vp1);
			clientNotes[y].setSingleLine();
			clientNotes[y].setInputType(InputType.TYPE_CLASS_NUMBER);
			clientNotes[y].setImeOptions(EditorInfo.IME_ACTION_NEXT);			
			clientNotes[y].setBackgroundDrawable(getResources().getDrawable(R.drawable.frs_edit_text));
			clientNotes[y].setMinHeight(45);
			clientNotes[y].setPadding(5, 0, 10, 0);

			if(allEt != null){
				if(allEt.containsKey(clientNotes[y])){
					allEt.remove(clientNotes[y]);
				}
				//String s = (clientNotes[i].getText().toString().trim().length() == 0)?"#":clientNotes[i].getText().toString().trim();
				allEt.put(clientNotes[y], q);
			}

			if(temp != null){			
				for (Questions qs:temp.keySet()) {
					if(qs.getQuestion().equalsIgnoreCase(q.getQuestion())){
						String tx = temp.get(qs);
						clientNotes[y].setText(tx);
					}
				}
			}
			View v = new View(this);
			v.setLayoutParams(defaultView);
			relative[y].addView(v);
			relative[y].addView(clientNotes[y]);

			Button next = new Button(this);
			next.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_button));
			next.setTextColor(getResources().getColor(R.color.ColorPureWhite));
			next.setText(btnName);
			//next.setPadding(0, 10, 0, 10);
			next.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub


					if(temp != null){
						if(temp.containsKey(q)){
							temp.remove(q);
						}
						temp.put(q,clientNotes[y].getText().toString());				
					}

					if(isLast){
						Bundle bundle = new Bundle();
						bundle.putSerializable("allEt", allEt);
						bundle.putSerializable("qAnsMap", qAnsMAp);
						bundle.putSerializable("qustionMapping", questionMapping);
						bundle.putString("CardType",cardType);
						bundle.putInt("Cardtype", cardtype);
						bundle.putString("ClientNumber", clientNumber);
						bundle.putString("ClientName",clientName);
						Intent intent = new Intent(FRSQuestions.this,ActivityCardPostView.class);
						intent.putExtras(bundle);
						com.frontrow.ui.TabManager parentActivity = (TabManager) getParent();
						parentActivity.startChild("Activity_PostView_Activity",intent);

						//sendActivityCard();
					
						return;
					}

					y++;
					questionLayout.removeAllViews();
					displayQuestions();
				}
			});
			View v1 = new View(this);
			v1.setLayoutParams(defaultView);
			relative[y].addView(v1);
			relative[y].addView(next);

		}else if(q.getqType() == Constants.TYPE_DATE){
			clientNotes[y] = new EditText(this);
			//allEt.add(clientNotes[i]);			
			clientNotes[y].setLayoutParams(vp1);
			clientNotes[y].setSingleLine();
			clientNotes[y].setEnabled(false);
			clientNotes[y].setFocusable(false);			
			clientNotes[y].setBackgroundDrawable(getResources().getDrawable(R.drawable.frs_edit_text));
			clientNotes[y].setMinHeight(45);
			clientNotes[y].setPadding(5, 0, 10, 0);

			if(allEt != null){
				if(allEt.containsKey(clientNotes[y])){
					allEt.remove(clientNotes[y]);
				}
				//String s = (clientNotes[i].getText().toString().trim().length() == 0)?"#":clientNotes[i].getText().toString().trim();
				allEt.put(clientNotes[y], q);
			}

			if(temp != null){			
				for (Questions qs:temp.keySet()) {
					if(qs.getQuestion().equalsIgnoreCase(q.getQuestion())){
						String tx = temp.get(qs);
						clientNotes[y].setText(tx);
					}
				}
			}

			dateBtn[y] = new Button(this);
			dateBtn[y].setBackgroundDrawable(getResources().getDrawable(R.drawable.login_button));
			dateBtn[y].setImeOptions(EditorInfo.IME_ACTION_NEXT);
			dateBtn[y].setText("Select Date");
			dateBtn[y].setTextColor(getResources().getColor(R.color.ColorPureWhite));
			dateBtn[y].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					clientNotes[y].clearFocus();
					showDatePicker(y,q);
				}
			});
			View v = new View(this);
			v.setLayoutParams(defaultView);
			relative[y].addView(v);
			relative[y].addView(clientNotes[y]);
			//	relative[y].addView(v);
			relative[y].addView(dateBtn[y]);
			//relative[y].setPadding(0, 10, 0, 10);

			Button next = new Button(this);
			next.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_button));
			next.setText(btnName);			
			next.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(temp != null){
						if(temp.containsKey(q)){
							temp.remove(q);
						}
						temp.put(q,clientNotes[y].getText().toString());				
					}
					if(isLast){
						Bundle bundle = new Bundle();
						bundle.putSerializable("allEt", allEt);
						bundle.putSerializable("qAnsMap", qAnsMAp);
						bundle.putSerializable("qustionMapping", questionMapping);
						bundle.putString("CardType",cardType);
						bundle.putInt("Cardtype", cardtype);
						bundle.putString("ClientNumber", clientNumber);
						bundle.putString("ClientName",clientName);
						Intent intent = new Intent(FRSQuestions.this,ActivityCardPostView.class);
						intent.putExtras(bundle);
						com.frontrow.ui.TabManager parentActivity = (TabManager) getParent();
						parentActivity.startChild("Activity_PostView_Activity",intent);

						//sendActivityCard();
					
						return;
					}

					y++;
					questionLayout.removeAllViews();
					displayQuestions();
				}
			});
			View v1 = new View(this);
			v1.setLayoutParams(defaultView);
			relative[y].addView(v1);
			relative[y].addView(next);
			//relative[y].addView(v);
		}else if(q.getqType() == Constants.TYPE_DATE_TIME){
			clientNotes[y] = new EditText(this);
			//allEt.add(clientNotes[i]);
			//clientNotes[y].setLayoutParams(vp1);
			clientNotes[y].setSingleLine();
			clientNotes[y].setEnabled(false);
			clientNotes[y].setFocusable(false);
			clientNotes[y].setBackgroundDrawable(getResources().getDrawable(R.drawable.frs_edit_text));
			clientNotes[y].setMinHeight(45);
			clientNotes[y].setPadding(5, 0, 10, 0);
			if(allEt != null){
				if(allEt.containsKey(clientNotes[y])){
					allEt.remove(clientNotes[y]);
				}
				//String s = (clientNotes[i].getText().toString().trim().length() == 0)?"#":clientNotes[i].getText().toString().trim();
				allEt.put(clientNotes[y], q);
			}

			if(temp != null){			
				for (Questions qs:temp.keySet()) {
					if(qs.getQuestion().equalsIgnoreCase(q.getQuestion())){
						String tx = temp.get(qs);
						clientNotes[y].setText(tx);
					}
				}
			}
			dateBtn[y] = new Button(this);
			dateBtn[y].setBackgroundDrawable(getResources().getDrawable(R.drawable.login_button));
			dateBtn[y].setText("Select Date");
			dateBtn[y].setImeOptions(EditorInfo.IME_ACTION_NEXT);
			dateBtn[y].setTextColor(getResources().getColor(R.color.ColorPureWhite));			
			dateBtn[y].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					clientNotes[y].clearFocus();
					showDatePicker(y,q);
				}
			});

			timeBtn[y] = new Button(this);
			timeBtn[y].setBackgroundDrawable(getResources().getDrawable(R.drawable.login_button));
			timeBtn[y].setText("Select Time");
			timeBtn[y].setImeOptions(EditorInfo.IME_ACTION_NEXT);
			timeBtn[y].setTextColor(getResources().getColor(R.color.ColorPureWhite));
			timeBtn[y].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					clientNotes[y].clearFocus();
					showTimePicker(y,q);
				}
			});

			btnLayour[y].addView(dateBtn[y]);
			btnLayour[y].addView(timeBtn[y]);
			btnLayour[y].setPadding(0, 10, 0, 10);
			relative[y].addView(clientNotes[y]);
			View v = new View(this);
			v.setLayoutParams(defaultView);
			relative[y].addView(v);
			relative[y].addView(btnLayour[y]);
			//relative[y].setPadding(0, 10, 0, 10);

			Button next = new Button(this);
			next.setText(btnName);
			next.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_button));
			next.setPadding(0, 10, 0, 10);
			next.setTextColor(getResources().getColor(R.color.ColorPureWhite));
			next.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(temp != null){
						if(temp.containsKey(q)){
							temp.remove(q);
						}
						temp.put(q,clientNotes[y].getText().toString());				
					}

					if(isLast){
						Bundle bundle = new Bundle();
						bundle.putSerializable("allEt", allEt);
						bundle.putSerializable("qAnsMap", qAnsMAp);
						bundle.putSerializable("qustionMapping", questionMapping);
						bundle.putString("CardType",cardType);
						bundle.putInt("Cardtype", cardtype);
						bundle.putString("ClientNumber", clientNumber);
						bundle.putString("ClientName",clientName);
						Intent intent = new Intent(FRSQuestions.this,ActivityCardPostView.class);
						intent.putExtras(bundle);
						com.frontrow.ui.TabManager parentActivity = (TabManager) getParent();
						parentActivity.startChild("Activity_PostView_Activity",intent);

						//sendActivityCard();
					
						return;
					}
					y++;
					questionLayout.removeAllViews();
					displayQuestions();
				}
			});
			View v1 = new View(this);
			v1.setLayoutParams(defaultView);
			relative[y].addView(v1);
			relative[y].addView(next);
		}else if(q.getqType() == Constants.TYPE_COMMENT){
			clientNotes[y] = new EditText(this);
			clientNotes[y].setSingleLine();
			clientNotes[y].setBackgroundDrawable(getResources().getDrawable(R.drawable.frs_edit_text));
			clientNotes[y].setMinHeight(45);
			clientNotes[y].setPadding(5, 0, 10, 0);

			//clientNotes[y].setHint("Testing");
			clientNotes[y].addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					// TODO Auto-generated method stub
					SharedMethods.logError("SSSSSSSSSSSSS " + s + Integer.toString(count));
					String searchTerm = s.toString();
					if(searchTerm.contains("#")){
						//searchTerm = "";
						isHash = true;
					}else{
						isHash = false;
					}
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					// TODO Auto-generated method stub

				}

				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					if(isHash){
						s.delete(s.length()-1, s.length());
					}

				}
			});
			//clientNotes[y].setLayoutParams(vp1);
			//allEt.add(clientNotes[i]);
			if(allEt != null){
				if(allEt.containsKey(clientNotes[y])){
					allEt.remove(clientNotes[y]);
				}
				//String s = (clientNotes[i].getText().toString().trim().length() == 0)?"#":clientNotes[i].getText().toString().trim();
				allEt.put(clientNotes[y], q);


			}

			if(temp != null){			
				for (Questions qs:temp.keySet()) {
					if(qs.getQuestion().equalsIgnoreCase(q.getQuestion())){
						String tx = temp.get(qs);

						clientNotes[y].setText(tx);
					}
				}
			}
			View v = new View(this);
			v.setLayoutParams(defaultView);
			relative[y].addView(v);
			relative[y].addView(clientNotes[y]);


			Button next = new Button(this);
			next.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_button));
			next.setText(btnName);
			next.setTypeface(null, Typeface.BOLD);
			//next.setPadding(0, 10, 0, 10);
			next.setTextColor(getResources().getColor(R.color.ColorPureWhite));
			next.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(temp != null){
						if(temp.containsKey(q)){
							temp.remove(q);
						}

						//clientNotes[y].setFilters(new InputFilter[]{alphaNumericFilter});
						String sq = clientNotes[y].getText().toString();

						temp.put(q,sq);				
					}

					if(isLast){
						Bundle bundle = new Bundle();
						bundle.putSerializable("allEt", allEt);
						bundle.putSerializable("qAnsMap", qAnsMAp);
						bundle.putSerializable("qustionMapping", questionMapping);
						bundle.putString("CardType",cardType);
						bundle.putInt("Cardtype", cardtype);
						bundle.putString("ClientNumber", clientNumber);
						bundle.putString("ClientName",clientName);
						Intent intent = new Intent(FRSQuestions.this,ActivityCardPostView.class);
						intent.putExtras(bundle);
						com.frontrow.ui.TabManager parentActivity = (TabManager) getParent();
						parentActivity.startChild("Activity_PostView_Activity",intent);

						//sendActivityCard();
					
						return;
					}

					y++;
					questionLayout.removeAllViews();
					displayQuestions();
				}
			});
			View v1 = new View(this);
			v1.setLayoutParams(defaultView);
			relative[y].addView(v1);
			relative[y].addView(next);
		}
		this.questionLayout.addView(relative[y]);		
	}
	//}


	private void showDatePicker(final int i,final Questions q) {

		final OnDateSetListener odsl=new OnDateSetListener()
		{
			public void onDateSet(DatePicker arg0, int year, int month, int dayOfMonth) {
				// TODO Auto-generated method stub
				String m = Integer.toString(month+1);
				String dom = Integer.toString(dayOfMonth);
				m = (month+1 <=9)?"0"+m:m;
				SharedMethods.logError("MONTH : "+ m);

				dom =(dayOfMonth<=9)?"0"+dom:dom;
				String date = year+"/"+m+"/"+dom;
				if(dateTimeMap.containsKey(Constants.DATE)){
					dateTimeMap.remove(Constants.DATE);
					dateTimeMap.put(Constants.DATE, date);
				}else{
					dateTimeMap.put(Constants.DATE, date);
				}
				addDateTme();

				String tmp = clientNotes[i].getText().toString();
				//String date = year+"/"+month+"/"+dayOfMonth;
				tmp = (!dateTimeMap.containsKey(Constants.TIME))?date:date+" " +dateTimeMap.get(Constants.TIME);
				clientNotes[i].setText(tmp);
			}

		};
		//clientNotes[i].setEnabled(false);
		Calendar cal=Calendar.getInstance();
		datePickDiag=new DatePickerDialog(this.getParent(),odsl,cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));
		datePickDiag.setCancelable(false);
		datePickDiag.show();

	}

	private void addDateTme(){
		String s="";
		if(dateTimeMap.containsKey(Constants.DATE) || dateTimeMap.containsKey(Constants.TIME)){
			s = dateTimeMap.get(Constants.DATE);
			String tm = dateTimeMap.get(Constants.TIME);
			if(tm != null){
				s = s+dateTimeMap.get(Constants.TIME);
				SharedMethods.logError("Full : "+ s);
				if(s.contains(" ")){
					s = s.replace(" ", "");
				}

				SharedMethods.logError("Full1 : "+ s);
			}

			dateTimeMap.put(Constants.DATE_TIME, s);
		}
	}

	private void showTimePicker(final int i, final Questions q) {

		final OnTimeSetListener tsl = new OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				// TODO Auto-generated method stub
				String tmp = clientNotes[i].getText().toString();
				String hod = Integer.toString(hourOfDay);
				String min = Integer.toString(minute);
				hod = (hourOfDay<= 9)?"0"+hod:hod;
				min = (minute <=9)?"0"+min:min;
				String time = hod+":"+min;
				if(dateTimeMap.containsKey(Constants.TIME)){
					dateTimeMap.remove(Constants.TIME);
					dateTimeMap.put(Constants.TIME, time);
				}else{
					dateTimeMap.put(Constants.TIME, time);
				}
				addDateTme();
				Date now = new Date();
				Date alsoNow = Calendar.getInstance().getTime();
				String nowAsString = new SimpleDateFormat("yyyy/MM/dd").format(now);
				tmp = (!dateTimeMap.containsKey(Constants.DATE))?nowAsString+" "+time:dateTimeMap.get(Constants.DATE)+" "+time;
				clientNotes[i].setText(tmp);
			}
		};
		Calendar cal=Calendar.getInstance();
		timePicker = new TimePickerDialog(this.getParent(),tsl,cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE),true);
		timePicker.setCancelable(false);
		timePicker.show();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(y > 0 && y <= qLst.size()){
			y--;
			isLast = (isLast)?false:false;
			questionLayout.removeAllViews();
			displayQuestions();			
		}else{
			super.onBackPressed();
		}
		//super.onBackPressed();
	}
	
	



	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		boolean b = false;
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(y > 0 && y <= qLst.size()){
				y--;
				isLast = (isLast)?false:false;
				questionLayout.removeAllViews();
				displayQuestions();		
				b = true;
			}else{
				return super.onKeyDown(keyCode, event);
			}

		}
		return b;
		//return super.onKeyDown(keyCode, event);
	}

	private void sendActivityCard() {
		getParent().showDialog(Constants.SHOW_DIALOG);
		/* Disable Click event */
		if(allEt != null){
			Set<EditText> editSet = allEt.keySet();
			Iterator<EditText> itrSet = editSet.iterator();
			while (itrSet.hasNext()) {
				EditText type = itrSet.next();
				String s = (type.getText().toString().trim().length() == 0)?"#":type.getText().toString().trim()+"#";
				if(questionMapping != null){
					Questions qs = allEt.get(type);
					if(qs.getqType() == Constants.TYPE_DATE || qs.getqType() == Constants.TYPE_DATE_TIME){
						if(s.contains(" ")){
							s = s.replace(" ", "");
						}
					}
					questionMapping.put(qs, s);
				}
			}
		}
		LinkedHashMap<Questions, String> sortedList = SharedMethods.sortByComparator(questionMapping);
		int count = sortedList.size();

		String ansCode = "";				
		LinkedHashMap<Questions, ArrayList<Answers>> qa = null;

		Set<Questions> qs =sortedList.keySet();

		Iterator<Questions> itrQ = qs.iterator();
		while(itrQ.hasNext()){
			Questions q = itrQ.next();
			if(qAnsMAp != null){
				Set<Questions> qsTmp = qAnsMAp.keySet();					
				Iterator itrAns = qsTmp.iterator();
				OUTER:
					while(itrAns.hasNext()){
						Questions tmpQuestions = (Questions) itrAns.next();
						if(q.equals(tmpQuestions)){
							String selectedAnswer = sortedList.get(q).toString().trim();
							if(q.getqType() == Constants.TYPE_SPINNER){
								LinkedList<Answers> tmpAns = qAnsMAp.get(tmpQuestions);
								for(int i=0;i<tmpAns.size();i++){
									Answers answer = tmpAns.get(i);
									if(answer.getAnswerText().trim().equals(selectedAnswer)){
										ansCode = ansCode + answer.getAnswerCode();
										break OUTER;
									}
								}
							}else{

								if(selectedAnswer.contains("/")){
									selectedAnswer = selectedAnswer.replace("/", "");
								}
								if(selectedAnswer.contains(":")){
									selectedAnswer = selectedAnswer.replace(":", "");
								}
								ansCode = ansCode + selectedAnswer;
								break OUTER;   
							}
						}
					}
			}
		}

		SharedMethods.logError("Clinet Note :" + ansCode);
		//AnswerActivityCardDataManager.getSharedInstance().submitActivityCard(clientNumber, ansCode, "",cardType,Integer.toString(cardtype));
	}

	@Override
	public void webResponseRecievedFromDataManager(Object response) {
		// TODO Auto-generated method stub
		if(response instanceof CardSubmitResponse){
			refreshViewInTheUIThread();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		// TODO Auto-generated method stub
		if(arg0 == salesGrp){
			cardType = arg0.getItemAtPosition(position).toString();
			CardSpinner cs = cardList.get(position);
			cardtype = cs.getCardId();
			titlename.setText(cs.getCardName());
			activityCardType=cs.getCardName();
			drawFeilds(cardtype);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	private void spinnerValues() {
		//questionTypes = AuthDataManager.getSharedInstance().getCardTypes();	
		ActivityCardDB cardDb = new ActivityCardDB(this);
		//	cardDb.open();
		questionTypes = cardDb.getAllActivityCards();
		if(questionTypes != null){
			for(ActivityCard card : this.questionTypes){
				cardList.add(new CardSpinner(card.getCardType().trim(), card.getCardId()));
			}
			ArrayAdapter<CardSpinner> cardAdapter = new ArrayAdapter<CardSpinner>(this, R.layout.spinnertext, cardList);
			cardAdapter.setDropDownViewResource(R.layout.spinnerdropdown);
			salesGrp.setAdapter(cardAdapter);
			salesGrp.setSelected(true);
		}
	}

}
