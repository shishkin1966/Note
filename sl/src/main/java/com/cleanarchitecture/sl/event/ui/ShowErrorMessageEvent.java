package com.cleanarchitecture.sl.event.ui;

import com.cleanarchitecture.sl.data.ExtError;
import com.cleanarchitecture.sl.data.Result;
import com.cleanarchitecture.sl.event.AbsEvent;
import com.cleanarchitecture.sl.event.Event;

/**
 * Событие - выполнить команду "показать сообщение об ошибке"
 */
public class ShowErrorMessageEvent extends AbsEvent {
    private ExtError mError;
    private String mMessage;

    public ShowErrorMessageEvent(final Event event) {
        mError = getError();
    }

    public ShowErrorMessageEvent(final Result result) {
        mError = result.getError();
    }

    public ShowErrorMessageEvent(final String message) {
        mMessage = message;
    }

    public String getErrorText() {
        if (mError != null) {
            return mError.getErrorText();
        }
        return mMessage;
    }

    public ExtError getError() {
        return mError;
    }

}
