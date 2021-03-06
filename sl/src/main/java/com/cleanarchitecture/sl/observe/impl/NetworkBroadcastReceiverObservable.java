package com.cleanarchitecture.sl.observe.impl;

import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.cleanarchitecture.sl.observe.AbsBroadcastReceiverObservable;

/**
 * Created by Shishkin on 16.12.2017.
 */

public class NetworkBroadcastReceiverObservable extends AbsBroadcastReceiverObservable {

    public static final String NAME = NetworkBroadcastReceiverObservable.class.getName();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public IntentFilter getIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        //intentFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        return intentFilter;
    }
}
