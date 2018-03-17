package com.cleanarchitecture.sl.event.ui;

import com.cleanarchitecture.sl.event.AbsEvent;

/**
 * Событие - нажата кнопка на SnackBar
 */
public class OnSnackBarClickEvent extends AbsEvent {
    private String mActionText;

    public OnSnackBarClickEvent(final String text) {
        mActionText = text;
    }

    public String getText() {
        return mActionText;
    }
}
