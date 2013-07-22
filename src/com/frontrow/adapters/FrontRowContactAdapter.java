package com.frontrow.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frontrow.common.Constants;
import com.frontrow.common.SharedMethods;
import com.frontrow.interfaces.FRSContactListBtnListener;
import com.frontrow.ui.R;
import com.row.mix.beans.CompositeContact;
import com.row.mix.beans.Contact;

public class FrontRowContactAdapter extends ArrayAdapter<Object>  {

	private Context ctx;
	private List<Contact> contactList;
	private List<CompositeContact> composite_contact_list;
	private View view;
	private FRSContactListBtnListener contactListBtnListener;
	private ImageButton callbtn;
	private Contact cmp;
	private CompositeContact compositeContact;
	private Button numbtn;
	private Button conmapbtn;
	private Button condirectionbtn;
	private Button conemailbtn;
	private LinearLayout callayout;
	private TextView contactprimarytext;
	//private List<String> imgList;

	public FRSContactListBtnListener getContactListBtnListener() {
		return contactListBtnListener;
	}

	public void setContactListBtnListener(
			FRSContactListBtnListener contactListBtnListener) {
		this.contactListBtnListener = contactListBtnListener;
	}

	public FrontRowContactAdapter(Context context, int resource, List<Contact> objects,List<CompositeContact> compositeObjects){
		super(context, resource);
		// TODO Auto-generated constructor stub
		this.ctx = context;
		this.contactList = objects;
		this.composite_contact_list = compositeObjects;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		int count = (contactList == null?composite_contact_list.size():contactList.size());
		return count;
	}



	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		Object obj = (contactList == null)?composite_contact_list.get(position):contactList.get(position);
		return obj;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		view = convertView;
		if(view == null){
			LayoutInflater vi = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = vi.inflate(R.layout.contactlistrow, null);
		}

		//callayout =(LinearLayout) view.findViewById(R.id.callayout);
		TextView cname = (TextView) view.findViewById(R.id.contactnametext);
		TextView title = (TextView) view.findViewById(R.id.contacttitletext);
		TextView email = (TextView) view.findViewById(R.id.contactemailtext);
		TextView phoneNumbers = (TextView) view.findViewById(R.id.contactphn);
		contactprimarytext = (TextView) view.findViewById(R.id.contactprimarytext);
		TextView address = (TextView) view.findViewById(R.id.contactaddresstext);
		//callbtn =(ImageButton) view.findViewById(R.id.callbtn);
		numbtn=(Button) view.findViewById(R.id.connumbersbtn);
		conmapbtn=(Button) view.findViewById(R.id.conmapbtn);
		//condirectionbtn=(Button) view.findViewById(R.id.condirectionbtn);
		conemailbtn=(Button) view.findViewById(R.id.conemailbtn);


		if(contactList != null && contactList.size() > 0){
			cmp = contactList.get(position);
		}
		if(composite_contact_list != null){
			compositeContact = composite_contact_list.get(position);
		}


