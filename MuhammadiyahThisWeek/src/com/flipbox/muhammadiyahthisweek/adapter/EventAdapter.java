package com.flipbox.muhammadiyahthisweek.adapter;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.flipbox.muhammadiyahthisweek.database.Database;
import com.flipbox.muhammadiyahthisweek.utils.MyUtil;
import com.flipbox.muhammadiyahthisweek.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EventAdapter extends BaseAdapter {

	private final String thisWeekEventsLabel = "This Week Events";
	private final String allEventsLabel = "All Events";
	private final String noEvent = "No Event";
	private final int maxNonClickable = 4;
	private final int thisWeekLabelType = 1;
	private final int allEventsLabelType = 2;
	private final int case_1 = 1;
	private final int case_2 = 2;
	private final int case_3 = 3;
	private final int case_4 = 4;
	private LayoutInflater inflater;
	private List<HashMap<String, Object>> eventListData;
	private int thisWeekLabelPos;
	private int allEventsLabelPos;
	private int conditionCase;
	private boolean isAllEventsLabelCreated;

	public EventAdapter(Context context, List<HashMap<String, Object>> data) {

		if (context == null)
			return;

		eventListData = data;
		inflater = LayoutInflater.from(context);
		thisWeekLabelPos = 0;
		allEventsLabelPos = eventListData.size();
		isAllEventsLabelCreated = false;
		conditionCase = 0;
	}

	public int getCount() {

		return eventListData.size() + maxNonClickable;
	}

	public Object getItem(int position) {

		return eventListData.get(position);
	}

	public long getItemId(int position) {

		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		System.out.println("Event Pos " + position);
		View v = convertView;
		if (position - getDistance(conditionCase, position) >= eventListData
				.size()) {
			if (eventListData.size() != 0) {
				v = setNoAllEventView(v, position);
			} else {
				if(conditionCase != case_4)
					conditionCase = case_4;
				v = setNoEventAtAllView(v, position);
			}
			return v;
		}

		int distance = getDistance(conditionCase, position);
		HashMap<String, Object> eDataMap = eventListData.get(position
				- distance);

		String startDate = (String) eDataMap.get(Database.EVENT_START_DATE);

		if (position == 0) {
			System.out.println("Set This Week label pos " + position);
			v = setEventListLabel(v, thisWeekLabelType);
			thisWeekLabelPos = position;
			if (conditionCase == 0) {
				conditionCase = case_1;
			}
			return v;

		} else if (position == 1) {

			if (!isDateThisWeek(MyUtil.stringToDate(startDate))) {
				v = setEventListNoEvent(v);

				if (conditionCase == case_1) {
					conditionCase = case_3;
					allEventsLabelPos = position + 1;
				}

				System.out.println("No This Week Event");

				return v;
			}

		} else {
			System.out.println("Is date this week "
					+ isDateThisWeek(MyUtil.stringToDate(startDate)));
			if (!isDateThisWeek(MyUtil.stringToDate(startDate))
					&& (!isAllEventsLabelCreated || position == allEventsLabelPos)) {

				// System.out.println("all event lbl created : " +
				// isAllEventsLabelCreated);
				// System.out.println("position : " + position);

				v = setEventListLabel(v, allEventsLabelType);
				setAllEventsLabelPos(position);
				if (conditionCase == case_1) {
					conditionCase = case_2;
				}

				System.out.println("Set All Event Label ");

				return v;
			}
		}

		v = setEventView(v, eDataMap);
		return v;
	}

	private View setEventView(View v, HashMap<String, Object> eventData) {
		v = inflater.inflate(R.layout.list_feature_item, null);

		TextView eventNameTv = (TextView) v.findViewById(R.id.event_name);
		System.out.println("Event name " + eventData.get(Database.EVENT_NAME));
		eventNameTv.setText((CharSequence) eventData.get(Database.EVENT_NAME));

		TextView eventRegTv = (TextView) v.findViewById(R.id.event_regional);
		eventRegTv.setText(" "
				+ (CharSequence) eventData.get(Database.EVENT_LOC));

		TextView eventStartDate = (TextView) v.findViewById(R.id.event_date);
		String date = MyUtil.dateFormatter((String) eventData
				.get(Database.EVENT_START_DATE));
		eventStartDate.setText(date);

		ImageView thumbImg = (ImageView) v.findViewById(R.id.event_thumb);
		String imgUrl = (String) eventData.get(Database.EVENT_IMG_URL);
		ImageLoader.getInstance().displayImage(imgUrl, thumbImg);

		return v;
	}

	private View setEventListNoEvent(View v) {
		v = inflater.inflate(R.layout.list_no_event_item, null);
		TextView tv = (TextView) v.findViewById(R.id.no_event_item);
		tv.setText(noEvent);
		v.setClickable(false);
		return v;
	}

	private View setEventListLabel(View v, int labelType) {
		v = inflater.inflate(R.layout.list_event_label, null);
		TextView tv = (TextView) v.findViewById(R.id.event_list_label);

		switch (labelType) {
		case thisWeekLabelType:
			tv.setText(thisWeekEventsLabel);
			// System.out.println("Set This Week label");

			break;

		case allEventsLabelType:
			tv.setText(allEventsLabel);
			isAllEventsLabelCreated = true;
			// System.out.println("Set all event label");
			break;
		}

		v.setClickable(false);
		return v;
	}

	private View setNoAllEventView(View v, int position) {

		int i = eventListData.size() + maxNonClickable - position;
		System.out.println("i" + i);
		switch (i) {
		case 3:
			v = setEventListLabel(v, allEventsLabelType);
			allEventsLabelPos = position;
			break;

		case 2:
			if (conditionCase != case_1) {
				v = inflater.inflate(R.layout.dummy_view, null);
				v.setVisibility(View.GONE);
			} else {
				v = setEventListNoEvent(v);
			}
			break;

		case 1:
			v = inflater.inflate(R.layout.dummy_view, null);
			v.setVisibility(View.GONE);
			break;
		}
		return v;
	}

	private View setNoEventAtAllView(View v, int position) {

		switch (position) {
		case 0:
			v = setEventListLabel(v, thisWeekLabelType);
			thisWeekLabelPos = position;
			break;

		case 1:
			v = setEventListNoEvent(v);
			break;
		case 2:
			v = setEventListLabel(v, allEventsLabelType);
			allEventsLabelPos = position;
			break;
		case 3:
			v = setEventListNoEvent(v);
			break;

		}
		return v;
	}

	private boolean isDateThisWeek(Date date) {
		boolean result = false;
		Calendar c = Calendar.getInstance();
		int todayDate = c.get(Calendar.DATE);
		int eventDate = date.getDate();

		if (eventDate - todayDate < 7) {
			int todayDay = c.get(Calendar.DAY_OF_WEEK);
			int eventDay = date.getDay();

			if (eventDay - todayDay >= 0) {

				result = true;
			}
		}

		return result;
	}

	private int getDistance(int condition, int position) {
		int distance = 0;

		if (position == 0) {
			return distance;
		}

		switch (condition) {
		case case_1:
			distance = 1;
			break;

		case case_2:
			if (position <= allEventsLabelPos) {
				distance = 1;
			} else if (position > allEventsLabelPos) {
				distance = 2;
			}

			break;
		case case_3:
			if (position <= allEventsLabelPos) {
				distance = position;
			} else {
				distance = 3;
			}
			break;
		}

		return distance;
	}

	public int getDistance(int position) {
		int distance = 0;

		switch (conditionCase) {
		case case_1:
			distance = 1;
			break;

		case case_2:
			if (position < allEventsLabelPos) {
				distance = 1;
			} else if (position > allEventsLabelPos) {
				distance = 2;
			}
			break;
		case case_3:
			distance = 3;
			break;
		}

		return distance;
	}

	public boolean isViewClickable(int position) {
		boolean clickable = false;
		
		System.out.println("Case " + conditionCase);
		System.out.println("This Week Label Pos " + thisWeekLabelPos);
		System.out.println("All event label pos " + allEventsLabelPos);

		switch (this.conditionCase) {
		case case_1:
			if (position > thisWeekLabelPos && position < allEventsLabelPos)
				clickable = true;
			break;

		case case_2:
			if (position != thisWeekLabelPos && position != allEventsLabelPos)
				clickable = true;
			break;

		case case_3:
			if (position > 2)
				clickable = true;
			break;
			
		case case_4:
			clickable = false;
			break;
		}
		return clickable;
	}

	public int getThisWeekLabelPos() {
		return thisWeekLabelPos;
	}

	public int getAllEventsLabelPos() {
		return allEventsLabelPos;
	}

	public void setAllEventsLabelPos(int position) {
		this.allEventsLabelPos = position;

		System.out.println("All Event Label Pos " + position);
	}

}
