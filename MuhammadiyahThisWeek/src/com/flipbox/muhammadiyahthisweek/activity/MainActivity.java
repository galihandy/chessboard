package com.flipbox.muhammadiyahthisweek.activity;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.flipbox.muhammadiyahthisweek.database.Database;
import com.flipbox.muhammadiyahthisweek.fragment.ArchiveFragment;
import com.flipbox.muhammadiyahthisweek.fragment.FeaturedFragment;
import com.flipbox.muhammadiyahthisweek.fragment.MainMenuFragment;
import com.flipbox.muhammadiyahthisweek.model.Event;
import com.flipbox.muhammadiyahthisweek.utils.CommonVariables;
import com.flipbox.muhammadiyahthisweek.utils.JSONfunctions;
import com.flipbox.muhammadiyahthisweek.utils.MyUtil;
import com.flipbox.muhammadiyahthisweek.R;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends SlidingFragmentActivity {

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
	public static final String GET_ALL_LOCATION = BASE_URL
			+ "/service/allLocation";

	private TextView headerSecondTv;
	private Fragment contentFragment;
	private MainMenuFragment mainMenuFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// init database
		MyUtil.createDatabaseTable(this);

		// init lazy image loader
		MyUtil.initLazyImageLoader(this);

		// init event archive database
		new JSONArrayLoaderTask().execute(MainActivity.GENERIC_QUERY
				+ "sort_by=" + Database.EVENT_START_DATE);

		// set the Above View
		if (savedInstanceState != null)
			contentFragment = getSupportFragmentManager().getFragment(
					savedInstanceState, "contentFragment");
		if (contentFragment == null) {
			contentFragment = new FeaturedFragment();

		}

		// set content frame
		setContentView(R.layout.content_frame);
		headerSecondTv = (TextView) findViewById(R.id.thisweek_tv);

		ImageView headerLogo = (ImageView) findViewById(R.id.menu_icon);
		headerLogo.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				getSlidingMenu().showMenu();
			}
		});

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, contentFragment).commit();

		// set menu frame
		setBehindContentView(R.layout.menu_frame);

		if (mainMenuFragment == null) {
			mainMenuFragment = new MainMenuFragment();
			mainMenuFragment
					.setFeaturedFragment((FeaturedFragment) contentFragment);
		}
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, mainMenuFragment).commit();

		// decor slidding menu
		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT);
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sm.setFadeDegree(0.35f);
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (CommonVariables.IS_GO_TO_MAIN) {
			CommonVariables.IS_GO_TO_MAIN = false;

			mainMenuFragment
					.updateCurrentFragment(CommonVariables.DEST_FRAGMENT);
		}
	}

	public Fragment getContentFragment() {
		return contentFragment;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		/*
		 * getSupportFragmentManager().putFragment(outState, "contentFrame",
		 * contentFragment);
		 */
	}

	public void switchContent(Fragment f) {

		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();

		ft.replace(R.id.content_frame, f);
		ft.commit();

		getSlidingMenu().showContent();
	}

	public TextView getHeaderSecondTv() {
		return headerSecondTv;
	}

	private class JSONArrayLoaderTask extends
			AsyncTask<String, Void, JSONArray> {

		// Before running code in the separate thread
		@Override
		protected void onPreExecute() {

		}

		/** Doing the parsing of xml data in a non-ui thread */
		@Override
		protected JSONArray doInBackground(String... url) {
			return JSONfunctions.getJSONArrayfromURL(url[0]);
		}

		/**
		 * Invoked by the Android system on "doInBackground" is executed
		 * completely
		 */
		/** This will be executed in ui thread */
		@Override
		protected void onPostExecute(JSONArray result) {
			if (result == null) {
				return;
			} else {
				MyUtil.getDatabase().clearEventArchive();
			}

			Database db = MyUtil.getDatabase();
			ArrayList<Event> listOfEvent = getListOfEvent(result);

			int archiveLength = (listOfEvent.size() < ArchiveFragment.MAX_ARCHIVED_EVENT) ? listOfEvent
					.size() : ArchiveFragment.MAX_ARCHIVED_EVENT;
			System.out.println("Archive Result length " + listOfEvent.size());

			for (int i = 0; i < archiveLength; i++) {
				Event e = listOfEvent.get(i);

				db.insertEventToArchive(e);
			}
		}

		private boolean isValid(JSONObject e) throws JSONException {
			if (e.getString(Database.EVENT_NAME) == null
					|| e.getString(Database.EVENT_NAME) == ""
					|| e.getString(Database.EVENT_NAME).equals("null")
					|| e.getString(Database.EVENT_NAME).startsWith("null")) {
				return false;
			} else {
				return true;
			}
		}

		private ArrayList<Event> getListOfEvent(JSONArray jsonArray) {
			ArrayList<Event> mylist = new ArrayList<Event>();
			try {
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject o = jsonArray.getJSONObject(i);

					System.out.println("Event Name " + o.getString(Database.EVENT_NAME));
					if (isValid(o)
							&& !MyUtil.isNoMoreThanTwoWeekPassed(o
									.getString(Database.EVENT_START_DATE))) {

						Event e = new Event();
						e.setAuthor(o.getString(Database.EVENT_AUTHOR));
						e.setCategory(o.getString(Database.EVENT_CAT));
						e.setContact(o.getString(Database.EVENT_CONTACT));
						e.setDateCreated(o
								.getString(Database.EVENT_DATE_CREATED));
						e.setDatePublished(o
								.getString(Database.EVENT_DATE_PUBLISHED));
						e.setDesc(o.getString(Database.EVENT_DESC));
						e.setEndDate(o.getString(Database.EVENT_END_DATE));
						e.setId(o.getString(Database.EVENT_ID));
						e.setImageUrl(o.getString(Database.EVENT_IMG_URL));
						e.setLatitude(o.getString(Database.EVENT_LATD));
						e.setLongitude(o.getString(Database.EVENT_LONGTD));
						e.setName(o.getString(Database.EVENT_NAME));
						e.setTicketPrice(o.getString(Database.EVENT_TICKET_PRC));
						e.setStartDate(o.getString(Database.EVENT_START_DATE));
						e.setLocation(o.getString(Database.EVENT_LOC));
						System.out.println("ARCHIVE event " + e.getName()
								+ " date " + e.getStartDate());
						mylist.add(e);
					}
				}
			} catch (JSONException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}

			return mylist;
		}
	}
}
