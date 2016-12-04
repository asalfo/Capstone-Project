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

import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.asalfo.wiulgi.data.model.Item;
import com.asalfo.wiulgi.data.provider.ItemsContract;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import com.asalfo.wiulgi.data.provider.ItemsContract.ItemsColumns;


public class Utils {
    private static final String TAG = Utils.class.getSimpleName();


    private static final String STARTED = "started";




    public static ArrayList itemListToContentVals(ArrayList<ContentProviderOperation> batchOperations,ArrayList<Item> items){

            if (items.size() > 0){
                for (Item item : items){
                    batchOperations.add(buildBatchOperation(item));
                }
            }
        return batchOperations;
    }

    private static ContentProviderOperation buildBatchOperation(Item item){

        Uri dirUri = ItemsContract.Items.buildDirUri();
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

}
