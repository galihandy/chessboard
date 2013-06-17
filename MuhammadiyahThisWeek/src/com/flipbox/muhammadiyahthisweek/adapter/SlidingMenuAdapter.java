package com.flipbox.muhammadiyahthisweek.adapter;

import com.flipbox.muhammadiyahthisweek.utils.Constant;
import com.flipbox.muhammadiyahthisweek.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SlidingMenuAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private String[] listMenu;

	public SlidingMenuAdapter(Context context) {

		if (context == null)
			return;

		this.inflater = LayoutInflater.from(context);
		listMenu = context.getResources().getStringArray(R.array.menu_list);
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return listMenu.length;
	}

	public Object getItem(int pos) {
		// TODO Auto-generated method stub
		return listMenu[pos];
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_slidding_menu, null);
		}
		v = convertView;

		TextView menuName = (TextView) v.findViewById(R.id.menu_name);
		menuName.setText(listMenu[position]);

		ImageView menuThumb = (ImageView) v.findViewById(R.id.menu_thumb);
		setMenuThumbImage(menuThumb, position);
		return v;
	}

	private void setMenuThumbImage(ImageView iv, int menuPos) {
		switch (menuPos) {
		case Constant.HOME:
			iv.setImageResource(R.drawable.ic_home_not_selected);
			break;

		case Constant.BY_LOCATION:
			iv.setImageResource(R.drawable.ic_location_not_selected);
			break;

		case Constant.BY_CATEGORIES:
			iv.setImageResource(R.drawable.ic_category_not_selected);
			break;

		case Constant.MY_EVENTS:
			iv.setImageResource(R.drawable.ic_myevent_not_selected);
			break;

		case Constant.TWITTER:
			iv.setImageResource(R.drawable.ic_twitter_not_selected);
			break;
		case Constant.ARCHIVE:
			iv.setImageResource(R.drawable.ic_archieve_not_selected);
			break;

		}
	}

}
