package com.cleanarchitecture.sl.event.navigation;

import android.content.Intent;

import com.cleanarchitecture.sl.event.AbsEvent;

/**
 * Событие - выполнить команду "start choose activity"
 */
public class StartChooseActivityEvent extends AbsEvent {

    private Intent mIntent;
    private String mTitle;

    public <F> StartChooseActivityEvent(final Intent intent, final String title) {
        mIntent = intent;
        mTitle = title;
    }

    public Intent getIntent() {
        return mIntent;
    }

    public String getTitle() {
        return mTitle;
    }
}
