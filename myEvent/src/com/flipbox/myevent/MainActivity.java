package com.flipbox.myevent;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class MainActivity extends TabActivity {
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
		
		// Create an Intent to launch an Activity for "Featured" tab (to be reused)
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
