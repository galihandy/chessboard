package com.flipbox.myevent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

public class SavedEventActivity extends ListActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_event_placeholder);
		
		ArrayList<Event> list = Database.getMyEventList();
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<HashMap<String, Object>> list_view = new ArrayList<HashMap<String, Object>>();
		for(int i = 0; i < list.size(); i++) {
			map.put("title", list.get(i).getTitle());
			map.put("date", list.get(i).getStart_date().toString());
			list_view.add(map);
		}
		
		ArrayAdapter<Event> adapter = new ArrayAdapter<Event>(this, android.R.layout.simple_list_item_1, list);

		setListAdapter(adapter);
	}
}
