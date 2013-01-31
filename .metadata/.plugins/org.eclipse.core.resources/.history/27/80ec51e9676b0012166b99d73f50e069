/**
 * 
 */
package com.flipbox.myevent;

import java.util.StringTokenizer;

/**
 * @author Galih
 * 
 */
public class GeneralUtilities {

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
