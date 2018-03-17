package com.cleanarchitecture.sl.data;

import android.support.annotation.Nullable;

public class Result<T> implements IResult {

    public static final int NOT_SEND = -1;
    public static final int LAST = -2;

    private T mData = null;
    private ExtError mExtError = null;
    private int mOrder = NOT_SEND;
    private String mName;
    private int mId = 0;

    public Result() {
    }

    public Result(T data) {
        mData = data;
    }

    public T getData() {
        return mData;
    }

    public Result setData(final T data) {
        mData = data;
        return this;
    }

    public ExtError getError() {
        return mExtError;
    }

    public Result setError(final ExtError extError) {
        mExtError = extError;
        return this;
    }

    public Result setError(final String sender, final String error) {
        if (mExtError == null) {
            mExtError = new ExtError();
        }
        mExtError.addError(sender, error);
        return this;
    }

    public Result setError(final String sender, final int errorCode) {
        if (mExtError == null) {
            mExtError = new ExtError();
        }
        mExtError.addError(sender, errorCode);
        return this;
    }

    public Result setError(final String sender, final Exception e) {
        if (mExtError == null) {
            mExtError = new ExtError();
        }
        mExtError.addError(sender, e);
        return this;
    }

    public Result setError(final String sender, final Throwable t) {
        if (mExtError == null) {
            mExtError = new ExtError();
        }
        mExtError.addError(sender, t.getMessage());
        return this;
    }

    @Nullable
    public String getErrorText() {
        if (mExtError != null) {
            return mExtError.getErrorText();
        }
        return null;
    }

    @Nullable
    public String getSender() {
        if (mExtError != null) {
            return mExtError.getSender();
        }
        return null;
    }

    @Override
    public boolean validate() {
        if (mExtError == null) {
            return true;
        }
        return !mExtError.hasError();
    }

    @Override
    public boolean isEmpty() {
        return (mData == null);
    }

    public int getOrder() {
        return mOrder;
    }

    public Result setOrder(final int order) {
        mOrder = order;
        return this;
    }

    @Override
    public boolean hasError() {
        if (mExtError != null) {
            return mExtError.hasError();
        }
        return false;
    }

    public String getName() {
        return mName;
    }

    public Result setName(final String name) {
        mName = name;
        return this;
    }

    public int getId() {
        return mId;
    }

    public Result setId(int id) {
        mId = id;
        return this;
    }

}

