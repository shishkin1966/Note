package com.cleanarchitecture.sl.observe.impl;

import android.database.ContentObserver;
import android.os.Handler;

import com.cleanarchitecture.sl.observe.IObservable;

/**
 * Created by Shishkin on 15.12.2017.
 */

public class BaseContentObserver extends ContentObserver {

    private final IObservable mObservable;

    public BaseContentObserver(IObservable observable) {
        super(new Handler());

        mObservable = observable;
    }

    @Override
    public void onChange(final boolean selfChange) {
        super.onChange(selfChange);

        mObservable.onChange(selfChange);
    }
}
