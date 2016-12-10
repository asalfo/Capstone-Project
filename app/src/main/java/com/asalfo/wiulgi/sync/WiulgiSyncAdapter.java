package com.asalfo.wiulgi.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;
import android.util.Log;

import com.asalfo.wiulgi.BuildConfig;
import com.asalfo.wiulgi.R;
import com.asalfo.wiulgi.data.model.Item;
import com.asalfo.wiulgi.data.model.WiugliCollection;
import com.asalfo.wiulgi.data.provider.WiulgiContract;
import com.asalfo.wiulgi.http.ApiInterface;
import com.asalfo.wiulgi.http.ApiServiceGenerator;
import com.asalfo.wiulgi.util.Utils;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class WiulgiSyncAdapter extends AbstractThreadedSyncAdapter {
    public final String LOG_TAG = WiulgiSyncAdapter.class.getSimpleName();
    // Interval at which to sync with the weather, in seconds.
    // 60 seconds (1 minute) * 180 = 3 hours
    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STATUS_OK, STATUS_SERVER_DOWN, STATUS_SERVER_INVALID, STATUS_UNKNOWN, STATUS_INVALID})
    public @interface LocationStatus {}

    public static final int STATUS_OK = 0;
    public static final int STATUS_SERVER_DOWN = 1;
    public static final int STATUS_SERVER_INVALID = 2;
    public static final int STATUS_UNKNOWN = 3;
    public static final int STATUS_INVALID = 4;

    private Context mContext ;

    public WiulgiSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContext = context;
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(LOG_TAG, "Starting sync");

        ApiInterface api = ApiServiceGenerator.createService(ApiInterface.class, null, null);

        String selection = extras.getString(ApiInterface.SELECTION);
        int page = extras.getInt(ApiInterface.PAGE, 1);

        Call<WiugliCollection<Item>> call = api.itemList(selection, page, BuildConfig.WIULGI_API_KEY);

        try {
            Response<WiugliCollection<Item>> response = call.execute();

            switch (response.code()){
                case 200:
                    WiugliCollection<Item> collection = response.body();
                    setLocationStatus(getContext(), STATUS_OK);
                    break;
                case 401:
                    setLocationStatus(getContext(), STATUS_SERVER_INVALID);
                    return;
                case 404:
                    setLocationStatus(getContext(), STATUS_SERVER_INVALID);
                    return;

                case  500:
                    setLocationStatus(getContext(), STATUS_SERVER_DOWN);
                    return;

                case  503:
                    setLocationStatus(getContext(), STATUS_SERVER_DOWN);
                    return;
                default:
                    setLocationStatus(getContext(), STATUS_INVALID);
                    return;
            }

            WiugliCollection<Item> collection = call.execute().body();

            try {
                ArrayList<ContentProviderOperation> batchOperations = new ArrayList<>();
                Uri dirUri = WiulgiContract.Items.buildDirUri();

                // Delete all items
                batchOperations.add(ContentProviderOperation.newDelete(dirUri).build());


                Utils.itemListToContentVals(batchOperations,collection.getItems());
                mContext.getContentResolver().applyBatch(WiulgiContract.CONTENT_AUTHORITY, batchOperations);

            } catch (NumberFormatException e) {
                e.printStackTrace();
                Log.e(LOG_TAG, "Error applying batch insert", e);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (OperationApplicationException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return;
    }




    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = WiulgiContract.CONTENT_AUTHORITY;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                WiulgiContract.CONTENT_AUTHORITY, bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        WiulgiSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, WiulgiContract.CONTENT_AUTHORITY, true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }


    /**
     * Sets the server status into shared preference.  This function should not be called from
     * the UI thread because it uses commit to write to the shared preferences.
     * @param c Context to get the PreferenceManager from.
     * @param locationStatus The IntDef value to set
     */
    static private void setLocationStatus(Context c, @LocationStatus int locationStatus){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor spe = sp.edit();
        spe.putInt(c.getString(R.string.pref_server_status_key), locationStatus);
        spe.commit();
    }
}