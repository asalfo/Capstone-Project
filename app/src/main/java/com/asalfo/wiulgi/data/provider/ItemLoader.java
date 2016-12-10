package com.asalfo.wiulgi.data.provider;


import android.content.Context;

import android.net.Uri;
import android.support.v4.content.CursorLoader;

public class ItemLoader  extends CursorLoader {

    public static ItemLoader newAllItemsInstance(Context context) {
        return new ItemLoader(context, WiulgiContract.Items.buildDirUri());
    }

    public static ItemLoader newInstanceForItemId(Context context, long itemId) {
        return new ItemLoader(context, WiulgiContract.Items.buildItemUri(itemId));
    }

    private ItemLoader(Context context, Uri uri) {
        super(context, uri, Query.PROJECTION, null, null, WiulgiContract.Items.DEFAULT_SORT);
    }



    public interface Query {
        String[] PROJECTION = {
               WiulgiContract.Items._ID,
               WiulgiContract.Items.TITLE,
               WiulgiContract.Items.DESCRIPTION,
               WiulgiContract.Items.BRAND,
               WiulgiContract.Items.MODEL,
               WiulgiContract.Items.PRICE,
               WiulgiContract.Items.THUMBNAIL,
               WiulgiContract.Items.LONGITUDE,
               WiulgiContract.Items.LATITUDE,
               WiulgiContract.Items.VOTE_COUNT,
               WiulgiContract.Items.VOTE_AVERAGE,
               WiulgiContract.Items.SIZE,
               WiulgiContract.Items.COLOR
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
        int SIZE = 11;
        int COLOR = 12;
    }


}
