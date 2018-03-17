package com.cleanarchitecture.sl.event.ui;

import android.support.design.widget.Snackbar;

import com.cleanarchitecture.common.ui.BaseSnackbar;
import com.cleanarchitecture.sl.event.AbsEvent;


/**
 * Событие - выполнить команду "показать текстовое сообщение"
 */
public class ShowMessageEvent extends AbsEvent {
    private String mMessage;

    private String mAction;
    private int mDuration = Snackbar.LENGTH_LONG;
    private int mType = BaseSnackbar.MESSAGE_TYPE_INFO;

    public ShowMessageEvent(final String message) {
        mMessage = message;
    }

    public ShowMessageEvent(final String message, final int type) {
        mMessage = message;
        mType = type;
    }

    public String getMessage() {
        return mMessage;
    }

    public String getAction() {
        return mAction;
    }

    public ShowMessageEvent setAction(String action) {
        this.mAction = action;
        return this;
    }

    public int getDuration() {
        return mDuration;
    }

    public ShowMessageEvent setDuration(int duration) {
        this.mDuration = duration;
        return this;
    }

    public int getType() {
        return mType;
    }

    public ShowMessageEvent setType(int type) {
        this.mType = type;
        return this;
    }

}
