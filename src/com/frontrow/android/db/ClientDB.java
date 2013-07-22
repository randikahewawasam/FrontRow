package com.frontrow.android.db;

import java.util.ArrayList;
import com.frontrow.common.Constants;
import com.frontrow.common.SharedMethods;
import com.frontrow.core.ApplicationUser;
import com.row.mix.beans.Address;
import com.row.mix.beans.Client;
import com.row.mix.beans.ContactRelationship;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils.InsertHelper;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class ClientDB extends FrontRowDBHelper{

	public static final String CARD_TYPE = "CardType";
	public static final String CLIENT_NAME = "ClientName";
	public static final String CLIENT_NUMBER = "ClientNumber";
	public static final String CLIENT_ID = "ClientId";
	public static final String ID = "_id";
	public static final String USER_ID = "UserId";
	public final static String TABLE = "Client";
	public static final String CLIENT_CARD_ID = "ClientCardId";
	public static final String POSTAL = "Postal";
	public static final String PROVINCE = "Province";
	public static final String CITY = "City";
	public static final String NUMBER = "Number";
	public static final String STREET = "Street";
	public static final String MOBILENUMBER = "Mobilenumber";
	public static final String LAN = "Lan";
	public static final String LAT = "Lat";
	public static final String ADDRESSID ="AddressId";

	public static ClientDB instance;

	/*	public static ClientDB getSharedInstance(){
		if(instance == null){
			instance = new ClientDB();
		}
		return instance;
	}*/


	public ClientDB(Context ctx) {
		// TODO Auto-generated constructor stub 
		super(); 
	}

	public long addClients(ArrayList<Client> clientList,int usrId){
		System.out.println("Client StartUp : "+ System.currentTimeMillis());
		long count = 0;
		isClientsAvailable();
		SQLiteDatabase db= this.getWritableDatabase();
		db.beginTransaction();
		SQLiteStatement statement = db.compileStatement(this.CLIENT_QUERY);
		try{
			for(Client client : clientList){

				statement.bindString(2, client.getCardtype());
				statement.bindLong(6, client.getCardId());
				statement.bindLong(1, client.getClientId());
				statement.bindString(3, client.getClientName());
				statement.bindString(4, client.getClientNumber());
				statement.bindLong(5, usrId);
				if (client.getAddress()!=null)
				{
				if (client.getAddress().getPostal()!=null){
				statement.bindString(7, client.getAddress().getPostal());
				}
				else{
					statement.bindString(7, null);
				}
				if(client.getAddress().getProvince()!=null){
				statement.bindString(8, client.getAddress().getProvince());
				}
				else
				{
					statement.bindString(8, null);
				}
				if(client.getAddress().getCity()!=null){
				statement.bindString(9, client.getAddress().getCity());
				}
				else{
					statement.bindString(9, null);	
				}
				if(client.getAddress().getStreet()!=null){
				statement.bindString(10, client.getAddress().getStreet());
				}
				else
				{
					statement.bindString(10, null);	
				}
				if (client.getMobilenumber()!=null){
				statement.bindString(11, client.getMobilenumber());
				}
				else{
					statement.bindString(11, null);					
				}
				statement.bindDouble(12, client.getAddress().getLon());
				statement.bindDouble(13, client.getAddress().getLat());
				statement.bindDouble(14, client.getAddress().getId());
				}
				else{
					
					statement.bindString(7, null);
					statement.bindString(8, null);
					statement.bindString(9, null);
					statement.bindString(10, null);
					statement.bindString(11, null);
					statement.bindDouble(12, 0);
					statement.bindDouble(13, 0);
					statement.bindDouble(14, 0);
					
					
				}
				statement.execute();
				statement.clearBindings();
			}
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
			db.close();
		}
		System.out.println("Client End : "+ System.currentTimeMillis());
		return count;
	}
	
	public long addClient(Client client,int usrId){
		System.out.println("Client StartUp : "+ System.currentTimeMillis());
		long count = 0;
	
		SQLiteDatabase db= this.getWritableDatabase();
		db.beginTransaction();
		SQLiteStatement statement = db.compileStatement(this.CLIENT_QUERY);
		try{
			
                if ( client.getCardtype()!= null) {
                	statement.bindString(2, client.getCardtype());
                }
                else
                {
                	statement.bindString(2,"");
                }
				statement.bindLong(6, client.getCardId());
				statement.bindLong(1, client.getClientId());
				statement.bindString(3, client.getClientName());
				statement.bindString(4, client.getClientNumber());
				statement.bindLong(5, usrId);
				if (client.getAddress()!=null)
				{
				if (client.getAddress().getPostal()!=null){
				statement.bindString(7, client.getAddress().getPostal());
				}
				else{
					statement.bindString(7, "");
				}
				if(client.getAddress().getProvince()!=null){
				statement.bindString(8, client.getAddress().getProvince());
				}
				else
				{
					statement.bindString(8, "");
				}
				if(client.getAddress().getCity()!=null){
				statement.bindString(9, client.getAddress().getCity());
				}
				else{
					statement.bindString(9, "");	
				}
				if(client.getAddress().getStreet()!=null){
				statement.bindString(10, client.getAddress().getStreet());
				}
				else
				{
					statement.bindString(10, "");	
				}
				if (client.getMobilenumber()!=null){
				statement.bindString(11, client.getMobilenumber());
				}
				else{
					statement.bindString(11, "");					
				}
				statement.bindDouble(12, client.getAddress().getLon());
				statement.bindDouble(13, client.getAddress().getLat());
				statement.bindDouble(14, client.getAddress().getId());
				}
				else{
					
					statement.bindString(7, "");
					statement.bindString(8, "");
					statement.bindString(9, "");
					statement.bindString(10, "");
					statement.bindString(11, "");
					statement.bindDouble(12, 0);
					statement.bindDouble(13, 0);
					statement.bindDouble(14, 0);
					
					
				}
				statement.execute();
				statement.clearBindings();
			
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
			db.close();
		}
		System.out.println("Client End : "+ System.currentTimeMillis());
		return count;
	}
	
	
	
	public void updateClientsLocation(Address address,int clientid,int usrId){
		System.out.println("Client StartUp : "+ System.currentTimeMillis());
		long count = 0;
		//isClientsAvailable();
		SQLiteDatabase db= this.getWritableDatabase();
		//db.beginTransaction();
		//ArrayList<Client> clientList = getAllClinets(usrId);
		SQLiteStatement statement = db.compileStatement("UPDATE "+TABLE+" SET "+ LAN +"=" +address.getLon() +", "+ LAT +"="+address.getLat() +" WHERE "+CLIENT_ID+"="+clientid +";");
		try{
			
		statement.execute();
			
			
    	//db.setTransactionSuccessful();
		}
		catch(SQLException e)
		{
			SharedMethods.logError(e.getMessage());
		}
		finally{
			//db.endTransaction();
			db.close();
		}
		System.out.println("Client End : "+ System.currentTimeMillis());
		ArrayList<Client> clientList2 = getAllClinets(usrId);
		count =5;
	}

	public synchronized void isClientsAvailable(){
		boolean b= false;
		Cursor cursor = this.getWritableDatabase().query(TABLE, null, null, null, null, null, null);
		if(cursor != null){
			if(cursor.moveToNext()){
				SQLiteDatabase db= this.getWritableDatabase();
				db.beginTransaction();
				SQLiteStatement statement = db.compileStatement(this.CLIENT_DELETE_QUERY);
				statement.execute();
				statement.clearBindings();
				db.setTransactionSuccessful();
				db.endTransaction();
				db.close();
				//this.getWritableDatabase().delete(TABLE, null, null);
			}
		}
		cursor.close();
	}

	public ArrayList<Client> getAllClinets(int usrId){
		SQLiteDatabase db= this.getWritableDatabase();
		Cursor cursor = db.query(TABLE, null, USER_ID+"=?", new String[]{Integer.toString(usrId)}, null, null, CLIENT_NAME+ " COLLATE NOCASE ASC");
		ArrayList<Client> clientList = new ArrayList<Client>(); 
		if(cursor != null){
			if(cursor.moveToNext()){
				do {
					Client client =  new Client();
					String cardType = cursor.getString(cursor.getColumnIndex(CARD_TYPE));
					String clientName = cursor.getString(cursor.getColumnIndex(CLIENT_NAME));
					String clientNumber = cursor.getString(cursor.getColumnIndex(CLIENT_NUMBER));
					int clientId = cursor.getInt(cursor.getColumnIndex(CLIENT_ID));
					int cardId = cursor.getInt(cursor.getColumnIndex(CLIENT_CARD_ID));
					String street = cursor.getString(cursor.getColumnIndex(STREET));
					String mobilenumber = cursor.getString(cursor.getColumnIndex(MOBILENUMBER));
					String postal = cursor.getString(cursor.getColumnIndex(POSTAL));
					String city = cursor.getString(cursor.getColumnIndex(CITY));
					String province = cursor.getString(cursor.getColumnIndex(PROVINCE));
					double lat=cursor.getDouble(cursor.getColumnIndex(LAT));
					double lan=cursor.getDouble(cursor.getColumnIndex(LAN));
					int addressid=cursor.getInt(cursor.getColumnIndex(ADDRESSID));
					client.setCardtype(cardType);
					client.setClientName(clientName);
					client.setClientNumber(clientNumber);
					client.setClientId(clientId);
					client.setCardId(cardId);
					Address address= new Address();
					address.setStreet(street);
					client.setMobilenumber(mobilenumber);
					address.setCity(city);
					address.setPostal(postal);
					address.setProvince(province);
					address.setLat(lat);
					address.setLon(lan);
					address.setId(addressid);
					client.setAddress(address);
					clientList.add(client);
					
				} while (cursor.moveToNext());
			}
		}
		db.close();
		cursor.close();
		return clientList;
	}

	public ArrayList<Client> getClientsForActivityCard(String cType,int usrId){
		ArrayList<Client> clientList = new ArrayList<Client>(); 
		SQLiteDatabase db= this.getWritableDatabase();
		String searchSql = (cType.equals(Constants.ALL_ACTIVITY_CARD))?"SELECT * FROM "+ClientDB.TABLE+" ORDER BY "+ ClientDB.CLIENT_NAME+" COLLATE NOCASE ASC;":"SELECT * FROM "
				+ClientDB.TABLE+" WHERE "+ClientDB.CARD_TYPE+"= '"+cType+"' AND "+ ClientDB.USER_ID+"= '"+Integer.toString(usrId)+"' ORDER BY "+ ClientDB.CLIENT_NAME+" COLLATE NOCASE ASC";
		//String searchSql = 
		SharedMethods.logError("SQL : "+searchSql);
		Cursor clientCursor = db.rawQuery(searchSql, null);
		if(clientCursor != null){
			if(clientCursor.moveToNext()){
				do {
					Client client =  new Client();
					String cardType = clientCursor.getString(clientCursor.getColumnIndex(CARD_TYPE));
					String clientName = clientCursor.getString(clientCursor.getColumnIndex(CLIENT_NAME));
					String clientNumber = clientCursor.getString(clientCursor.getColumnIndex(CLIENT_NUMBER));
					int clientId = clientCursor.getInt(clientCursor.getColumnIndex(CLIENT_ID));
					int cardId = clientCursor.getInt(clientCursor.getColumnIndex(CLIENT_CARD_ID));
					String postal = clientCursor.getString(clientCursor.getColumnIndex(POSTAL));
					String province = clientCursor.getString(clientCursor.getColumnIndex(PROVINCE));
					String city = clientCursor.getString(clientCursor.getColumnIndex(CITY));
					String street = clientCursor.getString(clientCursor.getColumnIndex(STREET));
					String mobilenumber = clientCursor.getString(clientCursor.getColumnIndex(MOBILENUMBER));
					double lat=clientCursor.getDouble(clientCursor.getColumnIndex(LAT));
					double lan=clientCursor.getDouble(clientCursor.getColumnIndex(LAN));
					int addressid = clientCursor.getInt(clientCursor.getColumnIndex(ADDRESSID));
					client.setCardtype(cardType);
					client.setClientName(clientName);
					client.setClientNumber(clientNumber);
					client.setClientId(clientId);
					client.setCardId(cardId);
					Address address = new Address();
					address.setPostal(postal);
					address.setProvince(province);
					address.setCity(city);
					address.setStreet(street);
					client.setMobilenumber(mobilenumber);
					address.setLat(lat);
					address.setLon(lan);
					address.setId(addressid);
					client.setAddress(address);
					clientList.add(client);

				} while (clientCursor.moveToNext());
			}
		}
		db.close();
		clientCursor.close();
		return clientList;
	}
	
	public void updateClient(Client client,int usrId){
		System.out.println("Client StartUp : "+ System.currentTimeMillis());
		long count = 0;
		//isClientsAvailable();
		SQLiteDatabase db= this.getWritableDatabase();
		//db.beginTransaction();
		//ArrayList<Client> clientList = getAllClinets(usrId);
		SQLiteStatement statement = db.compileStatement("UPDATE "+TABLE+" SET "+ CLIENT_ID +"=" +client.getClientId() +" WHERE "+CLIENT_NUMBER+"='"+client.getClientNumber() +"';");
		try{
			
		statement.execute();
		
			
		//Cursor cursor = db.query(TABLE, null, CLIENT_NUMBER+"=?", new String[]{client.getNumber()}, null, null, null);
		//Cursor cursor = db.query(TABLE, null, null, null, null, null, null);

		}
		catch(SQLException e)
		{
			SharedMethods.logError(e.getMessage());
		}
		finally{
			//db.endTransaction();
			db.close();
		}
		
		System.out.println("Client End : "+ System.currentTimeMillis());
		ArrayList<Client> clientList2 = getAllClinets(usrId);
		count =5;
	}
	
	public void deleteClient(Client client,int usrId){
		System.out.println("Client StartUp : "+ System.currentTimeMillis());
		long count = 0;
		//isClientsAvailable();
		SQLiteDatabase db= this.getWritableDatabase();
		//db.beginTransaction();
		//ArrayList<Client> clientList = getAllClinets(usrId);
		SQLiteStatement statement = db.compileStatement(" DELETE FROM "+TABLE+" WHERE "+CLIENT_NUMBER+"='"+client.getClientNumber() +"';");
		try{
			
		statement.execute();
			
			
    	//db.setTransactionSuccessful();
		}
		catch(SQLException e)
		{
			SharedMethods.logError(e.getMessage());
		}
		finally{
			//db.endTransaction();
			db.close();
		}
		System.out.println("Client End : "+ System.currentTimeMillis());
		ArrayList<Client> clientList2 = getAllClinets(usrId);
		count =5;
	}
	
	public boolean isClientAlreadyExist(String cnum,int usrId)
	{
		SQLiteDatabase db= this.getWritableDatabase();
		Cursor cursor = db.query(TABLE, null, USER_ID+"=?", new String[]{Integer.toString(usrId)}, null, null, CLIENT_NAME+ " COLLATE NOCASE ASC");
		ArrayList<Client> clientList = new ArrayList<Client>(); 
		if(cursor != null){
			if(cursor.moveToNext()){
				do {
					Client client =  new Client();
					String cardType = cursor.getString(cursor.getColumnIndex(CARD_TYPE));
					String clientName = cursor.getString(cursor.getColumnIndex(CLIENT_NAME));
					String clientNumber = cursor.getString(cursor.getColumnIndex(CLIENT_NUMBER));
					int clientId = cursor.getInt(cursor.getColumnIndex(CLIENT_ID));
					int cardId = cursor.getInt(cursor.getColumnIndex(CLIENT_CARD_ID));
					String street = cursor.getString(cursor.getColumnIndex(STREET));
					String mobilenumber = cursor.getString(cursor.getColumnIndex(MOBILENUMBER));
					String postal = cursor.getString(cursor.getColumnIndex(POSTAL));
					String city = cursor.getString(cursor.getColumnIndex(CITY));
					String province = cursor.getString(cursor.getColumnIndex(PROVINCE));
					double lat=cursor.getDouble(cursor.getColumnIndex(LAT));
					double lan=cursor.getDouble(cursor.getColumnIndex(LAN));
					int addressid=cursor.getInt(cursor.getColumnIndex(ADDRESSID));
					client.setCardtype(cardType);
					client.setClientName(clientName);
					client.setClientNumber(clientNumber);
					client.setClientId(clientId);
					client.setCardId(cardId);
					Address address= new Address();
					address.setStreet(street);
					client.setMobilenumber(mobilenumber);
					address.setCity(city);
					address.setPostal(postal);
					address.setProvince(province);
					address.setLat(lat);
					address.setLon(lan);
					address.setId(addressid);
					client.setAddress(address);
					clientList.add(client);
					
				} while (cursor.moveToNext());
			}
		}
		db.close();
		cursor.close();
		Boolean avl =false;
		for (int j = 0; j < clientList.size(); j++) {
			Client client = clientList.get(j);
			if (client.getClientNumber().trim()
					.equalsIgnoreCase(cnum.trim())) {
				avl = true;
			}
		}
		return avl;
		
	}
}
