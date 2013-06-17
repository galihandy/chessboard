package com.flipbox.muhammadiyahthisweek.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.flipbox.muhammadiyahthisweek.fragment.NavigationMenuFragment;
import com.flipbox.muhammadiyahthisweek.fragment.RegionalEventListFragment;
import com.flipbox.muhammadiyahthisweek.utils.CommonVariables;
import com.flipbox.muhammadiyahthisweek.utils.Constant;
import com.flipbox.muhammadiyahthisweek.R;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

public class RegionalEventListActivity extends SlidingFragmentActivity {

	private RegionalEventListFragment locationEventListFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null)
			locationEventListFragment = (RegionalEventListFragment) getSupportFragmentManager()
					.getFragment(savedInstanceState,
							"locationEventListFragment");
		if (locationEventListFragment == null) {
			Bundle b = getIntent().getBundleExtra(Constant.LOCATION_BUNDLE);

			locationEventListFragment = new RegionalEventListFragment();
			locationEventListFragment.setArguments(b);
		}

		// set content frame
		setContentView(R.layout.content_frame);
		ImageView headerLogo = (ImageView) findViewById(R.id.menu_icon);
		headerLogo.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				getSlidingMenu().showMenu();
			}
		});

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, locationEventListFragment)
				.commit();

		setBehindContentView(R.layout.menu_frame);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new NavigationMenuFragment())
				.commit();

		// decor slidding menu
		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT);
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sm.setFadeDegree(0.35f);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (CommonVariables.IS_GO_TO_MAIN)
			finishActivity();
	}

	public void finishActivity() {
		getSlidingMenu().showContent();
		finish();
	}
}
