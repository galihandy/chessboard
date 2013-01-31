package com.flipbox.myevent;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;

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

	private EditText searchEt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// init database
		Database db = new Database(this, this);
		Database.createTableIfNotExist();

		searchEt = (EditText) findViewById(R.id.search_edittext);

		searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {

			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// TODO Auto-generated method stub
				String keyWord = searchEt.getEditableText().toString();
				if (keyWord.trim().length() != 0) {
					Intent in = new Intent().setClass(MainActivity.this,
							SearchActivity.class);
					in.putExtra("keyword", keyWord);
					startActivity(in);
				}

				return true;
			}
		});

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
		intent = new Intent().setClass(this, CategorizedActivity.class);

		// Initialize a TabSpec for "Categorized" tab and add it to the TabHost
		spec = tabHost.newTabSpec("Categorized")
				.setIndicator("", res.getDrawable(R.drawable.tab_ticket))
				.setContent(intent);
		tabHost.addTab(spec);

		// Create an Intent to launch an Activity for "MyTicket" tab
		intent = new Intent().setClass(this, SavedEventActivity.class);

		// Initialize a TabSpec for "MyTicket" tab and add it to the TabHost
		spec = tabHost.newTabSpec("MyTicket")
				.setIndicator("", res.getDrawable(R.drawable.tab_myticket))
				.setContent(intent);
		tabHost.addTab(spec);

		// set default selected tab to "Featured Tab"
		tabHost.setCurrentTab(0);
	}
}
