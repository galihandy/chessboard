/**
 * 
 */
package com.flipbox.myevent;

import java.io.BufferedInputStream;
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
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

/**
 * @author Galih
 * 
 */
public class CategoryEventListActivity extends Activity {

	public static final int CLASS_ID = 4;
	private final String noEventMsg = "No event in ";
	private JSONObject e;
	private String category;
	private String[] keys;
	private ListView eventLv;
	private TextView catTv;
	private TextView noDataTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_list);

		keys = MyUtil.KEYS;

		Intent in = getIntent();
		category = in.getStringExtra(CategoryActivity.CAT_NAME);

		noDataTv = (TextView) findViewById(R.id.no_data_textview);
		catTv = (TextView) findViewById(R.id.single_list_tv);

		SwitchLayout1();

		new JSONArrayLoaderTask().execute(MainActivity.GET_EVENT_BY_CAT
				+ category);
	}

	/**
	 * This method will disable/dismiss the listview and show progress bar
	 * within multivew.xml layout.
	 */
	private void SwitchLayout1() {
		// progress bar layout
		RelativeLayout loadingLayout = (RelativeLayout) findViewById(R.id.loadingRelLayout);
		// list layout
		RelativeLayout eventListLayout = (RelativeLayout) findViewById(R.id.listRelLayout);
		// no data text layout
		RelativeLayout noDataLayout = (RelativeLayout) findViewById(R.id.noDataRelLayout);

		// show progress bar & hide the listview
		loadingLayout.setVisibility(View.VISIBLE);
		eventListLayout.setVisibility(View.GONE);
		noDataLayout.setVisibility(View.GONE);
	}

	/**
	 * This method will disable/dismiss the progress bar and show listview
	 * within multivew.xml layout.
	 */
	private void SwitchLayout2() {
		// progress bar layout
		RelativeLayout loadingLayout = (RelativeLayout) findViewById(R.id.loadingRelLayout);
		// list layout
		RelativeLayout eventListLayout = (RelativeLayout) findViewById(R.id.listRelLayout);
		// no data text layout
		RelativeLayout noDataLayout = (RelativeLayout) findViewById(R.id.noDataRelLayout);

		// hide the progress bar & show the listview
		loadingLayout.setVisibility(View.GONE);
		eventListLayout.setVisibility(View.VISIBLE);
		noDataLayout.setVisibility(View.GONE);
	}

	private void SwitchLayout3() {
		// progress bar layout
		RelativeLayout loadingLayout = (RelativeLayout) findViewById(R.id.loadingRelLayout);
		// list layout
		RelativeLayout eventListLayout = (RelativeLayout) findViewById(R.id.listRelLayout);
		// no data text layout
		RelativeLayout noDataLayout = (RelativeLayout) findViewById(R.id.noDataRelLayout);

		// hide the progress bar & show the listview
		loadingLayout.setVisibility(View.GONE);
		eventListLayout.setVisibility(View.GONE);
		noDataLayout.setVisibility(View.VISIBLE);
	}

	private class JSONArrayLoaderTask extends
			AsyncTask<String, Void, JSONArray> {
		HashMap<String, Object> map;

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
			System.out.println("jArray: " + result);
			if (result == null) {
				noDataTv.setText(MyUtil.NO_CONN);
				SwitchLayout3();
			} else {
				List<HashMap<String, Object>> eventList = new ArrayList<HashMap<String, Object>>();

				try {
					for (int i = 0; i < result.length(); i++) {
						map = new HashMap<String, Object>();
						e = result.getJSONObject(i);

						System.out.println("ZZ NAME : "
								+ e.getString(Database.EVENT_NAME) + ":");

						if (isValid(e)) {
							for (int j = 0; j < keys.length; j++) {
								map.put(keys[j], e.getString(keys[j]));
							}
							eventList.add(map);
						}
					}
				} catch (JSONException ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}

				String[] from = new String[] { Database.EVENT_NAME,
						Database.EVENT_START_DATE, "image" };

				int[] to = new int[] { R.id.event_name, R.id.event_startdate,
						R.id.event_image };

				ListAdapter adapter = new CustomAdapter(getBaseContext(),
						eventList, R.layout.list_feature_item, from, to);

				eventLv = (ListView) findViewById(R.id.event_listview);
				eventLv.setAdapter(adapter);

				eventLv.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						@SuppressWarnings("unchecked")
						HashMap<String, String> o = (HashMap<String, String>) eventLv
								.getItemAtPosition(position);

						Intent in = new Intent(getBaseContext(),
								DetailEventActivity.class);
						in.putExtra("starting act", CLASS_ID);
						for (int j = 0; j < keys.length; j++) {
							String key = keys[j];
							in.putExtra(key, o.get(key));
						}
						startActivity(in);
					}
				});

				if (adapter.isEmpty()) {
					noDataTv.setText(noEventMsg + category + " category");
					SwitchLayout3();
				} else {
					catTv.setText(category + " event list");
					catTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
					SwitchLayout2();
				}

			}
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