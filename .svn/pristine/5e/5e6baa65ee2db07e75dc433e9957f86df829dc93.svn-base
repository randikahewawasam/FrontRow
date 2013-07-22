package com.frontrow.adapters;

import java.io.InputStream;
import java.util.List;

import com.frontrow.ui.R;


import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;



public class FrontRowClientActivityAdapter extends ArrayAdapter<String>{
	private Context ctx;
	private List<String> catList;
	private View view;
	private List<String> imgList;

	public FrontRowClientActivityAdapter(Context context, int resource, List<String> objects,List<String> objects2){
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		this.ctx = context;
		this.catList = objects;
		this.imgList = objects2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		view = convertView;
		if(view == null){
			LayoutInflater vi = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = vi.inflate(R.layout.clientprofilelistrow, null);
		}
		
		String cmp = catList.get(position);
		String imgname=imgList.get(position);
		ImageView catImg = (ImageView) view.findViewById(com.frontrow.ui.R.id.clintactImg);
		ImageView nxt = (ImageView) view.findViewById(com.frontrow.ui.R.id.next);
		//Drawable dw = null;
		if(cmp != null){
			TextView cmpName = (TextView) view.findViewById(R.id.clintactname);
			cmpName.setText(cmp);
			//String imgName = cmp.getCategoryImage()+Constants.IMG_EXTENTION;
			//Bitmap bitMap = getBitmapFromAsset(Constants.ASSET_PATH+imgName);
			//bitMap = (bitMap == null)?getBitmapFromAsset(Constants.ASSET_PATH+"No_image_found.png"):bitMap;
			ctx.getResources().getIdentifier(imgname,"drawable", ctx.getPackageName());
			catImg.setImageDrawable(ctx.getResources().getDrawable(ctx.getResources().getIdentifier(imgname,"drawable", ctx.getPackageName())));

			/*if(cmp.isHaveChildren()){
				dw = ctx.getResources().getDrawable(R.drawable.map_popup_arrow);

			}else{
				dw = ctx.getResources().getDrawable(R.drawable.arrow);
			}*/
			//Drawable dw = (cmp.isHaveChildren())?ctx.getResources().getDrawable(R.drawable.arrow_subcategory):ctx.getResources().getDrawable(R.drawable.arrow);
			nxt.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.arrow));
		}
		return view;
	}

	private Bitmap getBitmapFromAsset(String strName) 
	{
		Bitmap bitmap = null;
		try{
			AssetManager assetManager = ctx.getAssets();
			InputStream istr = assetManager.open(strName);
			bitmap = BitmapFactory.decodeStream(istr);
		}catch (Exception e) {
			// TODO: handle exception
			//SharedMethods.logError("Image :"+e.getMessage());
		}
		return bitmap;
	}

	
}
