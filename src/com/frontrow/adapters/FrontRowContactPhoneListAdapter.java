package com.frontrow.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.frontrow.ui.R;
import com.row.mix.beans.Address;
import com.row.mix.beans.Phone;

public class FrontRowContactPhoneListAdapter  extends ArrayAdapter<Phone> {
	private Context ctx;
	private List<Phone> phoneList;
	private View view;
	private int primaryPhone;
	//private List<String> imgList;

	public FrontRowContactPhoneListAdapter(Context context, int resource, List<Phone> objects,int primaryAdd){
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		this.ctx = context;
		this.phoneList = objects;
		this.primaryPhone=primaryAdd;
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		view = convertView;
		if(view == null){
			LayoutInflater vi = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = vi.inflate(R.layout.phone_list_row, null);
		}
		
		Phone cmp = phoneList.get(position);
		
	
		//Drawable dw = null;
		if(cmp != null){

			/*
			 * if(aclient.get!=""){
			 * country=con.getPrimaryAddress().getCountry()+","; } else{
			 * country=""; }
			 */		    
			TextView cfileld = (TextView) view.findViewById(R.id.phone_number);
			cfileld.setText(cmp.getPhoneNumber());
			
			TextView primary = (TextView) view.findViewById(R.id.phone_primary_txt);
			if(primaryPhone != 0)
			{
			if(cmp.getPhoneID()== primaryPhone)
			{
				primary.setVisibility(view.VISIBLE);
			}
			else
			{
				primary.setVisibility(view.GONE);
			}
			}
			else
			{
				primary.setVisibility(view.GONE);	
			}
		}
		return view;
	}

	public Context getCtx() {
		return ctx;
	}

	public void setCtx(Context ctx) {
		this.ctx = ctx;
	}

	public List<Phone> getPhoneList() {
		return phoneList;
	}

	public void setPhoneList(List<Phone> phoneList) {
		this.phoneList = phoneList;
	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public int getPrimaryPhone() {
		return primaryPhone;
	}

	public void setPrimaryPhone(int primaryPhone) {
		this.primaryPhone = primaryPhone;
	}
}
