package com.cleanarchitecture.sl.observe.impl;

import android.content.Intent;
import android.content.IntentFilter;

import com.cleanarchitecture.sl.observe.AbsBroadcastReceiverObservable;

/**
 * Created by Shishkin on 16.12.2017.
 */

public class ScreenBroadcastReceiverObservable extends AbsBroadcastReceiverObservable {

    public static final String NAME = ScreenBroadcastReceiverObservable.class.getName();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public IntentFilter getIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        return intentFilter;
    }
}
