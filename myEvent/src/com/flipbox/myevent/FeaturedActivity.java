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
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class FeaturedActivity extends Activity {

	JSONObject e;
	JSONArray e_content;
	ListView lv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multiview);
		String url = "http://flipboxstudio.com/myEvent/service/getEvent.php";
		SwitchLayout1();
		new JSONArrayLoaderTask().execute(url);

	}

	private void SwitchLayout2() {
		RelativeLayout Layout1 = (RelativeLayout) findViewById(R.id.layout1);
		RelativeLayout Layout2 = (RelativeLayout) findViewById(R.id.layout2);

		// Enable Layout 2 and Disable Layout 1
		Layout1.setVisibility(View.GONE);
		Layout2.setVisibility(View.VISIBLE);
	}

	private void SwitchLayout1() {
		RelativeLayout Layout1 = (RelativeLayout) findViewById(R.id.layout1);
		RelativeLayout Layout2 = (RelativeLayout) findViewById(R.id.layout2);

		// Enable Layout 1 & Disable Layout2
		Layout1.setVisibility(View.VISIBLE);
		Layout2.setVisibility(View.GONE);
	}

	class JSONArrayLoaderTask extends AsyncTask<String, Void, JSONArray> {
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

			} else {
				List<HashMap<String, Object>> mylist = new ArrayList<HashMap<String, Object>>();

				try {
					for (int i = 0; i < result.length(); i++) {
						map = new HashMap<String, Object>();
						e = result.getJSONObject(i);
						e_content = e.names();
						
						System.out.println("ZZ TITLE : " + e.getString("title")
								+ ":");
						if (isValid(e)) {
							for (int j = 0; j < e_content.length(); j++) {
								map.put(e_content.getString(j),
										e.getString(e_content.getString(j)));
								//map.put("image", "http://mydinkydonuts.com/wp-content/uploads/2011/09/big-event.jpg");
//								new FetchImageTask() {
//									protected void onPostExecute(Bitmap result) {
//										if (result != null) {
//											map.put("image", result);
//										}
//									}
//								}.execute(e.getString("image"));

							}
							mylist.add(map);
						}
					}
				} catch (JSONException ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}

				String[] from = new String[] { "title", "startDate", "image" };

				int[] to = new int[] { R.id.item_title, R.id.item_subtitle,
						R.id.item_image };

				ListAdapter adapter = new CustomAdapter(getBaseContext(),
						mylist, R.layout.list_feature_item, from, to);

				lv = (ListView) findViewById(R.id.listview1);
				lv.setAdapter(adapter);

				lv.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						@SuppressWarnings("unchecked")
						HashMap<String, String> o = (HashMap<String, String>) lv
								.getItemAtPosition(position);
						
						Intent in = new Intent(getBaseContext(),
								DetailFeatureEventActivity.class);
						
						String[] keys = new String[e_content.length()];
						
						for (int j = 0; j < e_content.length(); j++) {
							try {
								keys[j] = e_content.getString(j);
								in.putExtra(e_content.getString(j),
										o.get(e_content.getString(j)));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						in.putExtra("keys", keys);
						startActivity(in);
					}
				});
			}

			// switch view
			SwitchLayout2();
		}

		private boolean isValid(JSONObject e) throws JSONException {
			if (e.getString("title") == null || e.getString("title") == ""
					|| e.getString("title").equals("null")
					|| e.getString("title").startsWith("null")) {
				return false;
			} else {
				return true;
			}
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