package com.frontrow.ui;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;



import com.frontrow.base.BaseActivity;
import com.frontrow.common.Constants;
import com.frontrow.common.SharedMethods;
import com.frontrow.datamanagers.ClientsNoteDataManager;
import com.frontrow.datamanagers.CustomFieldDataManager;
import com.frontrow.language.LanguageManager;
import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;
import com.memobile.connection.AsyncHttpClient;
import com.memobile.connection.ResponseHandler;
import com.row.mix.beans.ClientsNotes;
import com.row.mix.response.ClientsNotesResponse;
import com.row.mix.response.CustomFieldResponse;
import com.row.mix.response.NewClientResponse;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.ViewStub;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FRSClientsNoteDescription extends BaseActivity {
	private Button editbtn;
	private EditText edittext;
	private ClientsNotes newnote;
	private Object response; 
	private ClientsNotes tempNote;
	private String clientName;
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	setContentView(R.layout.activity_frsclients_note_description);
    	super.onCreate(savedInstanceState);
      
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_frsclients_note_description, menu);
        return true;
    }

  
    
	@Override
	protected void initializeUI() {
		editbtn =(Button) findViewById(R.id.btnnoteedit);
		edittext=(EditText) findViewById(R.id.notedesc);
		edittext.setEnabled(false);
		Bundle bundle = getIntent().getExtras();
		if(bundle != null){
			 int noteid =bundle.getInt(Constants.NOTE_ID);
			 newnote =(ClientsNotes) bundle.getSerializable(Constants.NOTE_TEXT);
			 edittext.setText(newnote.getNotetext());
				clientName=bundle.getString(Constants.CLIENT_NAME);
		}
		
		
	
		editbtn.setOnClickListener(new OnClickListener()
		    {

				@Override
				public void onClick(View v) {
					if (editbtn.getText().equals("Edit"))
					{	
					editbtn.setText("Save");
					edittext.setEnabled(true);
					}
					else{
						getParent().showDialog(Constants.SHOW_DIALOG);
						
						//  JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
						newnote.setNotetext(edittext.getText().toString());
							 tempNote= new ClientsNotes();
							tempNote.setDatetimestamp(newnote.getDatetimestamp());
							tempNote.setLastchanged(newnote.getLastchanged());
							tempNote.setId(newnote.getId());
							tempNote.setNotetext(newnote.getNotetext());
							tempNote.setClientId(newnote.getClientId());
							tempNote.setUserId(newnote.getUserId());
							tempNote.setCompanyCode(appUsr.getCompanyId());
							Gson gson=new Gson();
							String jstring=gson.toJson(tempNote);
		  				//String notstring=parameters();
					   	  AsyncHttpClient httpClient = new AsyncHttpClient(Constants.BASE_URL+"notes/note");
					  		httpClient.setBasicAuthToken(appUsr.getToken());
					  		httpClient.setMethod("PUT");
					  		httpClient.put(FRSClientsNoteDescription.this, null, null, new ResponseHandler(){
					  			@Override
					  			public void onSuccess(String successMsg) {
					  				// TODO Auto-generated method stub
					  				super.onSuccess(successMsg);
					  				ClientsNotesResponse authRes = new ClientsNotesResponse(successMsg, 200, "Success");
					  				refreshView(200, "",authRes);
					  			}

					  			@Override
					  			public void onFailed(String errorMsg, int errorCode) {
					  				// TODO Auto-generated method stub
					  				super.onFailed(errorMsg, errorCode);
					  			}

					  			@Override
					  			public void onFailure(Throwable error, String content) {
					  				// TODO Auto-generated method stub
					  				super.onFailure(error, content);
					  				ClientsNotesResponse authRes = new ClientsNotesResponse(content, 400, getResources().getString(R.string.NETWOK_UNREACHABLE));
					  				
					  				refreshView(400, getResources().getString(R.string.NETWOK_UNREACHABLE),authRes);
					  			}
					  			
					  			
					  			@Override
								public void onFailure(int statusCode, String content) {
									// TODO Auto-generated method stub
									super.onFailure(statusCode, content);
									ClientsNotesResponse authRes = new ClientsNotesResponse(content, statusCode, getResources().getString(R.string.NETWOK_UNREACHABLE));
									refreshView(statusCode, getResources().getString(R.string.NETWOK_UNREACHABLE),authRes);
								}
					  		},jstring);	
						
					}
				}
				
				
		
		    });

		  }
	
	private void refreshView(int code,String msg,ClientsNotesResponse authRes) {
		//ClientsNoteDataManager.getSharedInstance().setClientsNote((authRes));	
		
		response = authRes;
		refreshViewInTheUIThread();
	}
	

	@Override
	protected void refreshUI() {
		// TODO Auto-generated method stub
		titlename.setText(clientName);
		if (response instanceof ClientsNotesResponse) {
			getParent().removeDialog(Constants.SHOW_DIALOG);
			int code = ((ClientsNotesResponse) response).getResponseCode();
			if (code == Constants.NETWORK_SUCCESS) {
				
				int id = newnote.getId();
				ArrayList<ClientsNotes> clientnotelist =ClientsNoteDataManager.getSharedInstance().getClientsNote();
						
				edittext.setText(tempNote.getNotetext());
				for(int k=0;k<clientnotelist.size();k++){
					ClientsNotes temp=clientnotelist.get(k);
					if (id == temp.getId())
					{
						clientnotelist.set(k, tempNote);
						
					}
					
				}
				String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
				SharedMethods.saveFromFile("sucessfully Updated Note - "+ currentDateTimeString,false,this);
				
				
				
				
				ClientsNoteDataManager.getSharedInstance().setClientNoteByArrayList(clientnotelist);
				Bundle bundle = new Bundle();
				bundle.putString(Constants.ERROR_D, getResources().getString(R.string.NAVIGATE_MSG));
				bundle.putString(Constants.TYPE, Constants.NAVIGATE_TO_PREVIOUS_PAGE_TYPE);				
				getParent().showDialog(Constants.ERROR_DIALOG,bundle);
				
				
				
				
				/*Bundle b = new Bundle();
				b.putString(Constants.ERROR_D, getResources().getString(R.string.NOT_LOGGED));
				b.putString(Constants.TYPE, Constants.R_ALERT_TYPE);
				//().removeDialog(Constants.ERROR_DIALOG);
				getParent().showDialog(Constants.ERROR_DIALOG, b);*/
			}
			
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
	
		titlename.setText(clientName);
	}

	public void navigateToPreviousPage() {
		this.finish();
	}
	

	
}