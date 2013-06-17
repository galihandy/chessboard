package com.flipbox.muhammadiyahthisweek.service;

import java.util.Calendar;
import java.util.Date;

import com.flipbox.muhammadiyahthisweek.activity.DetailEventActivity;
import com.flipbox.muhammadiyahthisweek.database.Database;
import com.flipbox.muhammadiyahthisweek.utils.Constant;
import com.flipbox.muhammadiyahthisweek.utils.MyUtil;
import com.flipbox.muhammadiyahthisweek.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

	public static final int CLASS_ID = 3;
	private String notif_bar_msg = "You have event ";
	private final String notif_title = "MyEvent";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		showNotification(context, intent);
	}

	private void showNotification(Context context, Intent intent) {
		
		NotificationManager notifMgr = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		String date = intent.getStringExtra(Database.EVENT_START_DATE);
		System.out.println("Date " + date);
		
		Date startDate = MyUtil.stringToDate(date);
		
		if(isDateToday(startDate)){
			notif_bar_msg += "today";
		} else {
			notif_bar_msg += "tomorrow";
		}
		
		// notification set at 24 hours before the event
		long timeInMillis = intent.getLongExtra("alarm time", 0);
		Notification notification = new Notification(R.drawable.ic_launcher,
				notif_bar_msg, timeInMillis);

		// Hide the notification after its selected
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		Intent in = new Intent(context, DetailEventActivity.class);
		in.putExtra("source", "alarm receiver");
		
		Bundle b = new Bundle();
		b.putInt(Constant.STARTING_ACTIVITY, CLASS_ID);
		// put extra to activity intent that will be started from notification
		for (int i = 0; i < Constant.KEYS.length; i++) {
			String key = Constant.KEYS[i];
			String extra = intent.getStringExtra(key);
			System.out.println("Alarm Receiver key " + key + " extra " + extra);
			b.putString(key, extra);
		}
		
		in.putExtra(Constant.EVENT_BUNDLE, b);

		PendingIntent pIntent = PendingIntent.getActivity(context, 0, in, 0);

		String message = b.getString(Database.EVENT_NAME)
				+ " will be held tommorow.";
		notification.setLatestEventInfo(context, notif_title, message, pIntent);

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

		notifMgr.notify(0, notification);
		Log.i("Notify", "success");
	}
	
	private boolean isDateToday(Date date) {
		boolean result = false;
		Calendar c = Calendar.getInstance();
		int todayDay = c.get(Calendar.DATE);
		int eventDay = date.getDate();
		
		if(eventDay == todayDay)
			result = true;
		
		return result;
	}

}
