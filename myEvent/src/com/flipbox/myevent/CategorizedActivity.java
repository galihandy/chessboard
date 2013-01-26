package com.flipbox.myevent;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CategorizedActivity extends Activity {

	public static final String TAG_TOKEN = "token";
	public static final String TAG_ALL_EVENT = "all_event";
	public static final String TAG_EVENTS = "events";
	public static final String TAG_BUSINESS_NAME = "business_name";
	public static final String TAG_TIKET_EVENT_START = "tiket_event_start";
	public static final String TAG_TIKET_EVENT_END = "tiket_event_end";
	public static final String TAG_EVENT_URI = "event_uri";
	public static final String TAG_EVENT_IMAGE = "event_img";

	String token = null;
	String uri = null;

	JSONObject e;
	JSONArray e_content;
	ListView lv;
	Bitmap bmp = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multiview);
		SwitchLayout1();
		new JSONObjectLoaderTask().execute();
	}

	class JSONObjectLoaderTask extends AsyncTask<Void, Void, JSONObject> {

		// Before running code in the separate thread
		@Override
		protected void onPreExecute() {

		}

		/** Doing the parsing of json data in a non-ui thread */
		@Override
		protected JSONObject doInBackground(Void... url) {
			JSONObject getTokenJson, searchEventJson = null;
			try {
				getTokenJson = JSONfunctions
						.getJSONObjectfromURL("http://api.master18.tiket.com/apiv1/payexpress?method=getToken&secretkey=1ae330652bf4792a7e6654ab801693d2&output=json");
				System.out.println("GetTokenJSON: " + getTokenJson.names());

				token = getTokenJson.getString("token");
				System.out.println("Token: " + token);

				searchEventJson = JSONfunctions
						.getJSONObjectfromURL("http://api.master18.tiket.com/search/event?q=&token="
								+ token + "&output=json");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			return searchEventJson;
		}

		/**
		 * Invoked by the Android system on "doInBackground" is executed
		 * completely
		 */
		/** This will be executed in ui thread */
		@Override
		protected void onPostExecute(JSONObject result) {
			System.out.println("jObject: " + result);
			if (result == null) {

			} else {
				List<HashMap<String, Object>> mylist = new ArrayList<HashMap<String, Object>>();

				JSONObject searchEventJson = result;

				System.out.println("eventJson: " + searchEventJson.names());
				JSONObject allEvent = null;
				JSONArray events = null;
				try {
					allEvent = searchEventJson.getJSONObject(TAG_ALL_EVENT);
					System.out.println("all event: "
							+ allEvent.names().toString());
					events = allEvent.getJSONArray(TAG_EVENTS);
					System.out.println("events: " + events);
					System.out.println("events(0): " + events.get(0));

					for (int i = 0; i < events.length(); i++) {
						HashMap<String, Object> map = new HashMap<String, Object>();
						JSONObject e = events.getJSONObject(i);

						map.put("id", String.valueOf(i));
						map.put("name", e.getString(TAG_BUSINESS_NAME));
						map.put("startTime",
								"Start Time: "
										+ e.getString(TAG_TIKET_EVENT_START));
						map.put("uri", e.getString(TAG_EVENT_URI));

						bmp = loadBitmap(e.getString(TAG_EVENT_IMAGE));
						map.put("image", bmp);

//						new FetchImageTask() {
//							protected void onPostExecute(Bitmap result) {
//								if (result != null) {
//									bmp = result;;
//								}
//							}
//						}.execute(e.getString(TAG_EVENT_IMAGE));
						
						
						mylist.add(map);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				ListAdapter adapter = new CustomAdapter(getBaseContext(),
						mylist, R.layout.list_item, new String[] { "name",
								"startTime", "image" }, new int[] {
								R.id.item_title, R.id.item_subtitle,
								R.id.item_image });

				lv = (ListView) findViewById(R.id.event_listview);
				lv.setAdapter(adapter);
				TextView tx = (TextView) findViewById(R.id.no_data_textview);

				if (adapter.isEmpty()) {
					lv.setVisibility(View.GONE);
				} else {
					tx.setVisibility(View.GONE);
				}

				lv.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						@SuppressWarnings("unchecked")
						HashMap<String, String> o = (HashMap<String, String>) lv
								.getItemAtPosition(position);

						// Starting new intent
						Intent in = new Intent(getApplicationContext(),
								DetailEventActivity.class);
						in.putExtra("token", token);
						in.putExtra("uri", o.get("uri"));
						startActivity(in);
					}
				});
			}

			// switch view
			SwitchLayout2();
		}
	}

	private void SwitchLayout2() {
		RelativeLayout loadingLayout = (RelativeLayout) findViewById(R.id.loadingRelLayout);
		RelativeLayout eventListLayout = (RelativeLayout) findViewById(R.id.listRelLayout);

		// Enable Layout 2 and Disable Layout 1
		loadingLayout.setVisibility(View.GONE);
		eventListLayout.setVisibility(View.VISIBLE);
	}

	private void SwitchLayout1() {
		RelativeLayout loadingLayout = (RelativeLayout) findViewById(R.id.loadingRelLayout);
		RelativeLayout eventListLayout = (RelativeLayout) findViewById(R.id.listRelLayout);

		// Enable Layout 1 & Disable Layout2
		loadingLayout.setVisibility(View.VISIBLE);
		eventListLayout.setVisibility(View.GONE);
	}

	private class FetchImageTask extends AsyncTask<String, Integer, Bitmap> {
		@Override
		protected Bitmap doInBackground(String... url) {
			Bitmap b = null;
			b = loadBitmap(url[0]);
			return b;
		}
	}

	private Bitmap loadBitmap(String url) {
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

}