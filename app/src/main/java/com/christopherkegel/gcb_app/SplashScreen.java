package com.christopherkegel.gcb_app;

/**
 * Created by christopherkegel on 17.06.15.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

public class SplashScreen extends Activity{
    //-----------------------------------------
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;
    // Sonstige
    private RelativeLayout rootView;
    // Animation
    Animation animFadeout;
    //-----------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        rootView = (RelativeLayout) findViewById(R.id.rtSplash);
        // load the animation
        animFadeout = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_out);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);

                // start the animation
                rootView.startAnimation(animFadeout);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }


}