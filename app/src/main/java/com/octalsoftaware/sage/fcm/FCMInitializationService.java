package com.octalsoftaware.sage.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by this on 2/20/2017.
 */

public class FCMInitializationService extends FirebaseInstanceIdService {
    private static final String TAG = "FCMItoken==";
    public static String mDeviceToken;

    @Override
    public void onTokenRefresh() {
        mDeviceToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("token", mDeviceToken);

    }
}