package com.frontrow.android.db;

import com.frontrow.common.SharedMethods;

import android.R.string;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FrontRowDBHelper extends SQLiteOpenHelper{
	public final static int DB_VERTION = 6;
	public final static String DATABASE_NAME = "FrontRow";
	protected static SQLiteDatabase sqlDb;
	protected static Context context;
	protected static FrontRowDBHelper manager;
	public final static String LOCALE = "Locale";

	private static final String USER_DATABASE_CREATE = "create table "+UserDB.TABLE+"("+UserDB.USER_ID+" integer primary key, "
			+UserDB.USER_NAME+ " text not null, "+ UserDB.COMPANY_CODE+ " text not null, "+ UserDB.MOBILE_NO+" text not null,"+ UserDB.USER_DEF_CARD_TYPE+ " text not null,"+ UserDB.USER_DEF_CARD_ID+" integer not null," +UserDB.LANGUAGE_ID+" integer,"+UserDB.CARD_SOURCE+" int,"+UserDB.CARDOVERRIDE+" bit,"+UserDB.ROCLIENT+" bit,"+UserDB.ALL_CLIENTS+" bit,"+UserDB.CONTACT_STYLE +" bit );";

	private static final String CLIENT_DATABASE_CREATE = "create table "+ClientDB.TABLE+"("+ClientDB.ID+" integer primary key autoincrement, "+ClientDB.CLIENT_ID+" integer not null,"+ ClientDB.CARD_TYPE+ " text not null, "+ ClientDB.CLIENT_NAME +" text not null, "+ ClientDB.CLIENT_NUMBER+" text not null unique,  "
			+ClientDB.USER_ID+" integer not null,"+ ClientDB.CLIENT_CARD_ID+" integer not null,"+ ClientDB.POSTAL+" text,"+ClientDB.PROVINCE+" text,"+ ClientDB.CITY +" text,"+ClientDB.STREET+" text,"+ClientDB.MOBILENUMBER+" text,"+ClientDB.LAN+" REAL,"+ClientDB.LAT+" REAL,"+ ClientDB.ADDRESSID+" integer ,FOREIGN KEY("+ClientDB.USER_ID+") REFERENCES "+UserDB.TABLE+ "("+UserDB.USER_ID +"));";

	private static final String ANSWERS_DATABASE_CREATE = "create table "+AnswersDB.TABLE+"("+AnswersDB.ANSWER_ID+" integer primary key autoincrement, "
			+AnswersDB.ANSWER_TEXT+ " text not null, "+AnswersDB.ANSWER_CODE+" text not null,"+AnswersDB.QUESTION_ID+" integer not null, FOREIGN KEY ("+AnswersDB.QUESTION_ID+") REFERENCES "+ QuestionDB.TABLE+ "("+ QuestionDB.QUESTION_ID +"));";

	private static final String ACTIVITY_CARD_DATABASE_CREATE = "create table "+ActivityCardDB.TABLE+"("+ActivityCardDB.CARD_TYPE_ID+" integer primary key, "
			+ActivityCardDB.CARD_NAME+ " text not null);";

	private static final String QUESTIONS_DATABASE_CREATE = "create table "+QuestionDB.TABLE+"("+QuestionDB._ID+" integer primary key autoincrement, "
			+QuestionDB.QUESTION_ID+" integer not null, "+QuestionDB.QUESTION_SEQUENCE+" integer, "
			+QuestionDB.QUESTION_NAME+ " text not null,"+QuestionDB.QUESTION_TYPE+" integer not null," +QuestionDB.CARD_TYPE_ID+ " integer not null, FOREIGN KEY("+QuestionDB.CARD_TYPE_ID+") REFERENCES "+ActivityCardDB.TABLE+ "("+ActivityCardDB.CARD_TYPE_ID +"));";

	private static final String MESSAGES_DATABASE_CREATE = "create table "+ MessagesDB.TABLE+"("+MessagesDB.ID+" integer primary key autoincrement, "
			+MessagesDB.MESSAGE+ " text not null,"+MessagesDB.SUBMIT_TYPE+" text not null,"+ MessagesDB.TYPE+" text not null,"+ MessagesDB.CLIENT_NAME+" text not null,"+ MessagesDB.STATUS+" integer not null,"+MessagesDB.DATE+" DATE DEFAULT (datetime('now','localtime')),"
			+ MessagesDB.USR_ID+" integer not null, FOREIGN KEY("+MessagesDB.USR_ID+") REFERENCES "+ UserDB.TABLE+ "("+UserDB.USER_ID +"));";


	private static final String TERMS_DATABASE_CREATE = "create table "+ FRSTermsDB.TABLE +"("+FRSTermsDB._id+" integer primary key autoincrement, "
			+ FRSTermsDB.DESCRIPTION +" text not null,"+ FRSTermsDB.LANGUAGE_ID+" integer,"+ FRSTermsDB.TERM+" text not null,"
			+ FRSTermsDB.COMPANY_CODE+" text not null);";
	
	private static final String CLIENT_ACCOUNT_TYPE_DATABASE_CREATE = "create table "+ClientAccountTypeDB.TABLE+"("+ClientAccountTypeDB.CLIENT_TYPE_ID+" integer primary key, "
			+ClientAccountTypeDB.TYPE+ " text not null,"+ClientAccountTypeDB.COLOR+" text not null,"+ ClientAccountTypeDB.COMPANY_CODE+" text not null);";
	
	private static final String RELATIONSHIP_TYPES_DATABASE_CREATE = "create table "+RelationshipTypesDB.TABLE+"("+RelationshipTypesDB._ID+" integer primary key AUTOINCREMENT, "
			+RelationshipTypesDB.RELATIONSHIP_TYPE+ " text not null,"+RelationshipTypesDB.RELATIONSHIP_TYPE_TEXT+" text not null,"+ RelationshipTypesDB.RELATIONSHIP_TYPE_ID+" integer not null,"+ RelationshipTypesDB.LANGUAGE_ID+" integer not null);";
	
	//ClientAccountTypeDB.TEST+ " text"+ 
	private static final String DUMMY_TABLE = "create table "+ LOCALE +"("+"locale"+" TEXT DEFAULT 'en_US');";

	/* DB insert queries */
	protected String USER_QUERY = "INSERT INTO "+ UserDB.TABLE+" ("+ UserDB.USER_ID+", "+ UserDB.USER_NAME +", "+ UserDB.COMPANY_CODE +", "+ UserDB.MOBILE_NO +", "+
			UserDB.USER_DEF_CARD_TYPE+", "+ UserDB.USER_DEF_CARD_ID +", "+ UserDB.LANGUAGE_ID +", "+ UserDB.CARD_SOURCE +", "+ UserDB.CARDOVERRIDE+", "+UserDB.ROCLIENT +", "+UserDB.ALL_CLIENTS+","+ UserDB.CONTACT_STYLE+" ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

	protected String CLIENT_QUERY = "INSERT INTO "+ ClientDB.TABLE+" ("+ ClientDB.CLIENT_ID + ", "+ClientDB.CARD_TYPE +", "+ ClientDB.CLIENT_NAME+", "+
			ClientDB.CLIENT_NUMBER+", "+ClientDB.USER_ID+", "+ClientDB.CLIENT_CARD_ID+", "+ClientDB.POSTAL+", "+ClientDB.PROVINCE+", "+ClientDB.CITY+","+ClientDB.STREET+","+ClientDB.MOBILENUMBER+","+ClientDB.LAN+","+ClientDB.LAT+","+ClientDB.ADDRESSID+") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	protected String ANSWERS_QUERY = "INSERT INTO "+ AnswersDB.TABLE+" ("+ AnswersDB.ANSWER_TEXT+", "+AnswersDB.ANSWER_CODE+", "+AnswersDB.QUESTION_ID+") VALUES (?, ?, ?)";

	protected String ACTIVITY_CARD_QUERY = "INSERT INTO "+ ActivityCardDB.TABLE +"("+ ActivityCardDB.CARD_TYPE_ID+", "+ActivityCardDB.CARD_NAME +") VALUES (?, ?)";
	
	protected String CLIENT_ACCOUNT_TYPE_QUERY = "INSERT INTO "+ ClientAccountTypeDB.TABLE +"("+ ClientAccountTypeDB.CLIENT_TYPE_ID+", "+ ClientAccountTypeDB.TYPE +", "+ClientAccountTypeDB.COLOR +" , "+ ClientAccountTypeDB.COMPANY_CODE +") VALUES (?, ?,?,?)";

	protected String QUESTION_QUERY = "INSERT INTO "+ QuestionDB.TABLE +"("+ QuestionDB.QUESTION_ID +", "+ QuestionDB.QUESTION_SEQUENCE +", "+
			QuestionDB.QUESTION_NAME+", "+QuestionDB.QUESTION_TYPE+","+ QuestionDB.CARD_TYPE_ID +") VALUES (?,?,?,?,?)";

	protected String MESSAGE_QUERY = "INSERT INTO "+ MessagesDB.TABLE +"("+ MessagesDB.MESSAGE +", "+ MessagesDB.SUBMIT_TYPE +", "+
			MessagesDB.TYPE +", "+ MessagesDB.STATUS +", "+MessagesDB.DATE +") VALUES (?,?,?,?)";

	protected String TERMS_QUERY = "INSERT INTO "+ FRSTermsDB.TABLE +"("+ FRSTermsDB.DESCRIPTION +", "+ FRSTermsDB.LANGUAGE_ID +", "+
			FRSTermsDB.TERM +", "+ FRSTermsDB.COMPANY_CODE +") VALUES (?,?,?,?)";
	
	protected String RELATIONSHIP_TYPE_QUERY = "INSERT INTO "+ RelationshipTypesDB.TABLE +"("+ RelationshipTypesDB.RELATIONSHIP_TYPE+", "+ RelationshipTypesDB.RELATIONSHIP_TYPE_TEXT +", "+
			RelationshipTypesDB.RELATIONSHIP_TYPE_ID +","+ RelationshipTypesDB.LANGUAGE_ID +") VALUES (?,?,?,?)";

	protected String QUESTION_DELETE_QUERY = "DELETE FROM "+ QuestionDB.TABLE ;

	protected String CLIENT_DELETE_QUERY = "DELETE FROM "+ ClientDB.TABLE;

	protected String TERMS_DELETE_QUERY = "DELETE FROM "+ FRSTermsDB.TABLE;
	
	protected String CLIENT_ACCOUNT_TYPE_DELETE_QUERY ="DELETE FROM "+ ClientAccountTypeDB.TABLE;
	
	protected String RELATIONSHIP_TYPE_DELETE_QUERY ="DELETE FROM "+ RelationshipTypesDB.TABLE;
	
	protected String LOCALE_QUERY = "INSERT INTO "+ LOCALE +" VALUES ('en_US')";

	/* End Of SQL Queries */
	public static FrontRowDBHelper getSharedInstance(Context ctx){
		SharedMethods.logError("DB2 :"+ ctx);
		context = ctx;
		if(manager == null){
			manager = new FrontRowDBHelper();
		}		
		return manager;
	}    

	/*	public synchronized void open() {	
		if(sqlDb == null){
			sqlDb = getWritableDatabase();

			SharedMethods.logError("DB HElper instance NUll");
		}else{
			SharedMethods.logError("DB HElper instance NOT NUll");
		}
	}*/


	public FrontRowDBHelper() {
		// TODO Auto-generated constructor stub
		super(context, DATABASE_NAME, null, DB_VERTION);
		SharedMethods.logError("DB1 :"+ context);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		SharedMethods.logError("DB :"+ db);
		// TODO Auto-generated method stub
		db.execSQL(USER_DATABASE_CREATE);
		db.execSQL(CLIENT_DATABASE_CREATE);
		db.execSQL(ANSWERS_DATABASE_CREATE);
		db.execSQL(QUESTIONS_DATABASE_CREATE);
		db.execSQL(ACTIVITY_CARD_DATABASE_CREATE);
		db.execSQL(MESSAGES_DATABASE_CREATE);
		db.execSQL(TERMS_DATABASE_CREATE);
		db.execSQL(CLIENT_ACCOUNT_TYPE_DATABASE_CREATE);
		db.execSQL(RELATIONSHIP_TYPES_DATABASE_CREATE);
		//db.execSQL(DUMMY_TABLE);
		//db.execSQL(LOCALE_QUERY);
		SharedMethods.logError("DB Created");
		/* Foreign key triggers 
		db.execSQL("CREATE TRIGGER fk_usrdept_deptid " +
				" BEFORE INSERT "+
				" ON "+ClientDB.TABLE+				
				" FOR EACH ROW BEGIN"+
				" SELECT CASE WHEN ((SELECT "+UserDB.USER_ID+" FROM "+UserDB.TABLE+" WHERE "+UserDB.USER_ID+"=new."+ClientDB.CLIENT_CARD_ID+" ) IS NULL)"+
				" THEN RAISE (ABORT,'Foreign Key Violation') END;"+
				"  END;");

		db.execSQL("CREATE TRIGGER fk_ansdept_deptid " +
				" BEFORE INSERT "+
				" ON "+AnswersDB.TABLE+				
				" FOR EACH ROW BEGIN"+
				" SELECT CASE WHEN ((SELECT "+QuestionDB.QUESTION_ID+" FROM "+AnswersDB.TABLE+" WHERE "+QuestionDB.QUESTION_ID+"=new."+AnswersDB.QUESTION_ID+" ) IS NULL)"+
				" THEN RAISE (ABORT,'Foreign Key Violation') END;"+
				"  END;");

		db.execSQL("CREATE TRIGGER fk_qusdept_deptid " +
				" BEFORE INSERT "+
				" ON "+QuestionDB.TABLE+				
				" FOR EACH ROW BEGIN"+
				" SELECT CASE WHEN ((SELECT "+ActivityCardDB.CARD_TYPE_ID+" FROM "+QuestionDB.TABLE+" WHERE "+ActivityCardDB.CARD_TYPE_ID+"=new."+QuestionDB.CARD_TYPE_ID+" ) IS NULL)"+
				" THEN RAISE (ABORT,'Foreign Key Violation') END;"+
				"  END;");

		db.execSQL("CREATE TRIGGER fk_msgdept_deptid " +
				" BEFORE INSERT "+
				" ON "+MessagesDB.TABLE+				
				" FOR EACH ROW BEGIN"+
				" SELECT CASE WHEN ((SELECT "+UserDB.USER_ID+" FROM "+MessagesDB.TABLE+" WHERE "+UserDB.USER_ID+"=new."+MessagesDB.USR_ID+" ) IS NULL)"+
				" THEN RAISE (ABORT,'Foreign Key Violation') END;"+
				"  END;");
		 */
		/* applying foreign key this works Android 2.2 onwards  */
		db.equals("PRAGMA synchronous = OFF;");
		db.execSQL("PRAGMA foreign_keys = ON;");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		SharedMethods.logError("DB2 :"+ context);
		db.execSQL("DROP TABLE IF EXISTS "+UserDB.TABLE);
		db.execSQL("DROP TABLE IF EXISTS "+ClientDB.TABLE);
		db.execSQL("DROP TABLE IF EXISTS "+ActivityCardDB.TABLE);
		db.execSQL("DROP TABLE IF EXISTS "+QuestionDB.TABLE);
		db.execSQL("DROP TABLE IF EXISTS "+AnswersDB.TABLE);
		db.execSQL("DROP TABLE IF EXISTS "+MessagesDB.TABLE);
		db.execSQL("DROP TABLE IF EXISTS "+FRSTermsDB.TABLE);
		db.execSQL("DROP TABLE IF EXISTS "+ClientAccountTypeDB.TABLE);
		db.execSQL("DROP TABLE IF EXISTS "+RelationshipTypesDB.TABLE);

		onCreate(db);
	}


	public static Context getContext() {
		return context;
	}


	public static void setContext(Context context) {
		FrontRowDBHelper.context = context;
	}


	public SQLiteDatabase getSqlDb() {
		return sqlDb;
	}


	public void setSqlDb(SQLiteDatabase sqlDb) {
		this.sqlDb = sqlDb;
	}

	@Override
	public synchronized void close() {
		// TODO Auto-generated method stub
		/*if(sqlDb.isOpen())
			sqlDb.close();*/
	}

}
