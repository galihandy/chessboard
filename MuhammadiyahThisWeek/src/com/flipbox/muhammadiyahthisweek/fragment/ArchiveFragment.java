package com.flipbox.muhammadiyahthisweek.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import com.flipbox.muhammadiyahthisweek.R;
import com.flipbox.muhammadiyahthisweek.adapter.SavedEventAdapter;
import com.flipbox.muhammadiyahthisweek.database.Database;
import com.flipbox.muhammadiyahthisweek.model.Event;
import com.flipbox.muhammadiyahthisweek.utils.MyUtil;
import com.flipbox.muhammadiyahthisweek.activity.DetailEventActivity;
import com.flipbox.muhammadiyahthisweek.utils.Constant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class ArchiveFragment extends ListFragment {

	public static final int CLASS_ID = 4;
	public static final int MAX_ARCHIVED_EVENT = 20;
	private ArrayList<Event> eventList;
	private Database db;
	private View view;
	private String[] keys;
	private RelativeLayout noEventLayout;

	public ArchiveFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.archive_event_list, null);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		db = MyUtil.getDatabase();
		eventList = db.getArchivedEventList();
		
		keys = Constant.KEYS;
		
		noEventLayout = (RelativeLayout) view.findViewById(R.id.noDataRelLayout);
		if(eventList.size() != 0){
			noEventLayout.setVisibility(View.GONE);
		} else {
			noEventLayout.setVisibility(View.VISIBLE);
		}
		
		SavedEventAdapter adapter = new SavedEventAdapter(getActivity(),
				eventList);
		setListAdapter(adapter);
	}

	public void addToBackstack() {

		if (getActivity() == null) {
			System.out.println("GetActivity == null");
		}
		getActivity().getSupportFragmentManager().beginTransaction()
				.addToBackStack(null).commit();
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		Event e = eventList.get(position);

		Bundle b = new Bundle();

		HashMap<String, Object> eventMap = eventToHashMap(e);

		b.putInt(Constant.STARTING_ACTIVITY, CLASS_ID);
		for (int i = 0; i < Constant.KEYS.length; i++) {
			String key = keys[i];
			String extra = (String) eventMap.get(key);
			b.putString(key, extra);
		}

		Intent i = new Intent(getActivity(),
				DetailEventActivity.class);
		i.putExtra(Constant.EVENT_BUNDLE, b);
		startActivity(i);
	}
	
	private HashMap<String, Object> eventToHashMap(Event e) {
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put(Database.EVENT_AUTHOR, e.getAuthor());
		map.put(Database.EVENT_CAT, e.getCategory());
		map.put(Database.EVENT_CONTACT, e.getContact());
		map.put(Database.EVENT_DATE_CREATED, e.getDateCreated());
		map.put(Database.EVENT_DATE_PUBLISHED, e.getDatePublished());
		map.put(Database.EVENT_DESC, e.getDesc());
		map.put(Database.EVENT_END_DATE, e.getEndDate());
		map.put(Database.EVENT_ID, e.getId());
		map.put(Database.EVENT_IMG_URL, e.getImageUrl());
		map.put(Database.EVENT_LATD, e.getLatitude());
		map.put(Database.EVENT_LONGTD, e.getLongitude());
		map.put(Database.EVENT_LOC, e.getLocation());
		map.put(Database.EVENT_NAME, e.getName());
		map.put(Database.EVENT_START_DATE, e.getStartDate());
		map.put(Database.EVENT_TICKET_PRC, e.getTicketPrice());

		return map;
	}
	
	
}
