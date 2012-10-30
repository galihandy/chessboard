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
		
		//init database
		Database db = new Database(this, this);
		Database.createTableIfNotExist();

		final TabHost tabHost = getTabHost();
		 Resources res = getResources(); 
		TabHost.TabSpec spec;

		// Create an Intent to launch an Activity for the tab (to be reused)
		Intent intent;
		intent = new Intent().setClass(this, FeaturedActivity.class);

		// Initialize a TabSpec for each tab and add it to the TabHost
		spec = tabHost
				.newTabSpec("Featured")
				.setIndicator("",
                        res.getDrawable(R.drawable.tab_featured))
				.setContent(intent);

		tabHost.addTab(spec);

		
		intent = new Intent().setClass(this, TicketActivity.class);

		// Initialize a TabSpec for each tab and add it to the TabHost
		spec = tabHost
				.newTabSpec("Ticket")
				.setIndicator("",
                        res.getDrawable(R.drawable.tab_ticket))
				.setContent(intent);
		tabHost.addTab(spec);
		
		intent = new Intent().setClass(this, SavedEventActivity.class);

		// Initialize a TabSpec for each tab and add it to the TabHost
		spec = tabHost
				.newTabSpec("MyTicket")
				.setIndicator("",
                        res.getDrawable(R.drawable.tab_myticket))
				.setContent(intent);
		tabHost.addTab(spec);
		
		tabHost.setCurrentTab(0);
	}
}
