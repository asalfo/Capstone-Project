package com.asalfo.wiulgi.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.asalfo.wiulgi.util.Constants;


public class WidgetProvider extends AppWidgetProvider {

    public static void updateAppWidget(@NonNull Context context, AppWidgetManager appWidgetManager, int appWidgetId, String symbol) {
        Intent intent = new  Intent(context, WidgetIntentService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
        context.startService(intent);
    }

    @Override
    public void onUpdate(@NonNull Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        context.startService(new Intent(context, WidgetIntentService.class));
    }

    @Override
    public void onAppWidgetOptionsChanged(@NonNull Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        context.startService(new Intent(context, WidgetIntentService.class));
    }

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        super.onReceive(context, intent);
        if (Constants.ACTION_DATA_UPDATED.equals(intent.getAction())) {
            context.startService(new Intent(context, WidgetIntentService.class));
        }
    }
}
