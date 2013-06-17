package com.flipbox.muhammadiyahthisweek.activity;

import com.flipbox.muhammadiyahthisweek.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SplashscreenActivity extends Activity {

	private final long sleepTime = 4000;
	private ImageView splashImg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);
		splashImg = (ImageView) findViewById(R.id.splash_img);

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

		splashImg.setImageResource(R.drawable.splashscreen_flipbox);
		// entrance
		Animation anim = AnimationUtils.loadAnimation(this,
				R.anim.alpha_entrance);
		anim.reset();
		final LinearLayout linLay = (LinearLayout) findViewById(R.id.splash_layout);
		linLay.clearAnimation();
		linLay.setAnimation(anim);

		anim.setAnimationListener(new AnimationListener() {
			
			public void onAnimationStart(Animation animation) {
				
			}
			
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			public void onAnimationEnd(Animation animation) {
				//splashImg.setImageResource(R.drawable.splashscreen_mtw);
			}
		});
	}

}
