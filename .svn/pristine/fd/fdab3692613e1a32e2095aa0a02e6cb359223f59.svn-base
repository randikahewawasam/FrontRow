package com.frontrow.android.db;

import java.util.ArrayList;
import java.util.HashMap;

import com.frontrow.core.ApplicationUser;
import com.row.mix.beans.ActivityCard;
import com.row.mix.beans.ClientAccountType;
import com.row.mix.beans.Terms;
import com.row.mix.beans.User;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class ClientAccountTypeDB extends FrontRowDBHelper {
	
	
	public static final String COLOR = "Color";
	public static final String CLIENT_TYPE_ID = "ClientTypeId";
	public static final String TYPE ="Type";
	public static final String _id = "ID";
	public final static String TABLE ="ClientAccountType";
	public static final String COMPANY_CODE = "CompanyCode";
	
	
	private Context context;
	
	public ClientAccountTypeDB(Context ctx) {
		// TODO Auto-generated constructor stub
		super();
		//android.os.Debug.waitForDebugger();
		//this.context = ctx;
	}
	
	public void addClientAccountType(ArrayList<ClientAccountType> clientTypeList,User usr) {
		System.out.println("Terms StartUp : "+ System.currentTimeMillis());
		
		deleteClientAccountTypes();
			SQLiteDatabase db= this.getWritableDatabase();
			try{
				db.beginTransaction();
				SQLiteStatement statement = db.compileStatement(this.CLIENT_ACCOUNT_TYPE_QUERY);
				for(ClientAccountType cat:clientTypeList){
					statement.bindLong(1, cat.getId());
					statement.bindString(2, cat.getType());
					statement.bindString(3, cat.getColor());
					statement.bindString(4, usr.getCompanyId());
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

	public void deleteClientAccountTypes(){
		SQLiteDatabase db= this.getWritableDatabase();
		db.beginTransaction();
		SQLiteStatement statement = db.compileStatement(this.CLIENT_ACCOUNT_TYPE_DELETE_QUERY);
		statement.execute();
		statement.clearBindings();
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
	}



	public ArrayList<ClientAccountType> getAllClientAccountTypes(){
		ArrayList<ClientAccountType> allClientTypes = new ArrayList<ClientAccountType>();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.query(TABLE, null, null, null, null, null, null);
		if(cursor != null){
			if(cursor.moveToNext()){
				do {
					ClientAccountType type = new ClientAccountType();
					type.setId(cursor.getInt(cursor.getColumnIndex(CLIENT_TYPE_ID)));
					type.setType(cursor.getString(cursor.getColumnIndex(TYPE)));
					type.setColor(cursor.getString(cursor.getColumnIndex(COLOR)));
					allClientTypes.add(type);
				} while (cursor.moveToNext());
			}
		}
		db.close();
		cursor.close();
		return allClientTypes;
	}

}
