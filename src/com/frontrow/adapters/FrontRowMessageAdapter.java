package com.frontrow.adapters;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import com.frontrow.common.Constants;
import com.frontrow.common.SharedMethods;
import com.frontrow.ui.R;
import com.row.mix.beans.OfflineM;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FrontRowMessageAdapter extends ArrayAdapter<OfflineM>{

	private Context ctx;
	private List<OfflineM> msgList; 
	private View view;

	public FrontRowMessageAdapter(Context context, int textViewResourceId,
			List<OfflineM> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		this.ctx = context;
		//this.msgList = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		view = convertView;
		if(view == null){
			LayoutInflater vi = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = vi.inflate(com.frontrow.ui.R.layout.frsmessages, null);
		}

		ImageView statIcon = (ImageView) view.findViewById(R.id.statusicon);	
		TextView heading = (TextView) view.findViewById(R.id.head);
		TextView type = (TextView) view.findViewById(R.id.type);
		TextView date = (TextView) view.findViewById(R.id.date);
		OfflineM msg  = getItem(position);
		SharedMethods.logError("Status : "+ msg.getStatus());
		switch (msg.getStatus()) {
		case Constants.FAILED:
			statIcon.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.red));
			break;
		case Constants.PENDING:			
			SharedMethods.logError("Status : "+ msg.getStatus() + "Pending");
			statIcon.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.yellow));
			break;			
		case Constants.SUCCESS:			
			SharedMethods.logError("Status : "+ msg.getStatus() + "Success");
			statIcon.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.green));
			break;
		default:
			break;
		}
		heading.setText(msg.getClientName());		
		String time = convertTime(msg.getDateTime());		
		date.setText(time);
		type.setText(msg.getType());
		
		return view;
	}


	private String convertTime(String dateTime) {
		Date date = null;
		DateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date = iso8601Format.parse(dateTime);
		} catch (ParseException e) {
			SharedMethods.logError("Parsing ISO8601 datetime failed"+ e.getMessage());
		}

		long when = date.getTime();
		int flags = 0;
		flags |= android.text.format.DateUtils.FORMAT_SHOW_TIME;
		flags |= android.text.format.DateUtils.FORMAT_SHOW_DATE;
		flags |= android.text.format.DateUtils.FORMAT_ABBREV_MONTH;
		flags |= android.text.format.DateUtils.FORMAT_SHOW_YEAR;

		String finalDateTime = android.text.format.DateUtils.formatDateTime(ctx,
				when , flags);


		/*String finalDateTime = android.text.format.DateUtils.formatDateTime(ctx,
		when + TimeZone.getDefault().getOffset(when), flags);*/
		return finalDateTime;
	}

}
