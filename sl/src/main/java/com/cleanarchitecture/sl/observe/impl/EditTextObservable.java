package com.cleanarchitecture.sl.observe.impl;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.cleanarchitecture.sl.observe.AbsObservable;


import java.util.concurrent.TimeUnit;

/**
 * Created by Shishkin on 28.12.2017.
 */

public class EditTextObservable extends AbsObservable<String> implements TextWatcher, Runnable {

    private static final String NAME = EditTextObservable.class.getName();

    private Handler mHandler = new Handler();
    private long mDelay = TimeUnit.SECONDS.toMillis(1);
    private EditText mEditText;

    public EditTextObservable(final EditText editText) {
        mEditText = editText;
        mEditText.addTextChangedListener(this);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void register() {
    }

    @Override
    public void unregister() {
        finish();
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
        onChange(mEditText.getText().toString());
    }

    private void finish() {
        if (mHandler != null) {
            mHandler.removeCallbacks(this);
        }
    }

}
