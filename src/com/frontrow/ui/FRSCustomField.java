package com.frontrow.ui;

import java.util.Arrays;
import java.util.List;

import com.frontrow.adapters.FrontRowClientActivityAdapter;
import com.frontrow.adapters.FrontRowCustomFileldsAdapter;
import com.frontrow.base.BaseActivity;
import com.frontrow.common.Constants;
import com.frontrow.datamanagers.CustomFieldDataManager;
import com.row.mix.beans.CustomField;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;

public class FRSCustomField extends BaseActivity {
	ListView customfieldList;
	private String clientName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	 setContentView(R.layout.activity_frscustom_field);
        super.onCreate(savedInstanceState);
       
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_frscustom_field, menu);
        return true;
    }

	@Override
	protected void initializeUI() {
		// TODO Auto-generated method stub
		Bundle bundle = getIntent().getExtras();
		if(bundle != null){
			clientName=bundle.getString(Constants.CLIENT_NAME);
			titlename.setText(clientName);
		}
		customfieldList =(ListView) findViewById(R.id.customfieldlist);
		 List<CustomField> custom_field_list = CustomFieldDataManager.getSharedInstance().getCustomFields();
   	 FrontRowCustomFileldsAdapter adapter = new FrontRowCustomFileldsAdapter(this,
   			 R.layout.customfieldlistrow, custom_field_list);
   	 customfieldList.setAdapter(adapter);
  /* 	 if(custom_field_list !=null && custom_field_list.size()!=0){
   	
   	 }*/
/*	 else{
		 Bundle bundle2 = new Bundle();
			bundle2.putString(Constants.ERROR_D, getResources().getString(R.string.NO_CUSTOM_FIELD_MSG));
			bundle2.putString(Constants.TYPE, Constants.GENARAL_ERROR_TYPE);				
			getParent().showDialog(Constants.ERROR_DIALOG,bundle2);
		 
	 }*/
		
	}

	@Override
	protected void refreshUI() {
		titlename.setText(clientName);
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		// TODO Auto-generated method stub
		titlename.setText(clientName);
	
		
	}
}
