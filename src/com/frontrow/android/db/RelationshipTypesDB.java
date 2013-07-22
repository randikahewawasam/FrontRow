package com.frontrow.android.db;

import java.util.ArrayList;
import java.util.LinkedList;

import com.row.mix.beans.Answers;
import com.row.mix.beans.ClientAccountType;
import com.row.mix.beans.ContactRelationship;
import com.row.mix.beans.User;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class RelationshipTypesDB extends FrontRowDBHelper {
	public static final String RELATIONSHIP_TYPE_TEXT = "RealtionshipTypeText";
	public static final String RELATIONSHIP_TYPE = "RelationshipType";
	public static final String RELATIONSHIP_TYPE_ID = "RelationshipTypeId";
	public static final String LANGUAGE_ID = "LanguageId";
	public static final String _ID = "ID";

	private static QuestionDB instance;
	public final static String TABLE ="RelationshipTypes";
	private Context context;



	public RelationshipTypesDB(Context ctx) {
		// TODO Auto-generated constructor stub
		super();
		this.context = ctx;
	}
	
	
	public void addClientAccountType(ArrayList<ContactRelationship> relationshipTypeList,String type,Boolean initial) {
		System.out.println("Terms StartUp : "+ System.currentTimeMillis());
		if (initial){
		deleteRelationshipTypes();
		}
			SQLiteDatabase db= this.getWritableDatabase();
			try{
				db.beginTransaction();
				SQLiteStatement statement = db.compileStatement(this.RELATIONSHIP_TYPE_QUERY);
				for(ContactRelationship car:relationshipTypeList){
					statement.bindString(1, type);
					statement.bindString(2, car.getRelationshipText());
					statement.bindLong(3, car.getId());
					statement.bindLong(4, car.getLanguageId());
					statement.execute();
					statement.clearBindings();
				}
				db.setTransactionSuccessful();
			}catch (Exception e) {
				// TODO: handle exception
			}finally{
				db.endTransaction();
				db.close();
			}
		
		System.out.println("Terms End : "+ System.currentTimeMillis());
	}
	
	public void deleteRelationshipTypes(){
		SQLiteDatabase db= this.getWritableDatabase();
		db.beginTransaction();
		SQLiteStatement statement = db.compileStatement(this.RELATIONSHIP_TYPE_DELETE_QUERY);
		statement.execute();
		statement.clearBindings();
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
	}
	
	public ArrayList<ContactRelationship> getRelationshipTypes(String qId){
		ArrayList<ContactRelationship> relList = new ArrayList<ContactRelationship>();
		SQLiteDatabase db= this.getWritableDatabase();
		Cursor cursor = db.query(TABLE, null, RELATIONSHIP_TYPE+"=?", new String[]{qId}, null, null, null);
		//Cursor cursor = db.query(TABLE, null, null, null, null, null, null);
		if(cursor != null){
			if(cursor.moveToNext()){
				do {
					ContactRelationship ans = new ContactRelationship();
					ans.setId(cursor.getInt(cursor.getColumnIndex(RELATIONSHIP_TYPE_ID)));
					ans.setRelationshipText(cursor.getString(cursor.getColumnIndex(RELATIONSHIP_TYPE_TEXT)));
					ans.setLanguageId(cursor.getInt(cursor.getColumnIndex(LANGUAGE_ID)));
					relList.add(ans);
				} while (cursor.moveToNext());
			}
		}
		db.close();
		cursor.close();
		return relList;
	}

}
