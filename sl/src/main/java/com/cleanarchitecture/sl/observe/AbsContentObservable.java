package com.cleanarchitecture.sl.observe;

import android.content.Context;
import android.net.Uri;

import com.cleanarchitecture.sl.observe.impl.BaseContentObserver;
import com.cleanarchitecture.sl.sl.SLUtil;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shishkin on 16.12.2017.
 */

public abstract class AbsContentObservable extends AbsObservable<Boolean> {

    private BaseContentObserver mObserver = new BaseContentObserver(this);
    private List<Uri> mUris = new ArrayList<>();

    public AbsContentObservable(final Uri uri) {
        if (uri != null) {
            mUris.add(uri);
        }
    }

    public AbsContentObservable(final List<Uri> uris) {
        if (uris != null) {
            for (Uri uri : uris) {
                if (uri != null) {
                    mUris.add(uri);
                }
            }
        }
    }

    @Override
    public void register() {
        final Context context = SLUtil.getContext();
        if (context != null) {
            for (Uri uri : mUris) {
                context.getContentResolver().registerContentObserver(uri, true, mObserver);
            }
        }
    }

    @Override
    public void unregister() {
        final Context context = SLUtil.getContext();
        if (context != null) {
            context.getContentResolver().unregisterContentObserver(mObserver);
        }
    }
}
