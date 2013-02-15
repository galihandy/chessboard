package com.flipbox.arsenalindonesia;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class SavedEventActivity extends ListActivity {

	public static final int CLASS_ID = 2;
	private ArrayList<Event> eventList;
	private String[] keys;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.saved_item);

		eventList = Database.getMyEventList();
		keys = MyUtil.KEYS;

		SavedEventAdapter adapter = new SavedEventAdapter(getBaseContext(),
				R.layout.list_feature_item, eventList);
		setListAdapter(adapter);
		registerForContextMenu(getListView());
	}

	@Override
	protected void onResume() {
		super.onResume();
		eventList = Database.getMyEventList();
		SavedEventAdapter adapter = new SavedEventAdapter(getBaseContext(),
				R.layout.list_feature_item, eventList);
		setListAdapter(adapter);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.context_menu_remove:
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
					.getMenuInfo();
			Event e = eventList.get(info.position);
			Database.removeEvent(e.getId());
			DetailEventActivity.stopAlarm(getBaseContext(), e.getName());

			eventList = Database.getMyEventList();
			SavedEventAdapter adapter = new SavedEventAdapter(getBaseContext(),
					R.layout.list_feature_item, eventList);
			setListAdapter(adapter);
			break;
		}

		return super.onContextItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.saved_context_menu, menu);

		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		Event e = eventList.get(info.position);
		menu.setHeaderTitle(e.getName());

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		super.onListItemClick(l, v, position, id);

		Event e = eventList.get(position);

		Intent in = new Intent(getBaseContext(), DetailEventActivity.class);

		HashMap<String, Object> eventMap = eventToHashMap(e);

		in.putExtra("starting act", CLASS_ID);
		for (int i = 0; i < MyUtil.KEYS.length; i++) {
			String key = keys[i];
			String extra = (String) eventMap.get(key);
			in.putExtra(key, extra);
		}
		startActivity(in);
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