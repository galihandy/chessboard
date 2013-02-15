package com.flipbox.arsenalindonesia;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CategoryActivity extends Activity {

	public static final int CLASS_ID = 1;
	public static final String CAT_ID = "id_category";
	public static final String CAT_NAME = "name_category";
	public static final String CAT_DESC = "description_category";

	private ListView catLv;
	private TextView noDataTv;
	private ArrayList<String> categoryList;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multiview);
		noDataTv = (TextView) findViewById(R.id.no_data_textview);
		
		SwitchLayout1();
		new JSONArrayLoaderTask().execute(MainActivity.GET_ALL_CATEGORY);
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

			JSONObject eJSONObject;
			categoryList = new ArrayList<String>();

			if (result == null) {
				noDataTv.setText(MyUtil.NO_CONN);
				SwitchLayout3();
			} else {

				try {
					for (int i = 0; i < result.length(); i++) {
						eJSONObject = result.getJSONObject(i);

						if (isValid(eJSONObject)) {

							// get category and add it if it is not exist
							// in the category list
							String category = eJSONObject
									.getString(CAT_NAME);
							if (!categoryList.contains(category)) {
								categoryList.add(category);
							}
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				CategoryAdapter adapter = new CategoryAdapter(
						CategoryActivity.this, categoryList);

				catLv = (ListView) findViewById(R.id.listview);
				catLv.setAdapter(adapter);
				catLv.setOnItemClickListener(new OnItemClickListener() {

					public void onItemClick(AdapterView<?> parent, View v,
							int position, long id) {
						Intent in = new Intent(getBaseContext(),
								CategoryEventListActivity.class);

						String category = categoryList.get(position);
						in.putExtra(CAT_NAME, category);

						startActivity(in);
					}
				});

				if (adapter.isEmpty()) {
					noDataTv.setText(MyUtil.NO_EVT_AVLBL);
					SwitchLayout3();

				} else {
					SwitchLayout2();
				}
			}
		}

		private boolean isValid(JSONObject e) throws JSONException {
			if (e.getString(CAT_NAME) == null
					|| e.getString(CAT_NAME) == ""
					|| e.getString(CAT_NAME).equals("null")
					|| e.getString(CAT_NAME).startsWith("null")) {
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
		eventListLayout.setVisibility(View.GONE);
		noDataLayout.setVisibility(View.VISIBLE);
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