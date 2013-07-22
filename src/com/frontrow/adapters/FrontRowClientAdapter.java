package com.frontrow.adapters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.frontrow.common.SharedMethods;
import com.frontrow.ui.R;
import com.row.mix.beans.Client;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class FrontRowClientAdapter extends ArrayAdapter<Client>{
	private Context ctx;
	private List<Client> clientList; 
	private List<Client> originalItems = new ArrayList<Client>();
	private View view;
	private final Object mLock = new Object();
	private FRSFilter filter;


	public FrontRowClientAdapter(Context context, int resource,
			List<Client> objects) {
		super(context, resource,  objects);
		// TODO Auto-generated constructor stub
		this.ctx = context;
		this.clientList = objects;
		cloneItems(clientList);
	}	

	public void setClientList(List<Client> clientList) {
		this.clientList = clientList;
		originalItems = new ArrayList<Client>();
		cloneItems(clientList);
	}



	protected void cloneItems(List<Client> items) {
		for (Iterator<Client> iterator = items.iterator(); iterator
				.hasNext();) {
			Client s = (Client) iterator.next();

			originalItems.add(s);
		}
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		SharedMethods.logError("Original COunt :" + Integer.toString(clientList.size()));
		return clientList.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		view = convertView;
		if(view == null){
			LayoutInflater vi = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = vi.inflate(com.frontrow.ui.R.layout.clientdesclistrow, null);
		}
		Client client = clientList.get(position);
		TextView cName = (TextView) view.findViewById(R.id.cName);
		TextView cNumber = (TextView) view.findViewById(R.id.clientNumber);
		TextView cardtype = (TextView) view.findViewById(R.id.cardType);
		if(client != null){
			cName.setText(client.getClientName());
			cNumber.setText(client.getClientNumber());
			cardtype.setText(client.getCardtype());
		}

		return view;
	}



	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		if (filter == null) {
			filter = new FRSFilter();
		}
		return filter;	
	}

	/* Filtering ******************************************************/

	private class FRSFilter extends Filter {
		protected FilterResults performFiltering(CharSequence prefix) {
			// Initiate our results object
			FilterResults results = new FilterResults();

			// No prefix is sent to filter by so we're going to send back the original array
			if (prefix == null || prefix.length() == 0) {
				synchronized (mLock) {
					results.values = originalItems;
					results.count = originalItems.size();
				}
			} else {
				synchronized(mLock) {
					// Compare lower case strings
					String prefixString = prefix.toString().toLowerCase();
					final ArrayList<Client> filteredItems = new ArrayList<Client>();
					// Local to here so we're not changing actual array
					final ArrayList<Client> localItems = new ArrayList<Client>();
					localItems.addAll(originalItems);
					final int count = localItems.size();

					for (int i = 0; i < count; i++) {
						final Client item = localItems.get(i);
						final String itemName = item.getClientName().toString().toLowerCase();
						final String number = item.getClientNumber();
						final String province = item.getAddress().getProvince().toString().toLowerCase();
						final String city = item.getAddress().getCity().toString().toLowerCase();

						// First match against the whole, non-splitted value
						if (itemName.startsWith(prefixString) || city.startsWith(prefixString) || province.startsWith(prefixString) || number.startsWith(prefixString) ) {
							filteredItems.add(item);
						} else {} /* This is option and taken from the source of ArrayAdapter
                            final String[] words = itemName.split(" ");
                            final int wordCount = words.length;

                            for (int k = 0; k < wordCount; k++) {
                                if (words[k].startsWith(prefixString)) {
                                    newItems.add(item);
                                    break;
                                }
                            }
                        } */
					}

					// Set and return
					results.values = filteredItems;
					results.count = filteredItems.size();
				}//end synchronized
			}

			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence prefix, FilterResults results) {
			//noinspection unchecked
			synchronized(mLock) {
				final ArrayList<Client> localItems = (ArrayList<Client>) results.values;
				notifyDataSetChanged();
				clear();
				//Add the items back in
				for (Iterator iterator = localItems.iterator(); iterator
						.hasNext();) {
					Client gi = (Client) iterator.next();
					add(gi);
				}
			}//end synchronized
		}
	}


	/*************************************************************/



}
