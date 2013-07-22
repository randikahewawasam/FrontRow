package com.frontrow.ui;

import java.util.ArrayList;
import java.util.List;



import com.frontrow.common.SharedMethods;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.row.mix.beans.Client;
import com.row.mix.beans.CompositeContact;
import com.row.mix.beans.Contact;
import com.row.mix.beans.Location;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.Menu;

public class FRSMap extends MapActivity {
	private MapView mapView;
	private GeoPoint selectedGEOPoint;
	private MapController mapController;
	private int zoom;
	private int width;
	private int height;
	private SitesOverlay sites;
	private Context ctx;
	private double lat = 0.0;
	private double lon= 0.0;
	private Client client;
	private Contact contact;
	private CompositeContact compositeContact;
	private Location loc;
	String type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
      
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frsmap);
		mapView = (MapView) findViewById(R.id.mapView);
		if(getIntent().getExtras() != null){
			type= (String) getIntent().getExtras().get("TYPE");
			if(type.equals("LOCATION")){
				loc = (Location) getIntent().getExtras().get("LOCATION");
			}
			else if(type.equals("CONTACT"))
			{
				contact = (Contact) getIntent().getExtras().get("Contact");
			}
			else if (type.equals("COMPOSITECONTACT"))
			{
				compositeContact = (CompositeContact) getIntent().getExtras().get("CompositeContact");
			}
			else{
				client = (Client) getIntent().getExtras().get("CLIENT");
			}
			
		}


		sites = new SitesOverlay();
		mapView.getOverlays().add(sites);
    }

    private class SitesOverlay extends ItemizedOverlay<CustomItem> {
		List<CustomItem> items = new ArrayList<CustomItem>();  

		public SitesOverlay() {
			super(null);
			// TODO Auto-generated constructor stub
			//rest = CompanyDetailsDataManager.getSharedInstance().getCmpDetals();
			if (type.equalsIgnoreCase("LOCATION")){
				lat = loc.getLat();
				lon = loc.getLng();
			}
			else if(type.equalsIgnoreCase("CONTACT"))
			{
				lat = contact.getPrimaryAddress().getLat();
				lon = contact.getPrimaryAddress().getLon();
			}
			else if(type.equalsIgnoreCase("COMPOSITECONTACT"))
			{
				lat = compositeContact.getAddress().getLat();
				lon = compositeContact.getAddress().getLon();
			}
			else{
				
				lat = client.getAddress().getLat();
				lon = client.getAddress().getLon();
			}
				
				selectedGEOPoint = new GeoPoint((int)(lat*1E6), (int)(lon*1E6));
				//int minLat = Integer.MIN_VALUE;
				//int minLon = Integer.MIN_VALUE;
				double maxLat =  Math.max(lat, Integer.MAX_VALUE);
				double minLat = Math.min(lat, Integer.MIN_VALUE);
				double maxLon = Math.max(lon, Integer.MAX_VALUE);
				double minLon = Math.min(lon, Integer.MIN_VALUE);
				items.add(new CustomItem(selectedGEOPoint, "",
						null, getMarker(R.drawable.selectedmap),
						getMarker(R.drawable.selectedmap)));
				zoom = SharedMethods.calculateZoom(lon - 3, lon, lat- 4, lat,width,height);
				mapController = mapView.getController();
				mapController.setZoom(17);
				double centerLat = (lat + lat )/2;
				double centerLon = (lon + lon)/2;
				GeoPoint centerPOint = new GeoPoint((int)(centerLat*1E6), (int)(centerLon*1E6));

				//SharedMethods.calculateZoom(0.0, lon, 0.0, lat, width, height);
				mapView.setBuiltInZoomControls(true);

				mapController.setCenter(centerPOint);
				populate();
				mapView.invalidate();
			
		}

		@Override
		protected CustomItem createItem(int i) {
			// TODO Auto-generated method stub
			return items.get(i);
		}
		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
			// TODO Auto-generated method stub
			super.draw(canvas, mapView, shadow);
		}

		@Override
		public int size() {
			// TODO Auto-generated method stub
			return items.size();
		}

		private Drawable getMarker(int resource) {
			Drawable marker = getResources().getDrawable(resource);
			marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker
					.getIntrinsicHeight());
			boundCenter(marker);
			return (marker);
		}


	}

	class CustomItem extends OverlayItem {
		Drawable marker = null;
		boolean isHeart = false;
		Drawable heart = null;

		CustomItem(GeoPoint pt, String name, String snippet, Drawable marker,Drawable heart) {
			super(pt, name, snippet);
			this.marker = marker;
			this.heart = heart;
		}

		@Override
		public Drawable getMarker(int stateBitset) {
			Drawable result = (isHeart ? heart : marker);
			setState(result, stateBitset);
			return (result);
		}
		void toggleHeart() {
			isHeart = !isHeart;
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}


}
