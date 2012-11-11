package com.flipbox.myevent;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
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
import android.widget.Toast;

public class DetailFeatureEventActivity extends Activity {

	public static final String TAG_ID = "id";
	public static final String TAG_TITLE = "title";
	public static final String TAG_STARTDATE = "startDate";
	public static final String TAG_ENDDATE = "endDate";
	public static final String TAG_LOCATION = "location";
	public static final String TAG_IMAGE = "image";
	public static final String TAG_CATEGORY = "category";
	public static final String TAG_DESCRIPTION = "description";
	public static final String TAG_LATITUDE = "latitude";

	String title;
	Date date;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_feature_event);

		// getting intent data
		Intent in = getIntent();

		// Get JSON values from previous intent
		String[] keys = in.getStringArrayExtra("keys");
		System.out.println("keys: " + Arrays.toString(keys));
		HashMap<String, String> map = new HashMap<String, String>();

		String desc = null;
		for (int i = 0; i < keys.length; i++) {
			String key = keys[i];
			String extra = in.getStringExtra(key);
			System.out.println("extra: " + extra);
			map.put(key, extra);
			desc += key + ": " + extra + "\n";
		}
		Log.i("desc", desc);
		TextView lblName = (TextView) findViewById(R.id.name_label2);
		TextView lblDesc = (TextView) findViewById(R.id.description2);
		TextView lblDate = (TextView) findViewById(R.id.date2);
		
		title = map.get(TAG_TITLE);
		String dt = map.get(TAG_STARTDATE);
		date = stringToDate(dt);
		lblName.setText(map.get(TAG_TITLE));
		if(desc!=null)
			lblDesc.setText(desc);
		if(dt!=null)
			lblDate.setText(dt);
	}
	
	public void onClick(View view) {
		switch(view.getId()) {
		case R.id.add_event:
			//Event event = new Event(id, title, date, null, desc, null, null);
			Database.insertEvent(0, title, "", date, null, null, null);
			Log.i("Insert Event", title+"");
			if(date != null)
				showNotification();
			break;
		case R.id.purchase:
			/*String url = "http://m.tiket.com";
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(url));
			startActivity(i);*/
			Toast.makeText(this, "Not Implemented Yet", Toast.LENGTH_SHORT).show();
			break;
		}
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