package com.frontrow.android.db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import com.frontrow.common.SharedMethods;
import com.row.mix.beans.Answers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils.InsertHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class AnswersDB extends FrontRowDBHelper{

	public static final String ANSWER_ID = "AnswerId";
	public static final String ANSWER_TEXT = "AnswerText";
	public static final String QUESTION_ID = "QuestionId";
	public static final String ANSWER_CODE = "AnswerCode";

	public static AnswersDB instance;

	/* Table Name*/
	public final static String TABLE ="Answers";

	/*	public static AnswersDB getSharedInstance(){
		if(instance == null){
			instance = new AnswersDB();
		}
		return instance;
	}*/


	/*	@Override
	public void open() {
		// TODO Auto-generated method stub
		super.open();
	}*/

	public AnswersDB(Context ctx) {
		// TODO Auto-generated constructor stub
		super();
	}



	public synchronized long addAnswers(ArrayList<Answers> answerList,int questionId){
		long count =-2;		
		SQLiteDatabase db= this.getWritableDatabase();
		db.beginTransaction();
		SQLiteStatement statement = db.compileStatement(this.ANSWERS_QUERY);
		try{
			for(Answers answers : answerList){
				statement.bindString(1, answers.getAnswerText());
				statement.bindString(2, answers.getAnswerCode());
				statement.bindLong(3, questionId);
				statement.execute();
				statement.clearBindings();
			}
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
			db.close();
		}
		return count;
	}

	public void addAllAnswers(ArrayList<ArrayList<Answers>> ansList){
		System.out.println("Answers Start : "+ System.currentTimeMillis());
		SQLiteDatabase db= this.getWritableDatabase();
		db.beginTransaction();
		SQLiteStatement statement = db.compileStatement(this.ANSWERS_QUERY);
		try{
			for (int i = 0;i< ansList.size();i++) {
				ArrayList<Answers> lst = ansList.get(i);
				for(Answers answers:lst){
					statement.bindString(1, answers.getAnswerText());
					statement.bindString(2, answers.getAnswerCode());
					statement.bindLong(3, answers.getQuestionId());
					statement.execute();
					statement.clearBindings();
				}
			}
			db.setTransactionSuccessful();
		}catch (Exception e) {
			// TODO: handle exception
			SharedMethods.logError(e.getMessage());
		}finally{
			db.endTransaction();
			db.close();
		}
		System.out.println("Answers End : "+ System.currentTimeMillis());
	}

	public void isAnsweresAvailable(){
		long count = 0;
		boolean b = false;
		Cursor cursor = this.getWritableDatabase().query(TABLE, null, null, null, null, null, null);
		if(cursor != null){
			if(cursor.moveToNext()){
				count = this.getWritableDatabase().delete(TABLE, null, null);
				SharedMethods.logError("Deleted Answers : "+ Long.toString(count));
			}
		}	
		cursor.close();
	}

	public LinkedList<Answers> getAnswersForQuestion(int qId){
		LinkedList<Answers> anslist = new LinkedList<Answers>();
		SQLiteDatabase db= this.getWritableDatabase();
		Cursor cursor = db.query(TABLE, null, QUESTION_ID+"=?", new String[]{Integer.toString(qId)}, null, null, null);
		if(cursor != null){
			if(cursor.moveToNext()){
				do {
					Answers ans = new Answers();
					ans.setAnswerText(cursor.getString(cursor.getColumnIndex(ANSWER_TEXT)));
					ans.setAnswerCode(cursor.getString(cursor.getColumnIndex(ANSWER_CODE)));
					anslist.add(ans);
				} while (cursor.moveToNext());
			}
		}
		db.close();
		cursor.close();
		return anslist;
	}

	public long deleteData(){
		SQLiteDatabase db= this.getWritableDatabase();
		long count = db.delete(TABLE, null, null);
		db.close();
		return count;
	}
}
