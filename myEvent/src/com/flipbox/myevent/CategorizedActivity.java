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
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;

public class CategorizedActivity extends Activity {

	public static final int CLASS_ID = 1;

	private ExpandableListView expandLv;
	private ArrayList<String> categoryList;
	private HashMap<String, List<HashMap<String, Object>>> categoryEventListMap;
	private String[] keys;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multiview_categorized);
		SwitchLayout1();
		keys = MyUtil.KEYS;
		new JSONArrayLoaderTask().execute(MainActivity.GET_ALL_EVENT);
	}

	private class JSONArrayLoaderTask extends
			AsyncTask<String, Void, JSONArray> {

		// Before running code in the separate thread
		@Override
		protected void onPreExecute() {

		}

		/** Doing the parsing of json data in a non-ui thread */
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
			System.out.println("cat_jArray: " + result);

			HashMap<String, Object> event;
			JSONObject eJSONObject;

			categoryList = new ArrayList<String>();
			categoryEventListMap = new HashMap<String, List<HashMap<String, Object>>>();

			if (result == null) {

			} else {
				List<HashMap<String, Object>> eventList;

				try {
					for (int i = 0; i < result.length(); i++) {
						eJSONObject = result.getJSONObject(i);

						if (isValid(eJSONObject)) {

							event = new HashMap<String, Object>();

							for (int j = 0; j < keys.length; j++) {
								String name = keys[j];
								Object value = eJSONObject.get(name);
								event.put(name, value);
							}

							// get object category and check is it exist or not
							// in the category list
							String category = eJSONObject
									.getString(Database.EVENT_CAT);
							if (!categoryList.contains(category)) {
								categoryList.add(category);
								eventList = new ArrayList<HashMap<String, Object>>();
							} else {
								eventList = (ArrayList<HashMap<String, Object>>) categoryEventListMap
										.get(category);
							}

							eventList.add(event);
							categoryEventListMap.put(category, eventList);
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				CategorizedAdapter adapter = new CategorizedAdapter(
						CategorizedActivity.this, categoryList,
						categoryEventListMap, R.layout.list_category_item,
						R.layout.list_feature_item);

				expandLv = (ExpandableListView) findViewById(R.id.categorized_listview);
				expandLv.setAdapter(adapter);
				expandLv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

					public boolean onChildClick(ExpandableListView parent,
							View v, int groupPosition, int childPosition,
							long id) {

						String category = categoryList.get(groupPosition);
						List<HashMap<String, Object>> childList = categoryEventListMap
								.get(category);
						HashMap<String, Object> child = childList
								.get(childPosition);

						Intent in = new Intent(getBaseContext(),
								DetailEventActivity.class);
						in.putExtra("starting act", CLASS_ID);
						for (int j = 0; j < keys.length; j++) {
							String key = keys[j];
							in.putExtra(key, (String) child.get(key));
						}
						startActivity(in);

						return true;
					}
				});

				if (adapter.isEmpty()) {
					SwitchLayout3();

				} else {
					SwitchLayout2();
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
		eventListLayout.setVisibility(View.VISIBLE);
		noDataLayout.setVisibility(View.GONE);
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