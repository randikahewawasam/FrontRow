package com.frontrow.common;


public class Constants {

	public static final String APP_DEBUG_TAG = "FRONTROW";
	public static final String BASIC = "Basic ";
	public static final String AUTHORIZATION = "Authorization";
	public static final String VALUE_SEPERATOR = "|";
	public static final String SEPERATOR_PIPE = "|";
	public static final String VAL_CONNECTOR = "/";


	/* Network related */
	public static final int NETWORK_SUCCESS = 200;
	public static final int NETWORK_NOT_AVAILABLE = 701;
	public static final int CONNECT_TO_SERVER_ERROR =-1;
	public static final int NETWORK_PARSE_ERROR = 407;
	public static final int UNAUTHORIZE = 401;
	public static boolean isNetworkAailable;
	public static final String NETWORK_STATUS = "FRSNetwork";
	public static final String NATIVE_MAP_PATH = "http://maps.google.com/maps?";
	public static final String SADDR= "saddr=";
	public static final String DADDR = "daddr=";


	/* Bundle Values */
	public static final	String USER_NAME= "UserName";

	public static final String SUBMIT_TYPE = "messages/message";
	public static final String CLIENT_TYPE = "clients/client";
	public static final String GET_TERMS = "getterms";
	public static final String SEARCH_TERM = "clients";

	public static final String OFFLINE_MSG_CLIENT_TYPE = "AddClient";
	public static final String OFFLINE_MSG_SUBMIT_TYPE = "SubmitText";

	public static final String CLIENT_NUMBER = "ClientNumber";
	public static final String CARD_TYPE ="CardType";
	public static final String CLIENT_ID = "ClientID";
	public static final String CLIENT_NAME = "ClientName";


	/* CardSource Attributes */
	public static final int DEFAULT_CARD = 0;
	public static final int CLIENT_CARD = 1;
	public static final int USER_CARD = 2;


	/* Shared Preferences */
	public static final String AUTH_PREF = "UserPreferance";
	public static final String APP_STATE = "AppState";
	public static final byte SAVE_CREDENTIALS = 1;
	public static final byte LOAD_CREDENTIALS = 2;
	public static final byte REMOVE_CREDENTIALS = 3;


	/* Login Credentials msgs */

	public static final String COMPANY_NAME = "CompanyName";
	public static final String MOBILE_NO = "MobileNo";
	public static final String PASSWORD = "Password";
	public static final String USER_ID = "UserId";
	public static final String USERNAME = "UserName";
	public static final String USER_TOKEN = "Token";
	public static final String IS_SAVED = "IsSaved";
	public static final String IS_SINGLE_VIEW = "IsSingle";

	/* Message Status */

	public static final int FAILED = 1;
	public static final int PENDING =2;
	public static final int SUCCESS = 3;
	public static final String MESSAGE = "Message";
	public static final String UPDATE = "Update";
	public static final String NEW_CLIENT = "New Client";

	/* Progress Dialog*/
	public static final int SHOW_DIALOG = 2;
	public static final int DISMISS_DIALOG  =3;
	public static final int UPDATE_ALERT = 4;
	public static final int SEARCH_MORE = 5;

	/* Device Dencity */
	public static float SMALL_DENCITY = 120;
	public static float NORMAL_DENCITY = 160;
	public static float HIGH_DENCITY = 240;

	/* Field Types */

	public static final int TYPE_SPINNER = 1;
	public static final int TYPE_NUMERIC = 2;
	public static final int TYPE_ALPHA_NUMERIC = 3;
	public static final int TYPE_FORECAST = 4;
	public static final int TYPE_DATE = 5;
	public static final int TYPE_DATE_TIME =6;
	public static final int TYPE_COMMENT = 7;

	/*Date Time Constants */
	public static final String DATE = "Date";
	public static final String TIME ="Time";
	public static final String DATE_TIME = "DateTime";
	
	/*Frs Questions Calendar */
	public static final String FOLLOW_UP = "Follow Up";
	

	/* Calendar Event */
	public static final long ONE_HOUR = 3600000;
	
	/*Parceble Key */
	public static final String PARCEBLE_KEY ="FRS"; 

	/* Activity Card All */
	public static final String ALL_ACTIVITY_CARD = "*All";

	/* Terms Keys */

	public static final String ACTIVITY_CARD_KEY ="Activity Card";
	public static final String FIELD_AGENT_KEY ="Field Agent";
	public static final String REGION_KEY ="Region";
	public static final String CLIENT_KEY = "Client";
	public static final String CARD_KEY = "Card";

