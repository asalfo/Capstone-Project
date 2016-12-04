package com.asalfo.wiulgi.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class Preferences {
    public static String getString(Context context, String key) {
        return getSharedPreferences(context).getString(key, null);
    }

    public static int getInt(Context context, String key) {
        return getSharedPreferences(context).getInt(key, -1);
    }

    public static boolean putString(Context context, String key, String value) {
        return getSharedPreferencesEditor(context).putString(key, value).commit();
    }

    public static boolean putInt(Context context, String key, int value) {
        return getSharedPreferencesEditor(context).putInt(key, value).commit();
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
    }

    private static Editor getSharedPreferencesEditor(Context context) {
        return getSharedPreferences(context).edit();
    }
}
