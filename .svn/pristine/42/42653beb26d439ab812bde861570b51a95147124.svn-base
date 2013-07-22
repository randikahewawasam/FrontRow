package com.frontrow.android.db;

import java.util.LinkedList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.frontrow.common.Constants;
import com.frontrow.common.SharedMethods;
import com.frontrow.datamanagers.AnswerActivityCardDataManager;
import com.row.mix.beans.OfflineM;

public class MessagesDB extends FrontRowDBHelper{

	public static final String ID = "_id";
	public static final String USR_ID = "UserId";
	public static final String MESSAGE = "Message";
	public static final String STATUS = "Status";
	public static final String DATE = "Date";
	public static final String TYPE = "Type";
	public static final String SUBMIT_TYPE = "SubmitType";
	public static final String CLIENT_NAME ="ClientName";

	public static final String TABLE = "MessageT";
	public static MessagesDB instance;

	/*	public static MessagesDB getSharedInstance(){
		if(instance == null){
			instance = new MessagesDB();
		}
		return instance;
	}
	 */
	/*@Override
	public void open() {
		// TODO Auto-generated method stub
		super.open();
	}
	 */

	public MessagesDB(Context ctx) {
		// TODO Auto-generated constructor stub
		super();
	}

	public long addOfflineMessages(OfflineM msg, int usrId){
		long count = -2;		
		ContentValues values = new ContentValues();
		SQLiteDatabase db= this.getWritableDatabase();
		SharedMethods.logError("Saved Message : "+ msg.getMessage());
		values.put(MESSAGE, msg.getMessage());
		values.put(USR_ID, usrId);
		values.put(STATUS, msg.getStatus());
		SharedMethods.logError("Time : "+ Long.toString(msg.getDateMillies()));
		values.put(CLIENT_NAME, msg.getClientName());
		values.put(TYPE, msg.getType());
		values.put(SUBMIT_TYPE, msg.getSubmitType());
		count  = db.insert(TABLE, null, values);
		SharedMethods.logError("COunt "+Long.toString(count));
		db.close();
		return count;		
	}

	public LinkedList<OfflineM> getAllMessagesForUser(int usrId, int status) {
		LinkedList<OfflineM> msgList = new LinkedList<OfflineM>();
		if(this != null){
			SQLiteDatabase db= this.getWritableDatabase();
			Cursor cursor = db.query(TABLE, null, USR_ID+"=? AND "+ STATUS+"=?", new String[]{Integer.toString(usrId),Integer.toString(status)}, null, null, DATE+" DESC");
			if(cursor != null){
				if(cursor.moveToNext()){
					do {
						OfflineM msg = new OfflineM();
						msg.setMessage(cursor.getString(cursor.getColumnIndex(MESSAGE)));
						msg.setDateMillies(cursor.getInt(cursor.getColumnIndex(DATE)));
						msg.setDateTime(cursor.getString(cursor.getColumnIndex(DATE)));
						msg.setSubmitType(cursor.getString(cursor.getColumnIndex(SUBMIT_TYPE)));
						msg.setStatus(cursor.getInt(cursor.getColumnIndex(STATUS)));
						msg.setType(cursor.getString(cursor.getColumnIndex(TYPE)));
						msg.setId(cursor.getInt(cursor.getColumnIndex(ID)));
						msg.setClientName(cursor.getString(cursor.getColumnIndex(CLIENT_NAME)));
						msgList.add(msg);
					} while (cursor.moveToNext());				
				}
			}
			db.close();
			cursor.close();			
		}
		return msgList;
	}

	public long deleteList(int usrId){
		SQLiteDatabase db= this.getWritableDatabase();
		long count = db.delete(TABLE, USR_ID+"=?"+" AND "+STATUS+"=?", new String[]{Integer.toString(usrId),Integer.toString(Constants.PENDING)});
		db.close();
		return count;
	}

	public synchronized long deleteList(int usrId,int id){
		SQLiteDatabase db= this.getWritableDatabase();
		long count = db.delete(TABLE, USR_ID+"=?"+" AND "+STATUS+"=?"+" AND "+ID+"=?" , new String[]{Integer.toString(usrId),Integer.toString(Constants.PENDING), Integer.toString(id)});
		db.close();
		return count;
	}


	public LinkedList<OfflineM> getAllMessages(int usrId){
		LinkedList<OfflineM> msgList = new LinkedList<OfflineM>();
		Cursor cursor = null;
		SQLiteDatabase db= this.getWritableDatabase();
		if(db != null){
			cursor = db.query(TABLE, null, USR_ID+"=?", new String[]{Integer.toString(usrId)}, null, null, DATE+" DESC");
			if(cursor != null){
				if(cursor.moveToNext()){
					do {						
						OfflineM msg = new OfflineM();
						msg.setMessage(cursor.getString(cursor.getColumnIndex(MESSAGE)));
						msg.setDateMillies(cursor.getInt(cursor.getColumnIndex(DATE)));
						msg.setDateTime(cursor.getString(cursor.getColumnIndex(DATE)));
						msg.setSubmitType(cursor.getString(cursor.getColumnIndex(SUBMIT_TYPE)));
						msg.setStatus(cursor.getInt(cursor.getColumnIndex(STATUS)));
						msg.setType(cursor.getString(cursor.getColumnIndex(TYPE)));
						msg.setId(cursor.getInt(cursor.getColumnIndex(ID)));
						msg.setClientName(cursor.getString(cursor.getColumnIndex(CLIENT_NAME)));
						msgList.add(msg);
					} while (cursor.moveToNext());				
				}
			}
		}

		db.close();
		cursor.close();
		return msgList;
	}


}
