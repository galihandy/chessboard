/**
 * 
 */
package com.flipbox.myevent;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author Galih
 * 
 */
public class CategoryAdapter extends BaseAdapter {

	private ArrayList<String> categoryList;
	private LayoutInflater inflater;

	public CategoryAdapter(Context context, ArrayList<String> categoryList) {

		this.categoryList = categoryList;
		this.inflater = LayoutInflater.from(context);
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return categoryList.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return categoryList.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		String category = categoryList.get(position);
		View v;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_category_item, null);
		}
		v = convertView;

		TextView catName = (TextView) v.findViewById(R.id.category_name);
		catName.setText(category);

		return v;
	}
}
