package com.flipbox.muhammadiyahthisweek.adapter;

import java.util.Date;
import java.util.List;

import com.flipbox.muhammadiyahthisweek.utils.Constant;
import com.flipbox.muhammadiyahthisweek.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import twitter4j.Status;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TwitterAdapter extends BaseAdapter {

	private List<Status> listOfStatus;
	private LayoutInflater inflater;
	private int followerCount;

	public TwitterAdapter(Context context, List<Status> listOfStatus) {

		if (context == null)
			return;

		this.listOfStatus = listOfStatus;
		countUserFollower();

		inflater = LayoutInflater.from(context);
	}

	public int getCount() {

		if (listOfStatus != null)
			return listOfStatus.size();
		else
			return 0;
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

		ImageView profpic = (ImageView) v.findViewById(R.id.profpic_thumbnail);
		TextView elapsedTimeTv = (TextView) v
				.findViewById(R.id.tweet_elapsed_time);
		TextView tweetTv = (TextView) v.findViewById(R.id.tweet_text);

		Status status = listOfStatus.get(position);

		String elapsedTime = elapsedTime(status.getCreatedAt());

		tweetTv.setText(status.getText());
		elapsedTimeTv.setText(elapsedTime);
		ImageLoader.getInstance().displayImage(
				status.getUser().getOriginalProfileImageURL(), profpic);

		return v;
	}

	private String elapsedTime(Date time) {

		final int millisToSecondsScale = 1000;

		String elapsedTime = "";
		long pTime = time.getTime();
		long currentTime = System.currentTimeMillis();

		long passedTime = currentTime - pTime;
		if (passedTime >= Constant.A_DAY) {
			passedTime = (long) Math.ceil(passedTime / Constant.A_DAY);
			elapsedTime = passedTime + " day";

		} else if (passedTime >= Constant.AN_HOUR) {
			passedTime = (long) Math.ceil(passedTime / Constant.AN_HOUR);
			elapsedTime = passedTime + " hour";

		} else if (passedTime >= Constant.A_MINUTE) {
			passedTime = (long) Math.ceil(passedTime / Constant.A_MINUTE);
			elapsedTime = passedTime + " minute";

		} else {
			passedTime = passedTime / millisToSecondsScale;
			elapsedTime = passedTime + " second";

		}

		if (passedTime > 1) {
			elapsedTime += "s";
		}

		return elapsedTime;
	}

	public int getFollowerCount() {
		return followerCount;
	}

	private void countUserFollower() {
		if (listOfStatus.size() != 0) {
			Status s = listOfStatus.get(0);

			followerCount = s.getUser().getFollowersCount();
		}
	}

}
