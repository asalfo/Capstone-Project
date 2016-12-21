package com.asalfo.wiulgi.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.asalfo.wiulgi.MainActivity;
import com.asalfo.wiulgi.R;
import com.asalfo.wiulgi.auth.ProfileManager;
import com.asalfo.wiulgi.auth.User;
import com.asalfo.wiulgi.data.model.Item;
import com.asalfo.wiulgi.data.model.WiugliCollection;
import com.asalfo.wiulgi.data.provider.WiulgiContract;
import com.asalfo.wiulgi.http.ApiInterface;
import com.asalfo.wiulgi.http.ApiServiceGenerator;
import com.asalfo.wiulgi.util.Constants;
import com.asalfo.wiulgi.util.Settings;
import com.asalfo.wiulgi.util.Utils;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;


public class RecommendationTaskService extends GcmTaskService {


    private final String LOG_TAG = RecommendationTaskService.class.getSimpleName();
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
    private static final int ITEM_NOTIFICATION_ID = 3004;

    private Context mContext;



    public RecommendationTaskService() {
    }

    public RecommendationTaskService(Context context) {
        mContext = context;
    }


    @Override
    public int onRunTask(@NonNull TaskParams params) {

        if(!ProfileManager.getInstance().isLoggedIn())
            return GcmNetworkManager.RESULT_SUCCESS;

        if (mContext == null) {
            mContext = this;
        }
        int result = GcmNetworkManager.RESULT_FAILURE;

        if (params.getTag().equals(Constants.INIT) || params.getTag().equals(Constants.PERIODIC_TAG)) {
            String token = "Bearer " + Settings.getInstance().getUserApiToken();

            ApiInterface apiEndPoint =
                    ApiServiceGenerator.createService(ApiInterface.class, token);

            User user = ProfileManager.getInstance().getUser();
            Call<WiugliCollection<Item>> call = apiEndPoint.recommended(user.getUsername());
            try {
                Response<WiugliCollection<Item>> response = call.execute();
                ArrayList<ContentProviderOperation> batchOperations = new ArrayList<>();
                Utils.getItemsContentVals(batchOperations, response.body().getItems(), WiulgiContract.Items.RECOMMENDED, "1");
                mContext.getContentResolver().applyBatch(WiulgiContract.CONTENT_AUTHORITY, batchOperations);
                notifyRecommended();
                result = GcmNetworkManager.RESULT_SUCCESS;

            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (OperationApplicationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }


    private void notifyRecommended() {

        //checking the last update and notify if it' the first of the day
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        String displayNotificationsKey = mContext.getString(R.string.pref_enable_notifications_key);
        boolean displayNotifications = prefs.getBoolean(displayNotificationsKey,
                Boolean.parseBoolean(mContext.getString(R.string.pref_enable_notifications_default)));

        if ( displayNotifications ) {

            String lastNotificationKey = mContext.getString(R.string.pref_last_notification);
            long lastSync = prefs.getLong(lastNotificationKey, 0);


                     Resources resources = mContext.getResources();
                     resources.getDimensionPixelSize(android.R.dimen.notification_large_icon_width);
                     resources.getDimensionPixelSize(android.R.dimen.notification_large_icon_height);


                    String title = mContext.getString(R.string.app_name);

                    // Define the text of the forecast.
                    String contentText = "New Recommendations for you";

                    // NotificationCompatBuilder is a very convenient way to build backward-compatible
                    // notifications.  Just throw in some data.
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(mContext)
                                    .setSmallIcon(R.drawable.cast_ic_notification_small_icon)
                                    .setColor(resources.getColor(R.color.colorPrimary))
                                    .setContentTitle(title)
                                    .setContentText(contentText);

                    // Make something interesting happen when the user clicks on the notification.
                    // In this case, opening the app is sufficient.
                    Intent resultIntent = new Intent(mContext, MainActivity.class);
                    resultIntent.putExtra(MainActivity.FRAG,MainActivity.RECOMMENDED);

                    // The stack builder object will contain an artificial back stack for the
                    // started Activity.
                    // This ensures that navigating backward from the Activity leads out of
                    // your application to the Home screen.
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
                    stackBuilder.addNextIntent(resultIntent);
                    PendingIntent resultPendingIntent =
                            stackBuilder.getPendingIntent(
                                    0,
                                    PendingIntent.FLAG_UPDATE_CURRENT
                            );
                    mBuilder.setContentIntent(resultPendingIntent);

                    NotificationManager mNotificationManager =
                            (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                    // ITEM_NOTIFICATION_ID allows you to update the notification later on.
                    mNotificationManager.notify(ITEM_NOTIFICATION_ID, mBuilder.build());

                    //refreshing last sync
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putLong(lastNotificationKey, System.currentTimeMillis());
                    editor.apply();

            }
        }
    }


