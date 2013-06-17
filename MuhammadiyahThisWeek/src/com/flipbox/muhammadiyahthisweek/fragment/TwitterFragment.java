package com.flipbox.muhammadiyahthisweek.fragment;

import com.flipbox.muhammadiyahthisweek.adapter.TwitterAdapter;
import com.flipbox.muhammadiyahthisweek.utils.Constant;
import com.flipbox.muhammadiyahthisweek.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;

import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TwitterFragment extends Fragment {

	// myevent key
	public static final String CONSUMMER_KEY = "LevFBVy7W1ifUCOS4HpoKA";
	public static final String CONSUMMER_SECRET = "2FYoBybVDzFZmCoV41EBoH9TQJsPTtqSqrdohyohA";

	// flipbox key
	public static final String ACCESS_TOKEN = "328403521-zUovsGFExGBG5LXlVLnAivWrF4FYR7PMbQnmxAbx";
	public static final String ACCESS_SECRET = "CBJFXg4NvmZA2fUKILWYJ5RwUF91iGlLlJOFEFPnnM";

	private final String SCREEN_NAME = "flipboxstudio";
	private final String ANDROID_TWITTER_PACKAGE = "com.twitter.android";

	private final int LAYOUT_2 = 2;
	private final int LAYOUT_3 = 3;

	private Twitter twitter;
	private TwitterAdapter adapter;
	private ResponseList<twitter4j.Status> statusList;
	private PullToRefreshListView pullToRefreshLv;
	private TextView noDataTv;
	private TextView totalFollower;
	private TextView followUs;
	private View view;

	private int followerCount;
	private boolean isFinishLoadData;
	private int selectedLayout;
	private String noDataText;

	public TwitterFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.twitter, null);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		switchToLoadingView();

		pullToRefreshLv = (PullToRefreshListView) view
				.findViewById(R.id.listview);
		pullToRefreshLv.setMode(Mode.BOTH);
		pullToRefreshLv
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						new TwitterLoaderTask()
								.execute(new ConfigurationBuilder());
					}

				});

		noDataTv = (TextView) view.findViewById(R.id.no_data_textview);
		noDataTv.setText(noDataText);

		totalFollower = (TextView) view.findViewById(R.id.follower_total);
		followUs = (TextView) view.findViewById(R.id.follow_us);
		followUs.setText(followUs.getText() + SCREEN_NAME);

		Button retryBtn = (Button) view.findViewById(R.id.retry_btn);
		retryBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				switchToLoadingView();
				new TwitterLoaderTask().execute(new ConfigurationBuilder());
			}
		});

		if (!isFinishLoadData) {
			new TwitterLoaderTask().execute(new ConfigurationBuilder());
		} else {
			showSelectedLayout();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		pullToRefreshLv.setAdapter(adapter);
	}

	public void addToBackstack() {
		getActivity().getSupportFragmentManager().beginTransaction()
				.addToBackStack(null).commit();
	}

	private void showSelectedLayout() {
		switch (selectedLayout) {
		case LAYOUT_2:
			switchToListView();
			break;

		case LAYOUT_3:
			switchToNoDataTextView();
			break;
		}
	}

	/**
	 * This method will disable/dismiss the listview and show progress bar
	 * within multivew.xml layout.
	 */
	private void switchToLoadingView() {
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
	private void switchToListView() {
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

	private void switchToNoDataTextView() {
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

	private class TwitterLoaderTask extends
			AsyncTask<ConfigurationBuilder, Void, TwitterAdapter> {

		@Override
		protected TwitterAdapter doInBackground(ConfigurationBuilder... cb) {

			if (getActivity() == null)
				return null;

			adapter = null;

			// configure authentication to twitter api with flipbox account
			ConfigurationBuilder confBuilder = cb[0];
			confBuilder.setOAuthConsumerKey(CONSUMMER_KEY)
					.setOAuthConsumerSecret(CONSUMMER_SECRET)
					.setOAuthAccessToken(ACCESS_TOKEN)
					.setOAuthAccessTokenSecret(ACCESS_SECRET);
			Configuration conf = confBuilder.build();

			// make twitter object with the configuration.
			twitter = new TwitterFactory(conf).getInstance();

			try {
				statusList = twitter.getUserTimeline();
				adapter = new TwitterAdapter(getActivity(), statusList);
			} catch (TwitterException e) {
				e.printStackTrace();
			}

			return adapter;
		}

		@Override
		protected void onPostExecute(TwitterAdapter result) {
			super.onPostExecute(result);

			pullToRefreshLv.onRefreshComplete();

			if (result == null) {
				noDataText = Constant.NO_CONN;
				noDataTv.setText(noDataText);
				switchToNoDataTextView();
				selectedLayout = LAYOUT_3;
			} else if (result.isEmpty()) {
				noDataText = "No tweet by @" + SCREEN_NAME;
				noDataTv.setText(noDataText);
				switchToNoDataTextView();
				selectedLayout = LAYOUT_3;
			} else {
				pullToRefreshLv.setAdapter(result);

				followerCount = result.getFollowerCount();
				totalFollower.setText(followerCount + "");

				followUs.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {

						Intent twitterIntent = null;

						try {
							getActivity().getPackageManager().getPackageInfo(
									ANDROID_TWITTER_PACKAGE, 0);
							twitterIntent = new Intent(Intent.ACTION_VIEW, Uri
									.parse("twitter://user?user_id="
											+ twitter.getId()));
						} catch (NameNotFoundException e) {
							twitterIntent = new Intent(Intent.ACTION_VIEW,
									Uri.parse("https://twitter.com/"
											+ SCREEN_NAME));
						} catch (IllegalStateException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (TwitterException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						startActivity(twitterIntent);
					}
				});
				selectedLayout = LAYOUT_2;
				switchToListView();
			}
			isFinishLoadData = true;
		}
	}
}
