package com.cleanarchitecture.sl.event.ui;

import android.widget.ImageView;

import com.cleanarchitecture.sl.event.AbsEvent;
import com.cleanarchitecture.sl.request.Rank;


import java.lang.ref.WeakReference;


public class ImageEvent extends AbsEvent {
    private int mRank = Rank.MIN_RANK;
    private String mUrl;
    private WeakReference<ImageView> mView = null;
    private boolean mWithCache = true;
    private int mPlaceholder = -1;
    private int mErrorHolder = -1;

    public ImageEvent(final ImageView view, final String url) {
        mUrl = url;
        mView = new WeakReference<>(view);
    }

    public int getRank() {
        return mRank;
    }

    public String getUrl() {
        return mUrl;
    }

    public WeakReference<ImageView> getView() {
        return mView;
    }

    public boolean isWithCache() {
        return mWithCache;
    }

    public int getPlaceHolder() {
        return mPlaceholder;
    }

    public int getErrorHolder() {
        return mErrorHolder;
    }

    public ImageEvent setRank(int rank) {
        mRank = rank;
        return this;
    }

    public ImageEvent setWithCache(boolean withCache) {
        mWithCache = withCache;
        return this;
    }

    public ImageEvent setPlaceholder(int placeholder) {
        mPlaceholder = placeholder;
        return this;
    }

    public ImageEvent setErrorHolder(int errorHolder) {
        mErrorHolder = errorHolder;
        return this;
    }

}
