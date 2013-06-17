/**
 * 
 */
package com.flipbox.muhammadiyahthisweek.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import com.flipbox.muhammadiyahthisweek.database.Database;
import com.flipbox.muhammadiyahthisweek.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * @author Galih
 * 
 */
public class MyUtil {

	private static Database db;

	public static void createDatabaseTable(Context context) {
		db = new Database(context);
	}

	public static void initLazyImageLoader(Context context) {

		// build image display option
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.not_loaded_image)
				.showImageOnFail(R.drawable.not_loaded_image)
				.showImageForEmptyUri(R.drawable.not_loaded_image)
				.cacheOnDisc().cacheInMemory().build();

		// configure image loader
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).defaultDisplayImageOptions(defaultOptions).build();

		// init ImageLoader
		if (!ImageLoader.getInstance().isInited()) {
			ImageLoader.getInstance().init(config);
			System.out.println("UIL inited");
		}
	}

	public static String dateFormatter(String rawDate) {
		final String[] monthValue = { "01", "02", "03", "04", "05", "06", "07",
				"08", "09", "10", "11", "12" };
		final String[] dayValue = { "Senin", "Selasa", "Rabu", "Kamis",
				"Jum'at", "Sabtu", "Minggu" };

		System.out.println("FORMAT DATE " + rawDate);

		Date date = stringToDate(rawDate);
		String formattedDate = "";
		String sDate = date.getDate() + "";

		String sYear = (date.getYear() + 1900) + "";
		String sMonth = monthValue[date.getMonth()];
		String sDay = dayValue[((date.getDay() - 1 < 0) ? 0 : date.getDay() - 1)];

		String sTime = " Jam "
				+ timeFormatter(date.getHours(), date.getMinutes()) + " WIB";

		formattedDate = " " + sDay + ", " + sDate + "/" + sMonth + "/" + sYear
				+ "\n" + sTime;

		return formattedDate;
	}

	public static String timeFormatter(int hour, int minute) {
		String time = "";
		String sHour;
		String sMin;

		// format hour
		if (hour < 10)
			sHour = "0" + hour;
		else
			sHour = hour + "";

		// format minute
		if (minute < 10)
			sMin = "0" + minute;
		else
			sMin = minute + "";

		time = sHour + ":" + sMin;
		return time;
	}

	public static Bitmap loadBitmap(String url) {
		Bitmap bm = null;
		InputStream is = null;
		BufferedInputStream bis = null;
		try {
			URLConnection conn = new URL(url).openConnection();
			conn.connect();
			is = conn.getInputStream();
			bis = new BufferedInputStream(is, 8192);
			bm = BitmapFactory.decodeStream(bis);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bm;
	}

	public static Date stringToDate(String date_string) {
		if (date_string == null) {
			return null;
		} else if (date_string.equals("")) {
			return null;
		}
		StringTokenizer tokenizer = new StringTokenizer(date_string, " ");
		String date = tokenizer.nextToken();
		String time = tokenizer.nextToken();

		tokenizer = new StringTokenizer(date, "-");
		int year = Integer.parseInt(tokenizer.nextToken()) - 1900;
		int month = Integer.parseInt(tokenizer.nextToken()) - 1;
		int day = Integer.parseInt(tokenizer.nextToken());

		tokenizer = new StringTokenizer(time, ":");
		int hour = Integer.parseInt(tokenizer.nextToken());
		int minute = Integer.parseInt(tokenizer.nextToken());
		int second = Integer.parseInt(tokenizer.nextToken());

		return new Date(year, month, day, hour, minute, second);
	}

	public static Database getDatabase() {
		return db;
	}

	public static String getTodayCurrentTime() {
		String todayCurrentTime = getTodayDate() + " " + getCurrentTime();
		return todayCurrentTime;
	}

	public static String getTodayDate() {
		String todayDate = "";
		String date = "";
		String month = "";

		Calendar c = Calendar.getInstance();
		int dateValue = c.get(Calendar.DATE);
		int monthValue = c.get(Calendar.MONTH) + 1;
		int year = c.get(Calendar.YEAR);

		if (dateValue < 10) {
			date = "0" + dateValue;
		} else {
			date = dateValue + "";
		}

		if (monthValue < 10) {
			month = "0" + monthValue;
		} else {
			month = monthValue + "";
		}

		todayDate = year + "-" + month + "-" + date;

		System.out.println(todayDate);
		return todayDate;
	}

	public static String getCurrentTime() {
		String time = "";
		String hour = "";
		String min = "";
		String sec = "";

		Calendar c = Calendar.getInstance();
		int hourValue = c.get(Calendar.HOUR_OF_DAY);
		int minValue = c.get(Calendar.MINUTE);
		int secValue = c.get(Calendar.SECOND);

		// format hour
		if (hourValue < 10)
			hour = "0" + hourValue;
		else
			hour = hourValue + "";

		// format minute
		if (minValue < 10)
			min = "0" + minValue;
		else
			min = minValue + "";

		// format second
		if (secValue < 10)
			sec = "0" + secValue;
		else
			sec = secValue + "";

		time = hour + ":" + min + ":" + sec;

		System.out.println(time);
		return time;
	}

	public static boolean isDatePassed(String date) {
		boolean passed = false;

		Date currentDate = new Date();
		Date dateToCheck = stringToDate(date);

		if (dateToCheck.getTime() - currentDate.getTime() < 0) {
			passed = true;
		}

		return passed;
	}

	public static boolean isNoMoreThanTwoWeekPassed(String date) {
		boolean passed = false;

		Date currentDate = new Date();
		Date dateToCheck = stringToDate(date);

		if (currentDate.getTime() - dateToCheck.getTime() < (Constant.A_DAY * 14)) {
			passed = true;
		}

		return passed;
	}
}
