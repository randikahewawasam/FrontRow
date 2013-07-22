package com.frontrow.android.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;

import com.frontrow.common.SharedMethods;
import com.row.mix.beans.ActivityCard;
import com.row.mix.beans.Answers;
import com.row.mix.beans.Questions;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils.InsertHelper;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.webkit.WebChromeClient.CustomViewCallback;

public class QuestionDB extends FrontRowDBHelper{

	public static final String QUESTION_NAME = "QuestionName";
	public static final String QUESTION_ID = "QuestionId";
	public static final String CARD_TYPE_ID = "CardTypeId";
	public static final String QUESTION_TYPE = "QuestionType";
	public static final String QUESTION_SEQUENCE ="QuenstionSequence";
	public static final String _ID = "ID";

	private static QuestionDB instance;
	public final static String TABLE ="Questions";
	private Context context;

	/*	public static QuestionDB getSharedInstance(){
		if(instance == null){
			instance = new QuestionDB();
		}
		return instance;
	}

	@Override
	public void open() {
		// TODO Auto-generated method stub
		super.open();
	}*/

	public QuestionDB(Context ctx) {
		// TODO Auto-generated constructor stub
		super();
		this.context = ctx;
	}

	public long addQuestions(LinkedHashMap<ActivityCard, LinkedHashMap<Questions, ArrayList<Answers>>> questAns) {
		System.out.println("Question StartUp : "+ System.currentTimeMillis());
		long count =-2;
		int question_id = 0;
		deleteData();
		ArrayList<ArrayList<Answers>> totAnswerList = new ArrayList<ArrayList<Answers>>();

		AnswersDB ansDb = new AnswersDB(this.context);
		ansDb.deleteData();
		//ansDb.isAnsweresAvailable();
		SQLiteDatabase db= this.getWritableDatabase();

		SQLiteStatement statement = db.compileStatement(this.QUESTION_QUERY);		
		try{
			for(ActivityCard cards:questAns.keySet()){
				LinkedHashMap<Questions, ArrayList<Answers>>qAns = questAns.get(cards);
				int questionId ;
				for(Questions ques:qAns.keySet()){
					db.beginTransaction();
					ArrayList<Answers> ans = qAns.get(ques);
					totAnswerList.add(ans);
					questionId = ques.getQuestionId();					
					statement.bindString(3, ques.getQuestion());
					statement.bindLong(1, questionId);
					statement.bindLong(5, cards.getCardId());
					statement.bindLong(2, ques.getSequence());
					statement.bindLong(4, ques.getqType());

					try {
						//count = db.insert(TABLE, null, content);
						statement.execute();
						statement.clearBindings();
						db.setTransactionSuccessful();

					} catch (SQLException e) {
						// TODO: handle exception
						SharedMethods.logError("SQL  : "+ e.getMessage());
					}
					db.endTransaction();
					ansDb.addAnswers(ans,questionId);
				}
			}


			/*	Set<ActivityCard> cards = questAns.keySet();
		Iterator<ActivityCard> itr = cards.iterator();
		while (itr.hasNext()) {
			ActivityCard card = itr.next();
			LinkedHashMap<Questions, ArrayList<Answers>>qAns = questAns.get(card);
			Set<Questions> q = qAns.keySet();
			Iterator<Questions> qItr = q.iterator();
			int questionId ;

			while (qItr.hasNext()) {
				db.beginTransaction();
				Questions questions =qItr.next();
				ArrayList<Answers> ans = qAns.get(questions);

				totAnswerList.add(ans);
				//ins.prepareForInsert();
				questionId = questions.getQuestionId();

				statement.bindString(3, questions.getQuestion());
				statement.bindLong(1, questionId);
				statement.bindLong(5, card.getCardId());
				statement.bindLong(2, questions.getSequence());
				statement.bindLong(4, questions.getqType());

				try {
					//count = db.insert(TABLE, null, content);
					statement.execute();
					statement.clearBindings();
					db.setTransactionSuccessful();

				} catch (SQLException e) {
					// TODO: handle exception
					SharedMethods.logError("SQL  : "+ e.getMessage());
				}
				db.endTransaction();				
				//ansDb.addAnswers(ans,questionId);
			}		
		}*/
			System.out.println("Question End : "+ System.currentTimeMillis());
			//ansDb.addAllAnswers(totAnswerList);
			//db.setTransactionSuccessful();
		}catch (Exception e) {
			// TODO: handle exception
			SharedMethods.logError("EXPT :"+ e.getMessage());
		}
		finally{
			db.close();
		}
		System.out.println("Question tot End : "+ System.currentTimeMillis());
		return count;
	}

	public LinkedHashMap<Questions, LinkedList<Answers>> getQuestionsForSelectedCard(int cardId){
		LinkedHashMap<Questions, LinkedList<Answers>> questionAnsMap = new LinkedHashMap<Questions, LinkedList<Answers>>();
		SQLiteDatabase db= this.getWritableDatabase();
		Cursor cursor = db.query(TABLE, null, CARD_TYPE_ID+"=?", new String[]{Integer.toString(cardId)}, null, null, null);
		if(cursor != null){
			if(cursor.moveToNext()){
				do {
					Questions question = new Questions();
					question.setQuestion(cursor.getString(cursor.getColumnIndex(QUESTION_NAME)));
					question.setqType(cursor.getInt(cursor.getColumnIndex(QUESTION_TYPE)));
					question.setSequence(cursor.getInt(cursor.getColumnIndex(QUESTION_SEQUENCE)));
					LinkedList<Answers> tempAns = new AnswersDB(context).getAnswersForQuestion(cursor.getInt(cursor.getColumnIndex(QUESTION_ID)));
					questionAnsMap.put(question, tempAns);
				} while (cursor.moveToNext());
			}
		}
		db.close();
		cursor.close();
		return questionAnsMap;
	}

	public long deleteData(){
		SQLiteDatabase db= this.getWritableDatabase();
		db.beginTransaction();
		SQLiteStatement statement = db.compileStatement(this.QUESTION_DELETE_QUERY);
		statement.execute();
		statement.clearBindings();
		db.setTransactionSuccessful();
		db.endTransaction();
		//long count = db.delete(TABLE, null, null);
		db.close();
		return 0;
	}
}
