package com.cleanarchitecture.sl.event.ui;

import com.cleanarchitecture.sl.R;
import com.cleanarchitecture.sl.event.AbsEvent;
import com.cleanarchitecture.sl.ui.dialog.MaterialDialogExt;

/**
 * Событие - выполнить команду "показать диалог"
 */
public class ShowDialogEvent extends AbsEvent {

    private int mId;
    private String mTitle;
    private String mMessage;
    private int mButtonPositive = R.string.ok_upper;
    private int mButtonNegative = MaterialDialogExt.NO_BUTTON;
    private boolean mCancelable = false;
    private String mListener;

    public ShowDialogEvent(final int id, final String listener) {
        mId = id;
        mListener = listener;
    }

    public ShowDialogEvent(final int id, final String listener, final String title, final String message) {
        this(id, listener);
        mTitle = title;
        mMessage = message;
    }

    public ShowDialogEvent(final int id, final String listener, final String title, final String message, final int button_positive) {
        this(id, listener, title, message);

        mButtonPositive = button_positive;
    }

    public ShowDialogEvent(final int id, final String listener, final String title, final String message, final int button_positive, final int button_negative) {
        this(id, listener, title, message, button_positive);

        mButtonNegative = button_negative;
    }

    public ShowDialogEvent(final int id, final String listener, final String title, final String message, final int button_positive, final int button_negative, final boolean cancelable) {
        this(id, listener, title, message, button_positive, button_negative);

        mCancelable = cancelable;
    }

    public String getMessage() {
        return mMessage;
    }

    public int getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getButtonPositive() {
        return mButtonPositive;
    }

    public int getButtonNegative() {
        return mButtonNegative;
    }

    public boolean isCancelable() {
        return mCancelable;
    }

    public String getListener() {
        return mListener;
    }

    public ShowDialogEvent setPositiveButton(int button) {
        mButtonPositive = button;
        return this;
    }

    public ShowDialogEvent setNegativeButton(int button) {
        mButtonNegative = button;
        return this;
    }

    public ShowDialogEvent setCancelable(boolean cancelable) {
        mCancelable = cancelable;
        return this;
    }

    public ShowDialogEvent setMessage(String message) {
        mMessage = message;
        return this;
    }

    public ShowDialogEvent setTitle(String title) {
        mTitle = title;
        return this;
    }
}
