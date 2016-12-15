package com.asalfo.wiulgi.data.provider;


import android.net.Uri;

public class WiulgiContract {

    public static final String CONTENT_AUTHORITY = "com.asalfo.wiugli";
    public static final Uri BASE_URI = Uri.parse("content://com.asalfo.wiugli");


    public interface ItemsColumns {
        /** Type: INTEGER PRIMARY KEY AUTOINCREMENT */
        String _ID = "_id";
        /** Type: TEXT NOT NULL */
        String MONGO_ID = "mongo_id";
        /** Type: TEXT NOT NULL */
        String TITLE = "title";
        /** Type: TEXT NOT NULL */
        String SLUG = "slug";
        /** Type: TEXT NOT NULL */
        String DESCRIPTION = "description";

        String COLOR = "color";

        String BRAND = "brand";

        String MODEL = "model";

        String SIZE = "size";

        String PRICE = "price";
        /** Type: TEXT NOT NULL */
        String THUMBNAIL = "thumbnail";
        /** Type: TEXT NOT NULL */
        String LATITUDE = "latitude";
        /** Type: TEXT NOT NULL */
        String LONGITUDE = "longitude";
        /** Type: TEXT NOT NULL */
        String VOTE_COUNT = "vote_count";
        /** Type: TEXT NOT NULL */
        String VOTE_AVERAGE = "vote_average";

        String FAVORITED = "favorited";

        String RECOMMENDED = "vote_average";
    }

    public static class Items implements ItemsColumns {

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.com.asalfo.wiulgi.items";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.com.asalfo.wiulgi.items";

        public static final String DEFAULT_SORT = VOTE_COUNT + " DESC";


        /** Matches: /items/ */
        public static Uri buildDirUri() {
            return BASE_URI.buildUpon().appendPath("items").build();
        }

        /** Matches: /items/[_id]/ */
        public static Uri buildItemUri(long _id) {
            return BASE_URI.buildUpon().appendPath("items").appendPath(Long.toString(_id)).build();
        }

        /** Read item ID item detail URI. */
        public static long getItemId(Uri itemUri) {
            return Long.parseLong(itemUri.getPathSegments().get(1));
        }

    }

    private WiulgiContract() {
    }
}
