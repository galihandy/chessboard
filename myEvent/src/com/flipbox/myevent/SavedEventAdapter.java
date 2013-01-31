/**
 * 
 */
package com.flipbox.myevent;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author Galih
 * 
 */
public class SavedEventAdapter extends BaseAdapter {

	private ArrayList<Event> savedEventList;
	private LayoutInflater inflater;
	private int resource;

	public SavedEventAdapter(Context context, int resource,
			ArrayList<Event> savedEventList) {
		this.savedEventList = savedEventList;
		this.resource = resource;
		this.inflater = LayoutInflater.from(context);
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return savedEventList.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return savedEventList.get(position);
	}

	public long getItemId(int position) {
		Event e = savedEventList.get(position);
		return Long.parseLong(e.getId());
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		View v;

		if (convertView == null) {
			convertView = inflater.inflate(resource, parent, false);
		}
		v = convertView;

		if (position % 2 != 0) {
			v.setBackgroundColor(Color.LTGRAY);
		} else {
			v.setBackgroundColor(Color.WHITE);
		}
		
		TextView eventName = (TextView) v.findViewById(R.id.event_name);
		TextView startDate = (TextView) v.findViewById(R.id.event_startdate);

		Event e = savedEventList.get(position);
		eventName.setText(e.getName());
		startDate.setText(MyUtil.dateFormatter(e.getStartDate()));

		return v;
	}
}
