package com.asalfo.wiulgi.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


public class Preferences {
    @Nullable
    public static String getString(@NonNull Context context, String key) {
        return getSharedPreferences(context).getString(key, null);
    }

    public static int getInt(@NonNull Context context, String key) {
        return getSharedPreferences(context).getInt(key, -1);
    }

    public static boolean putString(@NonNull Context context, String key, String value) {
        return getSharedPreferencesEditor(context).putString(key, value).commit();
    }

    public static boolean putInt(@NonNull Context context, String key, int value) {
        return getSharedPreferencesEditor(context).putInt(key, value).commit();
    }

    private static SharedPreferences getSharedPreferences(@NonNull Context context) {
        return context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
    }

    private static Editor getSharedPreferencesEditor(@NonNull Context context) {
        return getSharedPreferences(context).edit();
    }
}
