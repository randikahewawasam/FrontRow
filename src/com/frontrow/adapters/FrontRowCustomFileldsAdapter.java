package com.frontrow.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.frontrow.ui.R;
import com.row.mix.beans.CustomField;

public class FrontRowCustomFileldsAdapter extends ArrayAdapter<CustomField> {

	private Context ctx;
	private List<CustomField> customFieldList;
	private View view;

	public FrontRowCustomFileldsAdapter(Context context, int resource, List<CustomField> objects){
		super(context, resource, objects);
		this.ctx = context;
		this.customFieldList = objects;
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	
		view = convertView;
		if(view == null){
			LayoutInflater vi = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = vi.inflate(R.layout.customfieldlistrow, null);
		}
		
		CustomField cmp = customFieldList.get(position);
		
	
	
		if(cmp != null){
			TextView cfileld = (TextView) view.findViewById(R.id.customfield);
			cfileld.setText(cmp.getName());
			TextView cvalue = (TextView) view.findViewById(R.id.customvalue);
			cvalue.setText(cmp.getValue());

		}
		return view;
	}
}
