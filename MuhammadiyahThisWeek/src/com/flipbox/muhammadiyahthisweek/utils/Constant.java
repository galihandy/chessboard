package com.flipbox.muhammadiyahthisweek.utils;

import com.flipbox.muhammadiyahthisweek.database.Database;

public class Constant {

	public static final int A_DAY = 86400000;
	public static final int AN_HOUR = 3600000;
	public static final int A_MINUTE = 60000;
	public static final int ALARM_REQUEST_CODE = 0;

	public static final int HOME = 0;
	public static final int BY_LOCATION = 1;
	public static final int BY_CATEGORIES = 2;
	public static final int MY_EVENTS = 3;
	public static final int ARCHIVE = 4;
	public static final int TWITTER = 5;

	public static final int MAIN_ACTIVITY = 0;
	public static final int CATEGORY_EVENT_ACTIVITY = 1;
	public static final int DETAIL_EVENT_ACTIVITY = 2;

	public static final long A_WEEK = 7 * A_DAY;

	public static final String NO_CONN = "No connection";
	public static final String NO_EVT_AVLBL = "No event";

	public static final String EVENT_BUNDLE = "event bundle";
	public static final String CATEGORY_BUNDLE = "category bundle";
	public static final String LOCATION_BUNDLE = "location bundle";
	public static final String SEARCH_BUNDLE = "search bundle";
	
	public static final String STARTING_ACTIVITY = "starting activity";
	public static final String CAT_EVENT_LIST_ACT_CONTEXT = "cat event list act context";
	
	public static final String SEARCH_QUERY = "search query";

	public static final String[] KEYS = { Database.EVENT_AUTHOR,
			Database.EVENT_CAT, Database.EVENT_CONTACT,
			Database.EVENT_DATE_CREATED, Database.EVENT_DATE_PUBLISHED,
			Database.EVENT_DESC, Database.EVENT_END_DATE, Database.EVENT_ID,
			Database.EVENT_IMG_URL, Database.EVENT_LATD, Database.EVENT_LOC,
			Database.EVENT_LONGTD, Database.EVENT_NAME,
			Database.EVENT_START_DATE, Database.EVENT_TICKET_PRC };

}
