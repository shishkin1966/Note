package com.cleanarchitecture.sl.event.ui;

import android.widget.Toast;

import com.cleanarchitecture.common.ui.BaseSnackbar;
import com.cleanarchitecture.sl.event.AbsEvent;

/**
 * Событие - выполнить команду "показать Toast"
 */
public class ShowToastEvent extends AbsEvent {
    private String mMessage;
    private int mDuration = Toast.LENGTH_SHORT;
    private int mType = BaseSnackbar.MESSAGE_TYPE_INFO;

    public ShowToastEvent(final String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }

    public int getDuration() {
        return mDuration;
    }

    public ShowToastEvent setDuration(int duration) {
        this.mDuration = duration;
        return this;
    }

    public int getType() {
        return mType;
    }

    public ShowToastEvent setType(int type) {
        this.mType = type;
        return this;
    }

}
