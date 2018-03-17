package com.cleanarchitecture.sl.event.navigation;

import android.content.Intent;

import com.cleanarchitecture.sl.event.AbsEvent;

/**
 * Событие - выполнить команду "start activity for result"
 */
public class StartActivityForResultEvent extends AbsEvent {

    private Intent mIntent;
    private int mRequestCode;

    public <F> StartActivityForResultEvent(final Intent intent, final int requestCode) {
        mIntent = intent;
        mRequestCode = requestCode;
    }

    public Intent getIntent() {
        return mIntent;
    }

    public int getRequestCode() {
        return mRequestCode;
    }
}
