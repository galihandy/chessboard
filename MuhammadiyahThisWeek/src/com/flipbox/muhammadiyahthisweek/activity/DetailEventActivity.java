package com.flipbox.muhammadiyahthisweek.activity;

import com.flipbox.muhammadiyahthisweek.fragment.DetailEventFragment;
import com.flipbox.muhammadiyahthisweek.fragment.NavigationMenuFragment;
import com.flipbox.muhammadiyahthisweek.utils.Constant;
import com.flipbox.muhammadiyahthisweek.R;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class DetailEventActivity extends SlidingFragmentActivity {

	private DetailEventFragment detailEventFragment;
	private Bundle bundle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null)
			detailEventFragment = (DetailEventFragment) getSupportFragmentManager()
					.getFragment(savedInstanceState, "detailEventFragment");
		if (detailEventFragment == null) {
			bundle = getIntent().getBundleExtra(Constant.EVENT_BUNDLE);

			detailEventFragment = new DetailEventFragment();
			detailEventFragment.setArguments(bundle);
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
				.replace(R.id.content_frame, detailEventFragment).commit();

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

	public void finishActivity() {

		getSlidingMenu().showContent();
		finish();
	}
}
