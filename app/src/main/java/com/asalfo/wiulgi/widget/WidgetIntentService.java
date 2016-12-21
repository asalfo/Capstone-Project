package com.asalfo.wiulgi.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.RemoteViews;

import com.asalfo.wiulgi.ItemDetailActivity;
import com.asalfo.wiulgi.R;
import com.asalfo.wiulgi.data.provider.WiulgiContract;
import com.squareup.picasso.Picasso;

import java.io.IOException;


public class WidgetIntentService extends IntentService {

    public WidgetIntentService() {
        super("WidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {


        // Retrieve all of the Today widget ids: these are the widgets we need to update
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                WidgetProvider.class));

        // Get Item data from the ContentProvider
        Cursor data = getContentResolver().query(WiulgiContract.Items.buildDirUri(),
                new String[]{WiulgiContract.Items._ID, WiulgiContract.Items.THUMBNAIL,
                        WiulgiContract.Items.TITLE, WiulgiContract.Items.PRICE},
                null,
                null,
                WiulgiContract.Items.VOTE_AVERAGE+" DESC");
        if (data == null) {
            return;
        }
        if (!data.moveToFirst()) {
            data.close();
            return;
        }

        // Extract the quote data from the Cursor

        long id = data.getLong(data.getColumnIndex(WiulgiContract.Items._ID));
        String thumbnail = data.getString(data.getColumnIndex(WiulgiContract.Items.THUMBNAIL));
        String title = data.getString(data.getColumnIndex(WiulgiContract.Items.TITLE));
        String price = data.getString(data.getColumnIndex(WiulgiContract.Items.PRICE));
        data.close();

        // Perform this loop procedure for each Today widget
        for (int appWidgetId : appWidgetIds) {

            int layoutId = R.layout.widget;
            final RemoteViews views = new RemoteViews(getPackageName(), layoutId);

            views.setTextViewText(R.id.title, title);
            views.setTextViewText(R.id.price,
                    String.format(getBaseContext().getString(R.string.currency), price));

            try {
                Bitmap bitmap = Picasso.with(getBaseContext())
                        .load(thumbnail)
                        .get();
                views.setImageViewBitmap(R.id.widget_icon, bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }


            Uri contentUri = WiulgiContract.Items.buildItemUri(id);
            Intent launchIntent = new Intent(this, ItemDetailActivity.class)
                    .setData(contentUri);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

    }

}
