package com.flipbox.myevent;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class DetailEventActivity extends Activity {

	private final int classIdDefault = 10;
	private String[] keys;
	private Date startDate;
	private HashMap<String, String> map;

	private Intent in;
	private TextView eventNameTV;
	private TextView eventDateTV;
	private TextView eventLocTV;
	private TextView eventTicketTV;
	private TextView eventCatTV;
	private TextView eventAuthorTV;
	private TextView eventContactTV;
	private TextView eventDescTV;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_feature_event);

		// set view element and its value
		setViewElement();

		// getting intent data
		in = getIntent();

		// Get JSON values from previous intent
		keys = MyUtil.KEYS;
		System.out.println("keys: " + Arrays.toString(keys));
		map = new HashMap<String, String>();

		for (int i = 0; i < keys.length; i++) {
			String key = keys[i];
			String extra = in.getStringExtra(key);
			System.out.println("extra: " + extra);
			map.put(key, extra);
		}

		setViewElementValue(map);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		int startingActId = in.getIntExtra("starting act",
				classIdDefault);
		if (startingActId == SavedEventActivity.CLASS_ID) {
			inflater.inflate(R.menu.detail_saved_menu, menu);
		} else if (startingActId != classIdDefault) {

			inflater.inflate(R.menu.detail_event_menu, menu);
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.add_event:

			addEvent(map);

			if (startDate != null)
				showNotification();
			break;
		case R.id.remove_event:
			Database.removeEvent(map.get(Database.EVENT_ID));
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void showNotification() {
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		// notification set at 24 hours before the event
		long millis = startDate.getTime() - 86400000;
		Notification notification = new Notification(R.drawable.ic_launcher,
				"You have event tomorrow.", millis);

		// Hide the notification after its selected
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		Intent intent = new Intent(this, DetailEventActivity.class);
		// put extra to activity intent that will be started from notification
		for (int i = 0; i < keys.length; i++) {
			String key = keys[i];
			String extra = map.get(key);
			intent.putExtra(key, extra);
		}
		intent.putExtra("keys", keys);

		PendingIntent activity = PendingIntent.getActivity(this, 0, intent, 0);

		notification.setLatestEventInfo(this, "Notification",
				"Click for more info", activity);

		// Set default sound
		notification.defaults |= Notification.DEFAULT_SOUND;
		// Set the default Vibration
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
		if (date_string.equals("") || date_string == null) {
			return null;
		}
		StringTokenizer tokenizer = new StringTokenizer(date_string, " ");
		String date = tokenizer.nextToken();
		String time = tokenizer.nextToken();

		tokenizer = new StringTokenizer(date, "-");
		int year = Integer.parseInt(tokenizer.nextToken());
		int month = Integer.parseInt(tokenizer.nextToken()) - 1;
		int day = Integer.parseInt(tokenizer.nextToken());

		tokenizer = new StringTokenizer(time, ":");
		int hour = Integer.parseInt(tokenizer.nextToken());
		int minute = Integer.parseInt(tokenizer.nextToken());
		int second = Integer.parseInt(tokenizer.nextToken());

		return new Date(year, month, day, hour, minute, second);
	}

	private void setViewElement() {

		eventNameTV = (TextView) findViewById(R.id.event_name);
		eventDateTV = (TextView) findViewById(R.id.event_date);
		eventLocTV = (TextView) findViewById(R.id.event_location);
		eventTicketTV = (TextView) findViewById(R.id.event_ticket);
		eventCatTV = (TextView) findViewById(R.id.event_category);
		eventAuthorTV = (TextView) findViewById(R.id.event_author);
		eventContactTV = (TextView) findViewById(R.id.event_contact);
		eventDescTV = (TextView) findViewById(R.id.event_description);
	}

	private void setViewElementValue(HashMap<String, String> valueMap) {

		// set event name ************************************
		String name = valueMap.get(Database.EVENT_NAME);
		eventNameTV.setText(name);

		// set event date ************************************
		String eventStartDate = "Start : "
				+ MyUtil.dateFormatter(valueMap.get(Database.EVENT_START_DATE));
		String eventEndDate = "End : ";
		if (valueMap.get(Database.EVENT_END_DATE).equals("null")) {
			eventEndDate += "-";
		} else {
			eventEndDate += MyUtil.dateFormatter(valueMap
					.get(Database.EVENT_END_DATE));
		}
		String date = eventStartDate + "\n" + eventEndDate;
		eventDateTV.setText(date);

		startDate = stringToDate(valueMap.get(Database.EVENT_START_DATE));

		// set event location ********************************
		String loc = valueMap.get(Database.EVENT_LOC);
		String longtd = "-";
		String latd = "-";

		if (!valueMap.get(Database.EVENT_LONGTD).equals("null")) {
			longtd = valueMap.get(Database.EVENT_LONGTD);
		}
		if (!valueMap.get(Database.EVENT_LATD).equals("null")) {
			latd = valueMap.get(Database.EVENT_LATD);
		}

		String eventLoc = loc + "\n" + "lat. " + latd + ", " + "long. "
				+ longtd;
		eventLocTV.setText(eventLoc);

		// set event ticket ************************************
		String ticket = valueMap.get(Database.EVENT_TICKET_PRC);
		eventTicketTV.setText(ticket);

		// set event category **********************************
		String category = valueMap.get(Database.EVENT_CAT);
		eventCatTV.setText(category);

		// set event author ************************************
		String author = valueMap.get(Database.EVENT_AUTHOR);
		eventAuthorTV.setText(author);

		// set event contact ***********************************
		String contact = valueMap.get(Database.EVENT_CONTACT);
		eventContactTV.setText(contact);

		// set event description *******************************
		String description = valueMap.get(Database.EVENT_DESC);
		eventDescTV.setText(description);

	}

	private void addEvent(HashMap<String, String> valueMap) {
		String id = valueMap.get(Database.EVENT_ID);
		String name = valueMap.get(Database.EVENT_NAME);
		String desc = valueMap.get(Database.EVENT_DESC);
		String startDate = valueMap.get(Database.EVENT_START_DATE);
		String endDate = valueMap.get(Database.EVENT_END_DATE);
		String loc = valueMap.get(Database.EVENT_LOC);
		String cat = valueMap.get(Database.EVENT_CAT);
		String imgUrl = valueMap.get(Database.EVENT_IMG_URL);
		String contact = valueMap.get(Database.EVENT_CONTACT);
		String datePublished = valueMap.get(Database.EVENT_DATE_PUBLISHED);
		String dateCreated = valueMap.get(Database.EVENT_DATE_CREATED);
		String author = valueMap.get(Database.EVENT_AUTHOR);
		String ticketPrc = valueMap.get(Database.EVENT_TICKET_PRC);
		String lat = valueMap.get(Database.EVENT_LATD);
		String longtd = valueMap.get(Database.EVENT_LONGTD);

		Event e = new Event(id, name, desc, startDate, endDate, loc, cat,
				imgUrl, contact, dateCreated, datePublished, author, ticketPrc,
				lat, longtd);

		Database.insertEvent(e);
	}
}
