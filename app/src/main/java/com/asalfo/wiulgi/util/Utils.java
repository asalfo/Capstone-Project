/*
 * Copyright 2015 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.asalfo.wiulgi.util;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;

import com.asalfo.wiulgi.R;
import com.asalfo.wiulgi.data.model.Item;
import com.asalfo.wiulgi.data.provider.WiulgiContract;
import com.asalfo.wiulgi.sync.WiulgiSyncAdapter;
import com.google.android.gms.maps.model.LatLng;


import java.text.NumberFormat;
import java.util.ArrayList;
import com.asalfo.wiulgi.data.provider.WiulgiContract.ItemsColumns;
import com.google.maps.android.SphericalUtil;


public class Utils {


    private static final String TAG = Utils.class.getSimpleName();

    private static final String PREFERENCES_LAT = "lat";
    private static final String PREFERENCES_LNG = "lng";
    private static final String PREFERENCES_GEOFENCE_ENABLED = "geofence";
    private static final String DISTANCE_KM_POSTFIX = "km";
    private static final String DISTANCE_M_POSTFIX = "m";
    private static final String STARTED = "started";

    /**
     * Check if the app has access to fine location permission. On pre-M
     * devices this will always return true.
     */
    public static boolean checkFineLocationPermission(Context context) {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    /**
     * Calculate distance between two LatLng points and format it nicely for
     * display. As this is a sample, it only statically supports metric units.
     * A production app should check locale and support the correct units.
     */
    public static String formatDistanceBetween(LatLng point1, LatLng point2) {
        if (point1 == null || point2 == null) {
            return null;
        }

        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        double distance = Math.round(SphericalUtil.computeDistanceBetween(point1, point2));

        // Adjust to KM if M goes over 1000 (see javadoc of method for note
        // on only supporting metric)
        if (distance >= 1000) {
            numberFormat.setMaximumFractionDigits(1);
            return numberFormat.format(distance / 1000) + DISTANCE_KM_POSTFIX;
        }
        return numberFormat.format(distance) + DISTANCE_M_POSTFIX;
    }

    /**
     * Store the location in the app preferences.
     */
    public static void storeLocation(Context context, LatLng location) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(PREFERENCES_LAT, Double.doubleToRawLongBits(location.latitude));
        editor.putLong(PREFERENCES_LNG, Double.doubleToRawLongBits(location.longitude));
        editor.apply();
    }

    /**
     * Fetch the location from app preferences.
     */
    public static LatLng getLocation(Context context) {
        if (!checkFineLocationPermission(context)) {
            return null;
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Long lat = prefs.getLong(PREFERENCES_LAT, Long.MAX_VALUE);
        Long lng = prefs.getLong(PREFERENCES_LNG, Long.MAX_VALUE);
        if (lat != Long.MAX_VALUE && lng != Long.MAX_VALUE) {
            Double latDbl = Double.longBitsToDouble(lat);
            Double lngDbl = Double.longBitsToDouble(lng);
            return new LatLng(latDbl, lngDbl);
        }
        return null;
    }


    /**
     * Store if geofencing triggers will show a notification in app preferences.
     */
    public static void storeGeofenceEnabled(Context context, boolean enable) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(PREFERENCES_GEOFENCE_ENABLED, enable);
        editor.apply();
    }

    /**
     * Retrieve if geofencing triggers should show a notification from app preferences.
     */
    public static boolean getGeofenceEnabled(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(PREFERENCES_GEOFENCE_ENABLED, true);
    }




    public static ArrayList itemListToContentVals(ArrayList<ContentProviderOperation> batchOperations,ArrayList<Item> items){

            if (items.size() > 0){
                for (Item item : items){
                    batchOperations.add(buildBatchOperation(item));
                }
            }
        return batchOperations;
    }

    private static ContentProviderOperation buildBatchOperation(Item item){

        Uri dirUri = WiulgiContract.Items.buildDirUri();
        ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(
                dirUri);

             LatLng cord = item.getLocation();

            builder.withValue(ItemsColumns.MONGO_ID, item.getId());
            builder.withValue(ItemsColumns.TITLE, item.getTitle());
            builder.withValue(ItemsColumns.SLUG, item.getSlug());
            builder.withValue(ItemsColumns.DESCRIPTION,item.getDescription());
            builder.withValue(ItemsColumns.PRICE,item.getPrice());
            builder.withValue(ItemsColumns.MODEL,item.getModel());
            builder.withValue(ItemsColumns.BRAND,item.getBrand());
            builder.withValue(ItemsColumns.COLOR,item.getColor());
            builder.withValue(ItemsColumns.SIZE,item.getSize());
           if(null != item.getLocation()) {
                builder.withValue(ItemsColumns.LATITUDE, item.getLocation().latitude);
                builder.withValue(ItemsColumns.LONGITUDE, item.getLocation().longitude);
           }
            builder.withValue(ItemsColumns.THUMBNAIL,item.getThumbnail());
            builder.withValue(ItemsColumns.VOTE_AVERAGE,item.getVoteAverage());
            builder.withValue(ItemsColumns.VOTE_COUNT,item.getVoteCount());

        return builder.build();
    }


    public static boolean isFristTime(Context context) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String started  = prefs.getString(STARTED,null);
        if(started == null){
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(STARTED,STARTED);
            editor.apply();
            return true;
        }
        return false;
    }



    /**
     * Returns true if the network is available or about to become available.
     *
     * @param c Context used to get the ConnectivityManager
     * @return true if the network is available
     */
    static public boolean isNetworkAvailable(Context c) {
        ConnectivityManager cm =
                (ConnectivityManager)c.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
    /**
     *
     * @param c Context used to get the SharedPreferences
     * @return the location status integer type
     */
    @SuppressWarnings("ResourceType")
    static public @WiulgiSyncAdapter.LocationStatus
    int getLocationStatus(Context c){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        return sp.getInt(c.getString(R.string.pref_server_status_key), WiulgiSyncAdapter.STATUS_UNKNOWN);
    }

    /**
     * Resets the location status.  (Sets it to SunshineSyncAdapter.LOCATION_STATUS_UNKNOWN)
     * @param c Context used to get the SharedPreferences
     */
    static public void resetLocationStatus(Context c){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor spe = sp.edit();
        spe.putInt(c.getString(R.string.pref_server_status_key), WiulgiSyncAdapter.STATUS_UNKNOWN);
        spe.apply();
    }

}
