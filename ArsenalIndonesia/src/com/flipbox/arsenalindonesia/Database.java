package com.flipbox.arsenalindonesia;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

import android.content.ContentValues;
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

	public static final String EVENT_ID = "id";
	public static final String EVENT_NAME = "name";
	public static final String EVENT_DESC = "description";
	public static final String EVENT_START_DATE = "start_date";
	public static final String EVENT_END_DATE = "end_date";
	public static final String EVENT_DATE_PUBLISHED = "date_published";
	public static final String EVENT_DATE_CREATED = "date_created";
	public static final String EVENT_LOC = "location";
	public static final String EVENT_LATD = "latitude";
	public static final String EVENT_LONGTD = "longitude";
	public static final String EVENT_CAT = "category";
	public static final String EVENT_IMG_URL = "img_url";
	public static final String EVENT_CONTACT = "contact";
	public static final String EVENT_AUTHOR = "author";
	public static final String EVENT_TICKET_PRC = "ticket_price";
	private static final String TABLE_CACHE = "cache";
	private static final String TABLE_EVENT = "event";
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
			+ EVENT_LATD
			+ " INTEGER, "
			+ EVENT_LONGTD
			+ " INTEGER" + ");";

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
	public static boolean insertEvent(Event e) {
		long result = -1;
		
		if (!isEventExist(e.getId())) {
			Log.i("db", "insert event");
			ContentValues values = new ContentValues();
			values.put(EVENT_ID, e.getId());
			values.put(EVENT_NAME, e.getName());
			values.put(EVENT_DESC, e.getDesc());
			values.put(EVENT_START_DATE, e.getStartDate());
			values.put(EVENT_END_DATE, e.getEndDate());
			values.put(EVENT_LOC, e.getLocation());
			values.put(EVENT_CAT, e.getCategory());
			values.put(EVENT_IMG_URL, e.getImageUrl());
			values.put(EVENT_CONTACT, e.getContact());
			values.put(EVENT_DATE_PUBLISHED, e.getDatePublished());
			values.put(EVENT_DATE_CREATED, e.getDateCreated());
			values.put(EVENT_AUTHOR, e.getAuthor());
			values.put(EVENT_TICKET_PRC, e.getTicketPrice());
			values.put(EVENT_LATD, e.getLatitude());
			values.put(EVENT_LONGTD, e.getLongitude());

			result = db.insert(TABLE_EVENT, null, values);
		}

		return (result == -1) ? false : true;
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
	public static void removeEvent(String id) {
		
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

	private static boolean isEventExist(String id) {
		boolean isExist = false;
		final String query = "SELECT * FROM " + TABLE_EVENT + " WHERE id='"
				+ id + "'";

		Log.i("isEventExist() - Database", "check existing...");

		// ambil data dari database
		Cursor c = db.rawQuery(query, null);

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
		final String query = "SELECT * FROM " + TABLE_EVENT;
		ArrayList<Event> list = new ArrayList<Event>();

		// ambil data dari database
		Cursor c = db.rawQuery(query, null);
		int iId = c.getColumnIndex(EVENT_ID);
		int iName = c.getColumnIndex(EVENT_NAME);
		int iDesc = c.getColumnIndex(EVENT_DESC);
		int iStartDate = c.getColumnIndex(EVENT_START_DATE);
		int iEndDate = c.getColumnIndex(EVENT_END_DATE);
		int iDatePublished = c.getColumnIndex(EVENT_DATE_PUBLISHED);
		int iDateCreated = c.getColumnIndex(EVENT_DATE_CREATED);
		int iLoc = c.getColumnIndex(EVENT_LOC);
		int iLat = c.getColumnIndex(EVENT_LATD);
		int iLongtd = c.getColumnIndex(EVENT_LONGTD);
		int iCat = c.getColumnIndex(EVENT_CAT);
		int iImgUrl = c.getColumnIndex(EVENT_IMG_URL);
		int iContact = c.getColumnIndex(EVENT_CONTACT);
		int iAuthor = c.getColumnIndex(EVENT_AUTHOR);
		int iTicketPrc = c.getColumnIndex(EVENT_TICKET_PRC);

		if (c != null && c.moveToFirst()) {
			do {
				Event myEvent = new Event();

				// set event value
				myEvent.setId(c.getString(iId));
				myEvent.setName(c.getString(iName));
				myEvent.setDesc(c.getString(iDesc));
				myEvent.setStartDate(c.getString(iStartDate));
				myEvent.setEndDate(c.getString(iEndDate));
				myEvent.setDatePublished(c.getString(iDatePublished));
				myEvent.setDateCreated(c.getString(iDateCreated));
				myEvent.setLocation(c.getString(iLoc));
				myEvent.setLatitude(c.getString(iLat));
				myEvent.setLongitude(c.getString(iLongtd));
				myEvent.setCategory(c.getString(iCat));
				myEvent.setImageUrl(c.getString(iImgUrl));
				myEvent.setContact(c.getString(iContact));
				myEvent.setAuthor(c.getString(iAuthor));
				myEvent.setTicketPrice(c.getString(iTicketPrc));
				
				list.add(myEvent);

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
