package com.flipbox.muhammadiyahthisweek.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import com.flipbox.muhammadiyahthisweek.activity.DetailEventActivity;
import com.flipbox.muhammadiyahthisweek.adapter.SavedEventAdapter;
import com.flipbox.muhammadiyahthisweek.database.Database;
import com.flipbox.muhammadiyahthisweek.model.Event;
import com.flipbox.muhammadiyahthisweek.utils.Constant;
import com.flipbox.muhammadiyahthisweek.utils.MyUtil;
import com.flipbox.muhammadiyahthisweek.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class SavedEventFragment extends ListFragment {

	public static final int CLASS_ID = 2;

	private ArrayList<Event> eventList;
	private String[] keys;
	private Database db;
	private View view;
	private RelativeLayout noEventLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		System.out.println("SAVED oncreate called");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.saved_event_list, null);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("SAVED onactivitycreated called");

		db = MyUtil.getDatabase();

		eventList = db.getMyEventList();
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
		registerForContextMenu(getListView());
	}

	public void addToBackstack() {
		
		if (getActivity() == null){
			System.out.println("GetActivity == null");
		}
		getActivity().getSupportFragmentManager().beginTransaction()
				.addToBackStack(null).commit();
	}

	@Override
	public void onResume() {
		super.onResume();
		System.out.println("On Resume");
		eventList = db.getMyEventList();
		
		if(eventList.size() != 0){
			noEventLayout.setVisibility(View.GONE);
		} else {
			noEventLayout.setVisibility(View.VISIBLE);
		}
		
		SavedEventAdapter adapter = new SavedEventAdapter(getActivity(),
				eventList);
		setListAdapter(adapter);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.context_menu_remove:

			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
					.getMenuInfo();
			Bundle b = new Bundle();
			Event e = eventList.get(info.position);

			b.putInt(ConfirmationDialog.DIALOG_TYPE,
					ConfirmationDialog.DELETE_FROM_LIST);
			b.putSerializable(ConfirmationDialog.DATABASE_KEY, this.db);
			b.putSerializable(ConfirmationDialog.EVENT_KEY, e);

			ConfirmationDialog dialog = new ConfirmationDialog();
			dialog.setArguments(b);
			dialog.setTargetFragment(this, ConfirmationDialog.DELETE_FROM_LIST);
			dialog.show(getFragmentManager(),
					ConfirmationDialog.DELETE_FROM_LIST_TAG);

			break;
		}

		return super.onContextItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.saved_context_menu, menu);

		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		Event e = eventList.get(info.position);
		menu.setHeaderTitle(e.getName());

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