	public static final String MARKET_URL = "market://details?id=com.frontrow.ui";
	public static final String APP_USR ="AppUsr";

	/* TabMenu */
	public static final String ACTIVITY_TYPE = "Activity";

	/* referance */
	public static final int MESSAGES_REF = 1;
	public static final int CLIENTS_REF = 2;
	public static final int SETTINGS_REF = 3;

	/* Note */
	public static final String NOTE_ID = "NoteID";
	public static final String NOTE_TEXT = "NoteText";
	
	/* Address */
	public static final String ADDRESS_ID="AddressID";
	public static final String ADDRESS_TXT ="AddressText";
	
	public static final String PHONE_ID="PhoneID";
	public static final String PHONE_TXT ="PhoneText";

	/* Alert Dialog Types */
	public static final int ERROR_DIALOG = 1;
	public static final String ERROR_D ="Error";
	public static final String TYPE ="Type";
	public static final String NAVIGATE_TO_PREVIOUS_PAGE_TYPE ="NavigateType";
	public static final int ADD_NOTE_DIALOG =6;
	public static final int SHOW_CONTACTS_PHONE=7;
	public static final int LOCATION_SETTINGS_DIALOG=8;
	public static final String CONTACT="Contact";
	public static final String GENARAL_ERROR_TYPE ="GenaralErrorType";
	public static final String LOG_OUT_TYPE="LogOutType";
	public static final String CARD_SUBMIT_TYPE="CardSubmitType";
	public static final String APP_EXIT_TYPE="AppExitType";
	public static final String PROGRESS_MSG="ProMsg";
	public static final String DELETE_CONTACT_TYPE="DeleteContactType";
	public static final String DELETE_ADDRESS_TYPE="DeleteAddressType";
	public static final String DELETE_PHONE_TYPE="DeletePhoneType";
	/* Contact List Button Type */
	public static final int CALL_BTN = 1;
	public static final int NUMBERS_BTN=2;
	public static final int MAP_BTN=3;
	public static final int DIRECTION_BTN=4;
	public static final int EMAIL_BTN=5;

	public static ContactType contactType;

	public static enum ContactType{
		Initial,
		Edit_Contact,
		Add_Contact,
		Delete_Contact
	}
	
	public static AddressType addressType; 
	
	public static enum AddressType{
		Edit_Address,
		Add_Address,
		Delete_Address
	}
	
	public static PhoneType phoneType; 
	
	public static enum PhoneType{
		Edit_Phone,
		Add_Phone,
		Delete_Phone
	}
	/*Contacts Values */
	public static final String CONTACT_ID = "ContactId";
	public static final String CONATCT = "Contact";
	public static final String COMPOSITE_CONTACT = "CompositeContact";

	/*Contact Style*/
	public static final String NEW_CONTACT_RECORD="NewContactRecord"; 

	/* log file */
	public static final String FILENAME="log_file.txt"; 
	public static final String LOG_EMAIL_ADDRESS=" support@frontrow-solutions.com";
	//public static final String LOG_EMAIL_ADDRESS="sudeepa.m31@gmail.com";

	/* Orientation Handle */
	public static final String INFLATE_PROGRESS = "Progress";
	public static final String INFLATE_ERROR = "Error";

	public static final String IS_SHOW_MORE ="IsShowMore";

	/* Web service method signatures */
	public static final String CONATCT_ADD = "compositecontacts/compositecontact";
	public static final String CONTACT_DELETE ="contacts/contact/";
	public static final String ADDRESS_ADD = "addresses/address";
	public static final String PHONE_ADD = "phones/phone";
	public static final String PHONE_DELETE = "phones/phone/";
	public static final String SAVE_LOCATION ="addresses/savelocation";

	/* Base URL */
	//public static final String BASE_URL ="http://beta3.frslogin.com/SalesProMobileServiceTestv1.5/MobileService.svc/api/v1/";
	//public static final String BASE_URL ="http://beta3.frslogin.com/SalesProServiceSoori/MobileService.svc/api/v1/";
	public static final String BASE_URL ="http://beta3.frslogin.com/SalesProService_v2.1/MobileService.svc/api/v1/";
	//http://beta3.frslogin.com/SalesProServiceSoori/MobileService.svc
	//[9:51:14 PM] Sooriyakumar Rajendra: http://beta3.frslogin.com/SalesProServiceSoori/MobileService.svc
	
	/* Relationship Types */
	public static final String PHONE_RELATIONSHIP = "Phone";
	public static final String ADDRESS_RELATIONSHIP ="Address";
	public static final String CONTACT_RELATIONSHIP ="Contact";
}
