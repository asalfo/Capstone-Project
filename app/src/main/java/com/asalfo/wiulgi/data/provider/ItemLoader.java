package com.asalfo.wiulgi.data.provider;


import android.content.Context;

import android.net.Uri;
import android.support.v4.content.CursorLoader;

public class ItemLoader  extends CursorLoader {

    public static ItemLoader newAllItemsInstance(Context context) {
        return new ItemLoader(context, ItemsContract.Items.buildDirUri());
    }

    public static ItemLoader newInstanceForItemId(Context context, long itemId) {
        return new ItemLoader(context, ItemsContract.Items.buildItemUri(itemId));
    }

    private ItemLoader(Context context, Uri uri) {
        super(context, uri, Query.PROJECTION, null, null, ItemsContract.Items.DEFAULT_SORT);
    }



    public interface Query {
        String[] PROJECTION = {
               ItemsContract.Items._ID,
               ItemsContract.Items.TITLE,
               ItemsContract.Items.DESCRIPTION,
               ItemsContract.Items.BRAND,
               ItemsContract.Items.MODEL,
               ItemsContract.Items.PRICE,
               ItemsContract.Items.THUMBNAIL,
               ItemsContract.Items.LONGITUDE,
               ItemsContract.Items.LATITUDE,
               ItemsContract.Items.VOTE_COUNT,
               ItemsContract.Items.VOTE_AVERAGE
        };

        int _ID = 0;
        int TITLE = 1;
        int DESCRIPTION = 2;
        int BRAND = 3;
        int MODEL = 4;
        int PRICE = 5;
        int THUMBNAIL = 6;
        int LONGITUDE = 7;
        int LATITUDE = 8;
        int VOTE_COUNT = 9;
        int VOTE_AVERAGE = 10;
    }


}
