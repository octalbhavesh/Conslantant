package com.octalsoftaware.sage.view.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.octalsoftaware.sage.SageSeekerApplication;
import com.octalsoftaware.sage.network.APIRequest;
import com.octalsoftaware.sage.network.MyApiEndpointInterface;
import com.octalsoftaware.sage.util.S;
import com.octalsoftaware.sage.util.SagePreference;
import com.octalsoftaware.sage.view.fragment.BrowseCategoriesFragment;
import com.octalsoftaware.sage.view.fragment.ContactFragment;
import com.octalsoftaware.sage.view.fragment.RatingFragment;
import com.octalsoftaware.sage.view.fragment.WalletFragment;
import com.sage.conslantant.android.R;
import com.sage.conslantant.android.databinding.ActivityHomeActiviyBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActiviy extends BaseActivity implements View.OnClickListener {
    private DrawerLayout drawer;
    private android.app.FragmentManager mFragmentManager = null;
    //private SupportF mFragmentManager = null;
    private android.app.FragmentTransaction fragmentTransaction = null;
    public static Context mContext;
    android.app.Fragment mFragment = null;
    private String TAG = HomeActiviy.class.getSimpleName();
    private ActivityHomeActiviyBinding mBinding;
    private Toolbar toolbar;
    private FrameLayout frameLayout;
    private View container;
    private LinearLayout layoutHeader;
    private String statusLogin;
    private RelativeLayout navCategories, navMessage, navHistory, navWallet, navRating, navHelp, navContact, navAbout, navLogout;
    private ImageView ivCategories, ivMessage, ivHistory, ivWallet, ivRating, ivHelp, ivContact, ivAbout, ivLogout, ivHeader;
    private TextView tvCategories, tvMessage, tvHistory, tvWallet, tvRating, tvHelp, tvContact, tvAbout, tvLogout, tvHeader;
    private View v1, v2, v3, v4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_activiy);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_home_activiy);
        mContext = this;
        statusLogin = SagePreference.getUnit(S.STATUS_LOGIN, mContext);
        try {
            findViewByID();
        } catch (Exception e) {
            e.printStackTrace();
        }
        displayView(S.BROWSE_CATEGORIES);
        selectedColor("categories");
       /* if (getIntent().getStringExtra("fragment") != null && getIntent().getStringExtra("fragment").equalsIgnoreCase("alert"))
            displayView(S.ALERT);
        else
            displayView(S.HOMEFRAGMENT);*/


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void findViewByID() {
        mFragmentManager = getFragmentManager();
        toolbar = findViewById(R.id.toolbar);
        frameLayout = findViewById(R.id.mainFrame);
        // setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);

        container = findViewById(R.id.container);

      /*  getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setElevation(0);*/

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerClosed(View view) {

                //   supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
             /*   hideKeyboard();
                supportInvalidateOptionsMenu();*/
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

              /*  if (TripStatusPreferences.getUnit(S.LANGUAGE, mContext).equalsIgnoreCase("ar")) {
                    toolbar.setTranslationX(-(slideOffset * drawerView.getWidth()));
                    frameLayout.setTranslationX(-(slideOffset * drawerView.getWidth()));
                } else if (TripStatusPreferences.getUnit(S.LANGUAGE, mContext).equalsIgnoreCase("en")) {
                    toolbar.setTranslationX((slideOffset * drawerView.getWidth()));
                    frameLayout.setTranslationX((slideOffset * drawerView.getWidth()));
                }*/
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        try {
            View headerView = navigationView.inflateHeaderView(R.layout.nav_header_home_activiy);
            navCategories = (RelativeLayout) headerView.findViewById(R.id.nav_categories);
            ivHeader = (ImageView) headerView.findViewById(R.id.iv_nav_header_image);
            v1 = (View) headerView.findViewById(R.id.view1);
            v2 = (View) headerView.findViewById(R.id.view2);
            v3 = (View) headerView.findViewById(R.id.view3);
            v4 = (View) headerView.findViewById(R.id.view4);
            tvHeader = (TextView) headerView.findViewById(R.id.tv_nav_header_name);
            layoutHeader = (LinearLayout) headerView.findViewById(R.id.layout_header);
            navMessage = (RelativeLayout) headerView.findViewById(R.id.nav_message);
            navHistory = (RelativeLayout) headerView.findViewById(R.id.nav_history);
            navRating = (RelativeLayout) headerView.findViewById(R.id.nav_rating);
            navWallet = (RelativeLayout) headerView.findViewById(R.id.nav_wallet);
            navHelp = (RelativeLayout) headerView.findViewById(R.id.nav_help);
            navContact = (RelativeLayout) headerView.findViewById(R.id.nav_contact);
            navAbout = (RelativeLayout) headerView.findViewById(R.id.nav_about);
            navLogout = (RelativeLayout) headerView.findViewById(R.id.nav_logout);

            tvCategories = (TextView) headerView.findViewById(R.id.tv_categories);
            tvMessage = (TextView) headerView.findViewById(R.id.tv_message);
            tvHistory = (TextView) headerView.findViewById(R.id.tv_history);
            tvRating = (TextView) headerView.findViewById(R.id.tv_rating);
            tvWallet = (TextView) headerView.findViewById(R.id.tv_wallet);
            tvHelp = (TextView) headerView.findViewById(R.id.tv_help);
            tvContact = (TextView) headerView.findViewById(R.id.tv_contact);
            tvAbout = (TextView) headerView.findViewById(R.id.tv_about);
            tvLogout = (TextView) headerView.findViewById(R.id.tv_logout);

            ivCategories = (ImageView) headerView.findViewById(R.id.img_categories);
            ivMessage = (ImageView) headerView.findViewById(R.id.img_message);
            ivHistory = (ImageView) headerView.findViewById(R.id.img_history);
            ivRating = (ImageView) headerView.findViewById(R.id.img_rating);
            ivWallet = (ImageView) headerView.findViewById(R.id.img_wallet);
            ivHelp = (ImageView) headerView.findViewById(R.id.img_help);
            ivContact = (ImageView) headerView.findViewById(R.id.img_contact);
            ivAbout = (ImageView) headerView.findViewById(R.id.img_about);
            ivLogout = (ImageView) headerView.findViewById(R.id.img_logout);

        } catch (Exception e) {

        }

        if (statusLogin.equalsIgnoreCase("true")) {
            layoutHeader.setVisibility(View.VISIBLE);
            navCategories.setVisibility(View.VISIBLE);
            navHistory.setVisibility(View.VISIBLE);
            navRating.setVisibility(View.VISIBLE);
            navWallet.setVisibility(View.VISIBLE);
            navHelp.setVisibility(View.VISIBLE);
            navContact.setVisibility(View.VISIBLE);
            navAbout.setVisibility(View.VISIBLE);
            navLogout.setVisibility(View.VISIBLE);
            navMessage.setVisibility(View.VISIBLE);
            v1.setVisibility(View.VISIBLE);
            v2.setVisibility(View.VISIBLE);
            v3.setVisibility(View.VISIBLE);
            v4.setVisibility(View.VISIBLE);
            tvLogout.setText(getResources().getString(R.string.logout));
            ivLogout.setImageResource(R.drawable.logout);
            tvHeader.setText(SagePreference.getUnit(S.FIRST_NAME, mContext) + " " + SagePreference.getUnit(S.LAST_NAME, mContext));
            Picasso.get()
                    .load(SagePreference.getUnit(S.PROFILE_URL, mContext))
                    .placeholder(R.drawable.ic_user)
                    .into(ivHeader);
        } else {
            v1.setVisibility(View.GONE);
            v2.setVisibility(View.GONE);
            v3.setVisibility(View.GONE);
            v4.setVisibility(View.GONE);
            tvLogout.setText(getResources().getString(R.string.loginRegister));
            layoutHeader.setVisibility(View.GONE);
            navHistory.setVisibility(View.GONE);
            navRating.setVisibility(View.GONE);
            navWallet.setVisibility(View.GONE);
            navMessage.setVisibility(View.GONE);
        }
        navCategories.setOnClickListener(this);
        navHistory.setOnClickListener(this);
        navRating.setOnClickListener(this);
        navWallet.setOnClickListener(this);
        navHelp.setOnClickListener(this);
        navContact.setOnClickListener(this);
        navAbout.setOnClickListener(this);
        navLogout.setOnClickListener(this);
        navMessage.setOnClickListener(this);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void drawerClose() {
        drawer.closeDrawer(GravityCompat.START);
    }

    public void displayView(String fragmentName) {
        fragmentTransaction = mFragmentManager.beginTransaction();
        switch (fragmentName) {
            case S.LAYOUT_HEADER:
                mFragment = new BrowseCategoriesFragment();
                break;
            case S.BROWSE_CATEGORIES:
                mFragment = new BrowseCategoriesFragment();
                break;
            case S.MY_WALLET:
                mFragment = new WalletFragment();
                break;
            case S.RATING:
                mFragment = new RatingFragment();
                break;
            case S.MESSAGE:
                mFragment = new NewChatFragment();
                break;
            case S.CONTACT_US:
                mFragment = new ContactFragment();
                break;

        }
        if (mFragment != null) {
            try {
                fragmentTransaction.replace(R.id.mainFrame, mFragment).addToBackStack(fragmentName);
                fragmentTransaction.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_header:
                selectedColor("");
                displayView(S.LAYOUT_HEADER);
                drawerClose();
                break;
            case R.id.nav_categories:
                selectedColor("categories");
                displayView(S.BROWSE_CATEGORIES);
                drawerClose();
                break;
            case R.id.nav_history:
                selectedColor("history");
                //displayView(S.PLANS);
                drawerClose();
                break;
            case R.id.nav_rating:
                selectedColor("rating");
                displayView(S.RATING);
                drawerClose();
                break;
            case R.id.nav_wallet:
                selectedColor("wallet");
                displayView(S.MY_WALLET);
                //displayView(S.PLANS);
                drawerClose();
                break;
            case R.id.nav_help:
                selectedColor("help");
                //displayView(S.PLANS);
                drawerClose();
                break;
            case R.id.nav_contact:
                selectedColor("contact");
                displayView(S.CONTACT_US);
                drawerClose();
                break;
            case R.id.nav_about:
                selectedColor("about");
                //displayView(S.PLANS);
                drawerClose();
                break;
            case R.id.nav_message:
                selectedColor("message");
                //displayView(S.PLANS);
                drawerClose();
                break;
            case R.id.nav_logout:

                if (tvLogout.getText().toString().equalsIgnoreCase("Logout")) {
                    final Dialog dialog;
                    dialog = new Dialog(HomeActiviy.this);
                    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_logout);
                    dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    Window window = dialog.getWindow();
                    window.setGravity(Gravity.CENTER);
                    dialog.show();
                    TextView dialog_yes = dialog.findViewById(R.id.dialog_yes);
                    TextView dialog_no = dialog.findViewById(R.id.dialog_no);
                    TextView dialog_twoaction_descss = dialog.findViewById(R.id.dialog_twoaction_descss);
                    dialog_twoaction_descss.setText(getResources().getString(R.string.logout_message));

                    dialog_yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                callLogout(APIRequest.logout(SagePreference.getUnit(S.USER_ID, mContext)));
                            } catch (Exception e) {
                                Log.e(TAG, e.toString());
                            }

                            dialog.dismiss();
                        }
                    });

                    dialog_no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                } else {
                    startActivity(new Intent(HomeActiviy.this, IntroActivity.class));
                }

                break;


        }
    }

    private void callLogout(Map<String, Object> logout) {
        showProgress(mContext);
        MyApiEndpointInterface myApiEndpointInterface = SageSeekerApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
        Call<ResponseBody> call = null;
        call = myApiEndpointInterface.logout(logout);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    hideProgress();
                    String response1 = response.body().string();
                    JSONObject jsonObject = new JSONObject(response1);
                    if (jsonObject.optInt("status_code") == 1) {
                        sucessToast(mContext, jsonObject.optString("message"));
                        SagePreference.setUnit(S.STATUS_LOGIN, "false", mContext);
                        startActivity(new Intent(HomeActiviy.this, LoginActivity.class));
                        finish();
                    } else {
                        hideProgress();
                        errorToast(mContext, jsonObject.optString("message"));
                    }
                } catch (Exception e) {
                    hideProgress();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideProgress();
            }
        });
    }

    private void selectedColor(String s1) {
        if (s1.equalsIgnoreCase("categories")) {
            tvCategories.setTextColor(getResources().getColor(R.color.theme_dark));
            ivCategories.setColorFilter(getResources().getColor(R.color.theme_dark));
            tvHistory.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivHistory.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvRating.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivRating.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvWallet.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivWallet.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvHelp.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivHelp.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvContact.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivContact.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvAbout.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivAbout.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvLogout.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivLogout.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvMessage.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivMessage.setColorFilter(getResources().getColor(R.color.colorNavOption));
        } else if (s1.equalsIgnoreCase("history")) {
            tvCategories.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivCategories.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvHistory.setTextColor(getResources().getColor(R.color.theme_dark));
            ivHistory.setColorFilter(getResources().getColor(R.color.theme_dark));
            tvRating.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivRating.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvWallet.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivWallet.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvHelp.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivHelp.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvContact.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivContact.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvAbout.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivAbout.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvLogout.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivLogout.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvMessage.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivMessage.setColorFilter(getResources().getColor(R.color.colorNavOption));
        } else if (s1.equalsIgnoreCase("rating")) {
            tvCategories.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivCategories.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvHistory.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivHistory.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvRating.setTextColor(getResources().getColor(R.color.theme_dark));
            ivRating.setColorFilter(getResources().getColor(R.color.theme_dark));
            tvWallet.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivWallet.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvHelp.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivHelp.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvContact.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivContact.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvAbout.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivAbout.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvLogout.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivLogout.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvMessage.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivMessage.setColorFilter(getResources().getColor(R.color.colorNavOption));
        } else if (s1.equalsIgnoreCase("wallet")) {
            tvCategories.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivCategories.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvHistory.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivHistory.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvRating.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivRating.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvWallet.setTextColor(getResources().getColor(R.color.theme_dark));
            ivWallet.setColorFilter(getResources().getColor(R.color.theme_dark));
            tvHelp.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivHelp.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvContact.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivContact.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvAbout.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivAbout.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvLogout.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivLogout.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvMessage.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivMessage.setColorFilter(getResources().getColor(R.color.colorNavOption));
        } else if (s1.equalsIgnoreCase("help")) {
            tvCategories.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivCategories.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvHistory.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivHistory.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvRating.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivRating.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvWallet.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivWallet.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvHelp.setTextColor(getResources().getColor(R.color.theme_dark));
            ivHelp.setColorFilter(getResources().getColor(R.color.theme_dark));
            tvContact.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivContact.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvAbout.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivAbout.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvLogout.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivLogout.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvMessage.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivMessage.setColorFilter(getResources().getColor(R.color.colorNavOption));
        } else if (s1.equalsIgnoreCase("contact")) {
            tvCategories.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivCategories.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvHistory.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivHistory.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvRating.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivRating.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvWallet.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivWallet.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvHelp.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivHelp.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvContact.setTextColor(getResources().getColor(R.color.theme_dark));
            ivContact.setColorFilter(getResources().getColor(R.color.theme_dark));
            tvAbout.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivAbout.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvLogout.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivLogout.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvMessage.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivMessage.setColorFilter(getResources().getColor(R.color.colorNavOption));
        } else if (s1.equalsIgnoreCase("about")) {
            tvCategories.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivCategories.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvHistory.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivHistory.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvRating.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivRating.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvWallet.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivWallet.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvHelp.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivHelp.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvContact.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivContact.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvAbout.setTextColor(getResources().getColor(R.color.theme_dark));
            ivAbout.setColorFilter(getResources().getColor(R.color.theme_dark));
            tvLogout.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivLogout.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvMessage.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivMessage.setColorFilter(getResources().getColor(R.color.colorNavOption));
        } else if (s1.equalsIgnoreCase("message")) {
            tvMessage.setTextColor(getResources().getColor(R.color.theme_yellow));
            ivMessage.setColorFilter(getResources().getColor(R.color.theme_yellow));
            tvCategories.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivCategories.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvHistory.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivHistory.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvRating.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivRating.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvWallet.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivWallet.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvHelp.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivHelp.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvContact.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivContact.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvAbout.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivAbout.setColorFilter(getResources().getColor(R.color.colorNavOption));
            tvLogout.setTextColor(getResources().getColor(R.color.colorNavOption));
            ivLogout.setColorFilter(getResources().getColor(R.color.colorNavOption));
        }

    }

    public void openDrawer() {
        if (!drawer.isDrawerVisible(GravityCompat.END)) {
            drawer.openDrawer(GravityCompat.START);
        }
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        customeFont(mContext, tvCategories);
        customeFont(mContext, tvHistory);
        customeFont(mContext, tvRating);
        customeFont(mContext, tvWallet);
        customeFont(mContext, tvContact);
        customeFont(mContext, tvAbout);
        customeFont(mContext, tvHelp);
        customeFont(mContext, tvLogout);
        customeFont(mContext, tvMessage);
    }
}
