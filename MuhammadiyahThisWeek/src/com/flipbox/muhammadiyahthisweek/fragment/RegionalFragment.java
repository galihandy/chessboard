package com.flipbox.muhammadiyahthisweek.fragment;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.flipbox.muhammadiyahthisweek.activity.MainActivity;
import com.flipbox.muhammadiyahthisweek.activity.RegionalEventListActivity;
import com.flipbox.muhammadiyahthisweek.adapter.LocationAdapter;
import com.flipbox.muhammadiyahthisweek.utils.Constant;
import com.flipbox.muhammadiyahthisweek.utils.JSONfunctions;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 19/4/13 Saat ini query nya masih get all category karena masih belum memiliki
 * webservice untuk mengquery semua location.
 * 
 * @author Galih
 * 
 */
public class RegionalFragment extends Fragment {

	public static final int CLASS_ID = 1;
	public static final String LOC_ID = "id_category";
	public static final String LOC_NAME = "name_category";
	public static final String LOC_DESC = "description_category";

	private final int LAYOUT_2 = 2;
	private final int LAYOUT_3 = 3;

	private PullToRefreshListView pullToRefreshLv;
	private LocationAdapter adapter;
	private TextView noDataTv;
	private ArrayList<String> locationList;
	private View view;
	private String noDataText;
	private boolean isFinishLoadData;
	private int selectedLayout;

	public RegionalFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		System.out.println("LOCATION oncreate called");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.multiview_regional, null);
		return view;
	}

	/** Called when the activity is first created. */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("LOCATION onactivitycreated called");

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
								.execute(MainActivity.GET_ALL_CATEGORY);
					}
				});

		pullToRefreshLv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				String location = locationList.get(position - 1);

				Intent i = new Intent(getActivity(),
						RegionalEventListActivity.class);
				Bundle b = new Bundle();
				b.putString(LOC_NAME, location);

				i.putExtra(Constant.LOCATION_BUNDLE, b);
				startActivity(i);
			}
		});

		Button retryBtn = (Button) view.findViewById(R.id.retry_btn);
		retryBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				new JSONArrayLoaderTask()
						.execute(MainActivity.GET_ALL_CATEGORY);
			}
		});

		SwitchLayout1();
		if (!isFinishLoadData) {
			new JSONArrayLoaderTask().execute(MainActivity.GET_ALL_CATEGORY);
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
		// adapter = new CategoryAdapter(getActivity(), categoryList);
		pullToRefreshLv.setAdapter(adapter);
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

			pullToRefreshLv.onRefreshComplete();

			JSONObject eJSONObject;
			locationList = new ArrayList<String>();

			if (result == null) {
				noDataText = Constant.NO_CONN;
				noDataTv.setText(noDataText);
				SwitchLayout3();
				selectedLayout = LAYOUT_3;
			} else if (result.length() == 0) {
				noDataText = Constant.NO_EVT_AVLBL;
				noDataTv.setText(noDataText);
				SwitchLayout3();
				selectedLayout = LAYOUT_3;
			} else {

				try {
					for (int i = 0; i < result.length(); i++) {
						eJSONObject = result.getJSONObject(i);

						// i != 0 cuma buat handle kondisi sekarang yang belum
						// punya webservice buat query semua location
						if (isValid(eJSONObject)) {

							// get category and add it if it is not exist
							// in the category list
							String location = eJSONObject.getString(LOC_NAME);
							if (!locationList.contains(location)) {
								locationList.add(location);
							}
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				adapter = new LocationAdapter(getActivity(), locationList);
				pullToRefreshLv.setAdapter(adapter);
				selectedLayout = LAYOUT_2;
				SwitchLayout2();
			}
			isFinishLoadData = true;
		}

		private boolean isValid(JSONObject e) throws JSONException {
			if (e.getString(LOC_NAME) == null || e.getString(LOC_NAME) == ""
					|| e.getString(LOC_NAME).equals("null")
					|| e.getString(LOC_NAME).startsWith("null")) {
				return false;
			} else {
				return true;
			}
		}
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
}