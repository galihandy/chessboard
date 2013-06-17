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
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

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

public class FeaturedFragment extends Fragment {

	public static final int CLASS_ID = 0;

	private final int LAYOUT_2 = 2;
	private final int LAYOUT_3 = 3;

	private PullToRefreshListView pullToRefreshLv;
	private EventAdapter adapter;
	private List<HashMap<String, Object>> mylist;
	private TextView noDataTv;
	private JSONObject e;
	private String[] keys;
	private View view;
	private String url = MainActivity.GENERIC_QUERY + "sort_by="
			+ Database.EVENT_START_DATE;
	private boolean isFinishLoadData;
	private int selectedLayout;
	private String noDataText;

	public FeaturedFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		System.out.println("FEATURED oncreate called");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.multiview_featured, null);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		System.out.println("FEATURED onactivitycreated called");

		// show progress bar while load the data from url
		SwitchLayout1();

		noDataTv = (TextView) view.findViewById(R.id.no_data_textview);
		noDataTv.setText(noDataText);

		pullToRefreshLv = (PullToRefreshListView) view
				.findViewById(R.id.listview);

		pullToRefreshLv.setMode(Mode.BOTH);
		pullToRefreshLv
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						new JSONArrayLoaderTask()
								.execute(MainActivity.GENERIC_QUERY
										+ "sort_by="
										+ Database.EVENT_START_DATE);

					}
				});
		pullToRefreshLv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (adapter.isViewClickable(position - 1)) {

					int distance = adapter.getDistance(position - 1);
					int pos = position - distance - 1;

					@SuppressWarnings("unchecked")
					HashMap<String, Object> o = (HashMap<String, Object>) adapter
							.getItem(pos);

					Bundle bundle = new Bundle();
					bundle.putInt(Constant.STARTING_ACTIVITY, CLASS_ID);
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

		Button retryBtn = (Button) view.findViewById(R.id.retry_btn);
		retryBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				SwitchLayout1();
				new JSONArrayLoaderTask().execute(url);
			}
		});

		keys = Constant.KEYS;

		if (!isFinishLoadData) {
			new JSONArrayLoaderTask().execute(MainActivity.GENERIC_QUERY
					+ "sort_by=" + Database.EVENT_START_DATE);
		} else {
			showSelectedLayout();
		}
	}

	public void addToBackstack() {
		getActivity().getSupportFragmentManager().beginTransaction()
				.addToBackStack(null).commit();
	}

	@Override
	public void onResume() {
		super.onResume();

		pullToRefreshLv.setAdapter(adapter);
	}

	private void showSelectedLayout() {
		switch (selectedLayout) {
		case LAYOUT_2:
			SwitchLayout2();
			break;

		case LAYOUT_3:
			SwitchLayout3();
			break;
		}
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
				System.out.println("No connection");
				noDataTv.setText(Constant.NO_CONN);
				SwitchLayout3();

				noDataText = Constant.NO_CONN;
				selectedLayout = LAYOUT_3;
				return;
			}

			mylist = getListOfEventMap(result);
			if (mylist.size() == 0) {
				System.out.println("No event available");
				noDataTv.setText(Constant.NO_EVT_AVLBL);

				noDataText = Constant.NO_EVT_AVLBL;
				selectedLayout = LAYOUT_3;
				SwitchLayout3();
			} else {
				adapter = new EventAdapter(getActivity(), mylist);

				pullToRefreshLv.setAdapter(adapter);

				SwitchLayout2();
				selectedLayout = LAYOUT_2;
			}

			isFinishLoadData = true;
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
	}
}
