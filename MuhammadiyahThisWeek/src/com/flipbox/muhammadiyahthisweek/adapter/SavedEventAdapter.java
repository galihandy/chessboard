/**
 * 
 */
package com.flipbox.muhammadiyahthisweek.adapter;

import java.util.ArrayList;

import com.flipbox.muhammadiyahthisweek.model.Event;
import com.flipbox.muhammadiyahthisweek.utils.MyUtil;
import com.flipbox.muhammadiyahthisweek.R;

import android.content.Context;
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

	public SavedEventAdapter(Context context, ArrayList<Event> savedEventList) {
		
		if(context == null)
			return;
		
		this.savedEventList = savedEventList;
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
			convertView = inflater.inflate(R.layout.list_saved_event_item,
					parent, false);
		}
		v = convertView;
		Event e = savedEventList.get(position);
		
		String date = MyUtil.dateFormatter(e.getStartDate());
		
		TextView eventName = (TextView) v.findViewById(R.id.saved_event_name);
		TextView eventDate = (TextView) v.findViewById(R.id.saved_event_date);
		TextView eventRegional = (TextView) v.findViewById(R.id.saved_event_reg);

		eventName.setText(e.getName());
		eventDate.setText(date);
		eventRegional.setText(" " + e.getLocation());

		return v;
	}
}
