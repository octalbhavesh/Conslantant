package com.octalsoftaware.sage.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SagePreference {

    final static String CHIRI_SHARED_PREFERENCE = "sage";

    public static void setUnit(String key, String unit, Context context) {
        SharedPreferences sp = context.getSharedPreferences(CHIRI_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        sp.edit().putString(key, unit).apply();
    }

    public static String getUnit(String key, Context context) {
        SharedPreferences sp = context.getSharedPreferences(CHIRI_SHARED_PREFERENCE, Activity.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    public static void ClearAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(CHIRI_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        sp.edit().clear().apply();
    }
}
