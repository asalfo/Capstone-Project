package com.asalfo.wiulgi.data.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class WiulgiDatabase  extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "wiulgi.db";
    private static final int DATABASE_VERSION = 2;


    public WiulgiDatabase(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + WiulgiProvider.Tables.ITEMS + " ("
                + WiulgiContract.ItemsColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + WiulgiContract.ItemsColumns.MONGO_ID + " TEXT,"
                + WiulgiContract.ItemsColumns.TITLE + " TEXT NOT NULL,"
                + WiulgiContract.ItemsColumns.SLUG + " TEXT NOT NULL,"
                + WiulgiContract.ItemsColumns.DESCRIPTION + " TEXT NOT NULL,"
                + WiulgiContract.ItemsColumns.COLOR + " TEXT ,"
                + WiulgiContract.ItemsColumns.BRAND + " TEXT ,"
                + WiulgiContract.ItemsColumns.MODEL + " TEXT,"
                + WiulgiContract.ItemsColumns.SIZE + " INTEGER,"
                + WiulgiContract.ItemsColumns.THUMBNAIL+ " TEXT NOT NULL, "
                + WiulgiContract.ItemsColumns.LATITUDE + " TEXT NOT NULL, "
                + WiulgiContract.ItemsColumns.LONGITUDE + " TEXT NOT NULL, "
                + WiulgiContract.ItemsColumns.VOTE_AVERAGE + " FLOAT NOT NULL DEFAULT 0.0 ,"
                + WiulgiContract.ItemsColumns.VOTE_COUNT + " INTEGER NOT NULL DEFAULT 0 ,"
                + WiulgiContract.ItemsColumns.PRICE + " FLOAT NOT NULL DEFAULT 0.0 ,"
                + WiulgiContract.ItemsColumns.FAVORITED + " INT  DEFAULT 0 ,"
                + WiulgiContract.ItemsColumns.RECOMMENDED + " INT DEFAULT 0 ,"
                + WiulgiContract.ItemsColumns.WISHED + " INT DEFAULT 0 ," +
                " UNIQUE("+ WiulgiContract.ItemsColumns.MONGO_ID +"))" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + WiulgiProvider.Tables.ITEMS);
        onCreate(db);
    }
}
