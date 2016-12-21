package com.asalfo.wiulgi.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class WiulgiSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    @Nullable
    private  WiulgiSyncAdapter sWiulgiSyncAdapter = null;

    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock) {
            if (sWiulgiSyncAdapter == null) {
                sWiulgiSyncAdapter = new WiulgiSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sWiulgiSyncAdapter.getSyncAdapterBinder();
    }
}