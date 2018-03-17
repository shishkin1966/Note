package com.cleanarchitecture.sl.event.ui;

import android.widget.EditText;

import com.cleanarchitecture.sl.event.AbsEvent;

/**
 * Событие - выполнить команду "показать клавиатуру"
 */
public class ShowKeyboardEvent extends AbsEvent {

    private EditText mEditText;

    public ShowKeyboardEvent(final EditText view) {
        mEditText = view;
    }

    public EditText getView() {
        return mEditText;
    }
}
