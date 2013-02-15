package com.flipbox.arsenalindonesia;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;

public class MainActivity extends TabActivity {

	public static final String BASE_URL = "http://conanx.id1945.com/index.php";
	public static final String GET_ALL_EVENT = BASE_URL + "/service/allEvent";
	public static final String GET_EVENT_BY_ID = BASE_URL
			+ "/service/detailEvent?id=";
	public static final String GET_EVENT_BY_NAME = BASE_URL
			+ "/service/eventByName?name=";
	public static final String GET_EVENT_BY_LOC = BASE_URL
			+ "/service/eventByLocation?location=";
	public static final String GET_EVENT_BY_AUTHOR = BASE_URL
			+ "/service/eventByAuthor?author=";
	public static final String GET_EVENT_BY_TIME = BASE_URL
			+ "/service/eventByTime?time=";
	public static final String GET_EVENT_BY_CAT = BASE_URL
			+ "/service/eventByCategory?category=";
	public static final String GENERIC_QUERY = BASE_URL + "/service/query?";
	public static final String GET_ALL_CATEGORY = BASE_URL
			+ "/service/allCategory";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// init database
		Database db = new Database(this, this);
		Database.createTableIfNotExist();

		final TabHost tabHost = getTabHost();
		Resources res = getResources();
		TabHost.TabSpec spec;

		Intent intent;

		// Create an Intent to launch an Activity for "Featured" tab (to be
		// reused)
		intent = new Intent().setClass(this, FeaturedActivity.class);

		// Initialize a TabSpec for "Featured" tab and add it to the TabHost
		spec = tabHost.newTabSpec("Featured")
				.setIndicator("", res.getDrawable(R.drawable.tab_featured))
				.setContent(intent);
		tabHost.addTab(spec);

		// Create an Intent to launch an Activity for "Ticket" tab
		// *akan dihapus dan diganti dengan tab kategorisasi
		intent = new Intent().setClass(this, CategoryActivity.class);

		// Initialize a TabSpec for "Categorized" tab and add it to the TabHost
		spec = tabHost.newTabSpec("Categorized")
				.setIndicator("", res.getDrawable(R.drawable.tab_category))
				.setContent(intent);
		tabHost.addTab(spec);

		// Create an Intent to launch an Activity for "MyTicket" tab
		intent = new Intent().setClass(this, SavedEventActivity.class);

		// Initialize a TabSpec for "MyTicket" tab and add it to the TabHost
		spec = tabHost.newTabSpec("MyTicket")
				.setIndicator("", res.getDrawable(R.drawable.tab_myticket))
				.setContent(intent);
		tabHost.addTab(spec);

		// Create an Intent to launch an Activity for "Twitter" tab
		intent = new Intent().setClass(this, TwitterActivity.class);

		// Initialize a TabSpec for "Twitter" tab and add it to the TabHost
		spec = tabHost.newTabSpec("Twitter")
				.setIndicator("", res.getDrawable(R.drawable.tab_twitter))
				.setContent(intent);
		tabHost.addTab(spec);
		
		// set default selected tab to "Featured Tab"
		tabHost.setCurrentTab(0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.search_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.search_event_menu:
			onSearchRequested();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}