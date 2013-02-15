package com.flipbox.arsenalindonesia;

import java.util.Date;
import java.util.List;

import twitter4j.Status;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TwitterAdapter extends BaseAdapter {

	private List<Status> listOfStatus;
	private LayoutInflater inflater;

	public TwitterAdapter(Context context, List<Status> listOfStatus) {
		this.listOfStatus = listOfStatus;
		inflater = LayoutInflater.from(context);
	}

	public int getCount() {

		return listOfStatus.size();
	}

	public Object getItem(int position) {
		Status s = listOfStatus.get(position);

		return s;
	}

	public long getItemId(int position) {
		Status s = listOfStatus.get(position);

		return s.getId();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_twitter_item, null,
					false);
		}
		v = convertView;

		TextView screenNameTv = (TextView) v.findViewById(R.id.screen_name);
		TextView elapsedTimeTv = (TextView) v
				.findViewById(R.id.tweet_elapsed_time);
		TextView tweetTv = (TextView) v.findViewById(R.id.tweet_text);

		Status status = listOfStatus.get(position);
		String elapsedTime = elapsedTime(status.getCreatedAt());
		
		screenNameTv.setText("@" + status.getUser().getScreenName());
		tweetTv.setText(status.getText());
		elapsedTimeTv.setText(elapsedTime);
		
		return v;
	}

	private String elapsedTime(Date time) {

		final int millisToSecondsScale = 1000;

		String elapsedTime = "";
		long pTime = time.getTime();
		long currentTime = System.currentTimeMillis();

		long passedTime = currentTime - pTime;
		if (passedTime >= MyUtil.A_DAY) {
			passedTime = (long) Math.ceil(passedTime / MyUtil.A_DAY);
			elapsedTime = passedTime + " day";

		} else if (passedTime >= MyUtil.AN_HOUR) {
			passedTime = (long) Math.ceil(passedTime / MyUtil.AN_HOUR);
			elapsedTime = passedTime + " hour";

		} else if (passedTime >= MyUtil.A_MINUTE) {
			passedTime = (long) Math.ceil(passedTime / MyUtil.A_MINUTE);
			elapsedTime = passedTime + " minute";

		} else {
			passedTime = passedTime / millisToSecondsScale;
			elapsedTime = passedTime + " second";

		}
		
		if(passedTime > 1){
			elapsedTime += "s";
		}

		return elapsedTime;
	}

}
