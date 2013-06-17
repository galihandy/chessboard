package com.flipbox.muhammadiyahthisweek.fragment;

import com.flipbox.muhammadiyahthisweek.activity.MainActivity;
import com.flipbox.muhammadiyahthisweek.activity.SearchResultActivity;
import com.flipbox.muhammadiyahthisweek.adapter.SlidingMenuAdapter;
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

public class MainMenuFragment extends Fragment {

	private int currentFragment;
	// private int destFragment;
	private ListView menuList;
	private View view;
	private EditText searchInput;

	private FeaturedFragment featuredFragment;
	private RegionalFragment locationFragment;
	private CategoryFragment categoryFragment;
	private SavedEventFragment savedEventFragment;
	private ArchiveFragment archiveFragment;
	private TwitterFragment twitterFragment;

	public MainMenuFragment() {
		currentFragment = Constant.HOME;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.out.println("MAIN MENU FRAGMENT on create view");
		view = inflater.inflate(R.layout.slidding_menu_layout, null);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		System.out.println("MAIN MENU FRAGMENT on activity created");

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
					((MainActivity) getActivity()).getSlidingMenu()
							.showContent();
					
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

				updateCurrentFragment(position);
			}
		});
	}

	private void switchFragment(Fragment newFragment) {
		if (getActivity() == null)
			return;

		if (getActivity() instanceof MainActivity) {

			// addCurrentFragmentToBackStack();
			MainActivity ma = (MainActivity) getActivity();

			ma.switchContent(newFragment);
		}
	}

	private void updateHeaderSecondText(boolean isChangeToTwitter) {
		if (getActivity() == null)
			return;

		if (getActivity() instanceof MainActivity) {
			MainActivity ma = (MainActivity) getActivity();
			if (isChangeToTwitter) {
				String s = ma.getResources().getString(R.string.on_twitter);
				ma.getHeaderSecondTv().setText(s);
			} else {
				String s = ma.getResources().getString(R.string.this_week);
				ma.getHeaderSecondTv().setText(s);
			}
		}
	}

	/*
	 * private void addCurrentFragmentToBackStack() { switch (currentFragment) {
	 * case Constant.HOME: featuredFragment.addToBackstack();
	 * System.out.println("Menu Fragment: add FEATURED to backstack"); break;
	 * 
	 * case Constant.BY_LOCATION: // f = new break;
	 * 
	 * case Constant.BY_CATEGORIES: categoryFragment.addToBackstack();
	 * System.out.println("Menu Fragment: add CATEGORY to backstack"); break;
	 * 
	 * case Constant.TWITTER: twitterFragment.addToBackstack();
	 * System.out.println("Menu Fragment: add TWITTER to backstack"); break;
	 * 
	 * case Constant.MY_EVENTS: savedEventFragment.addToBackstack();
	 * System.out.println("Menu Fragment: add SAVED to backstack"); break; }
	 * 
	 * currentFragment = destFragment; }
	 */

	public void updateCurrentFragment(int destFragment) {

		if (destFragment == currentFragment) {
			MainActivity ma = (MainActivity) getActivity();
			ma.getSlidingMenu().showContent();
			return;
		}

		Fragment f = null;
		boolean isChangeToTwitter = false;
		switch (destFragment) {
		case Constant.HOME:

			if (featuredFragment == null) {
				System.out.println("create featured fragment");
				featuredFragment = new FeaturedFragment();
			}

			f = featuredFragment;

			break;
		case Constant.BY_LOCATION:

			if (locationFragment == null) {
				System.out.println("create category fragment");
				locationFragment = new RegionalFragment();
			}

			f = locationFragment;

			break;
		case Constant.BY_CATEGORIES:

			if (categoryFragment == null) {
				System.out.println("create category fragment");
				categoryFragment = new CategoryFragment();
			}

			f = categoryFragment;

			break;
		case Constant.TWITTER:
			isChangeToTwitter = true;

			if (twitterFragment == null)
				System.out.println("create twitter fragment");
			{
				twitterFragment = new TwitterFragment();
			}

			f = twitterFragment;

			break;
		case Constant.MY_EVENTS:

			if (savedEventFragment == null) {
				System.out.println("create saved fragment");
				savedEventFragment = new SavedEventFragment();
			}

			f = savedEventFragment;

			break;
			
		case Constant.ARCHIVE:

			if (archiveFragment == null) {
				System.out.println("create archive fragment");
				archiveFragment = new ArchiveFragment();
			}

			f = archiveFragment;

			break;
		}

		if (f != null) {
			switchFragment(f);
			updateHeaderSecondText(isChangeToTwitter);
			currentFragment = destFragment;
		}
	}

	public void setFeaturedFragment(FeaturedFragment featuredFragment) {
		this.featuredFragment = featuredFragment;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		System.out.println("MAIN MENU FRAGMENT pause");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

		System.out.println("MAIN MENU FRAGMENT stop");
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		System.out.println("MAIN MENU FRAGMENT destroy view");
	}

}
