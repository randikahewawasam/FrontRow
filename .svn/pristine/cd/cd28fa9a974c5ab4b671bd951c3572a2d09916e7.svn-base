package com.frontrow.adapters;

import java.util.LinkedHashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.frontrow.ui.R;
import com.row.mix.beans.Questions;

public class FrontRowActivity_PostView_Adapter extends ArrayAdapter<Questions> {

	private Context ctx;
	private LinkedHashMap<Questions, String> catList;
	private View view;
	private List<Questions> questions;
	private List<String> ans;

	public FrontRowActivity_PostView_Adapter(Context context, int resource, List<Questions> objects,List<String> objects2){
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		this.ctx = context;
		this.questions = objects;
		this.ans = objects2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		view = convertView;
		if(view == null){
			LayoutInflater vi = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = vi.inflate(R.layout.activity_post_list_row, null);
		}
		
		Questions cmp = questions.get(position);
		//String qname=cmp.getQuestion();
		String ansk=ans.get(position);
		ansk=ansk.replace("#","");
		TextView question = (TextView) view.findViewById(com.frontrow.ui.R.id.activity_ques);
		TextView answer = (TextView) view.findViewById(com.frontrow.ui.R.id.activity_ans);
		
		question.setText(cmp.getQuestion());
		answer.setText(ansk);

		return view;
	}

}
