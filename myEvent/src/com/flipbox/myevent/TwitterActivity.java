package com.flipbox.myevent;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TwitterActivity extends Activity {

	// myevent key
	public static final String CONSUMMER_KEY = "LevFBVy7W1ifUCOS4HpoKA";
	public static final String CONSUMMER_SECRET = "2FYoBybVDzFZmCoV41EBoH9TQJsPTtqSqrdohyohA";

	// flipbox key
	public static final String ACCESS_TOKEN = "328403521-zUovsGFExGBG5LXlVLnAivWrF4FYR7PMbQnmxAbx";
	public static final String ACCESS_SECRET = "CBJFXg4NvmZA2fUKILWYJ5RwUF91iGlLlJOFEFPnnM";

	private ListView tweetListLv;
	private TextView noDataTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.multiview);
		switchToLoadingView();
		tweetListLv = (ListView) findViewById(R.id.listview);
		noDataTv = (TextView) findViewById(R.id.no_data_textview);
		
		new TwitterLoaderTask().execute(new ConfigurationBuilder());
	}

	/**
	 * This method will disable/dismiss the listview and show progress bar
	 * within multivew.xml layout.
	 */
	private void switchToLoadingView() {
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
	private void switchToListView() {
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

	private void switchToNoDataTextView() {
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

	private class TwitterLoaderTask extends
			AsyncTask<ConfigurationBuilder, Void, TwitterAdapter> {

		@Override
		protected TwitterAdapter doInBackground(ConfigurationBuilder... cb) {
			TwitterAdapter adapter = null;

			// configure authentication to twitter api with flipbox account
			ConfigurationBuilder confBuilder = cb[0];
			confBuilder.setOAuthConsumerKey(CONSUMMER_KEY)
					.setOAuthConsumerSecret(CONSUMMER_SECRET)
					.setOAuthAccessToken(ACCESS_TOKEN)
					.setOAuthAccessTokenSecret(ACCESS_SECRET);
			Configuration conf = confBuilder.build();

			// make twitter object with the configuration.
			Twitter twitter = new TwitterFactory(conf).getInstance();
			try {
				adapter = new TwitterAdapter(TwitterActivity.this,
						twitter.getUserTimeline());

			} catch (TwitterException e) {
				e.printStackTrace();
			}

			return adapter;
		}

		@Override
		protected void onPostExecute(TwitterAdapter result) {
			super.onPostExecute(result);

			if(result == null){
				noDataTv.setText(MyUtil.NO_CONN);
				switchToNoDataTextView();
			} else if (result.isEmpty()) {
				noDataTv.setText("No tweet by @filpboxstudio");
				switchToNoDataTextView();
			} else {
				tweetListLv.setAdapter(result);
				switchToListView();
			}
		}
	}
}