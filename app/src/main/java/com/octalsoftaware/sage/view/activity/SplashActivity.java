package com.octalsoftaware.sage.view.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;

import com.octalsoftaware.sage.util.S;
import com.octalsoftaware.sage.util.SagePreference;
import com.sage.conslantant.android.R;
import com.sage.conslantant.android.databinding.ActivitySplashBinding;

public class SplashActivity extends BaseActivity {
    private ActivitySplashBinding mBinding;
    private Context mContext;
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
    }

    @Override
    protected void onResume() {
        super.onResume();
        goToIntro();
    }

    private void goToIntro() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String statusLogin = SagePreference.getUnit(S.STATUS_LOGIN, mContext);
                if (statusLogin.equalsIgnoreCase("true")) {
                    Intent i = new Intent(mContext, HomeActiviy.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent(mContext, IntroActivity.class);
                    startActivity(i);
                }

            }
        }, SPLASH_TIME_OUT);
    }
}
