package com.frontrow.android.db;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.frontrow.common.Constants;
import com.frontrow.common.SharedMethods;
import com.frontrow.core.ApplicationUser;
import com.row.mix.beans.Terms;
import com.row.mix.beans.User;

public class FRSTermsDB extends FrontRowDBHelper{

	public static final String DESCRIPTION = "Description";
	public static final String LANGUAGE_ID = "LanguageId";
	public static final String COMPANY_CODE = "CompanyCode";
	public static final String TERM ="Term";
	public static final String _id = "ID";

	public final static String TABLE ="Terms";
	private Context context;

	public static FRSTermsDB instance;

	public FRSTermsDB(Context ctx) {
		// TODO Auto-generated constructor stub
		super();
		//android.os.Debug.waitForDebugger();
		//this.context = ctx;
	}

	public void addTerms(ArrayList<Terms> termList,User usr) {
		System.out.println("Terms StartUp : "+ System.currentTimeMillis());
		if(!isTermsAvailableForCompany(usr.getCompanyId())){
			deleteTerms();
			SQLiteDatabase db= this.getWritableDatabase();
			try{
				db.beginTransaction();
				SQLiteStatement statement = db.compileStatement(this.TERMS_QUERY);
				for(Terms terms:termList){
					statement.bindString(1, terms.description);
					statement.bindLong(2, terms.languageId);
					statement.bindString(3, terms.term);
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
		}
		System.out.println("Terms End : "+ System.currentTimeMillis());
	}

	public void deleteTerms(){
		SQLiteDatabase db= this.getWritableDatabase();
		db.beginTransaction();
		SQLiteStatement statement = db.compileStatement(this.TERMS_DELETE_QUERY);
		statement.execute();
		statement.clearBindings();
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
	}

	public boolean isTermsAvailableForCompany(String compID){
		String cmp = (compID == null)?ApplicationUser.getSharedInstance().getCompanyId():compID;
		SQLiteDatabase db= this.getWritableDatabase();
		/*String searchSql = "SELECT * FROM "+FRSTermsDB.TABLE+" WHERE "+FRSTermsDB.COMPANY_CODE+"= '"+cmp+"'";
		SharedMethods.logError("SQL : "+searchSql);
		Cursor termCursor = db.rawQuery(searchSql, null);
		if(termCursor != null){
			if(termCursor.moveToNext()){
				db.close();
				termCursor.close();
				return true;
			}
		}
		db.close();
		termCursor.close();*/



		Cursor usrCursor = db.query(TABLE, null, COMPANY_CODE+"=?", new String[]{cmp}, null, null, null);
		if(usrCursor!= null){
			if(usrCursor.moveToNext()){
				db.close();
				usrCursor.close();
				return true;
			}
		}
		db.close();
		usrCursor.close();
		return false;
	}

	public HashMap<String, String> getAllTermsForCompanyID(String companyCode) {
		System.out.println("TErmes : "+ companyCode);
		HashMap<String, String> allTerms = new HashMap<String, String>();
		SQLiteDatabase db= this.getWritableDatabase();
		String id = ApplicationUser.getSharedInstance().getCompanyId();
		System.out.println("TErmes ID : "+ companyCode);
		Cursor termCursor = db.query(TABLE, null, COMPANY_CODE + "=? ",new String[]{companyCode} , null, null, null, null);
		/*String searchSql = "SELECT * FROM "+FRSTermsDB.TABLE+" WHERE "+FRSTermsDB.COMPANY_CODE+"= '"+ApplicationUser.getSharedInstance().getCompanyId()+"'";
		Cursor termCursor = db.rawQuery(searchSql, null);*/
		if(termCursor != null){
			if(termCursor.moveToNext()){
				do {
					String desc = termCursor.getString(termCursor.getColumnIndex(DESCRIPTION));
					String term = termCursor.getString(termCursor.getColumnIndex(TERM));
					allTerms.put(desc, term);
				} while (termCursor.moveToNext());
			}
		}
		db.close();
		termCursor.close();
		System.out.println("TErmes End : "+ System.currentTimeMillis());
		return allTerms;
	}
}
