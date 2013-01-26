package com.flipbox.myevent;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Database extends SQLiteOpenHelper {

	private static SQLiteDatabase db;
	private static MainActivity main;

	private static final String DATABASE_NAME = "MyEvent";

	private static final String TABLE_CACHE = "cache";
	private static final String TABLE_EVENT = "event";
	private static final String EVENT_ID = "id";
	private static final String EVENT_NAME = "name";
	private static final String EVENT_DESC = "description";
	private static final String EVENT_START_DATE = "start_date";
	private static final String EVENT_END_DATE = "end_date";
	private static final String EVENT_DATE_PUBLISHED = "date_published";
	private static final String EVENT_DATE_CREATED = "date_created";
	private static final String EVENT_LOC = "location";
	private static final String EVENT_LATD = "latitude";
	private static final String EVENT_LONGTD = "longitude";
	private static final String EVENT_CAT = "category";
	private static final String EVENT_IMG_URL = "img_url";
	private static final String EVENT_CONTACT = "contact";
	private static final String EVENT_AUTHOR = "author";
	private static final String EVENT_TICKET_PRC = "ticket_price";
	private static final String CREATE_EVENT_TABLE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_EVENT
			+ " ("
			+ EVENT_ID
			+ " INTEGER, "
			+ EVENT_NAME
			+ " VARCHAR PRIMARY KEY, "
			+ EVENT_DESC
			+ " VARCHAR, "
			+ EVENT_START_DATE
			+ " DATETIME, "
			+ EVENT_END_DATE
			+ " DATETIME, "
			+ EVENT_LOC
			+ " VARCHAR, "
			+ EVENT_CAT
			+ " VARCHAR, "
			+ EVENT_IMG_URL
			+ " URI, "
			+ EVENT_CONTACT
			+ " VARCHAR, "
			+ EVENT_DATE_CREATED
			+ " DATETIME, "
			+ EVENT_DATE_PUBLISHED
			+ " DATETIME, "
			+ EVENT_AUTHOR
			+ " VARCHAR, "
			+ EVENT_TICKET_PRC
			+ " VARCHAR, "
			+ EVENT_LATD + " INTEGER, " + EVENT_LONGTD + " INTEGER," + ");";

	public Database(Context context) {
		super(context, DATABASE_NAME, null, 1);
		db = this.getWritableDatabase();
		createTableIfNotExist();
	}

	public Database(Context context, MainActivity main) {
		super(context, DATABASE_NAME, null, 1);
		this.main = main;
		db = this.getWritableDatabase();
		createTableIfNotExist();
	}

	public Database(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		db = this.getWritableDatabase();
		createTableIfNotExist();
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		db = arg0;
		createTableIfNotExist();
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		Log.i("db", "Upgrading Table from " + arg1 + " to " + arg2
				+ ". This will erase all datas.");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT);
		// db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOKEN);
		onCreate(db);
	}

	public static void createTableIfNotExist() {
		Log.i("db", "create table");
		// tabel untuk simpan event
		db.execSQL(CREATE_EVENT_TABLE);

		// tabel token
		// db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_TOKEN
		// + " (token VARCHAR PRIMARY KEY, date DATETIME);");
		Log.i("db", "table created (TABLE_EVENT and TABLE_CACHE)");
	}

	// simpan event ke database
	/* butuh diperbaiki */
	public static Event insertEvent(Event e) {

		Log.i("db", "insert event");
		if (isEventExist(e.getId())) {
			// do nothing

			Log.i("db", "event is exist");
			return null;
		} else {
			// insert data to a table
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String start_datetime = "";
			if (start_date != null)
				start_datetime = formatter.format(start_date);
			String end_datetime = "";
			if (end_date != null)
				end_datetime = formatter.format(end_date);
			db.execSQL("INSERT INTO " + TABLE_EVENT + " VALUES ('" + id + "', "
					+ "'" + title + "', '" + desc_uri + "', " + "'"
					+ start_datetime + "', '" + end_datetime + "', '" + image
					+ "', '" + token + "');");
			Bitmap image_bitmap = null;
			if (image != null)
				image_bitmap = retrieveImageFromInternalStorage(image);
			return new Event(id, title, start_date, end_date, desc_uri,
					image_bitmap, token);
		}
	}

	public static void insertToken(String token) {

		Log.i("db", "insert token");
		removeToken();
		db.execSQL("INSERT INTO " + TABLE_CACHE + "(cache)" + " VALUE ('"
				+ token + "');");
	}

	public static void updateToken(String token, Date date) {

		Log.i("db", "update token");

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String datetime = formatter.format(date);
		db.execSQL("UPDATE " + TABLE_CACHE + " SET date=" + datetime
				+ "WHERE token=" + token + ";");
	}

	// hapus event berdasarkan id
	public static void removeEvent(int id) {

		Log.i("removeEvent", "remove event");
		db.execSQL("DELETE FROM " + TABLE_EVENT + " where id = '" + id + "'");
	}

	// hapus token
	public static void removeToken() {

		Log.i("removeToken", "remove token");
		db.execSQL("DELETE FROM " + TABLE_CACHE);
	}

	// hapus database
	public static void dropTable(String tableName) {
		db.execSQL("DROP TABLE IF EXISTS " + tableName);

	}

	private static boolean isEventExist(int id) {
		boolean isExist = false;

		Log.i("isEventExist() - Database", "check existing...");
		// ambil data dari database
		Cursor c = db.rawQuery("SELECT * FROM " + TABLE_EVENT + " WHERE id='"
				+ id + "'", null);

		if (c != null && c.moveToFirst()) {
			isExist = true;
		}

		if (c != null) {
			c.close();
		}

		return isExist;
	}

	public void openDB() {
		try {
			db = this.getWritableDatabase();
		} catch (Exception e) {

		}
	}

	public static void closeDB() {
		// close database
		if (db != null) {
			db.close();
		}
	}

	public static ArrayList<Event> getMyEventList() {

		Log.i("getMyEventList() - Database", "get list article...");
		// ambil data dari database
		Cursor c = db.rawQuery("SELECT * FROM " + TABLE_EVENT, null);

		ArrayList<Event> list = new ArrayList<Event>();
		int iid = c.getColumnIndex("id");
		int ititle = c.getColumnIndex("title");
		int istart_date = c.getColumnIndex("start_date");
		int iend_date = c.getColumnIndex("end_date");
		int idesc_uri = c.getColumnIndex("desc_uri");
		int iimage = c.getColumnIndex("image");
		int itoken = c.getColumnIndex("token");

		if (c != null && c.moveToFirst()) {
			// loop
			do {
				Event my_event = new Event();
				// set nilai
				my_event.setId(c.getInt(iid));
				my_event.setTitle(c.getString(ititle));
				my_event.setDesc_uri(c.getString(idesc_uri));
				my_event.setToken(c.getString(itoken));
				String start_d = c.getString(istart_date);
				String end_d = c.getString(iend_date);
				my_event.setStart_date(SQLDateToJavaDate(start_d));
				my_event.setEnd_date(SQLDateToJavaDate(end_d));
				// my_event.setImage(retrieveImageFromInternalStorage((c.getString(iimage))));

				// tambahkan ke list
				list.add(my_event);

			} while (c.moveToNext());
		}

		// menutup cursor
		if (c != null) {
			c.close();
		}

		return list;
	}

	public static String getToken() {

		Log.i("getToken()", "get token...");
		// ambil data dari database
		Cursor c = db.rawQuery("SELECT * FROM " + TABLE_CACHE, null);

		int itoken = c.getColumnIndex("token");
		String token = "";
		if (c != null && c.moveToFirst()) {
			token = c.getString(itoken);
		}

		return token;
	}

	// retrieve image from internal storage by image's name parameter
	public static Bitmap retrieveImageFromInternalStorage(String name) {
		Bitmap image = null;
		FileInputStream fis;
		try {
			fis = new FileInputStream(name);
			ByteArrayOutputStream bufStream = new ByteArrayOutputStream();
			DataOutputStream outWriter = new DataOutputStream(bufStream);
			int ch;
			while ((ch = fis.read()) != -1)
				outWriter.write(ch);

			outWriter.close();
			byte[] data = bufStream.toByteArray();
			bufStream.close();
			fis.close();

			image = BitmapFactory.decodeByteArray(data, 0, data.length);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return image;
	}

	public static Date SQLDateToJavaDate(String date_string) {
		if (date_string.equals("") || date_string == null) {
			return null;
		}
		StringTokenizer tokenizer = new StringTokenizer(date_string, " ");
		String date = tokenizer.nextToken();
		String time = tokenizer.nextToken();

		tokenizer = new StringTokenizer(date, "-");
		int year = Integer.parseInt(tokenizer.nextToken());
		int month = Integer.parseInt(tokenizer.nextToken()) - 1;
		int day = Integer.parseInt(tokenizer.nextToken());

		tokenizer = new StringTokenizer(time, ":");
		int hour = Integer.parseInt(tokenizer.nextToken());
		int minute = Integer.parseInt(tokenizer.nextToken());
		int second = Integer.parseInt(tokenizer.nextToken());

		return new Date(year, month, day, hour, minute, second);
	}
}
