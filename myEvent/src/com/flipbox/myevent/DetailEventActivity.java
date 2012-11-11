package com.flipbox.myevent;

import java.util.Date;
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class DetailEventActivity extends Activity {

	public static final String TAG_BREADCRUMB = "breadcrumb";
	public static final String TAG_BUSINESS_ID = "business_id";
	public static final String TAG_BUSINESS_NAME = "business_name";
	public static final String TAG_KECAMATAN_NAME = "kecamatan_name";
	public static final String TAG_CITY_NAME = "city_name";
	public static final String TAG_PROVINCE_NAME = "province_name";
	public static final String TAG_COUNTRY_NAME = "country_name";

	public static final String TAG_EVENT_PROFILE = "event_profile";
	public static final String TAG_TIKET_EVENT_START = "tiket_event_start";
	public static final String TAG_TIKET_EVENT_END = "tiket_event_end";
	public static final String TAG_DATE = "date";
	public static final String TAG_EVENT_TYPE_DESCRIPTION = "event_type_description";

	public static final String TAG_PRIMARY_PHOTOS = "primary_photos";

	public static final String TAG_EVENT_TIKETS = "event_tikets";
	public static final String TAG_EVENT_TIKET = "event_tiket";
	public static final String TAG_TIKET_ID = "tiket_id";
	public static final String TAG_TIKET_NAME = "tiket_name";
	public static final String TAG_TIKET_PRICE = "tiket_price";
	public static final String TAG_TIKET_AVAILABLE = "tiket_available";
	public static final String TAG_TIKET_START_SELL = "tiket_start_sell";
	public static final String TAG_TIKET_END_SELL = "tiket_end_sell";
	public static final String TAG_TIKET_MIN_PURCHASE = "tiket_min_purchase";
	public static final String TAG_TIKET_MAX_PURCHASE = "tiket_max_purchase";
	public static final String TAG_TIKET_REQUIRED_INFO = "tiket_required_info";

	public static final String TAG_NEARBY_HOTELS = "nearby_hotels";
	public static final String TAG_NEARBY_HOTEL = "nearby_hotel";
	public static final String TAG_DISTANCE = "distance";
	public static final String TAG_BUSINESS_PRIMARY_PHOTO = "business_primary_photo";
	public static final String TAG_BUSINESS_URI = "business_uri";

	int id;
	String title;
	String desc;
	Date date;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_event);

		// getting intent data
		Intent in = getIntent();

		// Get JSON values from previous intent
		String token = in.getStringExtra("token");
		System.out.println("token: " + token);
		String uri = in.getStringExtra("uri");
		System.out.println("uri: " + uri);

		if (uri.startsWith("https"))
			uri = "http" + uri.substring(5);

		System.out.println("new uri: " + uri);

		TextView lblName = (TextView) findViewById(R.id.name_label);
		//TextView lblDesc = (TextView) findViewById(R.id.desc_label);

		JSONObject detailEventJson = JSONfunctions.getJSONObjectfromURL(uri
				+ "?token=" + token + "&output=json");
		System.out.println("detailEventJson: " + detailEventJson.names());

		String name, address, kcmtn, city, prov, country, desc;

		try {

			JSONObject breadcrumb = detailEventJson
					.getJSONObject(TAG_BREADCRUMB);
			// Event name
			name = breadcrumb.getString(TAG_BUSINESS_NAME);
			//int id = Integer.parseInt(breadcrumb.getString(TAG_BUSINESS_ID));
			lblName.setText(name);
			
			desc = "";

			// Event Address
			address = "";
			kcmtn = breadcrumb.getString(TAG_KECAMATAN_NAME);
			if (kcmtn != "null")
				address += kcmtn + ", ";
			city = breadcrumb.getString(TAG_CITY_NAME);
			if (city != "null")
				address += city + ", ";
			prov = breadcrumb.getString(TAG_PROVINCE_NAME);
			if (prov != "null")
				address += prov + ", ";
			country = breadcrumb.getString(TAG_COUNTRY_NAME);
			if (country != "null")
				address += country;

			System.out.println("address: " + address);
			desc += "address: " + address + "\n";
			// Event's Profile
			JSONObject event_profile = detailEventJson
					.getJSONObject(TAG_EVENT_PROFILE);
			JSONArray event_profile_content = event_profile.names();
			for (int i = 0; i < event_profile_content.length(); i++) {
				String tag = event_profile_content.getString(i);
				System.out.println(tag + ": " + event_profile.getString(tag));
				desc += tag + ": " + event_profile.getString(tag) + "\n";
			}

			// Event's Profile Picture
			String event_picture = detailEventJson
					.getString(TAG_PRIMARY_PHOTOS);
			System.out.println(event_picture);

			// Event's Tiket
			JSONObject event_tikets = detailEventJson
					.getJSONObject(TAG_EVENT_TIKETS);
			JSONArray event_tiket = event_tikets.getJSONArray(TAG_EVENT_TIKET);
			for (int i = 0; i < event_tiket.length(); i++) {
				JSONObject tiket = event_tiket.getJSONObject(i);
				System.out.println(tiket);
				JSONArray tiket_content = tiket.names();
				System.out.println(tiket_content);
				for (int j = 0; j < tiket_content.length(); j++) {
					String tag = tiket_content.getString(j);
					System.out.println(tag + ": " + tiket.getString(tag));
					desc += tag + ": " + tiket.getString(tag) + "\n";
				}
			}

			// Nearby Hotel
			JSONObject nearby_hotels = detailEventJson
					.getJSONObject(TAG_NEARBY_HOTELS);

			JSONArray nearby_hotel = nearby_hotels
					.getJSONArray(TAG_NEARBY_HOTEL);
			for (int i = 0; i < nearby_hotel.length(); i++) {
				JSONObject hotel = nearby_hotel.getJSONObject(i);
				System.out.println(hotel);
				JSONArray hotel_content = hotel.names();
				System.out.println(hotel_content);
				for (int j = 0; j < hotel_content.length(); j++) {
					String tag = hotel_content.getString(j);
					System.out.println(tag + ": " + hotel.getString(tag));
					desc += tag + ": " + hotel.getString(tag) + "\n";
				}
			}

			//lblDesc.setText(desc);
			//this.desc = desc;
			JSONObject tiket = detailEventJson.getJSONObject(TAG_EVENT_PROFILE);
			String date = tiket.getString(TAG_TIKET_EVENT_START);
			
			this.title = name;
			
			this.date = new Date();
			Log.i("date", date);
			this.date = stringToDate(date);
			TextView add = (TextView) findViewById(R.id.address);
			add.setText(address);
			TextView dt = (TextView) findViewById(R.id.date);
			dt.setText(date);
			TextView note = (TextView) findViewById(R.id.description);
			note.setText(event_profile.getString(TAG_EVENT_TYPE_DESCRIPTION));
			TextView ticket = (TextView) findViewById(R.id.ticket);
			// Event's Tiket
			String tk = "";		
			for (int i = 0; i < event_tiket.length(); i++) {
				tiket = event_tiket.getJSONObject(i);
				
				String nama = tiket.getString(TAG_TIKET_NAME);
				String harga = tiket.getString(TAG_TIKET_PRICE);
				tk += (nama + "\n\tRp. "+harga+"\n\n");
				
			}
			ticket.setText(tk);
			//this.id = id;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}

	}
	
	public void onClick(View view) {
		switch(view.getId()) {
		case R.id.add_event:
			//Event event = new Event(id, title, date, null, desc, null, null);
			Database.insertEvent(0, title, desc, date, null, null, null);
			Log.i("Insert Event", id+"");
			if(date != null)
				showNotification();
			break;
		case R.id.purchase:
			String url = "http://m.tiket.com";
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(url));
			startActivity(i);
			break;
		}
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		
		super.onPostCreate(savedInstanceState);
		
	}
	
	public void showNotification() {
	  NotificationManager notificationManager = (NotificationManager) 
      getSystemService(NOTIFICATION_SERVICE);
	  
	  //notification set at 24 hours before the event
	  long millis = date.getTime() - 86400000;
	  Notification notification = new Notification(R.drawable.ic_launcher,
	        "You have event tomorrow.", millis);
	  
	  
	    // Hide the notification after its selected
	  notification.flags |= Notification.FLAG_AUTO_CANCEL;
	
	  Intent intent = new Intent(this, DetailEventActivity.class);
	  PendingIntent activity = PendingIntent.getActivity(this, 0, intent, 0);
	  notification.setLatestEventInfo(this, "Notification",
	      "Click for more info", activity);
	  // Set default sound
      notification.defaults |= Notification.DEFAULT_SOUND;
      //Set the default Vibration
      notification.defaults |= Notification.DEFAULT_VIBRATE;
      // Set the light pattern
      notification.ledARGB = 0xff00ff00;
      notification.ledOnMS = 300;
      notification.ledOffMS = 1000;
      notification.flags |= Notification.FLAG_SHOW_LIGHTS;
	  notification.number += 1;
	  
	  notificationManager.notify(0, notification);
	  Log.i("Notify", "success");
	}
	
	public Date stringToDate(String date_string) {
		if(date_string.equals("") || date_string == null){
			return null;
		}
		StringTokenizer tokenizer = new StringTokenizer(date_string, " ");
    	String date = tokenizer.nextToken();
    	String time = tokenizer.nextToken();
    	
    	tokenizer = new StringTokenizer(date,"-");
    	int year = Integer.parseInt(tokenizer.nextToken());
    	int month = Integer.parseInt(tokenizer.nextToken()) - 1;
    	int day = Integer.parseInt(tokenizer.nextToken());
    	
    	tokenizer = new StringTokenizer(time,":");
    	int hour = Integer.parseInt(tokenizer.nextToken());
    	int minute =  Integer.parseInt(tokenizer.nextToken());
    	int second =  Integer.parseInt(tokenizer.nextToken());
    	
    	return new Date(year,month,day,hour,minute,second);
	}
}