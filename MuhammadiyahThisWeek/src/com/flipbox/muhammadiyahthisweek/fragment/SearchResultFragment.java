package com.flipbox.muhammadiyahthisweek.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.flipbox.muhammadiyahthisweek.activity.DetailEventActivity;
import com.flipbox.muhammadiyahthisweek.activity.MainActivity;
import com.flipbox.muhammadiyahthisweek.adapter.EventAdapter;
import com.flipbox.muhammadiyahthisweek.database.Database;
import com.flipbox.muhammadiyahthisweek.utils.Constant;
import com.flipbox.muhammadiyahthisweek.utils.JSONfunctions;
import com.flipbox.muhammadiyahthisweek.utils.MyUtil;
import com.flipbox.muhammadiyahthisweek.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SearchResultFragment extends Fragment {

	public static final int CLASS_ID = 4;

	private View view;
	private TextView searchResult;
	private TextView noDataTv;
	private PullToRefreshListView pullToRefreshLv;
	private JSONObject e;
	private String[] keys;
	private String searchKey;
	private String searchResultText;
	private int searchResultCount;

	public SearchResultFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.titled_list, null);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		keys = Constant.KEYS;

		Bundle b = getArguments();

		searchKey = b.getString(Constant.SEARCH_QUERY);

		searchResult = (TextView) view.findViewById(R.id.list_title_tv);
		searchResultText = "Search result for \'" + searchKey + "\' : ";
		searchResult.setText(searchResultText);

		noDataTv = (TextView) view.findViewById(R.id.no_data_textview);

		pullToRefreshLv = (PullToRefreshListView) view
				.findViewById(R.id.event_listview);
		pullToRefreshLv.setMode(Mode.BOTH);
		pullToRefreshLv
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						new JSONArrayLoaderTask()
								.execute(MainActivity.GENERIC_QUERY + "name="
										+ searchKey + "&sort_by="
										+ Database.EVENT_START_DATE);
					}
				});

		Button retryBtn = (Button) view.findViewById(R.id.retry_btn);
		retryBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				SwitchLayout1();
				new JSONArrayLoaderTask().execute(MainActivity.GENERIC_QUERY
						+ "name=" + searchKey + "&sort_by="
						+ Database.EVENT_START_DATE);
			}
		});

		// show progress bar while load the data from url
		SwitchLayout1();

		new JSONArrayLoaderTask().execute(MainActivity.GENERIC_QUERY + "name="
				+ searchKey + "&sort_by=" + Database.EVENT_START_DATE);
	}

	/**
	 * This method will disable/dismiss the listview and show progress bar
	 * within multivew.xml layout.
	 */
	private void SwitchLayout1() {
		// progress bar layout
		RelativeLayout loadingLayout = (RelativeLayout) view
				.findViewById(R.id.loadingRelLayout);
		// list layout
		RelativeLayout eventListLayout = (RelativeLayout) view
				.findViewById(R.id.listRelLayout);
		// no data text layout
		RelativeLayout noDataLayout = (RelativeLayout) view
				.findViewById(R.id.noDataRelLayout);

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
		RelativeLayout loadingLayout = (RelativeLayout) view
				.findViewById(R.id.loadingRelLayout);
		// list layout
		RelativeLayout eventListLayout = (RelativeLayout) view
				.findViewById(R.id.listRelLayout);
		// no data text layout
		RelativeLayout noDataLayout = (RelativeLayout) view
				.findViewById(R.id.noDataRelLayout);

		// hide the progress bar & show the listview
		loadingLayout.setVisibility(View.GONE);
		eventListLayout.setVisibility(View.VISIBLE);
		noDataLayout.setVisibility(View.GONE);
	}

	private void SwitchLayout3() {
		// progress bar layout
		RelativeLayout loadingLayout = (RelativeLayout) view
				.findViewById(R.id.loadingRelLayout);
		// list layout
		RelativeLayout eventListLayout = (RelativeLayout) view
				.findViewById(R.id.listRelLayout);
		// no data text layout
		RelativeLayout noDataLayout = (RelativeLayout) view
				.findViewById(R.id.noDataRelLayout);

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

			if (getActivity() == null)
				return;
			pullToRefreshLv.onRefreshComplete();

			if (result == null) {
				noDataTv.setText(Constant.NO_CONN);
				SwitchLayout3();
				return;
			}

			List<HashMap<String, Object>> eventList = getListOfEventMap(result);
			if (eventList.size() == 0) {
				searchResultCount = 0;
			} else {

				final EventAdapter adapter = new EventAdapter(getActivity(),
						eventList);

				pullToRefreshLv.setAdapter(adapter);

				pullToRefreshLv
						.setOnItemClickListener(new OnItemClickListener() {
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {

								if (adapter.isViewClickable(position - 1)) {

									int distance = adapter
											.getDistance(position - 1);
									int pos = position - distance - 1;

									@SuppressWarnings("unchecked")
									HashMap<String, String> o = (HashMap<String, String>) adapter
											.getItem(pos);

									Bundle bundle = new Bundle();
									bundle.putInt(Constant.STARTING_ACTIVITY,
											CLASS_ID);
									for (int j = 0; j < keys.length; j++) {
										String key = keys[j];
										String data = (String) o.get(key);

										bundle.putString(key, data);
									}

									Intent i = new Intent(getActivity(),
											DetailEventActivity.class);
									i.putExtra(Constant.EVENT_BUNDLE, bundle);
									startActivity(i);
								}
							}
						});

				searchResultCount = eventList.size();
			}

			searchResult.setText(searchResultText + searchResultCount);
			// switch view
			SwitchLayout2();

		}
		
		private List<HashMap<String, Object>> getListOfEventMap(
				JSONArray jsonArray) {
			List<HashMap<String, Object>> mylist = new ArrayList<HashMap<String, Object>>();
			System.out.println("event length " + jsonArray.length());
			try {
				for (int i = 0; i < jsonArray.length(); i++) {
					map = new HashMap<String, Object>();
					e = jsonArray.getJSONObject(i);

					if (isValid(e)
							&& !MyUtil.isDatePassed(e
									.getString(Database.EVENT_START_DATE))) {
						for (int j = 0; j < keys.length; j++) {
							map.put(keys[j], e.getString(keys[j]));
						}
						mylist.add(map);
					}
				}
			} catch (JSONException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}

			return mylist;
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

}
