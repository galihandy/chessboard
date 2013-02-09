package com.flipbox.myevent;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

	private final String notif_bar_msg = "You have event tomorrow";
	private final String notif_title = "MyEvent";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		showNotification(context, intent);
	}

	private void showNotification(Context context, Intent intent) {
		NotificationManager notifMgr = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		// notification set at 24 hours before the event
		long timeInMillis = intent.getLongExtra("alarm time", 0);
		Notification notification = new Notification(R.drawable.ic_launcher,
				notif_bar_msg, timeInMillis);

		// Hide the notification after its selected
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		Intent in = new Intent(context, DetailEventActivity.class);
		// put extra to activity intent that will be started from notification
		for (int i = 0; i < MyUtil.KEYS.length; i++) {
			String key = MyUtil.KEYS[i];
			String extra = intent.getStringExtra(key);
			in.putExtra(key, extra);
		}

		PendingIntent pIntent = PendingIntent.getActivity(context, 0, in, 0);

		String message = in.getStringExtra(Database.EVENT_NAME)
				+ " will be held tommorow.";
		notification.setLatestEventInfo(context, notif_title,
				message, pIntent);

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

}
