package com.cleanarchitecture.sl.event.ui;

import android.os.Bundle;

import com.cleanarchitecture.sl.event.AbsEvent;

/**
 * Событие - диалог завершен, с указанным результатом
 */
public class DialogResultEvent extends AbsEvent {

    private Bundle mResult;

    public DialogResultEvent(final Bundle result) {
        mResult = result;
    }

    public DialogResultEvent(final Bundle result, final int id) {
        super(id);

        mResult = result;

    }

    public Bundle getResult() {
        return mResult;
    }

}
