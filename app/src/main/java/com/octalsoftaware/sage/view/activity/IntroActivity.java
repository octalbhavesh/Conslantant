package com.octalsoftaware.sage.view.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.octalsoftaware.sage.util.Utils;
import com.sage.conslantant.android.R;
import com.sage.conslantant.android.databinding.ActivityIntroBinding;

public class IntroActivity extends BaseActivity implements View.OnClickListener {

    private ActivityIntroBinding mBinding;

    private TextView[] dots;
    private int[] layouts;
    private MyViewPagerAdapter myViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_intro);
        tempMethod();
    }

    @Override
    protected void onStart() {
        super.onStart();
        layouts = new int[]{
                R.layout.into_one,
                R.layout.intro_two,
                R.layout.intro_three,
        };
        addBottomDots(0);
        changeStatusBarColor();
    }

    @Override
    protected void onResume() {
        super.onResume();
        myViewPagerAdapter = new MyViewPagerAdapter();
        mBinding.viewPager.setAdapter(myViewPagerAdapter);
        mBinding.viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

    }

    private void tempMethod() {


       /* btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });*/

       /* btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                    launchHomeScreen();
                }
            }
        });*/
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        mBinding.layoutDots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            mBinding.layoutDots.addView(dots[i]);
        }
        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return mBinding.viewPager.getCurrentItem() + i;
    }

   /* private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(IntroActivity.this, MainActivity.class));
        finish();
    }*/

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
             /*   btnNext.setText(getString(R.string.start));
                btnSkip.setVisibility(View.GONE);*/
            } else {
                // still pages are left
             /*   btnNext.setText(getString(R.string.next));
                btnSkip.setVisibility(View.VISIBLE);*/
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_main_login:
                Utils.clickAnimation(mBinding.tvMainLogin);
                startActivity(new Intent(IntroActivity.this, LoginActivity.class));
                break;
            case R.id.tv_main_register:
                Utils.clickAnimation(mBinding.tvMainRegister);
                startActivity(new Intent(IntroActivity.this, RegisterOneActivity.class));
                break;
            case R.id.tv_main_skip:
                Utils.clickAnimation(mBinding.tvMainSkip);
                startActivity(new Intent(IntroActivity.this, HomeActiviy.class));
                break;


        }

    }

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }


}
