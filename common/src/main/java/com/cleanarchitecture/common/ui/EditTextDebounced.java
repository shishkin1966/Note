package com.cleanarchitecture.common.ui;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;


import java.util.concurrent.TimeUnit;

/**
 * Created by Shishkin on 28.12.2017.
 */

public class EditTextDebounced implements TextWatcher, Runnable {

    private Handler mHandler = new Handler();
    private long mDelay = TimeUnit.MILLISECONDS.toMillis(500);
    private EditText mEditText;
    private EditTextListener mListener;

    public EditTextDebounced(final EditText editText, final EditTextListener listener) {
        mEditText = editText;
        mEditText.addTextChangedListener(this);
        mListener = listener;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        mHandler.removeCallbacks(this);
        mHandler.postDelayed(this, mDelay);
    }

    @Override
    public void run() {
        if (mListener != null) {
            mListener.afterTextChanged(mEditText);
        }
    }

    private void finish() {
        if (mHandler != null) {
            mHandler.removeCallbacks(this);
        }
    }

}
