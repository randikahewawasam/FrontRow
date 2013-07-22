package com.frontrow.adapters;

import java.text.SimpleDateFormat;
import java.util.Formatter;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.frontrow.ui.R;
import com.row.mix.beans.ClientsNotes;
import com.row.mix.beans.CustomField;

public class FrontRowClientsNoteAdapter extends ArrayAdapter<ClientsNotes> {

	private Context ctx;
	private List<ClientsNotes> clientsNoteList;
	private View view;
	//private List<String> imgList;

	public FrontRowClientsNoteAdapter(Context context, int resource, List<ClientsNotes> objects){
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		this.ctx = context;
		this.clientsNoteList = objects;
		
	}

	public List<ClientsNotes> getClientsNoteList() {
		return clientsNoteList;
	}

	public void setClientsNoteList(List<ClientsNotes> clientsNoteList) {
		this.clientsNoteList = clientsNoteList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		view = convertView;
		if(view == null){
			LayoutInflater vi = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = vi.inflate(R.layout.clientsnoteslistrow, null);
		}
		
		ClientsNotes cmp = clientsNoteList.get(position);
		
	
		//Drawable dw = null;
		if(cmp != null){
			TextView cfileld = (TextView) view.findViewById(R.id.clintsnotetext);
			cfileld.setText(cmp.getNotetext());
			TextView cvalue = (TextView) view.findViewById(R.id.clintsnotedatetimestamp);
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm");
			String dateString = formatter.format(cmp.getDatetimestamp());
			cvalue.setText(dateString);

		}
		return view;
	}
}