		//Drawable dw = null;
		if(cmp != null){			
			cname.setText(cmp.getFirstName() +" "+cmp.getLastName());	
			if(cmp.getTitle() != null && cmp.getTitle().length() >0){
				title.setText(cmp.getTitle());		
			}else{
				title.setVisibility(View.GONE);
			}
			email.setText(cmp.getEmail());

			if(cmp.isPrimary()){
				contactprimarytext.setVisibility(view.VISIBLE);
			}else{
				contactprimarytext.setVisibility(view.INVISIBLE);	
			}

			String street,city,postal,province,country;
			if (cmp.getPrimaryAddress().getStreet()!=null && !cmp.getPrimaryAddress().getStreet().equalsIgnoreCase("") && !cmp.getPrimaryAddress().getStreet().equalsIgnoreCase("null"))
			{
				street=cmp.getPrimaryAddress().getStreet()+",";
			}else{
				street="";
			}

			if(cmp.getPrimaryAddress().getCity()!=null  && !cmp.getPrimaryAddress().getCity().equalsIgnoreCase("") && !cmp.getPrimaryAddress().getCity().equalsIgnoreCase("null")){

				city=cmp.getPrimaryAddress().getCity()+",";	
			}else{
				city="";
			}

			if(cmp.getPrimaryAddress().getPostal()!=null && !cmp.getPrimaryAddress().getPostal().equalsIgnoreCase("") && cmp.getPrimaryAddress().getPostal()!=null  && !cmp.getPrimaryAddress().getPostal().equalsIgnoreCase("null")){
				postal=cmp.getPrimaryAddress().getPostal()+",";
			}else{
				postal="";
			}

			if(cmp.getPrimaryAddress().getProvince()!=null && !cmp.getPrimaryAddress().getProvince().equalsIgnoreCase("") && !cmp.getPrimaryAddress().getProvince().equalsIgnoreCase("null"))
			{
				province=cmp.getPrimaryAddress().getProvince()+",";
			}else{
				province="";
			}

			if(cmp.getPrimaryAddress().getCountry()!=null && !cmp.getPrimaryAddress().getCountry().equalsIgnoreCase("") && !cmp.getPrimaryAddress().getCountry().equalsIgnoreCase("null")){
				country=cmp.getPrimaryAddress().getCountry()+",";
			}else{
				country="";
			}

			if(cmp.getPrimaryPhone().getPhoneNumber() != null && cmp.getPrimaryPhone().getPhoneNumber().length() != 0 && !cmp.getPrimaryPhone().getPhoneNumber().equalsIgnoreCase("null")){
				phoneNumbers.setText(cmp.getPrimaryPhone().getPhoneNumber());
			}

			String tempaddress = street+city+postal+province+country;			
			address.setText(tempaddress);
			/*if (cmp.getPrimaryPhone().getPhoneNumber() != null && cmp.getPrimaryPhone().getPhoneNumber().length() >3 ){
				callayout.setVisibility(view.VISIBLE);	
				TextView phone = (TextView) view.findViewById(R.id.contacttpnumber);
				phone.setText(cmp.getPrimaryPhone().getPhoneNumber());
			}
			else{
				callayout.setVisibility(view.INVISIBLE);	
			}*/

			/*callbtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Contact tempcmp = contactList.get(position);
					//SharedMethods.logError("position : "+ position);
					contactListBtnListener.doBtnWorks(view,Constants.CALL_BTN , tempcmp);

				}
			});*/

			numbtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Contact tempcmp = contactList.get(position);
					contactListBtnListener.doBtnWorks(view,Constants.NUMBERS_BTN , tempcmp);

				}
			});

			conmapbtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {	
					Contact tempcmp = contactList.get(position);
					contactListBtnListener.doBtnWorks(view,Constants.MAP_BTN , tempcmp);
				}
			});

			conemailbtn.setOnClickListener(new OnClickListener() {

				Contact tempcmp = contactList.get(position);
				@Override
				public void onClick(View v) {
					contactListBtnListener.doBtnWorks(view,Constants.EMAIL_BTN , tempcmp);
				}
			});
		}else if(compositeContact != null){
			cname.setText(compositeContact.getContact().getFirstName() +" "+compositeContact.getContact().getLastName());

			if(compositeContact.getContact().getTitle() != null && compositeContact.getContact().getTitle().length() >0){
				title.setText(compositeContact.getContact().getTitle());		
			}else{
				title.setVisibility(View.GONE);
			}

			email.setText(compositeContact.getContact().getEmail());
				if(compositeContact.getIsprimary()){
				contactprimarytext.setVisibility(view.VISIBLE);
			}else{
				contactprimarytext.setVisibility(view.INVISIBLE);	
			}

			String street,city,postal,province,country;
			if (compositeContact.getAddress().getStreet()!=null && !compositeContact.getAddress().getStreet().equalsIgnoreCase("") && !compositeContact.getAddress().getStreet().equalsIgnoreCase("null"))
			{
				street=compositeContact.getAddress().getStreet()+",";
			}else{
				street="";
			}

			if(compositeContact.getAddress().getCity()!=null  && !compositeContact.getAddress().getCity().equalsIgnoreCase("") && !compositeContact.getAddress().getCity().equalsIgnoreCase("null")){
				city=compositeContact.getAddress().getCity()+",";	
			}else{
				city="";
			}

			if(compositeContact.getAddress().getPostal()!=null && !compositeContact.getAddress().getPostal().equalsIgnoreCase("") && compositeContact.getAddress().getPostal()!=null  && !compositeContact.getAddress().getPostal().equalsIgnoreCase("null")){
				postal = compositeContact.getAddress().getPostal()+",";
			}else{
				postal="";
			}

			if(compositeContact.getAddress().getProvince()!=null && !compositeContact.getAddress().getProvince().equalsIgnoreCase("") && !compositeContact.getAddress().getProvince().equalsIgnoreCase("null"))
			{
				province = compositeContact.getAddress().getProvince()+",";
			}else{
				province="";
			}

			if(compositeContact.getAddress().getCountry()!=null && !compositeContact.getAddress().getCountry().equalsIgnoreCase("") && !compositeContact.getAddress().getCountry().equalsIgnoreCase("null")){
				country = compositeContact.getAddress().getCountry()+",";
			}else{
				country="";
			}

			String mobile_numbers = null;
			if(compositeContact.getMobilePhone().getPhoneNumber() != null && compositeContact.getMobilePhone().getPhoneNumber().length() != 0 && !compositeContact.getMobilePhone().getPhoneNumber().equalsIgnoreCase("null")){
				mobile_numbers = compositeContact.getMobilePhone().getPhoneNumber();
			}
			if(compositeContact.getBusinessPhone().getPhoneNumber() != null && compositeContact.getBusinessPhone().getPhoneNumber().length() != 0 && !compositeContact.getBusinessPhone().getPhoneNumber().equalsIgnoreCase("null")){
				if(mobile_numbers != null && mobile_numbers.trim().length() > 0){
					mobile_numbers = " , "+ compositeContact.getBusinessPhone().getPhoneNumber();
				}else{
					mobile_numbers =compositeContact.getBusinessPhone().getPhoneNumber();
				}
			}

			if(mobile_numbers != null){
				phoneNumbers.setText(mobile_numbers);
			}
			String tempaddress = street+city+postal+province+country;			
			address.setText(tempaddress);
			/*if (compositeContact.getMobilePhone() != null && compositeContact.getMobilePhone().getPhoneNumber().length() >3 ){
				callayout.setVisibility(view.VISIBLE);	
				TextView phone = (TextView) view.findViewById(R.id.contacttpnumber);
				phone.setText(cmp.getPrimaryPhone().getPhoneNumber());
			}
			else{
				callayout.setVisibility(view.INVISIBLE);	
			}*/

			/*callbtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					CompositeContact tempcmp = composite_contact_list.get(position);
					//SharedMethods.logError("position : "+ position);
					//contactListBtnListener.doBtnWorks(view,Constants.CALL_BTN , tempcmp);

				}
			});*/

			numbtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					CompositeContact tempcmp = composite_contact_list.get(position);
					contactListBtnListener.doBtnWorks(view,Constants.NUMBERS_BTN , tempcmp);

				}
			});

			conmapbtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {	
					CompositeContact tempcmp = composite_contact_list.get(position);
					contactListBtnListener.doBtnWorks(view,Constants.MAP_BTN , tempcmp);
				}
			});

			conemailbtn.setOnClickListener(new OnClickListener() {

				CompositeContact tempcmp = composite_contact_list.get(position);
				@Override
				public void onClick(View v) {
					contactListBtnListener.doBtnWorks(view,Constants.EMAIL_BTN , tempcmp);
				}
			});

		}
		return view;
	}
}
