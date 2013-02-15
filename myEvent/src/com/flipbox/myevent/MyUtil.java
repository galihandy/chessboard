/**
 * 
 */
package com.flipbox.myevent;

import java.util.StringTokenizer;

/**
 * @author Galih
 * 
 */
public class MyUtil {
	
	public static final int A_DAY = 86400000;
	public static final int AN_HOUR = 3600000;
	public static final int A_MINUTE = 60000;
	public static final int ALARM_REQUEST_CODE = 0;
	
	public static final String NO_CONN = "No connection";
	public static final String NO_EVT_AVLBL = "No event";
	
	public static final String[] KEYS = { Database.EVENT_AUTHOR,
			Database.EVENT_CAT, Database.EVENT_CONTACT,
			Database.EVENT_DATE_CREATED, Database.EVENT_DATE_PUBLISHED,
			Database.EVENT_DESC, Database.EVENT_END_DATE, Database.EVENT_ID,
			Database.EVENT_IMG_URL, Database.EVENT_LATD, Database.EVENT_LOC,
			Database.EVENT_LONGTD, Database.EVENT_NAME,
			Database.EVENT_START_DATE, Database.EVENT_TICKET_PRC };

	public static String dateFormatter(String rawDate) {
		final String[] monthValue = { "Jan", "Feb", "Mar", "Apr", "Mei", "Jun",
				"Jul", "Agu", "Sep", "Okt", "Nov", "Des" };

		String formattedDate = "";
		StringTokenizer tokenizer = new StringTokenizer(rawDate, " ");
		String date = tokenizer.nextToken();
		String time = tokenizer.nextToken();

		tokenizer = new StringTokenizer(date, "-");
		String year = tokenizer.nextToken();
		int monthIndex = Integer.parseInt(tokenizer.nextToken()) - 1;
		String month = monthValue[monthIndex];
		String day = tokenizer.nextToken();

		formattedDate = day + " " + month + " " + year + ", " + time;

		return formattedDate;
	}
}
