package com.cleanarchitecture.sl.event.navigation;

import android.content.Intent;

import com.cleanarchitecture.sl.event.AbsEvent;

/**
 * Событие - выполнить команду "start activity"
 */
public class StartActivityEvent extends AbsEvent {

    private Intent mIntent;

    public <F> StartActivityEvent(final Intent intent) {
        mIntent = intent;
    }

    public Intent getIntent() {
        return mIntent;
    }
}
