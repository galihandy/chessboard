package com.flipbox.myevent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

public class SplashscreenActivity extends Activity {

	private final long sleepTime = 3000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);

		startAnimation();
		new SplashThread().start();

	}

	private class SplashThread extends Thread {

		@Override
		public void run() {
			super.run();

			try {
				sleep(sleepTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				Intent in = new Intent(SplashscreenActivity.this,
						MainActivity.class);
				startActivity(in);
				finish();
			}

		}
	}

	private void startAnimation() {

		// entrance
		Animation anim = AnimationUtils.loadAnimation(this,
				R.anim.alpha_entrance);
		anim.reset();
		LinearLayout linLay = (LinearLayout) findViewById(R.id.splash_layout);
		linLay.clearAnimation();
		linLay.setAnimation(anim);
	}

}
