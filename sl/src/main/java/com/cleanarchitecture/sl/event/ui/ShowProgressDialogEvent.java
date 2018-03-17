package com.cleanarchitecture.sl.event.ui;


import com.cleanarchitecture.sl.event.AbsEvent;

/**
 * Событие - выполнить команду "показать Progress Dialog"
 */
public class ShowProgressDialogEvent extends AbsEvent {

    private String mTitle;

    public ShowProgressDialogEvent(final String title) {
        mTitle = title;
    }

    public ShowProgressDialogEvent() {
    }

    public String getTitle() {
        return mTitle;
    }

}
