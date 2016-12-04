package com.asalfo.wiulgi;


import android.app.Application;
import android.content.Context;

import com.asalfo.wiulgi.auth.ProfileManager;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class WApplication extends Application {
    private static Context sContext;

    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        ProfileManager.getInstance().init(getApplicationContext());

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    public static Context getContext() {
        return sContext;
    }

}
