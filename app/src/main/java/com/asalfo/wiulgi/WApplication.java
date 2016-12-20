package com.asalfo.wiulgi;


import android.app.Application;
import android.content.Context;

import com.asalfo.wiulgi.auth.ProfileManager;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.tagmanager.ContainerHolder;
import com.google.android.gms.tagmanager.TagManager;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class WApplication extends Application {
    private static Context sContext;


    public Tracker mTracker;

    public ContainerHolder mContainerHolder;
    public TagManager mTagManager;



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


    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }
}
