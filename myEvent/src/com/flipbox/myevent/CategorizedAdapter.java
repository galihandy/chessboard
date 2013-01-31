/**
 * 
 */
package com.flipbox.myevent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

/**
 * @author Galih
 * 
 */
public class CategorizedAdapter extends BaseExpandableListAdapter {

	private ArrayList<String> categoryList;
	private List<HashMap<String, Object>> childList;
	private HashMap<String, List<HashMap<String, Object>>> categoryContentMap;
	private HashMap<String, Object> child;
	private String category;
	private LayoutInflater inflater;
	private int parentRes;
	private int childRes;

	public CategorizedAdapter(Context context, ArrayList<String> categoryList,
			HashMap<String, List<HashMap<String, Object>>> categoryContentMap,
			int parentRes, int childRes) {

		this.categoryList = categoryList;
		this.categoryContentMap = categoryContentMap;
		this.parentRes = parentRes;
		this.childRes = childRes;
		this.inflater = LayoutInflater.from(context);
	}

	public Object getChild(int groupPosition, int childPosition) {

		category = categoryList.get(groupPosition);
		childList = categoryContentMap.get(category);

		return childList.get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {

		category = categoryList.get(groupPosition);
		childList = categoryContentMap.get(category);
		child = childList.get(childPosition);

		return Long.parseLong((String) child.get(Database.EVENT_ID));
	}

	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		View v;

		if (convertView == null) {
			convertView = inflater.inflate(childRes, parent, false);
		}
		v = convertView;

		if (childPosition % 2 != 0) {
			v.setBackgroundColor(Color.LTGRAY);
		} else {
			v.setBackgroundColor(Color.WHITE);
		}

		category = categoryList.get(groupPosition);
		childList = categoryContentMap.get(category);
		child = childList.get(childPosition);

		TextView eventName = (TextView) v.findViewById(R.id.event_name);
		eventName.setText((String) child.get(Database.EVENT_NAME));
		Log.i("getChildView", "name : " + eventName.getText());

		TextView startDate = (TextView) v.findViewById(R.id.event_startdate);
		String date = (String) child.get(Database.EVENT_START_DATE);
		startDate.setText(MyUtil.dateFormatter(date));

		return v;
	}

	public int getChildrenCount(int groupPosition) {
		category = categoryList.get(groupPosition);
		childList = categoryContentMap.get(category);

		return childList.size();
	}

	public Object getGroup(int groupPosition) {

		return categoryList.get(groupPosition);
	}

	public int getGroupCount() {

		return categoryList.size();
	}

	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		View v;

		if (convertView == null) {
			convertView = inflater.inflate(parentRes, parent, false);
		}
		v = convertView;

		TextView catName = (TextView) v.findViewById(R.id.category_name);
		catName.setText(categoryList.get(groupPosition));

		return v;
	}

	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEmpty() {
		return categoryList.isEmpty();
	}

}