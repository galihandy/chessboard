package com.flipbox.muhammadiyahthisweek.fragment;

import java.util.Date;
import java.util.HashMap;

import com.flipbox.muhammadiyahthisweek.database.Database;
import com.flipbox.muhammadiyahthisweek.model.Event;
import com.flipbox.muhammadiyahthisweek.service.AlarmReceiver;
import com.flipbox.muhammadiyahthisweek.utils.Constant;
import com.flipbox.muhammadiyahthisweek.utils.MyUtil;
import com.flipbox.muhammadiyahthisweek.R;
import com.flipbox.muhammadiyahthisweek.fragment.ArchiveFragment;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DetailEventFragment extends Fragment {

	private final String successSavedEventMsg = " successfully saved";
	private final String duplicationSavedMsg = " is on your event list";

	private String[] keys;
	private int startingActId;
	private HashMap<String, Object> map;

	private Bundle b;
	private View view;
	private TextView eventNameTV;
	private TextView eventDateTV;
	private TextView eventLocTV;
	private TextView eventDescTV;
	private TextView eventCategoryTV;
	private TextView eventAuthorTV;
	private TextView eventContactTV;
	private TextView eventTicketPriceTV;
	private TextView tbName;
	private ImageView eventImg;
	private ImageView tbIcon;
	private RelativeLayout textBaseBtn;
	private Database db;

	// private RayMenu rayMenu;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.detail_feature_event, null);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		db = MyUtil.getDatabase();
		// getting intent data

		b = getArguments();
		switchToLoadingLayout();

		startingActId = b.getInt(Constant.STARTING_ACTIVITY);
		System.out.println("Starting id " + startingActId);

		// Get JSON values from previous intent
		keys = Constant.KEYS;

		map = new HashMap<String, Object>();

		for (int i = 0; i < keys.length; i++) {
			String key = keys[i];
			String extra = b.getString(key);

			map.put(key, extra);
		}

		// init view element
		initViewElement();
		setViewElementValue(map);
	}

	public static void stopAlarm(Context context, String eventName) {
		AlarmManager alarmMgr = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);

		// Hide the notification after its selected

		Intent intent = new Intent(context, AlarmReceiver.class);
		intent.setType(Constant.ALARM_REQUEST_CODE + " " + eventName);
		PendingIntent pIntent = PendingIntent.getBroadcast(context,
				Constant.ALARM_REQUEST_CODE, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		alarmMgr.cancel(pIntent);
	}

	private void setAlarm(Context context, Date date) {

		// alarm set at 24 hours before the event
		long millis = date.getTime() - Constant.A_DAY;
		AlarmManager alarmMgr = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);

		// Hide the notification after its selected

		Intent intent = new Intent(getActivity(), AlarmReceiver.class);
		intent.setType(Constant.ALARM_REQUEST_CODE + " "
				+ map.get(Database.EVENT_NAME));

		// put extra to activity intent that will be started from notification
		for (int i = 0; i < keys.length; i++) {
			String key = keys[i];
			String extra = (String) map.get(key);
			intent.putExtra(key, extra);
		}

		intent.putExtra("alarm time", millis);

		PendingIntent pIntent = PendingIntent.getBroadcast(getActivity(),
				Constant.ALARM_REQUEST_CODE, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		alarmMgr.set(AlarmManager.RTC_WAKEUP, millis, pIntent);
	}

	private void initViewElement() {

		if (startingActId == AlarmReceiver.CLASS_ID)
			MyUtil.initLazyImageLoader(getActivity());

		eventNameTV = (TextView) view.findViewById(R.id.event_name);
		eventDateTV = (TextView) view.findViewById(R.id.event_date);
		eventLocTV = (TextView) view.findViewById(R.id.event_location);
		eventDescTV = (TextView) view.findViewById(R.id.event_description);
		eventCategoryTV = (TextView) view.findViewById(R.id.event_category);
		eventAuthorTV = (TextView) view.findViewById(R.id.event_author);
		eventContactTV = (TextView) view.findViewById(R.id.event_contact);
		eventTicketPriceTV = (TextView) view.findViewById(R.id.event_ticket);
		eventImg = (ImageView) view.findViewById(R.id.event_image);
		tbName = (TextView) view.findViewById(R.id.tb_btn_name);
		tbIcon = (ImageView) view.findViewById(R.id.tb_btn_icon);

		textBaseBtn = (RelativeLayout) view.findViewById(R.id.text_base_btn);

		// rayMenu = (RayMenu) view.findViewById(R.id.ray_menu);
		// setRayMenuItem(rayMenu);

	}

	private void setTextBaseButtonAction() {

		switch (startingActId) {
		case SavedEventFragment.CLASS_ID:
			System.out.println("Saved event");
			tbName.setText(R.string.detail_event_rmv_menu_title);
			tbIcon.setImageResource(R.drawable.ic_delete_myevent);
			textBaseBtn.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					Bundle b = new Bundle();
					Event e = hashMapToEvent(map);

					b.putInt(ConfirmationDialog.DIALOG_TYPE,
							ConfirmationDialog.DELETE_FROM_DETAIL);
					b.putSerializable(ConfirmationDialog.DATABASE_KEY, db);
					b.putSerializable(ConfirmationDialog.EVENT_KEY, e);

					ConfirmationDialog dialog = new ConfirmationDialog();
					dialog.setArguments(b);
					dialog.setTargetFragment(DetailEventFragment.this,
							ConfirmationDialog.DELETE_FROM_DETAIL);
					dialog.show(getFragmentManager(),
							ConfirmationDialog.DELETE_FROM_DETAIL_TAG);

				}
			});
			break;
		case AlarmReceiver.CLASS_ID:
			System.out.println("From alarm receiver");
			textBaseBtn.setVisibility(View.GONE);
			break;

		case ArchiveFragment.CLASS_ID:
			System.out.println("From archive");
			textBaseBtn.setVisibility(View.GONE);
			break;

		default:
			System.out.println("default");
			tbIcon = (ImageView) view.findViewById(R.id.tb_btn_icon);
			tbIcon.setImageResource(R.drawable.ic_add_myevent);
			tbName.setText(R.string.detail_event_add_menu_title);
			textBaseBtn.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					String eventId = (String) map.get(Database.EVENT_ID);
					String eventName = (String) map.get(Database.EVENT_NAME);
					String msg;

					if (!db.isEventExist(eventId)) {
						msg = eventName + successSavedEventMsg;
						Date startDate = MyUtil.stringToDate((String) map
								.get(Database.EVENT_START_DATE));
						System.out.println("DEFAULT STARTDATE " + startDate);
						setAlarm(getActivity(), startDate);
						addEvent(map);
					} else {
						msg = eventName + duplicationSavedMsg;
					}

					Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT)
							.show();

				}
			});
			break;
		}
	}

	private void setViewElementValue(HashMap<String, Object> valueMap) {

		// set event name ************************************
		String name = (String) valueMap.get(Database.EVENT_NAME);
		eventNameTV.setText(name);

		// set event date ************************************
		String date = MyUtil.dateFormatter((String) valueMap
				.get(Database.EVENT_START_DATE));
		eventDateTV.setText(date);

		// set event location ********************************
		String loc = (String) valueMap.get(Database.EVENT_LOC);

		String eventLoc = (String) loc;
		eventLocTV.setText(" " + eventLoc);

		// set event description *******************************
		String description = (String) valueMap.get(Database.EVENT_DESC);
		eventDescTV.setText(description);

		// set event category
		String category = (String) valueMap.get(Database.EVENT_CAT);
		eventCategoryTV.setText(" " + category);

		// set event author
		String author = (String) valueMap.get(Database.EVENT_AUTHOR);
		eventAuthorTV.setText(" " + author);

		// set event contact
		String contact = (String) valueMap.get(Database.EVENT_CONTACT);
		eventContactTV.setText(" " + contact);

		// set event ticket price
		String ticketPrice = (String) valueMap.get(Database.EVENT_TICKET_PRC);
		if (ticketPrice != null && !ticketPrice.equals("")) {
			eventTicketPriceTV.setText(" " + ticketPrice);
		} else {
			eventTicketPriceTV.setVisibility(View.GONE);
			TextView ticketPriceLabel = (TextView) view
					.findViewById(R.id.ticket_label);
			ticketPriceLabel.setVisibility(View.GONE);
		}

		// set event image *************************************
		String url = (String) valueMap.get(Database.EVENT_IMG_URL);
		ImageLoader.getInstance().displayImage(url, eventImg);

		setTextBaseButtonAction();
		switchToContentLayout();
	}

	private void addEvent(HashMap<String, Object> valueMap) {
		String id = (String) valueMap.get(Database.EVENT_ID);
		String name = (String) valueMap.get(Database.EVENT_NAME);
		String desc = (String) valueMap.get(Database.EVENT_DESC);
		String startDate = (String) valueMap.get(Database.EVENT_START_DATE);
		String endDate = (String) valueMap.get(Database.EVENT_END_DATE);
		String loc = (String) valueMap.get(Database.EVENT_LOC);
		String cat = (String) valueMap.get(Database.EVENT_CAT);
		String imgUrl = (String) valueMap.get(Database.EVENT_IMG_URL);
		String contact = (String) valueMap.get(Database.EVENT_CONTACT);
		String datePublished = (String) valueMap
				.get(Database.EVENT_DATE_PUBLISHED);
		String dateCreated = (String) valueMap.get(Database.EVENT_DATE_CREATED);
		String author = (String) valueMap.get(Database.EVENT_AUTHOR);
		String ticketPrc = (String) valueMap.get(Database.EVENT_TICKET_PRC);
		String lat = (String) valueMap.get(Database.EVENT_LATD);
		String longtd = (String) valueMap.get(Database.EVENT_LONGTD);

		Event e = new Event(id, name, desc, startDate, endDate, loc, cat,
				imgUrl, contact, dateCreated, datePublished, author, ticketPrc,
				lat, longtd);

		db.insertEvent(e);
	}

	private Event hashMapToEvent(HashMap<String, Object> map) {
		Event e = new Event();

		e.setAuthor((String) map.get(Database.EVENT_AUTHOR));
		e.setCategory((String) map.get(Database.EVENT_CAT));
		e.setContact((String) map.get(Database.EVENT_CONTACT));
		e.setDateCreated((String) map.get(Database.EVENT_DATE_CREATED));
		e.setDatePublished((String) map.get(Database.EVENT_DATE_PUBLISHED));
		e.setDesc((String) map.get(Database.EVENT_DESC));
		e.setEndDate((String) map.get(Database.EVENT_END_DATE));
		e.setId((String) map.get(Database.EVENT_ID));
		e.setImageUrl((String) map.get(Database.EVENT_IMG_URL));
		e.setLatitude((String) map.get(Database.EVENT_LATD));
		e.setLongitude((String) map.get(Database.EVENT_LONGTD));
		e.setLocation((String) map.get(Database.EVENT_LOC));
		e.setName((String) map.get(Database.EVENT_NAME));
		e.setStartDate((String) map.get(Database.EVENT_START_DATE));
		e.setTicketPrice((String) map.get(Database.EVENT_TICKET_PRC));

		return e;
	}

	private void switchToLoadingLayout() {
		// progress bar layout
		RelativeLayout loadingLayout = (RelativeLayout) view
				.findViewById(R.id.loadingRelLayout);
		// content layout
		RelativeLayout contentLayout = (RelativeLayout) view
				.findViewById(R.id.content_layout);

		// show progress bar & hide the listview & hide the arc menu
		loadingLayout.setVisibility(View.VISIBLE);
		contentLayout.setVisibility(View.GONE);
	}

	private void switchToContentLayout() {
		// progress bar layout
		RelativeLayout loadingLayout = (RelativeLayout) view
				.findViewById(R.id.loadingRelLayout);
		// content layout
		RelativeLayout contentLayout = (RelativeLayout) view
				.findViewById(R.id.content_layout);

		// hide progress bar & show the listview & show the arc menu
		loadingLayout.setVisibility(View.GONE);
		contentLayout.setVisibility(View.VISIBLE);
		// rayMenu.setVisibility(View.VISIBLE);
	}
}
