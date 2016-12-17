package com.asalfo.wiulgi.service;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import com.asalfo.wiulgi.ItemDetailActivity;
import com.asalfo.wiulgi.data.provider.WiulgiContract;

/**
 * Created by asalfo on 16/12/2016.
 */

public class DatabaseUpdateTask  extends AsyncTask<String, Integer, Boolean> {

    Context mContext;
    ContentValues mUpdateValues;
    AsyncCallback mDelegate = null;


    public DatabaseUpdateTask( Context context, ContentValues updateValues, AsyncCallback delegate) {
        mUpdateValues = updateValues;
        mDelegate = delegate;
        mContext = context;
    }

    protected Boolean doInBackground(String... ids) {
        Boolean result = true;


        String id = ids[0];
        if(id != null){
            String mSelectionClause = WiulgiContract.Items._ID +  "= ?";
            String[] mSelectionArgs = {id};

            int rowsUpdated = mContext.getContentResolver().update(
                    WiulgiContract.Items.buildDirUri(),
                    mUpdateValues,                       // the columns to update
                    mSelectionClause ,                   // the column to select on
                    mSelectionArgs                      // the value to compare to
            );

            result = rowsUpdated > 0;
        }

        return result;
    }

    protected void onProgressUpdate(Integer... progress) {

    }

    protected void onPostExecute(Boolean result) {
        if(result)
            mDelegate.onCallback();
    }


    public interface AsyncCallback {
        void onCallback();
    }
}
