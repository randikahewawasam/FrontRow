package com.frontrow.adapters;

import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.frontrow.ui.R;
import com.row.mix.beans.ClientsNotes;
import com.row.mix.beans.Contact;
import com.row.mix.beans.Phone;

public class FrontRowContactPhoneAdapter extends ArrayAdapter<Phone>  {

	private Context ctx;
	private List<Phone> contactPhoneList;
	private View view;
	//private List<String> imgList;

	public FrontRowContactPhoneAdapter(Context context, int resource, List<Phone> objects){
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		this.ctx = context;
		this.contactPhoneList = objects;
		
	}

	public List<Phone> getClientsNoteList() {
		return contactPhoneList;
	}

	public void setClientsNoteList(List<Phone> clientsNoteList) {
		this.contactPhoneList = clientsNoteList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		view = convertView;
		if(view == null){
			LayoutInflater vi = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = vi.inflate(R.layout.contactsnumberlistrow, null);
		}
		
		Phone cmp = contactPhoneList.get(position);
		
	
		//Drawable dw = null;
		if(cmp != null){
			TextView phonenumber = (TextView) view.findViewById(R.id.contactphone);
			phonenumber.setText(cmp.getPhoneNumber());
		

		}
		return view;
	}

}
