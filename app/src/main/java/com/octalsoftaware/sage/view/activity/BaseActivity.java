package com.octalsoftaware.sage.view.activity;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.octalsoftaware.sage.sinch.SinchService;
import com.sage.conslantant.android.R;
import com.sage.conslantant.android.databinding.ActivityBaseBinding;
import com.valdesekamdem.library.mdtoast.MDToast;

public class BaseActivity extends AppCompatActivity implements ServiceConnection {

    private ActivityBaseBinding mBinding;
    private View mView;
    private ImageView ivBack;
    public Context mContext;
    private Dialog dialog;
    private SinchService.SinchServiceInterface mSinchServiceInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApplicationContext().bindService(new Intent(this, SinchService.class), this,
                BIND_AUTO_CREATE);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_base);
        mContext = this;
    }

    public void clickAnimation(TextView textView) {
        AlphaAnimation animation1 = new AlphaAnimation(0.2f, 0.3f);
        animation1.setDuration(200);
        animation1.setStartOffset(1000);
        textView.startAnimation(animation1);
    }

    public boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
        }
        return false;
    }

    public void sucessToast(Context context, String message) {
        MDToast mdToast = MDToast.makeText(context, message, MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS);
        mdToast.show();
    }

    public void errorToast(Context context, String message) {
        MDToast mdToast = MDToast.makeText(context, message, MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
        mdToast.show();
    }

    public void warningToast(Context context, String message) {
        MDToast mdToast = MDToast.makeText(context, message, MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING);
        mdToast.show();
    }

    public boolean checkValidation(EditText[] etTxt) {
        boolean status = false;
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        for (int i = 0; i < etTxt.length; i++) {
            EditText et = etTxt[i];
            // String ss = String.valueOf(et.getHint().toString());
            if (et.getText().toString().trim().equalsIgnoreCase("")) {
                et.setError("Enter " + et.getHint() + "");
                et.setFocusable(true);
                status = false;
            } else {
                // email vaidation
                if (et.getHint().toString().trim().equalsIgnoreCase("email") ||
                        et.getHint().toString().trim().equalsIgnoreCase("email address")) {
                    if (!et.getText().toString().trim().matches(emailPattern)) {
                        et.setError("Enter " + "valid email" + "");
                        et.setFocusable(true);
                        status = false;
                    }
                }
                // phone number validation
                else if (et.getHint().toString().trim().equalsIgnoreCase("mobile number")
                        || et.getHint().toString().trim().equalsIgnoreCase("phone number")
                        || et.getHint().toString().trim().equalsIgnoreCase("phone")
                        || et.getHint().toString().trim().equalsIgnoreCase("mobile")) {
                    if (et.getText().toString().trim().length() < 6) {
                        et.setError("Enter " + "valid phone number" + "");
                        et.setFocusable(true);
                        status = false;
                    } else {
                        status = true;
                    }
                } else {
                    status = true;
                }
            }
        }
        return status;
    }

    public void showProgress(Context mContext) {
        dialog = new Dialog(mContext, android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custome_progress_dialog);
        dialog.setCancelable(false);
        dialog.show();
    }

    public void hideProgress() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public void customeFont(Context context, TextView tv) {
        Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "FaktPro-Normal.ttf");
        tv.setTypeface(custom_font);
    }


    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        if (SinchService.class.getName().equals(componentName.getClassName())) {
            mSinchServiceInterface = (SinchService.SinchServiceInterface) iBinder;
            onServiceConnected();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        if (SinchService.class.getName().equals(componentName.getClassName())) {
            mSinchServiceInterface = null;
            onServiceDisconnected();
        }
    }

    protected void onServiceConnected() {
        // for subclasses
    }

    protected void onServiceDisconnected() {
        // for subclasses
    }
    protected SinchService.SinchServiceInterface getSinchServiceInterface() {
        return mSinchServiceInterface;
    }
}
