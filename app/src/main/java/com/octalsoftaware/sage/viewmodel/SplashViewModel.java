package com.octalsoftaware.sage.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.os.Handler;

import com.octalsoftaware.sage.view.activity.IntroActivity;

public class SplashViewModel extends BaseObservable {

    private static int SPLASH_TIME_OUT = 3000;
    private Context mContext;

    public SplashViewModel(Context mContext) {
        mContext = mContext;
        goToIntro();
    }

    private void goToIntro() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(mContext, IntroActivity.class);
                mContext.startActivity(i);
            }
        }, SPLASH_TIME_OUT);
    }
}
