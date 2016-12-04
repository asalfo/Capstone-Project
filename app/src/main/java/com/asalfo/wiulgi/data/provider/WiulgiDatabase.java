package com.asalfo.wiulgi.data.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class WiulgiDatabase  extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "wiulgi.db";
    private static final int DATABASE_VERSION = 1;


    public WiulgiDatabase(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + WiulgiProvider.Tables.ITEMS + " ("
                + ItemsContract.ItemsColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ItemsContract.ItemsColumns.MONGO_ID + " TEXT,"
                + ItemsContract.ItemsColumns.TITLE + " TEXT NOT NULL,"
                + ItemsContract.ItemsColumns.SLUG + " TEXT NOT NULL,"
                + ItemsContract.ItemsColumns.DESCRIPTION + " TEXT NOT NULL,"
                + ItemsContract.ItemsColumns.COLOR + " TEXT ,"
                + ItemsContract.ItemsColumns.BRAND + " TEXT ,"
                + ItemsContract.ItemsColumns.MODEL + " TEXT,"
                + ItemsContract.ItemsColumns.SIZE + " INTEGER,"
                + ItemsContract.ItemsColumns.THUMBNAIL+ " TEXT NOT NULL, "
                + ItemsContract.ItemsColumns.LATITUDE + " TEXT NOT NULL, "
                + ItemsContract.ItemsColumns.LONGITUDE + " TEXT NOT NULL, "
                + ItemsContract.ItemsColumns.VOTE_AVERAGE + " FLOAT NOT NULL DEFAULT 0.0 ,"
                + ItemsContract.ItemsColumns.VOTE_COUNT + " INTEGER NOT NULL DEFAULT 0 ,"
                + ItemsContract.ItemsColumns.PRICE + " FLOAT NOT NULL DEFAULT 0.0 "
                + ")" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + WiulgiProvider.Tables.ITEMS);
        onCreate(db);
    }
}
