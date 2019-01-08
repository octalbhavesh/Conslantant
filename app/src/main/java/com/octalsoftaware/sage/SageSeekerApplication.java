package com.octalsoftaware.sage;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Location;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.DisplayMetrics;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.octalsoftaware.sage.constants.S;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by anandj on 3/16/2018.
 */

public class SageSeekerApplication extends MultiDexApplication {
    private Retrofit retrofit;
    private static SageSeekerApplication mChiriApplication;
    public static Location location;
    //public static GoogleApiClient mGoogleApiClient;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mChiriApplication = this;

    }

    /**
     * @return VivaLingApplication Single Instance
     */
    public static SageSeekerApplication getInstance() {
        return mChiriApplication;
    }

    /**
     * @return The Volley Request queue, the queue will be created if it is null
     */
    public Retrofit getRequestQueue() {
        // initialize the request queue, the queue instance will be created when it is accessed for the first time
        if (retrofit == null) {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(60, TimeUnit.SECONDS);
            builder.readTimeout(60, TimeUnit.SECONDS);
            builder.writeTimeout(60, TimeUnit.SECONDS);
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();

            // Can be Level.BASIC, Level.HEADERS, or Level.BODY
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            // add logging as last interceptor
            builder.addInterceptor(httpLoggingInterceptor);
            //builder.networkInterceptors().add(httpLoggingInterceptor);
            OkHttpClient okHttpClient = builder.build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(S.BASE_URL_LIVE)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    public static void changeLanguage(Context mContext, int id) {
        Locale locale = null;
        String language_code = "en";
        String lang = Locale.getDefault().getLanguage();
        //   Log.d("language", Locale.getAvailableLocales().toString() + "");
        if (id == 0) {
            S.SELECT_LANGUAGE = "ar";
            language_code = "ar";
        } else if (id == 1) {
            S.SELECT_LANGUAGE = "en";
            language_code = "en";
        } else {
            if (lang.equalsIgnoreCase("ar") || lang.equalsIgnoreCase("ar")) {
                S.SELECT_LANGUAGE = "ar";
                language_code = lang;
            } else {
                S.SELECT_LANGUAGE = "en";
                language_code = "en";
            }
        }

        //  Locale.setDefault(locale);
        setLocale(mContext, language_code);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Util.setLanguage(getApplicationContext());
    }

    public static void setLocale(Context context, String lang) {

        Locale myLocale = new Locale(lang);
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        // conf.locale = myLocale;
        conf.setLocale(new Locale(lang));


        res.updateConfiguration(conf, dm);
        // reloadUI(); // you may not need this, in my activity ui must be refreshed immediately so it has a function like this.
    }
}
