package com.flipbox.muhammadiyahthisweek.fragment;

import com.flipbox.muhammadiyahthisweek.activity.CategoryEventListActivity;
import com.flipbox.muhammadiyahthisweek.activity.DetailEventActivity;
import com.flipbox.muhammadiyahthisweek.activity.RegionalEventListActivity;
import com.flipbox.muhammadiyahthisweek.activity.SearchResultActivity;
import com.flipbox.muhammadiyahthisweek.adapter.SlidingMenuAdapter;
import com.flipbox.muhammadiyahthisweek.utils.CommonVariables;
import com.flipbox.muhammadiyahthisweek.utils.Constant;
import com.flipbox.muhammadiyahthisweek.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

public class NavigationMenuFragment extends Fragment {

	private ListView menuList;
	private View view;
	private EditText searchInput;

	public NavigationMenuFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.slidding_menu_layout, null);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		SlidingMenuAdapter adapter = new SlidingMenuAdapter(getActivity());

		searchInput = (EditText) view.findViewById(R.id.search_input);
		searchInput.setOnKeyListener(new View.OnKeyListener() {

			public boolean onKey(View v, int keyCode, KeyEvent event) {

				if (event.getAction() == KeyEvent.ACTION_DOWN
						&& keyCode == KeyEvent.KEYCODE_ENTER) {

					// if the enter key was pressed, then hide the keyboard and
					// do whatever needs doing.
					InputMethodManager imm = (InputMethodManager) getActivity()
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);

					Intent intent = new Intent(getActivity(),
							SearchResultActivity.class);
					Bundle b = new Bundle();
					b.putString(Constant.SEARCH_QUERY, searchInput.getText()
							.toString());
					intent.putExtra(Constant.SEARCH_BUNDLE, b);
					
					startActivity(intent);
					searchInput.setText("");
					showHostActivityContent();
					return true;
				}

				return false;
			}
		});

		searchInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			public void onFocusChange(View v, boolean hasFocus) {

				if (!hasFocus) {
					InputMethodManager imm = (InputMethodManager) getActivity()
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
				}
			}
		});

		menuList = (ListView) view.findViewById(R.id.menu_list);
		menuList.setAdapter(adapter);

		menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {

				switch (position) {
				case Constant.HOME:
					CommonVariables.DEST_FRAGMENT = Constant.HOME;
					break;

				case Constant.BY_LOCATION:
					CommonVariables.DEST_FRAGMENT = Constant.BY_LOCATION;
					break;

				case Constant.BY_CATEGORIES:
					CommonVariables.DEST_FRAGMENT = Constant.BY_CATEGORIES;
					break;

				case Constant.TWITTER:
					CommonVariables.DEST_FRAGMENT = Constant.TWITTER;
					break;

				case Constant.MY_EVENTS:
					CommonVariables.DEST_FRAGMENT = Constant.MY_EVENTS;
					break;
					
				case Constant.ARCHIVE:
					CommonVariables.DEST_FRAGMENT = Constant.ARCHIVE;
					break;
				}

				finishHostActivity();
			}
		});
	}

	private void finishHostActivity() {
		CommonVariables.IS_GO_TO_MAIN = true;

		if (getActivity() instanceof CategoryEventListActivity) {
			CategoryEventListActivity cela = (CategoryEventListActivity) getActivity();
			cela.finishActivity();
		} else if (getActivity() instanceof RegionalEventListActivity) {
			RegionalEventListActivity rela = (RegionalEventListActivity) getActivity();
			rela.finishActivity();
		} else if (getActivity() instanceof DetailEventActivity) {
			DetailEventActivity dea = (DetailEventActivity) getActivity();
			dea.finishActivity();
		} else if (getActivity() instanceof SearchResultActivity) {
			SearchResultActivity sra = (SearchResultActivity) getActivity();
			sra.finishActivity();
		}
	}

	private void showHostActivityContent() {
		
		if (getActivity() instanceof CategoryEventListActivity) {
			CategoryEventListActivity cela = (CategoryEventListActivity) getActivity();
			cela.getSlidingMenu().showContent();
		} else if (getActivity() instanceof RegionalEventListActivity) {
			RegionalEventListActivity rela = (RegionalEventListActivity) getActivity();
			rela.getSlidingMenu().showContent();
		} else if (getActivity() instanceof DetailEventActivity) {
			DetailEventActivity dea = (DetailEventActivity) getActivity();
			dea.getSlidingMenu().showContent();
		}
	}
}
